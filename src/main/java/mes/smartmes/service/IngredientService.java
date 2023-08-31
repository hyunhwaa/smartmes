package mes.smartmes.service;

import mes.smartmes.entity.*;
import mes.smartmes.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;



@Service
@Transactional
public class IngredientService {

    @PersistenceContext
    private EntityManager em;

    // 재고와 ingredientId의 매핑 정보를 관리하기 위한 맵
    private Map<String, String> ingredientStockMapping;

    private IngredientInputRepository ingredientInputRepository;
    private IngredientStockRepository ingredientStockRepository;
    private ProductionPlanRepository productionPlanRepository;


    private PorderRepository porderRepository;

    @Autowired
    private PorderService porderService;

    @Autowired
    public IngredientService(IngredientInputRepository ingredientInputRepository, IngredientStockRepository ingredientStockRepository, ProductionPlanRepository productionPlanRepository, PorderRepository porderRepository) {
        this.ingredientInputRepository = ingredientInputRepository;
        this.ingredientStockRepository = ingredientStockRepository;
        this.productionPlanRepository = productionPlanRepository;
        this.porderRepository = porderRepository;
    }

    // 발주 재료 입고  업데이트 확인
    @Transactional
    public void updatePorderStatusAndInsertIngredient(String porderNo, String planNo) {

        List<Porder> porders = porderRepository.findAll();
        //IngredientStock ingredientStock = new IngredientStock();


        Porder pd = new Porder();
        ProductionPlan pp = productionPlanRepository.findByPlanNo(planNo);
        //pd.setPorderStatus("입고완료");
        //porderRepository.updatePorderStatus(porderNo, "입고완료");
        for(Porder porder : porders) {
            //재고 넘버링 부여할 스트링 필드
            String selectStockNo = ingredientStockRepository.selectMaxStockNo();
            // 자재 입고 테이블
            IngredientInput ingredientInput = new IngredientInput();
            // 자재 테이블




            // 지금 보다 이전이면
            System.out.println("일단 제일 첫 번째" + porder.getPorderStatus());

            //이걸 스케줄링으로변경
            if (porder != null && porder.getThinkInputDate().isBefore(LocalDateTime.now()) && porder.getPorderStatus().equals("입고대기")) {
                porder.setPorderStatus("입고완료");
                porderRepository.save(porder);
                System.out.println("입고완료" + porder.toString());

                // 자재 입고테이블에 인서트
                String no = generateOutputNumber();
                ingredientInput.setIngredientInId(no);
                ingredientInput.setPorderNo(porder.getPorderNo());              // 발주번호
//            System.out.println("ingredientInput1 = " + ingredientInput);
                if(porder.getIngredientName().equals("양배추")){
                    ingredientInput.setIngredientId("I001");   // 발주 된 제품명
                }else if(porder.getIngredientName().equals("흑마늘")){
                    ingredientInput.setIngredientId("I002");
                }else if(porder.getIngredientName().equals("석류농축액")){
                    ingredientInput.setIngredientId("I003");
                }else if(porder.getIngredientName().equals("매실농축액")){
                    ingredientInput.setIngredientId("I004");
                }else if(porder.getIngredientName().equals("콜라겐")){
                    ingredientInput.setIngredientId("I005");
                }else if(porder.getIngredientName().equals("파우치")){
                    ingredientInput.setIngredientId("I006");
                }else if(porder.getIngredientName().equals("스틱파우치")){
                    ingredientInput.setIngredientId("I007");
                }else if(porder.getIngredientName().equals("포장Box")){
                    ingredientInput.setIngredientId("I008");
                }
//            System.out.println("ingredientInput2 = " + ingredientInput);
                ingredientInput.setInputQuantity(porder.getPorderQuantity());    // 발주 수량
//            System.out.println("ingredientInput3 = " + ingredientInput);
                ingredientInput.setInputDate(LocalDateTime.now());
                System.out.println("ingredientInput4 = " + ingredientInput);
                ingredientInputRepository.save(ingredientInput);
                System.out.println(ingredientInput.getIngredientInId());
                System.out.println(ingredientInput.getPorderNo());
                System.out.println("찍어보자 : " + ingredientInput.toString());
                pp.setProdPlanFinYn("발주입고완료");
                productionPlanRepository.save(pp);

                // 현재 재고를 업데이트  // 저장 된 재고를 더하기

                System.out.println("이건 리스트고" + porder.toString());
                System.out.println("이건 뭐고2 " + porder.getIngredientName());

                ingredientStockRepository.increaseStockQuantity(ingredientInput.getIngredientId(), ingredientInput.getInputQuantity());


            }

        }
    }

    private static int sequence = 1;
    public String generateOutputNumber() {
        // 현재 시간 정보를 가져옵니다.
        LocalDateTime now = LocalDateTime.now();
        // 형식 지정을 위한 DateTimeFormatter를 생성합니다.
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        // 현재 시간 정보를 "yyyyMMdd" 형식의 문자열로 변환합니다.
        String formattedDate = now.format(formatter);
        // 시퀀스 값을 문자열로 변환합니다.
        String formattedSequence = String.format("%03d", sequence);

        List<String> no = ingredientInputRepository.findingredientinId();
        for(int i=0;i<no.size();i++){
            if(("II"+formattedDate+formattedSequence).equals(no.get(i))){
                int incrementedValue = Integer.parseInt(formattedSequence) + 1;
                formattedSequence = String.format("%03d", incrementedValue);
            }
        }

        // 시퀀스 값을 1 증가시킵니다.
        sequence++;
        // 생산계획번호를 조합하여 반환합니다.
        return "II" + formattedDate + formattedSequence;
    }

    @Scheduled(cron = "*/20 * * * * ?") // 30초 마다 실행
    public void processPordersAutomatically() {
        LocalDateTime currentDateTime = LocalDateTime.now();
        List<Porder> porders = porderRepository.findByPorderStatusAndPorderDateBefore("입고대기", currentDateTime);
        String planNo = null;
        if (porders != null && !porders.isEmpty()) {
            List<ProductionPlan> plans = productionPlanRepository.findByProdPlanFinYn("발주입고대기중");
            for(ProductionPlan plan : plans){
                planNo = plan.getProdPlanNo();
            }
            for (Porder porder : porders) {
                String porderNo = porder.getPorderNo();
                if(planNo != null){
                    updatePorderStatusAndInsertIngredient(porderNo, planNo);
                }

            }
        }

    }

    public void changePorder(String porderNo){
        Porder po = porderRepository.findByPorderNo(porderNo);
        LocalDateTime time = porderService.selectInputDate(porderNo);
        po.setThinkInputDate(time);
        po.setPorderStatus("입고대기");
        System.out.println("업데이트포더 = "+po);
        porderRepository.save(po);
        porderRepository.flush();
    }

    @Scheduled(cron = "*/8 * * * * ?") // 30초 마다 실행
    public void processPorderChangeAutomatically() {
        LocalDateTime currentDateTime = LocalDateTime.now();
        List<Porder> porders = porderRepository.findByPorderStatus("입고준비");
        if (porders != null && !porders.isEmpty()) {
            for (Porder porder : porders) {
                String porderNo = porder.getPorderNo();
                changePorder(porderNo);
            }
        }

    }



    public List<IngredientInput> selectList() {
        return ingredientInputRepository.findAll();
    }





}




package mes.smartmes.service;

import mes.smartmes.dto.Ratio;
import mes.smartmes.dto.Ratio1;
import mes.smartmes.entity.ProductionPlan;
import mes.smartmes.entity.Routing;
import mes.smartmes.entity.WorkOrder;
import mes.smartmes.repository.ProdPlanRepository;
import mes.smartmes.repository.ProductionPlanRepository;
import mes.smartmes.repository.RoutingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class CheckService {

    @Autowired
    WorkOrderService workOrderService;
    @Autowired
    CheckQuantityService CheckQuantityService;
    @Autowired
    LotService lotService;

    private final ProdPlanRepository prodPlanRepository;
    private final ProductionPlanRepository productionPlanRepository;
    private Ratio ratio;
    private Ratio1 ratio1;
    private final RoutingRepository routingRepository;

    public CheckService(ProdPlanRepository prodPlanRepository,
                        ProductionPlanRepository productionPlanRepository,
                        RoutingRepository routingRepository) {
        this.prodPlanRepository = prodPlanRepository;
        this.productionPlanRepository = productionPlanRepository;

        this.routingRepository = routingRepository;
    }

    public LocalDateTime getAllProductionPlans(String productId, int quantity) {
        System.out.println("겟올 왓음");
        System.out.println("아이디 = " + productId);
        System.out.println("수량 = "+quantity);
        List<ProductionPlan> pp = prodPlanRepository.findProduction();
        System.out.println("pp는 = " + pp);
        LocalDateTime maxFinishDate = null;
        List<String> ppList = new ArrayList<>();
        for (ProductionPlan plan : pp) {
            System.out.println("겟올플랜포문 왓음");
            String planNo = plan.getProdPlanNo();
            ppList.add(planNo);
        }
            //LocalDateTime expectInputDate = plan.getExpectInputDate();
            List<LocalDateTime> wo = processOrder11(ppList);
//            for (LocalDateTime Time : wo) {
//                System.out.println("겟올워크포문 왓음");
//                LocalDateTime finish = Time.get;
//                if (maxFinishDate == null || finish.isAfter(maxFinishDate)) {
//                    maxFinishDate = finish;
//                }
//            }
            System.out.println("겟올더블유오 : "+wo);
            if (!wo.isEmpty()) {
                maxFinishDate = Collections.max(wo);

                System.out.println("최대 시간: " + maxFinishDate);

        }
        System.out.println("겟올플랜포문 나왓음");
        Map<String, Object> resultMap = CheckQuantityService.countTime1(productId, maxFinishDate, quantity);
        Map<String, LocalDateTime> processTimes = (Map<String, LocalDateTime>) resultMap.get("processTimes"); //끝난시간
        LocalDateTime process10time = processTimes.get("process10");   //이게 최종납기일
        System.out.println("최종예상납기일 = "+process10time);

        return  process10time;
    }

    public List<LocalDateTime> processOrder11(List<String> ppList){
        System.out.println("프로세스오더11왓음");
        List<LocalDateTime> processTimesList = new ArrayList<>(); //끝난시간
        LocalDateTime date = LocalDateTime.now();
        for(int i=0; i < ppList.size(); i++){
            String planNo = ppList.get(i);
            System.out.println("생산계획번호 : "+planNo);

            ProductionPlan pp = productionPlanRepository.findByPlanNo(planNo);
            int quantity = pp.getProdPlanQuantity();
            ratio = new Ratio(pp);
            ratio1 = new Ratio1(quantity);
            CheckQuantityService.find(pp);

            String productId = pp.getProductId();
            Routing rt = routingRepository.findByProductId(productId);

            List<String> processNumbers = new ArrayList<>();
            processNumbers.add(rt.getProcessNo1());
            processNumbers.add(rt.getProcessNo2());
            processNumbers.add(rt.getProcessNo3());
            processNumbers.add(rt.getProcessNo4());
            processNumbers.add(rt.getProcessNo5());
            processNumbers.add(rt.getProcessNo6());
            processNumbers.add(rt.getProcessNo7());
            processNumbers.add(rt.getProcessNo8());
            processNumbers.add(rt.getProcessNo9());
            processNumbers.add(rt.getProcessNo10());

            //int quantity = pp.getProdPlanQuantity();

            if(i == 0){
                date = LocalDateTime.now();
            }else{
                LocalDateTime prevEndTime = processTimesList.get(i - 1);
                date = prevEndTime;
            }
            Map<String ,Object> resultMap = CheckQuantityService.countTime1(pp.getProductId(),date,quantity);
            Map<String, LocalDateTime> processTimes = (Map<String, LocalDateTime>) resultMap.get("processTimes"); //끝난시간
            Map<String, LocalDateTime> processStartTimes = (Map<String, LocalDateTime>) resultMap.get("processStartTimes"); //시작시간
            System.out.println("프로세스타임(서비스) - "+processTimes); //끝난시간
            System.out.println("스타트타임 - "+processStartTimes); //시작시간


            LocalDateTime time = processTimes.get("process10");
            processTimesList.add(time);

        }


        return processTimesList;
    }






}

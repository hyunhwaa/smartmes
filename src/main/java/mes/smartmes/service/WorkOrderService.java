package mes.smartmes.service;


import com.querydsl.core.BooleanBuilder;
import mes.smartmes.dto.Ratio;
import mes.smartmes.entity.QWorkOrder;
import mes.smartmes.entity.Routing;
import mes.smartmes.entity.WorkOrder;
import mes.smartmes.repository.ProcessRepository;

import mes.smartmes.entity.ProductionPlan;
import mes.smartmes.repository.ProductionPlanRepository;
import mes.smartmes.repository.RoutingRepository;
import mes.smartmes.repository.WorkOrderRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class WorkOrderService {
    private ProductionPlanRepository productionPlanRepository;
    private RoutingRepository routingRepository;
    private WorkOrderRepository workOrderRepository;
    private ProcessRepository processRepository;

    private Ratio ratio;



    @Autowired
    private ProdPlanService productService;

    @Autowired
    private LotService lotService;





    public WorkOrderService(ProductionPlanRepository productionPlanRepository, RoutingRepository routingRepository,
                            WorkOrderRepository workOrderRepository,ProcessRepository processRepository){
        this.productionPlanRepository = productionPlanRepository;
        this.routingRepository = routingRepository;
        this.workOrderRepository = workOrderRepository;
        this.processRepository = processRepository;

    }

    public void processOrder(String planNo){

        System.out.println("생산계획번호 : "+planNo);

        ProductionPlan pp = productionPlanRepository.findByPlanNo(planNo);
        ratio = new Ratio(pp);
        lotService.find(pp);

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

        List<WorkOrder> work = new ArrayList<>();

        for (int i = 0; i <= processNumbers.size(); i++) {
            String processNo = processNumbers.get(i);
            System.out.println("리스트 사이즈 : "+processNumbers.size());
            System.out.println(i+"번째 여기여기 : "+processNo);
            if (processNo != null && !processNo.isEmpty()) {
                System.out.println(i+"번째 여기여기 : "+processNo);
                WorkOrder workOrder = new WorkOrder();
                String won = generateWorkOrderNumber();
                workOrder.setWorkOrderNo(won);
                // prodplan_seq 할당
                Optional<Integer> maxSeqOptional = Optional.ofNullable(productionPlanRepository.getMaxProdPlanSeqByOrderNo(planNo));
                int maxSeq = maxSeqOptional.orElse(0);
                int nextSeq = maxSeq + 1;
                workOrder.setWorkOrderSeq(nextSeq);
                workOrder.setProdPlanNo(pp.getProdPlanNo());
                workOrder.setProdPlanSeq(pp.getProdPlanSeq());
                workOrder.setProductId(pp.getProductId());
                workOrder.setProcessNo(processNo);
                String equip = processNumbers.get(i);
                String eq = processRepository.findByProcessNo(equip);
                workOrder.setEquipmentId(eq);
                //System.out.println("ordersInput : "+ra);
                if("P001".equals(pp.getProductId())){
                    if("원료계량".equals(eq)){
                        workOrder.setInputQuantity(pp.getProdPlanQuantity());
                        workOrder.setOutputQuantity(pp.getProdPlanQuantity());
                    }else if("전처리".equals(eq)){
                        workOrder.setInputQuantity(pp.getProdPlanQuantity());
                        workOrder.setOutputQuantity(pp.getProdPlanQuantity());
                    }else if("추출".equals(eq)){
                        workOrder.setInputQuantity((int) ratio.getCabbageWater());
                        workOrder.setOutputQuantity((int) ratio.getCabbageWaterOutput());
                    }else if("혼합 및 살균_즙".equals(eq)){
                        workOrder.setInputQuantity((int) ratio.getCabbageWaterOutput());
                        workOrder.setOutputQuantity((int) ratio.getCabbageWaterOutput());
                    }else if("충진_즙".equals(eq)){
                        workOrder.setInputQuantity((int) ratio.getCabbageWaterOutput());
                        workOrder.setOutputQuantity((int) ratio.getCabbageWaterOutput()*1000/80);
                    }else if("검사".equals(eq)){
                        workOrder.setInputQuantity((int) ratio.getCabbageWaterOutput()*1000/80);
                        workOrder.setOutputQuantity((int) ratio.getCabbageWaterOutput()*1000/80);
                    }else if("식힘".equals(eq)){
                        workOrder.setInputQuantity((int) ratio.getCabbageWaterOutput()*1000/80);
                        workOrder.setOutputQuantity((int) ratio.getCabbageWaterOutput()*1000/80);
                    }else if("포장".equals(eq)){
                        workOrder.setInputQuantity((int) ratio.getCabbageWaterOutput()*1000/80);
                        workOrder.setOutputQuantity((int) ratio.getCabbageWaterOutput()*1000/80/30);
                    }
                }else if("P002".equals(pp.getProductId())){
                    if("원료계량".equals(eq)){
                        workOrder.setInputQuantity(pp.getProdPlanQuantity());
                        workOrder.setOutputQuantity(pp.getProdPlanQuantity());
                    }else if("전처리".equals(eq)){
                        workOrder.setInputQuantity(pp.getProdPlanQuantity());
                        workOrder.setOutputQuantity(pp.getProdPlanQuantity());
                    }else if("추출".equals(eq)){
                        System.out.println("in-추출");
                        System.out.println("추출투입개수 : "+ratio.getBlackGarlicWater());
                        workOrder.setInputQuantity(pp.getProdPlanQuantity());
                        workOrder.setOutputQuantity((int) ratio.getBlackGarlicOutput());  //추출후 나온양 = 투입량*0.6 => (int) ratio.getBlackGarlicWater()*0.6
                    }else if("혼합 및 살균_즙".equals(eq)){
                        workOrder.setInputQuantity((int) ratio.getBlackGarlicOutput());
                        workOrder.setOutputQuantity((int) ratio.getBlackGarlicOutput()*4); // 혼합 후 나온양 = (int) ratio.getBlackGarlicOutput()*4
                    }else if("충진_즙".equals(eq)){
                        workOrder.setInputQuantity((int) ratio.getBlackGarlicOutput()*4);
                        workOrder.setOutputQuantity((int) ratio.getBlackGarlicOutput()*4*1000/80); //충진후 생산량 = (int) ratio.getBlackGarlicOutput()*4*1000/80
                    }else if("검사".equals(eq)){
                        workOrder.setInputQuantity((int) ratio.getBlackGarlicOutput()*4*1000/80);
                        workOrder.setOutputQuantity((int) ratio.getBlackGarlicOutput()*4*1000/80);
                    }else if("식힘".equals(eq)){
                        workOrder.setInputQuantity((int) ratio.getBlackGarlicOutput()*4*1000/80);
                        workOrder.setOutputQuantity((int) ratio.getBlackGarlicOutput()*4*1000/80);
                    }else if("포장".equals(eq)){
                        workOrder.setInputQuantity((int) ratio.getBlackGarlicOutput()*4*1000/80);
                        workOrder.setOutputQuantity((int) ratio.getBlackGarlicOutput()*4*1000/80/30); //(int) ratio.getBlackGarlicOutput()*4*1000/80/30
                    }
                }else if("P003".equals(pp.getProductId()) || "P004".equals(pp.getProductId())){
                    if("원료계량".equals(eq)){
                        workOrder.setInputQuantity((int) ratio.getJellyInputQty()); //완료 수량 그대로
                        workOrder.setOutputQuantity((int) ratio.getJellyInputQty());
                    }else if("혼합 및 살균_젤리".equals(eq)){
                        workOrder.setInputQuantity((int) ratio.getJellyInputQty());
                        workOrder.setOutputQuantity((int) ratio.getJellyInputQty());
                    }else if("충진_젤리".equals(eq)){
                        workOrder.setInputQuantity((int) ratio.getJellyInputQty());
                        workOrder.setOutputQuantity((int) ratio.getJellyInputQty()/15);
                    }else if("검사".equals(eq)){
                        workOrder.setInputQuantity((int) ratio.getJellyInputQty()/15);
                        workOrder.setOutputQuantity((int) ratio.getJellyInputQty()/15);
                    }else if("식힘".equals(eq)){
                        workOrder.setInputQuantity((int) ratio.getJellyInputQty()/15);
                        workOrder.setOutputQuantity((int) ratio.getJellyInputQty()/15);
                    }else if("포장".equals(eq)){
                        workOrder.setInputQuantity((int) ratio.getJellyInputQty()/15);
                        workOrder.setOutputQuantity((int) ((ratio.getJellyInputQty()/15/25)));
                    }
                }
                Map<String ,Object> resultMap = lotService.countTime(pp.getProductId());
                Map<String, LocalDateTime> processTimes = (Map<String, LocalDateTime>) resultMap.get("processTimes"); //끝난시간
                Map<String, LocalDateTime> processStartTimes = (Map<String, LocalDateTime>) resultMap.get("processStartTimes"); //시작시간
                System.out.println("프로세스타임(서비스) - "+processTimes); //끝난시간
                System.out.println("스타트타임 - "+processStartTimes); //시작시간

                List<LocalDateTime> processTimesList = new ArrayList<>(); //끝난시간
                List<LocalDateTime> processStartTimesList = new ArrayList<>(processStartTimes.values());  //시작시간
                for (Map.Entry<String, LocalDateTime> entry : processTimes.entrySet()) {
                    if (!entry.getKey().equals("process0")) {
                        processTimesList.add(entry.getValue());
                    }
                }
                Collections.sort(processTimesList);
                Collections.sort(processStartTimesList);
                System.out.println("프로세스타임(서비스뒤) - "+processTimesList); //끝난시간

                if(i == 0){
                    LocalDateTime process0 = processTimes.get("process0");
                    workOrder.setWorkOrderDate(process0);
                }else if(i > 0){
//                    WorkOrder previousWorkOrder = work.get(i - 1); // 이전 workOrder를 가져오기 위해 work 리스트 사용
//                    LocalDateTime previousDate = previousWorkOrder.getWorkOrderFinshDate();
//                    workOrder.setWorkOrderDate(previousDate);
                    LocalDateTime starttime = processStartTimesList.get(i);
                    System.out.println(i+"번 프로세스타임마 -"+starttime);
                    workOrder.setWorkOrderDate(starttime);
                }

//                Map<String, LocalDateTime> processTimes = lotService.countTime(pp.getProductId());
//                LocalDateTime process01Time = processTimes.get(i);


                System.out.println("프로세스타임리스트 = "+ processTimesList);


                LocalDateTime processTime = processTimesList.get(i);
                System.out.println(i+"번"+processTime);
                workOrder.setWorkOrderFinishDate(processTime);
                // 작업 수행

                System.out.println( "시발여기요 - "+processTimes);


                //workOrder.setWorkOrderDate(date); //여기서 각 공정에 들어갈수 있는 최대양 만큼 작업지시 로우를 늘린다
                //workOrder.setWorkOrderFinshDate(process01Time);

                String workStatus = determineProgressStatus();
                workOrder.setWorkStatus(workStatus); // 작업지시상태(디폴트)

                work.add(workOrder);

                // 작업지시테이블에 insert 작업 수행
                workOrderRepository.save(workOrder);
                workOrderRepository.flush();
                productionPlanRepository.setPlanStatus(planNo,"작업지시완료");
                System.out.println("야야야야야야야야야");
//                productionPlanRepository.save(pp);
//                productionPlanRepository.flush();

            }
        }

    }

//    private String getProcessNoFromRouting(Routing routing, int processNumber) {
//        switch (processNumber) {
//            case 1:
//                return routing.getProcessNo1();
//            case 2:
//                return routing.getProcessNo2();
//            case 3:
//                return routing.getProcessNo3();
//            case 4:
//                return routing.getProcessNo4();
//            case 5:
//                return routing.getProcessNo5();
//            case 6:
//                return routing.getProcessNo6();
//            case 7:
//                return routing.getProcessNo7();
//            case 8:
//                return routing.getProcessNo8();
//            case 9:
//                return routing.getProcessNo9();
//            case 10:
//                return routing.getProcessNo10();
//            default:
//                return null;
//        }
//    }



    @Scheduled(cron = "*/15 * * * * ?") // 30초 마다 실행
    public void processOrdersAutomatically() {
        List<ProductionPlan> plans = productionPlanRepository.findByProdPlanFinYn("진행중");
        if (plans != null && !plans.isEmpty()) {
            for (ProductionPlan plan : plans) {
                int planQuantity = plan.getProdPlanQuantity();
                String planNo = plan.getProdPlanNo();
                processOrder(planNo);
            }
        }

    }



    private String determineProgressStatus() {
        // 생산계획 테이블에서 모든 로우를 조회
        List<WorkOrder> workOrders = workOrderRepository.findAll();

        for (WorkOrder workOrder : workOrders) {
            if ("작업중".equals(workOrder.getWorkStatus())) {
                return "대기중"; // "진행중"인 로우가 하나라도 있으면 "대기" 상태 반환
            }
        }
        return "작업중"; // "진행중"인 로우가 없는 경우 "진행중" 상태 반환
    }

    private static int sequence = 1;

    public String generateWorkOrderNumber() {
        // 현재 시간 정보를 가져옵니다.
        LocalDateTime now = LocalDateTime.now();
        // 형식 지정을 위한 DateTimeFormatter를 생성합니다.
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        // 현재 시간 정보를 "yyyyMMdd" 형식의 문자열로 변환합니다.
        String formattedDate = now.format(formatter);
        // 시퀀스 값을 문자열로 변환합니다.
        String formattedSequence = String.format("%03d", sequence);

        List<String> no = workOrderRepository.findByPlanNo1();
        for(int i=0;i<no.size();i++){
            if(("WO"+formattedDate+formattedSequence) == no.get(i)){
                int incrementedValue = Integer.parseInt(formattedSequence) + 1;
                formattedSequence = String.format("%03d", incrementedValue);
            }
        }

        // 시퀀스 값을 1 증가시킵니다.
        sequence++;
        // 생산계획번호를 조합하여 반환합니다.
        return "WO" + formattedDate + formattedSequence;
    }

    public List<WorkOrder> getAllWorkOrder() {
        return workOrderRepository.findAll();
    }

    @Transactional
    public List<WorkOrder> searchWorkOrder(String workStatus, LocalDateTime startDate, LocalDateTime endDate, String productId, String workOrderNo,String processNo) {
        QWorkOrder qWorkOrder = QWorkOrder.workOrder;
        BooleanBuilder builder = new BooleanBuilder();


        if (workOrderNo != null && workOrderNo != "") {
            builder.and(qWorkOrder.workOrderNo.contains(workOrderNo)); //출하번호
        }
        if (workStatus != null && workStatus != "") {
            builder.and(qWorkOrder.workStatus.contains(workStatus)); //출하번호
        }


        if (productId != null && productId != "") {
            builder.and(qWorkOrder.productId.contains(productId)); // 거래처
        }

        if (processNo != null && processNo != "") {
            builder.and(qWorkOrder.processNo.contains(processNo)); // 거래처
        }

        if (startDate != null && endDate != null) {
            builder.and(qWorkOrder.workOrderDate.between(startDate, endDate)); // 날짜
        }

        return (List<WorkOrder>) workOrderRepository.findAll(builder);

//        return (List<Shipment>) shipmentRepository.findAll(builder.getValue());
    }
}
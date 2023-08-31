package mes.smartmes.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import mes.smartmes.entity.Finproduct;
import mes.smartmes.entity.ProductionPlan;
import mes.smartmes.entity.WorkOrder;
import mes.smartmes.repository.FinproductRepository;
import mes.smartmes.repository.ProductionPlanRepository;
import mes.smartmes.repository.WorkOrderRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Log4j2
@Service
@RequiredArgsConstructor
public class FinproductService {

    private final FinproductRepository finproductRepository;
    private final WorkOrderRepository workOrderRepository;
    private final ProductionPlanRepository productionPlanRepository;
    public List<Finproduct> selectList(){
        return finproductRepository.findAll();
    }

    public void insertFinproduct(String workNo){
       WorkOrder wo = workOrderRepository.findByWorkOrder(workNo);
       String id = wo.getProductId();

       Finproduct finp = finproductRepository.findByProductId(id);
       String planNo = wo.getProdPlanNo();

       ProductionPlan pp = productionPlanRepository.findByPlanNo(planNo);

       System.out.println("아이디"+finp.getProductId());

       String processNo =  wo.getProcessNo();
       System.out.println("공정넘버 -"+processNo);

       String status = wo.getWorkStatus();
       System.out.println("공정스테이터스 -"+status);
       System.out.println("인서트핀프로덕트왓어");

       if(processNo.equals("process10") && status.equals("작업완료(lot부여완료)")){
           System.out.println("이프문왓어");
           finp.setFinProductQuantity(wo.getOutputQuantity()+finp.getFinProductQuantity());

           wo.setWorkStatus("lot,완재품적재완료");
           workOrderRepository.save(wo);

           pp.setProdPlanFinYn("생산완료");
           productionPlanRepository.save(pp);

           finproductRepository.save(finp);
           finproductRepository.flush();
       }



    }


    @Scheduled(cron = "*/20 * * * * ?") // 30초 마다 실행
    public void processOrdersAutomatically() {
        List<WorkOrder> plans = workOrderRepository.findByWorkStatus("작업완료(lot부여완료)");
        if (plans != null && !plans.isEmpty()) {
            for (WorkOrder workOrder : plans) {
                //int planQuantity = workOrder.getProdPlanQuantity();
                String workNo = workOrder.getWorkOrderNo();
                insertFinproduct(workNo);
            }
        }
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

        List<String> no = finproductRepository.findByPlanNo1();
        for(int i=0;i<no.size();i++){
            if(("FP"+formattedDate+formattedSequence).equals(no.get(i))){
                int incrementedValue = Integer.parseInt(formattedSequence) + 1;
                formattedSequence = String.format("%03d", incrementedValue);
            }
        }

        // 시퀀스 값을 1 증가시킵니다.
        sequence++;
        // 생산계획번호를 조합하여 반환합니다.
        return "FP" + formattedDate + formattedSequence;
    }

}

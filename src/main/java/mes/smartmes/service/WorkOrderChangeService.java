package mes.smartmes.service;

import mes.smartmes.entity.WorkOrder;
import mes.smartmes.repository.WorkOrderRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class WorkOrderChangeService {

    private WorkOrderRepository workOrderRepository;

    public WorkOrderChangeService(WorkOrderRepository workOrderRepository) {
        this.workOrderRepository = workOrderRepository;
    }


    public void workorderchange(String workNo){


        WorkOrder wo = workOrderRepository.findByWorkOrder(workNo);
        if(wo.getWorkStatus().equals("대기중") && wo.getWorkOrderDate().isBefore(LocalDateTime.now())){
            wo.setWorkStatus("작업중");
        }else if(wo.getWorkStatus().equals("작업중") && wo.getWorkOrderFinishDate().isBefore(LocalDateTime.now())){
            wo.setWorkStatus("작업완료");
        }
        workOrderRepository.save(wo);
        workOrderRepository.flush();

    }


    @Scheduled(cron = "*/20 * * * * ?") // 30초 마다 실행
    public void processWorkOrderChangeAutomatically() {

        List<WorkOrder> workOrders = workOrderRepository.findAll();
        if (workOrders != null && !workOrders.isEmpty()) {
            for (WorkOrder workorder : workOrders) {
                String workNo = workorder.getWorkOrderNo();
                workorderchange(workNo);
            }
        }

    }



}

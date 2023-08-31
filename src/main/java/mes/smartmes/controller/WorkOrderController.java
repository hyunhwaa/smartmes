package mes.smartmes.controller;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import mes.smartmes.entity.WorkOrder;
import mes.smartmes.service.WorkOrderService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.List;


@Controller
@Log4j2
@Getter
@Setter
@RequestMapping("/mes")
public class WorkOrderController {

    private final WorkOrderService workOrderService;

    public WorkOrderController(WorkOrderService workOrderService) {
        this.workOrderService = workOrderService;
    }

    @GetMapping("/workorder")
    public String list(Model model){
        List<WorkOrder> workorder = workOrderService.getAllWorkOrder();
        model.addAttribute("WorkOrderList",workorder);
        return "workorder";
    }

    @GetMapping("/workordersearch")
    public String search(@RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDateTime startDate,
                         @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDateTime endDate,
                         @RequestParam(name = "workStatus") String workStatus,
                         @RequestParam(name = "productId") String productId,
                         @RequestParam(name = "workOrderNo") String workOrderNo,
                         @RequestParam(name = "processNo") String processNo,
//                         @RequestParam(name = "orderNo") String orderNo,
//                         @RequestParam(name = "productId") String productId,

                         Model model) {

        // 여기에서 검색 로직을 수행하고, 결과를 모델에 저장합니다.
        // 예시로서 각 매개변수를 모델에 추가하고 "searchResults"라는 이름으로 반환합니다.
        List<WorkOrder> workOrders = workOrderService.searchWorkOrder(workStatus,startDate,endDate,productId,workOrderNo,processNo);
//        List<Orders> orders = shipmentService.searchOrders(orderNo,productId);
        System.out.println(workOrders);
        model.addAttribute("WorkOrderList",workOrders);
//        model.addAttribute("orderList", orders);

        return "workorder";
    }


}

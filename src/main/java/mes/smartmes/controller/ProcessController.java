package mes.smartmes.controller;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import mes.smartmes.entity.Process;
import mes.smartmes.service.ProcessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@Log4j2
@Getter
@Setter
@RequestMapping("/mes")
public class ProcessController {

    private final ProcessService processService;
    @Autowired
    ProcessController(ProcessService processService) {
        this.processService = processService;
    }


    @GetMapping("/process")
    public String list(Model model){
        List<Process> processes = processService.getAllProcess();
        model.addAttribute("processList",processes);
        return "process";
    }


    //다중검색

    @GetMapping("/processsearch")
    public String search(
                         @RequestParam(name = "processNo") String processNO,
                         @RequestParam(name = "processName") String processName,
                         @RequestParam(name = "processDivision") String processDivision,
//                         @RequestParam(name = "productId") String productId,

                         Model model) {

        // 여기에서 검색 로직을 수행하고, 결과를 모델에 저장합니다.
        // 예시로서 각 매개변수를 모델에 추가하고 "searchResults"라는 이름으로 반환합니다.
        List<Process> processes = processService.searchProcess(processNO,processName,processDivision);
//        List<Orders> orders = shipmentService.searchOrders(orderNo,productId);
        System.out.println(processes);
        model.addAttribute("processList",processes);
//        model.addAttribute("orderList", orders);

        return "process";
    }
}

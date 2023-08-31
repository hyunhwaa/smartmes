package mes.smartmes.controller;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import mes.smartmes.entity.Routing;
import mes.smartmes.service.RoutingService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@Log4j2
@Getter
@Setter
@RequestMapping("/mes")
public class RoutingController {

    private final RoutingService routingService;


    public RoutingController(RoutingService routingService) {
        this.routingService = routingService;
    }
    @GetMapping("/routing")
    public String list(Model model){
        List<Routing> routings = routingService.getAllRouting();
        model.addAttribute("routingList",routings);
        return "Routing";
    }

}

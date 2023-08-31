package mes.smartmes.controller;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import mes.smartmes.dto.LotDTO;
import mes.smartmes.dto.Ratio;
import mes.smartmes.entity.Lot;
import mes.smartmes.entity.Porder;
import mes.smartmes.repository.LotRepository;
import mes.smartmes.service.LotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@AllArgsConstructor
@RequestMapping("/mes")
public class LotController {

    @Autowired
    private LotService lotService;

    @Autowired
    private LotRepository lotRepository;

    @GetMapping("/lot")
    public String selectList(Model model) {
        List<Lot> lotList = lotService.selectList();
        model.addAttribute("lots", lotList);

        Pageable pageable = PageRequest.of(0, 10);
        Page<Lot> result = lotRepository.findAll(pageable);
        model.addAttribute("pageable",pageable);

        System.out.println("===================================");
        System.out.println(result);
        System.out.println("Total Pages:"+result.getTotalPages());

        return "Lot";
    }


}

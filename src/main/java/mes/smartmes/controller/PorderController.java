package mes.smartmes.controller;

import lombok.RequiredArgsConstructor;
import mes.smartmes.entity.Porder;
import mes.smartmes.repository.PorderRepository;
import mes.smartmes.service.PorderService;
import org.springframework.boot.Banner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Controller
@Transactional
@RequiredArgsConstructor
@RequestMapping("/mes")
public class PorderController {

    private final PorderService porderService;
    private final PorderRepository porderRepository;
    private Date convertToDate(LocalDate localDate) {
        return java.sql.Date.valueOf(localDate);
    }

    @GetMapping("/porder")
    public String selectList(Model model) {
        List<Porder> porderList = porderService.selectList();
        model.addAttribute("porders", porderList);

        Pageable pageable = PageRequest.of(0, 10);
        Page<Porder> result = porderRepository.findAll(pageable);
        model.addAttribute("pageable",pageable);

        System.out.println("===================================");
        System.out.println(result);
        System.out.println("Total Pages:"+result.getTotalPages());

        return "Porder";
    }

    @GetMapping("/porderSearch")
    public String searchForm(){
        return "Porder";
    }

    @PostMapping("/porderSearch")
    public String performSearch(@RequestParam("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
                                @RequestParam("endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
                                @RequestParam("porderStatus") String porderStatus,
                                @RequestParam("supplierId") String supplierId,
                                Model model) {
        Date convertedStartDate = convertToDate(startDate);
        Date convertedEndDate = convertToDate(endDate);
        List<Porder> searchResults = porderService.findSearch(convertedStartDate, convertedEndDate, porderStatus, supplierId);
        model.addAttribute("porders", searchResults);
        return "Porder";
    }

}
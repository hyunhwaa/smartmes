package mes.smartmes.controller;


import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import mes.smartmes.entity.IngredientInput;
import mes.smartmes.entity.IngredientOutput;
import mes.smartmes.repository.IngredientInputRepository;
import mes.smartmes.repository.IngredientsOutputRepository;
import mes.smartmes.service.IngredientOutputService;
import mes.smartmes.service.IngredientService;
import org.hibernate.internal.build.AllowSysOut;
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
public class InOutController {

    @Autowired
    private IngredientOutputService ingredientOutputService;
    @Autowired
    private IngredientService IngredientService;
    @Autowired
    private IngredientInputRepository ingredientInputRepository;
    @Autowired
    private IngredientsOutputRepository ingredientsOutputRepository;

    @GetMapping("/in")
    public String selectList(Model model) {
        List<IngredientInput> inList = IngredientService.selectList();
        model.addAttribute("in", inList);

        Pageable pageable = PageRequest.of(0, 10);
        Page<IngredientInput> result = ingredientInputRepository.findAll(pageable);
        model.addAttribute("pageable",pageable);

        System.out.println("===================================");
        System.out.println(result);
        System.out.println("Total Pages:"+result.getTotalPages());

        return "in";
    }


    @GetMapping("/out")
    public String selectList1(Model model) {
        List<IngredientOutput> outList = ingredientOutputService.selectList();
        model.addAttribute("out", outList);

        Pageable pageable = PageRequest.of(0, 10);
        Page<IngredientOutput> result = ingredientsOutputRepository.findAll(pageable);
        model.addAttribute("pageable",pageable);

        System.out.println("===================================");
        System.out.println(result);
        System.out.println("Total Pages:"+result.getTotalPages());

        return "out";
    }





}

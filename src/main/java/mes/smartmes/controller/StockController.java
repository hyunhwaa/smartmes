package mes.smartmes.controller;

import lombok.RequiredArgsConstructor;
import mes.smartmes.entity.Finproduct;
import mes.smartmes.entity.IngredientStock;
import mes.smartmes.service.FinproductService;
import mes.smartmes.service.IngredientStockService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.transaction.Transactional;
import java.util.List;

@Controller
@Transactional
@RequiredArgsConstructor
@RequestMapping("/mes")
public class StockController {

    private final IngredientStockService ingredientStockService;
    private final FinproductService finproductService;

    @GetMapping("/stock")
    public String selectList(Model model){

        List<IngredientStock> ingredientStockList = ingredientStockService.selectList();
        List<Finproduct> finproductList = finproductService.selectList();
        model.addAttribute("ingredientStocks", ingredientStockList);
        model.addAttribute("finproducts", finproductList);

        return"ingredientStock";
    }

}

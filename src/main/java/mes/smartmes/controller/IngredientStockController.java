package mes.smartmes.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("mes")
public class IngredientStockController {

    @GetMapping("ingredientStock")
    public String test(){

        return "ingredientStock";
    }

    @GetMapping("inout")
    public String test2(){
        return "inout";
    }
}

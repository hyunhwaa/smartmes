/*
package mes.smartmes.controller;

import mes.smartmes.entity.Orders;
import mes.smartmes.entity.Porder;
import mes.smartmes.repository.PorderRepository;
import mes.smartmes.service.IngredientService;
import mes.smartmes.service.PorderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Controller
@RequestMapping("/mes")
public class IngredientController {

    @Autowired
    private IngredientService ingredientService;


    @GetMapping("/ingre")
    public String save() {
        ingredientService.updatePorderStatusAndInsertIngredient("PD20230522002");

        @PostMapping("/addOrder")
        public String saveOder(Orders orders, Model model){
            orders.setOrderDate(LocalDateTime.now());
            String dayNo = "OD" + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
            int orderIntNo=0;

            // 값이 없을 시 값 시작 값 생성
            if (ordersService.selectOrderNo() == null) {
                orderIntNo = 1;
                String orderNo = dayNo + String.format("%04d", orderIntNo);
                orders.setOrderNo(orderNo);
                orders.setOrderDate(LocalDateTime.now());
                ordersRepository.save(orders);
            } else {
                orderIntNo = Integer.parseInt(ordersService.selectOrderNo()) + 1;
                String orderNo = dayNo + String.format("%04d", orderIntNo);
                orders.setOrderNo(orderNo);
                orders.setOrderDate(LocalDateTime.now());
                ordersRepository.save(orders);
            }
            ordersRepository.save(orders);
            System.out.println(orders);
//        ordersService.selectProcessTime();




            return "ingredientStock";
    }

}



*/

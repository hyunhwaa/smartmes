package mes.smartmes.controller;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import mes.smartmes.dto.OrdersDTO;
import mes.smartmes.repository.OrdersRepository;
import mes.smartmes.service.CheckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@AllArgsConstructor
public class RestOrderController {

    @Autowired
    private OrdersRepository ordersRepository;
    @Autowired
    private CheckService checkService;

    @PostMapping("/mes/updateStatus")
    @ResponseStatus(value= HttpStatus.OK)
    public void updateStatus(String order_no){
        System.out.println(order_no+"======");
        System.out.println("=====================");
        System.out.println("=====================");
        System.out.println("=====================");
        ordersRepository.updateOrderStatus(order_no);
    }

    @PostMapping("/calculateDeliveryDate")
    public Map<String, String> calculateDeliveryDate(@RequestParam("orderqty") int orderqty, @RequestParam("selectedBox") String selectedBox) {

        // 개수와 제품명을 활용한 계산 로직 수행
        LocalDateTime time = checkService.getAllProductionPlans(selectedBox, orderqty);

        // 예상 납기일을 응답에 담아 JSON 형태로 반환
        Map<String, String> response = new HashMap<>();
        response.put("expectedDeliveryDate", String.valueOf(time));
        return response;
    }



}

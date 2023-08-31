package mes.smartmes.controller;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import mes.smartmes.entity.Ingredients;
import mes.smartmes.entity.Orders;
import mes.smartmes.entity.Product;
import mes.smartmes.repository.IngredientsRepository;
import mes.smartmes.repository.OrdersRepository;
import mes.smartmes.repository.ProcessRepository;
import mes.smartmes.repository.ProductRepository;
import mes.smartmes.service.CheckService;
import mes.smartmes.service.LotService;
import mes.smartmes.service.OrdersService;
import mes.smartmes.service.PorderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@AllArgsConstructor
@RequestMapping("/mes")
public class OrdersController {


    @Autowired
    private OrdersService ordersService;

    @Autowired
    private LotService lotService;

    @Autowired
    private OrdersRepository ordersRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CheckService checkService;

    @GetMapping("/main")
    public String main(){
        return "main";
    }


    // 수주 화면에 수주목록 띄우기
    @GetMapping("/order")
    public String save(Model model) {
        List<Orders>  orderList = ordersRepository.findAll();
        List<Product> productList = productRepository.findAll();

        // order에 수주 상세 표시
        model.addAttribute("orderList", orderList);
        model.addAttribute("productList", productList);

        return "order";
    }

    //삭제
    @GetMapping("/deleteOrder/{orderNo}")
    public String  deleteOrder(@PathVariable String orderNo) {
        ordersService.deleteByOrderNo(orderNo);

        return "redirect:/mes/order";
    }



    @PostMapping("/addOrder")
    public ResponseEntity<String> saveOrder(@RequestParam("orderDate") String orderDate,
                                            @RequestParam("companyId") String companyId,
                                            @RequestParam("productId") String productId,
                                            @RequestParam("orderQuantity") int  orderQuantity,
                                            @RequestParam("deliveryDate") String deliveryDate,
                                            @ModelAttribute Orders orders) {
        System.out.println("값이 넘어오나");
        System.out.println(ordersService.selectOrderNo());


        try{
            String dayNo = "OD" + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
            int orderIntNo = 0;
            String orderNo = null;

            if(ordersService.selectOrderNo() == null){
                orderIntNo = 1;
                orderNo = dayNo + String.format("%04d", orderIntNo);
            } else {
                System.out.println("여길 안들어오나");
                orderIntNo = Integer.parseInt(ordersService.selectOrderNo()) + 1;
                orderNo = dayNo + String.format("%04d", orderIntNo);
            }
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
            LocalDateTime orderDateTime = LocalDateTime.parse(orderDate, formatter);
            LocalDateTime deliveryDateTime = LocalDateTime.parse(deliveryDate, formatter);



            orders.setOrderNo(orderNo);
            orders.setOrderDate(orderDateTime);
            orders.setCompanyId(companyId);
            orders.setProductId(productId);
            orders.setOrderQuantity(orderQuantity);
            orders.setDeliveryDate(deliveryDateTime);
            System.out.println(orderNo);
            ordersRepository.save(orders);

            return ResponseEntity.ok("수주 저장 완료.");


        }  catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("주문 저장 중 오류가 발생했습니다.");
        }
    }






    // 조회
    @GetMapping("orderList")
    public String orderList(Model model){

        List<Orders>  orderList = ordersRepository.findAll();

        // orderList 리스트 객체를 orderList 라는 이름으로 뷰페이지에서 사용 가능하게 세팅
        model.addAttribute("orderList", orderList);

        return "order";
    }

    //주문 수정 페이지
    @GetMapping("/orderUpdate/{orderNo}")
    public String updateOrderDetail(@PathVariable("orderNo") String orderNo, Model model ){
        // 오더
        Orders orderView = new Orders();
        model.addAttribute("orders", orderView);
        Orders orders = ordersRepository.findById(orderNo).get();
        System.out.println(orders);

        // 뷰단에 넘겨줄 때 이름 체크
        model.addAttribute("orderList", orders);

        return "order";
    }
    
    //수정 후 저장
    @PostMapping("/orderUpdate")
    public String updateOrderPage(@RequestBody Orders orders,BindingResult result) {
        try {
            System.out.println("========================try 통과");
            System.out.println("orders: " + orders);

            switch (orders.getProductId().trim()) {
                case "양배추즙":
                    orders.setProductId("P001");
                    break;
                case "흑마늘즙":
                    orders.setProductId("P002");
                    break;
                case "석류젤리스틱":
                    orders.setProductId("P003");
                    break;
                case "매실젤리스틱":
                    orders.setProductId("P004");
                    break;
                default:
                    // 기본값 설정 또는 예외 처리
                    break;
            }

            orders.setOrderDate(LocalDateTime.now());
            orders.setOrderStatus("A");


            ordersService.updateOrder(orders);
            return "redirect:/mes/orderList";
        } catch (Exception e) {

            System.out.println("========================안댐안댐");
            System.out.println("orders: " + orders);


            return "order"; // 오류 페이지로 리다이렉트 또는 오류 메시지를 표시하는 뷰를 반환
        }
    }



        @GetMapping("/mainOrder")
    public String mainSave(){
        return "order";
    }

}











/*        @PostMapping
    public ResponseEntity<?> createOrder(@RequestBody OrdersDTO ordersDTO) {
        Orders orders = ordersService.createOrder(ordersDTO);
        return ResponseEntity.ok(orders);
    }*/

/*      @GetMapping("/{orderNo}")
    public ResponseEntity<?> getOrder(@PathVariable String orderNo) {
        Orders orders = ordersService.getOrderByOrderNo(orderNo);
        return ResponseEntity.ok(orders);
    }*/

/*        @PutMapping("/{orderNo}")
    public ResponseEntity<?> updateOrder(@PathVariable String orderNo, @RequestBody OrdersDTO ordersDTO) {
        Orders orders = ordersService.updateOrderByOrderNo(orderNo, ordersDTO);
        return ResponseEntity.ok(orders);
    }*/




/*          데이터 저장 확인 코드
    //수주
    @RequestMapping("/addOrder")
    public String save(*/
/*OrdersDTO ordersDTO,*//*
 Model model) {
        Orders orders = new Orders();
        //수주 번호 생성
        String dayNo = "OD" + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        int orderIntNo=0;

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


        System.out.println(orders);
        System.out.println("=========================");
        System.out.println((orderIntNo));


        return "order";
    }
*/



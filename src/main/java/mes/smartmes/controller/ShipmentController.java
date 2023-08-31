package mes.smartmes.controller;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import mes.smartmes.entity.Orders;
import mes.smartmes.entity.Porder;
import mes.smartmes.entity.Product;
import mes.smartmes.entity.Shipment;
import mes.smartmes.repository.OrdersRepository;
import mes.smartmes.repository.PorderRepository;
import mes.smartmes.repository.ProductRepository;
import mes.smartmes.repository.ShipmentRepository;
import mes.smartmes.service.CalendarService;
import mes.smartmes.service.OrdersService;
import mes.smartmes.service.ShipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@AllArgsConstructor
@Log4j2
@Getter
@Setter
@RequestMapping("/mes")
public class ShipmentController {

    @Autowired
    private ShipmentService shipmentService;

    @Autowired
    private ShipmentRepository shipmentRepository;

    private OrdersService ordersService;
    @Autowired
    private OrdersRepository ordersRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private PorderRepository porderRepository;
    @Autowired
    private CalendarService calendarService;


    public ShipmentController(ShipmentService shipmentService, OrdersService ordersService) {
        this.shipmentService = shipmentService;
        this.ordersService = ordersService;
    }


//    //출하 등록
//    @GetMapping("/shipment")
//    public String save(Model model){
//        List<Shipment> shipmentList =  shipmentRepository.findAll();
//
//        model.addAttribute("shipmentList" , shipmentList);
//
//
//        return "shipment";
//
//    }

    @GetMapping("/event") //ajax 데이터 전송 URL
    public @ResponseBody List<Map<String, Object>> getEvent(){
        return CalendarService.getEventList();
    }


    //출하 등록
    @PostMapping("/addShipment")
    // 저장할 모델 객체, 보내줄 디비 객체
    public String saveShipment(Shipment shipment, Model model){
        shipment.setShipmentDate(LocalDateTime.now());
        String dayNo = "SD" + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        int shipmentIntNo=0;

        // 값이 없을 시 값 시작 값 생성
        if (shipmentService.selectShipmentNo() == null) {
            shipmentIntNo = 1;
            String shipmentNo = dayNo + String.format("%04d", shipmentIntNo);
            shipment.setShipmentNo(shipmentNo);
            shipment.setShipmentDate(LocalDateTime.now());
            shipmentRepository.save(shipment);
        } else {
            shipmentIntNo = Integer.parseInt(shipmentService.selectShipmentNo()) + 1;
            String shipmentNo = dayNo + String.format("%04d", shipmentIntNo);
            shipment.setShipmentNo(shipmentNo);
            shipment.setShipmentDate(LocalDateTime.now());
            shipmentRepository.save(shipment);
        }
        shipmentRepository.save(shipment);
        System.out.println(shipment);

        return  "redirect:/shipment/shipment";
    }


    // 조회
    @GetMapping("/shipment")
    public String selectList(Model model) {
        List<Shipment> shipmentList = shipmentService.selectList();
        model.addAttribute("shipments", shipmentList);

        Pageable pageable = PageRequest.of(0, 10);
        Page<Porder> result = porderRepository.findAll(pageable);
        model.addAttribute("pageable",pageable);

        System.out.println("===================================");
        System.out.println(result);
        System.out.println("Total Pages:"+result.getTotalPages());

        return "shipment";
    }

    // 디비에 있는 데이터  뷰페이지에 전달
//    @GetMapping("/shipment")
//    public String list(Model model){
//        List<Shipment> shipments = shipmentService.getAllShipments();
//        List<Orders>  orderList = ordersRepository.findAll();
//        List<Product> productList = productRepository.findAll();
//        List<Porder> porderList = porderRepository.findAll();
//        model.addAttribute("shipmentlist",shipments);
//        model.addAttribute("orderList",orderList);
//        model.addAttribute("productList",productList);
//        model.addAttribute("porderList",porderList);
//        return "shipment";
//    }

    //다중검색

    @GetMapping("/shipmentsearch")
    public String search(@RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDateTime startDate,
                         @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDateTime endDate,
                         @RequestParam(name = "shipmentNo") String shipmentNo,
                         @RequestParam(name = "companyName") String companyName,
                         @RequestParam(name = "orderNo") String orderNo,
                         @RequestParam(name = "productId") String productId,

                         Model model) {

        // 여기에서 검색 로직을 수행하고, 결과를 모델에 저장합니다.
        // 예시로서 각 매개변수를 모델에 추가하고 "searchResults"라는 이름으로 반환합니다.
        List<Shipment> shipments = shipmentService.searchShipment(shipmentNo,startDate,endDate,companyName);
        List<Orders> orders = shipmentService.searchOrders(orderNo,productId);
        System.out.println(shipments);
        model.addAttribute("shipmentlist",shipments);
        model.addAttribute("orderList", orders);

        return "shipment";
    }


}

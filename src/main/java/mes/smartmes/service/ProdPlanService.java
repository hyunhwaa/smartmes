package mes.smartmes.service;

import lombok.extern.log4j.Log4j2;
import mes.smartmes.entity.*;
import mes.smartmes.entity.RealPorderSelect;
import mes.smartmes.repository.*;
import net.bytebuddy.asm.Advice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Log4j2
@Service
public class ProdPlanService {

    private OrdersRepository ordersRepository;
    private IngredientStockRepository ingredientStockRepository;
    private PorderRepository porderRepository;
    private ProductionPlanRepository productionPlanRepository;
    private FinproductRepository finproductRepository;
    private IngredientsRepository ingredientsRepository;
    private ShipmentRepository shipmentRepository;
    private ProductRepository productRepository;
    private ProdPlanRepository prodPlanRepository;
    private IngredientOutputService ingredientOutputService;



    @Autowired
    public ProdPlanService(OrdersRepository ordersRepository, IngredientStockRepository ingredientStockRepository,
                           PorderRepository porderRepository, ProductionPlanRepository productionPlanRepository,
                           FinproductRepository finproductRepository, IngredientsRepository ingredientsRepository,
                           ShipmentRepository shipmentRepository, ProductRepository productRepository) {
        this.ordersRepository = ordersRepository;
        this.ingredientStockRepository = ingredientStockRepository;
        this.porderRepository = porderRepository;
        this.productionPlanRepository = productionPlanRepository;
        this.finproductRepository = finproductRepository;
        this.ingredientsRepository = ingredientsRepository;
        this.shipmentRepository = shipmentRepository;
        this.productRepository = productRepository;
    }

    @Autowired
    private ProductService productService;
    @Autowired
    private PorderService porderService;

    public void processOrder(String orderNo) {


        System.out.println("수주번호 : "+ orderNo);
        // 수주 관리 테이블에서 제품명과 수주수량 가져오기
        Orders order = ordersRepository.findByOrderNo(orderNo);
        String productId = order.getProductId();
        int orderQuantity = order.getOrderQuantity();
        System.out.println("------------------");
        System.out.println("제품id : " + productId);

        Finproduct finproduct = finproductRepository.findByProductId(productId);
        int haveFinquantity = finproduct.getFinProductQuantity();
        int productQuantity = orderQuantity - haveFinquantity;
        System.out.println("완재품 재고량(box) :" + haveFinquantity);
        System.out.println("생산해야할 제품(box)양 : " + productQuantity);
        System.out.println("수주량(box) : " + orderQuantity);

        if (orderQuantity > haveFinquantity) {
            System.out.println("In-1");
            System.out.println("=========여기용========");
            //완재품 재고량 빼기(수주량 100- 현재 완제품량 50)
            if(haveFinquantity > 0) {
                System.out.println("완재품빼기 왔어");
                finproductRepository.decreaseStockQuantity(finproduct.getProductId(), order.getOrderQuantity());
                Shipment shipment = new Shipment();
                shipment.setShipmentDate(LocalDateTime.now());
                shipment.setShipmentStatus("남은 완제품 생산될때까지 출하 대기");
                shipment.setCompanyName(order.getCompanyId());
                shipment.setOrderNo(orderNo);
                shipment.setProductId(productId);
                //자동채번
                String shipno = generateShipNumber();
                shipment.setShipmentNo(shipno);
                shipment.setShipmentQuantity(haveFinquantity);
                shipmentRepository.save(shipment);
                shipmentRepository.flush();

                System.out.println("완재품 재고량(box) :" + haveFinquantity);
                System.out.println("출하량(box) :" + haveFinquantity);
                System.out.println("수주량에서 가지고있는양 빼고 생산해야할 제품(box)양 : "+productQuantity);
                System.out.println("완제품 출고대기");

                if(productQuantity > 0){
                    System.out.println("완재품이 0보다 작을때 0으로 set하기 왔어");
                    finproduct.setFinProductQuantity(0);
                    finproductRepository.save(finproduct); // 완재품 재고량 저장
                    System.out.println(finproduct.getFinProductQuantity());

                }
            }
            // 수주수량(box 단위)을 원자재 kg 단위로 치환(양배추일때, 흑마늘일때, 석류젤리스틱일때, 매실젤리스틱일때)

            int needMaterials = 0;
            if("양배추즙".equals(productRepository.findProductNameByProductId(productId))) {
                needMaterials = (productQuantity * 30) / 20;
            }else if("흑마늘즙".equals(productRepository.findProductNameByProductId(productId))){
                needMaterials = (productQuantity * 30) / 120;
            }else if("매실젤리스틱".equals(productRepository.findProductNameByProductId(productId)) || "석류젤리스틱".equals(productRepository.findProductNameByProductId(productId))) {
                needMaterials = (productQuantity * 25 * 5) / 1000;
            }

            //필요한 자재(박스,파우치,스틱파우치,콜라겐) 확인(부족시 발주)
            int boxneed = productQuantity;
            int stickpouch = productQuantity * 30;
            int pouch = productQuantity * 30;
            int needCollagen = (productQuantity*30)/1000;
            System.out.println("booxNeed : "+boxneed);

            if("양배추즙".equals(productRepository.findProductNameByProductId(productId))){

                String ingredientId = "I008";

                int haveBox = ingredientStockRepository.findBoxNumByProductId(ingredientId);

                System.out.println("ingredientStock : " +haveBox);
                if(boxneed > haveBox) {

                    //박스 최소주문량 적용
                    System.out.println("booxNeed1 : "+boxneed);
                    System.out.println("ingredientStock1 : " +haveBox);
                    int orderBox = boxneed - haveBox;

                    //필요한 만큼 발주관리에 insert
                    Porder porder = new Porder();
                    String Porderno = generatePorderNumber();
                    porder.setPorderNo(Porderno);
                    porder.setProdPlanNo(orderNo);
                    porder.setEmergencyYn("N");
                    String porderIngredientName = ingredientsRepository.findBoxConcentrateByProductId(productId);
                    porder.setIngredientName(porderIngredientName);
                    if(orderBox<500){
                        porder.setPorderQuantity(500);
                    }else if(orderBox >= 500 && orderBox <= 10000){
                        int porderBox = (int) (Math.ceil(orderBox/500)*500.0);
                        porder.setPorderQuantity(porderBox);
                    }else if(orderBox > 10000){
                        porder.setPorderQuantity(10000);
                        int remainingQuantity = orderBox - 10000; // 남은 수량 계산
                        int numAdditionalOrders = (int) Math.ceil(remainingQuantity / 10000.0); // 추가 발주 횟수 계산

                        for (int i = 1; i <= numAdditionalOrders; i++) {
                            Porder additionalPorder = new Porder();
                            String porderNo = generatePorderNumber();
                            additionalPorder.setPorderNo(porderNo);
                            additionalPorder.setEmergencyYn("N");
                            additionalPorder.setIngredientName(porderIngredientName);
                            additionalPorder.setSupplierId("A포장회사");

                            //발주수량정하기
                            if (i == numAdditionalOrders) {
                                if(remainingQuantity >= 10000) {
                                    additionalPorder.setPorderQuantity(remainingQuantity);
                                }else if(remainingQuantity >= 500 && remainingQuantity < 10000){
                                    System.out.println("여기여기 : "+remainingQuantity);
                                    System.out.println("여기여기 : "+(orderBox-haveBox));
                                    additionalPorder.setPorderQuantity((int)(Math.ceil(remainingQuantity/500.0)*500));

                                }
                            } else {
                                additionalPorder.setPorderQuantity(10000);
                                remainingQuantity -= 10000;
                            }

                            //rps.setRealporderid();
                            porderRepository.save(additionalPorder);
                            porderRepository.flush();



                            // 추가 발주 객체(porder)를 처리하는 로직 작성
                            // 예를 들어, 추가 발주 객체를 저장하는 리스트에 추가하거나 데이터베이스에 저장하는 등의 처리를 수행
                        }
                    }

                    System.out.println(porder.getPorderQuantity());

                    porder.setSupplierId("A포장회사");

                    //박스 자재량 빼기
                    IngredientStock ingredientStock = ingredientStockRepository.findBoxConcentrateByProductId(ingredientId);
                    int haveMaterials = ingredientStock.getQuantity();
                    System.out.println("박스 재고량 : " + haveMaterials + "kg");
                    String pId = ingredientsRepository.findBoxIdByProductId(productId);
                    System.out.println("Pid : "+pId);
                    System.out.println("야야야ㅑ : "+ingredientStock.getQuantity());
                    if(ingredientStock.getQuantity() == 0){
                        ingredientStock.setQuantity(0);
                    }else if(ingredientStock.getQuantity() > 0){
                        System.out.println("여기용ㅇㅇㅇㅇㅇㅇㅇ");
                        System.out.println("pid : "+ pId);
                        System.out.println("haveBox : "+ haveMaterials);
                        ingredientStockRepository.decreaseStock1Quantity(pId, haveMaterials);
                    }

                    //ingredientStockRepository.decreaseStockQuantity(pId, boxneed);
                    porderRepository.save(porder);
                    porderRepository.flush();
                }else{
                    //boxneed값만큼 박스자재량 빼기
                    IngredientStock ingredientStock = ingredientStockRepository.findBoxConcentrateByProductId(ingredientId);
                    int haveMaterials = ingredientStock.getQuantity();
                    String pId = ingredientsRepository.findBoxIdByProductId(productId);
                    ingredientStockRepository.decreaseStock1Quantity(pId, haveMaterials);
                    if(ingredientStock.getQuantity() == 0){
                        ingredientStock.setQuantity(0);
                    }
                }

                String ingredientId1 = "I006";
                int havePouch = ingredientStockRepository.findPouchNumByProductId(ingredientId1);

                if(pouch > havePouch) {
                    //파우치 최소주문량 적용
                    int needPouch = (int)Math.ceil((pouch - havePouch) /1000.0)*1000;
                    int orderPouch = pouch - havePouch;
                    //필요한 만큼 발주
                    Porder porder = new Porder();
                    String Porderno = generatePorderNumber();
                    porder.setPorderNo(Porderno);
                    porder.setEmergencyYn("N");
                    String porderIngredientId = ingredientsRepository.findPouchConcentrateByProductId(productId);
                    porder.setIngredientName(porderIngredientId);
                    if(orderPouch<1000){
                        porder.setPorderQuantity(1000);
                    }else if(orderPouch >= 1000 && orderPouch <= 1000000){
                        porder.setPorderQuantity(needPouch);
                    }else if(orderPouch > 1000000){
                        porder.setPorderQuantity(1000000);
                        int remainingQuantity = orderPouch-1000000;
                        int numOrders = (int)Math.ceil(remainingQuantity / 1000000.0); // 추가 발주 횟수 계산

                        for (int i = 1; i <= numOrders; i++) {
                            Porder additionalPorder = new Porder();
                            String porderNo = generatePorderNumber();
                            additionalPorder.setPorderNo(porderNo);
                            additionalPorder.setEmergencyYn("N");
                            additionalPorder.setIngredientName(porderIngredientId);

                            additionalPorder.setSupplierId("A파우치회사");

                            //발주수량정하기
                            if (i == numOrders) {
                                if (remainingQuantity >= 1000000) {
                                    additionalPorder.setPorderQuantity(remainingQuantity);
                                } else if (remainingQuantity >= 1000 && remainingQuantity < 1000000) {
                                    System.out.println("여기여기 : " + remainingQuantity);
                                    System.out.println("여기여기 : " + (orderPouch - haveBox));
                                    additionalPorder.setPorderQuantity((int) (Math.ceil(remainingQuantity / 500.0) * 500));
                                    System.out.println("여기여기 : " + additionalPorder.getPorderQuantity());
                                }
                            } else {
                                additionalPorder.setPorderQuantity(1000000);
                                remainingQuantity -= 1000000;
                            }
                            porderRepository.save(additionalPorder);
                            porderRepository.flush();
                            // 추가 발주 객체(porder)를 처리하는 로직 작성
                            // 예를 들어, 추가 발주 객체를 저장하는 리스트에 추가하거나 데이터베이스에 저장하는 등의 처리를 수행
                        }
                    }

                    System.out.println(porder.getPorderQuantity());

                    porder.setSupplierId("A파우치회사");

                    //파우치 자재량 빼기
                    IngredientStock ingredientStock2 = new IngredientStock();
                    String pId = ingredientsRepository.findPouchIdByProductId(productId);
                    System.out.println("pid : " + pId);
                    ingredientStockRepository.decreaseStock1Quantity(pId, havePouch);
                    porderRepository.save(porder);
                    porderRepository.flush();
                }else{
                    //pouch값만큼 파우치자재량 빼기
                    IngredientStock ingredientStock3 = new IngredientStock();
                    String pId = ingredientsRepository.findPouchIdByProductId(productId);
                    System.out.println("pid : " + pId);
                    ingredientStockRepository.decreaseStock1Quantity(pId, havePouch);
                    if(ingredientStock3.getQuantity() == 0){
                        ingredientStock3.setQuantity(0);
                    }
                }
            }
            if("흑마늘즙".equals(productRepository.findProductNameByProductId(productId))){
                String ingredientId = "I008";
                int haveBox = ingredientStockRepository.findBoxNumByProductId(ingredientId);
                if(boxneed > haveBox) {

                    //박스 최소주문량 적용
                    System.out.println("booxNeed1 : "+boxneed);
                    System.out.println("ingredientStock1 : " +haveBox);
                    int orderBox = boxneed - haveBox;

                    //필요한 만큼 발주관리에 insert
                    Porder porder = new Porder();
                    String Porderno = generatePorderNumber();
                    porder.setPorderNo(Porderno);
                    porder.setEmergencyYn("N");
                    String porderIngredientId = ingredientsRepository.findBoxConcentrateByProductId(productId);
                    porder.setIngredientName(porderIngredientId);
                    if(orderBox<500){
                        porder.setPorderQuantity(500);
                    }else if(orderBox >= 500 && orderBox <= 10000){
                        int porderBox = (int) (Math.ceil(orderBox/500)*500.0);
                        porder.setPorderQuantity(porderBox);
                    }else if(orderBox > 10000){
                        porder.setPorderQuantity(10000);
                        int remainingQuantity = orderBox - 10000; // 남은 수량 계산
                        int numAdditionalOrders = (int) Math.ceil(remainingQuantity / 10000.0); // 추가 발주 횟수 계산

                        for (int i = 1; i <= numAdditionalOrders; i++) {
                            Porder additionalPorder = new Porder();
                            String porderNo = generatePorderNumber();
                            additionalPorder.setPorderNo(porderNo);
                            additionalPorder.setEmergencyYn("N");
                            additionalPorder.setIngredientName(porderIngredientId);

                            additionalPorder.setSupplierId("A포장회사");

                            //발주수량정하기
                            if (i == numAdditionalOrders) {
                                if(remainingQuantity >= 10000) {
                                    additionalPorder.setPorderQuantity(remainingQuantity);
                                }else if(remainingQuantity >= 500 && remainingQuantity < 10000){
                                    System.out.println("여기여기 : "+remainingQuantity);
                                    System.out.println("여기여기 : "+(orderBox-haveBox));
                                    additionalPorder.setPorderQuantity((int)(Math.ceil(remainingQuantity/500.0)*500));
                                    System.out.println("여기여기 : "+additionalPorder.getPorderQuantity());
                                }
                            } else {
                                additionalPorder.setPorderQuantity(10000);
                                remainingQuantity -= 10000;
                            }
                            porderRepository.save(additionalPorder);
                            porderRepository.flush();
                            // 추가 발주 객체(porder)를 처리하는 로직 작성
                            // 예를 들어, 추가 발주 객체를 저장하는 리스트에 추가하거나 데이터베이스에 저장하는 등의 처리를 수행
                        }
                    }

                    System.out.println(porder.getPorderQuantity());

                    porder.setSupplierId("A포장회사");

                    //박스 자재량 빼기
                    IngredientStock ingredientStock = ingredientStockRepository.findBoxConcentrateByProductId(ingredientId);
                    int haveMaterials = ingredientStock.getQuantity();
                    System.out.println("박스 재고량 : " + haveMaterials + "kg");
                    String pId = ingredientsRepository.findBoxIdByProductId(productId);
                    System.out.println("Pid : "+pId);
                    System.out.println("야야야ㅑ : "+ingredientStock.getQuantity());
                    if(ingredientStock.getQuantity() == 0){
                        ingredientStock.setQuantity(0);
                    }else if(ingredientStock.getQuantity() > 0){
                        System.out.println("여기용ㅇㅇㅇㅇㅇㅇㅇ");
                        System.out.println("pid : "+ pId);
                        System.out.println("haveBox : "+ haveMaterials);
                        ingredientStockRepository.decreaseStock1Quantity(pId, haveMaterials);
                    }

                    //ingredientStockRepository.decreaseStockQuantity(pId, boxneed);
                    porderRepository.save(porder);
                    porderRepository.flush();
                }else{
                    //boxneed값만큼 박스자재량 빼기
                    IngredientStock ingredientStock = ingredientStockRepository.findBoxConcentrateByProductId(ingredientId);
                    int haveMaterials = ingredientStock.getQuantity();
                    String pId = ingredientsRepository.findBoxIdByProductId(productId);
                    ingredientStockRepository.decreaseStock1Quantity(pId, haveMaterials);
                    if(ingredientStock.getQuantity() == 0){
                        ingredientStock.setQuantity(0);
                    }
                }

                String ingredientId1 = "I006";
                int havePouch = ingredientStockRepository.findPouchNumByProductId(ingredientId1);

                if(pouch > havePouch) {
                    //파우치 최소주문량 적용
                    int needPouch = (int)Math.ceil((pouch - havePouch)/500.0)*500;
                    int orderPouch = pouch - havePouch;

                    //필요한 만큼 발주
                    Porder porder = new Porder();
                    String Porderno = generatePorderNumber();
                    porder.setPorderNo(Porderno);
                    porder.setEmergencyYn("N");
                    String porderIngredientId = ingredientsRepository.findPouchConcentrateByProductId(productId);
                    porder.setIngredientName(porderIngredientId);

                    if(orderPouch<1000){
                        porder.setPorderQuantity(1000);
                    }else if(orderPouch >= 1000 && orderPouch <= 1000000){
                        porder.setPorderQuantity(needPouch);
                    }else if(orderPouch > 1000000){
                        porder.setPorderQuantity(1000000);
                        int remainingQuantity = orderPouch-1000000;
                        int numOrders = (int)Math.ceil(remainingQuantity / 1000000.0); // 추가 발주 횟수 계산

                        for (int i = 1; i <= numOrders; i++) {
                            Porder additionalPorder = new Porder();
                            String porderNo = generatePorderNumber();
                            additionalPorder.setPorderNo(porderNo);
                            additionalPorder.setEmergencyYn("N");
                            additionalPorder.setIngredientName(porderIngredientId);

                            additionalPorder.setSupplierId("A파우치회사");

                            //발주수량정하기
                            if (i == numOrders) {
                                if (remainingQuantity >= 1000000) {
                                    additionalPorder.setPorderQuantity(remainingQuantity);
                                } else if (remainingQuantity >= 1000 && remainingQuantity < 1000000) {
                                    System.out.println("여기여기 : " + remainingQuantity);
                                    System.out.println("여기여기 : " + (orderPouch - haveBox));
                                    additionalPorder.setPorderQuantity((int) (Math.ceil(remainingQuantity / 500.0) * 500));
                                    System.out.println("여기여기 : " + additionalPorder.getPorderQuantity());
                                }
                            } else {
                                additionalPorder.setPorderQuantity(1000000);
                                remainingQuantity -= 1000000;
                            }
                            porderRepository.save(additionalPorder);
                            porderRepository.flush();
                            // 추가 발주 객체(porder)를 처리하는 로직 작성
                            // 예를 들어, 추가 발주 객체를 저장하는 리스트에 추가하거나 데이터베이스에 저장하는 등의 처리를 수행
                        }
                    }

                    porder.setSupplierId("A파우치회사");
                    //파우치 자재량 빼기
                    IngredientStock ingredientStock2 = new IngredientStock();
                    String pId = ingredientsRepository.findPouchIdByProductId(productId);
                    System.out.println("pid : " + pId);
                    ingredientStockRepository.decreaseStock1Quantity(pId, havePouch);
                    porderRepository.save(porder);
                    porderRepository.flush();
                }else{
                    //pouch값만큼 파우치자재량 빼기
                    IngredientStock ingredientStock3 = new IngredientStock();
                    String pId = ingredientsRepository.findPouchIdByProductId(productId);
                    System.out.println("pid : " + pId);
                    ingredientStockRepository.decreaseStock1Quantity(pId, havePouch);
                    if(ingredientStock3.getQuantity() == 0){
                        ingredientStock3.setQuantity(0);
                    }
                }
            }
            if("석류젤리스틱".equals(productRepository.findProductNameByProductId(productId))){
                String ingredientId = "I008";
                int haveBox = ingredientStockRepository.findBoxNumByProductId(ingredientId);
                if(boxneed > haveBox) {

                    //박스 최소주문량 적용
                    System.out.println("booxNeed1 : "+boxneed);
                    System.out.println("ingredientStock1 : " +haveBox);
                    int orderBox = boxneed - haveBox;

                    //필요한 만큼 발주관리에 insert
                    Porder porder = new Porder();
                    String Porderno = generatePorderNumber();
                    porder.setPorderNo(Porderno);
                    porder.setEmergencyYn("N");
                    String porderIngredientId = ingredientsRepository.findBoxConcentrateByProductId(productId);
                    porder.setIngredientName(porderIngredientId);
                    if(orderBox<500){
                        porder.setPorderQuantity(500);
                    }else if(orderBox >= 500 && orderBox <= 10000){
                        int porderBox = (int) (Math.ceil(orderBox/500)*500.0);
                        porder.setPorderQuantity(porderBox);
                    }else if(orderBox > 10000){
                        porder.setPorderQuantity(10000);
                        int remainingQuantity = orderBox - 10000; // 남은 수량 계산
                        int numAdditionalOrders = (int) Math.ceil(remainingQuantity / 10000.0); // 추가 발주 횟수 계산

                        for (int i = 1; i <= numAdditionalOrders; i++) {
                            Porder additionalPorder = new Porder();
                            String porderNo = generatePorderNumber();
                            additionalPorder.setPorderNo(porderNo);
                            additionalPorder.setEmergencyYn("N");
                            additionalPorder.setIngredientName(porderIngredientId);
                            additionalPorder.setSupplierId("A포장회사");

                            //발주수량정하기
                            if (i == numAdditionalOrders) {
                                if(remainingQuantity >= 10000) {
                                    additionalPorder.setPorderQuantity(remainingQuantity);
                                }else if(remainingQuantity >= 500 && remainingQuantity < 10000){
                                    System.out.println("여기여기 : "+remainingQuantity);
                                    System.out.println("여기여기 : "+(orderBox-haveBox));
                                    additionalPorder.setPorderQuantity((int)(Math.ceil(remainingQuantity/500.0)*500));
                                    System.out.println("여기여기 : "+additionalPorder.getPorderQuantity());
                                }
                            } else {
                                additionalPorder.setPorderQuantity(10000);
                                remainingQuantity -= 10000;
                            }
                            porderRepository.save(additionalPorder);
                            porderRepository.flush();
                            // 추가 발주 객체(porder)를 처리하는 로직 작성
                            // 예를 들어, 추가 발주 객체를 저장하는 리스트에 추가하거나 데이터베이스에 저장하는 등의 처리를 수행
                        }
                    }

                    System.out.println(porder.getPorderQuantity());

                    porder.setSupplierId("A포장회사");

                    //박스 자재량 빼기
                    IngredientStock ingredientStock = ingredientStockRepository.findBoxConcentrateByProductId(ingredientId);
                    int haveMaterials = ingredientStock.getQuantity();
                    System.out.println("박스 재고량 : " + haveMaterials + "kg");
                    String pId = ingredientsRepository.findBoxIdByProductId(productId);
                    System.out.println("Pid : "+pId);
                    System.out.println("야야야ㅑ : "+ingredientStock.getQuantity());
                    if(ingredientStock.getQuantity() == 0){
                        ingredientStock.setQuantity(0);
                    }else if(ingredientStock.getQuantity() > 0){
                        System.out.println("여기용ㅇㅇㅇㅇㅇㅇㅇ");
                        System.out.println("pid : "+ pId);
                        System.out.println("haveBox : "+ haveMaterials);
                        ingredientStockRepository.decreaseStock1Quantity(pId, haveMaterials);
                    }

                    //ingredientStockRepository.decreaseStockQuantity(pId, boxneed);
                    porderRepository.save(porder);
                    porderRepository.flush();
                }else{
                    //boxneed값만큼 박스자재량 빼기
                    IngredientStock ingredientStock = ingredientStockRepository.findBoxConcentrateByProductId(ingredientId);
                    int haveMaterials = ingredientStock.getQuantity();
                    String pId = ingredientsRepository.findBoxIdByProductId(productId);
                    ingredientStockRepository.decreaseStock1Quantity(pId, haveMaterials);
                    if(ingredientStock.getQuantity() == 0){
                        ingredientStock.setQuantity(0);
                    }
                }

                String ingredientId1 = "I007";
                int haveStickPouch = ingredientStockRepository.findStickPouchNumByProductId(ingredientId1);

                if(stickpouch > haveStickPouch) {
                    //스틱파우치 최소주문량 적용
                    int needPouch = (int)Math.ceil((stickpouch - haveStickPouch)/1000.0)*1000;

                    System.out.println("orderpouch : "+ (stickpouch-haveStickPouch));
                    System.out.println("needpouch : "+ needPouch);
                    int orderStickPouch = stickpouch - haveStickPouch;
                    //필요한 만큼 발주
                    Porder porder = new Porder();
                    String Porderno = generatePorderNumber();
                    porder.setPorderNo(Porderno);
                    porder.setEmergencyYn("N");
                    String porderIngredientId = ingredientsRepository.findStickPouchConcentrateByProductId(productId);
                    porder.setIngredientName(porderIngredientId);
                    if(orderStickPouch<1000){
                        porder.setPorderQuantity(1000);
                    }else if(orderStickPouch >= 1000 && orderStickPouch <= 1000000){
                        System.out.println("in-1000");
                        porder.setPorderQuantity(needPouch);
                    }else if(orderStickPouch > 1000000){
                        porder.setPorderQuantity(1000000);
                        int remainingQuantity = orderStickPouch-1000000;
                        int numOrders = (int)Math.ceil(remainingQuantity / 1000000.0); // 추가 발주 횟수 계산

                        for (int i = 1; i <= numOrders; i++) {
                            Porder additionalPorder = new Porder();
                            String porderNo = generatePorderNumber();
                            additionalPorder.setPorderNo(porderNo);
                            additionalPorder.setEmergencyYn("N");
                            additionalPorder.setIngredientName(porderIngredientId);
                            additionalPorder.setSupplierId("A스틱파우치회사");

                            //발주수량정하기
                            if (i == numOrders) {
                                if (remainingQuantity >= 1000000) {
                                    additionalPorder.setPorderQuantity(remainingQuantity);
                                } else if (remainingQuantity >= 1000 && remainingQuantity < 1000000) {
                                    System.out.println("여기여기 : " + remainingQuantity);
                                    System.out.println("여기여기 : " + (orderStickPouch - haveBox));
                                    additionalPorder.setPorderQuantity((int) (Math.ceil(remainingQuantity / 1000.0) * 1000));
                                    System.out.println("여기여기 : " + additionalPorder.getPorderQuantity());
                                }
                            } else {
                                additionalPorder.setPorderQuantity(1000000);
                                remainingQuantity -= 1000000;
                            }
                            porderRepository.save(additionalPorder);
                            porderRepository.flush();
                            // 추가 발주 객체(porder)를 처리하는 로직 작성
                            // 예를 들어, 추가 발주 객체를 저장하는 리스트에 추가하거나 데이터베이스에 저장하는 등의 처리를 수행
                        }
                    }
                    System.out.println(porder.getPorderQuantity());

                    porder.setSupplierId("A스틱파우치회사");

                    //스틱파우치 자재량 빼기
                    IngredientStock ingredientStock2 = new IngredientStock();
                    String pId = ingredientsRepository.findStickPouchIdByProductId(productId);
                    System.out.println("pid : " + pId);
                    ingredientStockRepository.decreaseStock1Quantity(pId, haveStickPouch);
                    porderRepository.save(porder);
                    porderRepository.flush();
                }else{
                    //stickpouch값만큼 스틱파우치자재량 빼기
                    IngredientStock ingredientStock3 = new IngredientStock();
                    String pId = ingredientsRepository.findStickPouchIdByProductId(productId);
                    System.out.println("pid : " + pId);
                    ingredientStockRepository.decreaseStock1Quantity(pId, haveStickPouch);
                    if(ingredientStock3.getQuantity() == 0){
                        ingredientStock3.setQuantity(0);
                    }
                }

                String ingredientId2 = "I005";
                int haveCollagen = ingredientStockRepository.findCollagenNumByProductId(ingredientId2);
                if(needCollagen > haveCollagen){
                    //콜라겐 최소주문량 적용
                    int needColla = (int)Math.ceil((needCollagen - haveCollagen) /5.0)*5;
                    int orderCollagen = needCollagen - haveCollagen;
                    //필요한 만큼 발주
                    Porder porder = new Porder();
                    String Porderno = generatePorderNumber();
                    porder.setPorderNo(Porderno);
                    porder.setEmergencyYn("N");
                    String porderIngredientId = ingredientsRepository.findCollagenConcentrateByProductId(productId);
                    porder.setIngredientName(porderIngredientId);
                    if(orderCollagen<5){
                        porder.setPorderQuantity(5);
                    }else if(orderCollagen >= 5 && orderCollagen <= 500){
                        porder.setPorderQuantity(needColla);
                    }else if(orderCollagen > 500){
                        porder.setPorderQuantity(500);
                        int remainingQuantity = orderCollagen-500;
                        int numOrders = (int)Math.ceil(remainingQuantity / 500.0); // 추가 발주 횟수 계산

                        for (int i = 1; i <= numOrders; i++) {
                            Porder additionalPorder = new Porder();
                            String porderNo = generatePorderNumber();
                            additionalPorder.setPorderNo(porderNo);
                            additionalPorder.setEmergencyYn("N");
                            additionalPorder.setIngredientName(porderIngredientId);
                            additionalPorder.setSupplierId("A콜라겐회사");

                            //발주수량정하기
                            if (i == numOrders) {
                                if (remainingQuantity >= 500) {
                                    additionalPorder.setPorderQuantity(remainingQuantity);
                                } else if (remainingQuantity >= 5 && remainingQuantity < 500) {
                                    System.out.println("여기여기 : " + remainingQuantity);
                                    System.out.println("여기여기 : " + (orderCollagen - haveBox));
                                    additionalPorder.setPorderQuantity((int) (Math.ceil(remainingQuantity / 5.0) * 5));
                                    System.out.println("여기여기 : " + additionalPorder.getPorderQuantity());
                                }
                            } else {
                                additionalPorder.setPorderQuantity(500);
                                remainingQuantity -= 500;
                            }
                            porderRepository.save(additionalPorder);
                            porderRepository.flush();
                            // 추가 발주 객체(porder)를 처리하는 로직 작성
                            // 예를 들어, 추가 발주 객체를 저장하는 리스트에 추가하거나 데이터베이스에 저장하는 등의 처리를 수행
                        }
                    }

                    System.out.println(porder.getPorderQuantity());

                    porder.setSupplierId("A콜라겐회사");

                    //콜라겐 자재량빼기
                    IngredientStock ingredientStock2 = new IngredientStock();
                    String pId = ingredientsRepository.findCollagenIdByProductId(productId);
                    System.out.println("pid : " + pId);
                    ingredientStockRepository.decreaseStock1Quantity(pId, haveCollagen);
                    porderRepository.save(porder);
                    porderRepository.flush();
                }else{
                    //needCollagen 값만큼 콜라겐 자재량 빼기
                    IngredientStock ingredientStock3 = new IngredientStock();
                    String pId = ingredientsRepository.findCollagenIdByProductId(productId);
                    System.out.println("pid : " + pId);
                    ingredientStockRepository.decreaseStock1Quantity(pId, haveCollagen);
                    if(ingredientStock3.getQuantity() == 0){
                        ingredientStock3.setQuantity(0);
                    }
                }
            }
            if("매실젤리스틱".equals(productRepository.findProductNameByProductId(productId))){
                String ingredientId = "I008";
                int haveBox = ingredientStockRepository.findBoxNumByProductId(ingredientId);
                if(boxneed > haveBox) {

                    //박스 최소주문량 적용
                    System.out.println("booxNeed1 : "+boxneed);
                    System.out.println("ingredientStock1 : " +haveBox);
                    int orderBox = boxneed - haveBox;

                    //필요한 만큼 발주관리에 insert
                    Porder porder = new Porder();
                    String Porderno = generatePorderNumber();
                    porder.setPorderNo(Porderno);
                    porder.setEmergencyYn("N");
                    String porderIngredientId = ingredientsRepository.findBoxConcentrateByProductId(productId);
                    porder.setIngredientName(porderIngredientId);
                    if(orderBox<500){
                        porder.setPorderQuantity(500);
                    }else if(orderBox >= 500 && orderBox <= 10000){
                        int porderBox = (int) (Math.ceil(orderBox/500)*500.0);
                        porder.setPorderQuantity(porderBox);
                    }else if(orderBox > 10000){
                        porder.setPorderQuantity(10000);
                        int remainingQuantity = orderBox - 10000; // 남은 수량 계산
                        int numAdditionalOrders = (int) Math.ceil(remainingQuantity / 10000.0); // 추가 발주 횟수 계산

                        for (int i = 1; i <= numAdditionalOrders; i++) {
                            Porder additionalPorder = new Porder();
                            String porderNo = generatePorderNumber();
                            additionalPorder.setPorderNo(porderNo);
                            additionalPorder.setEmergencyYn("N");
                            additionalPorder.setIngredientName(porderIngredientId);
                            additionalPorder.setSupplierId("A포장회사");

                            //발주수량정하기
                            if (i == numAdditionalOrders) {
                                if(remainingQuantity >= 10000) {
                                    additionalPorder.setPorderQuantity(remainingQuantity);
                                }else if(remainingQuantity >= 500 && remainingQuantity < 10000){
                                    System.out.println("여기여기 : "+remainingQuantity);
                                    System.out.println("여기여기 : "+(orderBox-haveBox));
                                    additionalPorder.setPorderQuantity((int)(Math.ceil(remainingQuantity/500.0)*500));
                                    System.out.println("여기여기 : "+additionalPorder.getPorderQuantity());
                                }
                            } else {
                                additionalPorder.setPorderQuantity(10000);
                                remainingQuantity -= 10000;
                            }
                            porderRepository.save(additionalPorder);
                            porderRepository.flush();
                            // 추가 발주 객체(porder)를 처리하는 로직 작성
                            // 예를 들어, 추가 발주 객체를 저장하는 리스트에 추가하거나 데이터베이스에 저장하는 등의 처리를 수행
                        }
                    }

                    System.out.println(porder.getPorderQuantity());

                    porder.setSupplierId("A포장회사");

                    //박스 자재량 빼기
                    IngredientStock ingredientStock = ingredientStockRepository.findBoxConcentrateByProductId(ingredientId);
                    int haveMaterials = ingredientStock.getQuantity();
                    System.out.println("박스 재고량 : " + haveMaterials + "kg");
                    String pId = ingredientsRepository.findBoxIdByProductId(productId);
                    System.out.println("Pid : "+pId);
                    System.out.println("야야야ㅑ : "+ingredientStock.getQuantity());
                    if(ingredientStock.getQuantity() == 0){
                        ingredientStock.setQuantity(0);
                    }else if(ingredientStock.getQuantity() > 0){
                        System.out.println("여기용ㅇㅇㅇㅇㅇㅇㅇ");
                        System.out.println("pid : "+ pId);
                        System.out.println("haveBox : "+ haveMaterials);
                        ingredientStockRepository.decreaseStock1Quantity(pId, haveMaterials);
                    }

                    //ingredientStockRepository.decreaseStockQuantity(pId, boxneed);
                    porderRepository.save(porder);
                    porderRepository.flush();
                }else{
                    //boxneed값만큼 박스자재량 빼기
                    IngredientStock ingredientStock = ingredientStockRepository.findBoxConcentrateByProductId(ingredientId);
                    int haveMaterials = ingredientStock.getQuantity();
                    String pId = ingredientsRepository.findBoxIdByProductId(productId);
                    ingredientStockRepository.decreaseStock1Quantity(pId, haveMaterials);
                    if(ingredientStock.getQuantity() == 0){
                        ingredientStock.setQuantity(0);
                    }
                }

                String ingredientId1 = "I007";
                int haveStickPouch = ingredientStockRepository.findStickPouchNumByProductId(ingredientId1);

                if(stickpouch > haveStickPouch) {
                    //스틱파우치 최소주문량 적용
                    int needPouch = (int)Math.ceil((stickpouch - haveStickPouch) /1000.0)*1000;
                    int orderStickPouch = stickpouch - haveStickPouch;
                    //필요한 만큼 발주
                    Porder porder = new Porder();
                    String Porderno = generatePorderNumber();
                    porder.setPorderNo(Porderno);
                    porder.setEmergencyYn("N");
                    String porderIngredientId = ingredientsRepository.findStickPouchConcentrateByProductId(productId);
                    porder.setIngredientName(porderIngredientId);
                    if(orderStickPouch<1000){
                        porder.setPorderQuantity(1000);
                    }else if(orderStickPouch >= 1000 && orderStickPouch <= 1000000){
                        porder.setPorderQuantity(needPouch);
                    }else if(orderStickPouch > 1000000){
                        porder.setPorderQuantity(1000000);
                        int remainingQuantity = orderStickPouch-1000000;
                        int numOrders = (int)Math.ceil(remainingQuantity / 1000000.0); // 추가 발주 횟수 계산

                        for (int i = 1; i <= numOrders; i++) {
                            Porder additionalPorder = new Porder();
                            String porderNo = generatePorderNumber();
                            additionalPorder.setPorderNo(porderNo);
                            additionalPorder.setEmergencyYn("N");
                            additionalPorder.setIngredientName(porderIngredientId);
                            additionalPorder.setSupplierId("A스틱파우치회사");

                            //발주수량정하기
                            if (i == numOrders) {
                                if (remainingQuantity >= 1000000) {
                                    additionalPorder.setPorderQuantity(remainingQuantity);
                                } else if (remainingQuantity >= 1000 && remainingQuantity < 1000000) {
                                    System.out.println("여기여기 : " + remainingQuantity);
                                    System.out.println("여기여기 : " + (orderStickPouch - haveBox));
                                    additionalPorder.setPorderQuantity((int) (Math.ceil(remainingQuantity / 1000.0) * 1000));
                                    System.out.println("여기여기 : " + additionalPorder.getPorderQuantity());
                                }
                            } else {
                                additionalPorder.setPorderQuantity(1000000);
                                remainingQuantity -= 1000000;
                            }
                            porderRepository.save(additionalPorder);
                            porderRepository.flush();
                            // 추가 발주 객체(porder)를 처리하는 로직 작성
                            // 예를 들어, 추가 발주 객체를 저장하는 리스트에 추가하거나 데이터베이스에 저장하는 등의 처리를 수행
                        }
                    }
                    System.out.println(porder.getPorderQuantity());

                    porder.setSupplierId("A스틱파우치회사");
                    //스틱파우치 자재량 빼기
                    IngredientStock ingredientStock2 = new IngredientStock();
                    String pId = ingredientsRepository.findStickPouchIdByProductId(productId);
                    System.out.println("pid : " + pId);
                    ingredientStockRepository.decreaseStock1Quantity(pId, haveStickPouch);
                    porderRepository.save(porder);
                    porderRepository.flush();
                }else{
                    //stickpouch값만큼 스틱파우치자재량 빼기
                    IngredientStock ingredientStock3 = new IngredientStock();
                    String pId = ingredientsRepository.findStickPouchIdByProductId(productId);
                    System.out.println("pid : " + pId);
                    ingredientStockRepository.decreaseStock1Quantity(pId, haveStickPouch);
                    if(ingredientStock3.getQuantity() == 0){
                        ingredientStock3.setQuantity(0);
                    }
                }

                String ingredientId2 = "I005";
                int haveCollagen = ingredientStockRepository.findCollagenNumByProductId(ingredientId2);

                if(needCollagen > haveCollagen){
                    //콜라겐 최소주문량 적용
                    int needColla = (int)Math.ceil((needCollagen - haveCollagen) /5.0)*5;
                    int orderCollagen = needCollagen - haveCollagen;
                    //필요한 만큼 발주
                    Porder porder = new Porder();
                    String Porderno = generatePorderNumber();
                    porder.setPorderNo(Porderno);
                    porder.setEmergencyYn("N");
                    String porderIngredientId = ingredientsRepository.findCollagenConcentrateByProductId(productId);
                    porder.setIngredientName(porderIngredientId);
                    if(orderCollagen<5){
                        porder.setPorderQuantity(5);
                    }else if(orderCollagen >= 5 && orderCollagen <= 500){
                        porder.setPorderQuantity(needColla);
                    }else if(orderCollagen > 500){
                        porder.setPorderQuantity(500);
                        int remainingQuantity = orderCollagen-500;
                        int numOrders = (int)Math.ceil(remainingQuantity / 500.0); // 추가 발주 횟수 계산

                        for (int i = 1; i <= numOrders; i++) {
                            Porder additionalPorder = new Porder();
                            String porderNo = generatePorderNumber();
                            additionalPorder.setPorderNo(porderNo);
                            additionalPorder.setEmergencyYn("N");
                            additionalPorder.setIngredientName(porderIngredientId);
                            additionalPorder.setSupplierId("A콜라겐회사");

                            //발주수량정하기
                            if (i == numOrders) {
                                if (remainingQuantity >= 500) {
                                    additionalPorder.setPorderQuantity(remainingQuantity);
                                } else if (remainingQuantity >= 5 && remainingQuantity < 500) {
                                    System.out.println("여기여기 : " + remainingQuantity);
                                    System.out.println("여기여기 : " + (orderCollagen - haveBox));
                                    additionalPorder.setPorderQuantity((int) (Math.ceil(remainingQuantity / 5.0) * 5));
                                    System.out.println("여기여기 : " + additionalPorder.getPorderQuantity());
                                }
                            } else {
                                additionalPorder.setPorderQuantity(500);
                                remainingQuantity -= 500;
                            }
                            porderRepository.save(additionalPorder);
                            porderRepository.flush();
                            // 추가 발주 객체(porder)를 처리하는 로직 작성
                            // 예를 들어, 추가 발주 객체를 저장하는 리스트에 추가하거나 데이터베이스에 저장하는 등의 처리를 수행
                        }
                    }
                    System.out.println(porder.getPorderQuantity());

                    porder.setSupplierId("A콜라겐회사");
                    //콜라겐 자재량빼기
                    IngredientStock ingredientStock2 = new IngredientStock();
                    String pId = ingredientsRepository.findCollagenIdByProductId(productId);
                    System.out.println("pid : " + pId);
                    ingredientStockRepository.decreaseStock1Quantity(pId, haveCollagen);
                    porderRepository.save(porder);
                    porderRepository.flush();
                }else{
                    //needCollagen 값만큼 콜라겐 자재량 빼기
                    IngredientStock ingredientStock3 = new IngredientStock();
                    String pId = ingredientsRepository.findCollagenIdByProductId(productId);
                    System.out.println("pid : " + pId);
                    ingredientStockRepository.decreaseStock1Quantity(pId, haveCollagen);
                    if(ingredientStock3.getQuantity() == 0){
                        ingredientStock3.setQuantity(0);
                    }
                }
            }



            System.out.println("수주수량 : " + needMaterials + "kg");

            // 재고관리 테이블에서 해당 원자재명에 대한 재고 수량 가져오기
            IngredientStock ingredientStock = ingredientStockRepository.findByProductId(productId);
            int haveMaterials = ingredientStock.getQuantity();
            System.out.println("원자재 재고량 : " + haveMaterials + "kg");
            System.out.println("중요 : "+ ingredientStock.getIngredientId());

            //흑마늘,양배추,석류농축액,매실농축액
            // 발주량 계산
            int orderMaterials = needMaterials - haveMaterials; // 500 - 100 = 400(ordermaterials), 100 - 500 = -400(ordermaterials)
            System.out.println("발주량 : " + orderMaterials + "kg");

            if (needMaterials > haveMaterials) {
                // 발주 관리 테이블에 insert
                System.out.println("----------여기--------");
                Porder porder = new Porder();
                String porderno = generatePorderNumber();
                porder.setPorderNo(porderno);
                porder.setEmergencyYn("N");

                if("양배추즙".equals(productRepository.findProductNameByProductId(productId))) {
                    String porderproductId = ingredientsRepository.findCabbageNameByProductId(productId);
                    porder.setIngredientName(porderproductId);
                    porder.setSupplierId("양배추농장");
                }else if("석류젤리스틱".equals(productRepository.findProductNameByProductId(productId))) {
                    String porderproductId = ingredientsRepository.findRaspberryConcentrateByProductId(productId);
                    porder.setIngredientName(porderproductId);
                    porder.setSupplierId("석류농장");
                }else if("매실젤리스틱".equals(productRepository.findProductNameByProductId(productId))){
                    String porderproductId = ingredientsRepository.findplumConcentrateByProductId(productId);
                    porder.setIngredientName(porderproductId);
                    porder.setSupplierId("매실농장");
                }else if("흑마늘즙".equals(productRepository.findProductNameByProductId(productId))){
                    String porderproductId = ingredientsRepository.findBlackNameByProductId(productId);
                    porder.setIngredientName(porderproductId);
                    porder.setSupplierId("흑마늘농장");
                }

                //porder.setIngredient_id(porderproductId);
                System.out.println("제품명!!!! = "+productRepository.findProductNameByProductId(productId));
                //최소주문량 계산해서 적용하기(해야함)
                if("양배추즙".equals(productRepository.findProductNameByProductId(productId))){
                    System.out.println("in-양배추즙");
                    if(orderMaterials < 1000){
                        System.out.println("in-발주량 1000미만");
                        porder.setPorderQuantity(1000);
                    }else if(orderMaterials >= 1000 && orderMaterials <= 5000) {
                        porder.setPorderQuantity(((int) Math.ceil(orderMaterials/1000.0)*1000));
                    }else if(orderMaterials > 5000){
                        // 5000kg 선발주 처리
                        porder.setPorderQuantity(5000);
                        int remainingOrderQuantity = orderMaterials - 5000;
                        int numOrders = (int) Math.ceil(remainingOrderQuantity / 5000.0);
                        // 2차 발주 정보를 생성하여 발주 테이블에 저장
                        for (int i = 1; i <= numOrders; i++) {
                            Porder additionalPorder = new Porder();
                            String porderNo = generatePorderNumber();
                            additionalPorder.setPorderNo(porderNo);
                            additionalPorder.setEmergencyYn("N");
                            String porderproductId1 = ingredientsRepository.findCabbageNameByProductId(productId);
                            additionalPorder.setIngredientName(porderproductId1);
                            additionalPorder.setSupplierId("양배추농장");

                            //발주수량정하기
                            if (i == numOrders) {
                                if(remainingOrderQuantity >= 5000) {
                                    System.out.println("여기 왔찌용 : "+remainingOrderQuantity);
                                    additionalPorder.setPorderQuantity(remainingOrderQuantity);
                                }else if(remainingOrderQuantity >= 1000 && remainingOrderQuantity < 5000){
                                    System.out.println("여기여기 : "+remainingOrderQuantity);
                                    //System.out.println("여기여기 : "+(orderBox-haveBox));
                                    additionalPorder.setPorderQuantity((int)(Math.ceil(remainingOrderQuantity/1000.0)*1000));
                                    System.out.println("여기여기 : "+additionalPorder.getPorderQuantity());
                                }
                            } else {
                                additionalPorder.setPorderQuantity(5000);
                                remainingOrderQuantity -= 5000;
                            }
                            porderRepository.save(additionalPorder);
                            porderRepository.flush();
                            // 추가 발주 객체(porder)를 처리하는 로직 작성
                            // 예를 들어, 추가 발주 객체를 저장하는 리스트에 추가하거나 데이터베이스에 저장하는 등의 처리를 수행
                        }
                    }
//                        Porder secondPorder = new Porder();
//                        String secondPorderno = generatePorderNumber();
//                        secondPorder.setPorder_no(secondPorderno);
//                        String porderproductId1 = ingredientsRepository.findCabbageNameByProductId(productId);
//                        secondPorder.setIngredient_id(porderproductId1);
//                        secondPorder.setPorder_quantity(((int) Math.ceil(remainingOrderQuantity/1000.0)*1000));
//                        // 납품예정일(계산) 설정
//                        // 예를 들어, 14일 후로 설정하는 경우:
//                        Calendar cal = Calendar.getInstance();
//                        cal.add(Calendar.DATE, 14);
//                        Date secondPorderDate = cal.getTime();
//                        // secondPorder.setPorder_date(secondPorderDate);
//                        porderRepository.save(secondPorder);
//                        porderRepository.flush();
                }

                if("흑마늘즙".equals(productRepository.findProductNameByProductId(productId))){
                    System.out.println("in-흑마늘즙");
                    if(orderMaterials < 10){
                        System.out.println("in-발주량 10미만");
                        porder.setPorderQuantity(10);
                    }else if(orderMaterials >= 10 && orderMaterials <= 5000) {
                        porder.setPorderQuantity(((int) Math.ceil(orderMaterials/10.0)*10));
                    }else if(orderMaterials > 5000){
                        // 5000kg 선발주 처리
                        porder.setPorderQuantity(5000);
                        int remainingOrderQuantity = orderMaterials - 5000;
                        // 2차 발주 정보를 생성하여 발주 테이블에 저장
                        int numOrders = (int) Math.ceil(remainingOrderQuantity / 5000.0);
                        // 2차 발주 정보를 생성하여 발주 테이블에 저장
                        for (int i = 1; i <= numOrders; i++) {
                            Porder additionalPorder = new Porder();
                            String porderNo = generatePorderNumber();
                            additionalPorder.setPorderNo(porderNo);
                            additionalPorder.setEmergencyYn("N");
                            String porderproductId1 = ingredientsRepository.findBlackNameByProductId(productId);
                            additionalPorder.setIngredientName(porderproductId1);
                            additionalPorder.setSupplierId("흑마늘농장");

                            //발주수량정하기
                            if (i == numOrders) {
                                if(remainingOrderQuantity >= 5000) {
                                    additionalPorder.setPorderQuantity(remainingOrderQuantity);
                                }else if(remainingOrderQuantity >= 10 && remainingOrderQuantity < 5000){
                                    System.out.println("여기여기 : "+remainingOrderQuantity);
                                    //System.out.println("여기여기 : "+(orderBox-haveBox));
                                    additionalPorder.setPorderQuantity((int)(Math.ceil(remainingOrderQuantity/10.0)*10));
                                    System.out.println("여기여기 : "+additionalPorder.getPorderQuantity());
                                }
                            } else {
                                additionalPorder.setPorderQuantity(5000);
                                remainingOrderQuantity -= 5000;
                            }
                            porderRepository.save(additionalPorder);
                            porderRepository.flush();
                            // 추가 발주 객체(porder)를 처리하는 로직 작성
                            // 예를 들어, 추가 발주 객체를 저장하는 리스트에 추가하거나 데이터베이스에 저장하는 등의 처리를 수행

                        }
                    }
                }
                if("석류젤리스틱".equals(productRepository.findProductNameByProductId(productId))) {
                    System.out.println("in-석류젤리스틱");
                    if (orderMaterials < 5) {
                        System.out.println("in-발주량 5미만");
                        porder.setPorderQuantity(5);
                    } else if (orderMaterials >= 5 && orderMaterials <= 500) {
                        porder.setPorderQuantity(((int) Math.ceil(orderMaterials / 5.0) * 5));
                    } else if (orderMaterials > 500) {
                        // 5000kg 선발주 처리
                        porder.setPorderQuantity(500);
                        int remainingOrderQuantity = orderMaterials - 500;
                        // 2차 발주 정보를 생성하여 발주 테이블에 저장
                        int numOrders = (int) Math.ceil(remainingOrderQuantity / 500.0);
                        // 2차 발주 정보를 생성하여 발주 테이블에 저장
                        for (int i = 1; i <= numOrders; i++) {
                            Porder additionalPorder = new Porder();
                            String porderNo = generatePorderNumber();
                            additionalPorder.setPorderNo(porderNo);
                            additionalPorder.setEmergencyYn("N");
                            String porderproductId1 = ingredientsRepository.findRasberryNameByProductId(productId);
                            additionalPorder.setIngredientName(porderproductId1);
                            additionalPorder.setSupplierId("석류농장");

                            //발주수량정하기
                            if (i == numOrders) {
                                if (remainingOrderQuantity >= 500) {
                                    additionalPorder.setPorderQuantity(remainingOrderQuantity);
                                } else if (remainingOrderQuantity >= 5 && remainingOrderQuantity < 500) {
                                    System.out.println("여기여기 : " + remainingOrderQuantity);
                                    //System.out.println("여기여기 : "+(orderBox-haveBox));
                                    additionalPorder.setPorderQuantity((int) (Math.ceil(remainingOrderQuantity / 5.0) * 5));
                                    System.out.println("여기여기 : " + additionalPorder.getPorderQuantity());
                                }
                            } else {
                                additionalPorder.setPorderQuantity(500);
                                remainingOrderQuantity -= 500;
                            }
                            porderRepository.save(additionalPorder);
                            porderRepository.flush();
                            // 추가 발주 객체(porder)를 처리하는 로직 작성
                            // 예를 들어, 추가 발주 객체를 저장하는 리스트에 추가하거나 데이터베이스에 저장하는 등의 처리를 수행
                        }
                    }
                }
                if("매실젤리스틱".equals(productRepository.findProductNameByProductId(productId))) {
                    System.out.println("in-매실젤리스틱");
                    if (orderMaterials < 5) {
                        System.out.println("in-발주량 5미만");
                        porder.setPorderQuantity(5);
                    } else if (orderMaterials >= 5 && orderMaterials <= 500) {
                        porder.setPorderQuantity(((int) Math.ceil(orderMaterials / 5.0) * 5));
                    } else if (orderMaterials > 500) {
                        // 5000kg 선발주 처리
                        porder.setPorderQuantity(500);
                        int remainingOrderQuantity = orderMaterials - 500;
                        // 2차 발주 정보를 생성하여 발주 테이블에 저장
                        int numOrders = (int) Math.ceil(remainingOrderQuantity / 500.0);
                        // 2차 발주 정보를 생성하여 발주 테이블에 저장
                        for (int i = 1; i <= numOrders; i++) {
                            Porder additionalPorder = new Porder();
                            String porderNo = generatePorderNumber();
                            additionalPorder.setPorderNo(porderNo);
                            additionalPorder.setEmergencyYn("N");
                            String porderproductId1 = ingredientsRepository.findPlumNameByProductId(productId);
                            additionalPorder.setIngredientName(porderproductId1);
                            additionalPorder.setSupplierId("매실농장");

                            //발주수량정하기
                            if (i == numOrders) {
                                if (remainingOrderQuantity >= 500) {
                                    additionalPorder.setPorderQuantity(remainingOrderQuantity);
                                } else if (remainingOrderQuantity >= 5 && remainingOrderQuantity < 500) {
                                    System.out.println("여기여기 : " + remainingOrderQuantity);
                                    //System.out.println("여기여기 : "+(orderBox-haveBox));
                                    additionalPorder.setPorderQuantity((int) (Math.ceil(remainingOrderQuantity / 5.0) * 5));
                                    System.out.println("여기여기 : " + additionalPorder.getPorderQuantity());
                                }
                            } else {
                                additionalPorder.setPorderQuantity(500);
                                remainingOrderQuantity -= 500;
                            }
                            porderRepository.save(additionalPorder);
                            porderRepository.flush();
                            // 추가 발주 객체(porder)를 처리하는 로직 작성
                            // 예를 들어, 추가 발주 객체를 저장하는 리스트에 추가하거나 데이터베이스에 저장하는 등의 처리를 수행
                        }
                    }
                }


                // 품목명, 수량(orderMaterials), 납품예정일(계산) 설정
                // 예를 들어, 7일 후로 설정하는 경우:
                Date porderDate = Calendar.getInstance().getTime();
                //porder.setPorder_date(porderDate);
                porderRepository.save(porder);
                porderRepository.flush();


                // 생산계획 테이블에 insert
                ProductionPlan productionPlan = new ProductionPlan();
                IngredientStock ig = new IngredientStock();
                Ingredients ingredients = new Ingredients();
                String planno = generatePlanNumber();
                System.out.println("1차생산 플랜번호 : "+ planno);
                productionPlan.setProdPlanNo(planno);
                productionPlan.setOrderNo(orderNo);

                Date planDate = Calendar.getInstance().getTime();
                //productionPlan.setProdPlan_date(planDate);
                Optional<Integer> maxSeqOptional = Optional.ofNullable(productionPlanRepository.getMaxProdPlanSeqByOrderNo(orderNo));
                int maxSeq = maxSeqOptional.orElse(0);
                int nextSeq = maxSeq + 1;
                productionPlan.setProdPlanSeq(nextSeq);
                productionPlan.setProductId(productId);

                //원자재명 기입
                String productId1 = productionPlan.getProductId();
                System.out.println("찾을려는 제품ID : "+ productId1);
                String productName = productRepository.findProductNameByProductId(productId1);
                System.out.println("찾은 원자재명 : "+ productName);
                productionPlan.setProductName(productName);

                //productionPlan.setProdPlan_quantity(haveMaterials);
                //System.out.println(productionPlan.getProdPlan_quantity());

                // 진행상태(생산계획 테이블 로우에 진행중이 있는 경우 대기 or 없는 경우 진행중으로) 설정
                // 예를 들어, 대기 상태로 설정하는 경우:
                String progressStatus = determineProgressStatus();
                productionPlan.setProdPlanFinYn(progressStatus);
                ig.setProductId(productId);
                //ig.setQuantity(needMaterials);
                System.out.println("haveMaterials : "+haveMaterials);
                // 첫 번째 생산계획의 수량 설정
                if(haveMaterials <= 0){
                    productionPlan.setProdPlanQuantity(orderMaterials);
                    productionPlan.setProdPlanFinYn("발주입고대기중");


                }else if(haveMaterials > 0){
                    productionPlan.setProdPlanQuantity(haveMaterials);
                }
                ingredientStockRepository.decreaseStock1Quantity(ingredientStock.getIngredientId(), productionPlan.getProdPlanQuantity());


                System.out.println(ingredientStock.getQuantity());
                if(ingredientStock.getQuantity() <= 0){
                    System.out.println("1차생산 재고빼기 들어옴");
                    ingredientStock.setQuantity(0);
                    ingredientStockRepository.save(ingredientStock);
                }
                ordersRepository.setOrderStatus(orderNo, "C");
                productionPlanRepository.save(productionPlan);
                productionPlanRepository.flush();

                if(productionPlan.getProdPlanQuantity() < orderMaterials && productionPlan.getProdPlanQuantity() != orderMaterials) {
                    // 두 번째 생산계획의 수량 설정
                    int remainingMaterials = needMaterials - haveMaterials;
                    if (remainingMaterials > 0) {
                        ProductionPlan productionPlan2 = new ProductionPlan();
                        String planno2 = generatePlanNumber();
                        System.out.println("2차생산 플랜번호 : "+ planno2);
                        productionPlan.setProdPlanNo(planno2);
                        productionPlan.setOrderNo(orderNo);
                        //productionPlan.setProdPlan_date(planDate);
                        int nextSeq2 = nextSeq + 1;
                        productionPlan.setProdPlanSeq(nextSeq2);
                        productionPlan.setProductId(productId);
                        //원자재명 기입
                        String productId2 = productionPlan.getProductId();
                        System.out.println("찾을려는 제품ID : "+ productId2);
                        String productName1 = productRepository.findProductNameByProductId(productId2);
                        System.out.println("찾은 원자재명 : "+ productName1);
                        productionPlan.setProductName(productName1);
//                        //원자재명 기입
//                        String productId2 = productionPlan.getProduct_id();
//                        System.out.println("찾을려는 제품ID : "+ productId2);
//                        String productName1 = ingredientsRepository.findIngredientNameByProductId(productId2);
//                        System.out.println("찾은 원자재명 : "+ productName1);
//                        productionPlan.setProduct_name(productName1);
                        productionPlan.setProdPlanQuantity(remainingMaterials);
                        productionPlan.setProdPlanFinYn("발주입고대기중");

                        //원자재 재고량 빼기
                        if (haveMaterials < 0) {
                            ingredientStockRepository.decreaseStock1Quantity(ingredientStock.getIngredientId(), productionPlan.getProdPlanQuantity());

                            if(ingredientStock.getQuantity() <= 0){
                                ingredientStock.setQuantity(0);
                                ingredientStockRepository.save(ingredientStock);

                            }
                        }
                        productionPlanRepository.save(productionPlan);
                        productionPlanRepository.flush();


                    }
                }

            } else {
                // 생산계획 테이블에 insert
                System.out.println("----------여기2--------");
                ProductionPlan productionPlan = new ProductionPlan();
                IngredientStock ig = new IngredientStock();

                //자동채번
                String planno1 = generatePlanNumber();
                productionPlan.setProdPlanNo(planno1);

                productionPlan.setOrderNo(orderNo);
                Date planDate = Calendar.getInstance().getTime();
                //productionPlan.setProdPlan_date(planDate);
                productionPlan.setProductId(productId);

                //원자재명 기입
                String productId2 = productionPlan.getProductId();
                System.out.println("찾을려는 제품ID : "+ productId2);
                String productName1 = productRepository.findProductNameByProductId(productId2);
                System.out.println("찾은 제품명 : "+ productName1);
                productionPlan.setProductName(productName1);
//                //원자재명 기입
//                String productId3 = productionPlan.getProduct_id();
//                System.out.println("찾을려는 제품ID : "+ productId3);
//                String productName2 = ingredientsRepository.findIngredientNameByProductId(productId3);
//                System.out.println("찾은 원자재명 : "+ productName2);
//                productionPlan.setProduct_name(productName2);

                productionPlan.setProdPlanQuantity(needMaterials);
                System.out.println("생산계획수량 : "+productionPlan.getProdPlanQuantity());
                // 진행상태(생산계획 테이블 로우에 진행중이 있는 경우 대기 or 없는 경우 진행중으로) 설정
                // 예를 들어, 대기 상태로 설정하는 경우:
                String progressStatus = determineProgressStatus();
                productionPlan.setProdPlanFinYn(progressStatus);
                // prodplan_seq 할당
                Optional<Integer> maxSeqOptional = Optional.ofNullable(productionPlanRepository.getMaxProdPlanSeqByOrderNo(orderNo));
                int maxSeq = maxSeqOptional.orElse(0);
                int nextSeq = maxSeq + 1;
                productionPlan.setProdPlanSeq(nextSeq);
                ig.setProductId(productId);
                ig.setQuantity(needMaterials);
                //완재품 재고량 빼기
                //finproductRepository.decreaseStockQuantity(finproduct.getProduct_id(), order.getOrder_quantity());
                //원자재 재고량 빼기
                ingredientStockRepository.decreaseStockQuantity(ig.getProductId(), productionPlan.getProdPlanQuantity());
                ordersRepository.setOrderStatus(orderNo,"C");
                productionPlanRepository.save(productionPlan);
                productionPlanRepository.flush();
//                System.out.println("하이 in_포더서비스");
//                LocalDateTime time = porderService.prodPlanDay(planno1);
//                System.out.println("하이 in_포더서비스끝");
//                productionPlan.setExpectInputDate(time);
//                productionPlanRepository.save(productionPlan);
//                productionPlanRepository.flush();
            }
        }else{
            //수주량과 완제품량을 비교해서 완제품량이 충분할 경우 바로 출고 부분
            System.out.println("In-2");
            //완재품 재고량 빼기
            finproductRepository.decreaseStockQuantity(finproduct.getProductId(), order.getOrderQuantity());
            ordersRepository.setOrderStatus(orderNo,"C");
            Shipment shipment = new Shipment();
            Date planDate = Calendar.getInstance().getTime();
            //shipment.setShipmentDate(planDate);
            shipment.setShipmentStatus("Y");
            shipment.setCompanyName(order.getCompanyId());
            shipment.setProductId(productId);
            //자동채번
            String shipno = generateShipNumber();
            shipment.setShipmentNo(shipno);
            shipment.setOrderNo(orderNo);
            shipment.setShipmentQuantity(order.getOrderQuantity());
            int outquantity = shipment.getShipmentQuantity();
            order.setOrderStatus("납품완료");
            shipmentRepository.save(shipment);
            shipmentRepository.flush();

            System.out.println("완재품 재고량(box) :" + haveFinquantity);
            System.out.println("출하량(box) :" + outquantity);
            System.out.println("완제품량이 충분하므로 바로 출고");
        }
    }



//    private String determineProgressStatus(String productId) {
//        // 생산계획 테이블에서 해당 제품의 진행중인 로우를 조회하는 쿼리를 실행
//        List<ProductionPlan> productionPlans = productionPlanRepository.findByProductIdAndProdPlanFinYn(productId, "진행중");
//
//        if (productionPlans.isEmpty()) {
//            return "진행중"; // 진행 중인 로우가 없는 경우 "진행중" 상태 반환
//        } else {
//            return "대기"; // 진행 중인 로우가 있는 경우 "대기" 상태 반환
//        }
//    }

    private String determineProgressStatus() {
        // 생산계획 테이블에서 모든 로우를 조회
        List<ProductionPlan> productionPlans = productionPlanRepository.findAll();

        for (ProductionPlan productionPlan : productionPlans) {
            if ("진행중".equals(productionPlan.getProdPlanFinYn())) {
                return "대기"; // "진행중"인 로우가 하나라도 있으면 "대기" 상태 반환
            }
        }
        return "진행중"; // "진행중"인 로우가 없는 경우 "진행중" 상태 반환
    }

    @Scheduled(cron = "*/10 * * * * ?") // 30초 마다 실행
    public void processOrdersAutomatically() {
        List<Orders> orders = ordersRepository.findByOrderStatus("B");
        if (orders != null && !orders.isEmpty()) {
            for (Orders order : orders) {
                int orderQuantity = order.getOrderQuantity();
                String orderNo = order.getOrderNo();
                processOrder(orderNo);
                // 주문 처리 후 상태를 "C"로 변경
//                ordersRepository.setOrderStatus(orderNo,"C");
//                ordersRepository.save(order);


//                int orderQuantity = order.getOrder_quantity();
//                String orderNo = order.getOrder_no();
//                int needMaterials = (orderQuantity * 30) / 20;
//
//                ProductionPlan productionPlan = productionPlanRepository.findByOrderNo(orderNo);
//                IngredientStock ig = new IngredientStock();
//
//                if (productionPlan == null || productionPlan.getProdPlan_quantity() != needMaterials) {
//                    processOrder(orderNo);
//                }else if(ig.getQuantity() < needMaterials){
//                    System.out.println("=====================");
//                    System.out.println("재고수량을 확인하세요!!!");
//                    System.out.println("=====================");
//
//                }

            }
        }
    }

    private static int sequence = 1;
    private static int sequence1 = 1;
    private static int sequence2 = 1;

    public String generatePlanNumber() {
        // 현재 시간 정보를 가져옵니다.
        LocalDateTime now = LocalDateTime.now();
        // 형식 지정을 위한 DateTimeFormatter를 생성합니다.
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        // 현재 시간 정보를 "yyyyMMdd" 형식의 문자열로 변환합니다.
        String formattedDate = now.format(formatter);
        // 시퀀스 값을 문자열로 변환합니다.
        String formattedSequence = String.format("%03d", sequence);

        List<String> no = productionPlanRepository.findByPlanNo1();
        for(int i=0;i<no.size();i++){
            if(("PP"+formattedDate+formattedSequence).equals(no.get(i))){
                int incrementedValue = Integer.parseInt(formattedSequence) + 1;
                formattedSequence = String.format("%03d", incrementedValue);
            }
        }

        // 시퀀스 값을 1 증가시킵니다.
        sequence++;
        // 생산계획번호를 조합하여 반환합니다.
        return "PP" + formattedDate + formattedSequence;
    }

    public String generatePorderNumber() {
        // 현재 시간 정보를 가져옵니다.
        LocalDateTime now = LocalDateTime.now();
        // 형식 지정을 위한 DateTimeFormatter를 생성합니다.
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        // 현재 시간 정보를 "yyyyMMdd" 형식의 문자열로 변환합니다.
        String formattedDate = now.format(formatter);
        // 시퀀스 값을 문자열로 변환합니다.
        String formattedSequence = String.format("%03d", sequence1);

        List<String> no = porderRepository.findByPlanNo1();
        for(int i=0;i<no.size();i++){
            if(("PD"+formattedDate+formattedSequence).equals(no.get(i))){
                int incrementedValue = Integer.parseInt(formattedSequence) + 1;
                formattedSequence = String.format("%03d", incrementedValue);
            }
        }



        // 시퀀스 값을 1 증가시킵니다.
        sequence1++;
        // 생산계획번호를 조합하여 반환합니다.
        return "PD" + formattedDate + formattedSequence;
    }

    public String generateShipNumber() {
        // 현재 시간 정보를 가져옵니다.
        LocalDateTime now = LocalDateTime.now();
        // 형식 지정을 위한 DateTimeFormatter를 생성합니다.
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        // 현재 시간 정보를 "yyyyMMdd" 형식의 문자열로 변환합니다.
        String formattedDate = now.format(formatter);
        // 시퀀스 값을 문자열로 변환합니다.
        String formattedSequence = String.format("%03d", sequence2);


        List<String> no = shipmentRepository.findByPlanNo1();
        for(int i=0;i<no.size();i++){
            if(("SM"+formattedDate+formattedSequence).equals(no.get(i))){
                int incrementedValue = Integer.parseInt(formattedSequence) + 1;
                formattedSequence = String.format("%03d", incrementedValue);
            }
        }


        // 시퀀스 값을 1 증가시킵니다.
        sequence2++;
        // 생산계획번호를 조합하여 반환합니다.
        return "SM" + formattedDate + formattedSequence;
    }

    //현화
    public List<ProductionPlan> selectList(){
        return productionPlanRepository.findAll();
    }
    public void prodPlanService(ProdPlanRepository prodPlanRepository){
        this.prodPlanRepository = prodPlanRepository;
    }


    public List<ProductionPlan> findSearch(LocalDate startDate, LocalDate endDate, String prodPlanFinYn, String productName) {
        Date start = java.sql.Date.valueOf(startDate);
        Date end = java.sql.Date.valueOf(endDate);
        return prodPlanRepository.findSearch(start, end, prodPlanFinYn, productName);
    }
}

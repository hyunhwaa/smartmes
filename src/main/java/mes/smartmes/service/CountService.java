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
public class CountService {

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
    public CountService(OrdersRepository ordersRepository, IngredientStockRepository ingredientStockRepository,
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

    public int processOrder(String orderNo) {

        Orders order = ordersRepository.findByOrderNo(orderNo);
        String productId = order.getProductId();
        int orderQuantity = order.getOrderQuantity();
        Finproduct finproduct = finproductRepository.findByProductId(productId);
        int haveFinquantity = finproduct.getFinProductQuantity();
        int productQuantity = orderQuantity - haveFinquantity;
        System.out.println("완재품 재고량(box) :" + haveFinquantity);
        System.out.println("생산해야할 제품(box)양 : " + productQuantity);
        System.out.println("수주량(box) : " + orderQuantity);



            int needMaterials = 0;
            if ("양배추즙".equals(productRepository.findProductNameByProductId(productId))) {
                needMaterials = (productQuantity * 30) / 20;
            } else if ("흑마늘즙".equals(productRepository.findProductNameByProductId(productId))) {
                needMaterials = (productQuantity * 30) / 120;
            } else if ("매실젤리스틱".equals(productRepository.findProductNameByProductId(productId)) || "석류젤리스틱".equals(productRepository.findProductNameByProductId(productId))) {
                needMaterials = (productQuantity * 25 * 5) / 1000;
            }

            //필요한 자재(박스,파우치,스틱파우치,콜라겐) 확인(부족시 발주)
            int boxneed = productQuantity;
            int stickpouch = productQuantity * 30;
            int pouch = productQuantity * 30;
            int needCollagen = (productQuantity * 30) / 1000;


        return orderQuantity;
    }

    public int needCabbage(String orderNo) {

        Orders order = ordersRepository.findByOrderNo(orderNo);
        String productId = order.getProductId();
        int orderQuantity = order.getOrderQuantity();
        Finproduct finproduct = finproductRepository.findByProductId(productId);
        int haveFinquantity = finproduct.getFinProductQuantity();
        int productQuantity = orderQuantity - haveFinquantity;
        System.out.println("완재품 재고량(box) :" + haveFinquantity);
        System.out.println("생산해야할 제품(box)양 : " + productQuantity);
        System.out.println("수주량(box) : " + orderQuantity);

        int needMaterials = 0;
        if ("양배추즙".equals(productRepository.findProductNameByProductId(productId))) {
            needMaterials = (orderQuantity * 30) / 20;
        }

        return needMaterials;
    }

    public int needBlackGaric(String orderNo) {

        Orders order = ordersRepository.findByOrderNo(orderNo);
        String productId = order.getProductId();
        int orderQuantity = order.getOrderQuantity();
        Finproduct finproduct = finproductRepository.findByProductId(productId);
        int haveFinquantity = finproduct.getFinProductQuantity();
        int productQuantity = orderQuantity - haveFinquantity;
        System.out.println("완재품 재고량(box) :" + haveFinquantity);
        System.out.println("생산해야할 제품(box)양 : " + productQuantity);
        System.out.println("수주량(box) : " + orderQuantity);



        int needMaterials = 0;
        if ("흑마늘즙".equals(productRepository.findProductNameByProductId(productId))) {
            needMaterials = (orderQuantity * 30) / 120;
        }

        return needMaterials;
    }

    public int suckryuMaesilc(String orderNo) {

        Orders order = ordersRepository.findByOrderNo(orderNo);
        String productId = order.getProductId();
        int orderQuantity = order.getOrderQuantity();
        Finproduct finproduct = finproductRepository.findByProductId(productId);
        int haveFinquantity = finproduct.getFinProductQuantity();
        int productQuantity = orderQuantity - haveFinquantity;
        System.out.println("완재품 재고량(box) :" + haveFinquantity);
        System.out.println("생산해야할 제품(box)양 : " + productQuantity);
        System.out.println("수주량(box) : " + orderQuantity);



        int needMaterials = 0;
        if ("매실젤리스틱".equals(productRepository.findProductNameByProductId(productId)) || "석류젤리스틱".equals(productRepository.findProductNameByProductId(productId))) {
            needMaterials = (orderQuantity * 25 * 5) / 1000;
        }

        return needMaterials;
    }

    public int needBox(String orderNo) {

        Orders order = ordersRepository.findByOrderNo(orderNo);
        String productId = order.getProductId();
        int orderQuantity = order.getOrderQuantity();
        Finproduct finproduct = finproductRepository.findByProductId(productId);
        int haveFinquantity = finproduct.getFinProductQuantity();
        int productQuantity = orderQuantity - haveFinquantity;
        System.out.println("완재품 재고량(box) :" + haveFinquantity);
        System.out.println("생산해야할 제품(box)양 : " + productQuantity);
        System.out.println("수주량(box) : " + orderQuantity);



        int boxneed = orderQuantity;

        return boxneed;
    }

    public int needPouch(String orderNo) {

        Orders order = ordersRepository.findByOrderNo(orderNo);
        String productId = order.getProductId();
        int orderQuantity = order.getOrderQuantity();
        Finproduct finproduct = finproductRepository.findByProductId(productId);
        int haveFinquantity = finproduct.getFinProductQuantity();
        int productQuantity = orderQuantity - haveFinquantity;
        System.out.println("완재품 재고량(box) :" + haveFinquantity);
        System.out.println("생산해야할 제품(box)양 : " + productQuantity);
        System.out.println("수주량(box) : " + orderQuantity);



        int pouchneed = orderQuantity*30*1;

        return pouchneed;
    }

    public int needStickPouch(String orderNo) {

        Orders order = ordersRepository.findByOrderNo(orderNo);
        String productId = order.getProductId();
        int orderQuantity = order.getOrderQuantity();
        Finproduct finproduct = finproductRepository.findByProductId(productId);
        int haveFinquantity = finproduct.getFinProductQuantity();
        int productQuantity = orderQuantity - haveFinquantity;
        System.out.println("완재품 재고량(box) :" + haveFinquantity);
        System.out.println("생산해야할 제품(box)양 : " + productQuantity);
        System.out.println("수주량(box) : " + orderQuantity);



        int stickpouch = orderQuantity*30*1;

        return stickpouch;
    }

    public int needCollagen(String orderNo) {

        Orders order = ordersRepository.findByOrderNo(orderNo);
        String productId = order.getProductId();
        int orderQuantity = order.getOrderQuantity();
        Finproduct finproduct = finproductRepository.findByProductId(productId);
        int haveFinquantity = finproduct.getFinProductQuantity();
        int productQuantity = orderQuantity - haveFinquantity;
        System.out.println("완재품 재고량(box) :" + haveFinquantity);
        System.out.println("생산해야할 제품(box)양 : " + productQuantity);
        System.out.println("수주량(box) : " + orderQuantity);



        int collagen = orderQuantity*25*2/1000;

        return collagen;
    }


}







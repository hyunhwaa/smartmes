package mes.smartmes.service;

import com.querydsl.core.BooleanBuilder;
import mes.smartmes.entity.*;
import mes.smartmes.repository.FinproductRepository;
import mes.smartmes.repository.OrdersRepository;
import mes.smartmes.repository.ProductionPlanRepository;
import mes.smartmes.repository.ShipmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class ShipmentService {

    private  final ShipmentRepository shipmentRepository;
    private final OrdersRepository ordersRepository;
    private final ProductionPlanRepository productionPlanRepository;
    private final FinproductRepository finproductRepository;

    @Autowired
    public ShipmentService(ShipmentRepository shipmentRepository, OrdersRepository ordersRepository, ProductionPlanRepository productionPlanRepository, FinproductRepository finproductRepository) {
        this.shipmentRepository = shipmentRepository;
        this.ordersRepository = ordersRepository;
        this.productionPlanRepository = productionPlanRepository;
        this.finproductRepository = finproductRepository;
    }

    public List<Shipment> getAllShipments() {
        return shipmentRepository.findAll();

    }



    //조회
    public String selectShipmentNo(){
        String shipmentIntNo = shipmentRepository.findByShipmentNo();
        return  shipmentIntNo;
    }

    //삭제
    public int deleteByShipmentNo(String shipmentNo){

        return shipmentRepository.deleteByShipmentNo(shipmentNo);
    }

    public List<Shipment> selectList() {
        return shipmentRepository.findAll();
    }

    // 다중검색

    @javax.transaction.Transactional
    public List<Shipment> searchShipment(String shipmentNo, LocalDateTime startDate, LocalDateTime endDate, String companyName) {
        QShipment qShipment = QShipment.shipment;
        BooleanBuilder builder = new BooleanBuilder();


        if (shipmentNo != null && shipmentNo != "") {
            builder.and(qShipment.shipmentNo.contains(shipmentNo)); //출하번호
        }


        if (companyName != null && companyName != "") {
            builder.and(qShipment.companyName.contains(companyName)); // 거래처
        }

        if (startDate != null && endDate != null) {
            builder.and(qShipment.shipmentDate.between(startDate, endDate)); // 날짜
        }

        return (List<Shipment>) shipmentRepository.findAll(builder);

//        return (List<Shipment>) shipmentRepository.findAll(builder.getValue());
    }


    //오더 리스트 다중검색
    @javax.transaction.Transactional
    public List<Orders> searchOrders(String orderNo , String productId){
        QOrders qOrders = QOrders.orders;
        BooleanBuilder builder = new BooleanBuilder();

        if(orderNo != null && orderNo != ""){
            builder.and(qOrders.orderNo.contains(orderNo));
        }
        if(productId != null && productId != ""){
            builder.and(qOrders.productId.contains(productId));
        }

        return (List<Orders>) ordersRepository.findAll(builder);

    }

    public void insertShipment(String planNo){

        ProductionPlan pp = productionPlanRepository.findByPlanNo(planNo);
        pp.setProdPlanFinYn("생산완료(출하완료)");

        Shipment sh = new Shipment();
        Orders order = ordersRepository.findByOrderNo(pp.getOrderNo());
        Finproduct fp = finproductRepository.findByProductId(pp.getProductId());

        String no = generateShipNumber();
        sh.setShipmentNo(no);
        sh.setShipmentDate(LocalDateTime.now());
        sh.setShipmentStatus("납품완료");
        sh.setShipmentQuantity((int) Math.ceil(pp.getProdPlanQuantity()/30*20.0));
        sh.setCompanyName(order.getCompanyId());
        sh.setOrderNo(order.getOrderNo());
        sh.setProductId(pp.getProductId());
        int quantity = fp.getFinProductQuantity() - pp.getProdPlanQuantity();
        if (quantity < 0) {
            quantity = 0;
        }
        fp.setFinProductQuantity(quantity);
        List<Shipment> shipments = shipmentRepository.findAllByOrderNoAndShipmentStatus(order.getOrderNo(), "완제품 생산될때까지 출하 대기");
        for (Shipment shipment : shipments) {
            shipment.setShipmentStatus("납품완료1");
        }
        productionPlanRepository.save(pp);
        shipmentRepository.saveAll(shipments);
        shipmentRepository.save(sh);
        shipmentRepository.flush();
    }

    @Scheduled(cron = "*/15 * * * * ?") // 30초 마다 실행
    public void processOrdersAutomatically() {
        List<ProductionPlan> plans = productionPlanRepository.findByProdPlanFinYn("생산완료");
        if (plans != null && !plans.isEmpty()) {
            for (ProductionPlan plan : plans) {
                int planQuantity = plan.getProdPlanQuantity();
                String planNo = plan.getProdPlanNo();
                insertShipment(planNo);
            }
        }

    }

    private static int sequence = 1;



    public String generateShipNumber() {
        // 현재 시간 정보를 가져옵니다.
        LocalDateTime now = LocalDateTime.now();
        // 형식 지정을 위한 DateTimeFormatter를 생성합니다.
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        // 현재 시간 정보를 "yyyyMMdd" 형식의 문자열로 변환합니다.
        String formattedDate = now.format(formatter);
        // 시퀀스 값을 문자열로 변환합니다.
        String formattedSequence = String.format("%03d", sequence);

        List<String> no = shipmentRepository.findshipNo();
        for(int i=0;i<no.size();i++){
            if(("SM"+formattedDate+formattedSequence).equals(no.get(i))){
                int incrementedValue = Integer.parseInt(formattedSequence) + 1;
                formattedSequence = String.format("%03d", incrementedValue);
            }
        }

        // 시퀀스 값을 1 증가시킵니다.
        sequence++;
        // 생산계획번호를 조합하여 반환합니다.
        return "SM" + formattedDate + formattedSequence;
    }





}

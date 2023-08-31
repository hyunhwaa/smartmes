package mes.smartmes.repository;

import mes.smartmes.dto.OrdersDTO;
import mes.smartmes.entity.Orders;
import mes.smartmes.service.OrdersService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.ResourceAccessException;

import javax.transaction.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class OrdersRepositoryTest {

    @Autowired
    private OrdersRepository ordersRepository;
    @Autowired
    private OrdersService ordersService;

    private Orders orders;
    private Orders orders1;

    @Test
    public void test() {
        String[] memoTexts = {"양배추즙", "흑마늘즙", "석류 젤리스틱", "매실 젤리스틱"};
        String expectedPrefix = "OD" + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        IntStream.rangeClosed(1, 50).forEach(i -> {
            Orders orders = Orders.builder()
                .orderNo(expectedPrefix + String.format("%03d", i))
                .companyId(memoTexts[new Random().nextInt(memoTexts.length)])
                .build();
            ordersRepository.save(orders);
            Assertions.assertEquals(expectedPrefix + String.format("%03d", i), orders.getOrderNo());
            Assertions.assertTrue(orders.getOrderNo().startsWith(expectedPrefix));
        });
    }

    @Test
    public void  삭제(){
        try{
        orders = new Orders();
        orders1 = new Orders();


        orders.setOrderNo("하이");
        orders.setCompanyId("CommandA");
        orders.setOrderDate(orders.getOrderDate());
        orders.setProductId("productA");


            orders1.setOrderNo("하이1515");
            orders1.setCompanyId("CommandA1515");
            orders1.setOrderDate(orders.getOrderDate());
            orders1.setProductId("productA1515");

        ordersRepository.save(orders);
        ordersRepository.save(orders1);

        String h = ordersService.selectOrderNo();
        System.out.println("h는는"  + h);

        System.out.println("삭제삭제삭제1" + orders.toString());

//        int s = ordersService.deleteByOrderNo("하이");
//            System.out.println("s는" + s);



        System.out.println("삭제삭제삭제2" + orders.toString());
            System.out.println("s는는" +h);

        String test = ordersService.selectOrderNo();
            System.out.println("테스트" + test);


        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void 저장(){
        String orderId = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        Orders orders = new Orders();
        orders.setOrderNo(orderId);
        ordersRepository.save(orders);

        ordersService.selectOrderNo();

        System.out.println(orders);

    }




/*    @Test
    public void 주문생성() {


        String dayNo = "PD" + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String maxOrderNo = ordersRepository.findMaxOrderNo();

        int orderInt = Integer.parseInt(maxOrderNo.substring(10)) + 1;
        Orders orders = Orders.builder()
                .orderNo(dayNo + String.format("%04d", orderInt))
                .orderDate(LocalDateTime.now())
                .productId("12345")
                .orderQuantity(5)
                .build();
        ordersRepository.save(orders);
    }*/

}

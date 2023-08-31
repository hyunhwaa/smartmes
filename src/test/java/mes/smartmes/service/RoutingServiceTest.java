package mes.smartmes.service;

import net.bytebuddy.asm.Advice;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

@SpringBootTest
public class RoutingServiceTest {

    @Autowired
    private LotService routingService;


    @Test
    public void test(){

        System.out.println(routingService.countTime("P001"));

    }
}

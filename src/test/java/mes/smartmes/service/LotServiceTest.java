package mes.smartmes.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

//@SpringBootTest
public class LotServiceTest {

    //@Autowired
    private LotService lotService;


    //@Test
    public void test(){

        System.out.println(lotService.countTime("P001"));

    }
}

package mes.smartmes.entity;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter @Setter @ToString
public class RealPorderSelect {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // 오토인크리먼트 디비에 맡긴다

    private int realporderid;                  // 자재 입고 번호

    @Column(length = 20)
    private String orderNo;                        // 발주 번호

    @Column(length = 20)
    private String ingredientId;                   // 재료 아이디

    private int inputQuantity;                      // 입고 수량







}


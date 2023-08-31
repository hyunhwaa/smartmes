package mes.smartmes.entity;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "IngredientOutput")
@Getter @Setter @ToString
public class IngredientOutput {


    @Id
    @Column(length = 20)

    private String ingredientOutId;                  // 자재 입고 번호

    @Column(length = 20)
    private String porderNo;                        // 발주 번호

    @Column(length = 20)
    private String orderNo;


    @Column(length = 20, nullable = false)
    private String ingredientId;                   // 재료 아이디


    private int outputQuantity;                      // 입고 수량


    private LocalDateTime outputDate;                    // 입고 날짜





}


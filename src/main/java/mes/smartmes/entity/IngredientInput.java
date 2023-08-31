package mes.smartmes.entity;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "ingredientInput")
@Getter @Setter @ToString
public class IngredientInput {


        @Id
        @Column(length = 20)



        private String ingredientInId;                  // 자재 입고 번호

        @Column(name = "porder_no")
        private String porderNo;                        // 발주 번호


        @Column(length = 20, nullable = false)
        private String ingredientId;                   // 재료 아이디

//        @Column(name ="ingredient_name")
//        private String ingredientName;                // 재료 이름

        @Column(name = "input_quantity")
        private int inputQuantity;                      // 입고 수량

        @Column(name = "input_date")
        private LocalDateTime inputDate;                    // 입고 날짜





}


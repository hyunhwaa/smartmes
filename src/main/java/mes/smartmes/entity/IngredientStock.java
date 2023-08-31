package mes.smartmes.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Date;
import java.time.LocalDate;

@Entity
@Table(name = "ingredientStock")
@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class IngredientStock {

    @Id
    @Column(length = 20, nullable = false)
    private String stockNo;


    @Column(length = 20, nullable = false)
    private String productId;

    @Column(length = 20, nullable = false)
    private String ingredientId;

    @Column(nullable = false)
    private int quantity;

    private Date productDate;

    @Column(name = "input_date")
    private LocalDate inputDate;




    //재고관리 스탁 은 합계만 나타낸다.
    // 총재고량 표시 업데이트



    // 발주하면 입고로 넘어 오고
    // 발주 입고대기가 입고완료로 바뀌고

    // 입고완료로 바뀌는 순간  재고 테이블로 넣기

    // 재고 인에서 전부 재고 스탁으로 옮기기  // 입고에서 총 재고로 옮기고

    // 재고에서 나갈 때 딜리트   // 출하에서 인서트





    //재고관리 스탁 은 합계만 나타낸다.
    // 총재고량 표시 업데이트



    // 발주하면 입고로 넘어 오고
    // 발주 입고대기가 입고완료로 바뀌고

    // 입고완료로 바뀌는 순간  재고 테이블로 넣기

    // 재고 인에서 전부 재고 스탁으로 옮기기  // 입고에서 총 재고로 옮기고

    // 재고에서 나갈 때 딜리트   // 출하에서 인서트







}
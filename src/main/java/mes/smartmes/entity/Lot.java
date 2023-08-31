package mes.smartmes.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "lot")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Lot {


    @Id
    @Column(length = 20)
    private String lotId;                          //로트 id

    //private String ingredientId;                    //재료 id

    @Column(length = 20)
    private String lotPlotNo;                          //직전 공정 로트 번호

    @Column(length = 30)
    private String lotNo;                              //현재 공정 로트 번호

    @Column(length = 20)
    private String processNo;                       //공정 id

    private int inputQuantity;                      //투입량

    private int outputQuantity;                     //산출량

    private int inventoryQuantity;                  //재공재고량(투입량-산출량)

    @Column(length = 20)
    private String productId;                       //제품 id

    private LocalDateTime regDate;                  //등록일시

    //private LocalDateTime modDate;                  //수정일시


}
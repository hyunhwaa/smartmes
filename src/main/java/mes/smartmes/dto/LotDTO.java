package mes.smartmes.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class LotDTO {

    private String lotId;                          //로트 id

    private String ingredientId;                    //재료 id

    private int lotPlotNo;                          //직전 공정 로트 번호

    private int lotNo;                              //현재 공정 로트 번호

    private String processNo;                       //공정 id

    private int inputQuantity;                      //투입량

    private int outputQuantity;                     //산출량

    private int inventoryQuantity;                  //재공재고량(투입량-산출량)

    private String productId;                       //제품 id

    private LocalDateTime regDate;                  //등록일시

    private LocalDateTime modDate;                  //수정일시
}

package mes.smartmes.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Id;
import java.time.LocalDateTime;
@Getter
@Setter
@ToString
public class PorderDTO {

    private  String porderNo;                       // 발주 번호

    private LocalDateTime porderDate;               // 발주 날짜

    private char porderStatus;                      // 발주 주문 상태

    private String ingredientsId;                   // 재료 id

    private int porderQuantity;                     // 주문 수량

    private String supplierId;                      // 공급 업체 id

    private  char emergency_yn;                     // 긴급 요청 여부

}

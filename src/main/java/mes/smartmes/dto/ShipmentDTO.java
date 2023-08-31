package mes.smartmes.dto;

import javax.persistence.Column;
import javax.persistence.Id;
import java.time.LocalDateTime;

public class ShipmentDTO {

    private String shipmentNo;              // 출하 관리 번호

    private String productId;               // 출하 제품 id

    private String companyName;             // 출하 업체 이름

    private LocalDateTime shipmentDate;     // 출하 일

    private int shipmentQuantity;           // 출하수량

    private char shipmentStatus;            // 출하 상태


}

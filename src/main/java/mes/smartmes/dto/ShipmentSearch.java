package mes.smartmes.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ShipmentSearch {
    private String companyName;
    private LocalDate shipmentDate;
    private String shipmentNumber;
    private String item;
    private String supplier;

    // 각 필드의 getter와 setter 메소드
    // 필요에 따라 다른 필드 및 메소드 추가
}

package mes.smartmes.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ProductDTO {

    private String productId;           // 제품 id


    private String division;            // 제품 구분 ( 반제품, 완제품 )

    private String productName;         // 제품명

    private int productPrice;           // 제품 가격

}

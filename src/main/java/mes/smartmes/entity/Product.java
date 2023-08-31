package mes.smartmes.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "product")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @Column(length = 20)
    private String productId;           // 제품 id

    @Column(length = 20)
    private String division;            // 제품 구분 ( 반제품, 완제품 )

    @Column(length =50)
    private String productName;         // 제품명

    private int productPrice;        // 제품 가격


}


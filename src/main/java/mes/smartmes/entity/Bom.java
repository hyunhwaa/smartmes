package mes.smartmes.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "bom")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Bom {

    @Id
    @Column(length = 20)
    private String bomId;                      //bom id

    @Column(length = 20, nullable = false)
    private String productId;                  //제품 id

    @Column(length = 20, nullable = false)
    private long ingredientId;                 //원자재, 부품 id

    private long productQty;                   //생산량

    private long ingreQty;                     //생산량 대비 제품 소요량

    private String ingreQtyUnit;               //소요량 단위


}

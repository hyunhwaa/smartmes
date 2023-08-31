package mes.smartmes.entity;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@EntityListeners(value = {AuditingEntityListener.class})
@Entity
@Table(name = "prod_plan")
@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductionPlan {

    @Id
    @Column(length = 20)
    private String prodPlanNo;

    private int prodPlanSeq;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime prodPlanDate;

    @Column(length = 20, nullable = false)
    private String productId;

    @Column(nullable = false)
    private int prodPlanQuantity;

    @Column(length = 20)
    private String orderNo;

    @Column(length = 20)
    private String prodPlanFinYn;

    @Column(length = 10)
    private String productName;

}
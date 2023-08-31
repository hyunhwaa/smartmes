package mes.smartmes.entity;


import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@EntityListeners(value = {AuditingEntityListener.class})
@Entity
@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "shipment")
public class Shipment {

    @Id
    @Column(length = 20)
    private String shipmentNo;

    @Column(length = 20)
    private String orderNo;

    @Column(length = 20)
    private String productId;

    @Column(length = 50)
    private String companyName;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime shipmentDate;

    private int shipmentQuantity;

    @Column(length = 20)
    private String shipmentStatus;
}

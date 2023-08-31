package mes.smartmes.entity;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@EntityListeners(value = {AuditingEntityListener.class})
@Entity
@Table(name = "porder")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Porder {      // 발주 테이블

    @Id
    @Column(length = 20)
    private String porderNo;                       // 발주 번호

    @Column(length = 20)
    private String ProdPlanNo;

    @Column
    private LocalDateTime thinkInputDate;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime porderDate;               // 발주 날짜


//    @Column(length = 20, nullable= false)
//    @ColumnDefault("입고대기")

    // 입고대기 넣기
    @PrePersist
    public void prePersist() {
        if (porderStatus == null) {
            porderStatus = "입고준비";
        }
    }

    @Column(length = 20)
    private String porderStatus;                      // 발주 주문 상태

//    @Column(length = 20)
//    private String ingredientId;                   // 재료 이름

    @Column(length = 20)
    private String ingredientName;                   // 재료 이름

    private int porderQuantity;                     // 주문 수량

    private int realPorderQuantity;

    @Column(length = 20)
    private String supplierId;                      // 공급 업체 id

    @Column(columnDefinition = "varchar(1) default 'N'" , nullable = false)
    private  String emergencyYn;                     // 긴급 요청 여부


}

package mes.smartmes.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name="prodperform")
@Getter
@Setter
@ToString
public class ProdPerform {

    @Id
    @Column(length = 20)
    private String productionNo;

    private int productionSeq;

    private LocalDateTime productionDate;

    @Column(length = 20)
    private String productionId;

    @Column(length = 20)
    private String processNo;

    private int productionQuantity;

    @Column(length = 20)
    private String workOrderNo;

    private int workOrderSeq;

    @Column(length = 1)
    private String productionStatus;

    @Column(length = 20)
    private String equipmentId;



}

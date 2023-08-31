package mes.smartmes.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@ToString
@Entity
@Table(name = "process")
@NoArgsConstructor
@AllArgsConstructor
public class Process {


    @Id
    @Column(length = 20)
    private String processNO;

    private String processDivision;

    private String processName;

    @Column(length = 20)
    private String productId;

    private long leadTime;

    private long processTime;

    private long processCapacity;

    private String CapacityUnit;
}
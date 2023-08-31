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
@Table(name = "routing")
@NoArgsConstructor
@AllArgsConstructor
public class Routing {

    @Id
    @Column(length = 20)
    private String routingId;

    private String routingName;

    @Column(length = 20)
    private String productId;

    @Column(length = 20)
    private String processNo1;

    @Column(length = 20)
    private String processNo2;

    @Column(length = 20)
    private String processNo3;

    @Column(length = 20)
    private String processNo4;

    @Column(length = 20)
    private String processNo5;

    @Column(length = 20)
    private String processNo6;

    @Column(length = 20)
    private String processNo7;

    @Column(length = 20)
    private String processNo8;

    @Column(length = 20)
    private String processNo9;

    @Column(length = 20)
    private String processNo10;


}
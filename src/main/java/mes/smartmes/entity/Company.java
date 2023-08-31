package mes.smartmes.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "company")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Company {

    @Id
    @Column(length = 20)
    private String companyId;                  //업체 id

    @Column(length = 20, nullable = false)
    private String companyName;                //업체 이름


}

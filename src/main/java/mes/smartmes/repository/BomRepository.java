package mes.smartmes.repository;


import mes.smartmes.entity.Bom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BomRepository extends JpaRepository<Bom,String> {
    List findByProductId(String productId);
}

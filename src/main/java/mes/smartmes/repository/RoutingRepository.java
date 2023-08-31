package mes.smartmes.repository;

import mes.smartmes.entity.ProductionPlan;
import mes.smartmes.entity.Routing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RoutingRepository extends JpaRepository<Routing, String> {

    Routing findByProductId(String productId);

    @Query("SELECT r FROM Routing r WHERE r.productId = :productId")
    ProductionPlan findByPlanNo(String productId);

    List<Routing> findAll();
}

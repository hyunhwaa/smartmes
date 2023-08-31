package mes.smartmes.repository;

import mes.smartmes.entity.ProdPerform;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProdPerformRepository extends JpaRepository<ProdPerform, String> {
    List<ProdPerform> findAll();
}

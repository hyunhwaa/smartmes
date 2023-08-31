package mes.smartmes.repository;

import mes.smartmes.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {

    @Query("select p.productName from Product p where p.productId = :productId")
    String findProductNameByProductId(@Param("productId") String productId);

    List<Product> findAll();

}


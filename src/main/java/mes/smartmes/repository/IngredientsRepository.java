package mes.smartmes.repository;

import mes.smartmes.entity.Ingredients;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IngredientsRepository extends JpaRepository<Ingredients, String> {
    @Query("SELECT i.ingredientName FROM Ingredients i WHERE i.productId = :productId")
    String findIngredientNameByProductId(@Param("productId") String productId);

    @Query("SELECT i.ingredientName FROM Ingredients i WHERE i.productId = :productId AND i.ingredientId = 'I001'")
    String findCabbageNameByProductId(@Param("productId") String productId);

    @Query("SELECT i.ingredientName FROM Ingredients i WHERE i.productId = :productId AND i.ingredientId = 'I002 '")
    String findBlackNameByProductId(@Param("productId") String productId);

    @Query("SELECT i.ingredientName FROM Ingredients i WHERE i.productId = :productId AND i.ingredientId = 'I003'")
    String findRasberryNameByProductId(@Param("productId") String productId);

    @Query("SELECT i.ingredientName FROM Ingredients i WHERE i.productId = :productId AND i.ingredientId = 'I004'")
    String findPlumNameByProductId(@Param("productId") String productId);

    @Query("SELECT i.ingredientName FROM Ingredients i WHERE i.productId = :productId AND i.ingredientId = 'I003'")
    String findRaspberryConcentrateByProductId(@Param("productId") String productId);

    @Query("SELECT i.ingredientName FROM Ingredients i WHERE i.productId = :productId AND i.ingredientId = 'I005'")
    String findCollagenConcentrateByProductId(@Param("productId") String productId);

    @Query("SELECT i.ingredientName FROM Ingredients i WHERE i.productId = :productId AND i.ingredientId = 'I004'")
    String findplumConcentrateByProductId(@Param("productId") String productId);

    @Query("SELECT i.ingredientName FROM Ingredients i WHERE i.productId = :productId AND i.ingredientId = 'I008'")
    String findBoxConcentrateByProductId(@Param("productId") String productId);

    @Query("SELECT i.ingredientName FROM Ingredients i WHERE i.productId = :productId AND i.ingredientId = 'I006'")
    String findPouchConcentrateByProductId(@Param("productId") String productId);

    @Query("SELECT i.ingredientName FROM Ingredients i WHERE i.productId = :productId AND i.ingredientId = 'I007'")
    String findStickPouchConcentrateByProductId(@Param("productId") String productId);

    @Query("SELECT i.ingredientId FROM Ingredients i WHERE i.productId = :productId AND i.ingredientName = '파우치'")
    String findPouchIdByProductId(@Param("productId") String productId);

    @Query("SELECT i.ingredientId FROM Ingredients i WHERE i.productId = :productId AND i.ingredientName = '스틱파우치'")
    String findStickPouchIdByProductId(@Param("productId") String productId);

    @Query("SELECT i.ingredientId FROM Ingredients i WHERE i.productId = :productId AND i.ingredientName = '포장Box'")
    String findBoxIdByProductId(@Param("productId") String productId);

    @Query("SELECT i.ingredientId FROM Ingredients i WHERE i.productId = :productId AND i.ingredientName = '콜라겐'")
    String findCollagenIdByProductId(@Param("productId") String productId);


    List<Ingredients> findAll();




}




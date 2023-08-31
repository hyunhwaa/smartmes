package mes.smartmes.repository;

import mes.smartmes.entity.IngredientOutput;
import mes.smartmes.entity.IngredientStock;
import mes.smartmes.entity.Ingredients;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface IngredientStockRepository extends JpaRepository<IngredientStock, String> {



    // 현일
    @Query("SELECT i FROM IngredientStock i WHERE i.productId = :productId")
    IngredientStock findByProductId(String productId);

    @Query("SELECT i FROM IngredientStock i WHERE i.ingredientId = :ingredientId")
    IngredientStock findByIngId(String ingredientId);

    @Transactional
    @Modifying
    @Query("UPDATE IngredientStock ig SET ig.quantity = ig.quantity - :quantity WHERE ig.productId = :product_id")
    void decreaseStockQuantity(String product_id, int quantity);

    @Transactional
    @Modifying
    @Query("UPDATE IngredientStock ig SET ig.quantity = ig.quantity - :quantity WHERE ig.ingredientId = :ingredientId")
    void decreaseStock1Quantity(String ingredientId, int quantity);

    @Query("SELECT i.quantity FROM IngredientStock i WHERE i.productId = :productId AND i.ingredientId = 'I008'")
    int findCabbageBoxConcentrateByProductId(@Param("productId") String productId);

    @Query("SELECT i.quantity FROM IngredientStock i WHERE i.productId = :productId AND i.ingredientId = 'I008'")
    int findBlackBoxConcentrateByProductId(@Param("productId") String productId);

    @Query("SELECT i.quantity FROM IngredientStock i WHERE i.productId = :productId AND i.ingredientId = 'I008'")
    int findRaspberryBoxConcentrateByProductId(@Param("productId") String productId);

    @Query("SELECT i.quantity FROM IngredientStock i WHERE i.productId = :productId AND i.ingredientId = 'I008'")
    int findPlumBoxConcentrateByProductId(@Param("productId") String productId);

    @Query("SELECT i.quantity FROM IngredientStock i WHERE i.productId = :productId AND i.ingredientId = 'I006'")
    int findCabbagePouchConcentrateByProductId(@Param("productId") String productId);

    @Query("SELECT i.quantity FROM IngredientStock i WHERE i.productId = :productId AND i.ingredientId = 'I006'")
    int findBlackPouchConcentrateByProductId(@Param("productId") String productId);

    @Query("SELECT i.quantity FROM IngredientStock i WHERE i.productId = :productId AND i.ingredientId = 'I007'")
    int findRaspberryStickPouchConcentrateByProductId(@Param("productId") String productId);

    @Query("SELECT i.quantity FROM IngredientStock i WHERE i.productId = :productId AND i.ingredientId = 'I007'")
    int findPlumStickPouchConcentrateByProductId(@Param("productId") String productId);

    @Query("SELECT i.quantity FROM IngredientStock i WHERE i.productId = :productId AND i.ingredientId = 'I005'")
    int findPlumCollagenConcentrateByProductId(@Param("productId") String productId);

    @Query("SELECT i.quantity FROM IngredientStock i WHERE i.productId = :productId AND i.ingredientId = 'I005'")
    int findRaspberryCollagenConcentrateByProductId(@Param("productId") String productId);

    @Query("SELECT i FROM IngredientStock i WHERE i.ingredientId = :ingredientId")
    IngredientStock findBoxConcentrateByProductId(@Param("ingredientId") String ingredientId);

    @Query("SELECT i.quantity FROM IngredientStock i WHERE i.ingredientId = :ingredientId")
    int findBoxNumByProductId(@Param("ingredientId") String ingredientId);

    @Query("SELECT i.quantity FROM IngredientStock i WHERE i.ingredientId = :ingredientId")
    int findPouchNumByProductId(@Param("ingredientId") String ingredientId);

    @Query("SELECT i.quantity FROM IngredientStock i WHERE i.ingredientId = :ingredientId")
    int findStickPouchNumByProductId(@Param("ingredientId") String ingredientId);

    @Query("SELECT i.quantity FROM IngredientStock i WHERE i.ingredientId = :ingredientId")
    int findCollagenNumByProductId(@Param("ingredientId") String ingredientId);


    Optional<IngredientStock> findByIngredientId(String ingredientId);

    //스탁넘버 생성
    //발주번호 생성
    @Query(value = "SELECT MAX(RIGHT(i.stock_no,4)) FROM ingredient_stock AS i WHERE (select date_format(input_date, '%Y%m%d')) = (Select date_format(sysdate(), '%Y%m%d'))",nativeQuery = true)
    String findByStockNo();


    //스탁넘버 생성
    //발주번호 생성
    @Query(value = "SELECT MAX(RIGHT(i.stock_no,4)) FROM ingredient_stock AS i WHERE (select date_format(product_date, '%Y%m%d')) = (Select date_format(sysdate(), '%Y%m%d'))",nativeQuery = true)
    String selectMaxStockNo();



    @Query(value = "SELECT MAX(RIGHT(o.order_no,4)) FROM orders AS o WHERE (select date_format(order_date, '%Y%m%d')) = (Select date_format(sysdate(), '%Y%m%d'))",nativeQuery = true)
    String findByOrderNo();


    // 재고 더하기
    @Transactional
    @Modifying
    @Query("UPDATE IngredientStock ig SET ig.quantity = ig.quantity + :quantity WHERE ig.ingredientId = :ingredientId")
    void increaseStockQuantity(String ingredientId, int quantity);



}

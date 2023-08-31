package mes.smartmes.repository;


import mes.smartmes.entity.Orders;
import mes.smartmes.entity.ProductionPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ProductionPlanRepository extends JpaRepository<ProductionPlan, String> {

    @Query("SELECT p FROM ProductionPlan p WHERE p.productId = :productId")
    List<ProductionPlan> findByProductId(String productId);

    @Query("SELECT o FROM ProductionPlan o WHERE o.prodPlanNo = :planNo")
    ProductionPlan findByPlanNo(String planNo);

    @Query("select i.prodPlanNo from ProductionPlan i")
    List<String> findByPlanNo1();

    @Transactional
    @Modifying
    @Query(value = "INSERT INTO ProductionPlan (product_id, prodPlan_quantity, prodPlan_fin_yn) " +
            "VALUES (:productId, :quantity, :status)", nativeQuery = true)
    void insertProductionPlan(@Param("productId") String productId, @Param("quantity") int quantity, @Param("status") String status);

    @Query("SELECT p FROM ProductionPlan p WHERE p.prodPlanFinYn = :prodPlanFinYn")
    List<ProductionPlan> findByProdPlanFinYn(@Param("prodPlanFinYn") String prodPlanFinYn);

//    @Query("SELECT p FROM ProductionPlan p WHERE p.prodPlanFinYn = :prodPlanFinYn")
//    ProductionPlan findByProdPlanFinYn1(@Param("prodPlanFinYn") String prodPlanFinYn);

    @Query(value = "SELECT p.* FROM prod_plan p WHERE p.prod_plan_fin_yn = :prodPlanFinYn ORDER BY p.prod_plan_no DESC LIMIT 1", nativeQuery = true)
    ProductionPlan findByProdPlanFinYn1(@Param("prodPlanFinYn") String prodPlanFinYn);


//    @Query("SELECT p FROM ProductionPlan p WHERE p.prodPlanNo > :currentPlanId ORDER BY p.prodPlanNo ASC LIMIT 1", nativeQuery = true)
//    ProductionPlan findNextPlan(@Param("currentPlanId") String currentPlanId);

    @Query(value = "SELECT p.* FROM prod_plan p WHERE p.prod_plan_no > :currentPlanId ORDER BY p.prod_plan_no ASC LIMIT 1", nativeQuery = true)
    ProductionPlan findNextPlan(@Param("currentPlanId") String currentPlanId);


    @Query("SELECT MAX(p.prodPlanSeq) FROM ProductionPlan p WHERE p.orderNo = :orderNo")
    Integer getMaxProdPlanSeqByOrderNo(@Param("orderNo") String orderNo);

    @Query("SELECT o FROM Orders o WHERE o.orderStatus = :orderStatus")
    List<Orders> findByOrderStatus(@Param("orderStatus") String orderStatus);

    @Modifying
    @Transactional
    @Query("UPDATE ProductionPlan p SET p.prodPlanFinYn = :planStatus WHERE p.prodPlanNo = :planNo")
    void setPlanStatus(@Param("planNo") String orderNo, @Param("planStatus") String planStatus);



}
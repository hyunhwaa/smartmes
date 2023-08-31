package mes.smartmes.repository;

import mes.smartmes.entity.ProductionPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Repository
public interface ProdPlanRepository extends JpaRepository<ProductionPlan, String> {

    @Query("SELECT p FROM ProductionPlan p")
    List<ProductionPlan> findProduction();

    //prodPlanDate가 startDate와 endDate 사이에 있는지
    @Query("SELECT p FROM ProductionPlan p WHERE p.prodPlanDate BETWEEN :startDate AND :endDate AND p.prodPlanFinYn = :status AND p.productName = :items")
    List<ProductionPlan> findSearch(@Param("startDate") Date startDate, @Param("endDate") Date endDate, @Param("status") String status, @Param("items") String items);

    @Query(value= "SELECT date_format(time(p.prod_plan_date),'%H%i%S') FROM prod_plan p WHERE prod_plan_no = :planNo" ,nativeQuery = true)
    String selectTime(String planNo);

    @Query(value= "SELECT dayofweek(p.prod_plan_date) FROM prod_plan p WHERE prod_plan_no = :planNo" ,nativeQuery = true)
    Integer selectDay(String planNo);

    @Query(value= "SELECT p.prod_plan_date FROM prod_plan p WHERE prod_plan_no = :planNo ", nativeQuery = true)
    LocalDateTime selectDate(String planNo);

    @Query("select p from ProductionPlan p where p.prodPlanNo = :planNo")
    ProductionPlan selectPlanNo(@Param("planNo") String plnaNo);

}




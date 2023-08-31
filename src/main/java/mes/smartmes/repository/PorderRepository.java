package mes.smartmes.repository;

import mes.smartmes.entity.Porder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface PorderRepository extends JpaRepository<Porder, String> {

    Porder save(Porder porder);

    Optional<Porder> findById(String id);

    //발주번호 생성
    @Query(value = "SELECT MAX(RIGHT(p.porder_no,4)) FROM porder p WHERE (select date_format(porder_date, '%Y%m%d')) = (Select date_format(sysdate(), '%Y%m%d'))",nativeQuery = true)
    String findByPorderNo();


    @Query("select i.porderNo from Porder i")
    List<String> findByPlanNo1();

    //발주 내역 리스트
    List<Porder> findAll();

    //발주 제품 입고 예정 일자 추출 쿼리_1 : 시간 비교
    @Query(value= "SELECT date_format(time(p.porder_date),'%H%i%S') FROM porder p WHERE porder_no = :porderNo" ,nativeQuery = true)
    String selectTime(String porderNo);

    //발주 제품 입고 예정 일자 추출 쿼리_2 : 요일 비교
    @Query(value= "SELECT dayofweek(p.porder_date) FROM porder p WHERE porder_no = :porderNo" ,nativeQuery = true)
    Integer selectDay(String porderNo);

    //발주 등록 날짜
    @Query(value= "SELECT p.porder_date FROM porder p WHERE porder_no = :porderNo ", nativeQuery = true)
    LocalDateTime selectDate(String porderNo);

    //긴급입고여부
    @Query(value = "SELECT emergency_yn FROM porder WHERE porder_no = :porderNo", nativeQuery = true)
    String emergencyYn(String porderNo);

    @Query(value = "SELECT ingredient_name FROM porder p WHERE porder_no = :porderNo", nativeQuery = true)
    String selectIngreId(String porderNo);

    @Query("SELECT p FROM Porder p WHERE p.porderStatus = :porderStatus")
    List<Porder> findByPorderStatus(@Param("porderStatus") String porderStatus);

    @Query("SELECT p FROM Porder p WHERE p.porderNo = :porderNo")
    Porder findByPorderNo(String porderNo);

    @Query(value= "SELECT dayofweek(:date) " ,nativeQuery = true)
    Integer checkDay(LocalDateTime date);

    //검색
    @Query("SELECT p FROM Porder p WHERE DATE(p.porderDate) BETWEEN :startDate AND :endDate AND p.porderStatus = :porderStatus AND p.supplierId = :supplierId")
    List<Porder> findSearch(@Param("startDate") java.util.Date startDate, @Param("endDate") java.util.Date endDate, @Param("porderStatus") String porderStatus, @Param("supplierId") String supplierId);


    List<Porder> findByPorderStatusAndPorderDateBefore(String porderStatsus, LocalDateTime porderDate);



    @Modifying
    @Transactional
    @Query("update Porder p set p.porderStatus = :porderStatus where p.porderNo = :porderNo")
    void updatePorderStatus(@Param("porderNo") String porderNo,@Param("porderStatus") String porderStatus);

    @Query("update Porder p set p.thinkInputDate = :time where p.porderNo = :porderNo")
    void setThinkInputDate(@Param("time") LocalDateTime time, @Param("porderNo") String porderNo);

    @Query("select p.ingredientName from Porder p where p.ProdPlanNo =:orderNo and p.porderStatus =:status")
    List<String> findByNo(@Param("orderNo") String orderNo, @Param("status") String status);

    @Query("select p.porderQuantity from Porder p where p.ProdPlanNo =:orderNo and p.porderStatus =:status")
    List<Integer> findByNo1(@Param("orderNo") String orderNo, @Param("status") String status);
}


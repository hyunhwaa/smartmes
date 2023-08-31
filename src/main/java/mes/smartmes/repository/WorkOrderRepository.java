package mes.smartmes.repository;


import mes.smartmes.entity.Porder;
import mes.smartmes.entity.WorkOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface WorkOrderRepository extends JpaRepository<WorkOrder, String>, QuerydslPredicateExecutor {

    @Query("select i.workOrderNo from WorkOrder i")
    List<String> findByPlanNo1();

    WorkOrder save(WorkOrder workOrder);
    @Query("SELECT MAX(w.workOrderSeq) FROM WorkOrder w WHERE w.prodPlanNo = :planNo")
    Integer getMaxWorkPlanSeqByPlanNo(@Param("planNo") String planNo);

    @Query("SELECT w FROM WorkOrder w WHERE w.workStatus = :workStatus")
    List<WorkOrder> findByWorkStatus(@Param("workStatus") String workStatus);

    @Query("SELECT w FROM WorkOrder w WHERE w.workOrderNo = :workOrderNo")
    WorkOrder findByWorkOrder(@Param("workOrderNo") String workOrderNo);

    @Modifying
    @Transactional
    @Query("UPDATE WorkOrder w SET w.workStatus = :workStatus WHERE w.workOrderNo = :workNo")
    void setWorkStatus(@Param("workNo") String workNo, @Param("workStatus") String workStatus);




}
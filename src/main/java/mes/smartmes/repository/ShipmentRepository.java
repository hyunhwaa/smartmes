package mes.smartmes.repository;

import com.querydsl.core.BooleanBuilder;
import mes.smartmes.entity.Shipment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ShipmentRepository extends JpaRepository<Shipment, String>, QuerydslPredicateExecutor<Shipment> {

    Optional<Shipment> findById(String shipmentNo);

    @Query("select i.shipmentNo from Shipment i")
    List<String> findByPlanNo1();


    @Query(value = "SELECT MAX(RIGHT(s.shipmentNo,4)) FROM shipment AS s WHERE (select date_format(shipmentDate, '%Y%m%d')) = (Select date_format(sysdate(), '%Y%m%d'))",nativeQuery = true)
    String findByShipmentNo();

    List<Shipment> findAll();


    int deleteByShipmentNo(String shipmentNo);


    @Query("select s from Shipment s where s.orderNo =:orderNo and s.shipmentStatus =:status")
    List<Shipment> findAllByOrderNoAndShipmentStatus(@Param("orderNo") String orderNo, @Param("status") String status);

    @Query("select s.shipmentNo from Shipment s")
    List<String> findshipNo();
}

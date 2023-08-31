package mes.smartmes.repository;

import mes.smartmes.entity.Lot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import javax.persistence.EntityManager;
import java.util.List;

public interface LotRepository extends JpaRepository<Lot, String> {

    //발주번호 생성
    @Query(value = "SELECT MAX(RIGHT(l.lot_id,4)) FROM lot l WHERE (select date_format(reg_date, '%Y%m%d')) = (Select date_format(sysdate(), '%Y%m%d'))",nativeQuery = true)
    String findBylotId();


    @Query("select l.lotId from Lot l")
    List<String> findLotId();

    @Query("select l.lotNo from Lot l")
    List<String> findLotNo();
}

package mes.smartmes.repository;


import mes.smartmes.entity.IngredientInput;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IngredientInputRepository extends JpaRepository<IngredientInput, String> {
    @Query("select i.ingredientInId from IngredientInput i")
    List<String> findingredientinId();
}

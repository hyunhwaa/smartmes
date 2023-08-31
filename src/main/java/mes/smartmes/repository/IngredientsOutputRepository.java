package mes.smartmes.repository;

import mes.smartmes.entity.IngredientOutput;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IngredientsOutputRepository extends JpaRepository<IngredientOutput, String> {

    @Query("select i.ingredientOutId from IngredientOutput i")
    List<String> findingredientOutId();
}

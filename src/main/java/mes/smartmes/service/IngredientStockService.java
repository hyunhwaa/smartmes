package mes.smartmes.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import mes.smartmes.entity.IngredientStock;
import mes.smartmes.repository.IngredientStockRepository;
import org.springframework.stereotype.Service;

import java.util.List;
@Log4j2
@Service
@RequiredArgsConstructor
public class IngredientStockService {

    private final IngredientStockRepository ingredientStockRepository;

    public List<IngredientStock> selectList(){
        return ingredientStockRepository.findAll();
    }
}
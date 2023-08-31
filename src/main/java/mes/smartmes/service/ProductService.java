package mes.smartmes.service;

import lombok.RequiredArgsConstructor;
import mes.smartmes.entity.IngredientStock;
import mes.smartmes.entity.Product;
import mes.smartmes.repository.BomRepository;
import mes.smartmes.repository.IngredientStockRepository;
import mes.smartmes.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    @Autowired
    private  BomRepository bomRepository;

    @Autowired
    private  IngredientStockRepository ingredientStockRepository;

    @Autowired
    private  ProductRepository productRepository;

    public ProductService(BomRepository bomRepository, IngredientStockRepository ingredientStockRepository) {
        this.bomRepository = bomRepository;
        this.ingredientStockRepository = ingredientStockRepository;
    }

    public int getIngredientStockQuantityForProduct(String productId, String ingredientId) {
        List bomList = bomRepository.findByProductId(productId);

        for (Object bom : bomList) {
            if (bom.toString().equals(ingredientId)) {
                Optional<IngredientStock> ingredientStockOptional = ingredientStockRepository.findByIngredientId(ingredientId);
                if (ingredientStockOptional.isPresent()) {
                    IngredientStock ingredientStock = ingredientStockOptional.get();
                    return ingredientStock.getQuantity();
                }
            }
        }

        return 0; // 해당 원자재가 없거나 재고가 없는 경우 기본값으로 0을 반환
    }

    public List<Product> selectList(){
        return productRepository.findAll();
    }
}

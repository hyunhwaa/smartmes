package mes.smartmes.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QIngredientStock is a Querydsl query type for IngredientStock
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QIngredientStock extends EntityPathBase<IngredientStock> {

    private static final long serialVersionUID = 37835139L;

    public static final QIngredientStock ingredientStock = new QIngredientStock("ingredientStock");

    public final StringPath ingredientId = createString("ingredientId");

    public final DatePath<java.time.LocalDate> inputDate = createDate("inputDate", java.time.LocalDate.class);

    public final DatePath<java.sql.Date> productDate = createDate("productDate", java.sql.Date.class);

    public final StringPath productId = createString("productId");

    public final NumberPath<Integer> quantity = createNumber("quantity", Integer.class);

    public final StringPath stockNo = createString("stockNo");

    public QIngredientStock(String variable) {
        super(IngredientStock.class, forVariable(variable));
    }

    public QIngredientStock(Path<? extends IngredientStock> path) {
        super(path.getType(), path.getMetadata());
    }

    public QIngredientStock(PathMetadata metadata) {
        super(IngredientStock.class, metadata);
    }

}


package mes.smartmes.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QIngredients is a Querydsl query type for Ingredients
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QIngredients extends EntityPathBase<Ingredients> {

    private static final long serialVersionUID = 1538693504L;

    public static final QIngredients ingredients = new QIngredients("ingredients");

    public final StringPath ingredientId = createString("ingredientId");

    public final StringPath ingredientName = createString("ingredientName");

    public final NumberPath<Integer> ingredientNo = createNumber("ingredientNo", Integer.class);

    public final StringPath productId = createString("productId");

    public QIngredients(String variable) {
        super(Ingredients.class, forVariable(variable));
    }

    public QIngredients(Path<? extends Ingredients> path) {
        super(path.getType(), path.getMetadata());
    }

    public QIngredients(PathMetadata metadata) {
        super(Ingredients.class, metadata);
    }

}


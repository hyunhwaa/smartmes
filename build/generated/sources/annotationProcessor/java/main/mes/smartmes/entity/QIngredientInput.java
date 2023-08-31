package mes.smartmes.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QIngredientInput is a Querydsl query type for IngredientInput
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QIngredientInput extends EntityPathBase<IngredientInput> {

    private static final long serialVersionUID = 28422711L;

    public static final QIngredientInput ingredientInput = new QIngredientInput("ingredientInput");

    public final StringPath ingredientId = createString("ingredientId");

    public final StringPath ingredientInId = createString("ingredientInId");

    public final DateTimePath<java.time.LocalDateTime> inputDate = createDateTime("inputDate", java.time.LocalDateTime.class);

    public final NumberPath<Integer> inputQuantity = createNumber("inputQuantity", Integer.class);

    public final StringPath porderNo = createString("porderNo");

    public QIngredientInput(String variable) {
        super(IngredientInput.class, forVariable(variable));
    }

    public QIngredientInput(Path<? extends IngredientInput> path) {
        super(path.getType(), path.getMetadata());
    }

    public QIngredientInput(PathMetadata metadata) {
        super(IngredientInput.class, metadata);
    }

}


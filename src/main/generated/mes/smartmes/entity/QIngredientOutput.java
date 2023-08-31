package mes.smartmes.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QIngredientOutput is a Querydsl query type for IngredientOutput
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QIngredientOutput extends EntityPathBase<IngredientOutput> {

    private static final long serialVersionUID = 1059458100L;

    public static final QIngredientOutput ingredientOutput = new QIngredientOutput("ingredientOutput");

    public final StringPath ingredientId = createString("ingredientId");

    public final StringPath ingredientOutId = createString("ingredientOutId");

    public final DateTimePath<java.time.LocalDateTime> outputDate = createDateTime("outputDate", java.time.LocalDateTime.class);

    public final NumberPath<Integer> outputQuantity = createNumber("outputQuantity", Integer.class);

    public final StringPath porderNo = createString("porderNo");

    public QIngredientOutput(String variable) {
        super(IngredientOutput.class, forVariable(variable));
    }

    public QIngredientOutput(Path<? extends IngredientOutput> path) {
        super(path.getType(), path.getMetadata());
    }

    public QIngredientOutput(PathMetadata metadata) {
        super(IngredientOutput.class, metadata);
    }

}


package mes.smartmes.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QBom is a Querydsl query type for Bom
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBom extends EntityPathBase<Bom> {

    private static final long serialVersionUID = -459281282L;

    public static final QBom bom = new QBom("bom");

    public final StringPath bomId = createString("bomId");

    public final NumberPath<Long> ingredientId = createNumber("ingredientId", Long.class);

    public final NumberPath<Long> ingreQty = createNumber("ingreQty", Long.class);

    public final StringPath ingreQtyUnit = createString("ingreQtyUnit");

    public final StringPath productId = createString("productId");

    public final NumberPath<Long> productQty = createNumber("productQty", Long.class);

    public QBom(String variable) {
        super(Bom.class, forVariable(variable));
    }

    public QBom(Path<? extends Bom> path) {
        super(path.getType(), path.getMetadata());
    }

    public QBom(PathMetadata metadata) {
        super(Bom.class, metadata);
    }

}


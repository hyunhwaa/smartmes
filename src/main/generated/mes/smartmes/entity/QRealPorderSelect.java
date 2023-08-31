package mes.smartmes.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QRealPorderSelect is a Querydsl query type for RealPorderSelect
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QRealPorderSelect extends EntityPathBase<RealPorderSelect> {

    private static final long serialVersionUID = -569117126L;

    public static final QRealPorderSelect realPorderSelect = new QRealPorderSelect("realPorderSelect");

    public final StringPath ingredientId = createString("ingredientId");

    public final NumberPath<Integer> inputQuantity = createNumber("inputQuantity", Integer.class);

    public final StringPath orderNo = createString("orderNo");

    public final NumberPath<Integer> realporderid = createNumber("realporderid", Integer.class);

    public QRealPorderSelect(String variable) {
        super(RealPorderSelect.class, forVariable(variable));
    }

    public QRealPorderSelect(Path<? extends RealPorderSelect> path) {
        super(path.getType(), path.getMetadata());
    }

    public QRealPorderSelect(PathMetadata metadata) {
        super(RealPorderSelect.class, metadata);
    }

}


package mes.smartmes.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QFinproduct is a Querydsl query type for Finproduct
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QFinproduct extends EntityPathBase<Finproduct> {

    private static final long serialVersionUID = -575877626L;

    public static final QFinproduct finproduct = new QFinproduct("finproduct");

    public final StringPath finProductNo = createString("finProductNo");

    public final NumberPath<Integer> finProductQuantity = createNumber("finProductQuantity", Integer.class);

    public final DateTimePath<java.time.LocalDateTime> productDate = createDateTime("productDate", java.time.LocalDateTime.class);

    public final StringPath productId = createString("productId");

    public QFinproduct(String variable) {
        super(Finproduct.class, forVariable(variable));
    }

    public QFinproduct(Path<? extends Finproduct> path) {
        super(path.getType(), path.getMetadata());
    }

    public QFinproduct(PathMetadata metadata) {
        super(Finproduct.class, metadata);
    }

}


package mes.smartmes.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QLot is a Querydsl query type for Lot
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QLot extends EntityPathBase<Lot> {

    private static final long serialVersionUID = -459271665L;

    public static final QLot lot = new QLot("lot");

    public final NumberPath<Integer> inputQuantity = createNumber("inputQuantity", Integer.class);

    public final NumberPath<Integer> inventoryQuantity = createNumber("inventoryQuantity", Integer.class);

    public final StringPath lotId = createString("lotId");

    public final StringPath lotNo = createString("lotNo");

    public final StringPath lotPlotNo = createString("lotPlotNo");

    public final NumberPath<Integer> outputQuantity = createNumber("outputQuantity", Integer.class);

    public final StringPath processNo = createString("processNo");

    public final StringPath productId = createString("productId");

    public final DateTimePath<java.time.LocalDateTime> regDate = createDateTime("regDate", java.time.LocalDateTime.class);

    public QLot(String variable) {
        super(Lot.class, forVariable(variable));
    }

    public QLot(Path<? extends Lot> path) {
        super(path.getType(), path.getMetadata());
    }

    public QLot(PathMetadata metadata) {
        super(Lot.class, metadata);
    }

}


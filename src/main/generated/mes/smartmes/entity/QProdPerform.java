package mes.smartmes.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QProdPerform is a Querydsl query type for ProdPerform
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QProdPerform extends EntityPathBase<ProdPerform> {

    private static final long serialVersionUID = -566129464L;

    public static final QProdPerform prodPerform = new QProdPerform("prodPerform");

    public final StringPath equipmentId = createString("equipmentId");

    public final StringPath processNo = createString("processNo");

    public final DateTimePath<java.time.LocalDateTime> productionDate = createDateTime("productionDate", java.time.LocalDateTime.class);

    public final StringPath productionId = createString("productionId");

    public final StringPath productionNo = createString("productionNo");

    public final NumberPath<Integer> productionQuantity = createNumber("productionQuantity", Integer.class);

    public final NumberPath<Integer> productionSeq = createNumber("productionSeq", Integer.class);

    public final StringPath productionStatus = createString("productionStatus");

    public final StringPath workOrderNo = createString("workOrderNo");

    public final NumberPath<Integer> workOrderSeq = createNumber("workOrderSeq", Integer.class);

    public QProdPerform(String variable) {
        super(ProdPerform.class, forVariable(variable));
    }

    public QProdPerform(Path<? extends ProdPerform> path) {
        super(path.getType(), path.getMetadata());
    }

    public QProdPerform(PathMetadata metadata) {
        super(ProdPerform.class, metadata);
    }

}


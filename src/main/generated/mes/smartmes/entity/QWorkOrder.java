package mes.smartmes.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QWorkOrder is a Querydsl query type for WorkOrder
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QWorkOrder extends EntityPathBase<WorkOrder> {

    private static final long serialVersionUID = -426673413L;

    public static final QWorkOrder workOrder = new QWorkOrder("workOrder");

    public final StringPath equipmentId = createString("equipmentId");

    public final NumberPath<Integer> inputQuantity = createNumber("inputQuantity", Integer.class);

    public final NumberPath<Integer> outputQuantity = createNumber("outputQuantity", Integer.class);

    public final StringPath processNo = createString("processNo");

    public final StringPath prodPlanNo = createString("prodPlanNo");

    public final NumberPath<Integer> prodPlanSeq = createNumber("prodPlanSeq", Integer.class);

    public final StringPath productId = createString("productId");

    public final DateTimePath<java.time.LocalDateTime> workOrderDate = createDateTime("workOrderDate", java.time.LocalDateTime.class);

    public final DateTimePath<java.time.LocalDateTime> workOrderFinishDate = createDateTime("workOrderFinishDate", java.time.LocalDateTime.class);

    public final StringPath workOrderNo = createString("workOrderNo");

    public final NumberPath<Integer> workOrderSeq = createNumber("workOrderSeq", Integer.class);

    public final StringPath workStatus = createString("workStatus");

    public QWorkOrder(String variable) {
        super(WorkOrder.class, forVariable(variable));
    }

    public QWorkOrder(Path<? extends WorkOrder> path) {
        super(path.getType(), path.getMetadata());
    }

    public QWorkOrder(PathMetadata metadata) {
        super(WorkOrder.class, metadata);
    }

}


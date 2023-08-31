package mes.smartmes.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QProductionPlan is a Querydsl query type for ProductionPlan
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QProductionPlan extends EntityPathBase<ProductionPlan> {

    private static final long serialVersionUID = -1033668092L;

    public static final QProductionPlan productionPlan = new QProductionPlan("productionPlan");

    public final StringPath orderNo = createString("orderNo");

    public final DateTimePath<java.time.LocalDateTime> prodPlanDate = createDateTime("prodPlanDate", java.time.LocalDateTime.class);

    public final StringPath prodPlanFinYn = createString("prodPlanFinYn");

    public final StringPath prodPlanNo = createString("prodPlanNo");

    public final NumberPath<Integer> prodPlanQuantity = createNumber("prodPlanQuantity", Integer.class);

    public final NumberPath<Integer> prodPlanSeq = createNumber("prodPlanSeq", Integer.class);

    public final StringPath productId = createString("productId");

    public final StringPath productName = createString("productName");

    public QProductionPlan(String variable) {
        super(ProductionPlan.class, forVariable(variable));
    }

    public QProductionPlan(Path<? extends ProductionPlan> path) {
        super(path.getType(), path.getMetadata());
    }

    public QProductionPlan(PathMetadata metadata) {
        super(ProductionPlan.class, metadata);
    }

}


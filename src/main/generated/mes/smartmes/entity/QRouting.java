package mes.smartmes.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QRouting is a Querydsl query type for Routing
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QRouting extends EntityPathBase<Routing> {

    private static final long serialVersionUID = -792444796L;

    public static final QRouting routing = new QRouting("routing");

    public final StringPath processNo1 = createString("processNo1");

    public final StringPath processNo10 = createString("processNo10");

    public final StringPath processNo2 = createString("processNo2");

    public final StringPath processNo3 = createString("processNo3");

    public final StringPath processNo4 = createString("processNo4");

    public final StringPath processNo5 = createString("processNo5");

    public final StringPath processNo6 = createString("processNo6");

    public final StringPath processNo7 = createString("processNo7");

    public final StringPath processNo8 = createString("processNo8");

    public final StringPath processNo9 = createString("processNo9");

    public final StringPath productId = createString("productId");

    public final StringPath routingId = createString("routingId");

    public final StringPath routingName = createString("routingName");

    public QRouting(String variable) {
        super(Routing.class, forVariable(variable));
    }

    public QRouting(Path<? extends Routing> path) {
        super(path.getType(), path.getMetadata());
    }

    public QRouting(PathMetadata metadata) {
        super(Routing.class, metadata);
    }

}


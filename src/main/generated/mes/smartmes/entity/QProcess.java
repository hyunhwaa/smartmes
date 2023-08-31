package mes.smartmes.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QProcess is a Querydsl query type for Process
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QProcess extends EntityPathBase<Process> {

    private static final long serialVersionUID = 1807351341L;

    public static final QProcess process = new QProcess("process");

    public final StringPath CapacityUnit = createString("CapacityUnit");

    public final NumberPath<Long> leadTime = createNumber("leadTime", Long.class);

    public final NumberPath<Long> processCapacity = createNumber("processCapacity", Long.class);

    public final StringPath processDivision = createString("processDivision");

    public final StringPath processName = createString("processName");

    public final StringPath processNO = createString("processNO");

    public final NumberPath<Long> processTime = createNumber("processTime", Long.class);

    public final StringPath productId = createString("productId");

    public QProcess(String variable) {
        super(Process.class, forVariable(variable));
    }

    public QProcess(Path<? extends Process> path) {
        super(path.getType(), path.getMetadata());
    }

    public QProcess(PathMetadata metadata) {
        super(Process.class, metadata);
    }

}


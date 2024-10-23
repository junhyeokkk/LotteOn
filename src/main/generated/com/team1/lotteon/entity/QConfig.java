package com.team1.lotteon.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QConfig is a Querydsl query type for Config
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QConfig extends EntityPathBase<Config> {

    private static final long serialVersionUID = -569357415L;

    public static final QConfig config = new QConfig("config");

    public final StringPath addr1 = createString("addr1");

    public final StringPath addr2 = createString("addr2");

    public final StringPath b_name = createString("b_name");

    public final StringPath b_num = createString("b_num");

    public final StringPath b_report = createString("b_report");

    public final StringPath ceo = createString("ceo");

    public final NumberPath<Integer> companyid = createNumber("companyid", Integer.class);

    public final StringPath copyright = createString("copyright");

    public final StringPath cs_email = createString("cs_email");

    public final StringPath cs_num = createString("cs_num");

    public final StringPath cs_time = createString("cs_time");

    public final StringPath dispute = createString("dispute");

    public final StringPath favicon = createString("favicon");

    public final StringPath footerlogo = createString("footerlogo");

    public final StringPath headerlogo = createString("headerlogo");

    public final StringPath sub_title = createString("sub_title");

    public final StringPath title = createString("title");

    public QConfig(String variable) {
        super(Config.class, forVariable(variable));
    }

    public QConfig(Path<? extends Config> path) {
        super(path.getType(), path.getMetadata());
    }

    public QConfig(PathMetadata metadata) {
        super(Config.class, metadata);
    }

}


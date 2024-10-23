package com.team1.lotteon.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QVersion is a Querydsl query type for Version
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QVersion extends EntityPathBase<Version> {

    private static final long serialVersionUID = -1069719711L;

    public static final QVersion version1 = new QVersion("version1");

    public final StringPath content = createString("content");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final DatePath<java.time.LocalDate> rDate = createDate("rDate", java.time.LocalDate.class);

    public final StringPath uid = createString("uid");

    public final StringPath version = createString("version");

    public QVersion(String variable) {
        super(Version.class, forVariable(variable));
    }

    public QVersion(Path<? extends Version> path) {
        super(path.getType(), path.getMetadata());
    }

    public QVersion(PathMetadata metadata) {
        super(Version.class, metadata);
    }

}


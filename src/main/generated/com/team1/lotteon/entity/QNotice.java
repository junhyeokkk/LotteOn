package com.team1.lotteon.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QNotice is a Querydsl query type for Notice
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QNotice extends EntityPathBase<Notice> {

    private static final long serialVersionUID = -254255313L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QNotice notice = new QNotice("notice");

    public final QArticle _super;

    //inherited
    public final StringPath content;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt;

    //inherited
    public final NumberPath<Long> id;

    // inherited
    public final QMember member;

    //inherited
    public final StringPath title;

    //inherited
    public final StringPath type1;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt;

    //inherited
    public final NumberPath<Integer> views;

    public QNotice(String variable) {
        this(Notice.class, forVariable(variable), INITS);
    }

    public QNotice(Path<? extends Notice> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QNotice(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QNotice(PathMetadata metadata, PathInits inits) {
        this(Notice.class, metadata, inits);
    }

    public QNotice(Class<? extends Notice> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this._super = new QArticle(type, metadata, inits);
        this.content = _super.content;
        this.createdAt = _super.createdAt;
        this.id = _super.id;
        this.member = _super.member;
        this.title = _super.title;
        this.type1 = _super.type1;
        this.updatedAt = _super.updatedAt;
        this.views = _super.views;
    }

}


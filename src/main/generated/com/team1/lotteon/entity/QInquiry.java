package com.team1.lotteon.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QInquiry is a Querydsl query type for Inquiry
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QInquiry extends EntityPathBase<Inquiry> {

    private static final long serialVersionUID = 534432848L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QInquiry inquiry = new QInquiry("inquiry");

    public final QArticle _super;

    public final StringPath answer = createString("answer");

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

    public final StringPath type2 = createString("type2");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt;

    //inherited
    public final NumberPath<Integer> views;

    public QInquiry(String variable) {
        this(Inquiry.class, forVariable(variable), INITS);
    }

    public QInquiry(Path<? extends Inquiry> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QInquiry(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QInquiry(PathMetadata metadata, PathInits inits) {
        this(Inquiry.class, metadata, inits);
    }

    public QInquiry(Class<? extends Inquiry> type, PathMetadata metadata, PathInits inits) {
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


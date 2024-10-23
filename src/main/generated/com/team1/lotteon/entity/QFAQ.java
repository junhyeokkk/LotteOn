package com.team1.lotteon.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QFAQ is a Querydsl query type for FAQ
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QFAQ extends EntityPathBase<FAQ> {

    private static final long serialVersionUID = -1361414625L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QFAQ fAQ = new QFAQ("fAQ");

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

    public final StringPath type2 = createString("type2");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt;

    //inherited
    public final NumberPath<Integer> views;

    public QFAQ(String variable) {
        this(FAQ.class, forVariable(variable), INITS);
    }

    public QFAQ(Path<? extends FAQ> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QFAQ(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QFAQ(PathMetadata metadata, PathInits inits) {
        this(FAQ.class, metadata, inits);
    }

    public QFAQ(Class<? extends FAQ> type, PathMetadata metadata, PathInits inits) {
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


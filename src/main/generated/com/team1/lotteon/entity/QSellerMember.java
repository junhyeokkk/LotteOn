package com.team1.lotteon.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QSellerMember is a Querydsl query type for SellerMember
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QSellerMember extends EntityPathBase<SellerMember> {

    private static final long serialVersionUID = -1916807664L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QSellerMember sellerMember = new QSellerMember("sellerMember");

    public final QMember _super = new QMember(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final StringPath pass = _super.pass;

    //inherited
    public final StringPath role = _super.role;

    public final QShop shop;

    //inherited
    public final StringPath uid = _super.uid;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QSellerMember(String variable) {
        this(SellerMember.class, forVariable(variable), INITS);
    }

    public QSellerMember(Path<? extends SellerMember> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QSellerMember(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QSellerMember(PathMetadata metadata, PathInits inits) {
        this(SellerMember.class, metadata, inits);
    }

    public QSellerMember(Class<? extends SellerMember> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.shop = inits.isInitialized("shop") ? new QShop(forProperty("shop"), inits.get("shop")) : null;
    }

}


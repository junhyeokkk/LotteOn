package com.team1.lotteon.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QGeneralMember is a Querydsl query type for GeneralMember
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QGeneralMember extends EntityPathBase<GeneralMember> {

    private static final long serialVersionUID = 1474603371L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QGeneralMember generalMember = new QGeneralMember("generalMember");

    public final QMember _super = new QMember(this);

    public final QAddress address;

    public final DatePath<java.time.LocalDate> birth = createDate("birth", java.time.LocalDate.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final StringPath email = createString("email");

    public final StringPath etc = createString("etc");

    public final EnumPath<com.team1.lotteon.entity.enums.Gender> gender = createEnum("gender", com.team1.lotteon.entity.enums.Gender.class);

    public final EnumPath<com.team1.lotteon.entity.enums.Grade> grade = createEnum("grade", com.team1.lotteon.entity.enums.Grade.class);

    public final DateTimePath<java.time.LocalDateTime> lastLoginDate = createDateTime("lastLoginDate", java.time.LocalDateTime.class);

    public final StringPath name = createString("name");

    //inherited
    public final StringPath pass = _super.pass;

    public final StringPath ph = createString("ph");

    public final NumberPath<Integer> points = createNumber("points", Integer.class);

    //inherited
    public final StringPath role = _super.role;

    public final NumberPath<Integer> status = createNumber("status", Integer.class);

    //inherited
    public final StringPath uid = _super.uid;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QGeneralMember(String variable) {
        this(GeneralMember.class, forVariable(variable), INITS);
    }

    public QGeneralMember(Path<? extends GeneralMember> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QGeneralMember(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QGeneralMember(PathMetadata metadata, PathInits inits) {
        this(GeneralMember.class, metadata, inits);
    }

    public QGeneralMember(Class<? extends GeneralMember> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.address = inits.isInitialized("address") ? new QAddress(forProperty("address")) : null;
    }

}


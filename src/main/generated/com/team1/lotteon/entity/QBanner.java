package com.team1.lotteon.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QBanner is a Querydsl query type for Banner
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBanner extends EntityPathBase<Banner> {

    private static final long serialVersionUID = -610908285L;

    public static final QBanner banner = new QBanner("banner");

    public final StringPath backgroundColor = createString("backgroundColor");

    public final StringPath backgroundLink = createString("backgroundLink");

    public final DatePath<java.time.LocalDate> displayEndDate = createDate("displayEndDate", java.time.LocalDate.class);

    public final TimePath<java.time.LocalTime> displayEndTime = createTime("displayEndTime", java.time.LocalTime.class);

    public final DatePath<java.time.LocalDate> displayStartDate = createDate("displayStartDate", java.time.LocalDate.class);

    public final TimePath<java.time.LocalTime> displayStartTime = createTime("displayStartTime", java.time.LocalTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath img = createString("img");

    public final BooleanPath isActive = createBoolean("isActive");

    public final StringPath name = createString("name");

    public final StringPath position = createString("position");

    public final StringPath size = createString("size");

    public QBanner(String variable) {
        super(Banner.class, forVariable(variable));
    }

    public QBanner(Path<? extends Banner> path) {
        super(path.getType(), path.getMetadata());
    }

    public QBanner(PathMetadata metadata) {
        super(Banner.class, metadata);
    }

}


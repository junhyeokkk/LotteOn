package com.team1.lotteon.repository.coupon;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.team1.lotteon.dto.pageDTO.NewPageRequestDTO;
import com.team1.lotteon.entity.Coupon;
import com.team1.lotteon.entity.QCoupon;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class CouponRepositoryImpl implements CouponRepositoryCustom{

    private final JPAQueryFactory queryFactory;
    private final EntityManager entityManager;
    private QCoupon qCoupon = QCoupon.coupon;

    @Override
    public Page<Tuple> selectCouponAllForList(NewPageRequestDTO newPageRequestDTO, Pageable pageable) {
        List<Tuple> content = null;
        long total = 0;
        content = queryFactory.select(qCoupon,qCoupon)
                .from(qCoupon)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(qCoupon.couponid.desc())
                .fetch();
        total = queryFactory
                .select(qCoupon.count())
                .from(qCoupon)
                .fetchOne();
        return new PageImpl<Tuple>(content, pageable, total);
    }

}

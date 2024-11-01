package com.team1.lotteon.repository.coupontake;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.team1.lotteon.entity.CouponTake;
import com.team1.lotteon.entity.QCouponTake;
import com.querydsl.core.Tuple;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

/*
     날짜 : 2024/10/30
     이름 : 이도영(최초 작성자)
     내용 : CouponTakeRepositoryImpl 생성

     수정이력
     - 2024/11/01 이도영 사용하지 않은 쿠폰에 대해서만 출력 수정
*/
@RequiredArgsConstructor
@Repository
public class CouponTakeRepositoryImpl implements CouponTakeRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private final QCouponTake qCouponTake = QCouponTake.couponTake;

    //상점정보와 맴버 아이디를 가지고 와서 정보 가지고 오기
    @Override
    public List<CouponTake> findByMemberIdAndOptionalShopId(String memberId, Long shopId) {
        return queryFactory
                .selectFrom(qCouponTake)
                .where(
                        qCouponTake.member.uid.eq(memberId)
                                .and(qCouponTake.couponUseCheck.eq(1))
                                .and(shopId != null
                                        // 주어진 shopId와 일치하거나, shopId가 null인 데이터 모두 조회
                                        ? qCouponTake.shop.id.eq(shopId).or(qCouponTake.shop.isNull())
                                        // shopId가 null일 때, memberId와 일치하고 shopId가 null인 데이터만 조회
                                        : qCouponTake.shop.isNull()
                                )
                )
                .fetch();
    }


    //나의 정보에서 멤버 아이디만 가지고와서 출력하기
    @Override
    public Page<CouponTake> findPagedCouponsByMemberId(String memberId, Pageable pageable) {
        List<CouponTake> content = queryFactory
                .selectFrom(qCouponTake)
                .where(
                        qCouponTake.member.uid.eq(memberId)
                                .and(qCouponTake.couponUseCheck.eq(1))
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory
                .select(qCouponTake.count())
                .from(qCouponTake)
                .where(qCouponTake.member.uid.eq(memberId))
                .fetchOne();

        return new PageImpl<>(content, pageable, total);
    }
}
package com.team1.lotteon.repository.impl;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.team1.lotteon.entity.*;
import com.team1.lotteon.repository.custom.OrderRepositoryCustom;
import com.team1.lotteon.repository.custom.PointRepositoryCustom;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

/*
    날짜 : 2024/11/1
    이름 : 최준혁
    내용 : OrderRepositoryImpl 생성

    - QueryDSL 사용하여 검색기능 메서드 추가
*/
@Log4j2
@RequiredArgsConstructor
@Repository
public class OrderRepositoryImpl implements OrderRepositoryCustom {

    private final JPAQueryFactory queryFactory;


    @Override
    public Page<Order> findByDynamicType(String keyword, String type, Pageable pageable) {
        QOrder order = QOrder.order;

        // 검색 조건에 따른 where 조건 표현식 생성
        BooleanExpression expression = getPredicate(keyword, type);

        // keyword가 없을 때 빈 결과 반환
        if (expression == null) {
            return new PageImpl<>(List.of(), pageable, 0); // 빈 리스트와 총 0을 반환
        }

        // 기본 쿼리 작성
        List<Order> result = queryFactory.selectFrom(order)
                .where(expression)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(order.id.desc()) // 정렬 기준 설정
                .fetch();

        long total = queryFactory.selectFrom(order)
                .where(expression)
                .fetchCount();

        return new PageImpl<>(result, pageable, total);
    }

    @Override
    public Page<Order> findByDynamicTypeWithShopId(String keyword, String type, Pageable pageable, Long shopId) {
        QOrder order = QOrder.order;

        BooleanExpression expression = getPredicate(keyword, type)
                .and(order.orderItems.any().product.shop.id.eq(shopId));

        if (expression == null) {
            return new PageImpl<>(List.of(), pageable, 0);
        }

        List<Order> result = queryFactory.selectFrom(order)
                .where(expression)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(order.id.desc())
                .fetch();

        long total = queryFactory.selectFrom(order)
                .where(expression)
                .fetchCount();

        return new PageImpl<>(result, pageable, total);
    }

    private BooleanExpression getPredicate(String keyword, String type) {
        if (keyword == null || keyword.isEmpty() || type == null || type.isEmpty()) {
            return null; // 조건이 없을 경우 null 반환
        }

        // 검색 선택 조건에 따라 where 조건 표현식 생성
        BooleanExpression expression = null;

        if (type.equals("orderNo")) {
            expression = QOrder.order.id.eq(Long.valueOf(keyword));
            log.info("Type expression: " + expression);
        } else if (type.equals("orderId")) {
            expression = QOrder.order.member.uid.containsIgnoreCase(keyword);
        } else if (type.equals("orderId")) {
            expression = QOrder.order.member.name.containsIgnoreCase(keyword); // GeneralMember의 id 사용
        }
        return expression; // 생성된 조건 반환
    }

    // 새로운 findByShopId 메서드 추가
    public Page<Order> findByShopId(Long shopId, Pageable pageable) {
        QOrder order = QOrder.order;
        QOrderItem orderItem = QOrderItem.orderItem;
        QProduct product = QProduct.product;

        BooleanExpression expression = order.orderItems.any().product.shop.id.eq(shopId);

        List<Order> result = queryFactory.selectFrom(order)
                .join(order.orderItems, orderItem).fetchJoin()
                .join(orderItem.product, product).fetchJoin()
                .where(expression)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(order.id.desc())
                .fetch();

        long total = queryFactory.selectFrom(order)
                .join(order.orderItems, orderItem)
                .join(orderItem.product, product)
                .where(expression)
                .fetchCount();

        return new PageImpl<>(result, pageable, total);
    }
}

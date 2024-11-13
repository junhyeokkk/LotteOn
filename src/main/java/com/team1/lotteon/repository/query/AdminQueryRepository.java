package com.team1.lotteon.repository.query;

import com.querydsl.core.types.dsl.*;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.team1.lotteon.entity.QArticle;
import com.team1.lotteon.entity.QMember;
import com.team1.lotteon.entity.QOrder;
import com.team1.lotteon.entity.QOrderItem;
import com.team1.lotteon.entity.enums.OrderStatus;
import com.team1.lotteon.repository.query.dto.*;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.querydsl.jpa.JPAExpressions.select;

@Repository
public class AdminQueryRepository {
    private final JPAQueryFactory queryFactory;
    private final QOrder order = QOrder.order;
    private final QOrderItem orderItem = QOrderItem.orderItem;
    private final QMember member = QMember.member;
    private final QArticle article = QArticle.article;

    public AdminQueryRepository(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    public List<OrderDailyQueryDTO> findOrderDailyQueryLastFourDays() {

        LocalDateTime today = LocalDateTime.now();
        LocalDateTime fourDaysAgo = today.minusDays(3);

        // 기본 날짜 목록을 생성하고 카운트를 0으로 초기화
        Map<String, OrderDailyQueryDTO> dailyMap = new HashMap<>();
        for (int i = 0; i <= 3; i++) {
            LocalDate date = today.minusDays(i).toLocalDate();
            dailyMap.put(date.toString(), new OrderDailyQueryDTO(date.toString(), 0, 0, 0));
        }


        // 상태별로 개수를 계산하는 CaseBuilder 설정
        NumberExpression<Integer> orderCount = new CaseBuilder().when(order.status.eq(OrderStatus.ORDERED)).then(1).otherwise(0).sum();

        NumberExpression<Integer> deliveredCount = new CaseBuilder().when(order.status.eq(OrderStatus.DELIVERED)).then(1).otherwise(0).sum();

        NumberExpression<Integer> completeCount = new CaseBuilder().when(order.status.eq(OrderStatus.COMPLETE)).then(1).otherwise(0).sum();

        // LocalDateTime -> LocalDate 변환 예제
        StringTemplate stringTemplate = Expressions.stringTemplate("DATE_FORMAT({0}, {1})", order.orderDate, "%Y-%m-%d");
        List<OrderDailyQueryDTO> results = queryFactory
                .select(new QOrderDailyQueryDTO(
                        stringTemplate,
                        orderCount,
                        deliveredCount,
                        completeCount))
                .from(order)
                .where(order.orderDate.between(fourDaysAgo, today))
                .groupBy(stringTemplate)  // 날짜만 기준으로 그룹화
                .orderBy(stringTemplate.asc())  // 날짜만 기준으로 정렬
                .fetch();

        // 조회된 데이터로 날짜별 Map을 업데이트
        for (OrderDailyQueryDTO result : results) {
            dailyMap.put(result.getOrderDate(), result);
        }

        // Map을 날짜순으로 정렬하여 리스트로 반환
        return dailyMap.values().stream()
                .sorted((a, b) -> LocalDate.parse(a.getOrderDate()).compareTo(LocalDate.parse(b.getOrderDate())))
                .toList();
    }

    public List<OrderItemSalesRatioQueryDTO> findOrderItemSalesRatioQuery() {
        List<OrderItemSalesRatioQueryDTO> orderItemSalesRatioQueryDTOList = queryFactory
                .select(
                        new QOrderItemSalesRatioQueryDTO(
                                orderItem.product.category.name,
                                orderItem.product.price.sum()
                        )
                )
                .from(orderItem)
                .groupBy(orderItem.product.category.id)
                .fetch();
        return orderItemSalesRatioQueryDTOList;
    }

    public Optional<OperatingStatusQueryDTO> findOperatingStatusQuery() {
        try {
            OperatingStatusQueryDTO operatingStatusQueryDTO = queryFactory
                    .select(new QOperatingStatusQueryDTO(
                            select(order.count()).from(order),
                            select(Expressions.asNumber(order.totalPrice.sum()).longValue()).from(order),
                            select(member.count()).from(member),
                            select(member.count()).from(member),
                            select(article.count()).from(article).where(article.createdAt.between(LocalDateTime.now().minusDays(1), LocalDateTime.now()))
                    ))
                    .from(order)
                    .fetchFirst();

            return Optional.ofNullable(operatingStatusQueryDTO);

        } catch (Exception e) {
            return Optional.of(new OperatingStatusQueryDTO());
        }
    }
}

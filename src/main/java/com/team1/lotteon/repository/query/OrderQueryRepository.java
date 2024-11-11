package com.team1.lotteon.repository.query;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.*;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.team1.lotteon.entity.QOrder;
import com.team1.lotteon.entity.enums.OrderStatus;
import com.team1.lotteon.repository.query.dto.OrderDailyQueryDTO;
import com.team1.lotteon.repository.query.dto.QOrderDailyQueryDTO;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class OrderQueryRepository {
    private final JPAQueryFactory queryFactory;

    public OrderQueryRepository(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    public List<OrderDailyQueryDTO> findOrderDailyQueryLastFourDays() {
        QOrder order = QOrder.order;
        LocalDateTime today = LocalDateTime.now();
        LocalDateTime fourDaysAgo = today.minusDays(3);

        // 상태별로 개수를 계산하는 CaseBuilder 설정
        NumberExpression<Integer> orderCount = new CaseBuilder()
                .when(order.status.eq(OrderStatus.ORDERED)).then(1)
                .otherwise(0).sum();

        NumberExpression<Integer> deliveredCount = new CaseBuilder()
                .when(order.status.eq(OrderStatus.DELIVERED)).then(1)
                .otherwise(0).sum();

        NumberExpression<Integer> completeCount = new CaseBuilder()
                .when(order.status.eq(OrderStatus.COMPLETE)).then(1)
                .otherwise(0).sum();

        // LocalDateTime -> LocalDate 변환 예제
        StringTemplate stringTemplate = Expressions.stringTemplate(
                "CONVERT(CHAR(10), {0})",
                order.orderDate
        );
        List<OrderDailyQueryDTO> dailyStatistics = queryFactory
                .select(new QOrderDailyQueryDTO(
                        stringTemplate,
                        orderCount,
                        deliveredCount,
                        completeCount
                ))
                .from(order)
                .where(order.orderDate.between(fourDaysAgo, today))
                .groupBy(stringTemplate)  // 날짜만 기준으로 그룹화
                .orderBy(stringTemplate.asc())  // 날짜만 기준으로 정렬
                .fetch();

        return dailyStatistics;
    }
}

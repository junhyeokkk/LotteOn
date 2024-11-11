package com.team1.lotteon.repository.query;

import com.querydsl.core.types.ConstantImpl;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
}

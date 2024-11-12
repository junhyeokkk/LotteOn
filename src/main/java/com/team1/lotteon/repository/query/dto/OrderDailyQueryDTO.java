package com.team1.lotteon.repository.query.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Builder
public class OrderDailyQueryDTO {
    private String orderDate;
    private Integer orderCount;
    private Integer deliveredCount;
    private Integer completeCount;

    @QueryProjection
    public OrderDailyQueryDTO(String orderDate, Integer orderCount, Integer deliveredCount, Integer completeCount) {
        this.orderDate = orderDate;
        this.orderCount = orderCount;
        this.deliveredCount = deliveredCount;
        this.completeCount = completeCount;
    }
}

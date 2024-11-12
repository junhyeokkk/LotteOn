package com.team1.lotteon.repository.query.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
public class OperatingStatusQueryDTO {
    private long orderCount;
    private long orderPrice;
    private long memberRegistration;
    private long visitCount;
    private long newPostCount;

    @QueryProjection
    public OperatingStatusQueryDTO(long orderCount, long orderPrice, long memberRegistration, long visitCount, long newPostCount) {
        this.orderCount = orderCount;
        this.orderPrice = orderPrice;
        this.memberRegistration = memberRegistration;
        this.visitCount = visitCount;
        this.newPostCount = newPostCount;
    }
}

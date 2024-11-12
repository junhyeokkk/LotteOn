package com.team1.lotteon.repository.query.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
public class OrderItemSalesRatioQueryDTO {
    private String cateName;
    private Integer salesPrice;

    @QueryProjection
    public OrderItemSalesRatioQueryDTO(String cateName, Integer salesPrice) {
        this.cateName = cateName;
        this.salesPrice = salesPrice;
    }
}

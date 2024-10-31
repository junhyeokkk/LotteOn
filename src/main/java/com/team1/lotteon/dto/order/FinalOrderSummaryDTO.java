package com.team1.lotteon.dto.order;

import lombok.*;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FinalOrderSummaryDTO {
    private int totalQuantity;       // 총 상품 수량
    private int totalOriginalPrice;  // 총 상품 금액 (할인 전)
    private int totalDiscount;       // 총 할인 금액
    private int totalDeliveryFee;    // 총 배송비
    private int totalOrderAmount;    // 전체 주문 금액
    private int totalEarnedPoints;   // 적립 포인트
}

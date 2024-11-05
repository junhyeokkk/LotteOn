package com.team1.lotteon.dto.order;

import com.team1.lotteon.entity.GeneralMember;
import com.team1.lotteon.entity.Member;
import com.team1.lotteon.entity.Order;
import com.team1.lotteon.entity.enums.PaymentMethod;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderSummaryDTO {
    // 주문 및 결제 정보
    private Long orderId;
    private String orderNumber;
    private PaymentMethod paymentMethod;
    private String ordererName;
    private String ordererPhone;

    // 수취인 정보
    private String recipientName;
    private String recipientPhone;
    private String recipientAddress;

    // 요약된 주문 항목 리스트
    private List<OrderItemSummaryDTO> orderItems;

    // 총 금액 정보
    private int totalOriginalPrice;
    private int totalDiscountAmount;
    private int totalQuantity;
    private int totalDeliveryFee;
    private int totalPaymentAmount;
    private int totalOrderAmount; // 최종 결제금액

    // 생성자
    public OrderSummaryDTO(Order order) {
        this.orderId = order.getId();
        this.orderNumber = order.getOrderNumber(); // 주문 번호
        this.paymentMethod = order.getPaymentMethod(); // 결제방법
        this.ordererName = order.getMember().getName(); // 주문자 이름
        this.ordererPhone = order.getMember().getPh(); // 주문자 연락처
        this.totalOrderAmount = order.getTotalPrice(); // 주문 전체 금액

        this.recipientName = order.getRecipientName(); // 수취인 이름
        this.recipientPhone = order.getRecipientPhone(); // 수취인 연락처
        this.recipientAddress = order.getRecipientAddr1() + " " + order.getRecipientAddr2(); // 수취인 주소

        // OrderItemSummaryDTO 리스트 생성
        this.orderItems = order.getOrderItems().stream()
                .map(OrderItemSummaryDTO::new)
                .collect(Collectors.toList());

        // 총 상품 금액, 총 할인 금액, 배송비 계산
        this.totalOriginalPrice = orderItems.stream().mapToInt(OrderItemSummaryDTO::getOriginalPrice).sum();
        this.totalDiscountAmount = orderItems.stream().mapToInt(OrderItemSummaryDTO::getDiscountAmount).sum()+order.getCouponDiscount()+order.getPointDiscount();
        this.totalQuantity = orderItems.stream().mapToInt(OrderItemSummaryDTO::getQuantity).sum();
        this.totalDeliveryFee = order.getDeliveryFee();
        this.totalPaymentAmount = totalOrderAmount; // 결제 금액
    }
}

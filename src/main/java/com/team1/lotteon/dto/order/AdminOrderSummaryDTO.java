package com.team1.lotteon.dto.order;

import com.team1.lotteon.entity.Order;
import com.team1.lotteon.entity.enums.PaymentMethod;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;
/*
    날짜 : 2024/11/3
    이름 : 최준혁
    내용 : 주문 제출내역 DTO 생성 (관리자쪽 주문상세)
*/
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminOrderSummaryDTO {
    // 주문 및 결제 정보
    private Long orderId; // 주문 번호
    private String orderNumber; // 주문 번호(혹시 가공할 가능성 때문에 놔둠)
    private String paymentMethod; // 결제방법
    private String ordererName; // 주문자 이름
    private String ordererPhone; // 주문자 연락처
    private String status; // 주문상태

    // 수취인 정보
    private String recipientName; // 수취인 이름
    private String recipientPhone; // 수취인 연락처
    private String recipientAddress; // 수취인 배송지 주소

    // 요약된 주문 항목 리스트
    private List<AdminOrderItemSummaryDTO> orderItems;

    // 총 금액 정보
    private int totalOriginalPrice; // 총상품금액
    private int totalDiscountAmount; // 총할인금액
    private int totalQuantity; // 총 수량
    private int totalDeliveryFee; // 총 배달료
    private int totalPaymentAmount;
    private int totalOrderAmount; // 최종 결제금액

    // 생성자
    public AdminOrderSummaryDTO(Order order) {
        this.orderId = order.getId();
        this.orderNumber = order.getOrderNumber(); // 주문 번호
        this.paymentMethod = order.getPaymentMethod().getKoreanLabel(); // 결제방법
        this.ordererName = order.getMember().getName(); // 주문자 이름
        this.ordererPhone = order.getMember().getPh(); // 주문자 연락처
        this.totalOrderAmount = order.getTotalPrice(); // 주문 전체 금액
        this.status = order.getStatus().toString(); // 주문 상태

        this.recipientName = order.getRecipientName(); // 수취인 이름
        this.recipientPhone = order.getRecipientPhone(); // 수취인 연락처
        this.recipientAddress = order.getRecipientAddr1() + " " + order.getRecipientAddr2(); // 수취인 주소

        // OrderItemSummaryDTO 리스트 생성
        this.orderItems = order.getOrderItems().stream()
                .map(AdminOrderItemSummaryDTO::new)
                .collect(Collectors.toList());

        // 총 상품 금액, 총 할인 금액, 배송비 계산
        this.totalDiscountAmount = orderItems.stream().mapToInt(AdminOrderItemSummaryDTO::getDiscountAmount).sum();
        this.totalQuantity = orderItems.stream().mapToInt(AdminOrderItemSummaryDTO::getQuantity).sum();
        this.totalOriginalPrice = orderItems.stream().mapToInt(AdminOrderItemSummaryDTO::getOriginalPrice).sum() * totalQuantity;
        this.totalDeliveryFee = order.getDeliveryFee();
        this.totalPaymentAmount = totalOrderAmount; // 결제 금액
    }
}

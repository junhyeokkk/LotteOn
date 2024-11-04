package com.team1.lotteon.dto.order;

import com.team1.lotteon.entity.OrderItem;
import com.team1.lotteon.entity.enums.DeliveryStatus;
import lombok.*;
import lombok.experimental.SuperBuilder;

/*
    날짜 : 2024/11/4
    이름 : 최준혁
    내용 : 배송 등록을 위한 주문 아이템 DTO 생성
*/
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class OrderItemDeilveryDTO {

    private Long productId;
    private String productName;
    private String productImg;
    private String shopName;
    private int price;
    private int quantity;
    private int deliveryFee;
    private int totalPrice;

    private Long orderId;
    private Long orderItemId;

    private String recipientName;
    private String recipientPhone;
    private String recipientAddr1;

    // OrderItem을 매개변수로 받는 생성자 추가
    public OrderItemDeilveryDTO(OrderItem orderItem) {
        this.orderItemId = orderItem.getId();
        this.productId = orderItem.getProduct().getId();
        this.productName = orderItem.getProduct().getProductName();
        this.productImg = orderItem.getProduct().getProductImg1();
        this.shopName = orderItem.getProduct().getShop().getShopName();
        this.price = orderItem.getOrderPrice();
        this.quantity = orderItem.getQuantity();
        this.deliveryFee = orderItem.getDeliveryFee();
        this.totalPrice = orderItem.getOrderPrice() * orderItem.getQuantity();
        this.orderId = orderItem.getOrder().getId();
        this.recipientName = orderItem.getOrder().getRecipientName();
        this.recipientPhone = orderItem.getOrder().getRecipientPhone();
        this.recipientAddr1 = orderItem.getOrder().getRecipientAddr1();
    }
}

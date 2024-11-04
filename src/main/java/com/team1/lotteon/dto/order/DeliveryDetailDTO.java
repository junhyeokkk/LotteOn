package com.team1.lotteon.dto.order;

import com.team1.lotteon.entity.Delivery;
import com.team1.lotteon.entity.enums.DeliveryStatus;
import lombok.*;

/*
    날짜 : 2024/11/4
    이름 : 최준혁
    내용 : 배송 세부내용 출력을 위한 DTO 생성 (관리자쪽 배송상세)
*/
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class DeliveryDetailDTO {

    private Long id;
    private Long productId;
    private Long orderId;
    private Long orderItemId;
    private String productImage;
    private String productName;
    private String sellerName;
    private int price;
    private int quantity;
    private int deliveryFee;
    private int totalPrice;

    private String recipientPhone;
    private String recipientAddress;
    private String recipientName;

    private String deliveryCompany;
    private String invoiceNumber;
    private String memo;
    private String deliveryDate;
    private DeliveryStatus status;

    // Delivery 엔티티를 이용한 생성자
    public DeliveryDetailDTO(Delivery delivery) {
        this.productId = delivery.getOrderItem().getProduct().getId();
        this.id = delivery.getId();
        this.orderId = delivery.getOrderItem().getOrder().getId();
        this.orderItemId = delivery.getOrderItem().getId();
        this.productImage = delivery.getOrderItem().getProduct().getProductImg1();
        this.productName = delivery.getOrderItem().getProduct().getProductName();
        this.sellerName = delivery.getOrderItem().getProduct().getShop().getShopName();
        this.price = delivery.getOrderItem().getProduct().getPrice();
        this.quantity = delivery.getOrderItem().getQuantity();
        this.deliveryFee = delivery.getOrderItem().getDeliveryFee();
        this.totalPrice = delivery.getOrderItem().getOrderPrice();
        this.recipientName = delivery.getOrderItem().getOrder().getRecipientName();
        this.recipientPhone = delivery.getOrderItem().getOrder().getRecipientPhone();
        this.recipientAddress = delivery.getAddr1() + " " + delivery.getAddr2();
        this.deliveryCompany = delivery.getDelCompany();
        this.invoiceNumber = delivery.getInvoiceNum();
        this.memo = delivery.getMemo();
        this.deliveryDate = delivery.getDeliveryDate() != null ? delivery.getDeliveryDate().toString() : null;
        this.status = delivery.getStatus();
    }
}

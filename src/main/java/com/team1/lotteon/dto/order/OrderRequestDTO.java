package com.team1.lotteon.dto.order;

import com.team1.lotteon.entity.enums.PaymentMethod;
import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequestDTO {
    private String recipientName;
    private String recipientPhone;
    private String recipientZip;
    private String recipientAddr1;
    private String recipientAddr2;
    private String etc;
    private int usedPoint;
    private int totalPrice;
    private Long couponId;
    private PaymentMethod paymentMethod;
    private List<OrderItemDTO> orderItems;

    private int couponDiscount;
    private int pointDiscount;

}

package com.team1.lotteon.dto.order;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.team1.lotteon.entity.Order;
import com.team1.lotteon.entity.Product;
import com.team1.lotteon.entity.enums.DeliveryStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

/*
    날짜 : 2024/10/31
    이름 : 최준혁
    내용 : 주문 아이템 DTO 생성

*/
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"order", "product"}) // 순환 참조 방지
public class OrderItemDTO {

    private Long id;

    private int point;  // 구매 적립 포인트

    private int discountRate;   // 구매 할인률
    private int deliveryFee;    // 구매 배달료

    private int quantity;   // 수량
    private int orderPrice;  // 구매 가격

    private DeliveryStatus deliveryStatus;  // 배달상태

    private Long productId;       // 상품 ID (Product 대신)
    private String productName;   // 상품명
    private Long orderId;         // 주문 ID (Order 대신)
    private Long productOptionCombinationId; // 조합아이디

    private OrderDTO order;

    private Long cartId; // 카트 아이디 (카트에 담겨진 상품인지)

    private Product product;

    private String productOptionCombinationFormatting; // 조합 value 가공한 문자열
}

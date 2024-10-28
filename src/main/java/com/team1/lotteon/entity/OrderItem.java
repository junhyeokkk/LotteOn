package com.team1.lotteon.entity;

import com.team1.lotteon.entity.enums.DeliveryStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

/*
    날짜 : 2024/10/25
    이름 : 이상훈
    내용 : 주문 아이템 엔티티 생성
*/
@Entity
@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int purchasePrice;  // 구매 가격
    private int point;  // 구매 적립 포인트
    private int discountRate;   // 구배 할인률
    private int deliveryFee;    // 구배 배달료
    private int quantity;   // 갯수
    @Enumerated(EnumType.STRING)
    private DeliveryStatus deliveryStatus;  // 배달상태
    @OnDelete(action = OnDeleteAction.CASCADE)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;
    @OnDelete(action = OnDeleteAction.CASCADE)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;
    @OnDelete(action = OnDeleteAction.CASCADE)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="order_id")
    private Order order;
}

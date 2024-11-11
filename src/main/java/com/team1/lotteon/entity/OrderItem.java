package com.team1.lotteon.entity;

import com.team1.lotteon.entity.enums.DeliveryStatus;
import com.team1.lotteon.entity.productOption.ProductOptionCombination;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.List;

/*
    날짜 : 2024/10/25
    이름 : 이상훈
    내용 : 주문 아이템 엔티티 생성

    - 수정내역
    - 멤버 참조 컬럼 삭제 (10/31 준혁)
*/
@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int point;             // 구매 적립 포인트
    private int discountRate;      // 구매 할인률
    private int deliveryFee;       // 구매 배달료
    private int quantity;          // 수량
    private int orderPrice;        // 최종 구매 가격

    @Enumerated(EnumType.STRING)
    @Column(name = "delivery_status", length = 20) // Enum의 최대 길이에 맞게 조정
    private DeliveryStatus deliveryStatus;  // 배달상태

    @OnDelete(action = OnDeleteAction.CASCADE)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @OnDelete(action = OnDeleteAction.CASCADE)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "option_combination_id")
    private ProductOptionCombination productOptionCombination; // 옵션 조합

    @Column(columnDefinition = "TEXT")
    private String optionCombinationSnapshot;  // 주문 당시 조합 정보 스냅샷


    private int skuVersion; // SKU의 버전 저장

    @OneToOne(mappedBy = "orderItem", cascade = CascadeType.ALL)
    private Delivery delivery; // 개별 배송 정보 설정
}

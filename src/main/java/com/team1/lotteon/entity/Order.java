package com.team1.lotteon.entity;

import com.team1.lotteon.entity.enums.OrderStatus;
import com.team1.lotteon.entity.enums.PaymentMethod;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/*
    날짜 : 2024/10/25
    이름 : 이상훈
    내용 : 주문 엔티티 생성

    - 수정내역
    - 수령자 정보, 배달, 쿠폰 컬럼 추가 및 수정 (10/31 준혁)
    - 주문일자 명칭 정확하게 매핑 하도록 변경 (11/12 도영)
*/
@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "orders")
public class Order {


    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "order_date")
    private LocalDateTime orderDate; // 주문 일자

    private String orderNumber;  // 주문 번호

    private int usedPoint;  // 사용 포인트

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20) // Enum의 최대 길이에 맞게 조정
    private OrderStatus status; // 주문상태

    private int deliveryFee; // 전체 배달료

    @OnDelete(action = OnDeleteAction.CASCADE)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private GeneralMember member; // 주문자
    
    private int couponDiscount;
    private int pointDiscount;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>(); // OrderItem리스트

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method", length = 10) // 10 자로 설정하되 Enum 길이에 맞게 조정 필요
    private PaymentMethod paymentMethod;  // 결제방법

    private String etc;
    private int totalPrice; // 주문 전체 금액

    // 수령자 정보
    private String recipientName;     // 수령자 이름
    private String recipientPhone;    // 수령자 연락처
    private String recipientZip;  // 수령자 우편번호
    private String recipientAddr1;  // 수령자 기본주소
    private String recipientAddr2;  // 수령자 상세주소

    // 총 배달료 계산 메서드
    public void calculateTotalDeliveryFee() {
        this.deliveryFee = orderItems.stream()
                .mapToInt(OrderItem::getDeliveryFee)
                .sum();
    }
}

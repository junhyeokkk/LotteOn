package com.team1.lotteon.dto.order;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.team1.lotteon.entity.GeneralMember;
import com.team1.lotteon.entity.OrderItem;
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
    날짜 : 2024/11/1
    이름 : 최준혁
    내용 : 주문 DTO 생성
*/
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {


    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime orderDate; // 주문 일자

    private String orderNumber;  // 주문 번호

    private int usedPoint;  // 사용 포인트

    private OrderStatus status; // 주문상태

    private int deliveryFee; // 전체 배달료
    @JsonIgnore
    private GeneralMember member; // 주문자

//    @ManyToOne
//    @JoinColumn(name = "coupon_id")
//    private Coupon coupon; // 쿠폰

    private List<OrderItem> orderItems = new ArrayList<>(); // OrderItem리스트

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

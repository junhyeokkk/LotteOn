package com.team1.lotteon.entity;

import com.team1.lotteon.entity.enums.PaymentMethod;
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
    내용 : 주문 엔티티 생성
*/
@Entity
@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "orders")
public class Order {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String recipient;   // 수령자
    private String orderNum;  // 번호

    @Embedded
    private Address address;    // 주소
    private int usedPoint;  // 사용 포인트
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;   // 결제방법

    @OnDelete(action = OnDeleteAction.CASCADE)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private GeneralMember member;
    
}

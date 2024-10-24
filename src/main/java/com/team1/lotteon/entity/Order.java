package com.team1.lotteon.entity;

import com.team1.lotteon.entity.enums.PaymentMethod;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

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

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "member_id")
    private GeneralMember member;
    
}

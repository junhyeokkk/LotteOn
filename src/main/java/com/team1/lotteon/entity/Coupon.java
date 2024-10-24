package com.team1.lotteon.entity;

import com.team1.lotteon.entity.enums.CouponStatus;
import jakarta.persistence.*;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Coupon {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;    // id(PK)
    private String name;    // 쿠폰명
    private int discountAmount; // 할인금액
    private String criteria;    // 적용기준/제한조건
    

    @Enumerated(EnumType.STRING)
    private CouponStatus couponStatus;  // 상태
    private LocalDateTime validUntil;   // 유효기간


    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "member_id")
    private Member member;
}

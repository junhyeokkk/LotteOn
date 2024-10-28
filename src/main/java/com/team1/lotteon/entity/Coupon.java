package com.team1.lotteon.entity;

import com.team1.lotteon.entity.enums.CouponStatus;
import jakarta.persistence.*;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;

/*
    날짜 : 2024/10/25
    이름 : 이상훈
    내용 : 쿠폰 엔티티 생성
*/
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

    @OnDelete(action = OnDeleteAction.CASCADE)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;
}

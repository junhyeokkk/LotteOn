package com.team1.lotteon.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
/*
     날짜 : 2024/10/30
     이름 : 이도영(최초 작성자)
     내용 : CouponTake 생성
*/
@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "coupontake")
public class CouponTake {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "coupontakenid")
    private Long couponTakenId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_id", nullable = false, foreignKey = @ForeignKey(name = "FK_coupon_id"))
    private Coupon coupon;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false, foreignKey = @ForeignKey(name = "FK_member_id"))
    private Member member;

    @Column(name = "coupongetdate")
    private LocalDateTime couponGetDate;

    @Column(name = "couponexpiredate")
    private LocalDateTime couponExpireDate;

    @Column(name = "couponusedate")
    private LocalDateTime couponUseDate;

    @Column(name = "couponusecheck")
    private int couponUseCheck;
}
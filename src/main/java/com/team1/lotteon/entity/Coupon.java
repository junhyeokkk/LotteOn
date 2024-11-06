package com.team1.lotteon.entity;

import com.team1.lotteon.entity.enums.CouponStatus;
import jakarta.persistence.*;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/*
    날짜 : 2024/10/25
    이름 : 이상훈
    내용 : 쿠폰 엔티티 생성

    수정사항
        - 2024/10/29 이도영 - 엔티티 전체 변경
        - 2024/11/05 이도영 - coupondiscount (double) 변경
*/
@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Coupon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long couponid; //쿠폰번호

    //single, ordersale, freedelivery
    private String coupontype; //쿠폰종류
    private String couponname; //쿠폰명
    private double coupondiscount; // 할인혜택

    private LocalDateTime couponstart; //시작시간
    private LocalDateTime couponend; //종료시간

    private int couponperiod; // 발급일로 부터 사용기간

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member; // 발급자를 찾기 위한 멤버 id

    @Builder.Default
    private Long coupongive = 0L; //발급수 기본 0

    @Builder.Default
    private Long couponuse = 0L; //사용수 기본 0
    
    //발급준비 ready, 발급중 start ,발급종료 stop
    @Builder.Default
    private String couponstate = "ready"; //발급 상태 기본 발급준비

    @CreationTimestamp
    private LocalDateTime couponmakedate; //발급생성일

    private String couponetc; //유의사항

    public void setMember(Member member) {
        this.member = member;
    }
    public void setCouponstart(LocalDateTime couponstart) {
        this.couponstart = couponstart;
    }
    public void setCouponend(LocalDateTime couponend) {
        this.couponend = couponend;
    }
    public void setCouponstate(String couponstate) {
        this.couponstate = couponstate;
    }
    public void setCoupongive(long l) {
        this.coupongive = l;
    }
    public void setCouponuse(long l) {
        this.couponuse = l;
    }
}

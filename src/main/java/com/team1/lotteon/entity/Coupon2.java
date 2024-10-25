package com.team1.lotteon.entity;
/*
     날짜 : 2024/10/25
     이름 : 강유정(최초 작성자)
     내용 : Coupon2 entity 생성
*/

import com.team1.lotteon.entity.enums.CouponStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Coupon2 {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;    // id(PK)
    private String num;    // 쿠폰번호
    private String type;    // 쿠폰종류
    private String name;    // 쿠폰명
    private int discountAmount; // 혜택

    private int couponnum; // 발급수
    private int couponuse; // 사용수

    @CreationTimestamp
    private LocalDateTime issuedate; // 발급일
    private LocalDate useStartDate; // 사용 시작일
    private LocalDate useEndDate;   // 사용 종료일
    private CouponStatus couponStatus;  // 쿠폰 상태

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "member_id")
    private Member member; // 발급자

}

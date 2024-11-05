package com.team1.lotteon.dto;
/*
     날짜 : 2024/10/25
     이름 : 강유정(최초 작성자)
     내용 : CouponDTO 생성
     수정사항
        - 2024/10/29 이도영 - 엔티티 전체 변경
        - 2024/11/05 이도영 - coupondiscount (double) 변경
*/

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CouponDTO {

    private Long couponid; //쿠폰번호
    private String coupontype; //쿠폰종류
    private String couponname; //쿠폰명
    private double coupondiscount; //할인 혜택

    private String couponstart; //시작시간

    private String couponend; //종료시간

    private int couponperiod; // 발급일로 부터 사용기간
    private String memberId; // 발급자 멤버 ID
    private Long coupongive; //발급수
    private Long couponuse; //사용수
    private String couponstate; //발급 상태
    private String couponmakedate; //발급생성일
    private String couponmaketime; //발급생성일의 시간
    private String couponetc; //유의사항
    private String issuerInfo;

}

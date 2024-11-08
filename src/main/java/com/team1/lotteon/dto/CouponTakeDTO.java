package com.team1.lotteon.dto;
/*
     날짜 : 2024/10/30
     이름 : 이도영(최초 작성자)
     내용 : CouponTakeDTO 생성
     
     수정내용
        - 2024/10/31 이도영 shopid 추가
        - 2024/11/01 이도영 DTO 추가
        - 2024/11/05 이도영 받은 날짜에 대한 처리 방식 추가
        - 2024/11/08 이도영 발급 받은 쿠폰 날짜 처리
*/
import lombok.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CouponTakeDTO {
    private Long couponTakenId; //발급번호
    private Long couponId;  //쿠폰번호
    private String memberId;    //사용자
    private Long shopId;    //상점아이디
    private LocalDateTime couponGetDate;    //받은 날짜
    private LocalDateTime couponExpireDate; //파기 날짜
    private LocalDateTime couponUseDate;    //사용 날짜
    private int couponUseCheck;             //사용 여부

    // 추가된 필드 2024/11/01
    private String couponName;        // 쿠폰 이름
    private String username;          // 발급자 이름
    private double couponDiscount;    // 쿠폰 할인 정보
    private String shopName;          // 발급한 상점 이름
    private String couponGetDateFormatted;      //받은 날짜 수정
    private String couponExpireDateFormatted;   //파기 날짜 수정
    private String couponUseDateFormatted;      //사용 날짜 수정
    private String couponType;        //쿠폰 종류
    private String couponetc;       //상세정보

    private String couponExpireDateshow;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public CouponTakeDTO(String couponName, double couponDiscount, String shopName, LocalDateTime couponExpireDate) {
        this.couponName = couponName;
        this.couponDiscount = couponDiscount;
        this.shopName = shopName;
        this.couponExpireDateshow = couponExpireDate != null ? couponExpireDate.format(FORMATTER) : null;
    }

    public void setFormattedDates() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        this.couponGetDateFormatted = couponGetDate != null ? formatter.format(couponGetDate) : "N/A";
        this.couponExpireDateFormatted = couponExpireDate != null ? couponExpireDate.format(formatter) : "N/A";
        this.couponUseDateFormatted = couponUseDate != null ? couponUseDate.format(formatter) : "N/A";
    }
}

package com.team1.lotteon.dto;
/*
     날짜 : 2024/10/30
     이름 : 이도영(최초 작성자)
     내용 : CouponTakeDTO 생성
*/
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CouponTakeDTO {
    private Long couponTakenId;
    private Long couponId;
    private String memberId;
    private LocalDateTime couponGetDate;
    private LocalDateTime couponExpireDate;
    private LocalDateTime couponUseDate;
    private int couponUseCheck;
}

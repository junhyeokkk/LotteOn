package com.team1.lotteon.entity;

import jakarta.persistence.Embeddable;
import lombok.*;

/*
    날짜 : 2024/10/25
    이름 : 이상훈
    내용 : 주소 값타입 생성
*/
@Embeddable
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Address {
    private String zip;
    private String addr1;
    private String addr2;
}

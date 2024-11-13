package com.team1.lotteon.dto;

import com.team1.lotteon.entity.Address;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GeneralMemberDTO {
    private String uid;
    private int points;
    private int status;
    private LocalDateTime lastLoginDate;
    private LocalDateTime createdAt; // 가입일로 사용할 필드
    private String addr1;
    private String addr2;
    private String email;
    private String etc;
    private String name;
    private String ph;
    private String zip;
    private String gender;
    private String grade;
    private LocalDate birth;

    private Address address;
}

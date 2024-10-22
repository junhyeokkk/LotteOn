package com.team1.lotteon.dto;

import lombok.*;

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
    private Byte status;
    private LocalDateTime lastLoginDate;
    private String addr1;
    private String addr2;
    private String email;
    private String etc;
    private String name;
    private String ph;
    private String zip;
    private String gender;
    private String grade;
}

package com.team1.lotteon.dto;

import com.team1.lotteon.entity.Member;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

/*
    날짜 : 2024/11/01
    이름 : 강유정
    내용 : Recruit DTO 생성
*/
@ToString
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecruitDTO {

    private long recruitid;
    private String title; //제목
    private String etc; //비고

    private String position; //채용부서
    private String history; //경력사항
    private String type; //채용형태
    private String status; //상태
    private LocalDate displayStartDate; //모집기간(시작)
    private LocalDate displayEndDate; //모집기간(마감)

    private Member member; // 작성자를 찾기 위한 멤버 id
    private LocalDateTime rDate; //작성일

}

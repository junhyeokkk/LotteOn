/*
    날짜 : 2024/11/01
    이름 : 강유정
    내용 : 채용 엔티티 생성
*/
package com.team1.lotteon.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Getter
@Builder
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Recruit {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int recruitid;
    private String title; //제목
    private String etc; //비고

    private String position; //채용부서
    private String history; //경력사항
    private String type; //채용형태
    private String status; //상태
    private LocalDate displayStartDate; //모집기간(시작)
    private LocalDate displayEndDate; //모집기간(마감)

    //작성자
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member; // 작성자를 찾기 위한 멤버 id

    //작성일
    @CreationTimestamp
    private LocalDateTime rDate;
}

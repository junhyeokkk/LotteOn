package com.team1.lotteon.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

/*
    날짜 : 2024/10/25
    이름 : 이상훈
    내용 : 배너 엔티티 생성
*/
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Banner {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String size;
    private String backgroundColor;
    private String backgroundLink;
    private String img;

    private int isActive; // 활성화 상태: 1 = 활성, 0 = 비활성

    private String position;
    private LocalDate displayStartDate;
    private LocalDate displayEndDate;
    private LocalTime displayStartTime;
    private LocalTime displayEndTime;
    public void setIsActive(int isActive){
        this.isActive = isActive;
    }
}

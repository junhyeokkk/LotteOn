package com.team1.lotteon.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

/*
    날짜 : 2024/10/25
    이름 : 이상훈
    내용 : 배너 엔티티 생성
*/
@Entity
@Getter
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

    // 기본값을 false로 설정
    private boolean isActive = false;

    private String position;
    private LocalDate displayStartDate;
    private LocalDate displayEndDate;
    private LocalTime displayStartTime;
    private LocalTime displayEndTime;
    public void setIsActive(boolean isActive){
        this.isActive = isActive;
    }
}

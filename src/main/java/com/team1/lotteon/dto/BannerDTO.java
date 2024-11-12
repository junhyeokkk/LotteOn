package com.team1.lotteon.dto;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalTime;
/*
    날짜 : 2024/10/23
    이름 : 최준혁
    내용 : Banner DTO 생성
*/

@ToString
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BannerDTO {

    private int id;
    private String name;
    private String size;
    private String img;
    private MultipartFile banner_img;
    private String backgroundColor;
    private String backgroundLink;

    private int isActive;

    private String position;
    private LocalDate displayStartDate;
    private LocalDate displayEndDate;
    private LocalTime displayStartTime;
    private LocalTime displayEndTime;
}

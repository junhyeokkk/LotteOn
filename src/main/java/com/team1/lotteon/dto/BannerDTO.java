package com.team1.lotteon.dto;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalTime;

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

    private boolean isActive;

    private String position;
    private LocalDate displayStartDate;
    private LocalDate displayEndDate;
    private LocalTime displayStartTime;
    private LocalTime displayEndTime;
}

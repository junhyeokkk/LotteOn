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
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Banner {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String name;
    private String size;
    private String backgroundColor;
    private String backgroundLink;
    private boolean isActive;
    private String position;
    private LocalDate displayStartDate;
    private LocalDate displayEndDate;
    private LocalTime displayStartTime;
    private LocalTime displayEndTime;
}

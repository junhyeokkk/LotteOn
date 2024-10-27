package com.team1.lotteon.dto;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;

/*
    날짜 : 2024/10/24
    이름 : 최준혁
    내용 : Version DTO 생성
*/

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class VersionDTO {

    private Long id;
    private String version;

    private LocalDate rDate;
    private String content;

    // 외래키 목록
    private String uid;
}

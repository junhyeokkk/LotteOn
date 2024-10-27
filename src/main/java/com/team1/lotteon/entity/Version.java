package com.team1.lotteon.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
/*
    날짜 : 2024/10/24
    이름 : 최준혁
    내용 : Version 엔티티 생성
*/
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "version")
public class Version {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String version;

    @CreationTimestamp
    private LocalDate rDate;
    private String content;

    // 외래키 목록
    private String uid;
}

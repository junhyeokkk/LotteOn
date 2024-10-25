package com.team1.lotteon.entity;

import com.team1.lotteon.dto.TermDTO;
import jakarta.persistence.*;
import lombok.*;


/*
    날짜 : 2024/10/25
    이름 : 이상훈
    내용 : 약관 엔티티 생성
*/
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "term")
public class Term {
    @Id
    private String termCode; // 약관 코드

    @Lob
    private String content; // 약관 내용




}

package com.team1.lotteon.entity;

import com.team1.lotteon.dto.TermDTO;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "term")
public class Term {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private String termCode; // 약관 코드

    // Setter 메서드 추가
    @Setter
    @Lob
    private String content; // 약관 내용


    // DTO 변환 메서드
    public TermDTO toDTO() {
        return TermDTO.builder()
                .termCode(termCode)
                .content(content)
                .build();
    }
}

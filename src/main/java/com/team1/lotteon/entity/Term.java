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
    @Id
    private String termCode; // 약관 코드

    @Lob
    private String content; // 약관 내용




}

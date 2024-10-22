package com.team1.lotteon.entity;

import com.team1.lotteon.dto.TermDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "term")
public class Term {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private String termCode;
    @Lob
    private String content;

    public TermDTO toDTO(){
        return TermDTO.builder()
                .termCode(termCode)
                .content(content)
                .build();
    }


}

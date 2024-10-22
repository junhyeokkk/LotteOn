package com.team1.lotteon.dto;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import lombok.*;


@ToString
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor


public class TermDTO {
    private String termCode;
    private String content;


}

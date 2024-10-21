package com.team1.lotteon.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Data
// Article DTO
public class ArticleDTO {
    private Long id;
    private int views;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String content;
    private String title;

    private String type1;
    private String memberId;

}
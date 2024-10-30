package com.team1.lotteon.dto.cs;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

/*
*   날짜 : 2024/10/14
*   이름 : 이상훈
*   내용 : ArticleDTO 생성
*
*   수정이력
*   -2024/10/21 김소희 - DTO 구조 수정
*/


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

    private long displayNumber;
    private String type1;
    private String memberId;

}
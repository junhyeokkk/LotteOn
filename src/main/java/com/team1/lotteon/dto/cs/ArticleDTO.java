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
    protected Long id;
    protected int views;
    protected LocalDateTime createdAt;
    protected LocalDateTime updatedAt;
    protected String content;
    protected String title;

    protected long displayNumber;
    protected String type1;
    protected String memberId;

}
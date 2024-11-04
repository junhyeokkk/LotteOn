package com.team1.lotteon.dto.cs;

import lombok.*;
import lombok.experimental.SuperBuilder;
/*
 *   날짜 : 2024/10/21
 *   이름 : 김소희
 *   내용 : FaqDTO 생성
 *
 *   수정이력
 *   2024/10/25 김소희 - DTO 구조 수정
 *   2024/11/04 김소희 -  callSuper 추가
 */

@Getter
@Setter
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Data
@EqualsAndHashCode(callSuper = true)
public class FaqDTO extends ArticleDTO {
    private String type2;

}


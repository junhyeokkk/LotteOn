package com.team1.lotteon.dto.cs;

import lombok.*;
import lombok.experimental.SuperBuilder;
/*
 *   날짜 : 2024/10/21
 *   이름 : 김소희
 *   내용 : InquiryDTO 생성
 *
 *   수정이력
 *   2024/10/22 김소희 - DTO 구조 수정
 */

@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Data
@EqualsAndHashCode(callSuper = true)
public class InquiryDTO extends ArticleDTO {
    private String type2;
    private String answer;
}

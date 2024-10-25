package com.team1.lotteon.dto.cs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
/*
 *   날짜 : 2024/10/21
 *   이름 : 김소희
 *   내용 : NoticeDTO 생성
 *
 *   수정이력
 *   2024/10/25 김소희 - DTO 구조 수정
 */

@SuperBuilder
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper=true)
public class NoticeDTO extends ArticleDTO {
}

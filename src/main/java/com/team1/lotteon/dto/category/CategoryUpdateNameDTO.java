package com.team1.lotteon.dto.category;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
    날짜 : 2024/10/25
    이름 : 이상훈
    내용 : 카테고리 이름을 업데이트하는 요청 DTO 생성
*/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryUpdateNameDTO {
    private String name;
}

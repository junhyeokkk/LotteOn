package com.team1.lotteon.dto.category;

import com.team1.lotteon.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
    날짜 : 2024/10/25
    이름 : 이상훈
    내용 : 카테고리 생성시 요청되는 DTO 생성
*/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryCreateDTO {
    private String name;
    private int level;
    private Long parentId;

    public Category toEntity() {
        return Category.createCategory(name, level);
    }
}

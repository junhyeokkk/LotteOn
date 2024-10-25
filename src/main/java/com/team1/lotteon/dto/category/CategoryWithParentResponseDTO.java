package com.team1.lotteon.dto.category;

import com.team1.lotteon.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/*
    날짜 : 2024/10/25
    이름 : 이상훈
    내용 : 카테고리와 부모를 반환하는 응답 DTO 생성
*/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryWithParentResponseDTO {
    private Long id;
    private String name;
    private CategoryWithParentResponseDTO parent;

    public static CategoryWithParentResponseDTO fromEntity(Category category) {
        if(category == null) {
            return null;
        }

        return CategoryWithParentResponseDTO.builder()
                .id(category.getId())
                .name(category.getName())
                .parent(CategoryWithParentResponseDTO.fromEntity(category.getParent()))
                .build();
    }
}

package com.team1.lotteon.dto.category;

import com.team1.lotteon.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;


/*
    날짜 : 2024/10/25
    이름 : 이상훈
    내용 : 카테고리의 자식들을 트리형식으로 반환하는 응답 DTO 생성
*/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryWithChildrenResponseDTO {
    private Long id;
    private String name;
    @Builder.Default
    private List<CategoryWithChildrenResponseDTO> children = new ArrayList<>();

    public static CategoryWithChildrenResponseDTO fromEntity(Category category) {
        return CategoryWithChildrenResponseDTO.builder()
                .id(category.getId())
                .name(category.getName())
                .children(category.getChildren().stream()
                                .map(CategoryWithChildrenResponseDTO::fromEntity)
                                .toList())
                .build();
    }
}

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
    내용 : 카테고리의 부모 자식들을 반환하는 응답 DTO 생성
*/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryWithParentAndChildrenResponseDTO {
    private Long id;
    private String name;
    private CategoryWithParentResponseDTO parent;
    @Builder.Default
    private List<CategoryWithChildrenResponseDTO> children = new ArrayList<>();

    public static CategoryWithParentAndChildrenResponseDTO fromEntity(Category category) {
        return CategoryWithParentAndChildrenResponseDTO.builder()
                .id(category.getId())
                .name(category.getName())
                .parent(CategoryWithParentResponseDTO.fromEntity(category.getParent()))
                .children(category.getChildren().stream()
                        .map(CategoryWithChildrenResponseDTO::fromEntity)
                        .toList())
                .build();
    }
}

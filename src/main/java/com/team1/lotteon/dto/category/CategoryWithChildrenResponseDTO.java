package com.team1.lotteon.dto.category;

import com.team1.lotteon.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

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

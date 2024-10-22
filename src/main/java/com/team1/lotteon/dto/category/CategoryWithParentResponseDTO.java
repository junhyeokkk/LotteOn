package com.team1.lotteon.dto.category;

import com.team1.lotteon.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

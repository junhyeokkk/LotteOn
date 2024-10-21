package com.team1.lotteon.dto.category;

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
}

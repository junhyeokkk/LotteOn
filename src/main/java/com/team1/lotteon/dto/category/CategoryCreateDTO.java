package com.team1.lotteon.dto.category;

import com.team1.lotteon.entity.Category;
import com.team1.lotteon.entity.enums.CateLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryCreateDTO {
    private String name;
    private CateLevel level;
    private String parentId;

    public Category toEntity() {
        return Category.builder()
                .name(name)
                .level(level)
                .build();
    }
}

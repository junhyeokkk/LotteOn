package com.team1.lotteon.dto.category;

import com.team1.lotteon.entity.enums.CateLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryUpdateLevelDTO {
    private CateLevel level;
}

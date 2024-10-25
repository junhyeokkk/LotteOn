package com.team1.lotteon.dto.category;

import com.team1.lotteon.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
    날짜 : 2024/10/25
    이름 : 이상훈
    내용 : 클라이언트에 응답할 때 전송하는 기본 DTO 생성
*/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryResponseDTO {
    private Long id;
    private String name;
    private Long parentId;

    public static CategoryResponseDTO fromEntity(Category category) {
        return CategoryResponseDTO
                .builder()
                .id(category.getId())
                .name(category.getName())
                .parentId(category.getParent() != null ? category.getParent().getId() : null)
                .build();
    }
}

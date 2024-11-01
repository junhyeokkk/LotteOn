package com.team1.lotteon.dto.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductSearchRequestDto {
    @Builder.Default
    private int page = 0; // 기본 페이지 번호
    @Builder.Default
    private int size = 10; // 기본 페이지 크기
    @Builder.Default
    private String sortBy = "createdAt"; // 기본 정렬 필드
    @Builder.Default
    private String sortDirection = "Desc"; // 기본 정렬 방향 (ASC or DESC)

    // 검색 조건 필드들 (예: keyword, categoryId 등)
    private String keyword;
    private Long categoryId;

    public Pageable toPageable() {
        // 정렬 방향 결정
        Sort.Direction direction = Sort.Direction.fromString(sortDirection);

        // Sort 객체 생성
        Sort sort = Sort.by(direction, sortBy);

        // Pageable 객체 생성 (page는 0-based)
        return PageRequest.of(page, size, sort);
    }

}

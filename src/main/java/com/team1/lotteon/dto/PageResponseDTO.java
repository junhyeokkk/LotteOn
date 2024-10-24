package com.team1.lotteon.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class PageResponseDTO<T> {
    private List<T> content;
    private int currentPage; // 현재 페이지
    private int totalPages;
    private long totalElements; // 총 요소 수
    private int pageSize; // 페이지 당 요소
    private boolean isFirst; // 첫 페이지 여부
    private boolean isLast; // 마지막 페이지 여부

    public static <T> PageResponseDTO<T> fromPage(Page<T> page) {
        return PageResponseDTO.<T>builder()
                .content(page.getContent())
                .currentPage(page.getNumber())
                .totalPages(page.getTotalPages())
                .totalElements(page.getTotalElements())
                .pageSize(page.getSize())
                .isFirst(page.isFirst())
                .isLast(page.isLast())
                .build();
    }
}

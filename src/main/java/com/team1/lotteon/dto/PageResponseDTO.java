package com.team1.lotteon.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
/*
 *   날짜 : 2024/10/23
 *   이름 : 김소희
 *   내용 : PageResponseDTO 생성
 *
 *   수정이력
 *   2024/10/25 이상훈 - isFirst 추가
 */

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


    public static <T> PageResponseDTO<T> fromList(List<T> list, Pageable pageable) {
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<T> content;

        if (list.size() < startItem) {
            content = List.of(); // 페이지 범위 밖이면 빈 리스트
        } else {
            int toIndex = Math.min(startItem + pageSize, list.size());
            content = list.subList(startItem, toIndex);
        }

        return PageResponseDTO.<T>builder()
                .content(content)
                .currentPage(currentPage)
                .totalPages((int) Math.ceil((double) list.size() / pageSize))
                .totalElements(list.size())
                .pageSize(pageSize)
                .isFirst(currentPage == 0)
                .isLast((currentPage + 1) * pageSize >= list.size())
                .build();
    }

}

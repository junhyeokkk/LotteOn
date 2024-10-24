package com.team1.lotteon.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Builder
public class PageRequestDTO {
    private int pg;   // 요청 페이지
    private int size; // 페이지 크기

    public PageRequestDTO(int pg, int size) {
        this.pg = pg;
        this.size = size;
    }
}
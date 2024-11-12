package com.team1.lotteon.dto.order;

import lombok.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
/*
    날짜 : 2024/11/12
    이름 : 이도영
    내용 : 오더 아이템 페이지 처리  생성
*/
@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemPageRequestDTO {
    @Builder.Default
    private int no = 1;

    @Builder.Default
    private int pg = 1;

    @Builder.Default
    private int size = 10;

    private String type;
    private String keyword;

    private String startDate; // 추가된 필드
    private String endDate;   // 추가된 필드

    public Pageable getPageable(String sort) {
        return PageRequest.of(this.pg -1 , this.size, Sort.by(sort).descending());
    }
}

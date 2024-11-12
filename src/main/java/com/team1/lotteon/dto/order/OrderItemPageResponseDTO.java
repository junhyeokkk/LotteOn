package com.team1.lotteon.dto.order;

import lombok.*;

import java.util.List;
/*
    날짜 : 2024/11/12
    이름 : 이도영
    내용 : 오더 아이템 페이지 처리  생성
*/
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemPageResponseDTO {
    private List<OrderItemDTO> dtoList;
    private int pg;
    private int size;
    private int total;
    private int startNo;
    private int start, end;
    private boolean prev, next;

    private String type;
    private String keyword;
    public OrderItemPageResponseDTO(OrderItemPageRequestDTO requestDTO, List<OrderItemDTO> orderItemDTOs, int totalElements) {
        this.pg = requestDTO.getPg();
        this.size = requestDTO.getSize();
        this.total = totalElements;
        this.dtoList = orderItemDTOs;

        this.type = requestDTO.getType();
        this.keyword = requestDTO.getKeyword();

        this.startNo = total - ((pg - 1) * size);
        this.end = (int) (Math.ceil(this.pg / 10.0)) * 10;
        this.start = this.end - 9;

        int last = (int) (Math.ceil(total / (double) size));

        this.end = end > last ? last : end;
        // end가 start보다 작을 때 end가 0 되는것 방지
        if (this.start > this.end) {
            this.end = this.start;
        }
        this.prev = this.start > 1;
        this.next = total > this.end * this.size;
    }
}

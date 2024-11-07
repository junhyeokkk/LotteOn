package com.team1.lotteon.dto.order;

import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class OrderPageResponseDTO {

    private List<OrderDTO> dtoList;
    private int pg;
    private int size;
    private int total;
    private int startNo;
    private int start, end;
    private boolean prev, next;

    private String type;
    private String keyword;



    // 생성자 수정: builder() 메서드 대신 이 생성자를 사용
    public OrderPageResponseDTO(OrderPageRequestDTO orderPageRequestDTO, List<OrderDTO> dtoList, int total) {
        this.pg = orderPageRequestDTO.getPg();
        this.size = orderPageRequestDTO.getSize();
        this.total = total;
        this.dtoList = dtoList;

        this.type = orderPageRequestDTO.getType();
        this.keyword = orderPageRequestDTO.getKeyword();

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

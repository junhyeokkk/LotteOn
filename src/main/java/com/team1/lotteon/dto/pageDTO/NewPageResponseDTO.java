package com.team1.lotteon.dto.pageDTO;
/*
     날짜 : 2024/10/28
     이름 : 이도영
     내용 : 페이지 처리

     수정이력
      - 2024/10/28 이도영 - 페이지 처리 추가
*/
import lombok.*;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class NewPageResponseDTO<T> {

    private List<T> dtoList;
    private int pg;
    private int size;
    private int artsize;
    private int total;
    private int startNo;
    private int start, end;
    private boolean prev, next;

    private int cateType;
    private String artcateType;

    @Builder
    public NewPageResponseDTO(NewPageRequestDTO newPageRequestDTO, List<T> dtoList, int total) {
        this.pg = newPageRequestDTO.getPg();
//        this.size  = newPageRequestDTO.getArtsize();
        //페이지별로 출력되는 목록 수
        this.size = newPageRequestDTO.getSize();
        this.total = total;
        this.dtoList = dtoList;

        this.startNo = total - ((pg -1) * size);
        this.end = (int) (Math.ceil(this.pg / 5.0)) * 5;
        this.start = this.end - 4;

        int last = (int) (Math.ceil(total / (double)size));
        this.end = end > last ? last : end;
        this.prev = this.start > 1;
        this.next = total > this.end * this.size;
    }

}

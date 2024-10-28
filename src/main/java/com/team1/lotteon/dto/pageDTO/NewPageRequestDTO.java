package com.team1.lotteon.dto.pageDTO;
/*
     날짜 : 2024/10/28
     이름 : 이도영
     내용 : 페이지 처리

     수정이력
      - 2024/10/28 이도영 - 페이지 처리 추가
*/
import lombok.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NewPageRequestDTO {

    @Builder.Default
    private int no = 1;

    @Builder.Default
    private int pg = 1;

    @Builder.Default
    private int size = 5;

    private int artsize = 10;

    private int cateType;
    private String artcateType;

    private String type;  // 검색 타입
    private String keyword;  // 검색어

    public Pageable getPageable(String sort, boolean isOrderDTO) {
        int pageSize = isOrderDTO ? this.artsize : this.size;
        return PageRequest.of(this.pg - 1, pageSize, Sort.by(sort).descending());
    }

}

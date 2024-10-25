package com.team1.lotteon.dto.Member;

import com.team1.lotteon.dto.GeneralMemberDTO;
import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class MemberPageResponseDTO {

    private List<GeneralMemberDTO> dtoList;

    private int pg;
    private int size;
    private int total;
    private int startNo;
    private int start, end;
    private boolean prev, next;

    private String type;
    private String keyword;


    @Builder
    public MemberPageResponseDTO(MemberPageRequestDTO pageRequestDTO, List<GeneralMemberDTO> dtoList, int total) {
        this.pg = pageRequestDTO.getPg();
        this.size = pageRequestDTO.getSize();
        this.total = total;
        this.dtoList = dtoList;

        this.type = pageRequestDTO.getType();
        this.keyword = pageRequestDTO.getKeyword();

        this.startNo = total - ((pg - 1) * size);
        this.end = (int) (Math.ceil(this.pg / 10.0)) * 10;
        this.start = this.end - 9;

        int last = (int) (Math.ceil(total / (double)size));
        this.end = end > last ? last : end;
        if (this.start > this.end) {
            this.end = this.start;
        }
        this.prev = this.start > 1;
        this.next = total > this.end * this.size;
    }
}

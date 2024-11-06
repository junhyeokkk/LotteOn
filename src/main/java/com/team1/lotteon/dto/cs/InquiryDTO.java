package com.team1.lotteon.dto.cs;

import com.team1.lotteon.entity.Inquiry;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
/*
 *   날짜 : 2024/10/21
 *   이름 : 김소희
 *   내용 : InquiryDTO 생성
 *
 *   수정이력
 *   2024/10/22 김소희 - DTO 구조 수정
 */

@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Data
@EqualsAndHashCode(callSuper = true)
public class InquiryDTO extends ArticleDTO {
    private String type2;
    private String answer;

    public InquiryDTO(Inquiry inquiry) {
        id=inquiry.getId();
        views=inquiry.getViews();
        createdAt=inquiry.getCreatedAt();
        updatedAt=inquiry.getUpdatedAt();
        content=inquiry.getContent();
        title=inquiry.getTitle();

        type1=inquiry.getType1();
        memberId=inquiry.getMember() !=null? inquiry.getMember().getUid():null;
        type2=inquiry.getType2();
        answer=inquiry.getAnswer();
    }
}

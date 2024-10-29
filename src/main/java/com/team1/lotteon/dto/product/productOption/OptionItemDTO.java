package com.team1.lotteon.dto.product.productOption;

import lombok.*;

/*
    날짜 : 2024/10/25
    이름 : 최준혁
    내용 : 옵션 아이템 DTO 생성

*/

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OptionItemDTO {

    private Long id;             // OptionItem ID
    private String value;         // 옵션 값 (예: 빨강, S 등)
    private Long productOptionId; // 속한 옵션 그룹의 ID
}

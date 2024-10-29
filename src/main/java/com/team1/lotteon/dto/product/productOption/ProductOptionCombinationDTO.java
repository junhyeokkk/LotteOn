package com.team1.lotteon.dto.product.productOption;

import lombok.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
    날짜 : 2024/10/25
    이름 : 최준혁
    내용 : 제품 조합 dto 생성
*/

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ProductOptionCombinationDTO {

    private Long id;                   // ProductOptionCombination ID
    private Long productId;            // 상품 ID

    // 옵션 조합 정보 (ID 조합과 값 조합 각각 저장)
    private String optionCombination; // 문자열로 받기

    private int stock;                 // 해당 조합의 재고
}

package com.team1.lotteon.dto.product.productOption;

import lombok.*;

import java.util.List;

/*
    날짜 : 2024/10/25
    이름 : 최준혁
    내용 : 옵션 엔티티 생성 (옵션그룹의 값들 (사이즈 : S ))
*/

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductOptionDTO {

    private Long id;
    private String name; // 옵션 이름 예) 색상, 사이즈
    private boolean active; // 옵션 활성화 상태
    private Long productId; // 연관된 상품 ID
    private List<OptionItemDTO> optionItems; // 옵션 아이템 리스트
}

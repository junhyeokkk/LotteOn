package com.team1.lotteon.dto.product.productOption;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ModifyRequestProductCombinationDTO {
    private String combinationText; // 옵션 조합 설명 (예: "색상: 빨강, 사이즈: L")
    private int stock;              // 재고 수량
    private String status;          // 상태 (예: "SALE", "SOLDOUT", "STOP")
}
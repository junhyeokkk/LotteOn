package com.team1.lotteon.dto.product.productOption;

import com.team1.lotteon.entity.Product;
import com.team1.lotteon.entity.enums.CombinationStatus;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ModifyProductOptionCombinationDTO {

    private Long id;

    private Product product;

    private String optionIdCombination;

    private String optionValueCombination;

    private String combinationStatus; // 옵션 조합 상태 추가 컬럼

    private String formattedOptionValueCombination; // 가공된 형식 (옵션명: 옵션값)

    private int stock;

    private int version; // 버전 추가
}

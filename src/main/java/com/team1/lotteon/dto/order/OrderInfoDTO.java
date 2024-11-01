package com.team1.lotteon.dto.order;

import com.team1.lotteon.entity.productOption.ProductOptionCombination;
import lombok.*;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderInfoDTO {
    private Long productId;
    private String productImg;
    private String productName;
    private String productDescription;
    private int quantity;
    private int originalPrice;
    private int discountRate;
    private int discountedPrice;
    private int points;
    private int deliveryFee;
    private int total;

    private Long cartId;

    private ProductOptionCombination productOptionCombination; // 추가: 선택된 옵션 조합 객체
    private Long combinationId; // 아이디 받을 컬럼

    // 뷰 출력을 위한 옵션값 문자열 가공
    private String formattedOptions;

}

package com.team1.lotteon.dto.cart;

import com.team1.lotteon.entity.Member;
import com.team1.lotteon.entity.Product;
import com.team1.lotteon.entity.productOption.ProductOptionCombination;
import lombok.*;

/*
    날짜 : 2024/10/29
    이름 : 최준혁
    내용 : Cart DTO 생성
*/
@ToString
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartDTO {

    private Long id;
    private Member member;
    private Product product;
    private int quantity;
    private int totalPrice;

    private ProductOptionCombination productOptionCombination; // 추가: 선택된 옵션 조합 객체

    // 뷰 출력을 위한 옵션값 문자열 가공
    private String formattedOptions;
}

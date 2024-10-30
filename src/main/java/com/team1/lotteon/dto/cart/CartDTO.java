package com.team1.lotteon.dto.cart;

import com.team1.lotteon.entity.Member;
import com.team1.lotteon.entity.Product;
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
    private Long combinationid; // 선택한 조합 아이디값
    private int quantity;
    private int totalPrice;
}

package com.team1.lotteon.dto.cart;

import com.team1.lotteon.entity.Member;
import com.team1.lotteon.entity.Product;
import lombok.*;

/*
    날짜 : 2024/10/29
    이름 : 최준혁
    내용 : Cart insert 위한 request DTO 생성
*/
@ToString
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartRequestDTO {

    private Long productId;
    private Long combinationId;
    private int quantity;
    private int totalPrice;
}

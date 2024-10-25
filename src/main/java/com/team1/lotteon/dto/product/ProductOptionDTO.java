package com.team1.lotteon.dto.product;

import com.team1.lotteon.entity.Product;
import com.team1.lotteon.entity.productOption.Option;
import jakarta.persistence.*;
import lombok.*;

/*
    날짜 : 2024/10/25
    이름 : 최준혁
    내용 : 제품 옵션 DTO 생성 (상품과 옵션의 관계<상품ID>, <사이즈S_ID> )

*/

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductOptionDTO {

    private Long id;

    private Product product;

    private Option option;


}

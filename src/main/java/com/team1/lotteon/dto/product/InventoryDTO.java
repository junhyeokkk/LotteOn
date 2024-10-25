package com.team1.lotteon.dto.product;

import com.team1.lotteon.entity.Product;
import com.team1.lotteon.entity.productOption.Option;
import jakarta.persistence.*;
import lombok.*;

/*
    날짜 : 2024/10/25
    이름 : 최준혁
    내용 : 재고 DTO 생성 (상품의 옵션 조합에 대한 재고 정보)

*/

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InventoryDTO {

    private Long id;

    private Product product;

    private Option option;

    private int stock;
}

package com.team1.lotteon.entity.productOption;

import com.team1.lotteon.entity.Product;
import jakarta.persistence.*;
import lombok.*;

/*
    날짜 : 2024/10/25
    이름 : 최준혁
    내용 : 재고 엔티티 생성 (상품의 옵션 조합에 대한 재고 정보)

*/

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "inventory")
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "productId", nullable = false)
    private Product product;

    @ManyToOne
    @JoinColumn(name = "optionId", nullable = false)
    private Option option;

    private int stock;
}

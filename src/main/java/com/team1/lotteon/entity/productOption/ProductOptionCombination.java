package com.team1.lotteon.entity.productOption;

import com.team1.lotteon.entity.Product;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "product_option_combination ")
public class ProductOptionCombination {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    // OptionItem의 id 값으로 옵션 조합 저장 (예: {"색상": 1, "사이즈": 3})
    @Column(columnDefinition = "json")
    private String optionIdCombination;

    // OptionItem의 value 값으로 옵션 조합 저장 (예: {"색상": "빨강", "사이즈": "대"})
    @Column(columnDefinition = "json")
    private String optionValueCombination;

    private int stock;
}

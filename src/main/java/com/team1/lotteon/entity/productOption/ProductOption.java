package com.team1.lotteon.entity.productOption;

import com.team1.lotteon.entity.Product;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

/*
    날짜 : 2024/10/25
    이름 : 최준혁
    내용 : 옵션 엔티티 생성 (옵션 그룹(예: 색상, 사이즈 등)을 정의)

*/

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@ToString
@Table(name = "product_option")
public class ProductOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name; // 예) 색상, 사이즈

    @ManyToOne
    @JoinColumn(name = "product_id")  // 추가된 필드
    private Product product; // 어떤 상품에 대한 옵션인지

    @OneToMany(mappedBy = "productOption", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<OptionItem> Optionitems;

}

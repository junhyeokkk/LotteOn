package com.team1.lotteon.dto.product;

import com.team1.lotteon.entity.Product;
import jakarta.persistence.*;
import lombok.*;

/*
    날짜 : 2024/10/27
    이름 : 최준혁
    내용 : 상품 상세정보 DTO 생성

*/
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductdetailDTO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    private String name;   // 옵션 이름
    private String value; // 옵션 값

}



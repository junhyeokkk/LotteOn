package com.team1.lotteon.entity;

import com.team1.lotteon.entity.productOption.ProductOption;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

/*
    날짜 : 2024/10/25
    이름 : 이상훈
    내용 : 상품 엔티티 생성

    - 수정내역
    - 상품 옵션특성을 위한 엔티티 수정 (준혁)
*/
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Productdetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    private String name;   // 상세정보 제목
    private String value; // 상세정보 값

}



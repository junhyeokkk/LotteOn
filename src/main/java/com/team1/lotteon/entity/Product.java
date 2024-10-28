package com.team1.lotteon.entity;

import com.team1.lotteon.entity.productOption.ProductOption;
import jakarta.persistence.*;
import jakarta.persistence.Entity;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.List;

/*
    날짜 : 2024/10/26
    이름 : 최준혁
    내용 : 상품 정보 엔티티 생성

*/
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;    // id
    private int views;  // 조회수
    private String productImg1; // 상품 이미지1
    private String productImg2; // 상품 이미지2
    private String productImg3; // 상품 이미지3

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Productdetail> productDetails;  // 상품 상세정보 테이블을 활용해 별도 관리

    private String productName; // 상품명
    private String description;    // 기본설명
    private String manufacturer;    // 제조사
    private int price;  // 가격
    private int discountRate;   // 할인률
    private int point;  // 포인트
    private int deliveryFee;    // 배송비
    private int Status;   // 상품 상태 (1: 판매중/ 2: 품절/ 3: 기타)

    private String warranty;  // 부가세 면세여부
    private String receiptIssued;  // 영수증 발행 여부
    private String businessType;    // 사업자구분
    private String origin;  // 원산지

    private boolean hasOptions; // 옵션 여부 판단
    private int stock;  // 재고 (옵션이 존재하면 옵션 재고의 총합)

    @OnDelete(action = OnDeleteAction.CASCADE)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;  // 카테고리
    @OnDelete(action = OnDeleteAction.CASCADE)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_id")
    private Shop shop;
    @OnDelete(action = OnDeleteAction.CASCADE)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private SellerMember member;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ProductOption> productOptions;

    public void changeCategory(Category category) {
        this.category = category;
    }

}



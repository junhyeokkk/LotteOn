package com.team1.lotteon.dto.product;

import com.team1.lotteon.dto.product.productOption.ModifyProductOptionCombinationDTO;
import com.team1.lotteon.entity.*;
import com.team1.lotteon.entity.productOption.ProductOption;
import com.team1.lotteon.entity.productOption.ProductOptionCombination;
import lombok.*;

import java.util.List;

/*
    날짜 : 2024/10/26
    이름 : 최준혁
    내용 : 상품 DTO 생성
*/

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Setter
public class ProductDTO extends BaseEntity {
    private Long id;    // id
    private int views;  // 조회수
    private String productImg1; // 상품 이미지1
    private String productImg2; // 상품 이미지2
    private String productImg3; // 상품 이미지3

    private List<Productdetail> productDetails;  // 상품 상세정보 테이블을 활용해 별도 관리

    private String productName; // 상품명
    private String description;    // 기본설명
    private String manufacturer;    // 제조사
    private int price;  // 가격
    private int discountRate;   // 할인률
    private int point;  // 포인트
    private int deliveryFee;    // 배송비

    private String Status;   // 상품 상태
    private String warranty;  // 부가세 면세여부
    private String receiptIssued;  // 영수증 발행 여부
    private String businessType;    // 사업자구분
    private String origin;  // 원산지

    private Category category;  // 카테고리

    private Shop shop;

    private SellerMember member;


    private List<ProductOption> productOptions; // 옵션 리스트

    private boolean hasOptions; // 옵션 여부 판단
    private int stock;  // 재고 (옵션이 존재하면 옵션 재고의 총합)

    private List<ModifyProductOptionCombinationDTO> productOptionCombinations; // 조합리스트

    // 추가컬럼
    private int discountedPrice;

    public void changeCategory(Category category) {
        this.category = category;
    }
}



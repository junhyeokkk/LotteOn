package com.team1.lotteon.dto.product;

import com.team1.lotteon.entity.Category;
import com.team1.lotteon.entity.GeneralMember;
import com.team1.lotteon.entity.Shop;
import com.team1.lotteon.entity.enums.ProductStatus;
import jakarta.persistence.CascadeType;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

public class ProductResponseDTO {
    private Long id;    // id
    private int views;  // 조회수
    private String productImg1; // 상품 이미지1
    private String productImg2; // 상품 이미지2
    private String productImg3; // 상품 이미지3
    private String detailImage; // 상세정보 이미지
    private String productName; // 상품명
    private String basicDescription;    // 기본설명
    private String manufacturer;    // 제조사
    private int price;  // 가격
    private int discountRate;   // 할인률
    private int point;  // 포인트
    private int stock;  // 재고
    private int deliveryFee;    // 배송비
    private ProductStatus productStatus;   // 상품 상태
    private boolean taxExempt;  // 부가세 면세여부
    private boolean receiptIssued;  // 영수증 발행 여부
    private String businessType;    // 사업자구분
    private String origin;  // 원산지
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "category_id")
    private Category category;  // 카테고리
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "shop_id")
    private Shop shop;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "member_id")
    private GeneralMember member;
}

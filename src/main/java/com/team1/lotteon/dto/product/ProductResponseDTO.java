package com.team1.lotteon.dto.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/*
    날짜 : 2024/10/25
    이름 : 이상훈
    내용 : 상품의 기본정보를 반환하는 응답 DTO 생성
*/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductResponseDTO {
    private Long id;    // id
    private int views;  // 조회수
    private String productImg1; // 상품 이미지1
    private String productImg2; // 상품 이미지2
    private String productImg3; // 상품 이미지3
    private String detailImage; // 상세정보 이미지
    private String productName; // 상품명
    private String description;    // 기본설명
    private String manufacturer;    // 제조사
    private int price;  // 가격
    private int discountRate;   // 할인률
    private int point;  // 포인트
    private int stock;  // 재고
    private int deliveryFee;    // 배송비
    private String productStatus;   // 상품 상태
    private boolean taxExempt;  // 부가세 면세여부
    private boolean receiptIssued;  // 영수증 발행 여부
    private String businessType;    // 사업자구분
    private String origin;  // 원산지
    private Long categoryId;  // 카테고리
    private Long shopId;
    private String memberUid;


}

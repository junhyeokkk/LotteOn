package com.team1.lotteon.dto.product;

import com.team1.lotteon.entity.Category;
import com.team1.lotteon.entity.GeneralMember;
import com.team1.lotteon.entity.Product;
import com.team1.lotteon.entity.Shop;
import com.team1.lotteon.entity.enums.ProductStatus;
import jakarta.persistence.CascadeType;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    public static ProductResponseDTO fromEntity(Product product) {
        return ProductResponseDTO.builder()
                .id(product.getId())
                .views(product.getViews())
                .productImg1(product.getProductImg1())
                .productImg2(product.getProductImg2())
                .productImg3(product.getProductImg3())
                .detailImage(product.getDetailImage())
                .productName(product.getProductName())
                .description(product.getDescription())
                .manufacturer(product.getManufacturer())
                .price(product.getPrice())
                .discountRate(product.getDiscountRate())
                .point(product.getPoint())
                .stock(product.getStock())
                .deliveryFee(product.getDeliveryFee())
                .productStatus(product.getProductStatus() != null ? product.getProductStatus().name() : null)
                .taxExempt(product.isTaxExempt())
                .receiptIssued(product.isReceiptIssued())
                .businessType(product.getBusinessType())
                .origin(product.getOrigin())
                .categoryId(product.getCategory() != null ? product.getCategory().getId() : null)
                .shopId(product.getShop() != null ? product.getShop().getId() : null)
                .memberUid(product.getMember() != null ? product.getMember().getUid() : null)
                .build();
    }
}

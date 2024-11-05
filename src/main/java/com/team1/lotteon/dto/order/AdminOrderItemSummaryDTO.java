package com.team1.lotteon.dto.order;

import com.team1.lotteon.entity.OrderItem;
import com.team1.lotteon.entity.enums.DeliveryStatus;
import com.team1.lotteon.entity.productOption.ProductOptionCombination;
import lombok.*;

/*
    날짜 : 2024/11/3
    이름 : 최준혁
    내용 : 주문 아이템 제출내역 DTO 생성 (관리자쪽 주문상세)
*/
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AdminOrderItemSummaryDTO {

    private Long id;
    private String productImage;    // 상품 이미지
    private int productId; // 상품번호
    private String productName;     // 상품 이름
    private String optionCombination; // 옵션 조합 문자열
    private String ShopName; // 상점 이름
    private int originalPrice;      // 상품 금액
    private int discountAmount;     // 할인 금액
    private int quantity;           // 수량
    private int deliveryPrice;  // 배달료
    private int orderPrice;         // 각 OrderItem의 최종 주문 금액
    private String deliveryStatus;

    // 생성자
    public AdminOrderItemSummaryDTO(OrderItem orderItem) {
        this.id = orderItem.getId();
        this.productImage = orderItem.getProduct().getProductImg1();  // 상품 이미지
        this.productId = Math.toIntExact(orderItem.getProduct().getId());
        this.productName = orderItem.getProduct().getProductName();  // 상품 이름
        this.optionCombination = formatOptionCombination(orderItem.getProductOptionCombination()); // 옵션 조합 문자열
        this.ShopName = orderItem.getProduct().getShop().getShopName();
        this.originalPrice = orderItem.getProduct().getPrice();       // 상품 금액
        this.discountAmount = orderItem.getProduct().getPrice() * orderItem.getDiscountRate() / 100; // 할인 금액
        this.quantity = orderItem.getQuantity();                      // 수량
        this.deliveryPrice = orderItem.getDeliveryFee();
        this.orderPrice = orderItem.getOrderPrice();                  // 할인 적용된 최종 가격
        this.deliveryStatus = String.valueOf(orderItem.getDeliveryStatus());
    }

    // 옵션 조합을 보기 좋게 문자열로 포맷팅 (옵션 없는 경우 "옵션 없음" 반환)
    private String formatOptionCombination(ProductOptionCombination optionCombination) {
        if (optionCombination == null) {
            return "옵션 없음";
        }
        // 옵션 조합 문자열 포맷 예시: {"색상": "빨강", "크기": "M"} -> "색상: 빨강, 크기: M"
        return optionCombination.getOptionValueCombination().replace("{", "").replace("}", "")
                .replace("\"", "").replace(":", ": ").replace(",", ", ");
    }
}

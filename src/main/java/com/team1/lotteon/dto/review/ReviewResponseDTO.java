package com.team1.lotteon.dto.review;

import com.team1.lotteon.entity.Review;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewResponseDTO {
    private Long displayNum;
    private Long id;
    private String content;
    private BigDecimal score;
    private String memberId;
    private Long productId;
    private LocalDateTime createdAt;
    private String sellerName;
    private String option;
    private String productName;

    public static ReviewResponseDTO fromEntity(Review review) {
        return ReviewResponseDTO.builder()
                .id(review.getId())
                .content(review.getContent())
                .score(review.getScore())
                .memberId(review.getMember() != null ? review.getMember().getUid() : null)
                .productId(review.getProduct() != null ? review.getProduct().getId() : null)
                .sellerName(review.getProduct() != null ?
                        review.getProduct().getShop() != null ?
                                review.getProduct().getShop().getShopName() : null : null)
                .productName(review.getProduct() != null ? review.getProduct().getProductName() : null)
                .createdAt(review.getCreatedAt())
                .build();
    }

}

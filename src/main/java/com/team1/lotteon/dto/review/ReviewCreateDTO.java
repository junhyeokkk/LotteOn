package com.team1.lotteon.dto.review;

import com.team1.lotteon.entity.GeneralMember;
import com.team1.lotteon.entity.Member;
import com.team1.lotteon.entity.Product;
import com.team1.lotteon.entity.Review;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewCreateDTO {
    private String content;
    private String memberId;
    private Long productId;
    private BigDecimal score;

    public Review toEntity(GeneralMember member, Product product) {
       return Review.builder()
                .content(content)
                .score(score)
                .member(member)
                .product(product)
                .build();
    }
}

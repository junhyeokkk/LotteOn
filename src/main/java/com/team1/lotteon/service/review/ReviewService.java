package com.team1.lotteon.service.review;

import com.team1.lotteon.dto.PageResponseDTO;
import com.team1.lotteon.dto.review.ReviewResponseDTO;
import com.team1.lotteon.repository.review.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;

    public PageResponseDTO<ReviewResponseDTO> getReviewsByProductId(Long productId, Pageable pageable) {
        Page<ReviewResponseDTO> reviews = reviewRepository.findByProductId(productId, pageable).map(ReviewResponseDTO::fromEntity);
        return PageResponseDTO.fromPage(reviews);
    }

    public long getReviewCountByProductId(Long productId) {
        return reviewRepository.countByProductId(productId);
    }

    public double getReviewAvgRating(Long productId) {
        Double avgScore  = reviewRepository.findAverageScoreByProductId(productId);
        if(avgScore == null) {
            return 0;
        }
        return Math.round(avgScore * 10) / 10.0;
    }
}

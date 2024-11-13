package com.team1.lotteon.service.review;

import com.team1.lotteon.dto.PageResponseDTO;
import com.team1.lotteon.dto.review.ReviewCreateDTO;
import com.team1.lotteon.dto.review.ReviewResponseDTO;
import com.team1.lotteon.entity.GeneralMember;
import com.team1.lotteon.entity.Product;
import com.team1.lotteon.entity.Review;
import com.team1.lotteon.repository.Memberrepository.GeneralMemberRepository;
import com.team1.lotteon.repository.ProductRepository;
import com.team1.lotteon.repository.review.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;


@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final GeneralMemberRepository generalMemberRepository;
    private final ProductRepository productRepository;

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

    public Long create(ReviewCreateDTO reviewCreateDTO) {
        GeneralMember member = generalMemberRepository.findById(reviewCreateDTO.getMemberId()).orElseThrow(() -> new IllegalArgumentException("해당 사용자가 없습니다."));
        Product product = productRepository.findById(reviewCreateDTO.getProductId()).orElseThrow(() -> new IllegalArgumentException("해당 상품이 없습니다."));
        Review review = reviewCreateDTO.toEntity(member, product);
        reviewRepository.save(review);
        return review.getId();
    }

    public PageResponseDTO<ReviewResponseDTO> getReviewsByUid(String uid, Pageable pageable) {
        Page<ReviewResponseDTO> reviews = reviewRepository.findWithProductByMemberUid(uid, pageable).map(ReviewResponseDTO::fromEntity);

        // 순번 계산: 페이지당 요소 개수와 페이지 인덱스를 활용
        AtomicInteger startDisplayNum = new AtomicInteger(pageable.getPageNumber() * pageable.getPageSize() + 1);

        // `displayNum`을 계산하여 `ReviewResponseDTO` 리스트로 변환
        Page<ReviewResponseDTO> reviewResponseDTOPage = reviews.map(review -> {
            review.setDisplayNum((long) startDisplayNum.getAndIncrement());
            return review;
        });

        return PageResponseDTO.fromPage(reviewResponseDTOPage);
    }

    public List<ReviewResponseDTO> getReviewsTop3ByUid(String uid) {
        List<Review> reviews = reviewRepository.findTop3ByMemberUidOrderByCreatedAtDesc(uid);
        return reviews.stream().map(ReviewResponseDTO::fromEntity).toList();
    }
}

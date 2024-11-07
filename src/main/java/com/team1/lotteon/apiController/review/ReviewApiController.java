package com.team1.lotteon.apiController.review;

import com.team1.lotteon.dto.review.ReviewCreateDTO;
import com.team1.lotteon.entity.Review;
import com.team1.lotteon.service.review.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/review")
public class ReviewApiController {
    private final ReviewService reviewService;

    @PostMapping
    public ResponseEntity<Void> createReview(@RequestBody ReviewCreateDTO reviewCreateDTO) {
        reviewService.create(reviewCreateDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}

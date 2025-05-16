package com.wanted.ecommerce.review.controller;

import com.wanted.ecommerce.common.constants.MessageConstants;
import com.wanted.ecommerce.common.response.ApiResponse;
import com.wanted.ecommerce.review.dto.request.ReviewPageableRequest;
import com.wanted.ecommerce.review.dto.response.ReviewPaginationResponse;
import com.wanted.ecommerce.review.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @GetMapping("/products/{id}/reviews")
    public ResponseEntity<ApiResponse<ReviewPaginationResponse>> getProductReviews(
        @PathVariable("id") Long productId,
        @Valid @ModelAttribute ReviewPageableRequest request) {

        ReviewPaginationResponse response = reviewService.getProductReviews(productId, request);

        return ResponseEntity.ok(
            ApiResponse.success(response, MessageConstants.READ_PRODUCT_REVIEWS.getMessage()));
    }
}

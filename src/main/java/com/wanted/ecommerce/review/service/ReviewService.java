package com.wanted.ecommerce.review.service;

import com.wanted.ecommerce.review.dto.request.ReviewPageableRequest;
import com.wanted.ecommerce.review.dto.request.ReviewRegisterRequest;
import com.wanted.ecommerce.review.dto.response.RatingResponse;
import com.wanted.ecommerce.review.dto.response.ReviewPaginationResponse;
import com.wanted.ecommerce.review.dto.response.ReviewResponse;

public interface ReviewService {

    ReviewResponse registerReview(Long userId, Long productId, ReviewRegisterRequest request);

    ReviewPaginationResponse getProductReviews(Long productId, ReviewPageableRequest request);

    RatingResponse createRatingResponse(Long productId);
}

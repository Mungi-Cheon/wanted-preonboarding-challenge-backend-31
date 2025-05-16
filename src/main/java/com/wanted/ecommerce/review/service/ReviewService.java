package com.wanted.ecommerce.review.service;

import com.wanted.ecommerce.review.dto.request.ReviewPageableRequest;
import com.wanted.ecommerce.review.dto.response.RatingResponse;
import com.wanted.ecommerce.review.dto.response.ReviewPaginationResponse;

public interface ReviewService {

    ReviewPaginationResponse getProductReviews(Long productId, ReviewPageableRequest request);

    RatingResponse createRatingResponse(Long productId);
}

package com.wanted.ecommerce.review.service;

import com.wanted.ecommerce.common.exception.ErrorType;
import com.wanted.ecommerce.common.exception.ForbiddenException;
import com.wanted.ecommerce.common.exception.ResourceNotFoundException;
import com.wanted.ecommerce.common.mapper.ReviewMapper;
import com.wanted.ecommerce.product.domain.Product;
import com.wanted.ecommerce.product.repository.ProductRepository;
import com.wanted.ecommerce.review.domain.Review;
import com.wanted.ecommerce.review.dto.request.ReviewPageableRequest;
import com.wanted.ecommerce.review.dto.request.ReviewRegisterRequest;
import com.wanted.ecommerce.review.dto.request.ReviewUpdateRequest;
import com.wanted.ecommerce.review.dto.response.RatingResponse;
import com.wanted.ecommerce.review.dto.response.ReviewPaginationResponse;
import com.wanted.ecommerce.review.dto.response.ReviewResponse;
import com.wanted.ecommerce.review.repository.ReviewRepository;
import com.wanted.ecommerce.user.domain.User;
import com.wanted.ecommerce.user.repository.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final ReviewMapper mapper;

    @Transactional
    @Override
    public ReviewResponse registerReview(Long userId, Long productId,
        ReviewRegisterRequest request) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException(ErrorType.RESOURCE_NOT_FOUND));

        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new ResourceNotFoundException(ErrorType.RESOURCE_NOT_FOUND));

        Review review = mapper.mapToReview(request, product, user);
        review = reviewRepository.save(review);
        return mapper.mapToReviewResponse(review);
    }

    @Transactional(readOnly = true)
    @Override
    public ReviewPaginationResponse getProductReviews(Long productId,
        ReviewPageableRequest request) {
        productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException(
            ErrorType.RESOURCE_NOT_FOUND));

        int pageNumber = Math.max(0, request.getPage() - 1);
        Pageable pageable = PageRequest.of(pageNumber, request.getPerPage());

        List<Review> allReviews = reviewRepository.findAllReviewByProductId(productId);

        RatingResponse ratingResponse = mapper.mapToRatingResponse(allReviews);

        Page<Review> reviewPage;
        if (request.getRating() != null) {
            reviewPage = reviewRepository.findByProductIdAndRating(productId,
                request.getRating(), pageable);
        } else {
            reviewPage = reviewRepository.findByProductId(productId, pageable);
        }
        return mapper.mapToReviewPaginationResponse(ratingResponse, reviewPage);
    }

    @Transactional
    @Override
    public ReviewResponse updateReview(Long userId, Long reviewId, ReviewUpdateRequest request) {
        Review review = reviewRepository.findById(reviewId)
            .orElseThrow(() -> new ResourceNotFoundException(ErrorType.RESOURCE_NOT_FOUND));

        if(!review.getUser().getId().equals(reviewId)){
            throw new ForbiddenException(ErrorType.FORBIDDEN);
        }
        review.update(request);
        return mapper.mapToReviewResponse(review);
    }

    @Override
    public RatingResponse createRatingResponse(Long productId) {
        List<Review> reviews = reviewRepository.findAllReviewByProductId(productId);
        return mapper.mapToRatingResponse(reviews);
    }
}

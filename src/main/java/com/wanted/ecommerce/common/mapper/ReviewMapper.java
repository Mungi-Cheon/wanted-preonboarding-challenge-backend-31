package com.wanted.ecommerce.common.mapper;

import com.wanted.ecommerce.common.response.Pagination;
import com.wanted.ecommerce.product.domain.Product;
import com.wanted.ecommerce.review.domain.Review;
import com.wanted.ecommerce.review.dto.request.ReviewRegisterRequest;
import com.wanted.ecommerce.review.dto.response.RatingResponse;
import com.wanted.ecommerce.review.dto.response.ReviewPaginationResponse;
import com.wanted.ecommerce.review.dto.response.ReviewResponse;
import com.wanted.ecommerce.user.domain.User;
import com.wanted.ecommerce.user.dto.response.UserResponse;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
public class ReviewMapper {

    public Review mapToReview(ReviewRegisterRequest request, Product product, User user){
        return Review.builder()
            .product(product)
            .user(user)
            .rating(request.getRating())
            .title(request.getTitle())
            .content(request.getContent())
            .verifiedPurchase(true)
            .helpfulVotes(0)
            .build();
    }

    public RatingResponse mapToRatingResponse(List<Review> reviews) {
        double average = reviews.stream().mapToDouble(Review::getRating).average().orElse(0.0);
        Map<Integer, Long> rating = IntStream.rangeClosed(1, 5)
            .boxed()
            .collect(Collectors.toMap(Function.identity(), i -> 0L));

        rating.putAll(reviews.stream()
            .collect(Collectors.groupingBy(
                Review::getRating,
                Collectors.counting()
            )));

        Map<Integer, Long> sortedRating = rating.entrySet().stream()
            .sorted(Map.Entry.comparingByKey(Comparator.reverseOrder()))
            .collect(Collectors.toMap(
                Entry::getKey,
                Entry::getValue,
                (existing, replacement) -> existing,
                LinkedHashMap::new
            ));
        return RatingResponse.of(average, reviews.size(), sortedRating);
    }

    public ReviewPaginationResponse mapToReviewPaginationResponse(RatingResponse summary,
        Page<Review> page) {
        return ReviewPaginationResponse.builder()
            .items(page.getContent().stream().map(this::mapToReviewResponse).toList())
            .summary(summary)
            .pagination(Pagination.builder()
                .totalItems(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .currentPage(page.getNumber())
                .perPage(page.getNumberOfElements())
                .build())
            .build();
    }

    public ReviewResponse mapToReviewResponse(Review review){
        return ReviewResponse.builder()
            .id(review.getId())
            .user(UserResponse.of(review.getUser()))
            .rating(review.getRating())
            .title(review.getTitle())
            .content(review.getContent())
            .createdAt(review.getCreatedAt())
            .updatedAt(review.getUpdatedAt())
            .verifiedPurchase(review.getVerifiedPurchase())
            .helpfulVotes(review.getHelpfulVotes())
            .build();
    }

}

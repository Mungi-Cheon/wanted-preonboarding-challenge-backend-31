package com.wanted.ecommerce.review.dto.response;

import com.wanted.ecommerce.review.domain.Review;
import com.wanted.ecommerce.user.dto.response.UserResponse;
import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record ReviewResponse(
    Long id,
    UserResponse user,
    Integer rating,
    String title,
    String content,
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    boolean verifiedPurchase,
    Integer helpfulVotes
) {

    public static ReviewResponse of(Review review){
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

package com.wanted.ecommerce.review.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
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
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    LocalDateTime createdAt,
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    LocalDateTime updatedAt,
    boolean verifiedPurchase,
    Integer helpfulVotes
) {
}

package com.wanted.ecommerce.review.dto.response;

import com.wanted.ecommerce.common.response.Pagination;
import java.util.List;
import lombok.Builder;

@Builder
public record ReviewPaginationResponse(
    List<ReviewResponse> items,
    RatingResponse summary,
    Pagination pagination
) {
}

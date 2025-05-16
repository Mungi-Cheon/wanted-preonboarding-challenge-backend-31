package com.wanted.ecommerce.common.mapper;

import com.wanted.ecommerce.common.response.Pagination;
import com.wanted.ecommerce.review.domain.Review;
import com.wanted.ecommerce.review.dto.response.RatingResponse;
import com.wanted.ecommerce.review.dto.response.ReviewPaginationResponse;
import com.wanted.ecommerce.review.dto.response.ReviewResponse;
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
            .items(page.getContent().stream().map(ReviewResponse::of).toList())
            .summary(summary)
            .pagination(Pagination.builder()
                .totalItems(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .currentPage(page.getNumber())
                .perPage(page.getNumberOfElements())
                .build())
            .build();
    }

}

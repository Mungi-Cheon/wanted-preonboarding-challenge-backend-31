package com.wanted.ecommerce.review.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ReviewRegisterRequest {
    private Integer rating;
    private String title;
    private String content;
}

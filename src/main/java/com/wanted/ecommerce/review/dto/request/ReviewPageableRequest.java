package com.wanted.ecommerce.review.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ReviewPageableRequest {

    @Min(value = 1, message = "페이지 입력값은 1 이상이어야 합니다.")
    private Integer page = 1;
    @Min(value = 10, message = "페이지 번호 입력값은 10 이상이어야 합니다.")
    private Integer perPage = 10;
    @Pattern(regexp = "^[a-zA-Z_]+(:(asc|desc))?$", message = "올바른 포맷이 아닙니다.")
    private String sort = "created_at:desc";
    private Integer rating;
}

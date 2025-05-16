package com.wanted.ecommerce.main.dto.response;

import com.wanted.ecommerce.category.domain.Category;
import com.wanted.ecommerce.product.dto.response.ProductListResponse;
import java.util.List;
import java.util.Map;
import lombok.Builder;

@Builder
public record MainPageResponse(
    List<ProductListResponse> newProducts,
    List<ProductListResponse> popularProducts,
    List<FeaturedCategory> featuredCategories
) {

    public static MainPageResponse of(List<ProductListResponse> newProducts,
        List<ProductListResponse> popularProducts, List<FeaturedCategory> featuredCategories) {
        return MainPageResponse.builder()
            .newProducts(newProducts)
            .popularProducts(popularProducts)
            .featuredCategories(featuredCategories)
            .build();
    }

    @Builder
    public record FeaturedCategory(
        Long id,
        String name,
        String slug,
        String imageUrl,
        Integer productCount
    ) {

        public static FeaturedCategory of(Category category,
            Map<Long, Long> categoryProductCounts) {
            return FeaturedCategory.builder()
                .id(category.getId())
                .name(category.getName())
                .slug(category.getSlug())
                .imageUrl(category.getImageUrl())
                .productCount(categoryProductCounts.getOrDefault(category.getId(), 0L).intValue())
                .build();
        }
    }
}

package com.wanted.ecommerce.main.service;

import com.wanted.ecommerce.category.domain.Category;
import com.wanted.ecommerce.category.repository.CategoryRepository;
import com.wanted.ecommerce.common.mapper.ProductMapper;
import com.wanted.ecommerce.main.dto.response.MainPageResponse;
import com.wanted.ecommerce.main.dto.response.MainPageResponse.FeaturedCategory;
import com.wanted.ecommerce.product.domain.ProductStatus;
import com.wanted.ecommerce.product.dto.response.ProductListResponse;
import com.wanted.ecommerce.product.repository.ProductRepository;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MainServiceImpl implements MainService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductMapper mapper;

    @Transactional(readOnly = true)
    @Override
    public MainPageResponse getMainPageContents() {
        List<ProductListResponse> newProducts = productRepository.findTop5ByStatusOrderByCreatedAtDesc(
            ProductStatus.ACTIVE).stream().map(mapper::mapToProductListResponse).toList();

        List<ProductListResponse> popularProducts = productRepository.findTop5PopularProducts()
            .stream().map(mapper::mapToProductListResponse).toList();

        List<Category> categories = categoryRepository.findAllByLevel(1);

        List<Object[]> categoryCountResults = productRepository.countProductsByCategories();
        Map<Long, Long> categoryProductCounts = categoryCountResults.stream()
            .filter(Objects::nonNull)
            .collect(Collectors.toMap(
                result -> (Long) result[0],
                result -> ((Number) result[1]).longValue()
            ));

        List<FeaturedCategory> featuredCategories = categories.stream()
            .map(category -> FeaturedCategory.of(category, categoryProductCounts))
            .filter(featuredCategory -> featuredCategory.productCount() > 0) // 상품이 있는 카테고리만 필터링
            .sorted((fc1, fc2) -> fc2.productCount() - fc1.productCount())
            .limit(5)
            .toList();

        return MainPageResponse.of(newProducts, popularProducts, featuredCategories);
    }
}

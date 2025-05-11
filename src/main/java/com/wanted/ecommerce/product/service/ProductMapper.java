package com.wanted.ecommerce.product.service;

import com.wanted.ecommerce.category.domain.Category;
import com.wanted.ecommerce.category.dto.response.CategoryResponse;
import com.wanted.ecommerce.common.dto.response.ProductItemResponse.ProductImageResponse;
import com.wanted.ecommerce.product.domain.Product;
import com.wanted.ecommerce.product.domain.ProductCategory;
import com.wanted.ecommerce.product.domain.ProductDetail;
import com.wanted.ecommerce.product.domain.ProductImage;
import com.wanted.ecommerce.product.domain.ProductOptionGroup;
import com.wanted.ecommerce.product.domain.ProductPrice;
import com.wanted.ecommerce.product.dto.response.ProductOptionResponse;
import com.wanted.ecommerce.product.dto.response.ProductResponse.DetailResponse;
import com.wanted.ecommerce.product.dto.response.ProductResponse.DimensionsResponse;
import com.wanted.ecommerce.product.dto.response.ProductResponse.ProductImageCreateResponse;
import com.wanted.ecommerce.product.dto.response.ProductResponse.ProductOptionGroupResponse;
import com.wanted.ecommerce.product.dto.response.ProductResponse.ProductPriceResponse;
import com.wanted.ecommerce.product.dto.response.ProductResponse.RelatedProductResponse;
import java.math.RoundingMode;
import java.util.List;

public class ProductMapper {

    public List<RelatedProductResponse> mapToRelatedProductResponses(
        List<Product> relatedProducts) {
        return relatedProducts.stream().map(relatedProduct -> {
            ProductImageResponse imageResponse = relatedProduct.getImages().stream()
                .filter(ProductImage::isPrimary).findFirst().map(ProductImageResponse::of)
                .orElse(null);
            return RelatedProductResponse.of(relatedProduct, imageResponse);
        }).toList();
    }

    public List<CategoryResponse> mapToCategoryResponses(List<ProductCategory> productCategories) {
        return productCategories.stream().map(productCategory ->
        {
            Category category = productCategory.getCategory();
            return CategoryResponse.of(category, productCategory);
        }).toList();
    }

    public DetailResponse mapToProductDetailResponse(ProductDetail detail) {
        double weight = detail.getWeight()
            .setScale(1, RoundingMode.HALF_UP)
            .doubleValue();
        DimensionsResponse dimensionsResponse = DimensionsResponse.of(detail.getDimensions());

        return DetailResponse.of(weight, dimensionsResponse, detail);
    }

    public List<ProductOptionGroupResponse> mapToOptionGroupResponses(
        List<ProductOptionGroup> optionGroups) {
        return optionGroups.stream()
            .map(optionGroup -> {
                List<ProductOptionResponse> options = optionGroup.getOptions().stream()
                    .map(ProductOptionResponse::of)
                    .toList();

                return ProductOptionGroupResponse.of(optionGroup, options);
            })
            .toList();
    }

    public ProductPriceResponse mapToPriceResponse(ProductPrice price) {
        return ProductPriceResponse.of(price);
    }

    public List<ProductImageCreateResponse> mapToImageResponse(List<ProductImage> images) {
        return images.stream()
            .map(image -> {
                if (image.getOption() != null) {
                    return ProductImageCreateResponse.of(image, image.getOption());
                }
                return ProductImageCreateResponse.of(image);
            }).toList();
    }
}

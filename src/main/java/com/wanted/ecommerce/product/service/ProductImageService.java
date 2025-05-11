package com.wanted.ecommerce.product.service;

import com.wanted.ecommerce.product.domain.Product;
import com.wanted.ecommerce.product.dto.request.ProductRegisterRequest.ProductImageRequest;
import com.wanted.ecommerce.product.dto.response.ProductResponse.ProductImageCreateResponse;
import java.util.List;

public interface ProductImageService {

    List<ProductImageCreateResponse> createProductImages(Product product, List<ProductImageRequest> imageRequestList);

    void deleteProductImageByProductId(Long productId);
}

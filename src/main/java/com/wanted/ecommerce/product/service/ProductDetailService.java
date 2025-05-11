package com.wanted.ecommerce.product.service;

import com.wanted.ecommerce.product.domain.Product;
import com.wanted.ecommerce.product.domain.ProductDetail;
import com.wanted.ecommerce.product.dto.request.ProductRegisterRequest.ProductDetailRequest;

public interface ProductDetailService {

    ProductDetail saveDetail(Product product, ProductDetailRequest detailRequest);

    void updateDetail(ProductDetail detail, ProductDetailRequest request);

    ProductDetail getProductDetailByProductId(Long productId);
}

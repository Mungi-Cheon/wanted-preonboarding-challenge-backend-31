package com.wanted.ecommerce.product.service;

import com.wanted.ecommerce.product.domain.Product;
import com.wanted.ecommerce.product.domain.ProductOptionGroup;
import com.wanted.ecommerce.product.dto.request.ProductRegisterRequest.ProductOptionGroupRequest;
import java.util.List;

public interface ProductOptionGroupService {
    ProductOptionGroup saveOptionGroup(Product product, ProductOptionGroupRequest groupRequest);
    List<ProductOptionGroup> saveProductOptionsAndGroup(Product saved,List<ProductOptionGroupRequest> optionGroups);

    void deleteProductOptionGroup(Long productId);

    ProductOptionGroup updateOptionGroup(Product product, Long optionGroupId);
}

package com.wanted.ecommerce.product.repository;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.wanted.ecommerce.brand.domain.QBrand;
import com.wanted.ecommerce.category.domain.QCategory;
import com.wanted.ecommerce.product.domain.Product;
import com.wanted.ecommerce.product.domain.ProductStatus;
import com.wanted.ecommerce.product.domain.QProduct;
import com.wanted.ecommerce.product.domain.QProductCategory;
import com.wanted.ecommerce.product.domain.QProductOption;
import com.wanted.ecommerce.product.domain.QProductOptionGroup;
import com.wanted.ecommerce.product.domain.QProductPrice;
import com.wanted.ecommerce.product.dto.request.ProductSearchRequest;
import com.wanted.ecommerce.review.domain.QReview;
import com.wanted.ecommerce.seller.domain.QSeller;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ProductSearchRepositoryImpl implements ProductSearchRepository {

    private final JPAQueryFactory queryFactory;
    private final QProduct product = QProduct.product;
    private final QProductPrice price = QProductPrice.productPrice;
    private final QProductCategory productCategory = QProductCategory.productCategory;
    private final QCategory category = QCategory.category;
    private final QSeller seller = QSeller.seller;
    private final QBrand brand = QBrand.brand;
    private final QProductOption option = QProductOption.productOption;
    private final QProductOptionGroup optionGroup = QProductOptionGroup.productOptionGroup;

    @Override
    public PageImpl<Product> findAllByRequest(ProductSearchRequest request, Pageable pageable) {
        var query = queryFactory
            .selectFrom(product);

        // dynamic where
        List<Predicate> conditions = new ArrayList<>();

        // status
        if (request.getStatus() != null) {
            conditions.add(product.status.eq(ProductStatus.valueOf(request.getStatus())));
        }

        if(request.getMinPrice() != null || request.getMaxPrice() != null){
            query.join(product.price, price).fetchJoin();
        }

        // min price
        if(request.getMinPrice() != null){
            conditions.add(price.basePrice.goe(request.getMinPrice()));
        }

        // max price
        if(request.getMaxPrice() != null){
            conditions.add(price.basePrice.loe(request.getMaxPrice()));
        }

        // category
        if (request.getCategory() != null && !request.getCategory().isEmpty()) {
            query.join(product.categories, productCategory).fetchJoin();
            query.join(productCategory.category, category).fetchJoin();
            conditions.add(category.id.in(request.getCategory().stream()
                .map(Long::valueOf)
                .toList()));
        }

        // seller
        if (request.getSeller() != null) {
            query.join(product.seller, seller).fetchJoin();
            conditions.add(seller.id.eq(request.getSeller()));
        }
        // brand
        if (request.getBrand() != null) {
            query.join(product.brand, brand).fetchJoin();
            conditions.add(brand.id.eq(request.getBrand()));
        }
        // stock
        if (request.getInStock() != null) {
            query.join(product.optionGroups, optionGroup).fetchJoin();
            query.join(option).on(option.optionGroup.eq(optionGroup)).fetchJoin();

            if (Boolean.TRUE.equals(request.getInStock())) {
                conditions.add(option.stock.gt(0));
            } else {
                conditions.add(option.stock.eq(0));
            }
        }
        // search
        if (request.getSearch() != null && !request.getSearch().isBlank()) {
            String keyword = "%" + request.getSearch() + "%";
            conditions.add(product.name.like(keyword));
            conditions.add(product.shortDescription.like(keyword));
            conditions.add(product.fullDescription.like(keyword));
        }
        query.where(conditions.toArray(new Predicate[0]));

        // sort
        List<OrderSpecifier<?>> orders = new ArrayList<>();
        if (request.getSort() != null) {
            Set<String> fields = request.getSort().keySet();
            for(String field : fields){
                boolean isAsc = "asc".equalsIgnoreCase(request.getSort().get(field));
                switch (field) {
                    case "created_at" -> query.orderBy(isAsc ? product.createdAt.asc() : product.createdAt.desc());
                    case "price" -> {
                        if(request.getMaxPrice() == null & request.getMaxPrice() == null) query.join(product.price, price);
                        query.orderBy(isAsc ? price.basePrice.asc() : price.basePrice.desc());
                    }
                    case "rating" -> {
                        QReview review = QReview.review;
                        query.join(review).on(review.product.eq(product));
                        query.groupBy(product.id);
                        query.orderBy(isAsc ? review.rating.avg().asc() : review.rating.avg().desc());
                    }
                }
            }
        }else {
            query.orderBy(product.createdAt.desc());
        }
        // paging
        query.offset(pageable.getOffset())
            .limit(pageable.getPageSize());

        // execute
        List<Product> products = query.fetch();

        // count query
        long total = query.fetch().size();
        return new PageImpl<>(products, pageable, total);
    }

    @Override
    public List<Product> findRelatedProductsByCategoryId(Long categoryId) {
        var query = queryFactory
            .selectFrom(product)
            .join(product.categories, productCategory).fetchJoin()
            .join(productCategory.category, category).fetchJoin()
            .where(category.id.eq(categoryId));
        return query.fetch();
    }

}

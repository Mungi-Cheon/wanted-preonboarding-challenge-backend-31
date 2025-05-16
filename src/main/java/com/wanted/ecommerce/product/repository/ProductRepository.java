package com.wanted.ecommerce.product.repository;

import com.wanted.ecommerce.product.domain.Product;
import com.wanted.ecommerce.product.domain.ProductStatus;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProductRepository extends JpaRepository<Product, Long>, ProductSearchRepository {

    Page<Product> findByCategoriesIdIn(List<Long> categoryIds, Pageable pageable);

    List<Product> findTop5ByStatusOrderByCreatedAtDesc(ProductStatus status);

    @Query(value = "SELECT p.* FROM products p " +
        "JOIN reviews r ON p.id = r.product_id " +
        "WHERE p.status = 'ACTIVE' " +
        "GROUP BY p.id " +
        "ORDER BY AVG(r.rating) DESC, COUNT(r.id) DESC " +
        "LIMIT 5",
        nativeQuery = true)
    List<Product> findTop5PopularProducts();

    @Query("SELECT c.id, COUNT(p) " +
        "FROM Product p JOIN p.categories c " +
        "WHERE p.status = 'ACTIVE' " +
        "GROUP BY c.id")
    List<Object[]> countProductsByCategories();
}

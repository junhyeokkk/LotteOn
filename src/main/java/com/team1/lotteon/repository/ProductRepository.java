package com.team1.lotteon.repository;

import com.team1.lotteon.entity.Product;
import com.team1.lotteon.repository.productCustom.ProductRepositoryCustom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/*
    날짜 : 2024/10/25
    이름 : 이상훈
    내용 : 상품 리파지토리 생성
    수정사항
    - 2024/11/13 이도영 메인 화면에 출력할 상품 방식 기능 구현
*/
public interface ProductRepository extends JpaRepository<Product, Long>, ProductRepositoryCustom {
    Page<Product> findByCategoryId(Long categoryId, Pageable pageable);
    Page<Product> findByCategoryIdIn(List<Long> categoryIds, Pageable pageable);

    //히트 상품 조회수 가장 많은 순 8개
    List<Product> findTop8ByOrderByViewsDesc();

    //추천 상품 리뷰가 가장 많으면서 평점이 높은 순서대로 8개
    @Query(value = """
        SELECT p.*
        FROM product p
        JOIN (
            SELECT product_id, COUNT(*) AS review_count, AVG(score) AS avg_score
            FROM review
            GROUP BY product_id
            ORDER BY review_count DESC, avg_score DESC
            LIMIT 8
        ) AS top_products ON p.id = top_products.product_id
        """, nativeQuery = true)
    List<Product> findTopProductsByReviewCountAndScore();

    //최신상품 가장 최근에 등록된 상품 순서 대로
    List<Product> findTop8ByOrderByCreatedAtDesc();

    //인기 상품 구매율이 가장 높으면서 평점이 높은순
    @Query(value = """
        SELECT p.*, IFNULL(order_stats.total_quantity, 0) AS purchase_count, IFNULL(review_stats.avg_score, 0) AS avg_rating
        FROM product p
        JOIN (
            SELECT oi.product_id, SUM(oi.quantity) AS total_quantity
            FROM order_item oi
            WHERE oi.delivery_status IN ('SHIPPED', 'COMPLETE')
            GROUP BY oi.product_id
        ) AS order_stats ON p.id = order_stats.product_id
        LEFT JOIN (
            SELECT r.product_id, AVG(r.score) AS avg_score
            FROM review r
            GROUP BY r.product_id
        ) AS review_stats ON p.id = review_stats.product_id
        ORDER BY order_stats.total_quantity DESC, review_stats.avg_score DESC
        LIMIT 8
        """, nativeQuery = true)
    List<Product> findTopSellingAndRatedProducts();

    //할인상품 할인율이 가장 많은 순서
    List<Product> findTop8ByOrderByDiscountRateDesc();
}

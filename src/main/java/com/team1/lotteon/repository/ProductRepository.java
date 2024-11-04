package com.team1.lotteon.repository;

import com.team1.lotteon.entity.Product;
import com.team1.lotteon.repository.productCustom.ProductRepositoryCustom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/*
    날짜 : 2024/10/25
    이름 : 이상훈
    내용 : 상품 리파지토리 생성
*/
public interface ProductRepository extends JpaRepository<Product, Long>, ProductRepositoryCustom {
    Page<Product> findByCategoryId(Long categoryId, Pageable pageable);
    Page<Product> findByCategoryIdIn(List<Long> categoryIds, Pageable pageable);
}

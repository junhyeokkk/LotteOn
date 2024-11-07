package com.team1.lotteon.repository;

import com.team1.lotteon.entity.Product;
import com.team1.lotteon.entity.productOption.OptionItem;
import com.team1.lotteon.entity.productOption.ProductOptionCombination;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/*
    날짜 : 2024/10/27
    이름 : 최준혁
    내용 : 상품 조합 리파지토리 생성 (상품)
*/
public interface ProductOptionCombinationRepository extends JpaRepository<ProductOptionCombination, Long> {
    // 특정 상품 ID에 대한 모든 옵션 조합을 조회                                                                                                                                                                                                            `
    List<ProductOptionCombination> findByProductId(Long productId);

    List<ProductOptionCombination> findByProduct(Product product);
}

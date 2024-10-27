package com.team1.lotteon.repository;

import com.team1.lotteon.entity.productOption.OptionItem;
import com.team1.lotteon.entity.productOption.ProductOptionCombination;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/*
    날짜 : 2024/10/27
    이름 : 최준혁
    내용 : 상품 조합 리파지토리 생성 (상품)
*/
public interface ProductOptionCombinationRepository extends JpaRepository<ProductOptionCombination, Long> {
}

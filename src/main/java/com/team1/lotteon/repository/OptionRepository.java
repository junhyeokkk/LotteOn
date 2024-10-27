package com.team1.lotteon.repository;

import com.team1.lotteon.entity.productOption.ProductOption;
import org.springframework.data.jpa.repository.JpaRepository;

/*
    날짜 : 2024/10/27
    이름 : 최준혁
    내용 : 옵션 리파지토리 생성 (상품)
*/
public interface OptionRepository extends JpaRepository<ProductOption, Long> {
}

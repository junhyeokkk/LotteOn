package com.team1.lotteon.repository;

import com.team1.lotteon.entity.Product;
import com.team1.lotteon.entity.productOption.ProductOption;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/*
    날짜 : 2024/10/27
    이름 : 최준혁
    내용 : 옵션 리파지토리 생성 (상품)
*/
public interface OptionRepository extends JpaRepository<ProductOption, Long> {
    // 특정 Product에 속한 모든 ProductOption 엔티티를 삭제
    void deleteByProduct(Product product);

    List<ProductOption> findByProduct(Product product);

    Optional<ProductOption> findByProductAndName(Product product, String optionName);
}

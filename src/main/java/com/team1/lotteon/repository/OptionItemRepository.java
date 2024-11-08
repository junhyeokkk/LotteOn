package com.team1.lotteon.repository;

import com.team1.lotteon.entity.Product;
import com.team1.lotteon.entity.productOption.OptionItem;
import com.team1.lotteon.entity.productOption.ProductOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/*
    날짜 : 2024/10/27
    이름 : 최준혁
    내용 : 옵션 아이템 리파지토리 생성 (상품)
*/
public interface OptionItemRepository extends JpaRepository<OptionItem, Long> {

    Optional<OptionItem> findByProductOptionNameAndValue(String productOptionName, String value);

    // 특정 ProductOption과 연관된 OptionItem 조회
    List<OptionItem> findByProductOption(ProductOption productOption);

    // value 필드를 기준으로 OptionItem을 검색
    List<OptionItem> findByValue(String value); // 변경된 부분

    // product, productOption의 name과 value로 OptionItem을 조회하는 메서드
    @Query("SELECT o FROM OptionItem o WHERE o.productOption.product = :product AND o.productOption.name = :optionName AND o.value = :value")
    Optional<OptionItem> findByProductAndOptionNameAndValue(@Param("product") Product product,
                                                            @Param("optionName") String optionName,
                                                            @Param("value") String value);
}

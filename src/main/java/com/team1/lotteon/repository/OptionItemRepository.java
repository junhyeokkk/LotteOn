package com.team1.lotteon.repository;

import com.team1.lotteon.entity.productOption.OptionItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/*
    날짜 : 2024/10/27
    이름 : 최준혁
    내용 : 옵션 아이템 리파지토리 생성 (상품)
*/
public interface OptionItemRepository extends JpaRepository<OptionItem, Long> {

    // value 필드를 기준으로 OptionItem을 검색
    List<OptionItem> findByValue(String value); // 변경된 부분
}

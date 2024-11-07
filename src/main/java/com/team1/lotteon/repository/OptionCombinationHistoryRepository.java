package com.team1.lotteon.repository;

import com.team1.lotteon.entity.productOption.OptionCombinationHistory;
import org.springframework.data.jpa.repository.JpaRepository;

/*
    날짜 : 2024/11/6
    이름 : 최준혁
    내용 : 상품 조합 이전버전 관리 리파지토리 생성 (상품)
*/
public interface OptionCombinationHistoryRepository extends JpaRepository<OptionCombinationHistory, Long> {
}

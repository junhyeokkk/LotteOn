package com.team1.lotteon.repository;

import com.team1.lotteon.entity.FAQ;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/*
 *   날짜 : 2024/10/21
 *   이름 : 김소희
 *   내용 : FaqRepository 생성
 *
 *  수정이력
 *  - 2024/10/29 김소희 - 카테고리 연결
 */
public interface FaqRepository extends JpaRepository<FAQ, Long> {
    Page<FAQ> findByType2(String type2, Pageable pageable);
    List<FAQ> findTop10ByOrderByCreatedAtDesc();
    List<FAQ> findAllByOrderByCreatedAtDesc();
}

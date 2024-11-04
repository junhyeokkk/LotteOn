package com.team1.lotteon.repository;

import com.team1.lotteon.entity.Notice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/*
 *   날짜 : 2024/10/21
 *   이름 : 김소희
 *   내용 : NoticeRepository 생성
 *
 *  수정이력
 *  - 2024/10/29 김소희 - 카테고리 연결
 */

public interface NoticeRepository extends JpaRepository<Notice, Long> {
    Page<Notice> findByType1(String type1, Pageable pageable);
    Page<Notice> findByType1OrderByCreatedAtDesc(String type1, Pageable pageable);
}

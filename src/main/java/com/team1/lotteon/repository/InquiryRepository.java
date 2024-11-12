package com.team1.lotteon.repository;

import com.team1.lotteon.entity.FAQ;
import com.team1.lotteon.entity.Inquiry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/*
 *   날짜 : 2024/10/21
 *   이름 : 김소희
 *   내용 : FaqRepository 생성
 *
 *  수정이력
 *  - 2024/10/29 김소희 - 카테고리 연결
 */

public interface InquiryRepository extends JpaRepository<Inquiry, Long> {
    Page<Inquiry> findByType1(String type1, Pageable pageable);

    Page<Inquiry> findByType1AndType2(String type1, String type2, Pageable pageable);

    // type1으로 필터링하고, type2별로 그룹화한 후, 각 type2에서 최대 10개의 기사를 반환하는 커스텀 쿼리
    @Query("SELECT q FROM Inquiry q WHERE q.type1 = :type1 ORDER BY q.type2, q.createdAt DESC")
    List<Inquiry> findByType1OrderByType2AndCreatedAt(@Param("type1") String type1);
    List<Inquiry> findTop3ByOrderByIdDesc();

    // 새로운 메서드: 특정 회원 ID로 문의 데이터 조회
    Page<Inquiry> findByMember_Uid(String memberId, Pageable pageable);

    // 특정 회원 ID로 문의 개수 조회
    int countByMember_Uid(String memberId);


}


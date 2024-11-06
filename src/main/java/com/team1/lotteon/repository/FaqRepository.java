package com.team1.lotteon.repository;

import com.team1.lotteon.entity.FAQ;
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
public interface FaqRepository extends JpaRepository<FAQ, Long> {
    Page<FAQ> findByType1(String type1, Pageable pageable);
    List<FAQ> findByType1(String type1);
    Page<FAQ> findByType2(String type2, Pageable pageable);
    Page<FAQ> findByType1AndType2(String type1, String type2, Pageable pageable);
    List<FAQ> findTop10ByOrderByCreatedAtDesc();
    List<FAQ> findAllByOrderByCreatedAtDesc();

    @Query("SELECT f FROM FAQ f " +
            "WHERE f.type1 IN :types " +
            "ORDER BY f.views DESC")
    List<FAQ> findByType1InOrderByViewsDesc(@Param("types") List<String> types);
    // type1으로 필터링하고, type2별로 그룹화한 후, 각 type2에서 최대 10개의 기사를 반환하는 커스텀 쿼리
    @Query("SELECT f FROM FAQ f WHERE f.type1 = :type1 ORDER BY f.type2, f.createdAt DESC")
    List<FAQ> findByType1OrderByType2AndCreatedAt(@Param("type1") String type1);
}

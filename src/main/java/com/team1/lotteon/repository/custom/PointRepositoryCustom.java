package com.team1.lotteon.repository.custom;

import com.team1.lotteon.entity.Point;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
/*
    날짜 : 2024/10/25
    이름 : 최준혁
    내용 : PointRepositoryCustom 생성
*/
@Repository
public interface PointRepositoryCustom  {
    Page<Point> findByDynamicType(String keyword, String type, Pageable pageable);
}

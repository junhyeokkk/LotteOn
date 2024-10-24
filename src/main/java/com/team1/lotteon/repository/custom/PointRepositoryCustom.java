package com.team1.lotteon.repository.custom;

import com.team1.lotteon.entity.Point;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PointRepositoryCustom  {
    Page<Point> findByDynamicType(String keyword, String type, Pageable pageable);
}

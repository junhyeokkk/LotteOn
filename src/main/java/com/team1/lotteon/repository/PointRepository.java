package com.team1.lotteon.repository;

import com.team1.lotteon.entity.Point;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PointRepository extends JpaRepository<Point, Long> {
    @Query(value = "SELECT * FROM point WHERE :type LIKE %:keyword%", nativeQuery = true)
    Page<Point> findByDynamicType(@Param("type") String type, @Param("keyword") String keyword, Pageable pageable);
}

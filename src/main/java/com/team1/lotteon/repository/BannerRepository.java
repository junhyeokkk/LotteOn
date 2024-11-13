package com.team1.lotteon.repository;

import com.team1.lotteon.entity.Banner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/*
    날짜 : 2024/10/23
    이름 : 최준혁
    내용 : 배너 리파지토리 생성
*/
@Repository
public interface BannerRepository extends JpaRepository<Banner, Integer> {
    @Query("SELECT b FROM Banner b WHERE b.isActive = 1 " +
            "AND b.displayStartDate <= :currentDate AND b.displayEndDate >= :currentDate " +
            "AND b.displayStartTime <= :currentTime AND b.displayEndTime >= :currentTime")
    List<Banner> findActiveBannersByDateAndTime(@Param("currentDate") LocalDate currentDate,
                                                @Param("currentTime") LocalTime currentTime);

    // position과 isActive 값으로 배너 조회
    List<Banner> findByPositionAndIsActive(String position, int isActive);

    // 선택된 배너 삭제
    void deleteAllByIdIn(List<Long> ids);
}

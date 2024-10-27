package com.team1.lotteon.repository;

import com.team1.lotteon.entity.Banner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
/*
    날짜 : 2024/10/23
    이름 : 최준혁
    내용 : 배너 리파지토리 생성
*/
@Repository
public interface BannerRepository extends JpaRepository<Banner, Integer> {
}

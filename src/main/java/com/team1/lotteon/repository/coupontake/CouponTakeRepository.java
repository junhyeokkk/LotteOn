package com.team1.lotteon.repository.coupontake;

import com.team1.lotteon.entity.CouponTake;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/*
     날짜 : 2024/10/30
     이름 : 이도영(최초 작성자)
     내용 : CouponTakeRepository 생성
*/
@Repository
public interface CouponTakeRepository extends JpaRepository<CouponTake, Long> {
}

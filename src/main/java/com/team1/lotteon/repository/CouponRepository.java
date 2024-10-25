package com.team1.lotteon.repository;
/*
     날짜 : 2024/10/25
     이름 : 강유정(최초 작성자)
     내용 : CouponRepository 생성
*/

import com.team1.lotteon.entity.Coupon2;
import com.team1.lotteon.entity.Version;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CouponRepository extends JpaRepository<Coupon2, Long> {
}

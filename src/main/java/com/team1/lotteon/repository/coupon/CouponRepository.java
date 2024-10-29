package com.team1.lotteon.repository.coupon;
/*
     날짜 : 2024/10/25
     이름 : 강유정(최초 작성자)
     내용 : CouponRepository 생성
*/

import com.team1.lotteon.entity.Coupon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CouponRepository extends JpaRepository<Coupon, Long> {
    //쿠폰번호로 검색
    Page<Coupon> findByCouponid(Long  couponId, Pageable pageable);
    //쿠폰 이름으로 검색
    Page<Coupon> findByCouponnameContaining(String CouponName, Pageable pageable);

    Page<Coupon> findByMemberRole(String admin, Pageable pageable);

    Page<Coupon> findByMemberUidIn(List<String> sellerMemberIds, Pageable pageable);

    //발급자 이름으로 검색
//    Page<Coupon> findBySellerNameContaining(String SellerName, Pageable pageable);
}

package com.team1.lotteon.repository.coupontake;

import com.team1.lotteon.entity.CouponTake;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/*
     날짜 : 2024/10/30
     이름 : 이도영(최초 작성자)
     내용 : CouponTakeRepository 생성

     수정이력
     - 2024/11/01 이도영 다운받은 쿠폰 추가 기능 작성
*/
@Repository
public interface CouponTakeRepository extends JpaRepository<CouponTake, Long> {
    Page<CouponTake> findByCouponTakenId(Long couponTakenId, Pageable pageable);  // 수정된 메서드 이름
    Page<CouponTake> findByCoupon_Couponid(Long couponId, Pageable pageable);
    Page<CouponTake> findByCoupon_CouponnameContaining(String couponName, Pageable pageable);

    Optional<CouponTake> findByMember_UidAndCoupon_Couponid(String memberId, Long couponId);

    boolean existsByMember_UidAndCoupon_Couponid(String memberid, Long couponid);
}


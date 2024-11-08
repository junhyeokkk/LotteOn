package com.team1.lotteon.repository.coupon;
/*
     날짜 : 2024/10/25
     이름 : 강유정(최초 작성자)
     내용 : CouponRepository 생성
     수정이력
     - 2024/11/05 이도영 - 판매자 일경우 데이터 출력
*/

import com.team1.lotteon.entity.Coupon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CouponRepository extends JpaRepository<Coupon, Long> {
    //쿠폰번호로 검색
    Page<Coupon> findByCouponid(Long  couponId, Pageable pageable);
    //쿠폰 이름으로 검색
    Page<Coupon> findByCouponnameContaining(String CouponName, Pageable pageable);
    Page<Coupon> findByMemberRole(String admin, Pageable pageable);

    //role 이 판매자 일경우 데이터 출력
    Page<Coupon> findByCouponidAndMemberUid(Long couponId, String uid, Pageable pageable);
    Page<Coupon> findByCouponnameContainingAndMemberUid(String couponname, String uid, Pageable pageable);
    Page<Coupon> findByMemberUid(String uid, Pageable pageable);
    Page<Coupon> findByMemberUidIn(List<String> uids, Pageable pageable);

    List<Coupon> findByCouponstartBetweenOrCouponendBetween(LocalDateTime startOfToday, LocalDateTime endOfToday, LocalDateTime startOfYesterday, LocalDateTime endOfYesterday);

    //쿠폰 상태에 따라 사용자 쿠폰 출력
    List<Coupon> findByMember_UidAndCouponstate(String memberId, String start);
}

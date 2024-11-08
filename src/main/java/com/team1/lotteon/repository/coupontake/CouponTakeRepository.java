package com.team1.lotteon.repository.coupontake;

import com.team1.lotteon.entity.CouponTake;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/*
     날짜 : 2024/10/30
     이름 : 이도영(최초 작성자)
     내용 : CouponTakeRepository 생성

     수정이력
     - 2024/11/01 이도영 다운받은 쿠폰 추가 기능 작성
     - 2024/11/05 이도영 판매자일 경우 데이터 출력 형식
     - 2024/11/05 이도영 나의정보에 발급받은 쿠폰 수량 출력
*/
@Repository
public interface CouponTakeRepository extends JpaRepository<CouponTake, Long> {
    Page<CouponTake> findByCouponTakenId(Long couponTakenId, Pageable pageable);  // 수정된 메서드 이름
    Page<CouponTake> findByCoupon_Couponid(Long couponId, Pageable pageable);
    Page<CouponTake> findByCoupon_CouponnameContaining(String couponName, Pageable pageable);
    Optional<CouponTake> findByMember_UidAndCoupon_Couponid(String memberId, Long couponId);
    boolean existsByMember_UidAndCoupon_Couponid(String memberid, Long couponid);

    //판매자일 경우 데이터 출력 방식
    Page<CouponTake> findByCouponTakenIdAndShopId(long l, Long shopid, Pageable pageable);
    Page<CouponTake> findByCoupon_CouponidAndShopId(long l, Long shopid, Pageable pageable);
    Page<CouponTake> findByCoupon_CouponnameContainingAndShopId(String keyword, Long shopid, Pageable pageable);
    Page<CouponTake> findByShopId(Long shopid, Pageable pageable);

    //발급받은 쿠폰 수량 출력
    List<CouponTake> findAllByMember_UidAndCouponUseCheck(String memberId, int i);

    List<CouponTake> findByCouponExpireDateBetweenAndCouponUseCheck(LocalDateTime startDate, LocalDateTime endDate, int useCheck);
    //발급받은 쿠폰 사용 상태에 따라 출력
    List<CouponTake> findByMember_UidAndCouponUseCheck(String memberId, int i);


}


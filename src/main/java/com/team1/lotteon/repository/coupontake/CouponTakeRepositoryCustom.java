package com.team1.lotteon.repository.coupontake;

import com.querydsl.core.Tuple;
import com.team1.lotteon.entity.CouponTake;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/*
     날짜 : 2024/10/30
     이름 : 이도영(최초 작성자)
     내용 : CouoponTakeRepositoryCustom 생성
*/
public interface CouponTakeRepositoryCustom {
    List<CouponTake> findByMemberIdAndOptionalShopId(String memberId, Long shopId);
    Page<CouponTake> findPagedCouponsByMemberId(String memberId, Pageable pageable);
}

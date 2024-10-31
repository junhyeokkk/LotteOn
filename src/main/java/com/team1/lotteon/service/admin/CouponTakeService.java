package com.team1.lotteon.service.admin;

import com.team1.lotteon.dto.CouponTakeDTO;
import com.team1.lotteon.entity.Coupon;
import com.team1.lotteon.entity.CouponTake;
import com.team1.lotteon.entity.Member;
import com.team1.lotteon.entity.Shop;
import com.team1.lotteon.repository.coupon.CouponRepository;
import com.team1.lotteon.repository.coupontake.CouponTakeRepository;
import com.team1.lotteon.repository.coupontake.CouponTakeRepositoryCustom;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/*
     날짜 : 2024/10/30
     이름 : 이도영(최초 작성자)
     내용 : CouponTakeService 생성
*/
@Log4j2
@RequiredArgsConstructor
@Service
public class CouponTakeService {
    private final CouponTakeRepositoryCustom couponTakeRepositoryCustom;
    private final ModelMapper modelMapper;
    private final CouponTakeRepository couponTakeRepository;
    private final CouponRepository couponRepository;

    //쿠폰 가지고 오기(상점 정보를 이용해서)
    public List<CouponTakeDTO> findByMemberIdAndShopId(String memberId, Long shopId) {
        return couponTakeRepositoryCustom.findByMemberIdAndOptionalShopId(memberId, shopId)
                .stream()
                .map(couponTake -> modelMapper.map(couponTake, CouponTakeDTO.class))
                .collect(Collectors.toList());
    }
    //나의 정보에서 쿠폰 가지고 오기(멤버 정보만 활용해서)
    public Page<CouponTakeDTO> findPagedCouponsByMemberId(String memberId, Pageable pageable) {
        return couponTakeRepositoryCustom.findPagedCouponsByMemberId(memberId, pageable)
                .map(couponTake -> modelMapper.map(couponTake, CouponTakeDTO.class));
    }

    //쿠폰 저장 하기
    public CouponTakeDTO saveCouponTake(String memberid, Long shopid, Long couponid) {
        Coupon coupon = couponRepository.findById(couponid)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Coupon not found"));
        LocalDateTime couponGetDate = LocalDateTime.now();
        LocalDateTime couponExpireDate = couponGetDate.plusDays(coupon.getCouponperiod());
        CouponTake couponTake = CouponTake.builder()
                .coupon(coupon)
                .member(Member.builder().uid(memberid).build())
                .shop(shopid != null ? Shop.builder().id(shopid).build() : null)
                .couponGetDate(couponGetDate)
                .couponExpireDate(couponExpireDate)
                .build();

        CouponTake savedCouponTake = couponTakeRepository.save(couponTake);

        return modelMapper.map(savedCouponTake, CouponTakeDTO.class);
    }
}

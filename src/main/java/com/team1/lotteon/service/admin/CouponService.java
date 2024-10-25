/*
     날짜 : 2024/10/25
     이름 : 강유정(최초 작성자)
     내용 : CouponService 생성
*/

package com.team1.lotteon.service.admin;

import com.team1.lotteon.dto.CouponDTO;
import com.team1.lotteon.entity.Coupon2;
import com.team1.lotteon.repository.CouponRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Log4j2
@RequiredArgsConstructor
@Service
public class CouponService {

    private final CouponRepository couponRepository;

    private final ModelMapper modelMapper;


    // 버전 insert coupon
    public Coupon2 insertCoupon(CouponDTO couponDTO) {

        Coupon2 coupon = modelMapper.map(couponDTO,  Coupon2.class);

        return couponRepository.save(coupon);
    }



    // 버전 select (페이징)
    public Page<CouponDTO> getAllVersions(int page, int size) {
        Pageable pageable = PageRequest.of(page, size); // 페이지 번호와 크기 설정
        Page<Coupon2> coupons = couponRepository.findAll(pageable);

        // Version 객체를 VersionDTO로 변환
        return coupons.map(coupon -> modelMapper.map(coupon, CouponDTO.class));
    }



}

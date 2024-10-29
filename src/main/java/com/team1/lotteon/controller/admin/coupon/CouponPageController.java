package com.team1.lotteon.controller.admin.coupon;

import com.team1.lotteon.dto.CouponDTO;
import com.team1.lotteon.dto.MemberDTO;
import com.team1.lotteon.dto.ShopDTO;
import com.team1.lotteon.dto.pageDTO.NewPageRequestDTO;
import com.team1.lotteon.dto.pageDTO.NewPageResponseDTO;
import com.team1.lotteon.entity.Coupon;
import com.team1.lotteon.entity.Member;
import com.team1.lotteon.service.admin.CouponService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;

/*
     날짜 : 2024/10/21
     이름 : 최준혁
     내용 : 등록된 상점 출력

     수정이력
      - 2024/10/29 이도영 - 쿠폰 출력 , 삭제 기능
*/
@Log4j2
@Controller
@RequiredArgsConstructor
public class CouponPageController {

    private final CouponService couponService;
    private final ModelMapper modelMapper;
    @GetMapping("/admin/coupon/list")
    public String list(@RequestParam(required = false) String type,
                       @RequestParam(required = false) String keyword,
                       NewPageRequestDTO newPageRequestDTO, Model model){
        // 검색 조건 설정
        newPageRequestDTO.setType(type);
        newPageRequestDTO.setKeyword(keyword);
        NewPageResponseDTO<CouponDTO> coupondto = couponService.selectCouponAll(newPageRequestDTO);
        model.addAttribute("coupondtos", coupondto);
        model.addAttribute("type", type);
        model.addAttribute("keyword", keyword);
        return "admin/coupon/list";
    }
    @PostMapping("/admin/coupon/insertcoupon")
    public String insertcoupon(MemberDTO memberDTO, ShopDTO shopDTO, CouponDTO couponDTO) {
        // 1. 필요한 정보를 로그로 출력 (디버깅 용도)
        log.info("Member ID: " + couponDTO.getMemberId());
        log.info("Shop Name: " + shopDTO.getShopName());
        log.info("Coupon Name: " + couponDTO.getCouponname());
        log.info("Start Date: " + couponDTO.getCouponstart());
        log.info("End Date: " + couponDTO.getCouponend());
        LocalDateTime startDateTime = LocalDateTime.parse(couponDTO.getCouponstart() + "T00:00:00");
        LocalDateTime endDateTime = LocalDateTime.parse(couponDTO.getCouponend() + "T23:59:59");
        // 2. CouponDTO를 Coupon 엔티티로 매핑
        Coupon coupon = modelMapper.map(couponDTO, Coupon.class);
        coupon.setCouponstart(startDateTime); // startDateTime 설정
        coupon.setCouponend(endDateTime); // endDateTime 설정
        // 3. 발급자 정보를 설정
        Member member = new Member();
        member.setUid(couponDTO.getMemberId()); // memberDTO에서 UID를 가져옴
        coupon.setMember(member); // 쿠폰에 발급자 설정
        coupon.setCoupongive(0L);
        coupon.setCouponuse(0L);
        coupon.setCouponstate("ready");
        // 4. 쿠폰 저장
        couponService.insertCoupon(coupon);

        // 5. 성공 메시지를 추가하고, 리다이렉션 또는 뷰 반환
        return "redirect:/admin/coupon/list"; // 쿠폰 목록 페이지로 리다이렉트
    }
    @GetMapping("/admin/coupon/issued")
    public String issued(){

        return "admin/coupon/issued";
    }
}

package com.team1.lotteon.controller.admin.coupon;

import com.team1.lotteon.dto.CouponDTO;
import com.team1.lotteon.dto.MemberDTO;
import com.team1.lotteon.dto.ShopDTO;
import com.team1.lotteon.dto.pageDTO.NewPageRequestDTO;
import com.team1.lotteon.dto.pageDTO.NewPageResponseDTO;
import com.team1.lotteon.entity.Coupon;
import com.team1.lotteon.entity.Member;
import com.team1.lotteon.security.MyUserDetails;
import com.team1.lotteon.service.admin.CouponService;
import com.team1.lotteon.service.admin.CouponTakeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/*
     날짜 : 2024/10/21
     이름 : 최준혁
     내용 : 등록된 상점 출력

     수정이력
      - 2024/10/29 이도영 - 쿠폰 등록 출력
      - 2024/10/30 이도영 - 쿠폰 개별 출력
      - 2024/11/01 이도영 - 다운로드한 쿠폰 출력
      - 2024/11/05 이도영 - 쿠폰삭제, 관리자와 판매자에 따라 쿠폰 정보 출력 변경
      - 2024/11/08 이도영 - 쿠폰 등록일이 오늘이면 시작으로 변경
*/
@Log4j2
@Controller
@RequiredArgsConstructor
public class CouponPageController {

    private final CouponService couponService;
    private final CouponTakeService couponTakeService;
    private final ModelMapper modelMapper;
    //등록한 쿠폰 정보 출력
    @GetMapping("/admin/coupon/list")
    public String list(@RequestParam(required = false) String type,
                       @RequestParam(required = false) String keyword,
                       NewPageRequestDTO newPageRequestDTO, Model model){
        MyUserDetails userDetails = (MyUserDetails) model.getAttribute("userDetails");
        String role = userDetails.getMember().getRole();
        String uid = userDetails.getMember().getUid();
        // 검색 조건 설정
        newPageRequestDTO.setType(type);
        newPageRequestDTO.setKeyword(keyword);
        NewPageResponseDTO<CouponDTO> coupondto = couponService.selectCouponAll(newPageRequestDTO,role,uid);
        model.addAttribute("coupondtos", coupondto);
        model.addAttribute("type", type);
        model.addAttribute("keyword", keyword);
        return "admin/coupon/list";
    }

    //선택된 쿠폰 모달 출력
    @GetMapping("admin/coupon/select/{id}")
    public ResponseEntity<CouponDTO> select(@PathVariable Long id, Model model){
        log.info("LLLLL"+ id.toString());
        CouponDTO coupon = couponService.findCouponById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Coupon not found"));
        return ResponseEntity.ok(coupon);
    }


    //쿠폰 등록 
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
        // 4. startDateTime이 오늘이라면 couponstate를 "start"로 설정
        if (startDateTime.toLocalDate().isEqual(LocalDate.now())) {
            coupon.setCouponstate("start");
        } else {
            coupon.setCouponstate("ready");
        }

        // 5. 쿠폰 저장
        couponService.insertCoupon(coupon);

        // 5. 성공 메시지를 추가하고, 리다이렉션 또는 뷰 반환
        return "redirect:/admin/coupon/list"; // 쿠폰 목록 페이지로 리다이렉트
    }

    //멤버 이름을 활용해서 쿠폰 정보 가지고 오기
    @GetMapping("/coupon/member/{memberId}")
    public ResponseEntity<List<Coupon>> getCouponsByMemberId(@PathVariable String memberId) {
        List<Coupon> coupons = couponService.findCouponsByMemberId(memberId);
        return ResponseEntity.ok(coupons);
    }
    //쿠폰 삭제하기 기능
    @DeleteMapping("/admin/coupon/delete/{id}")
    public ResponseEntity<Void> deleteCoupon(@PathVariable Long id) {
        couponService.deleteCouponById(id);
        return ResponseEntity.noContent().build();
    }

}

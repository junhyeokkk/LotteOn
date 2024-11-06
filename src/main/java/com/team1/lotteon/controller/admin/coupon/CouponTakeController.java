package com.team1.lotteon.controller.admin.coupon;

import com.team1.lotteon.dto.CouponTakeDTO;
import com.team1.lotteon.dto.pageDTO.NewPageRequestDTO;
import com.team1.lotteon.dto.pageDTO.NewPageResponseDTO;
import com.team1.lotteon.security.MyUserDetails;
import com.team1.lotteon.service.admin.CouponTakeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
/*
     날짜 : 2024/10/30
     이름 : 이도영(최초 작성자)
     내용 : CouponTakeController 생성

     수정이력
     - 2024/11/01 이도영 선택된 쿠폰의 세부사항 임시 작업
     - 2024/11/03 이도영 선택된 쿠폰의 선택된 쿠폰 저장 완료
     - 2024/11/05 이도영 발급된 쿠폰에 대한 관리자 판매자 출력 방식 수정, 쿠폰 사용 가능 상태 변경
     - 2024/11/05 이도영 관리자가 진입시 shopid null 처리
*/
@Log4j2
@Controller
@RequiredArgsConstructor
public class CouponTakeController {
    private final CouponTakeService couponTakeService;
    //발급된 쿠폰 정보 출력
    @GetMapping("/admin/coupon/issued")
    public String issued(@RequestParam(required = false) String type,
                         @RequestParam(required = false) String keyword,
                         NewPageRequestDTO newPageRequestDTO, Model model){
        MyUserDetails userDetails = (MyUserDetails) model.getAttribute("userDetails");
        String role = userDetails.getMember().getRole();
        Long shopid = null;
        if (userDetails.getSellerMember() != null && userDetails.getSellerMember().getShop() != null) {
            shopid = userDetails.getSellerMember().getShop().getId();
        }
        // 검색 조건 설정
        newPageRequestDTO.setType(type);
        newPageRequestDTO.setKeyword(keyword);
        NewPageResponseDTO<CouponTakeDTO> coupontakedto = couponTakeService.selectcoupontakeAll(newPageRequestDTO,role,shopid);
        model.addAttribute("coupontakedtos", coupontakedto);
        model.addAttribute("type", type);
        model.addAttribute("keyword", keyword);
        return "admin/coupon/issued";
    }
    //선택된 쿠폰 저장 하기
    @GetMapping("/coupontake/set/{memberid}/{shopid}/{couponid}")
    public ResponseEntity<?> saveCouponTake(@PathVariable String memberid, @PathVariable(required = false) Long shopid, @PathVariable Long couponid) {
        log.info("memberid: " + memberid);
        log.info("shopid: " + shopid);
        log.info("couponid: " + couponid);

        try {
            CouponTakeDTO savedCouponTake = couponTakeService.saveCouponTake(memberid, shopid, couponid);
            return ResponseEntity.ok(savedCouponTake);
        } catch (ResponseStatusException e) {
            if (e.getStatusCode() == HttpStatus.CONFLICT) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getReason());
            }
            throw e;
        }
    }
    //쿠폰 전체 저장
    @GetMapping("/coupontake/all/{memberid}/{shopid}")
    public ResponseEntity<List<CouponTakeDTO>> saveCouponTakeList(
            @PathVariable String memberid,
            @PathVariable(required = false) Long shopid,
            @RequestParam List<Long> couponIds) {
        List<CouponTakeDTO> savedCouponTakes = couponTakeService.saveCouponTakeList(memberid, shopid, couponIds);
        return ResponseEntity.ok(savedCouponTakes);
    }

    //쿠폰 사용 가능 상태 변경
    @PostMapping("/admin/coupontake/cancelorrestart/{couponId}/{couponUseCheck}")
    public ResponseEntity<String> cancelOrRestartCoupon(
            @PathVariable Long couponId,
            @PathVariable int couponUseCheck) {
        boolean updated = couponTakeService.updateCouponStatus(couponId, couponUseCheck);

        if (updated) {
            return ResponseEntity.ok("Coupon status updated successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Coupon not found.");
        }
    }
    //선택된 다운로드한 쿠폰 모달 출력
    @GetMapping("admin/coupontake/select/{id}")
    public ResponseEntity<CouponTakeDTO> selecttake(@PathVariable Long id, Model model){
        log.info("LLLLL"+ id.toString());
        CouponTakeDTO coupon = couponTakeService.findCouponTakeById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Coupon not found"));
        return ResponseEntity.ok(coupon);
    }
}

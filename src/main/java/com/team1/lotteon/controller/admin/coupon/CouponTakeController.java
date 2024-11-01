package com.team1.lotteon.controller.admin.coupon;

import com.team1.lotteon.dto.CouponDTO;
import com.team1.lotteon.dto.CouponTakeDTO;
import com.team1.lotteon.entity.CouponTake;
import com.team1.lotteon.service.admin.CouponTakeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
/*
     날짜 : 2024/10/30
     이름 : 이도영(최초 작성자)
     내용 : CouponTakeController 생성

     수정이력
     - 2024/11/01 이도영 선택된 쿠폰의 세부사항 임시 작업
*/
@Log4j2
@Controller
@RequiredArgsConstructor
public class CouponTakeController {
    private final CouponTakeService couponTakeService;

    //선택된 쿠폰 저장 하기
    @GetMapping("/coupontake/set/{memberid}/{shopid}/{couponid}")
    public ResponseEntity<CouponTakeDTO> saveCouponTake(@PathVariable String memberid, @PathVariable(required = false) Long shopid, @PathVariable Long couponid) {
        CouponTakeDTO savedCouponTake = couponTakeService.saveCouponTake(memberid, shopid, couponid);
        return ResponseEntity.ok(savedCouponTake);
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
    //사용자가 가지고 있는 쿠폰 가지고 오기(상점이 가지고 있는)
    @GetMapping("/coupontake/get/{memberid}/{shopid}")
    public ResponseEntity<List<CouponTake>> select(@PathVariable String memberid, @PathVariable(required = false) Long shopid) {
        log.info("shopid: " + (shopid != null ? shopid.toString() : "null"));
        log.info("memberid: " + memberid);

        List<CouponTake> couponTakeList = couponTakeService.findByMemberIdAndOptionalShopId(memberid, shopid);

        if (couponTakeList.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Coupons not found");
        }

        return ResponseEntity.ok(couponTakeList);
    }

}

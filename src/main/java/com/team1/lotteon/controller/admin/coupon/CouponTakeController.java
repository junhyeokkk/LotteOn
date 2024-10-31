package com.team1.lotteon.controller.admin.coupon;

import com.team1.lotteon.dto.CouponTakeDTO;
import com.team1.lotteon.service.admin.CouponTakeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
/*
     날짜 : 2024/10/30
     이름 : 이도영(최초 작성자)
     내용 : CouponTakeController 생성
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
    //사용자가 가지고 있는 쿠폰 가지고 오기(상점이 가지고 있는)
    @GetMapping("/coupontake/get/{memberid}/{shopid}")
    public ResponseEntity<List<CouponTakeDTO>> select(@PathVariable String memberid, @PathVariable(required = false) Long shopid) {
        log.info("shopid: " + (shopid != null ? shopid.toString() : "null"));
        log.info("memberid: " + memberid);

        List<CouponTakeDTO> couponTakeList = couponTakeService.findByMemberIdAndShopId(memberid, shopid);

        if (couponTakeList.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Coupons not found");
        }

        return ResponseEntity.ok(couponTakeList);
    }

}

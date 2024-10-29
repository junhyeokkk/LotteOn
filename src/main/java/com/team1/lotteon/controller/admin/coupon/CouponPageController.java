package com.team1.lotteon.controller.admin.coupon;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
/*
     날짜 : 2024/10/23
     이름 : 최준혁
     내용 : 관리자 쿠폰 컨트롤러 생성
*/
@Controller
public class CouponPageController {

    @GetMapping("/admin/coupon/list")
    public String list(){

        return "admin/coupon/list";
    }

    @GetMapping("/admin/coupon/issued")
    public String issued(){

        return "admin/coupon/issued";
    }
}

package com.team1.lotteon.controller.admin.order;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
/*
     날짜 : 2024/10/23
     이름 : 최준혁
     내용 : 관리자 주문 컨트롤러 생성

*/
@Controller
public class OrderPageController {

    @GetMapping("/admin/order/delivery")
    public String delivery(){

        return "admin/order/delivery";
    }

    @GetMapping("/admin/order/list")
    public String list(){

        return "admin/order/list";
    }
}

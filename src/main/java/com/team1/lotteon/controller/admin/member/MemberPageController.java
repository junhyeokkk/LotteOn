package com.team1.lotteon.controller.admin.member;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MemberPageController {

    @GetMapping("/admin/member/list")
    public String list(){

        return "admin/member/list";
    }

    @GetMapping("/admin/member/point")
    public String point(){

        return "admin/member/point";
    }
}

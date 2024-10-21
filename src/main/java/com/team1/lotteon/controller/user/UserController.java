package com.team1.lotteon.controller.user;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.Console;

@Log4j2
@Controller
public class UserController {
    //회원가입 (판매자,회원) 선택 화면
    @GetMapping("/user/join")
    public String joinPage() {

        return "user/join";
    }
    //로그인 화면
    @GetMapping("/user/login")
    public String loginPage() {

        return "user/login";
    }
}
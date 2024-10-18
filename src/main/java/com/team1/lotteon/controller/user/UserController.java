package com.team1.lotteon.controller.user;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Log4j2
@Controller
public class UserController {

    @GetMapping("/user/join")
    public String joinPage() {

        return "/user/join";
    }

    @GetMapping("/user/login")
    public String loginPage() {

        return "user/login";
    }

    @GetMapping("/user/register")
    public String registerPage() {

        return "/user/register";
    }

    @GetMapping("/user/registerSeller")
    public String registerSellerPage() {

        return "/user/registerSeller";
    }

    @GetMapping("/user/signup")
    public String sighupPage() {

        return "/user/signup";
    }






}
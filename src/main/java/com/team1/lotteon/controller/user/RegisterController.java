package com.team1.lotteon.controller.user;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Log4j2
@Controller
public class RegisterController {
    //회원가입 정보 입력 (판매자, 일반회원) 구분
    @GetMapping("/user/register/{member}")
    public String registerPage(@PathVariable String member, Model model) {
        log.info(member);
        if(member.equals("user")){
            return "user/register";
        }
        else if(member.equals("seller")){
            return "user/registerSeller";
        }

        return "user/login";

    }
}

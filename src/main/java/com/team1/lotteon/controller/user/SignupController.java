package com.team1.lotteon.controller.user;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Log4j2
@Controller
public class SignupController {

    //회원가입시 동의하기 화면 출력(판매자, 일반회원)
    @GetMapping("/user/signup/{member}")
    public String signupPage(@PathVariable String member, Model model) {
        // 회원 유형에 따라 처리할 데이터를 다르게 설정
        if (member.equals("user")) {
            // 일반회원 가입 데이터 처리
            model.addAttribute("membershipType", "일반회원가입");
            model.addAttribute("description", "개인구매회원 (외국인포함)");
        } else if (member.equals("seller")) {
            // 판매회원 가입 데이터 처리
            model.addAttribute("membershipType", "판매회원가입");
            model.addAttribute("description", "사업자판매회원");
        }
        log.info(member);
        model.addAttribute("member", member);
        return "user/signup";  // 같은 뷰를 반환하되 데이터를 다르게 렌더링
    }
}

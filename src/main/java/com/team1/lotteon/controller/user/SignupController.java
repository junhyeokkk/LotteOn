package com.team1.lotteon.controller.user;

import com.team1.lotteon.entity.Term;
import com.team1.lotteon.service.PolicyService;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@Log4j2
@Controller
public class SignupController {

    private final PolicyService policyService;

    // 생성자 주입을 통해 PolicyService를 주입
    public SignupController(PolicyService policyService) {
        this.policyService = policyService;
    }

    // 회원가입 시 동의하기 화면 출력 (판매자, 일반회원)
    @GetMapping("/user/signup/{member}")
    public String signupPage(@PathVariable String member, Model model) {
        String termCode; // 약관 코드를 위한 변수 선언

        // 회원 유형에 따라 약관 코드를 결정
        if (member.equals("user")) {
            model.addAttribute("membershipType", "일반회원가입");
            model.addAttribute("description", "개인구매회원 (외국인포함)");
            termCode = "buyer"; // 일반회원 약관 코드
        } else if (member.equals("seller")) {
            model.addAttribute("membershipType", "판매회원가입");
            model.addAttribute("description", "사업자판매회원");
            termCode = "seller"; // 판매자 약관 코드
        } else {
            // 잘못된 회원 유형에 대한 처리
            model.addAttribute("error", "잘못된 회원 유형입니다.");
            return "error"; // 에러 페이지로 이동
        }

        // 약관 데이터 DB에서 조회
        Term terms = policyService.getTermsByCategory(termCode);
        if (terms != null) {
            model.addAttribute("terms", terms.getContent());
        } else {
            model.addAttribute("terms", "약관을 찾을 수 없습니다.");
        }

        // 공통 약관 추가
        addCommonTermsToModel(model);

        // 일반회원만 위치정보 약관 제공
        if (member.equals("user")) {
            Term locationTerms = policyService.getTermsByCategory("location");
            if (locationTerms != null) {
                model.addAttribute("location", locationTerms.getContent());
            }
        }

        log.info("회원 유형: " + member);
        model.addAttribute("member", member);
        return "user/signup"; // 사용자 정의 뷰로 이동
    }

    // 공통 약관 추가 메소드
    private void addCommonTermsToModel(Model model) {
        Term financeTerms = policyService.getTermsByCategory("finance");
        if (financeTerms != null) {
            model.addAttribute("finance", financeTerms.getContent());
        }

        Term privacyTerms = policyService.getTermsByCategory("privacy");
        if (privacyTerms != null) {
            model.addAttribute("privacy", privacyTerms.getContent());
        }
    }
}

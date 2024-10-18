package com.team1.lotteon.controller.policy;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Log4j2
@Controller
public class PolicyController {

    @GetMapping("/policy/index")
    public String index() {
        return "/policy/index";
    }

    @GetMapping("/policy/{cate}")
    public String terms(@PathVariable String cate, Model model) {
        String buy;
        String termsDetail;

        // switch-case로 카테고리별 약관 설정
        switch (cate) {
            case "buyer":
                buy = "구매회원 이용약관";
                termsDetail = "구매회원 이용약관의 세부 내용입니다...";
                break;
            case "seller":
                buy = "판매회원 이용약관";
                termsDetail = "판매회원 이용약관의 세부 내용입니다...";
                break;
            case "finance":
                buy = "전자금융거래 이용약관";
                termsDetail = "전자금융거래에 대한 세부 내용입니다...";
                break;
            case "privacy":
                buy = "개인정보처리방침";
                termsDetail = "개인정보처리방침의 세부 내용입니다...";
                break;
            case "location":
                buy = "위치정보 이용약관";
                termsDetail = "위치정보 이용약관의 세부 내용입니다...";
                break;
            default:
                // cate 값이 올바르지 않은 경우 처리
                log.error("잘못된 카테고리 요청: " + cate);
                return "error/404"; // 404 페이지 또는 다른 에러 페이지로 리다이렉트
        }

        // 로그 출력
        log.info("Requested category: " + cate + ", Buy: " + buy);

        // 모델에 데이터 추가
        model.addAttribute("terms", buy);
        model.addAttribute("termsDetail", termsDetail);

        return "policy/index";
    }
}

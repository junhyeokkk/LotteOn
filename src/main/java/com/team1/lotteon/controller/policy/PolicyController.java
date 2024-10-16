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

        // switch-case로 카테고리별 약관 설정
        switch (cate) {
            case "buyer":
                buy = "구매이용약관";
                break;
            case "seller":
                buy = "판매회원약관";
                break;
            case "finance":
                buy = "전자금융약관";
                break;
            case "privacy":
                buy = "개인정보처리";
                break;
            case "location":
                buy = "위치정보약관";
                break;
            default:
                buy = "기본 약관"; // 카테고리가 일치하지 않을 때 기본값 설정
                break;
        }

        // 로그 출력
        log.info("Requested category: " + cate + ", Buy: " + buy);

        // 모델에 데이터 추가
        model.addAttribute("terms", buy);

        return "/policy/index";
    }
}

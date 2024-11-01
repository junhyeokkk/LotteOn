package com.team1.lotteon.controller.policy;

import com.team1.lotteon.entity.Term;
import com.team1.lotteon.service.PolicyService;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
/*
     날짜 : 2024/10/25
     이름 : 박서홍
     내용 : 이용약관 컨트롤러 생성
*/

@Log4j2
@Controller
public class PolicyController {

    private final PolicyService policyService;

    public PolicyController(PolicyService policyService) {
        this.policyService = policyService;
    }

    @GetMapping("/policy/index")
    public String index() {
        return "policy/index";
    }

    @GetMapping("/policy/{cate}")
    public String terms(@PathVariable String cate, Model model) {
        String buy;
        String termsDetail;

        // 카테고리에 따라 약관 가져오기
        Term term = policyService.getTermsByCategory(cate);
        // switch-case로 카테고리별 약관 설정
        switch (cate) {
            case "buyer":
                buy = "구매회원 이용약관";
                termsDetail = term != null ? term.getContent() : "약관 내용이 없습니다.";
                break;
            case "seller":
                buy = "판매회원 이용약관";
                termsDetail = term != null ? term.getContent() : "약관 내용이 없습니다.";
                break;
            case "finance":
                buy = "전자금융거래 이용약관";
                termsDetail = term != null ? term.getContent() : "약관 내용이 없습니다.";
                break;
            case "privacy":
                buy = "개인정보처리방침";
                termsDetail = term != null ? term.getContent() : "약관 내용이 없습니다.";
                break;
            case "location":
                buy = "위치정보 이용약관";
                termsDetail = term != null ? term.getContent() : "약관 내용이 없습니다.";
                break;
            default:
                buy = "기타 약관";
                termsDetail = "기타 약관의 세부 내용입니다..."; // 여기에서 DB에서 가져온 내용으로 변경 가능
                break;
        }

        // DB에서 가져온 약관 내용에서 '제1조', '제2조' 등의 제목을 h2 태그로 감싸기
        if (termsDetail != null) {
            termsDetail = termsDetail.replaceAll("제(\\d+)조 \\((.*?)\\)", "<h2>제$1조 ($2)</h2>");
        }
        if (termsDetail != null) {
            termsDetail = termsDetail.replaceAll("제\\s*(\\d+)장\\s*(\\((.*?)\\))?\\s*(.*)", "<h3>제 $1장$2 $4</h3>");
        }
        // 줄 바꿈 처리
        if (term != null) {
            termsDetail = termsDetail.replace("\n", "<br>");  // \n을 <br>로 변환
        }
        // 로그 출력
        log.info("Requested category: " + cate + ", Buy: " + buy);

        // 모델에 데이터 추가
        model.addAttribute("terms", buy);
        model.addAttribute("termsDetail", termsDetail);

        return "policy/index";

    }
}

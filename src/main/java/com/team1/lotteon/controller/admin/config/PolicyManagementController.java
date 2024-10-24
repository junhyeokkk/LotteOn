package com.team1.lotteon.controller.admin.config;

import com.team1.lotteon.entity.Term;
import com.team1.lotteon.service.PolicyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Log4j2
@Controller
@RequiredArgsConstructor
public class PolicyManagementController {

    private final PolicyService policyService;


    @GetMapping("/admin/config/policy")
    public String showAllPolicies(Model model) {
        Term buyerTerm = policyService.getTermsByCategory("buyer");
        Term sellerTerm = policyService.getTermsByCategory("seller");
        Term financeTerm = policyService.getTermsByCategory("finance");
        Term privacyTerm = policyService.getTermsByCategory("privacy");
        Term locationTerm = policyService.getTermsByCategory("location");

        model.addAttribute("termsBuyer", buyerTerm != null ? buyerTerm.getContent() : "");
        model.addAttribute("termsSeller", sellerTerm != null ? sellerTerm.getContent() : "");
        model.addAttribute("termsFinance", financeTerm != null ? financeTerm.getContent() : "");
        model.addAttribute("termsPrivacy", privacyTerm != null ? privacyTerm.getContent() : "");
        model.addAttribute("termsLocation", locationTerm != null ? locationTerm.getContent() : "");

        return "admin/config/policy"; // 모든 약관을 보여주는 페이지
    }


    @PostMapping("/admin/policy/update")
    public String updatePolicy(@RequestParam String termCode, @RequestParam String content, Model model) {
        // 약관 수정 처리
        if (termCode == null || termCode.isEmpty() || content == null || content.isEmpty()) {
            model.addAttribute("errorMessage", "모든 필드를 입력해 주세요.");
            return showAllPolicies(model); // 수정된 부분: 약관 수정 실패 시 전체 약관 페이지로 이동
        }

        // 약관을 업데이트
        Term term = policyService.getTermsByCategory(termCode);
        if (term != null) {
            term.setContent(content);
            policyService.updateTerm(term);
            model.addAttribute("successMessage", "약관이 성공적으로 수정되었습니다.");
        } else {
            model.addAttribute("errorMessage", "해당 약관을 찾을 수 없습니다.");
        }

        // 수정된 약관을 다시 보여주기 위해 페이지 새로 로드
        return showAllPolicies(model); // 수정된 부분: 전체 약관 페이지로 이동
    }

}

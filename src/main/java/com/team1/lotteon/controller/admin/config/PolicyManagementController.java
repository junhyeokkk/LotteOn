package com.team1.lotteon.controller.admin.config;

import com.team1.lotteon.entity.Term;
import com.team1.lotteon.service.PolicyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PolicyManagementController {

    private final PolicyService policyService;

    @Autowired
    public PolicyManagementController(PolicyService policyService) {
        this.policyService = policyService;
    }

    // 약관 관리 페이지
    @GetMapping("/admin/policy")
    public String showPolicyPage(Model model) {
        model.addAttribute("userTerms", policyService.getTermsByCategory("user_terms"));
        model.addAttribute("sellerTerms", policyService.getTermsByCategory("seller_terms"));
        model.addAttribute("financeTerms", policyService.getTermsByCategory("finance"));
        model.addAttribute("privacyTerms", policyService.getTermsByCategory("privacy"));
        return "admin/policy"; // 해당 뷰로 이동
    }

    // 약관 수정 처리
    @PostMapping("/admin/policy/update")
    public String updateTerm(@RequestParam String termCode, @RequestParam String content, Model model) {
        // termCode나 content가 빈 값인지 확인
        if (termCode == null || termCode.isEmpty() || content == null || content.isEmpty()) {
            model.addAttribute("errorMessage", "잘못된 요청입니다. 모든 필드를 채워 주세요.");
            return "admin/policy";
        }

        // termCode를 기반으로 약관 조회
        Term term = policyService.getTermsByCategory(termCode);
        if (term != null) {
            // 약관 내용 수정
            term.setContent(content);
            policyService.updateTerm(term); // DB에 저장

            // 수정 성공 메시지 전달
            model.addAttribute("successMessage", "약관이 성공적으로 수정되었습니다.");
            return "redirect:/admin/policy"; // 수정 완료 후 리다이렉션
        } else {
            // 에러 메시지 전달
            model.addAttribute("errorMessage", "해당 약관을 찾을 수 없습니다.");
            return "admin/policy"; // 오류 발생 시 다시 폼으로 이동
        }

    }
}


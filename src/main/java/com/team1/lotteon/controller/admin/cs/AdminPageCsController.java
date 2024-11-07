package com.team1.lotteon.controller.admin.cs;

import com.team1.lotteon.dto.PageResponseDTO;
import com.team1.lotteon.dto.cs.FaqDTO;
import com.team1.lotteon.dto.cs.InquiryDTO;
import com.team1.lotteon.dto.cs.NoticeDTO;
import com.team1.lotteon.service.article.ArticleService;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 *   날짜 : 2024/10/22
 *   이름 : 최준혁
 *   내용 : AdminPageCsController 생성
 *
 *   수정이력
 *  -2024/10/29 김소희 - 채용하기 추가
 */

@Log4j2
@Controller
public class AdminPageCsController {
private final ArticleService articleService;

    public AdminPageCsController(ArticleService articleService) {
        this.articleService = articleService;
    }

    // notice
    // list
    @GetMapping("/admin/cs/notice/list")
    public String notice_list(
            @RequestParam(required = false, defaultValue = "all") String type1,
            Model model,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {

        PageResponseDTO<NoticeDTO> notices;
        if ("all".equals(type1)) {
            notices = articleService.getAllNotices(pageable);
        } else {
            notices = articleService.getNoticesByType1(type1, pageable);
        }

        model.addAttribute("notices", notices.getContent());
        model.addAttribute("currentPage", notices.getCurrentPage());
        model.addAttribute("totalPages", notices.getTotalPages());
        model.addAttribute("totalElements", notices.getTotalElements());
        model.addAttribute("pageSize", notices.getPageSize());
        model.addAttribute("isLast", notices.isLast());
        model.addAttribute("selectedType1", type1); // 현재 선택된 type1 추가
        return "admin/cs/notice/list";
    }


    // view
    @GetMapping("/admin/cs/notice/view/{id}")
    public String noticeView(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            model.addAttribute("group", "notice");
            model.addAttribute("cate", "view");

            // 조회수 증가 로직
            articleService.incrementNoticeViews(id); // 조회수를 증가시키는 서비스 메서드 호출

            NoticeDTO notice = articleService.getNoticeById(id); // 조회수를 증가한 상태로 조회
            model.addAttribute("notice", notice);

            return "admin/cs/notice/view"; // view 페이지로 이동
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "해당 공지사항을 찾을 수 없습니다.");
            return "redirect:/admin/cs/notice/list"; // 목록 페이지로 리다이렉트
        }
    }
    // write
    @GetMapping("/admin/cs/notice/write")
    public String notice_write(){

        return "admin/cs/notice/write";
    }
    @PostMapping("/admin/cs/notice/write")
    public String notice_write(NoticeDTO noticeDTO){
        articleService.createNotice(noticeDTO);

        return "redirect:/admin/cs/notice/list";
    }
    // modify
    @GetMapping("/admin/cs/notice/modify/{id}")
    public String noticeModify(@PathVariable Long id, Model model) {
        NoticeDTO noticeDTO = articleService.getNoticeById(id);
        model.addAttribute("notice", noticeDTO);
        return "admin/cs/notice/modify";
    }
    @PostMapping("/admin/cs/notice/modify/{id}")
    public String noticeModify(@PathVariable Long id, @ModelAttribute NoticeDTO noticeDTO) {
        articleService.updateNotice(id, noticeDTO);
        return "redirect:/admin/cs/notice/list";
    }


    // faq
    @GetMapping("/admin/cs/faq/list")
    public String getFaqList(
            @RequestParam(value = "type1", required = false, defaultValue = "1차선택") String type1,
            @RequestParam(value = "type2", required = false, defaultValue = "2차선택") String type2,
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            Model model) {

        Pageable pageable = PageRequest.of(page, 10);
        PageResponseDTO<FaqDTO> faqsPage;

        // "1차선택"과 "2차선택"일 때 전체 데이터를 가져옵니다.
        if ("1차선택".equals(type1) && "2차선택".equals(type2)) {
            faqsPage = articleService.getAllFaqs(pageable);
        } else if (!"1차선택".equals(type1) && "2차선택".equals(type2)) {
            // type1만 선택된 경우, type1에 해당하는 데이터만 가져옵니다.
            faqsPage = articleService.findFaqByType1(type1, pageable);
        } else {
            // type1과 type2 모두 선택된 경우, 해당 조건에 맞는 데이터를 가져옵니다.
            faqsPage = articleService.findFaqByType(type1, type2, pageable);
        }

        // type2 옵션 설정
        Map<String, List<String>> type2Options = new HashMap<>();
        type2Options.put("회원", Arrays.asList("가입", "탈퇴"));
        type2Options.put("쿠폰/이벤트", Arrays.asList("이벤트 참여", "쿠폰 사용"));
        type2Options.put("주문/결제", Arrays.asList("주문 확인", "결제 문제"));
        type2Options.put("배송", Arrays.asList("배송 추적", "배송 지연"));
        type2Options.put("취소/반품/환불", Arrays.asList("취소 요청", "반품 요청", "환불 요청"));
        type2Options.put("여행/숙박/항공", Arrays.asList("호텔 예약", "항공권 예약"));
        type2Options.put("안전결제", Arrays.asList("결제 안전 확인", "보안 문제 신고"));

        model.addAttribute("faqs", faqsPage.getContent());
        model.addAttribute("currentPage", faqsPage.getCurrentPage());
        model.addAttribute("totalPages", faqsPage.getTotalPages());
        model.addAttribute("selectedType1", type1);
        model.addAttribute("selectedType2", type2);
        model.addAttribute("type2Options", type2Options);

        return "admin/cs/faq/list";
    }


    // view
    @GetMapping("/admin/cs/faq/view/{id}")
    public String faq_view(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            model.addAttribute("group", "faq");
            model.addAttribute("cate", "view");

            // 조회수 증가 로직
            articleService.incrementFaqViews(id);

            FaqDTO faq = articleService.getFaqById(id);
            model.addAttribute("faq", faq);

            return "admin/cs/faq/view";
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "해당 자주 묻는 질문을 찾을 수 없습니다.");
            return "redirect:/admin/cs/faq/list";
        }
    }


    //write
    @GetMapping("/admin/cs/faq/write")
    public String faqWrite(
            @RequestParam(value = "type1", required = false, defaultValue = "1차선택") String type1,
            Model model
    ) {
        Map<String, List<String>> type2Options = new HashMap<>();
        type2Options.put("회원", Arrays.asList("가입", "탈퇴"));
        type2Options.put("쿠폰/이벤트", Arrays.asList("이벤트 참여", "쿠폰 사용"));
        type2Options.put("주문/결제", Arrays.asList("주문 확인", "결제 문제"));
        type2Options.put("배송", Arrays.asList("배송 추적", "배송 지연"));
        type2Options.put("취소/반품/환불", Arrays.asList("취소 요청", "반품 요청", "환불 요청"));
        type2Options.put("여행/숙박/항공", Arrays.asList("호텔 예약", "항공권 예약"));
        type2Options.put("안전결제", Arrays.asList("결제 안전 확인", "보안 문제 신고"));

        List<String> type2List = type2Options.getOrDefault(type1, List.of("2차선택"));

        model.addAttribute("type1", type1);
        model.addAttribute("type2List", type2List);
        model.addAttribute("type2Options", type2Options);

        return "admin/cs/faq/write";
    }
    @PostMapping("/admin/cs/faq/write")
    public String faq_write(FaqDTO faqDTO){
        articleService.createFaq(faqDTO);

        return "redirect:/admin/cs/faq/list";
    }
    // modify
    @GetMapping("/admin/cs/faq/modify/{id}")
    public String faqModify(@PathVariable Long id, Model model) {
        FaqDTO faqDTO = articleService.getFaqById(id);

        model.addAttribute("faq", faqDTO);
        return "admin/cs/faq/modify";
    }
    @PostMapping("/admin/cs/faq/modify/{id}")
    public String faqModify(@PathVariable Long id, @ModelAttribute FaqDTO faqDTO) {
        articleService.updateFaq(id, faqDTO);
        return "redirect:/admin/cs/faq/list";
    }

    // qna
    // list
    @GetMapping("/admin/cs/qna/list")
    public String qna_list(
            @RequestParam(value = "type1", required = false, defaultValue = "1차선택") String type1,
            @RequestParam(value = "type2", required = false, defaultValue = "2차선택") String type2,
            Model model, @PageableDefault(size = 10) Pageable pageable) {

        PageResponseDTO<InquiryDTO> allInquiries;
        if ("1차선택".equals(type1) && "2차선택".equals(type2)) {
            // type1과 type2가 모두 기본값일 때 모든 데이터 조회
            allInquiries = articleService.getAllInquiries(pageable);
        } else if (!"1차선택".equals(type1) && "2차선택".equals(type2)) {
            // type1만 선택되었을 때 type1에 해당하는 데이터 조회
            allInquiries = articleService.findQnaByType1(type1, pageable);
        } else {
            // type1과 type2가 모두 선택되었을 때 필터링
            allInquiries = articleService.findQnaByType(type1, type2, pageable);
        }

        model.addAttribute("inquiries", allInquiries.getContent());
        model.addAttribute("currentPage", allInquiries.getCurrentPage());
        model.addAttribute("totalPages", allInquiries.getTotalPages());
        model.addAttribute("totalElements", allInquiries.getTotalElements());
        model.addAttribute("pageSize", allInquiries.getPageSize());
        model.addAttribute("isLast", allInquiries.isLast());
        model.addAttribute("selectedType1", type1);
        model.addAttribute("selectedType2", type2);

        Map<String, List<String>> type2Options = new HashMap<>();
        type2Options.put("회원", Arrays.asList("가입", "탈퇴", "회원정보", "로그인"));
        type2Options.put("쿠폰/이벤트", Arrays.asList("이벤트 참여", "쿠폰 사용"));
        type2Options.put("주문/결제", Arrays.asList("주문 확인", "결제 문제"));
        type2Options.put("배송", Arrays.asList("배송 추적", "배송 지연"));
        type2Options.put("취소/반품/환불", Arrays.asList("취소 요청", "반품 요청", "환불 요청"));
        type2Options.put("여행/숙박/항공", Arrays.asList("호텔 예약", "항공권 예약"));
        type2Options.put("안전결제", Arrays.asList("결제 안전 확인", "보안 문제 신고"));
        model.addAttribute("type2Options", type2Options);

        return "admin/cs/qna/list";
    }


    // view
    @GetMapping("/admin/cs/qna/view/{id}")
    public String qnaView(@PathVariable Long id, Model model) {
        InquiryDTO inquiry = articleService.getInquiryById(id);
        model.addAttribute("inquiry", inquiry);
        return "admin/cs/qna/view";
    }

    // reply
    @GetMapping("/admin/cs/qna/reply/{id}")
    public String qnaReply(@PathVariable Long id, Model model) {
        InquiryDTO inquiry = articleService.getInquiryById(id);
        model.addAttribute("inquiry", inquiry);
        return "admin/cs/qna/reply";
    }

    // 답변 등록
    @PostMapping("/admin/cs/qna/reply/{id}")
    public String saveReply(@PathVariable Long id, @RequestParam("answer") String answer, RedirectAttributes redirectAttributes) {
        articleService.updateInquiryAnswer(id, answer);
        redirectAttributes.addFlashAttribute("replySuccess", "답변이 등록되었습니다.");
        return "redirect:/admin/cs/qna/list";
    }


}

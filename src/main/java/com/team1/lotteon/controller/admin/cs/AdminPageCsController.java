package com.team1.lotteon.controller.admin.cs;

import com.team1.lotteon.dto.PageResponseDTO;
import com.team1.lotteon.dto.cs.FaqDTO;
import com.team1.lotteon.dto.cs.InquiryDTO;
import com.team1.lotteon.dto.cs.NoticeDTO;
import com.team1.lotteon.service.article.ArticleService;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

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
    public String notice_list(Model model, @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC)Pageable pageable){

        PageResponseDTO<NoticeDTO> allNotices = articleService.getAllNotices(pageable);
        log.info("allNotices = " + allNotices);
        // 페이징 정보와 데이터를 모델에 추가
        model.addAttribute("notices", allNotices.getContent()); // 문의사항 목록
        model.addAttribute("currentPage", allNotices.getCurrentPage()); // 현재 페이지 번호
        model.addAttribute("totalPages", allNotices.getTotalPages()); // 총 페이지 수
        model.addAttribute("totalElements", allNotices.getTotalElements()); // 총 요소 수
        model.addAttribute("pageSize", allNotices.getPageSize()); // 페이지 당 요소 수
        model.addAttribute("isLast", allNotices.isLast()); // 마지막 페이지 여부

        return "admin/cs/notice/list";
    }
    // view
    @GetMapping("/admin/cs/notice/view/{id}")
    public String noticeView(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            model.addAttribute("group", "notice");
            model.addAttribute("cate", "view");
            NoticeDTO notice = articleService.getNoticeById(id);
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
    @GetMapping("/admin/cs/notice/modify")
    public String notice_modify(){

        return "admin/cs/notice/modify";
    }


    // faq
    @GetMapping("/admin/cs/faq/list")
    public String faq_list(Model model){
            List<FaqDTO> faqs = articleService.findTop10ByOrderByCreatedAtDesc();
            model.addAttribute("faqs",faqs);

        return "admin/cs/faq/list";
    }

    // view
    @GetMapping("/admin/cs/faq/view/{id}")
    public String faq_view(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            model.addAttribute("group", "faq");
            model.addAttribute("cate", "view");
            FaqDTO faq = articleService.getFaqById(id);
            model.addAttribute("faq", faq);
            return "admin/cs/faq/view";
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "해당 공지사항을 찾을 수 없습니다.");
            return "redirect:/admin/cs/faq/list";
        }
    }
    //write
    @GetMapping("/admin/cs/faq/write")
    public String faq_write(){

        return "admin/cs/faq/write";
    }
    @PostMapping("/admin/cs/faq/write")
    public String faq_write(FaqDTO faqDTO){
        articleService.createFaq(faqDTO);

        return "redirect:/admin/cs/faq/list";
    }
    // modify
    @GetMapping("/admin/cs/faq/modify")
    public String faq_modify(){

        return "admin/cs/faq/modify";
    }


    // qna
    // list
    @GetMapping("/admin/cs/qna/list")
    public String qna_list(Model model, @PageableDefault(size = 10)Pageable pageable){
        PageResponseDTO<InquiryDTO> allInquiries = articleService.getAllInquiries(pageable);
        log.info("allInquiries = " + allInquiries);
        // 페이징 정보와 데이터를 모델에 추가
        model.addAttribute("inquiries", allInquiries.getContent()); // 문의사항 목록
        model.addAttribute("currentPage", allInquiries.getCurrentPage()); // 현재 페이지 번호
        model.addAttribute("totalPages", allInquiries.getTotalPages()); // 총 페이지 수
        model.addAttribute("totalElements", allInquiries.getTotalElements()); // 총 요소 수
        model.addAttribute("pageSize", allInquiries.getPageSize()); // 페이지 당 요소 수
        model.addAttribute("isLast", allInquiries.isLast()); // 마지막 페이지 여부

        return "admin/cs/qna/list";
    }
    // view
    @GetMapping("/admin/cs/qna/view/{id}")
    public String qna_view(@PathVariable Long id, Model model){
        model.addAttribute("group", "qna");
        model.addAttribute("cate", "view");
        InquiryDTO inquiry = articleService.getInquiryById(id);
        model.addAttribute("inquiry", inquiry);

        return "admin/cs/qna/view";
    }
    // reply
    @GetMapping("/admin/cs/qna/reply")
    public String qna_reply(){

        return "admin/cs/qna/reply";
    }

    // 채용하기
    @GetMapping("/admin/cs/recruit/list")
    public String recruit_list(){

        return "admin/cs/recruit/list";
    }
}

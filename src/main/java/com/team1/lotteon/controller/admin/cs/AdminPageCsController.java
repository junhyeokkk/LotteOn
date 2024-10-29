package com.team1.lotteon.controller.admin.cs;

import com.team1.lotteon.dto.PageResponseDTO;
import com.team1.lotteon.dto.cs.InquiryDTO;
import com.team1.lotteon.service.article.ArticleService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/*
 *   날짜 : 2024/10/22
 *   이름 : 최준혁
 *   내용 : AdminPageCsController 생성
 *
 *   수정이력
 *  -2024/10/29 김소희 - 채용하기 추가
 */

@Controller
public class AdminPageCsController {
private final ArticleService articleService;

    public AdminPageCsController(ArticleService articleService) {
        this.articleService = articleService;
    }

    // notice
    @GetMapping("/admin/cs/notice/list")
    public String notice_list(Model model, @PageableDefault(size = 5)Pageable pageable){

        PageResponseDTO<InquiryDTO> allInquiries = articleService.getAllInquiries(pageable);
        System.out.println("allInquiries = " + allInquiries);
        // 페이징 정보와 데이터를 모델에 추가
        model.addAttribute("inquiries", allInquiries.getContent()); // 문의사항 목록
        model.addAttribute("currentPage", allInquiries.getCurrentPage()); // 현재 페이지 번호
        model.addAttribute("totalPages", allInquiries.getTotalPages()); // 총 페이지 수
        model.addAttribute("totalElements", allInquiries.getTotalElements()); // 총 요소 수
        model.addAttribute("pageSize", allInquiries.getPageSize()); // 페이지 당 요소 수
        model.addAttribute("isLast", allInquiries.isLast()); // 마지막 페이지 여부

        return "admin/cs/notice/list";
    }
    @GetMapping("/admin/cs/notice/view")
    public String notice_view(){

        return "admin/cs/notice/view";
    }
    @GetMapping("/admin/cs/notice/write")
    public String notice_write(){

        return "admin/cs/notice/write";
    }
    @GetMapping("/admin/cs/notice/modify")
    public String notice_modify(){

        return "admin/cs/notice/modify";
    }

    // faq
    @GetMapping("/admin/cs/faq/list")
    public String faq_list(){

        return "admin/cs/faq/list";
    }
    @GetMapping("/admin/cs/faq/view")
    public String faq_view(){

        return "admin/cs/faq/view";
    }
    @GetMapping("/admin/cs/faq/write")
    public String faq_write(){

        return "admin/cs/faq/write";
    }
    @GetMapping("/admin/cs/faq/modify")
    public String faq_modify(){

        return "admin/cs/faq/modify";
    }

    //qna
    @GetMapping("/admin/cs/qna/list")
    public String qna_list(){

        return "admin/cs/qna/list";
    }
    @GetMapping("/admin/cs/qna/view")
    public String qna_view(){

        return "admin/cs/qna/view";
    }
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

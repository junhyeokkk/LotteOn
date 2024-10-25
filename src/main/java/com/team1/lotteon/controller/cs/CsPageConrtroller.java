package com.team1.lotteon.controller.cs;

import com.team1.lotteon.dto.PageResponseDTO;
import com.team1.lotteon.dto.cs.ArticleDTO;
import com.team1.lotteon.dto.cs.FaqDTO;
import com.team1.lotteon.dto.cs.InquiryDTO;
import com.team1.lotteon.dto.cs.NoticeDTO;
import com.team1.lotteon.service.article.ArticleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
/*
 *   날짜 : 2024/10/18
 *   이름 : 최준혁
 *   내용 : CsPageController 생성
 *
 *   수정이력
 *   - 2024/10/25 김소희 - 내용 추가
 */
@Log4j2
@RequiredArgsConstructor
@Controller
public class CsPageConrtroller {
private final ArticleService articleService;

    @GetMapping("/cs/index")
    public String index(Model model, @PageableDefault(size = 5) Pageable pageable){
        PageResponseDTO<NoticeDTO> notices = articleService.getAllNotices(pageable);
        model.addAttribute("notices", notices);

        List<FaqDTO> faqs = articleService.getAllFaqs();
        model.addAttribute("faqs", faqs);

        PageResponseDTO<InquiryDTO> inquiries = articleService.getAllInquiries(pageable);
        model.addAttribute("inquiries", inquiries);

//        List<InquiryDTO> inquiries = articleService.getAllInquiries(Pageable.unpaged()).getContent();
//        model.addAttribute("inquireis", inquiries);

        return "cs/index";
    }

    @GetMapping("/cs/layout/{group}/{cate}")
    public String index(@PathVariable String group, @PathVariable String cate, Model model,@PageableDefault Pageable pageable){

        log.info("컨트롤러 들어오니?");
        log.info("ggggggggg" + group);
        log.info(cate);
        model.addAttribute("group", group);
        model.addAttribute("cate", cate);

        // 문의하기
        if (group.equals("qna")&&cate.equals("list")){
            PageResponseDTO<InquiryDTO> allInquiries = articleService.getAllInquiries(pageable);
            System.out.println("allInquiries = " + allInquiries);
            // 페이징 정보와 데이터를 모델에 추가
            model.addAttribute("inquiries", allInquiries.getContent()); // 문의사항 목록
            model.addAttribute("currentPage", allInquiries.getCurrentPage()); // 현재 페이지 번호
            model.addAttribute("totalPages", allInquiries.getTotalPages()); // 총 페이지 수
            model.addAttribute("totalElements", allInquiries.getTotalElements()); // 총 요소 수
            model.addAttribute("pageSize", allInquiries.getPageSize()); // 페이지 당 요소 수
            model.addAttribute("isLast", allInquiries.isLast()); // 마지막 페이지 여부
        }

        // 공지사항
        if (group.equals("notice")&&cate.equals("list")){
            PageResponseDTO<NoticeDTO> allInquiries = articleService.getAllNotices(pageable);
            // 페이징 정보와 데이터를 모델에 추가
            model.addAttribute("notices", allInquiries.getContent()); // 문의사항 목록
            model.addAttribute("currentPage", allInquiries.getCurrentPage()); // 현재 페이지 번호
            model.addAttribute("totalPages", allInquiries.getTotalPages()); // 총 페이지 수
            model.addAttribute("totalElements", allInquiries.getTotalElements()); // 총 요소 수
            model.addAttribute("pageSize", allInquiries.getPageSize()); // 페이지 당 요소 수
            model.addAttribute("isLast", allInquiries.isLast()); // 마지막 페이지 여부
        }

        // 자주묻는 질문
        if(group.equals("faq")&&cate.equals("list")){
            List<FaqDTO> faqs = articleService.getAllFaqs();
            model.addAttribute("faqs",faqs);
        }

        return "cs/layout/cs_layout";
    }

    // 문의하기 글쓰기
    @PostMapping("/cs/layout/qna/write")
    public String writeQna(InquiryDTO inquiryDTO){
        articleService.createInquiry(inquiryDTO);
        return "redirect:/cs/layout/qna/list";
    }

    // 문의하기 글보기
    @GetMapping("/cs/layout/qna/view/{id}")
    public String viewInquiry(@PathVariable Long id, Model model) {
        model.addAttribute("group", "qna");
        model.addAttribute("cate", "view");
        InquiryDTO inquiry = articleService.getInquiryById(id);
        model.addAttribute("inquiry", inquiry);
        return "cs/layout/cs_layout";
    }

    // 공지사항 글보기
    @GetMapping("/cs/layout/notice/view/{id}")
    public String viewNotice(@PathVariable Long id, Model model) {
        model.addAttribute("group", "notice");
        model.addAttribute("cate", "view");
        NoticeDTO notice = articleService.getNoticeById(id);
        model.addAttribute("notice", notice);
        return "cs/layout/cs_layout";
    }

    // 자주묻는질문 글보기
    @GetMapping("/cs/layout/faq/view/{id}")
    public String viewFaq(@PathVariable Long id, Model model) {
        model.addAttribute("group", "faq");
        model.addAttribute("cate", "view");
        FaqDTO faq = articleService.getFaqById(id);
        model.addAttribute("faq", faq);
        return "cs/layout/cs_layout";
    }


}

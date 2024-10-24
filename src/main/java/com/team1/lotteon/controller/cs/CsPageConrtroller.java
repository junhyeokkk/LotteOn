package com.team1.lotteon.controller.cs;

import com.team1.lotteon.dto.PageResponseDTO;
import com.team1.lotteon.dto.cs.ArticleDTO;
import com.team1.lotteon.dto.cs.InquiryDTO;
import com.team1.lotteon.dto.cs.NoticeDTO;
import com.team1.lotteon.service.article.ArticleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Log4j2
@RequiredArgsConstructor
@Controller
public class CsPageConrtroller {
private final ArticleService articleService;

    @GetMapping("/cs/index")
    public String index(){

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
            model.addAttribute("inquiries", allInquiries.getContent()); // 문의사항 목록
            model.addAttribute("currentPage", allInquiries.getCurrentPage()); // 현재 페이지 번호
            model.addAttribute("totalPages", allInquiries.getTotalPages()); // 총 페이지 수
            model.addAttribute("totalElements", allInquiries.getTotalElements()); // 총 요소 수
            model.addAttribute("pageSize", allInquiries.getPageSize()); // 페이지 당 요소 수
            model.addAttribute("isLast", allInquiries.isLast()); // 마지막 페이지 여부
        }

        return "cs/layout/cs_layout";
    }

    //  문의하기 글쓰기
    @PostMapping("/cs/layout/qna/write")
    public String writeQna(InquiryDTO inquiryDTO){
        articleService.createInquiry(inquiryDTO);
        return "redirect:/cs/layout/qna/list";
    }

    // 문의하기 글보기
    @GetMapping("/cs/layout/qna/view/{id}")
    public String viewInquiry(@PathVariable Long id, Model model) {
        InquiryDTO inquiry = articleService.getInquiryById(id);
        model.addAttribute("inquiry", inquiry);
        return "/cs/qna/view";
    }

    // 공지사항 글보기
    @GetMapping("cs/layout/notice/write/{id}")
    public String viewNotice(@PathVariable Long id, Model model) {
        NoticeDTO notice = articleService.getNoticeById(id);
        model.addAttribute("notice", notice);
        return "/cs/notice/view";
    }

    // 자주묻는질문 글보기






}

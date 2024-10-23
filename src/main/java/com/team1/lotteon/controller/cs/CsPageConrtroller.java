package com.team1.lotteon.controller.cs;

import com.team1.lotteon.dto.PageResponseDTO;
import com.team1.lotteon.dto.cs.ArticleDTO;
import com.team1.lotteon.dto.cs.InquiryDTO;
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

        if (group.equals("qna")&&cate.equals("list")){
            PageResponseDTO<InquiryDTO> allInquiries = articleService.getAllInquiries(pageable);
            // 페이징 정보와 데이터를 모델에 추가
            model.addAttribute("inquiries", allInquiries.getContent()); // 문의사항 목록
            model.addAttribute("currentPage", allInquiries.getCurrentPage()); // 현재 페이지 번호
            model.addAttribute("totalPages", allInquiries.getTotalPages()); // 총 페이지 수
            model.addAttribute("totalElements", allInquiries.getTotalElements()); // 총 요소 수
            model.addAttribute("pageSize", allInquiries.getPageSize()); // 페이지 당 요소 수
            model.addAttribute("isLast", allInquiries.isLast()); // 마지막 페이지 여부
            // 로그 출력
            log.info("Inquiries: {}", allInquiries.getContent()); // 문의사항 목록을 로그로 출력
            log.info("Current Page: {}", allInquiries.getCurrentPage()); // 현재 페이지 번호 로그 출력
            log.info("Total Pages: {}", allInquiries.getTotalPages()); // 총 페이지 수 로그 출력
            log.info("Total Elements: {}", allInquiries.getTotalElements()); // 총 요소 수 로그 출력
            log.info("Page Size: {}", allInquiries.getPageSize()); // 페이지 당 요소 수 로그 출력
            log.info("Is Last Page: {}", allInquiries.isLast()); // 마지막 페이지 여부 로그 출력
        }

        return "cs/layout/cs_layout";
    }

//    문의하기 글쓰기
    @PostMapping("/cs/layout/qna/write")
    public String writeQna(InquiryDTO inquiryDTO){
        articleService.createInquiry(inquiryDTO);
        return "redirect:/cs/layout/qna/list";
    }




}

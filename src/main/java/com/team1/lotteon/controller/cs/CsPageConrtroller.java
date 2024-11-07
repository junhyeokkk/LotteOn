package com.team1.lotteon.controller.cs;

import com.team1.lotteon.dto.PageResponseDTO;
import com.team1.lotteon.dto.cs.FaqDTO;
import com.team1.lotteon.dto.cs.InquiryDTO;
import com.team1.lotteon.dto.cs.NoticeDTO;
import com.team1.lotteon.entity.Inquiry;
import com.team1.lotteon.repository.InquiryRepository;
import com.team1.lotteon.service.article.ArticleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/*
 *   날짜 : 2024/10/18
 *   이름 : 최준혁
 *   내용 : CsPageController 생성
 *
 *   수정이력
 *   - 2024/10/25 김소희 - 내용 추가
 *   - 2024/11/03 김소희 - 유형선택 추가
 */
@Log4j2
@RequiredArgsConstructor
@Controller
public class CsPageConrtroller {
private final ArticleService articleService;
private final InquiryRepository inquiryRepository;

    @GetMapping("/cs/index")
    public String index(Model model, @PageableDefault(size = 5) Pageable pageable){
        PageResponseDTO<NoticeDTO> notices = articleService.getAllNotices(pageable);
        model.addAttribute("notices", notices);

        List<Inquiry> inquiryList = inquiryRepository.findTop3ByOrderByIdDesc();
        List<InquiryDTO> inquiries = inquiryList.stream().map(InquiryDTO::new).toList();
        log.info("qna size :{}",inquiries.size());
        model.addAttribute("inquiries", inquiries);

        return "cs/index";
    }

    @GetMapping("/cs/layout/{group}/{cate}")
    public String index(@PathVariable String group, @PathVariable String cate, Model model,@PageableDefault Pageable pageable){

        log.info("컨트롤러 들어오니?");
        log.info("ggggggggg" + group);
        log.info(cate);
        model.addAttribute("group", group);
        model.addAttribute("cate", cate);
        System.out.println("11111111");
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

    // 문의하기 list
    @GetMapping("/cs/layout/qna/list")
    public String listQnas(
            @RequestParam(required = false, defaultValue = "회원") String type1,
            @RequestParam(defaultValue = "0") int page,
            Model model) {
        log.info("Listing inquiries for type1: {}", type1);
        Pageable pageable = PageRequest.of(page, 10);
        PageResponseDTO<InquiryDTO> inquiriesPage = articleService.getQnasByType1(type1, pageable);

        model.addAttribute("type1", type1);
        model.addAttribute("group", "qna");
        model.addAttribute("cate", "list");
        model.addAttribute("inquiries", inquiriesPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", inquiriesPage.getTotalPages());
        model.addAttribute("isLast", inquiriesPage.isLast());

        return "cs/layout/cs_layout";
    }
    // 문의하기 write
    @PostMapping("/cs/layout/qna/write")
    public String writeQna(@ModelAttribute InquiryDTO inquiryDTO){
        articleService.createInquiry(inquiryDTO);
        return "redirect:/cs/layout/qna/list";
    }

    @GetMapping("/cs/layout/qna/write")
    public String showWriteQnaForm(@RequestParam(required = false, defaultValue = "회원") String type1, Model model) {
        System.out.println("type1 = " + type1);
        model.addAttribute("type1", type1);
        return "cs/qna/write";
    }

    // 문의하기 view
    @GetMapping("/cs/layout/qna/view/{id}")
    public String viewInquiry(@PathVariable Long id, Model model) {
        model.addAttribute("group", "qna");
        model.addAttribute("cate", "view");
        InquiryDTO inquiry = articleService.getInquiryById(id);
        model.addAttribute("inquiry", inquiry);
        return "cs/layout/cs_layout";
    }

    // 공지사항 list
    @GetMapping("/cs/layout/notice/list")
    public String listNotices(
            @RequestParam(required = false, defaultValue = "전체") String type1,
            @RequestParam(defaultValue = "0") int page,
            Model model) {

        Pageable pageable = PageRequest.of(page, 10); // 1페이지당 10개씩
        PageResponseDTO<NoticeDTO> noticesPage = articleService.getNoticesByType1(type1, pageable);

        model.addAttribute("type1", type1);
        model.addAttribute("group", "notice");
        model.addAttribute("cate", "list");
        model.addAttribute("notices", noticesPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", noticesPage.getTotalPages());
        model.addAttribute("isLast", noticesPage.isLast());

        return "cs/layout/cs_layout";
    }
    // 공지사항 view
    @GetMapping("/cs/layout/notice/view/{id}")
    public String viewNotice(
            @PathVariable Long id, Model model) {

        model.addAttribute("group", "notice");
        model.addAttribute("cate", "view");

        articleService.incrementNoticeViews(id);

        NoticeDTO notice = articleService.getNoticeById(id);
        System.out.println("조회수 확인: " + notice.getViews());
        model.addAttribute("notice", notice);
        return "cs/layout/cs_layout";
    }



    // 자주보는질문 list
    @GetMapping("/cs/layout/faq/list")
    public String listFaqs(
            @RequestParam(required = false, defaultValue = "회원") String type1,
            Model model) {
        log.info("Requested FAQ category: {}", type1); // 요청된 type1 값 로그 출력
        Map<String, List<FaqDTO>> groupedFaqs = articleService.getFaqsGroupedByType2(type1);

        // 반환된 groupedFaqs의 데이터 확인
        groupedFaqs.forEach((key, value) -> log.info("Category: {}, FAQ count: {}", key, value.size()));

        model.addAttribute("type1", type1);
        model.addAttribute("group", "faq");
        model.addAttribute("cate", "list");
        model.addAttribute("faqs", groupedFaqs);
        return "cs/layout/cs_layout";
    }

    // 자주묻는질문 view
    @GetMapping("/cs/layout/faq/view/{id}")
    public String viewFaq(@PathVariable Long id, Model model) {

        model.addAttribute("group", "faq");
        model.addAttribute("cate", "view");

        FaqDTO faq = articleService.getFaqById(id);
        model.addAttribute("faq", faq);
        return "cs/layout/cs_layout";
    }

}

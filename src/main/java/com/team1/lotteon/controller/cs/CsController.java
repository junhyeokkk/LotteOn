package com.team1.lotteon.controller.cs;

import com.team1.lotteon.dto.PageResponseDTO;
import com.team1.lotteon.dto.cs.FaqDTO;
import com.team1.lotteon.dto.cs.InquiryDTO;
import com.team1.lotteon.dto.cs.NoticeDTO;
import com.team1.lotteon.service.article.ArticleServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/*
*   날짜 : 2024/10/23
*   이름 : 김소희
*   내용 : CsController 생성
*
*   수정이력
*  -2024/10/29 김소희 - 카테고리 추가
*/

@Log4j2
@RestController
@RequestMapping
@RequiredArgsConstructor
public class CsController {
    private final ArticleServiceImpl articleService;

//  Inquiry 문의사항
//    글쓰기
    @PostMapping("/api/cs/qna/write")
    public ResponseEntity<InquiryDTO> createInquiry(@RequestBody InquiryDTO inquiryDTO) {
        InquiryDTO createInquiry = articleService.createInquiry(inquiryDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createInquiry);
    }
//    글목록
    @GetMapping("/api/cs/qna/list")
    public ResponseEntity<PageResponseDTO<InquiryDTO>> getAllInquiries(@PageableDefault Pageable pageable ) {
        PageResponseDTO<InquiryDTO> inquiries = articleService.getAllInquiries(pageable);
        return ResponseEntity.ok(inquiries);
    }
//    글보기
    @GetMapping("/api/cs/qna/view/{id}")
    public ResponseEntity<InquiryDTO> getInquiryById(@PathVariable Long id , Model model) {
        InquiryDTO inquiry = articleService.getInquiryById(id);
        model.addAttribute("test", inquiry);
        log.info("test1"+inquiry);
        return ResponseEntity.ok(inquiry);
    }
//    글삭제
    @DeleteMapping("/api/cs/qna/{id}")
    public ResponseEntity<Void> deleteInquiry(@PathVariable Long id) {
        articleService.deleteInquiry(id);
        return ResponseEntity.noContent().build();
    }
    //    QNA 카테고리
    @GetMapping("/api/cs/qna/list/{type1}/{type2}")
    public ResponseEntity<PageResponseDTO<InquiryDTO>> getInquiryByType(
            @PathVariable String type1,
            @PathVariable String type2,
            @PageableDefault Pageable pageable) {
        PageResponseDTO<InquiryDTO> inquirysByType = articleService.findQnaByType(type1, type2, pageable);
        return ResponseEntity.ok(inquirysByType);
    }


//    Notice 공지사항
//    글쓰기
    @PostMapping("/api/cs/notice/write")
    public ResponseEntity<NoticeDTO> createNotice(@RequestBody NoticeDTO noticeDTO) {
        NoticeDTO createNotice = articleService.createNotice(noticeDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createNotice);
    }

    //    글목록
    @GetMapping("/api/cs/notice/list")
    public ResponseEntity<PageResponseDTO<NoticeDTO>> getAllNotices(@RequestParam String type1, @PageableDefault Pageable pageable) {
        PageResponseDTO<NoticeDTO> notices;
        if (type1.equals("전체")){
            notices = articleService.getAllNotices(pageable);
        }else {
            notices = articleService.getNoticesByType1(type1, pageable);
        }

        return ResponseEntity.ok(notices);
    }
//    글보기
    @GetMapping("/api/cs/notice/view/{id}")
    public ResponseEntity<NoticeDTO> getNoticeById(@PathVariable Long id) {
        NoticeDTO notices = articleService.getNoticeById(id);
        return ResponseEntity.ok(notices);
    }
//    Notice 카테고리
    @GetMapping("/api/cs/notice/list/{type1}")
    public ResponseEntity<PageResponseDTO<NoticeDTO>> getNoticesByType(@PathVariable String type1, @PageableDefault Pageable pageable) {
        PageResponseDTO<NoticeDTO> noticesByType = articleService.findNoticeByType1(type1, pageable); // Pageable 추가
        return ResponseEntity.ok(noticesByType);
    }


//    Faq 자주묻는질문
//    글쓰기
    @PostMapping("/api/cs/faq/write")
    public ResponseEntity<FaqDTO> createFaq(@RequestBody FaqDTO faqDTO) {
        FaqDTO createFaq = articleService.createFaq(faqDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createFaq);
    }
//    글목록
    @GetMapping("/api/cs/faq/list")
    public ResponseEntity<List<FaqDTO>> getAllFaqs(@PageableDefault Pageable pageable) {
        List<FaqDTO> faqs = articleService.getAllFaqs();
        return ResponseEntity.ok(faqs);
    }
//    글보기
    @GetMapping("/api/cs/faq/view/{id}")
    public ResponseEntity<FaqDTO> getFaqById(@PathVariable Long id) {
        FaqDTO faqs = articleService.getFaqById(id);
        return ResponseEntity.ok(faqs);
    }
//    FAQ 카테고리
    @GetMapping("/api/cs/faq/list/{type1}/{type2}")
    public ResponseEntity<PageResponseDTO<FaqDTO>> getFaqsByType(
            @PathVariable String type1,
            @PathVariable String type2,
            @PageableDefault Pageable pageable) {
        PageResponseDTO<FaqDTO> faqsByType = articleService.findFaqByType(type1, type2, pageable);
        return ResponseEntity.ok(faqsByType);
    }



}

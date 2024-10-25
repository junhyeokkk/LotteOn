package com.team1.lotteon.controller.cs;

import com.team1.lotteon.dto.PageResponseDTO;
import com.team1.lotteon.dto.cs.FaqDTO;
import com.team1.lotteon.dto.cs.InquiryDTO;
import com.team1.lotteon.dto.cs.NoticeDTO;
import com.team1.lotteon.service.article.ArticleServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<InquiryDTO> getInquiryById(@PathVariable Long id) {
        InquiryDTO inquiry = articleService.getInquiryById(id);
        return ResponseEntity.ok(inquiry);
    }
//    글삭제
    @DeleteMapping("/api/cs/qna/{id}")
    public ResponseEntity<Void> deleteInquiry(@PathVariable Long id) {
        articleService.deleteInquiry(id);
        return ResponseEntity.noContent().build();
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
    public ResponseEntity<PageResponseDTO<NoticeDTO>> getAllNotices(@PageableDefault Pageable pageable) {
        PageResponseDTO<NoticeDTO> notices = articleService.getAllNotices(pageable);
        return ResponseEntity.ok(notices);
    }
//    글보기
    @GetMapping("/api/cs/notice/view/{id}")
    public ResponseEntity<NoticeDTO> getNoticeById(@PathVariable Long id) {
        NoticeDTO notices = articleService.getNoticeById(id);
        return ResponseEntity.ok(notices);
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

}

package com.team1.lotteon.controller.admin.cs;

/*
 *   날짜 : 2024/10/29
 *   이름 : 김소희
 *   내용 : AdminCsController 생성
 *
 */

import com.team1.lotteon.dto.PageResponseDTO;
import com.team1.lotteon.dto.cs.FaqDTO;
import com.team1.lotteon.dto.cs.InquiryDTO;
import com.team1.lotteon.dto.cs.NoticeDTO;
import com.team1.lotteon.entity.FAQ;
import com.team1.lotteon.service.article.ArticleServiceImpl;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Log4j2
@Controller
public class AdminCsController {
    private final ArticleServiceImpl articleService;

    public AdminCsController(ArticleServiceImpl articleService) {
        this.articleService = articleService;
    }

    // Notice 공지사항
    // list
    @GetMapping("/api/admin/cs/notice/list")
    public ResponseEntity<PageResponseDTO<NoticeDTO>> getAllNotices(@PageableDefault Pageable pageable) {
        PageResponseDTO<NoticeDTO> notices = articleService.getAllNotices(pageable);
        return ResponseEntity.ok(notices);
    }
    // view
    @GetMapping("/api/admin/notice/view/{id}")
    public ResponseEntity<NoticeDTO> getNoticeById(@PathVariable Long id) {
        NoticeDTO notices = articleService.getNoticeById(id);
        return ResponseEntity.ok(notices);
    }
    // write
    @PostMapping("/api/admin/notice/write")
    public ResponseEntity<NoticeDTO> createNotice(@RequestBody NoticeDTO noticeDTO) {
        NoticeDTO createNotice = articleService.createNotice(noticeDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createNotice);
    }

    // Faq 자주 묻는 질문
    // list
//    @GetMapping("/api/admin/faq/list")
//    public ResponseEntity<List<FaqDTO>> getAllFaqs(@PageableDefault Pageable pageable) {
//        List<FaqDTO> faqs = articleService.getAllFaqs();
//        return ResponseEntity.ok(faqs);
//    }

    @GetMapping("/api/admin/faq/list")
    public ResponseEntity<List<FaqDTO>> findTop10ByOrderByCreatedAtDesc() {
        List<FaqDTO> faqs = articleService.findTop10ByOrderByCreatedAtDesc();
        return ResponseEntity.ok(faqs);
    }

    // view
    @GetMapping("/api/admin/faq/view/{id}")
    public ResponseEntity<FaqDTO> getFaqById(@PageableDefault Long id) {
        FaqDTO faqs = articleService.getFaqById(id);
        return ResponseEntity.ok(faqs);
    }

    // Inquiry 문의사항
    // list
    @GetMapping("/api/admin/cs/qna/list")
    public ResponseEntity<PageResponseDTO<InquiryDTO>> getAllInquiries(@PageableDefault Pageable pageable ) {
        PageResponseDTO<InquiryDTO> inquiries = articleService.getAllInquiries(pageable);
        return ResponseEntity.ok(inquiries);
    }
    // view
    @GetMapping("/api/admin/qna/view/{id}")
    public ResponseEntity<InquiryDTO> getInquiryById(@PageableDefault Long id) {
        InquiryDTO inquirys = articleService.getInquiryById(id);
        return ResponseEntity.ok(inquirys);
    }


}

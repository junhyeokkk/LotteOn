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
import com.team1.lotteon.service.article.ArticleServiceImpl;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public ResponseEntity<PageResponseDTO<NoticeDTO>> getAllNotices(
            @RequestParam(required = false, defaultValue = "all") String type,
            @PageableDefault Pageable pageable) {

        log.info("Received type parameter: {}", type);  // 확인용 로그
        PageResponseDTO<NoticeDTO> notices;
        if ("all".equals(type)) {
            notices = articleService.getAllNotices(pageable);
        } else {
            notices = articleService.getNoticesByType1(type, pageable); // 유형별 공지사항 조회 메서드 추가 필요
        }
        log.info("Filtered notices: {}", notices);  // 필터된 데이터 확인
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
    // delete
    // 선택 삭제
    @PostMapping("/admin/cs/notice/deleteSelected")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> deleteSelectedNotices(@RequestBody List<Long> ids) {
        Map<String, Object> response = new HashMap<>();
        try {
            articleService.deleteNotice(ids); // 여러 ID 삭제
            response.put("success", true);
            response.put("message", "선택된 공지사항이 삭제되었습니다.");
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "삭제 중 오류가 발생했습니다.");
        }
        return ResponseEntity.ok(response);
    }
    // 삭제 버튼
    @DeleteMapping("/api/admin/notice/delete/{id}")
    public ResponseEntity<?> deleteNotice(@PathVariable Long id) {
        try {
            articleService.deleteNotice(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("삭제 실패");
        }
    }


    // Faq 자주 묻는 질문
    // list
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
    // write
    @PostMapping("/api/admin/faq/write")
    public ResponseEntity<FaqDTO> createFaq(@RequestBody FaqDTO faqDTO) {
        FaqDTO createFaq = articleService.createFaq(faqDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createFaq);
    }
    // delete
    // 선택 삭제
    @PostMapping("/admin/cs/faq/deleteSelected")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> deleteSelectedFaqs(@RequestBody List<Long> ids) {
        Map<String, Object> response = new HashMap<>();
        try {
            articleService.deleteFaq(ids); // 여러 ID 삭제
            response.put("success", true);
            response.put("message", "선택된 공지사항이 삭제되었습니다.");
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "삭제 중 오류가 발생했습니다.");
        }
        return ResponseEntity.ok(response);
    }
    // 삭제 버튼
    @DeleteMapping("/api/admin/faq/delete/{id}")
    public ResponseEntity<?> deleteFaq(@PathVariable Long id) {
        try {
            articleService.deleteFaq(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("삭제 실패");
        }
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
    // delete
    // 선택 삭제
    @PostMapping("/admin/cs/qna/deleteSelected")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> deleteSelectedInquirys(@RequestBody List<Long> ids) {
        Map<String, Object> response = new HashMap<>();
        try {
            articleService.deleteInquiry(ids); // 여러 ID 삭제
            response.put("success", true);
            response.put("message", "선택된 공지사항이 삭제되었습니다.");
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "삭제 중 오류가 발생했습니다.");
        }
        return ResponseEntity.ok(response);
    }
    // 삭제 버튼
    @DeleteMapping("/api/admin/qna/delete/{id}")
    public ResponseEntity<?> deleteInquiry(@PathVariable Long id) {
        try {
            articleService.deleteInquiry(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("삭제 실패");
        }
    }

}

package com.team1.lotteon.controller.cs;

import com.team1.lotteon.dto.PageResponseDTO;
import com.team1.lotteon.dto.cs.InquiryDTO;
import com.team1.lotteon.service.article.ArticleServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

//

}

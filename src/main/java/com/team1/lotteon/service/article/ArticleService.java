package com.team1.lotteon.service.article;

import com.team1.lotteon.dto.PageResponseDTO;
import com.team1.lotteon.dto.cs.ArticleDTO;
import com.team1.lotteon.dto.cs.FaqDTO;
import com.team1.lotteon.dto.cs.InquiryDTO;
import com.team1.lotteon.dto.cs.NoticeDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ArticleService {

    // Article
    ArticleDTO createArticle(ArticleDTO articleDTO);
    ArticleDTO getArticleById(Long id);
    List<ArticleDTO> getAllArticles();
    ArticleDTO updateArticle(Long id, ArticleDTO articleDTO);
    void deleteArticle(Long id);

    // Faq
    FaqDTO createFaq(FaqDTO faqDTO);
    FaqDTO getFaqById(Long id);
    List<FaqDTO> getAllFaqs();
    FaqDTO updateFaq(Long id, FaqDTO faqDTO);
    void deleteFaq(Long id);

    // Inquiry
    InquiryDTO createInquiry(InquiryDTO inquiryDTO);
    InquiryDTO getInquiryById(Long id);
    PageResponseDTO<InquiryDTO> getAllInquiries(Pageable pageable);
    InquiryDTO updateInquiry(Long id, InquiryDTO inquiryDTO);
    void deleteInquiry(Long id);

    // Notice
    NoticeDTO createNotice(NoticeDTO noticeDTO);
    NoticeDTO getNoticeById(Long id);
    PageResponseDTO<NoticeDTO> getAllNotices(Pageable pageable);
    NoticeDTO updateNotice(Long id, NoticeDTO noticeDTO);
    void deleteNotice(Long id);

}

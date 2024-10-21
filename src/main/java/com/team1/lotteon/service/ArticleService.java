package com.team1.lotteon.service;

import com.team1.lotteon.dto.ArticleDTO;
import com.team1.lotteon.dto.FaqDTO;
import com.team1.lotteon.dto.InquiryDTO;
import com.team1.lotteon.dto.NoticeDTO;

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
    List<InquiryDTO> getAllInquiries();
    InquiryDTO updateInquiry(Long id, InquiryDTO inquiryDTO);
    void deleteInquiry(Long id);

    // Notice 관련 메서드 -> ArticleDTO 사용
    NoticeDTO createNotice(NoticeDTO noticeDTO);
    NoticeDTO getNoticeById(Long id);
    List<NoticeDTO> getAllNotices();
    NoticeDTO updateNotice(Long id, NoticeDTO noticeDTO);
    void deleteNotice(Long id);

}

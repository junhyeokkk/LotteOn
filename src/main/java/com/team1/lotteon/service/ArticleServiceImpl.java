package com.team1.lotteon.service;

import com.team1.lotteon.dto.ArticleDTO;
import com.team1.lotteon.dto.FaqDTO;
import com.team1.lotteon.dto.InquiryDTO;
import com.team1.lotteon.dto.NoticeDTO;
import com.team1.lotteon.entity.FAQ;
import com.team1.lotteon.entity.Inquiry;
import com.team1.lotteon.entity.Notice;
import com.team1.lotteon.repository.FaqRepository;
import com.team1.lotteon.repository.InquiryRepository;
import com.team1.lotteon.repository.NoticeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private FaqRepository faqRepository;
    @Autowired
    private InquiryRepository inquiryRepository;
    @Autowired
    private NoticeRepository noticeRepository;


//  Article
    @Override
    public ArticleDTO createArticle(ArticleDTO articleDTO) {
        return null;
    }

    @Override
    public ArticleDTO getArticleById(Long id) {
        return null;
    }

    @Override
    public List<ArticleDTO> getAllArticles() {
        return List.of();
    }

    @Override
    public ArticleDTO updateArticle(Long id, ArticleDTO articleDTO) {
        return null;
    }

    @Override
    public void deleteArticle(Long id) {

    }


//  Faq
    @Override
    public FaqDTO createFaq(FaqDTO faqDTO) {
        return null;
    }

    @Override
    public FaqDTO getFaqById(Long id) {
        return null;
    }

    @Override
    public List<FaqDTO> getAllFaqs() {
        return List.of();
    }

    @Override
    public FaqDTO updateFaq(Long id, FaqDTO faqDTO) {
        return null;
    }

    @Override
    public void deleteFaq(Long id) {

    }


//  Inquiry
    @Override
    public InquiryDTO createInquiry(InquiryDTO inquiryDTO) {
        return null;
    }

    @Override
    public InquiryDTO getInquiryById(Long id) {
        return null;
    }

    @Override
    public List<InquiryDTO> getAllInquiries() {
        return List.of();
    }

    @Override
    public InquiryDTO updateInquiry(Long id, InquiryDTO inquiryDTO) {
        return null;
    }

    @Override
    public void deleteInquiry(Long id) {

    }


//  Notice
    @Override
    public NoticeDTO createNotice(NoticeDTO noticeDTO) {
        return null;
    }

    @Override
    public NoticeDTO getNoticeById(Long id) {
        return null;
    }

    @Override
    public List<NoticeDTO> getAllNotices() {
        return List.of();
    }

    @Override
    public NoticeDTO updateNotice(Long id, NoticeDTO noticeDTO) {
        return null;
    }

    @Override
    public void deleteNotice(Long id) {

    }

//  DTO Entity 변환

// Faq
    private FaqDTO convertToFaqDTO(FAQ faq) {
        FaqDTO faqDTO = new FaqDTO();
        faqDTO.setId(faq.getId());
        faqDTO.setTitle(faq.getTitle());
        faqDTO.setContent(faq.getContent());
        return faqDTO;
    }

    private FAQ convertToFaqEntity(FaqDTO faqDTO) {
        FAQ faq = new FAQ();
        return faq;
    }
}
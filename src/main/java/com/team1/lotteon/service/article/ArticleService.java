package com.team1.lotteon.service.article;

import com.team1.lotteon.dto.PageResponseDTO;
import com.team1.lotteon.dto.cs.ArticleDTO;
import com.team1.lotteon.dto.cs.FaqDTO;
import com.team1.lotteon.dto.cs.InquiryDTO;
import com.team1.lotteon.dto.cs.NoticeDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

/*
 *   날짜 : 2024/10/17
 *   이름 : 이상훈
 *   내용 : ArticleService 생성
 *
 *   수정이력
 *   2024/10/25 김소희 - ArticleServiceImpl 추가를 위해 interface로 수정
 *   2024/10/29 김소희 - PageResponseDTO<> findFaqByType 추가
 */

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
    void deleteFaq(List<Long> ids);
    PageResponseDTO<FaqDTO> findFaqByType1(String type1, Pageable pageable);
    PageResponseDTO<FaqDTO> findFaqByType2(String type2, Pageable pageable);
    PageResponseDTO<FaqDTO> findFaqByType(String type1, String type2, Pageable pageable);
    List<FaqDTO> findTop10ByOrderByCreatedAtDesc();
    Map<String, List<FaqDTO>> getFaqsGroupedByType2(String type1);
    public List<FaqDTO> getFaqsSortedByViewsAndType1();
    PageResponseDTO<FaqDTO> getAllFaqs(Pageable pageable);
    void incrementFaqViews(Long id);


    // Inquiry
    InquiryDTO createInquiry(InquiryDTO inquiryDTO);
    InquiryDTO getInquiryById(Long id);
    PageResponseDTO<InquiryDTO> getAllInquiries(Pageable pageable);
    InquiryDTO updateInquiry(Long id, InquiryDTO inquiryDTO);
    void deleteInquiry(Long id);
    void deleteInquiry(List<Long> ids);
    PageResponseDTO<InquiryDTO> findQnaByType1(String type1, Pageable pageable);
    PageResponseDTO<InquiryDTO> findQnaByType(String type1, String type2, Pageable pageable);
    Map<String, List<InquiryDTO>> getQnasGroupedByType2(String type1);
    PageResponseDTO<InquiryDTO> getQnasByType1(String type1, Pageable pageable);
    void updateInquiryAnswer(Long id, String answer);
    PageResponseDTO<InquiryDTO> getInquiriesByMemberId(String memberId, Pageable pageable);
    int countInquiriesByMemberId(String uid);



    // Notice
    NoticeDTO createNotice(NoticeDTO noticeDTO);
    NoticeDTO getNoticeById(Long id);
    PageResponseDTO<NoticeDTO> getAllNotices(Pageable pageable);
    NoticeDTO updateNotice(Long id, NoticeDTO noticeDTO);
    void deleteNotice(Long id);
    void deleteNotice(List<Long> ids);
    PageResponseDTO<NoticeDTO> findNoticeByType1(String type1, Pageable pageable);
    PageResponseDTO<NoticeDTO> getNoticesByType1(String type1, Pageable pageable);
    void incrementNoticeViews(Long id);


}

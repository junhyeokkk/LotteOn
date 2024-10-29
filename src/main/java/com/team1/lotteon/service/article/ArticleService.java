package com.team1.lotteon.service.article;

import com.team1.lotteon.dto.PageResponseDTO;
import com.team1.lotteon.dto.cs.ArticleDTO;
import com.team1.lotteon.dto.cs.FaqDTO;
import com.team1.lotteon.dto.cs.InquiryDTO;
import com.team1.lotteon.dto.cs.NoticeDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;

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
    PageResponseDTO<FaqDTO> findFaqByType2(String type2, Pageable pageable);

    // Inquiry
    InquiryDTO createInquiry(InquiryDTO inquiryDTO);
    InquiryDTO getInquiryById(Long id);
    PageResponseDTO<InquiryDTO> getAllInquiries(Pageable pageable);
    InquiryDTO updateInquiry(Long id, InquiryDTO inquiryDTO);
    void deleteInquiry(Long id);
    PageResponseDTO<InquiryDTO> findQnaByType2(String type2, Pageable pageable);

    // Notice
    NoticeDTO createNotice(NoticeDTO noticeDTO);
    NoticeDTO getNoticeById(Long id);
    PageResponseDTO<NoticeDTO> getAllNotices(Pageable pageable);
    NoticeDTO updateNotice(Long id, NoticeDTO noticeDTO);
    void deleteNotice(Long id);
    PageResponseDTO<NoticeDTO> findNoticeByType1(String type1, Pageable pageable);

}

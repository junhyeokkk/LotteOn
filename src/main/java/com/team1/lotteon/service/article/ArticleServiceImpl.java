package com.team1.lotteon.service.article;

import com.team1.lotteon.dto.cs.ArticleDTO;
import com.team1.lotteon.dto.cs.FaqDTO;
import com.team1.lotteon.dto.cs.InquiryDTO;
import com.team1.lotteon.dto.cs.NoticeDTO;
import com.team1.lotteon.entity.*;
import com.team1.lotteon.repository.FaqRepository;
import com.team1.lotteon.repository.InquiryRepository;
import com.team1.lotteon.repository.Memberrepository.MemberRepository;
import com.team1.lotteon.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ArticleServiceImpl implements ArticleService {

    private final FaqRepository faqRepository;
    private final InquiryRepository inquiryRepository;
    private final NoticeRepository noticeRepository;
    private final MemberRepository memberRepository;


    //  Article 미구현
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
        FAQ faq = convertToFaqEntity(faqDTO, null);
        FAQ savedFaq = faqRepository.save(faq);
        return convertToFaqDTO(savedFaq);
    }

    @Override
    public FaqDTO getFaqById(Long id) {
        FAQ faq = faqRepository.findById(id.intValue())
                .orElseThrow(() -> new RuntimeException("Faq not found"));
        return convertToFaqDTO(faq);
    }

    @Override
    public List<FaqDTO> getAllFaqs() {
        List<FAQ> faqs = faqRepository.findAll();
        return faqs.stream()
                .map(this::convertToFaqDTO)
                .collect(Collectors.toList());
    }

    @Override
    public FaqDTO updateFaq(Long id, FaqDTO faqDTO) {
        FAQ existingFaq = faqRepository.findById(id.intValue())
                .orElseThrow(() -> new RuntimeException("Faq not found"));

        Member member= memberRepository.findById(faqDTO.getMemberId())
                .orElseThrow(() -> new RuntimeException("Member not found"));

        FAQ updatedFaq = convertToFaqEntity(faqDTO, member);
        updatedFaq.setId(existingFaq.getId());
        FAQ savedFaq = faqRepository.save(existingFaq);
        return convertToFaqDTO(savedFaq);
    }

    @Override
    public void deleteFaq(Long id) {
        faqRepository.deleteById(id.intValue());
    }


    //  Inquiry
    @Override
    public InquiryDTO createInquiry(InquiryDTO inquiryDTO) {
        Inquiry inquiry = convertToInquiryEntity(inquiryDTO, null);
        Inquiry savedInquiry = inquiryRepository.save(inquiry);
        return convertToInquiryDTO(savedInquiry);
    }

    @Override
    public InquiryDTO getInquiryById(Long id) {
        Inquiry inquiry = inquiryRepository.findById(id.intValue())
                .orElseThrow(() -> new RuntimeException("Inquiry not found"));
        return convertToInquiryDTO(inquiry);
    }

    @Override
    public List<InquiryDTO> getAllInquiries() {
        List<Inquiry> inquiries = inquiryRepository.findAll();
        return inquiries.stream()
                .map(this::convertToInquiryDTO)
                .collect(Collectors.toList());
    }

    @Override
    public InquiryDTO updateInquiry(Long id, InquiryDTO inquiryDTO) {
        Inquiry existingInquiry = inquiryRepository.findById(id.intValue())
                .orElseThrow(() -> new RuntimeException("Inquiry not found"));

        Member member = memberRepository.findById(inquiryDTO.getMemberId())
                .orElseThrow(() -> new RuntimeException("Member not found"));

        Inquiry updatedInquiry = convertToInquiryEntity(inquiryDTO, member);
        updatedInquiry.setId(existingInquiry.getId());
        Inquiry savedInquiry = inquiryRepository.save(existingInquiry);
        return convertToInquiryDTO(savedInquiry);
    }

    @Override
    public void deleteInquiry(Long id) {
        inquiryRepository.deleteById(id.intValue());
    }


    //  Notice
    @Override
    public NoticeDTO createNotice(NoticeDTO noticeDTO) {
        Notice notice = convertToNoticeEntity(noticeDTO, null);
        Notice savedNotice = noticeRepository.save(notice);
        return convertToNoticeDTO(savedNotice);
    }

    @Override
    public NoticeDTO getNoticeById(Long id) {
        Notice notice = noticeRepository.findById(id.intValue())
                .orElseThrow(() -> new RuntimeException("Notice not found"));
        return convertToNoticeDTO(notice);
    }

    @Override
    public List<NoticeDTO> getAllNotices() {
        List<Notice> notices = noticeRepository.findAll();
        return notices.stream()
                .map(this::convertToNoticeDTO)
                .collect(Collectors.toList());
    }

    @Override
    public NoticeDTO updateNotice(Long id, NoticeDTO noticeDTO) {
        Notice existingNotice = noticeRepository.findById(id.intValue())
                .orElseThrow(() -> new RuntimeException("Notice not found"));

        Member member = memberRepository.findById(noticeDTO.getMemberId())
                .orElseThrow(() -> new RuntimeException("Member not found"));

        Notice updatedNotice = convertToNoticeEntity(noticeDTO, member);
        updatedNotice.setId(existingNotice.getId());
        Notice savedNotice = noticeRepository.save(existingNotice);
        return convertToNoticeDTO(savedNotice);
    }

    @Override
    public void deleteNotice(Long id) {
        noticeRepository.deleteById(id.intValue());
    }

//  DTO Entity 변환

    // Faq 엔티티 - FaqDTO 변환
    private FaqDTO convertToFaqDTO(FAQ faq) {
        return FaqDTO.builder()
                .id(faq.getId())
                .title(faq.getTitle())
                .content(faq.getContent())
                .type2(faq.getType2())
                .createdAt(faq.getCreatedAt())
                .updatedAt(faq.getUpdatedAt())
                .memberId(faq.getMember() !=null ? faq.getMember().getUid() : null)
                .build();

    }
    private FAQ convertToFaqEntity(FaqDTO faqDTO, Member member) {
        return FAQ.builder()
                .id(faqDTO.getId())
                .title(faqDTO.getTitle())
                .content(faqDTO.getContent())
                .type2(faqDTO.getType2())
                .createdAt(faqDTO.getCreatedAt())
                .updatedAt(faqDTO.getUpdatedAt())
                .member(member)
                .build();
    }

    //  Inquiry 엔티티 - DTO
    private InquiryDTO convertToInquiryDTO(Inquiry inquiry) {
        return InquiryDTO.builder()
                .id(inquiry.getId())
                .title(inquiry.getTitle())
                .content(inquiry.getContent())
                .type2(inquiry.getType2())
                .createdAt(inquiry.getCreatedAt())
                .updatedAt(inquiry.getUpdatedAt())
                .memberId(inquiry.getMember() !=null ? inquiry.getMember().getUid() : null)
                .build();

    }

    private Inquiry convertToInquiryEntity(InquiryDTO inquiryDTO, Member member) {
        return Inquiry.builder()
                .id(inquiryDTO.getId())
                .title(inquiryDTO.getTitle())
                .content(inquiryDTO.getContent())
                .type2(inquiryDTO.getType2())
                .createdAt(inquiryDTO.getCreatedAt())
                .updatedAt(inquiryDTO.getUpdatedAt())
                .member(member)
                .build();
    }

    //    Notice
    private NoticeDTO convertToNoticeDTO(Notice notice) {
        return NoticeDTO.builder()
                .id(notice.getId())
                .title(notice.getTitle())
                .content(notice.getContent())
                .createdAt(notice.getCreatedAt())
                .updatedAt(notice.getUpdatedAt())
                .memberId(notice.getMember().getUid() !=null ? notice.getMember().getUid() : null)
                .build();
    }

    private Notice convertToNoticeEntity(NoticeDTO noticeDTO, Member member) {
        return Notice.builder()
                .id(noticeDTO.getId())
                .title(noticeDTO.getTitle())
                .content(noticeDTO.getContent())
                .createdAt(noticeDTO.getCreatedAt())
                .updatedAt(noticeDTO.getUpdatedAt())
                .member(member)
                .build();
    }

}
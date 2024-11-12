package com.team1.lotteon.service.article;

import com.team1.lotteon.dto.PageResponseDTO;
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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/*
 *   날짜 : 2024/10/22
 *   이름 : 김소희
 *   내용 : ArticleServiceImpl 생성
 *
 *  수정이력
 *  - 2024/10/29 김소희 - PageResponseDTO<> findFaqByType 추가
 */

@RequiredArgsConstructor
@Service
public class ArticleServiceImpl implements ArticleService {

    private static final Logger log = LogManager.getLogger(ArticleServiceImpl.class);
    private final FaqRepository faqRepository;
    private final InquiryRepository inquiryRepository;
    private final NoticeRepository noticeRepository;
    private final MemberRepository memberRepository;


//    memberId로 Member 엔티티 조회
    private Member findMemberById(String memberId) {
        if (memberId == null || memberId.isEmpty()) {
            return null;
        }
        return memberRepository.findByUid(memberId)
                .orElseThrow(() -> new RuntimeException("Member not found with memberId: " + memberId));
    }

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



    //  Faq 자주묻는질문
    @Override
    public FaqDTO createFaq(FaqDTO faqDTO) {
        FAQ faq = convertToFaqEntity(faqDTO, findMemberById(faqDTO.getMemberId()));
        return convertToFaqDTO(faqRepository.save(faq));
    }
    @Override
    public FaqDTO getFaqById(Long id) {
        return convertToFaqDTO(faqRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Faq not found")));
    }
    @Override
    public List<FaqDTO> getAllFaqs() {
        return faqRepository.findAllByOrderByCreatedAtDesc().stream()
                .map(this::convertToFaqDTO)
                .collect(Collectors.toList());
    }
    public List<FaqDTO> getFaqsSortedByViewsAndType1() {
        List<String> types = Arrays.asList("회원", "쿠폰/이벤트", "주문/결제",
                "배송", "취소/반품/교환", "여행/숙박/항공", "안전거래");

        return faqRepository.findByType1InOrderByViewsDesc(types).stream()
                .collect(Collectors.toMap(
                        FAQ::getType1,
                        this::convertToFaqDTO,
                        (existing, replacement) -> existing // 중복 시 기존 항목 유지
                ))
                .values()
                .stream()
                .toList();
    }
    @Override
    public FaqDTO updateFaq(Long id, FaqDTO faqDTO) {
        FAQ faq = faqRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Faq not found"));
        faq.setTitle(faqDTO.getTitle());
        faq.setContent(faqDTO.getContent());
        faq.setType1(faqDTO.getType1());
        faq.setType2(faqDTO.getType2());
        faq.setMember(findMemberById(faqDTO.getMemberId()));
        return convertToFaqDTO(faqRepository.save(faq));
    }
    // 삭제버튼
    @Override
    public void deleteFaq(Long id) {
        if (!faqRepository.existsById(id)) {
            throw new IllegalArgumentException("해당 자주 묻는 질문은 없습니다.");
        }
        faqRepository.deleteById(id);
    }
    // 선택삭제
    @Override
    public void deleteFaq(List<Long> ids) {
        faqRepository.deleteAllById(ids);
    }
    @Override
    public PageResponseDTO<FaqDTO> findFaqByType1(String type1, Pageable pageable) {
        log.info("Filtering FAQs with type1: {}", type1);
        Page<FAQ> faqsPage = faqRepository.findByType1(type1, pageable);
        return PageResponseDTO.fromPage(faqsPage.map(this::convertToFaqDTO));
    }

    @Override
    public PageResponseDTO<FaqDTO> findFaqByType2(String type2, Pageable pageable) {
        return PageResponseDTO.fromPage(
                faqRepository.findByType2(type2, pageable)
                        .map(this::convertToFaqDTO)
        );
    }

    @Override
    public List<FaqDTO> findTop10ByOrderByCreatedAtDesc() {
        AtomicInteger count = new AtomicInteger();
        return faqRepository.findTop10ByOrderByCreatedAtDesc().stream()
                .map(faq -> {
                    FaqDTO dto = convertToFaqDTO(faq);
                    dto.setDisplayNumber(count.incrementAndGet());
                    return dto;
                }).collect(Collectors.toList());
    }

    @Override
    public Map<String, List<FaqDTO>> getFaqsGroupedByType2(String type1) {
        List<FAQ> faqs = faqRepository.findByType1OrderByType2AndCreatedAt(type1);

        // type2별로 그룹화하고, 각 그룹에서 최대 10개의 기사만 남김
        Map<String, List<FaqDTO>> groupedFaqs = faqs.stream()
                .collect(Collectors.groupingBy(
                        FAQ::getType2,
                        Collectors.collectingAndThen(
                                Collectors.toList(),
                                list -> list.stream()
                                        .limit(10)
                                        .map(this::convertToFaqDTO)
                                        .collect(Collectors.toList())
                        )
                ));

        return groupedFaqs;
    }
    @Override
    public PageResponseDTO<FaqDTO> getAllFaqs(Pageable pageable) {
        Page<FAQ> faqsPage = faqRepository.findAll(pageable);
        AtomicInteger displayCount = new AtomicInteger(faqsPage.getNumber() * pageable.getPageSize() + 1);

        // Convert Page<FAQ> to List<FaqDTO> with displayNumber
        List<FaqDTO> faqDTOList = faqsPage.map(this::convertToFaqDTO)
                .map(dto -> {
                    dto.setDisplayNumber(displayCount.getAndIncrement());
                    return dto;
                })
                .getContent();

        // Use existing fromList method to wrap List<FaqDTO> into PageResponseDTO
        return PageResponseDTO.fromList(faqDTOList, pageable);
    }

    @Override
    public PageResponseDTO<FaqDTO> findFaqByType(String type1, String type2, Pageable pageable) {
        Page<FAQ> faqsPage = faqRepository.findByType1AndType2(type1, type2, pageable);
        AtomicInteger displayCount = new AtomicInteger(faqsPage.getNumber() * pageable.getPageSize() + 1);

        // Convert Page<FAQ> to List<FaqDTO> with displayNumber
        List<FaqDTO> faqDTOList = faqsPage.map(this::convertToFaqDTO)
                .map(dto -> {
                    dto.setDisplayNumber(displayCount.getAndIncrement());
                    return dto;
                })
                .getContent();

        return PageResponseDTO.fromList(faqDTOList, pageable);
    }

    @Override
    public void incrementFaqViews(Long id) {
        FAQ faq = faqRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("FAQ not found"));
        faq.setViews(faq.getViews() + 1); // 조회수 증가
        faqRepository.save(faq); // 변경사항 저장
    }





    //  Inquiry 문의하기
    @Override
    public InquiryDTO createInquiry(InquiryDTO inquiryDTO) {
        Inquiry inquiry = convertToInquiryEntity(inquiryDTO, findMemberById(inquiryDTO.getMemberId()));
        return convertToInquiryDTO(inquiryRepository.save(inquiry));
    }
    @Override
    public InquiryDTO getInquiryById(Long id) {
        return convertToInquiryDTO(inquiryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Inquiry not found")));
    }
    @Override
    public PageResponseDTO<InquiryDTO> getAllInquiries(Pageable pageable) {
        return PageResponseDTO.fromPage(
                inquiryRepository.findAll(pageable)
                        .map(this::convertToInquiryDTO)
        );
    }
    @Override
    public InquiryDTO updateInquiry(Long id, InquiryDTO inquiryDTO) {
        Inquiry inquiry = inquiryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Inquiry not found"));
        inquiry.setTitle(inquiryDTO.getTitle());
        inquiry.setContent(inquiryDTO.getContent());
        inquiry.setType1(inquiryDTO.getType1());
        inquiry.setType2(inquiryDTO.getType2());
        inquiry.setAnswer(inquiryDTO.getAnswer());
        inquiry.setMember(findMemberById(inquiryDTO.getMemberId()));
        return convertToInquiryDTO(inquiryRepository.save(inquiry));
    }
    // 삭제버튼
    @Override
    public void deleteInquiry(Long id) {
        if (!inquiryRepository.existsById(id)) {
            throw new IllegalArgumentException("해당 문의하기는 없습니다.");
        }
        inquiryRepository.deleteById(id);
    }
    // 선택삭제
    @Override
    public void deleteInquiry(List<Long> ids) {
        inquiryRepository.deleteAllById(ids);
    }
    @Override
    public PageResponseDTO<InquiryDTO> findQnaByType1(String type1, Pageable pageable) {
        return PageResponseDTO.fromPage(
                inquiryRepository.findByType1(type1, pageable)
                        .map(this::convertToInquiryDTO)
        );
    }
    @Override
    public PageResponseDTO<InquiryDTO> findQnaByType(String type1, String type2, Pageable pageable) {
        log.info("Filtering QNAs with type1: {} and type2: {}", type1, type2);
        Page<Inquiry> QnasPage = inquiryRepository.findByType1AndType2(type1, type2, pageable);
        return PageResponseDTO.fromPage(QnasPage.map(this::convertToInquiryDTO));
    }
    @Override
    public Map<String, List<InquiryDTO>> getQnasGroupedByType2(String type1) {
        // type1에 따라 type2별 그룹화
        List<Inquiry> qnas = inquiryRepository.findByType1OrderByType2AndCreatedAt(type1);

        Map<String, List<InquiryDTO>> groupedQnas = qnas.stream()
                .collect(Collectors.groupingBy(
                        Inquiry::getType2,
                        Collectors.collectingAndThen(
                                Collectors.toList(),
                                list -> list.stream()
                                        .limit(10)
                                        .map(this::convertToInquiryDTO)
                                        .collect(Collectors.toList())
                        )
                ));

            return groupedQnas;
        }
    // 문의사항을 type1으로 페이지네이션하여 조회
    @Override
    public PageResponseDTO<InquiryDTO> getQnasByType1(String type1, Pageable pageable) {
        Page<Inquiry> qnasPage = inquiryRepository.findByType1(type1, pageable);

        return PageResponseDTO.fromPage(qnasPage.map(this::convertToInquiryDTO));
    }

    @Override
    public void updateInquiryAnswer(Long id, String answer) {
        Inquiry inquiry = inquiryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid inquiry ID"));
        inquiry.setAnswer(answer);
        inquiryRepository.save(inquiry);
    }

    @Override
    public PageResponseDTO<InquiryDTO> getInquiriesByMemberId(String memberId, Pageable pageable) {
        Page<Inquiry> inquiriesPage = inquiryRepository.findByMember_Uid(memberId, pageable);
        return PageResponseDTO.fromPage(inquiriesPage.map(this::convertToInquiryDTO));
    }

    @Override
    public int countInquiriesByMemberId(String memberId) {
        return inquiryRepository.countByMember_Uid(memberId);
    }


    //  Notice 공지사항
    @Override
    public NoticeDTO createNotice(NoticeDTO noticeDTO) {
        Notice notice = convertToNoticeEntity(noticeDTO, findMemberById(noticeDTO.getMemberId()));
        return convertToNoticeDTO(noticeRepository.save(notice));
    }
    @Override
    public NoticeDTO getNoticeById(Long id) {
        return convertToNoticeDTO(noticeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notice not found with ID: " + id)));
    }
    @Override
    public PageResponseDTO<NoticeDTO> getAllNotices(Pageable pageable) {
        Page<Notice> notices = noticeRepository.findAll(pageable);
        AtomicLong count = new AtomicLong(notices.getTotalElements());
        return PageResponseDTO.fromPage(
                notices.map(notice -> {
                    NoticeDTO dto = convertToNoticeDTO(notice);
                    dto.setDisplayNumber(count.getAndDecrement());
                    return dto;
                })
        );
    }
    @Override
    public NoticeDTO updateNotice(Long id, NoticeDTO noticeDTO) {
        Notice notice = noticeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notice not found"));
        notice.setTitle(noticeDTO.getTitle());
        notice.setContent(noticeDTO.getContent());
        notice.setType1(noticeDTO.getType1());
        notice.setMember(findMemberById(noticeDTO.getMemberId()));
        return convertToNoticeDTO(noticeRepository.save(notice));
    }
    // 삭제 버튼
    @Override
    public void deleteNotice(Long id) {
        if (!noticeRepository.existsById(id)) {
            throw new IllegalArgumentException("해당 공지사항은 없습니다.");
        }
        noticeRepository.deleteById(id);
    }
    // 선택삭제
    @Override
    public void deleteNotice(List<Long> ids) {
        noticeRepository.deleteAllById(ids);
    }
    //  notice 카테고리
    @Override
    public PageResponseDTO<NoticeDTO> findNoticeByType1(String type1, Pageable pageable) {
        return PageResponseDTO.fromPage(
                noticeRepository.findByType1(type1, pageable)
                        .map(this::convertToNoticeDTO)
        );
    }
    // 공지사항을 type1으로 페이지네이션하여 조회
    @Override
    public PageResponseDTO<NoticeDTO> getNoticesByType1(String type1, Pageable pageable) {
        Page<Notice> noticesPage;
        if ("전체".equals(type1)) {
            noticesPage = noticeRepository.findAll(pageable);
        } else {
            noticesPage = noticeRepository.findByType1OrderByCreatedAtDesc(type1, pageable);
        }
        return PageResponseDTO.fromPage(noticesPage.map(this::convertToNoticeDTO));
    }
    // 조회수
    @Override
    public void incrementNoticeViews(Long id) {
        Notice notice = noticeRepository.findById(id).orElseThrow(() -> new RuntimeException("Notice not found"));
        notice.setViews(notice.getViews() + 1); // 조회수 증가
        noticeRepository.save(notice); // 변경사항 저장
    }



//  DTO Entity 변환

    // Faq 엔티티 - FaqDTO 변환
    private FaqDTO convertToFaqDTO(FAQ faq) {
        return FaqDTO.builder()
                .id(faq.getId())
                .title(faq.getTitle())
                .content(faq.getContent())
                .type1(faq.getType1())
                .type2(faq.getType2())
                .createdAt(faq.getCreatedAt())
                .updatedAt(faq.getUpdatedAt())
                .views(faq.getViews())
                .memberId(faq.getMember() !=null ? faq.getMember().getUid() : null)
                .build();

    }
    private FAQ convertToFaqEntity(FaqDTO faqDTO, Member member) {
        return FAQ.builder()
                .id(faqDTO.getId())
                .title(faqDTO.getTitle())
                .content(faqDTO.getContent())
                .type1(faqDTO.getType1())
                .type2(faqDTO.getType2())
                .createdAt(faqDTO.getCreatedAt())
                .updatedAt(faqDTO.getUpdatedAt())
                .views(faqDTO.getViews())
                .member(member)
                .build();
    }

    //  Inquiry 엔티티 - DTO
    private InquiryDTO convertToInquiryDTO(Inquiry inquiry) {
        return InquiryDTO.builder()
                .id(inquiry.getId())
                .title(inquiry.getTitle())
                .content(inquiry.getContent())
                .type1(inquiry.getType1())
                .type2(inquiry.getType2())
                .createdAt(inquiry.getCreatedAt())
                .updatedAt(inquiry.getUpdatedAt())
                .memberId(inquiry.getMember() !=null ? inquiry.getMember().getUid() : null)
                .answer(inquiry.getAnswer() != null ? inquiry.getAnswer() : null)
                .build();
    }

    private Inquiry convertToInquiryEntity(InquiryDTO inquiryDTO, Member member) {
        return Inquiry.builder()
                .id(inquiryDTO.getId())
                .title(inquiryDTO.getTitle())
                .content(inquiryDTO.getContent())
                .type1(inquiryDTO.getType1())
                .type2(inquiryDTO.getType2())
                .createdAt(inquiryDTO.getCreatedAt())
                .updatedAt(inquiryDTO.getUpdatedAt())
                .member(member)
                .build();
    }

    //    Notice 공지사항
    private NoticeDTO convertToNoticeDTO(Notice notice) {
        return NoticeDTO.builder()
                .id(notice.getId())
                .title(notice.getTitle())
                .content(notice.getContent())
                .type1(notice.getType1())
                .createdAt(notice.getCreatedAt())
                .updatedAt(notice.getUpdatedAt())
                .views(notice.getViews())
                .memberId(notice.getMember() != null ? notice.getMember().getUid() : null) // null 체크 추가
                .build();
    }

    private Notice convertToNoticeEntity(NoticeDTO noticeDTO, Member member) {
        return Notice.builder()
                .id(noticeDTO.getId())
                .title(noticeDTO.getTitle())
                .content(noticeDTO.getContent())
                .type1(noticeDTO.getType1())
                .createdAt(noticeDTO.getCreatedAt())
                .updatedAt(noticeDTO.getUpdatedAt())
                .views(noticeDTO.getViews())
                .member(member)
                .build();
    }

}
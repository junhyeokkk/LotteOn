package com.team1.lotteon.service.admin;
import com.team1.lotteon.dto.PageRequestDTO;
import com.team1.lotteon.dto.PageResponseDTO;
import com.team1.lotteon.dto.RecruitDTO;
import com.team1.lotteon.dto.pageDTO.NewPageRequestDTO;
import com.team1.lotteon.dto.pageDTO.NewPageResponseDTO;
import com.team1.lotteon.entity.Member;
import com.team1.lotteon.entity.Recruit;
import com.team1.lotteon.repository.Memberrepository.GeneralMemberRepository;
import com.team1.lotteon.repository.recruit.RecruitRepository;
import com.team1.lotteon.util.MemberUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/*
    날짜 : 2024/11/01
    이름 : 강유정
    내용 : 채용 서비스 생성
*/

@Log4j2
@RequiredArgsConstructor
@Service
@Transactional
public class RecruitService {

    @Autowired

    private final RecruitRepository recruitRepository;
    private final ModelMapper modelMapper;

    // 삭제
    public void deleteRecruitsByIds(List<Long> recruitIds) {
        recruitRepository.deleteAllByIdIn(recruitIds);
    }

    // 채용 db 저장
    public void saveRecruitDetails(RecruitDTO recruitDTO) {
        LocalDate startday = recruitDTO.getDisplayStartDate();
        LocalDate today = LocalDate.now();
        log.info("startday"+startday);
        log.info("today"+today);
        if (startday.equals(today)) {
            recruitDTO.setStatus("접수중");
        }else {
            recruitDTO.setStatus("접수준비");
        }
        Recruit recruit = modelMapper.map(recruitDTO, Recruit.class);
        recruitRepository.save(recruit);
    }

    // 채용 select 페이징 (ADMIN) + 검색기능
    public NewPageResponseDTO<RecruitDTO> getRecruitsWithPagination(NewPageRequestDTO newPageRequestDTO) {
        String type = newPageRequestDTO.getType();
        String keyword = newPageRequestDTO.getKeyword();
        Pageable pageable = newPageRequestDTO.getPageable("recruitid", false);
        Page<Recruit> recruitPage;

        if (keyword != null && !keyword.isEmpty()) {

            // 검색어가 있을 경우, 페이징을 무시하고 모든 결과를 필터링하여 반환
            switch (type) {
                case "recruitId":
                    try {
                        Long ID = Long.parseLong(keyword);
                        recruitPage = recruitRepository.findByRecruitid(keyword,pageable);
                    } catch (NumberFormatException e) {
                        recruitPage = Page.empty(pageable);
                    }
                    break;

                case "recruitPosition":
                    try {
                        recruitPage = recruitRepository.findByPositionContaining(keyword,pageable);
                    } catch (NumberFormatException e) {
                        recruitPage = Page.empty(pageable);
                    }
                    break;

                case "recruitType":
                    try {
                        recruitPage = recruitRepository.findByTypeContaining(keyword,pageable);
                    } catch (NumberFormatException e) {
                        recruitPage = Page.empty(pageable);
                    }
                    break;
                case "recruitTitle":
                    try {
                        recruitPage = recruitRepository.findByTitleContaining(keyword,pageable);
                    } catch (NumberFormatException e) {
                        recruitPage = Page.empty(pageable);
                    }
                    break;
                default:
                    recruitPage = recruitRepository.findAll(pageable);
                    break;
            }

        } else {
            // 검색어가 없을 경우 기본 페이징 처리
            recruitPage = recruitRepository.findAll(pageable);
        }
        // Recruit 엔티티를 DTO로 변환
        List<RecruitDTO> recruitDTOList = recruitPage.getContent()
                    .stream()
                    .map(recruit -> modelMapper.map(recruit, RecruitDTO.class))
                    .collect(Collectors.toList());

            return NewPageResponseDTO.<RecruitDTO>builder()
                    .newPageRequestDTO(newPageRequestDTO)
                    .dtoList(recruitDTOList)
                    .total((int) recruitPage.getTotalElements())
                    .build();
    }


    // 회사소개
    public List<RecruitDTO> getActiveRecruits() {
        List<Recruit> allRecruits = recruitRepository.findAll(); // 모든 데이터 조회
        List<RecruitDTO> activeRecruits = allRecruits.stream()
                .filter(recruit -> "접수중".equals(recruit.getStatus())) // "접수중"인 항목만 필터링
                .map(recruit -> modelMapper.map(recruit, RecruitDTO.class))
                .collect(Collectors.toList());
        return activeRecruits;
    }

}
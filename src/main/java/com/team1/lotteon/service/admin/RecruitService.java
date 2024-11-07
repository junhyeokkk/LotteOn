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
        Recruit recruit = modelMapper.map(recruitDTO, Recruit.class);
        recruitRepository.save(recruit);
    }

    // 채용 select 페이징 (ADMIN) + 검색기능
    public NewPageResponseDTO<RecruitDTO> getRecruitsWithPagination(NewPageRequestDTO newPageRequestDTO) {
        Pageable pageable = newPageRequestDTO.getPageable("recruitid", false);
        Page<Recruit> recruitPage = recruitRepository.findAll(pageable);

        String type = newPageRequestDTO.getType();
        String keyword = newPageRequestDTO.getKeyword();

        List<RecruitDTO> recruitDTOList = recruitPage.getContent()
                .stream()
                .filter(recruit -> {
                    if (type == null || keyword == null || keyword.isEmpty()) {
                        return true;
                    }
                    switch (type) {
                        case "recruitId":
                            return String.valueOf(recruit.getRecruitid()).contains(keyword);
                        case "recruitPosition":
                            return recruit.getPosition() != null && recruit.getPosition().contains(keyword);
                        case "recruitType":
                            return recruit.getType() != null && recruit.getType().contains(keyword);
                        case "recruitTitle":
                            return recruit.getTitle() != null && recruit.getTitle().contains(keyword);
                        default:
                            return true;
                    }
                })
                .map(recruit -> modelMapper.map(recruit, RecruitDTO.class))
                .collect(Collectors.toList());

        // 타입이 "all"인 경우 모든 데이터 가져오기
//        if ("all".equals(newPageRequestDTO.getType())) {
//            recruitPage = recruitRepository.findAll(pageable);
//        } else {
//            // 검색 조건이 있을 때 필터링
//            recruitPage = recruitRepository.findByDynamicType(
//                    newPageRequestDTO.getKeyword(),
//                    newPageRequestDTO.getType(),
//                    pageable);
//        }


        // PageResponseDTO 생성 및 반환
        // PageResponseDTO의 fromPage 메서드를 활용
        return NewPageResponseDTO.<RecruitDTO>builder()
                .newPageRequestDTO(newPageRequestDTO) // 요청 정보 설정
                .dtoList(recruitDTOList) // DTO 리스트 설정
                .total((int) recruitPage.getTotalElements()) // 총 요소 수 설정
                .build();
    }


}
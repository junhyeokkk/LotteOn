package com.team1.lotteon.service.admin;

import com.team1.lotteon.dto.GeneralMemberDTO;
import com.team1.lotteon.dto.Member.MemberPageRequestDTO;
import com.team1.lotteon.dto.Member.MemberPageResponseDTO;
import com.team1.lotteon.entity.GeneralMember;
import com.team1.lotteon.repository.Memberrepository.GeneralMemberRepository;
import com.team1.lotteon.repository.Memberrepository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminMemberService {

    private final GeneralMemberRepository generalMemberRepository;
    private final ModelMapper modelMapper;


    // 페이징 처리된 회원 목록 조회
    public MemberPageResponseDTO getPagedMembers(MemberPageRequestDTO pageRequestDTO) {
        // 페이지 요청 DTO에 있는 값으로 Pageable 객체 생성
        Pageable pageable = pageRequestDTO.getPageable("name"); // 기본 정렬은 'name' 기준으로

        // 검색 없이 전체 회원을 페이징 처리하여 가져옴
        Page<GeneralMember> result = generalMemberRepository.findAll(pageable);

        // Entity를 DTO로 변환
        List<GeneralMemberDTO> dtoList = result.getContent().stream()
                .map(generalMember -> modelMapper.map(generalMember, GeneralMemberDTO.class)) // generalMember로 명확히 표시
                .collect(Collectors.toList());


        // 페이지 응답 DTO 생성
        return MemberPageResponseDTO.builder()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(dtoList)
                .total((int) result.getTotalElements())
                .build();
    }
}



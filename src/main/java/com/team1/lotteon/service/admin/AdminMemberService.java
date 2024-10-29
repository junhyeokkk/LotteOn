package com.team1.lotteon.service.admin;

import com.team1.lotteon.dto.GeneralMemberDTO;
import com.team1.lotteon.dto.Member.MemberPageRequestDTO;
import com.team1.lotteon.dto.Member.MemberPageResponseDTO;
import com.team1.lotteon.entity.GeneralMember;
import com.team1.lotteon.entity.Address;
import com.team1.lotteon.entity.Member;
import com.team1.lotteon.entity.enums.Gender;
import com.team1.lotteon.entity.enums.Grade;
import com.team1.lotteon.repository.Memberrepository.GeneralMemberRepository;
import com.team1.lotteon.repository.Memberrepository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminMemberService {

    private final GeneralMemberRepository generalMemberRepository;
    private final ModelMapper modelMapper;


    // 검색 기능 추가된 페이징 처리된 회원 목록 조회 메서드
    public MemberPageResponseDTO getPagedMembers(MemberPageRequestDTO pageRequestDTO) {
        Pageable pageable = pageRequestDTO.getPageable("name"); // 기본 정렬을 'name' 기준으로 설정
        Page<GeneralMember> result;

        String type = pageRequestDTO.getType();
        String keyword = pageRequestDTO.getKeyword();

        // 타입에 따른 조건별 검색
        if ("uid".equals(type)) {
            result = generalMemberRepository.findByUidContaining(keyword, pageable);
        } else if ("name".equals(type)) {
            result = generalMemberRepository.findByNameContaining(keyword, pageable);
        } else if ("email".equals(type)) {
            result = generalMemberRepository.findByEmailContaining(keyword, pageable);
        } else if ("contact".equals(type)) {
            result = generalMemberRepository.findByPhContaining(keyword, pageable);
        } else {
            result = generalMemberRepository.findAll(pageable); // 기본 전체 조회
        }

        List<GeneralMemberDTO> dtoList = result.getContent().stream()
                .map(member -> modelMapper.map(member, GeneralMemberDTO.class))
                .collect(Collectors.toList());

        return MemberPageResponseDTO.builder()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(dtoList)
                .total((int) result.getTotalElements())
                .build();
    }


    // 모든 회원 조회
    public List<GeneralMemberDTO> getAllMembers() {
        return generalMemberRepository.findAll().stream()
                .map(member -> modelMapper.map(member, GeneralMemberDTO.class))
                .collect(Collectors.toList());
    }

    // 회원 정보 가져오기
    public GeneralMemberDTO getMemberByUid(String uid) {
        GeneralMember member = generalMemberRepository.findByUid(uid)
                .orElseThrow(() -> new IllegalArgumentException("해당 아이디를 가진 회원이 없습니다: " + uid));

        GeneralMemberDTO memberDTO = modelMapper.map(member, GeneralMemberDTO.class);
        if (member.getAddress() != null) {
            memberDTO.setZip(member.getAddress().getZip());
            memberDTO.setAddr1(member.getAddress().getAddr1());
            memberDTO.setAddr2(member.getAddress().getAddr2());
        }
        return memberDTO;
    }

    // 회원 정보 수정
    public GeneralMemberDTO updateMember(String uid, GeneralMemberDTO memberDTO) {
        GeneralMember member = generalMemberRepository.findByUid(uid)
                .orElseThrow(() -> new IllegalArgumentException("해당 아이디를 가진 회원이 없습니다: " + uid));

        member.setName(memberDTO.getName());
        member.setGender(Gender.valueOf(memberDTO.getGender()));
        member.setEmail(memberDTO.getEmail());
        member.setPh(memberDTO.getPh());

        // 기존 주소 객체를 가져온 후 업데이트
        Address address = member.getAddress();
        if (address == null) {
            // 주소가 없으면 새로 생성
            address = new Address(memberDTO.getZip(), memberDTO.getAddr1(), memberDTO.getAddr2());
            member.setAddress(address);
        } else {
            // 주소가 있으면 기존 주소 객체 업데이트
            address.setZip(memberDTO.getZip());
            address.setAddr1(memberDTO.getAddr1());
            address.setAddr2(memberDTO.getAddr2());
        }
        Address address1 = new Address(memberDTO.getZip(), memberDTO.getAddr1(), memberDTO.getAddr2());
        member.setAddress(address);

        member.setEtc(memberDTO.getEtc());

        GeneralMember updatedMember = generalMemberRepository.save(member);
        return modelMapper.map(updatedMember, GeneralMemberDTO.class);
    }

    // 회원 등록
    public GeneralMemberDTO createMember(GeneralMemberDTO memberDTO) {
        GeneralMember newMember = modelMapper.map(memberDTO, GeneralMember.class);
        GeneralMember savedMember = generalMemberRepository.save(newMember);
        return modelMapper.map(savedMember, GeneralMemberDTO.class);
    }

    // 회원 삭제
    public void deleteMember(String uid) {
        GeneralMember member = generalMemberRepository.findByUid(uid)
                .orElseThrow(() -> new IllegalArgumentException("해당 아이디를 가진 회원이 없습니다: " + uid));
        generalMemberRepository.delete(member);
    }

    // GeneralMember를 GeneralMemberDTO로 매핑
    private GeneralMemberDTO mapToDTO(GeneralMember member) {
        GeneralMemberDTO memberDTO = modelMapper.map(member, GeneralMemberDTO.class);
        memberDTO.setCreatedAt(member.getCreatedAt()); // 가입일 매핑
        if (member.getAddress() != null) {
            memberDTO.setZip(member.getAddress().getZip());
            memberDTO.setAddr1(member.getAddress().getAddr1());
            memberDTO.setAddr2(member.getAddress().getAddr2());
        }
        return memberDTO;
    }

    // 회원 등급 변경
    @Transactional
    public void updateMemberGrade(String uid, String grade) {
        // 디버깅 로그
        System.out.println("회원 ID: " + uid + ", 선택된 등급: " + grade);

        // 회원 조회 및 예외 처리
        GeneralMember member = generalMemberRepository.findByUid(uid)
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다. ID: " + uid));

        try {
            // 문자열을 enum으로 변환 및 예외 처리
            Grade gradeEnum = Grade.valueOf(grade.toUpperCase());
            member.setGrade(gradeEnum);

            // 변경 사항 저장
            generalMemberRepository.save(member);

        } catch (IllegalArgumentException e) {
            System.err.println("유효하지 않은 등급 값입니다: " + grade); // 오류 로그 출력
            throw new IllegalArgumentException("유효하지 않은 등급 값입니다. 입력한 등급: " + grade);
        }
    }

}





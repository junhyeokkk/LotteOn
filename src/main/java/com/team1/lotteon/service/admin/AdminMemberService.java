package com.team1.lotteon.service.admin;

import com.team1.lotteon.dto.GeneralMemberDTO;
import com.team1.lotteon.dto.Member.MemberPageRequestDTO;
import com.team1.lotteon.dto.Member.MemberPageResponseDTO;
import com.team1.lotteon.entity.GeneralMember;
import com.team1.lotteon.entity.Address;
import com.team1.lotteon.entity.enums.Gender;
import com.team1.lotteon.repository.Memberrepository.GeneralMemberRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
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

    // 페이징 처리된 회원 목록 조회 메서드
    public MemberPageResponseDTO getPagedMembers(MemberPageRequestDTO pageRequestDTO) {
        Pageable pageable = pageRequestDTO.getPageable("name"); // 기본 정렬을 'name' 기준으로 설정
        Page<GeneralMember> result = generalMemberRepository.findAll(pageable);

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
        return modelMapper.map(member, GeneralMemberDTO.class);
    }

    // 회원 정보 수정
    public GeneralMemberDTO updateMember(String uid, GeneralMemberDTO memberDTO) {
        GeneralMember member = generalMemberRepository.findByUid(uid)
                .orElseThrow(() -> new IllegalArgumentException("해당 아이디를 가진 회원이 없습니다: " + uid));

        member.setName(memberDTO.getName());
        member.setGender(Gender.valueOf(memberDTO.getGender()));
        member.setEmail(memberDTO.getEmail());
        member.setPh(memberDTO.getPh());

        Address address = new Address(memberDTO.getZip(), memberDTO.getAddr1(), memberDTO.getAddr2());
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
}

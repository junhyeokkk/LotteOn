package com.team1.lotteon.service.MemberService;

import com.team1.lotteon.entity.GeneralMember;
import com.team1.lotteon.repository.Memberrepository.GeneralMemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

/*
  날짜 : 2024/10/25
  이름 : 이도영
  내용 : 일반회원 레포지토리

  수정사항
  - 2024/11/04 이도영  이메일 아이디 찾기 기능 추가
*/
@Log4j2
@RequiredArgsConstructor
@Service
public class GeneralMemberService {
    private final GeneralMemberRepository generalMemberRepository;
    private final ModelMapper modelMapper;

    public void insertGeneralMember(GeneralMember generalMember) {
        generalMemberRepository.save(generalMember);
    }

    // 이메일 중복 확인
    public boolean isEmailExist(String email) {
        return generalMemberRepository.existsByEmail(email);
    }
    public boolean isphExist(String ph){
        return generalMemberRepository.existsByph(ph);
    }
    public GeneralMember  findUserIdByNameAndEmail(String name, String email) {
        return generalMemberRepository.findByNameAndEmail(name, email);
    }

    public GeneralMember findByUidAndEmail(String uid, String email) {
        return generalMemberRepository.findByUidAndEmail(uid, email);
    }


}

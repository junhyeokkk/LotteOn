package com.team1.lotteon.service.MemberService;

import com.team1.lotteon.entity.GeneralMember;
import com.team1.lotteon.repository.Memberrepository.GeneralMemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

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
}

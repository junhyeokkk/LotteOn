package com.team1.lotteon.service.MemberService;

import com.team1.lotteon.dto.MemberDTO;
import com.team1.lotteon.entity.Member;
import com.team1.lotteon.repository.Memberrepository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Log4j2
@RequiredArgsConstructor
@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    public MemberDTO insertmember(MemberDTO memberDTO) {
        String encoded = passwordEncoder.encode(memberDTO.getPass());
        memberDTO.setPass(encoded);
        Member entity = modelMapper.map(memberDTO, Member.class);
        memberRepository.save(entity);
        return null;
    }
}

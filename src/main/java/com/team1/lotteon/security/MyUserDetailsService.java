package com.team1.lotteon.security;

import com.team1.lotteon.entity.GeneralMember;
import com.team1.lotteon.entity.Member;
import com.team1.lotteon.repository.Memberrepository.GeneralMemberRepository;
import com.team1.lotteon.repository.Memberrepository.MemberRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MyUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;
    private final GeneralMemberRepository generalMemberRepository;

    public MyUserDetailsService(MemberRepository memberRepository, GeneralMemberRepository generalMemberRepository) {
        this.memberRepository = memberRepository;
        this.generalMemberRepository = generalMemberRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        //사용자가 입력한 아이디로 사용자 조회, 비밀번호에 대한 검증은 이전 컴포넌트인 AuthenticationProvider에서 수행
        Optional<Member> optMember  = memberRepository.findById(username);

        if (optMember.isPresent()) {
            Member member = optMember.get();

            // GeneralMember가 있는 경우 조회
            GeneralMember generalMember = generalMemberRepository.findByUid(member.getUid())
                    .orElse(null);  // 만약 GeneralMember가 존재하지 않으면 null

            return MyUserDetails.builder()
                    .member(member)
                    .generalMember(generalMember)  // generalMember를 추가
                    .build();
        }

        throw new UsernameNotFoundException("User not found with username: " + username);
    }
}

package com.team1.lotteon.security;

import com.team1.lotteon.entity.Member;
import com.team1.lotteon.repository.Memberrepository.MemberRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MyUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    public MyUserDetailsService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        //사용자가 입력한 아이디로 사용자 조회, 비밀번호에 대한 검증은 이전 컴포넌트인 AuthenticationProvider에서 수행
        Optional<Member> optUser = memberRepository.findById(username);

        if(optUser.isPresent()) {
            //시큐리티 사용자 인증객체 생성 후 반환
            MyUserDetails myUserDetails = MyUserDetails
                                                .builder()
                                                .member(optUser.get())
                                                .build();
            return myUserDetails;
        }
    //사용자가 입력한 아이디가 없을 경우
    return null;
    }
}

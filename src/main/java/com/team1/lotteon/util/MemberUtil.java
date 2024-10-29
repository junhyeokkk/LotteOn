package com.team1.lotteon.util;

import com.team1.lotteon.entity.GeneralMember;
import com.team1.lotteon.entity.Member;
import com.team1.lotteon.repository.Memberrepository.GeneralMemberRepository;

import com.team1.lotteon.repository.Memberrepository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

/*
    날짜 : 2024/10/29
    이름 : 최준혁
    내용 : 사용자 Util
*/
@RequiredArgsConstructor
@Component
public class MemberUtil {

    private static GeneralMemberRepository generalMemberRepository;
    private static MemberRepository MemberRepository;


    @Autowired
    public MemberUtil(GeneralMemberRepository generalMemberRepository, MemberRepository memberRepository) {
        MemberUtil.generalMemberRepository = generalMemberRepository;
        MemberUtil.MemberRepository = memberRepository;
    }

    // 로그인한 Member 객체 반환
    public static Member getLoggedInMember() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            String username;

            if (principal instanceof UserDetails) {
                username = ((UserDetails) principal).getUsername();
            } else {
                username = principal.toString();
            }

            return MemberRepository.findById(username)
                    .orElseThrow(() -> new IllegalArgumentException("로그인된 사용자를 찾을 수 없습니다."));
        }

        return null; // 인증된 사용자가 없을 경우
    }

    // 로그인한 GeneralMember 객체 반환
    public static GeneralMember getLoggedInGeneralMember() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            String username;

            if (principal instanceof UserDetails) {
                username = ((UserDetails) principal).getUsername();
            } else {
                username = principal.toString();
            }

            return generalMemberRepository.findById(username)
                    .orElseThrow(() -> new IllegalArgumentException("로그인된 사용자를 찾을 수 없습니다."));
        }

        return null; // 인증된 사용자가 없을 경우
    }
}
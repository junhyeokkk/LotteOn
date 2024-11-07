package com.team1.lotteon.security;

import com.team1.lotteon.entity.GeneralMember;
import com.team1.lotteon.entity.Member;
import com.team1.lotteon.entity.SellerMember;
import com.team1.lotteon.repository.Memberrepository.MemberRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.time.LocalDateTime;

@Component
public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private MemberRepository memberRepository;  // GeneralMember를 다루는 리포지토리

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        // 로그인한 사용자 정보 가져오기
        MyUserDetails userDetails = (MyUserDetails) authentication.getPrincipal();
        Member member = userDetails.getMember();
        String role = member.getRole();
        if (!authentication.isAuthenticated()) {
            response.sendRedirect("/");
            return;
        }

        // GeneralMember일 때만 로그인 시간을 설정
        if (role.equals("General")) {
            GeneralMember generalMember = (GeneralMember) member;
            if(generalMember.getStatus()==4){
                String errorMessage = "로그인 할수 없는 탈퇴한 계정입니다";
                response.sendRedirect("/member/logout?message=" + URLEncoder.encode(errorMessage, "UTF-8"));
                return;
            }else if(generalMember.getStatus()==3){
                String errorMessage = "휴면 계정입니다 관리자에게 문의 해주세요";
                response.sendRedirect("/member/logout?message=" + URLEncoder.encode(errorMessage, "UTF-8"));
                return;
            }else if(generalMember.getStatus()==2){
                String errorMessage = "정지된 계정입니다";
                response.sendRedirect("/member/logout?message=" + URLEncoder.encode(errorMessage, "UTF-8"));
                return;
            }
            generalMember.setLastLoginDate(LocalDateTime.now());
            memberRepository.save(generalMember);  // 로그인 시간을 DB에 저장
            response.sendRedirect("/");  // General 사용자 리다이렉트
            return;  // 더 이상 다른 로직을 실행하지 않도록 종료
        }

        // SellerMember일 때 리다이렉트
        if (role.equals("Seller")) {
            response.sendRedirect("/admin");   // Seller 사용자 리다이렉트
            return;
        }

        // AdminMember일 때 리다이렉트
        if (role.equals("Admin")) {
            response.sendRedirect("/admin");    // Admin 사용자 리다이렉트
            return;
        }
        // 다른 경우 처리 (에러 또는 기본 리다이렉트)
        response.sendRedirect("/error");
    }
}

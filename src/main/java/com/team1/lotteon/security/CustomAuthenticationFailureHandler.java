//package com.team1.lotteon.security;
//
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.web.authentication.AuthenticationFailureHandler;
//
//import java.io.IOException;
//
//public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {
//
//    @Override
//    public void onAuthenticationFailure(HttpServletRequest request,
//                                        HttpServletResponse response,
//                                        AuthenticationException exception) throws IOException, ServletException {
//        // 로그인 실패 시 리다이렉트할 URL
//        String targetUrl = "/user/UserLogin?error=true"; // 알림을 위한 쿼리 파라미터 추가
//        response.sendRedirect(targetUrl);
//    }
//}

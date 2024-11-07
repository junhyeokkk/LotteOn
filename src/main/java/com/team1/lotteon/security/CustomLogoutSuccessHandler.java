package com.team1.lotteon.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;

@Component
public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String errorMessage = request.getParameter("message");

        if (errorMessage != null) {
            response.sendRedirect("/?message=" + URLEncoder.encode(errorMessage, "UTF-8"));
        } else {
            response.sendRedirect("/?success=101");  // 기본 메시지
        }
    }
}

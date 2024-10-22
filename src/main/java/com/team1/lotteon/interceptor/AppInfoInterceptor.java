package com.team1.lotteon.interceptor;


import com.team1.lotteon.config.AppInfo;
import com.team1.lotteon.dto.ConfigDTO;
import com.team1.lotteon.service.admin.ConfigService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;


@RequiredArgsConstructor
public class AppInfoInterceptor implements HandlerInterceptor {

      /*
        Interceptor
        - 클라이언트의 요청과 컨트롤러 사이에서 특정 작읍을 수행하기 위한 컴포넌트
        - HTTP 요청을 가로채고, 요청이 컨트롤러에 도달전과 후에 추가 작업 수행
     */

    private final AppInfo appInfo;

    private final ConfigService configService;

    //postHandle은  controller의 요청 메서드에서
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        // 컨트롤러 (요청 메서드)를 수행 후 실행
        if (modelAndView != null) {
            modelAndView.addObject("appInfo", appInfo);  // 기존 AppInfo 객체 추가
            ConfigDTO config = configService.getCompanyInfo();
            modelAndView.addObject("config", config);
        }
    }
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //컨트롤러 를 수행 전 실행

        return HandlerInterceptor.super.preHandle(request, response, handler);
    }
}


package com.team1.lotteon.interceptor;


import com.team1.lotteon.config.AppInfo;
import com.team1.lotteon.dto.ConfigDTO;
import com.team1.lotteon.dto.VersionDTO;
import com.team1.lotteon.service.admin.ConfigService;
import com.team1.lotteon.service.admin.VersionService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/*
    날짜 : 2024/10/22
    이름 : 최준혁
    내용 : AppInfoInterceptor 생성

    - Config 정보 헤더, 푸터, 파비콘 등 동적 처리를 위한 인터셉터 생성
*/

@RequiredArgsConstructor
public class AppInfoInterceptor implements HandlerInterceptor {


    private final AppInfo appInfo;

    private final ConfigService configService;
    private final VersionService versionService;
    //postHandle은  controller의 요청 메서드에서
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        // 컨트롤러 (요청 메서드)를 수행 후 실행
        if (modelAndView != null) {
            modelAndView.addObject("appInfo", appInfo);  // 기존 AppInfo 객체 추가
            ConfigDTO config = configService.getCompanyInfo();
            VersionDTO versionDTO = versionService.getLatestVersion();

            modelAndView.addObject("config", config);
            modelAndView.addObject("version", versionDTO);
        }
    }
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //컨트롤러 를 수행 전 실행

        return HandlerInterceptor.super.preHandle(request, response, handler);
    }
}


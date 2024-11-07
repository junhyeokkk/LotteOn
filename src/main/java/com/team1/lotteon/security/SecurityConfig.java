package com.team1.lotteon.security;

//import com.farmstory.oauth2.MyOauth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
/*

  날짜 : 2024/10/25
  이름 : 이도영
  내용 : 인가설정 작업

  수정사항
  - 2024/11/04 이도영 인가설정 처리 최신화
  - 2024/11/05 이도영 자동 로그인 기능 추가
  - 2024/11/06 이도영 인가설정 변경(문의하기 작성 관리자 가능 하게, 관리자 Member 관리자 가능하게)
                     자동로그인 기능 추가
*/
@EnableWebSecurity
@RequiredArgsConstructor
@Configuration
public class SecurityConfig {

//    private final MyOauth2UserService myOauth2UserService;
    private final CustomLoginSuccessHandler customLoginSuccessHandler;
    private final UserDetailsService userDetailsService;
    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {

        //로그인 설정
        http.formLogin(login -> login
                .loginPage("/user/login")
                .loginProcessingUrl("/user/logincheck") // 로그인 폼 제출 시 처리될 경로
                .successHandler(customLoginSuccessHandler)
//                .defaultSuccessUrl("/")//컨트롤러 요청 주소
                .failureHandler(new CustomAuthenticationFailureHandler()) // 실패 시 핸들러 추가
                .usernameParameter("uid")
                .passwordParameter("pass"));
        //자동 로그인
        http.rememberMe(rememberMe -> rememberMe
                .key("uniqueAndSecret")
                .tokenValiditySeconds(60*60*24*3)
                .rememberMeParameter("remember-me") // HTML 폼의 체크박스 이름
                .rememberMeCookieName("remember-me-cookie")
                .userDetailsService(userDetailsService));
        //로그아웃 설정
        http.logout(logout -> logout
                .invalidateHttpSession(true)
                .logoutRequestMatcher(new AntPathRequestMatcher("/member/logout"))
                .logoutSuccessHandler(new CustomLogoutSuccessHandler()));


        //Oauth2 설정
//        http.oauth2Login(login -> login
//                .loginPage("/user/UserLogin")
//                .userInfoEndpoint(endpoint -> endpoint.userService(myOauth2UserService)));

        // 인가 설정
        http.authorizeHttpRequests(authorize -> authorize
                //나의 정보
                .requestMatchers("/myPage/**").hasRole("General")
                //상품 정보
                .requestMatchers("/product/cart").hasRole("General")
                .requestMatchers("/product/order").hasRole("General")
                .requestMatchers("/product/complete/**").hasRole("General")
                //문의 하기
                .requestMatchers("/cs/layout/qna/write").hasAnyRole("General","Admin")
                //관리자화면
                .requestMatchers("/admin/").hasAnyRole("Admin", "Seller")
                .requestMatchers("/admin/config/**").hasAnyRole("Admin")
                .requestMatchers("/admin/shop/list").hasAnyRole("Admin")
                .requestMatchers("/admin/shop/sales").hasAnyRole("Admin", "Seller")
                .requestMatchers("/admin/member/**").hasAnyRole("Admin")
                .requestMatchers("/admin/product/**").hasAnyRole("Admin", "Seller")
                .requestMatchers("/admin/order/**").hasAnyRole("Admin", "Seller")
                .requestMatchers("/admin/coupon/**").hasAnyRole("Admin", "Seller")
                .requestMatchers("/admin/cs/notice/list").hasAnyRole("Admin")
                .requestMatchers("/admin/cs/faq/list").hasAnyRole("Admin")
                .requestMatchers("/admin/cs/qna/list").hasAnyRole("Admin", "Seller")
                .requestMatchers("/admin/cs/recruit/list").hasAnyRole("Admin")
                .anyRequest().permitAll());
        // 인증되지 않은 사용자가 접근할 때 로그인 페이지로 리다이렉트
        http.exceptionHandling(exceptionHandling -> exceptionHandling
                .authenticationEntryPoint((request, response, authException) -> {
                    response.sendRedirect("/user/login");
                }));
        //보안 설정
        http.csrf(configure -> configure.disable());

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}

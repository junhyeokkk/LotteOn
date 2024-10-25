package com.team1.lotteon.security;

//import com.farmstory.oauth2.MyOauth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@EnableWebSecurity
@RequiredArgsConstructor
@Configuration
public class SecurityConfig {

//    private final MyOauth2UserService myOauth2UserService;

    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {

        //로그인 설정
        http.formLogin(login -> login
                .loginPage("/user/login")
                .loginProcessingUrl("/user/logincheck") // 로그인 폼 제출 시 처리될 경로
                .defaultSuccessUrl("/")//컨트롤러 요청 주소
                .failureHandler(new CustomAuthenticationFailureHandler()) // 실패 시 핸들러 추가
                .usernameParameter("uid")
                .passwordParameter("pass"));

        //로그아웃 설정
        http.logout(logout -> logout
                .invalidateHttpSession(true)
                .logoutRequestMatcher(new AntPathRequestMatcher("/member/logout"))
                .logoutSuccessUrl("/?success=101"));


        //Oauth2 설정
//        http.oauth2Login(login -> login
//                .loginPage("/user/UserLogin")
//                .userInfoEndpoint(endpoint -> endpoint.userService(myOauth2UserService)));

        // 인가 설정
        http.authorizeHttpRequests(authorize -> authorize
          //      .requestMatchers("/myPage/**").authenticated()
//                .requestMatchers("/admin/**").hasRole("ADMIN")
//
//                .requestMatchers("/crop/*/CropWrite").authenticated()
//                .requestMatchers("/crop/*/CropView/*").authenticated()
//
//                .requestMatchers("community/*/CommunityWrite/*").authenticated()
//
//                .requestMatchers("community/CommunityNotice/CommunityView/*").permitAll()
//                .requestMatchers("community/CommunityNotice/CommunityWrite").hasRole("ADMIN")
//                .requestMatchers("community/CommunityDiet/CommunityWrite").authenticated()
//                .requestMatchers("community/CommunityChef/CommunityWrite").authenticated()
//                .requestMatchers("community/CommunityCs/CommunityWrite").authenticated()
//
//                .requestMatchers("community/CommunityFaq/CommunityView/*").permitAll()
//                .requestMatchers("community/CommunityFaq/CommunityWrite").hasRole("ADMIN")
//
//                .requestMatchers("market/MarketView").permitAll()
//                .requestMatchers("market/MarketCart").authenticated()
//                .requestMatchers("market/MarketOrder12").authenticated()


                .anyRequest().permitAll());

        //보안 설정
        http.csrf(configure -> configure.disable());

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}

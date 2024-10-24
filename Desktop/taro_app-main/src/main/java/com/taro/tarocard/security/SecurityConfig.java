package com.taro.tarocard.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true) // 메서드 레벨 보안 사용
public class SecurityConfig {
    @Autowired
    @Lazy
    private CustomOAuth2UserService customOAuth2UserService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable() // CSRF 보호 비활성화 (테스트 용도)
                .authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests
                        .requestMatchers("/user/login", "/user/signup", "/main", "/card/list", "/feedback","/categories/**", "/images/**", "/css/**", "/js/**").permitAll() // 로그인, 회원가입, 메인 페이지는 허용
                        .requestMatchers("/profile/update").authenticated() // 프로필 업데이트 경로 인증 필요
                        .requestMatchers("/profile/**").authenticated() // 모든 프로필 관련 URL에 대해 인증 필요
                        .anyRequest().authenticated() // 나머지 요청은 인증 필요
                )
                .formLogin(formLogin -> formLogin
                        .loginPage("/user/login")
                        .defaultSuccessUrl("/main", true) // 로그인 성공 후 메인 페이지로 리다이렉트
                )
                .logout(logout -> logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/user/logout"))
                        .logoutSuccessHandler(logoutSuccessHandler()) // 로그아웃 성공 핸들러 설정
                        .invalidateHttpSession(true) // 세션 무효화
                        .deleteCookies("JSESSIONID") // JSESSIONID 쿠키 삭제
                )
                .oauth2Login(
                        oauth2Login -> oauth2Login
                                .loginPage("/user/login")
                                .defaultSuccessUrl("/main", true)
                                .failureUrl("/user/login?error=true")
                );

        return http.build();
    }

    private LogoutSuccessHandler logoutSuccessHandler() {
        return (HttpServletRequest request, HttpServletResponse response, Authentication authentication) -> {
            // 카카오 로그아웃을 위한 URL 설정
            String clientId = "a4006ed0c83cfbf02f9f167bb3fe7651"; // 카카오 REST API 키
            String logoutRedirectUri = "http://localhost:8080/main"; // 로그아웃 후 리다이렉트할 URI

            // 카카오 로그아웃 URL 생성
            String kakaoLogoutUrl = String.format("https://kauth.kakao.com/oauth/logout?client_id=%s&logout_redirect_uri=%s",
                    clientId, logoutRedirectUri);

            // 카카오 로그아웃 페이지로 리다이렉트
            response.sendRedirect(kakaoLogoutUrl);
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
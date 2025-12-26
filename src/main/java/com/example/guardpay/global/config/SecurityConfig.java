package com.example.guardpay.global.config;

import com.example.guardpay.global.auth.CustomOAuth2UserService;
import com.example.guardpay.global.auth.OAuth2AuthenticationSuccessHandler;
import com.example.guardpay.global.jwt.JwtAuthenticationFilter;
import com.example.guardpay.global.jwt.JwtTokenProvider;
// import jakarta.annotation.PostConstruct; // 👈 [제거됨]
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
// import org.springframework.security.core.context.SecurityContextHolder; // 👈 [제거됨]
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import lombok.RequiredArgsConstructor;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;

// 명시적 빈 정의
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;
    private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;
    private final JwtTokenProvider jwtTokenProvider;



    //비번 인코딩
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .httpBasic(httpBasic -> httpBasic.disable())
                .formLogin(formLogin -> formLogin.disable())
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );

        // h2-console을 위한 프레임 옵션 허용
        http.headers(headers ->
                headers.frameOptions(frameOptions -> frameOptions.sameOrigin())
        );

        http.cors(cors -> cors.configurationSource(request -> {
            CorsConfiguration config = new CorsConfiguration();
            config.setAllowedOrigins(List.of("*"));
            config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
            config.setAllowedHeaders(List.of("*"));
            config.setAllowCredentials(false);
            return config;
        }));

        http.authorizeHttpRequests(auth -> auth
                .requestMatchers(
                        "/",
                        "/h2-console/**", // h2-console 허용
                        "/api/auth/**",
                        "/oauth2/**",
                        "/login/oauth2/**",
                        "/api/videos/**",
                        "/swagger-ui/**",
                        "/swagger-ui.html",
                        "/v3/api-docs/**",
                        "/swagger-resources/**",
                        "/webjars/**",
                        //"/api/quiz/**",
                        "/api/v1/diagnoses/**",

                        // ⬇️ 1. 퀴즈 API 중 '조회' API들만 permitAll()에 둡니다.
                        "/api/quiz/categories",
                        "/api/quiz/{categoryId}/list",
                        "/api/quiz/{categoryId}/{level}/list",
                        "/api/quiz/{quizId}"
                ).permitAll()
                .requestMatchers(
                        "/api/chat/**",
                        // ⬇️ 퀴즈 API 중 인증이 필요한 경로들
                        "/api/quiz/history",
                        "/api/quiz/progress",
                        "/api/quiz/level",
                        "/api/quiz/{quizId}/submit",
                        "/api/v1/banks/"
                ).authenticated() // chat 경로는 인증 필요
                .anyRequest().permitAll()
        );

        http.exceptionHandling(ex -> ex
                .authenticationEntryPoint((req, res, ex1) -> {
                    res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    res.setContentType("application/json;charset=UTF-8");
                    res.getWriter().write("{\"status\":401,\"message\":\"UNAUTHORIZED\"}");
                })
                .accessDeniedHandler((req, res, ex2) -> {
                    res.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    res.setContentType("application/json;charset=UTF-8");
                    res.getWriter().write("{\"status\":403,\"message\":\"FORBIDDEN\"}");
                })
        );

        http.oauth2Login(oauth2 -> oauth2
                .successHandler(oAuth2AuthenticationSuccessHandler)
                .userInfoEndpoint(userInfo -> userInfo.userService(customOAuth2UserService))
        );

        // JwtAuthenticationFilter를 UsernamePasswordAuthenticationFilter 전에 추가
        http.addFilterBefore(
                new JwtAuthenticationFilter(jwtTokenProvider),
                UsernamePasswordAuthenticationFilter.class
        );

        return http.build();
    }
}
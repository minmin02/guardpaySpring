package com.example.guardpay.global.jwt;

import jakarta.servlet.DispatcherType;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

// ⚠️ @Component 제거!
// 이 필터는 SecurityConfig에서 수동으로 Bean에 등록됩니다.
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    // 생성자를 통해 JwtTokenProvider 주입
    public JwtAuthenticationFilter(JwtTokenProvider provider) {
        this.jwtTokenProvider = provider;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String requestURI = request.getRequestURI();
        String dispatchType = request.getDispatcherType().toString();

        log.info("🔍 [JWT Filter] URI: {}, DispatchType: {}", requestURI, dispatchType);

        // ✅ FORWARD나 ERROR 디스패치는 건너뛰기
        if (!DispatcherType.REQUEST.equals(request.getDispatcherType())) {
            log.info("⏭️ [JWT Filter] Skipping non-REQUEST dispatch: {}", dispatchType);
            filterChain.doFilter(request, response);
            return;
        }

        // ⚠️ [수정] 필터에서 경로 검사 로직 (if, shouldNotFilter) 완전 제거
        // 경로에 대한 접근 제어는 SecurityConfig에서만 담당합니다.
        // 필터는 헤더가 존재하면 무조건 검증을 시도합니다.

        // ✅ FORWARD나 ERROR 디스패치는 건너뛰기
        if (!DispatcherType.REQUEST.equals(request.getDispatcherType())) {
            log.info("⏭️ [JWT Filter] Skipping non-REQUEST dispatch: {}", dispatchType);
            filterChain.doFilter(request, response);
            return;
        }

        // ✅ [추가] Swagger 경로는 JWT 검증 스킵
        if (requestURI.startsWith("/swagger-ui") ||
                requestURI.startsWith("/v3/api-docs") ||
                requestURI.startsWith("/swagger-resources") ||
                requestURI.equals("/swagger-ui.html")) {
            log.info("🔓 [JWT Filter] Swagger path detected. Skipping JWT validation.");
            filterChain.doFilter(request, response);
            return;
        }

        // Authorization 헤더 확인
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            // 토큰이 없는 경우, SecurityConfig의 authorizeHttpRequests 설정에 따라
            // 접근이 허용될 수도 (permitAll) 있고, 거부될 수도 (authenticated) 있습니다.
            // 여기서는 단순히 다음 필터로 넘깁니다.
            log.info("🔍 [JWT Filter] No JWT token found. Passing to next filter.");
            filterChain.doFilter(request, response);
            return;
        }

        log.info("🔍 [JWT Filter] Authorization Header: exists");

        try {
            String token = authHeader.substring(7);
            log.info("🔍 [JWT Filter] Token extracted (length: {})", token.length());

            // 토큰 검증
            if (jwtTokenProvider.validateToken(token)) {
                // 토큰 타입 확인 (Access Token인지)
                String tokenType = jwtTokenProvider.getTokenType(token);
                log.info("🔍 [JWT Filter] Token type: {}", tokenType);

                if ("access".equals(tokenType)) {
                    // Authentication 생성 및 설정
                    Authentication authentication = jwtTokenProvider.getAuthentication(token);
                    SecurityContextHolder.getContext().setAuthentication(authentication);

                    log.info("✅ [JWT Filter] Authentication set - Principal: {}, Authorities: {}",
                            authentication.getPrincipal(),
                            authentication.getAuthorities());
                } else {
                    log.warn("❌ [JWT Filter] Not an access token: {}", tokenType);
                }
            } else {
                log.warn("❌ [JWT Filter] Invalid token");
            }

        } catch (Exception e) {
            log.error("❌ [JWT Filter] Error processing token: {}", e.getMessage());
        }

        // ✅ [중요] 필터 체인 계속 진행
        filterChain.doFilter(request, response);
    }

    // ⚠️ [수정] shouldNotFilter 메소드 제거
    // 경로 관리는 SecurityConfig에서만 수행합니다.
}

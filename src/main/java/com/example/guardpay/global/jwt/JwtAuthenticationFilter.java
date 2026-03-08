package com.example.guardpay.global.jwt;

import jakarta.servlet.DispatcherType;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    private static final List<String> SKIP_PATHS = Arrays.asList(
            "/api/auth/",
            "/api/v1/diagnoses/",
            //"/api/quiz/",
            "/api/videos/",
            "/oauth2/",
            "/login/oauth2/",
            "/h2-console/",
            "/swagger-ui/",
            "/swagger-ui.html",
            "/v3/api-docs/",
            "/swagger-resources/",
            "/webjars/"
    );

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

        if (!DispatcherType.REQUEST.equals(request.getDispatcherType())) {
            log.info("⏭️ [JWT Filter] Skipping non-REQUEST dispatch: {}", dispatchType);
            filterChain.doFilter(request, response);
            return;
        }

        if (shouldSkipJwtValidation(requestURI)) {
            log.info("🔓 [JWT Filter] Skipping JWT validation for path: {}", requestURI);
            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            log.info("🔍 [JWT Filter] No JWT token found. Passing to next filter.");
            filterChain.doFilter(request, response);
            return;
        }

        log.info("🔍 [JWT Filter] Authorization Header: exists");

        try {
            String token = authHeader.substring(7);
            log.info("🔍 [JWT Filter] Token extracted (length: {})", token.length());

            if (jwtTokenProvider.validateToken(token)) {
                String tokenType = jwtTokenProvider.getTokenType(token);
                log.info("🔍 [JWT Filter] Token type: {}", tokenType);

                if ("access".equals(tokenType)) {
                    Authentication authentication = jwtTokenProvider.getAuthentication(token);
                    SecurityContextHolder.getContext().setAuthentication(authentication);

                    log.info(" [JWT Filter] Authentication set - Principal: {}, Authorities: {}",
                            authentication.getPrincipal(),
                            authentication.getAuthorities());
                } else {
                    log.warn(" [JWT Filter] Not an access token: {}", tokenType);
                }
            } else {
                log.warn("❌ [JWT Filter] Invalid token");
            }

        } catch (Exception e) {
            log.error("❌ [JWT Filter] Error processing token: {}", e.getMessage());
        }

        filterChain.doFilter(request, response);
    }

    private boolean shouldSkipJwtValidation(String uri) {
        return SKIP_PATHS.stream().anyMatch(uri::startsWith);
    }
}
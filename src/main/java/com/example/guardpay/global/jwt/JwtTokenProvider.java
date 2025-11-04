package com.example.guardpay.global.jwt;

import com.example.guardpay.domain.member.entity.Member;
import com.example.guardpay.domain.member.repository.MemberRepository;
import com.example.guardpay.global.exception.MemberNotFoundException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.*;

@Slf4j
@Component
public class JwtTokenProvider {

    public static final String CLAIM_TYPE = "type";   // "access" | "refresh"
    public static final String CLAIM_ROLE = "role";   // "ROLE_USER" 등

    private final MemberRepository memberRepository;

    private final Key key;
    private final long accessTokenValidityInMs;
    private final long refreshTokenValidityInMs;
    private final long clockSkewSeconds;

    public JwtTokenProvider(
            MemberRepository memberRepository,
            @Value("${jwt.secret-key}") String secretKeyProp,
            @Value("${jwt.access-token-validity-in-seconds}") long accessSec,
            @Value("${jwt.refresh-token-validity-in-seconds}") long refreshSec,
            @Value("${jwt.clock-skew-seconds:60}") long clockSkewSeconds
    ) {
        this.memberRepository = memberRepository;
        this.key = buildSigningKey(secretKeyProp);
        this.accessTokenValidityInMs = accessSec * 1000L;
        this.refreshTokenValidityInMs = refreshSec * 1000L;
        this.clockSkewSeconds = clockSkewSeconds;
    }

    private Key buildSigningKey(String secret) {
        String s = secret == null ? "" : secret.trim();
        try {
            byte[] keyBytes = io.jsonwebtoken.io.Decoders.BASE64.decode(s);
            return io.jsonwebtoken.security.Keys.hmacShaKeyFor(keyBytes);
        } catch (io.jsonwebtoken.io.DecodingException | IllegalArgumentException e) {
            byte[] keyBytes = s.getBytes(java.nio.charset.StandardCharsets.UTF_8);
            if (keyBytes.length < 32) {
                throw new IllegalArgumentException(
                        "JWT secret too short. Need >= 32 bytes (256 bits). " +
                                "Use a Base64-encoded 32+ byte key or provide a longer raw secret."
                );
            }
            return io.jsonwebtoken.security.Keys.hmacShaKeyFor(keyBytes);
        }
    }

    // ---------- 발급 ----------
    public String createAccessToken(Long memberId, String role) {
        String token = createToken(String.valueOf(memberId), accessTokenValidityInMs, new HashMap<String, Object>() {{
            put(CLAIM_TYPE, "access");
            put(CLAIM_ROLE, role);
        }});
        log.info("✅ [JWT] Access token created for memberId: {}, role: {}", memberId, role);
        return token;
    }

    public String createRefreshToken(Long memberId) {
        String token = createToken(String.valueOf(memberId), refreshTokenValidityInMs, new HashMap<String, Object>() {{
            put(CLAIM_TYPE, "refresh");
        }});
        log.info("✅ [JWT] Refresh token created for memberId: {}", memberId);
        return token;
    }

    private String createToken(String subject, long validityInMs, Map<String, Object> extraClaims) {
        Date now = new Date();
        Date exp = new Date(now.getTime() + validityInMs);

        JwtBuilder builder = Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(now)
                .setExpiration(exp)
                .signWith(key, SignatureAlgorithm.HS256);

        if (extraClaims != null && !extraClaims.isEmpty()) {
            builder.addClaims(extraClaims);
        }
        return builder.compact();
    }

    // ---------- 검증/파싱 ----------
    private Claims parseClaims(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .setAllowedClockSkewSeconds(clockSkewSeconds)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            log.info("📝 [JWT] Token parsed - subject: {}, type: {}, exp: {}",
                    claims.getSubject(),
                    claims.get(CLAIM_TYPE),
                    claims.getExpiration());
            return claims;

        } catch (ExpiredJwtException e) {
            log.warn("❌ [JWT] Token expired: {}", e.getMessage());
            throw e;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.warn("❌ [JWT] Invalid signature/malformed: {}", e.getMessage());
            throw e;
        } catch (UnsupportedJwtException e) {
            log.warn("❌ [JWT] Unsupported: {}", e.getMessage());
            throw e;
        } catch (IllegalArgumentException e) {
            log.warn("❌ [JWT] Illegal argument: {}", e.getMessage());
            throw e;
        }
    }

    public boolean validateToken(String token) {
        try {
            parseClaims(token);
            log.info("✅ [JWT] Token validation SUCCESS");
            return true;
        } catch (JwtException ex) {
            log.warn("⚠️ [JWT] Token validation FAILED: {}", ex.getMessage());
            return false;
        }
    }

    public Long getMemberId(String token) {
        Long memberId = Long.valueOf(parseClaims(token).getSubject());
        log.info("🔍 [JWT] Extracted memberId: {}", memberId);
        return memberId;
    }

    public String getTokenType(String token) {
        Object t = parseClaims(token).get(CLAIM_TYPE);
        String type = (t == null) ? null : t.toString();
        log.info("🔍 [JWT] Token type: {}", type);
        return type;
    }

    public String getRole(String token) {
        Object r = parseClaims(token).get(CLAIM_ROLE);
        String role = (r == null) ? null : r.toString();
        log.info("🔍 [JWT] Token role: {}", role);
        return role;
    }

    public Authentication getAuthentication(String token) {
        Long memberId = getMemberId(token);
        log.info("🔍 [JWT] getAuthentication for memberId: {}", memberId);

        Member m = memberRepository.findById(memberId)
                .orElseThrow(() -> {
                    log.error("❌ [JWT] Member not found: {}", memberId);
                    return new MemberNotFoundException();
                });

        String role = m.getRole();
        log.info("🔍 [JWT] Member role from DB: {}", role);

        if (role != null && !role.startsWith("ROLE_")) {
            role = "ROLE_" + role;
        }

        var authorities = List.of(new SimpleGrantedAuthority(
                role == null ? "ROLE_USER" : role
        ));

        var principal = new org.springframework.security.core.userdetails.User(
                String.valueOf(m.getMemberId()), "", authorities);

        log.info("✅ [JWT] Authentication created - principal: {}, authorities: {}",
                principal.getUsername(), authorities);

        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }
}
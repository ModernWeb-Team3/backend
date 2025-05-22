package kr.unideal.server.backend.config.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import kr.unideal.server.backend.domain.user.dto.CustomUserDetails;
import kr.unideal.server.backend.domain.user.entity.User;
import kr.unideal.server.backend.domain.user.exception.CustomJWTException;
import kr.unideal.server.backend.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.crypto.SecretKey;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    @Value("${jwt.token.secretKey}")
    private String key;
    private SecretKey secretKey;
    private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 30L;
    private static final long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 60L * 24 * 7;
    private final UserRepository userRepository;

    @PostConstruct
    private void setSecretKey() {
        if (key == null || key.length() < 32) {
            throw new IllegalArgumentException("JWT 키가 너무 짧습니다. 최소 32바이트 이상이어야 합니다.");
        }
        secretKey = Keys.hmacShaKeyFor(key.getBytes());
    }


    /// AccessToken 만들기
    public String generateAccessToken(Authentication authentication) {
        return generateToken(authentication, ACCESS_TOKEN_EXPIRE_TIME);
    }


    /// Refresh token 발급
    public void generateRefreshToken(Authentication authentication, String accessToken) {
        String refreshToken = generateToken(authentication, REFRESH_TOKEN_EXPIRE_TIME);
    }

    ///
    public String generateToken(Authentication authentication, long expireTime) {
        Date now = new Date();
        Date expiredDate = new Date(now.getTime() + expireTime);

        List<String> authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        // roles를 단일 문자열로 저장
        String roles = String.join(",", authorities);

        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", roles);
        claims.put("userId", customUserDetails.getId());

        return Jwts.builder()
                .setSubject(authentication.getName())
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiredDate)
                .signWith(secretKey, SignatureAlgorithm.HS512)
                .compact();
    }

    ///
    public Authentication getAuthentication(String token) {
        Claims claims = parseClaims(token);
        String rolesStr = claims.get("roles", String.class);
        List<String> roles = Arrays.asList(rolesStr.split(","));

        Collection<? extends GrantedAuthority> authorities = roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        Long userId = claims.get("userId", Long.class);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AccessDeniedException("유효하지 않은 사용자 ID: " + userId));

        CustomUserDetails userDetails = new CustomUserDetails(user);
        return new UsernamePasswordAuthenticationToken(userDetails, token, authorities);
    }


    public boolean validateToken(String token) {
        if (!StringUtils.hasText(token)) {
            return false;
        }

        try {
            Claims claims = parseClaims(token);
            return claims.getExpiration().after(new Date());
        } catch (ExpiredJwtException e) {
            log.warn("⚠️ JWT 토큰 만료됨: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            log.warn("⚠️ 잘못된 JWT 형식: {}", e.getMessage());
        } catch (Exception e) {
            log.warn("⚠️ JWT 검증 실패: {}", e.getMessage());
        }
        return false;
    }


    /// 토큰 Parse 하기
    private Claims parseClaims(String token) {
        try {
            // JWT 파서를 빌드하고 서명된 토큰을 파싱
            return Jwts.parserBuilder()
                    .setSigningKey(secretKey)  // 서명 키를 설정
                    .build()
                    .parseClaimsJws(token)  // 서명된 JWT 토큰을 파싱
                    .getBody();  // Claims 객체 반환
        } catch (ExpiredJwtException e) {
            // 만료된 JWT 토큰의 경우 만료된 Claims 반환
            return e.getClaims();
        } catch (MalformedJwtException e) {
            // 잘못된 JWT 토큰 형식일 경우 예외 처리
            throw new CustomJWTException("잘못된 토큰 형식입니다.");
        }
    }

}

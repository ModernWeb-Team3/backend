package kr.unideal.server.backend.security.util;

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

import org.springframework.util.StringUtils;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    @Value("${jwt.token.secretKey}")
    private String key; // ✔️ 문자열로 주입
    private final RedisUtil redisUtil;

    private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 30*30L;
    private static final long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 60L * 24 * 7;
    private final UserRepository userRepository;

    private SecretKey secretKey;

    @PostConstruct
    private void setSecretKey() {
        if (key == null || key.isBlank()) {
            throw new IllegalArgumentException("jwt.token.secretKey 값이 비어 있습니다.");
        }
        byte[] keyBytes = io.jsonwebtoken.io.Decoders.BASE64.decode(key);
        this.secretKey = Keys.hmacShaKeyFor(keyBytes);
    }

    /// AccessToken 만들기
    public String generateAccessToken(Authentication authentication) {
        return generateToken(authentication, ACCESS_TOKEN_EXPIRE_TIME);
    }


    /// Refresh token 발급
    public String generateRefreshToken(Authentication authentication) {
        // 1) 인증 정보에서 사용자 ID 꺼내기
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Long userId = userDetails.getId();

        // 2) 토큰 생성
        String refreshToken = generateToken(authentication, REFRESH_TOKEN_EXPIRE_TIME);

        // 3) Redis에 userId를 키로 해서 저장
        redisUtil.setRefreshToken(userId, refreshToken);

        // 4) 생성된 토큰 반환
        return refreshToken;
    }


    /// 토큰 생성
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


    public Authentication getAuthentication(String token) {
        Claims claims = parseClaims(token);

        // 1) roles 클레임 읽기 (없으면 빈 문자열)
        String rolesStr = claims.get("roles", String.class);

        // 2) 빈 문자열은 필터링
        List<String> roles = StringUtils.hasText(rolesStr)
                ? Arrays.stream(rolesStr.split(","))
                .filter(StringUtils::hasText)        // 빈 문자열 필터
                .collect(Collectors.toList())
                : Collections.emptyList();

        // 3) SimpleGrantedAuthority 생성
        Collection<? extends GrantedAuthority> authorities = roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        // 4) userId 로 유저 조회
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

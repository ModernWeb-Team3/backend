package kr.unideal.server.backend.security.util;


import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.unideal.server.backend.global.exception.CustomException;
import kr.unideal.server.backend.global.exception.ErrorCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class CookieUtil {

    @Value("${jwt.cookieMaxAge}")
    private Long cookieMaxAge;

    @Value("${jwt.secureOption}")
    private boolean secureOption;

    @Value("${jwt.cookiePathOption}")
    private String cookiePathOption;

    // 쿠키 설정
    public void setCookie(String refreshToken, HttpServletResponse response) {
        ResponseCookie cookie = ResponseCookie.from("refreshToken", refreshToken) // name, value 순서
                .maxAge(cookieMaxAge)
                .path(cookiePathOption)
                .secure(true)
                .httpOnly(true)
                .sameSite("None")
                .build();

        response.setHeader("Set-Cookie", cookie.toString());
    }


    // 쿠키 가져오기
    public String getRefreshTokenFromCookies(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            throw new CustomException(ErrorCode.TOKEN_NOT_FOUND);
        }

        for (Cookie cookie : cookies) {
            if ("refreshToken".equals(cookie.getName())) {
                return cookie.getValue();
            }
        }

        throw new CustomException(ErrorCode.TOKEN_NOT_FOUND); // refreshToken 쿠키가 없을 경우
    }

    // 쿠키 삭제
    public void deleteCookie(HttpServletResponse response, UUID userId) {
        ResponseCookie cookie = ResponseCookie.from(String.valueOf(userId), "value")
                .maxAge(0) // 즉시만료
                .path("/")
                .secure(true)
                .httpOnly(true)
                .sameSite("None")
                .build();

        response.setHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }

}

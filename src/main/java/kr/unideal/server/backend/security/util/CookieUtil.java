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
    public void setCookie(String userId, HttpServletResponse response) {
        ResponseCookie cookie = ResponseCookie.from(userId)
                .maxAge(cookieMaxAge)
                .path(cookiePathOption)
                .secure(secureOption) //https 적용 시 true
                .httpOnly(true)
                .sameSite("None")
                .build();
        response.setHeader("Set-Cookie", cookie.toString());
    }

    // 쿠키 가져오기
    private static Cookie[] getCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();

        if (cookies == null) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND_IN_COOKIE);
        }
        return cookies;
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

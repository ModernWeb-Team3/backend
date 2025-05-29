package kr.unideal.server.backend.security.util;


import kr.unideal.server.backend.global.exception.CustomException;
import kr.unideal.server.backend.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class RedisUtil {

    @Value("${jwt.refresh.expiration}")
    private Long refreshTokenExpiration;

    private final static String TOKEN_FORMAT = "refreshToken:%s";
    private final RedisTemplate<String, String> redisTemplate;


    public void setRefreshToken(Long id, String value) {
        String key = String.format(TOKEN_FORMAT, id);
        redisTemplate.opsForValue().set(key, value, refreshTokenExpiration, TimeUnit.MILLISECONDS);
    }

    public Object getRefreshToken(UUID id) {
        String key = String.format(TOKEN_FORMAT, id);
        Object getObjecet = redisTemplate.opsForValue().get(key);

        if (getObjecet == null) {
            throw new CustomException(ErrorCode.INVALID_REFRESH_TOKEN);
        }


        return getObjecet;
    }

    public boolean hasRefreshTokenKey(Long id) {
        String key = String.format(TOKEN_FORMAT, id);
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }

    public boolean deleteRefreshToken(Long id) {
        String key = String.format(TOKEN_FORMAT, id);
        return Boolean.TRUE.equals(redisTemplate.delete(key));
    }

}

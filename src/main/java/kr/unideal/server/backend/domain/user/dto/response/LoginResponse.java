package kr.unideal.server.backend.domain.user.dto.response;

import lombok.Builder;

@Builder
public class LoginResponse {

    private String email;
    private String jwtToken;

    public static LoginResponse from(String email, String jwtToken) {
        return LoginResponse.builder()
                .email(email)
                .jwtToken(jwtToken)
                .build();
    }
}

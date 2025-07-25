package kr.unideal.server.backend.domain.user.dto.response;

import lombok.Getter;

@Getter
public class VerifyResponseDTO {
    private String email;
    private String code;

    public VerifyResponseDTO(String email, String code) {
        this.email = email;
        this.code = code;
    }

    public static VerifyResponseDTO from(String email, String code) {

        return new VerifyResponseDTO(email, code);
    }
}

package kr.unideal.server.backend.domain.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class EmailVerifyRequest {

    @Schema(description = "이메일", example = "example@gmail.com")
    private String email;

    @Schema(description = "인증번호 코드", example = "1234@a")
    private String code;
}

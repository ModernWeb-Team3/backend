package kr.unideal.server.backend.domain.user.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

public class VerifyRequestDTO {
    @Getter
    @Setter

    @NotBlank
    @Email
    String email;

    @Getter
    @Setter
    @NotBlank
    String code;
}

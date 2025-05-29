package kr.unideal.server.backend.domain.user.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VerifyRequestDTO {

    @NotBlank
    @Email
    String email;

    @NotBlank
    String code;
}

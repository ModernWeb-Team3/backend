package kr.unideal.server.backend.domain.user.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LogInRequestDTO {
    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String password;
}

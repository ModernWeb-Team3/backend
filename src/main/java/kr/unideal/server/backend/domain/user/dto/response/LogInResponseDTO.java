package kr.unideal.server.backend.domain.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class LogInResponseDTO {

    private String email;
    private String jwtToken;

    public static LogInResponseDTO from(String email, String jwtToken) {
        return LogInResponseDTO.builder()
                .email(email)
                .jwtToken(jwtToken)
                .build();
    }

}

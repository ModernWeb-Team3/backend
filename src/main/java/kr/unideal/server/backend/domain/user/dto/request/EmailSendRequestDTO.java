package kr.unideal.server.backend.domain.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class EmailSendRequestDTO {

    @Schema(description = "이메일", example = "example@gmail.com")
    private String email;
}

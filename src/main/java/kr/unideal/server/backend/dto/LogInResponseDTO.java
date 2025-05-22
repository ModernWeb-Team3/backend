package kr.unideal.server.backend.dto;

public class LogInResponseDTO {
    int memberId;
    String accessToken;

    public LogInResponseDTO(int memberId, String accessToken)
    {
        this.memberId = memberId;
        this.accessToken = accessToken;
    }
}
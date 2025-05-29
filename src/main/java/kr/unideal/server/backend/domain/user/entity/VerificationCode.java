package kr.unideal.server.backend.domain.user.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VerificationCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    private String email;

    private String code;

    private LocalDateTime expiresTime;

    public static VerificationCode of(String email, String code, LocalDateTime expiresTime) {
        return VerificationCode.builder()
                .email(email)
                .code(code)
                .expiresTime(expiresTime)
                .build();
    }
}

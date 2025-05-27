package kr.unideal.server.backend.domain.user.entity;

import jakarta.persistence.*;
import kr.unideal.server.backend.domain.user.dto.request.SignUpRequestDTO;
import kr.unideal.server.backend.domain.user.dto.response.LogInResponseDTO;
import lombok.*;

@Entity
@Table(name = "`user`") // user는 예약어이므로 백틱 사용
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, length = 100)
    private String email;

    @Column(length = 255)
    private String password;

    @Column(length = 100)
    private String name;

    @Column
    private boolean isVerified = false;



    public static User of(SignUpRequestDTO request) {
        return User.builder()
                .email(request.getEmail())
                .password(request.getPassword())
                .name(request.getName())
                .build();
    }

    public void setVerified(boolean b) {
        this.isVerified = b;
    }

//    public LogInResponseDTO from(User user) {
//        return LogInResponseDTO.builder()
//                .email(user.getEmail())
//                .jwtToken(user.getVerificationToken())
//                .build();
//    }
}

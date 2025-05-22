package kr.unideal.server.backend.entity;

import jakarta.persistence.*;
import kr.unideal.server.backend.utils.VerificationCodeUtils;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Instant;

@Entity
@Table(name = "`user`") // user는 예약어이므로 백틱 사용
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(nullable = false, length = 255)
    private String password;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false)
    private boolean isVerified = false;

    @Column
    private String verificationToken;
}
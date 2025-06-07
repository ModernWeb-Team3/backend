package kr.unideal.server.backend.global.config;

import kr.unideal.server.backend.domain.user.entity.User;
import kr.unideal.server.backend.domain.user.repository.UserRepository;
import kr.unideal.server.backend.security.util.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Collections;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class DataInitializer {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Bean
    public CommandLineRunner initAdmin() {
        return args -> {
            String adminEmail = "admin@gachon.ac.kr";
            if (userRepository.findByEmail(adminEmail).isEmpty()) {
                // 1. 관리자 생성
                User admin = User.builder()
                        .name("관리자")
                        .email(adminEmail)
                        .password("admin") // 초기 비번 (운영에선 사용 X)
                        .isVerified(true)
                        .build();

                userRepository.save(admin);

                // 2. 토큰 생성
                Long adminId = admin.getId();
                String accessToken = jwtTokenProvider.generateMasterToken(
                        adminId,
                        adminEmail,
                        Collections.singletonList("ROLE_ADMIN") // 권한 부여
                );

                // ✅ 로그로 확인
                log.info("✅ Admin 계정이 생성되었습니다: {}", adminEmail);
                log.info("🔑 Admin Access Token: {}", accessToken);
                // refreshToken은 필요 시 추가로 발급 가능
            } else {
                log.info("ℹ️ 이미 admin 계정이 존재합니다: {}", adminEmail);
            }
        };
    }
}

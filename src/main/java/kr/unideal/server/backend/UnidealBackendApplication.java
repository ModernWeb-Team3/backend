package kr.unideal.server.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing  // Auditing 활성화
public class UnidealBackendApplication {
    public static void main(String[] args) {
        SpringApplication.run(UnidealBackendApplication.class, args);
    }
}

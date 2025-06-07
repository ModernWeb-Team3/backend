package kr.unideal.server.backend.openapi;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(
                title = "Unideal API 명세서",
                description = "Unideal의 API 명세서입니다.",
                version = "v1"
        ),
        security = @SecurityRequirement(name = "bearerAuth") // 👈 모든 API에 기본 적용
)
@SecurityScheme(
        name = "bearerAuth",                // 👈 위 SecurityRequirement와 일치해야 함
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT"               // 👈 표기용, 실제 동작과는 무관
)
@Configuration
public class OpenAPIConfig {
}

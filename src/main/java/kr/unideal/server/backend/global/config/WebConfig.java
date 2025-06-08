package kr.unideal.server.backend.global.config;


import kr.unideal.server.backend.domain.user.aop.CurrentUserIdArgumentResolver;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final CurrentUserIdArgumentResolver currentUserIdArgumentResolver;

    public WebConfig(CurrentUserIdArgumentResolver currentUserIdArgumentResolver) {
        this.currentUserIdArgumentResolver = currentUserIdArgumentResolver;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(currentUserIdArgumentResolver);
    }

//
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/**")
//                .allowedOriginPatterns(
//                        "http://localhost:3000",
//                        "http://localhost:8080",
//                        "https://staging-api.unideal.kr",
//                        "https://staging.unideal.kr",
//                        "https://staging-front.unideal.kr",
//                        "https://unideal.kr",
//                        "https://front.unideal.kr"
//                )
//                .allowedMethods("GET", "POST","PATCH", "PUT", "DELETE", "OPTIONS")
//                .allowedHeaders("*")
//                .allowCredentials(true)
//                .maxAge(3600);
//    }


}

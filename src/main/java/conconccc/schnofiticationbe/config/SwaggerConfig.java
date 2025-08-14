package conconccc.schnofiticationbe.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("학교 알림 시스템 API")
                        .description("학교 알림 시스템 백엔드 API 문서입니다.")
                        .version("v1.0.0")
                        .contact(new Contact()
                                .name("개발팀")
                                .email("dev@school.com")));
    }
}

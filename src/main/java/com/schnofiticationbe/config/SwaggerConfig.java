package com.schnofiticationbe.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springdoc.core.customizers.PropertyCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("순리미 API")
                        .description("순리미 백엔드 API 문서입니다.")
                        .version("v1.0.0")
                        .contact(new Contact()
                                .name("개발팀")
                                .email("dev@school.com")));
    }

    @Bean
    public PropertyCustomizer disableNullableCustomizer() {
        return (schema, type) -> {
            schema.setNullable(false);
            return schema;
        };
    }
}

package com.schnofiticationbe.config;

//import com.schnofiticationbe.config.interceptor.LoggingInterceptor;
import com.schnofiticationbe.config.interceptor.LoggingInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.HandlerTypePredicate;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.context.annotation.Bean;
import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final LoggingInterceptor loggingInterceptor;

    @Bean
    public MultipartJackson2HttpMessageConverter multipartJackson2HttpMessageConverter(ObjectMapper objectMapper) {
        return new MultipartJackson2HttpMessageConverter(objectMapper);
    }


    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:uploads/");
    }

    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000", "https://notification.iubns.net/")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowCredentials(true);
    }

    @Override
    public void configurePathMatch(PathMatchConfigurer configurer){
        configurer.addPathPrefix("/api", HandlerTypePredicate.forAnnotation(RestController.class));
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry){
        registry.addInterceptor(loggingInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/error");
    }
}

package com.schnofiticationbe.security;


import org.springframework.context.annotation.Bean;
import org.springframework.beans.factory.annotation.Autowired;
import com.schnofiticationbe.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@EnableAsync
@Configuration
public class SecurityConfig {
    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .httpBasic(httpBasic -> httpBasic.disable())
                .formLogin(formLogin -> formLogin.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/admin/**").permitAll()
                        .requestMatchers("/api/notice/**").permitAll()
                        .requestMatchers("/api/department/**").permitAll()
                        .requestMatchers("/api/boards/**").permitAll()
                        .requestMatchers("/api/calenders/**").permitAll()
                        .requestMatchers("/api/swagger-ui/**", "/api/api-docs").permitAll()
                        .requestMatchers("/api/health/**").permitAll()
                        .requestMatchers("/api/subscribe/**").permitAll()
                        .requestMatchers("/api/kakao/**").permitAll()
                        .requestMatchers("/api/keyword/**").permitAll()
                        .requestMatchers("/error").permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter, org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

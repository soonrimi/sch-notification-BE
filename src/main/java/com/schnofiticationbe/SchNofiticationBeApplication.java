package com.schnofiticationbe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync // 로그 비동기 처리를 위해 활성화
@SpringBootApplication
@EnableJpaAuditing // createdAt, updatedAt 자동 관리 활성화
public class SchNofiticationBeApplication { 

    public static void main(String[] args) {
        SpringApplication.run(SchNofiticationBeApplication.class, args);
    }

}

package com.schnofiticationbe.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@EnableAsync
@Configuration
public class AsyncConfig {

    @Bean(name = "taskExecutor")
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10); // 기본 스레드 수
        executor.setMaxPoolSize(50);  // 최대 스레드 수 (스파이크 트래픽 방어)
        executor.setQueueCapacity(100); // 스레드가 꽉 차면 최대 100개까지 대기열 버퍼링
        executor.setThreadNamePrefix("Async-");
        executor.initialize();
        return executor;
    }
}

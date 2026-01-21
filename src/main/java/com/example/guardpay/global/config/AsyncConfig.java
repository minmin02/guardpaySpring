package com.example.guardpay.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.task.DelegatingSecurityContextAsyncTaskExecutor;

@Configuration
@EnableAsync // 비동기 기능을 활성화합니다.
public class AsyncConfig {

    /**
     * @Async 어노테이션이 사용할 기본 스레드 풀 Executor를 정의합니다.
     * SecurityContext를 전파하기 위해 DelegatingSecurityContextAsyncTaskExecutor로 래합니다.
     */
    @Bean
    public TaskExecutor taskExecutor() {
        // 1. 실제 작업을 수행할 스레드 풀을 생성합니다.
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);   // 기본 스레드 수
        executor.setMaxPoolSize(10);  // 최대 스레드 수
        executor.setQueueCapacity(25); // 대기 큐 크기
        executor.setThreadNamePrefix("async-worker-");
        executor.initialize();

        // 2.  SecurityContext를 비동기 스레드로 전파하는 래퍼로 감쌉니다.
        return new DelegatingSecurityContextAsyncTaskExecutor(executor);
    }
}

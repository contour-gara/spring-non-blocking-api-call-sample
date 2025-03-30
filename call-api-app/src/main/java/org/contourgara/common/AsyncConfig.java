package org.contourgara.common;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class AsyncConfig {
    @Bean
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(3);
        executor.setQueueCapacity(10);
        executor.setMaxPoolSize(500);
        executor.setThreadNamePrefix("async-");

        // Request スコープの Bean を子スレッドで使用できるようになる
        executor.setTaskDecorator(task -> {
            RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
            return () -> {
                try {
                    if (attributes != null) {
                        RequestContextHolder.setRequestAttributes(attributes);
                    }
                    task.run();
                } finally {
                    RequestContextHolder.resetRequestAttributes();
                }
            };
        });

        executor.initialize();
        return executor;
    }
}

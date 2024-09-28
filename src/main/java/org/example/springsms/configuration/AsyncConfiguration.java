package org.example.springsms.configuration;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class AsyncConfiguration implements AsyncConfigurer {

    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(Runtime.getRuntime().availableProcessors());
        executor.setCorePoolSize(2*Runtime.getRuntime().availableProcessors());
        executor.setQueueCapacity(500);  // İşlemlerin kuyruğa alınacağı kapasite
        executor.setThreadNamePrefix("AsyncThread-");  // Thread ismi
        executor.initialize();
        return executor;  // Özelleştirilmiş ThreadPoolTaskExecutor döndür
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        // Asenkron metotlarda meydana gelen yakalanmamış hataları yönetecek handler
        return (throwable, method, obj) -> {
            System.out.println("Asenkron hata: " + throwable.getMessage());
        };
    }
}

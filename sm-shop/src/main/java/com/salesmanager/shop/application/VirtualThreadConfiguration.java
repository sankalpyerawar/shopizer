package com.salesmanager.shop.application;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * Async executor configuration optimized for Java 21 Virtual Threads.
 * When running on Java 21+, uses virtual threads for better resource utilization.
 * Falls back to optimized thread pool on Java 17.
 */
@Configuration
@EnableAsync
public class VirtualThreadConfiguration implements AsyncConfigurer {

    @Override
    public Executor getAsyncExecutor() {
        // Check Java version at runtime
        int javaVersion = Runtime.version().feature();
        
        if (javaVersion >= 21) {
            // Java 21+: Use virtual threads via reflection to maintain Java 17 compatibility
            try {
                var method = Executors.class.getMethod("newVirtualThreadPerTaskExecutor");
                return (Executor) method.invoke(null);
            } catch (Exception e) {
                System.err.println("Failed to create virtual thread executor, falling back to thread pool: " + e.getMessage());
            }
        }
        
        // Java 17: Use optimized thread pool
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10);
        executor.setMaxPoolSize(50);
        executor.setQueueCapacity(100);
        executor.setThreadNamePrefix("async-");
        executor.initialize();
        return executor;
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return (ex, method, params) -> 
            System.err.println("Async exception in " + method.getName() + ": " + ex.getMessage());
    }
}

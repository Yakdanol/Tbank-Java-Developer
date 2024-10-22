package org.yakdanol.task8.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;

import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;

@Configuration
public class ThreadPoolConfig {

    @Value("${fixed.threadpool.size}")
    private int fixedThreadPoolSize;

    @Bean(name = "fixedThreadPool")
    public ExecutorService fixedThreadPool() {
        return Executors.newFixedThreadPool(fixedThreadPoolSize, runnable -> {
            Thread thread = new Thread(runnable);
            thread.setName("FixedPoolThread");
            return thread;
        });
    }

    @Bean(name = "scheduledThreadPool")
    public ExecutorService scheduledThreadPool() {
        return Executors.newScheduledThreadPool(2, runnable -> {
            Thread thread = new Thread(runnable);
            thread.setName("ScheduledPoolThread");
            return thread;
        });
    }
}

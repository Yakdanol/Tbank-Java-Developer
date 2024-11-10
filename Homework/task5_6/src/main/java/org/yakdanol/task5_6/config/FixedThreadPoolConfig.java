package org.yakdanol.task5_6.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;

import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;

@Configuration
public class FixedThreadPoolConfig {

    @Value("${fixed.threadpool.size}")
    private int fixedThreadPoolSize;

    @Bean(name = "fixedThreadPoolKudagoInitializer")
    public ExecutorService fixedThreadPool() {
        return Executors.newFixedThreadPool(fixedThreadPoolSize, runnable -> {
            Thread thread = new Thread(runnable);
            thread.setName("FixedPoolThread");
            return thread;
        });
    }
}
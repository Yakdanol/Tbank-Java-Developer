package org.yakdanol.task7.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Slf4j
@Configuration
@EnableCaching
public class AppConfig {

    @Bean
    public CacheManager hourlyCache() {
        log.info("Initializing hourly cache manager with Caffeine");
        CaffeineCacheManager cacheManager = new CaffeineCacheManager("currencyRates");
        cacheManager.setCaffeine(Caffeine.newBuilder()
                .expireAfterWrite(1, TimeUnit.HOURS)); // Данные будут истекать через 1 час
        return cacheManager;
    }
}

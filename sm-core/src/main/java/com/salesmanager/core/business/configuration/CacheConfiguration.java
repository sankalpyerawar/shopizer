package com.salesmanager.core.business.configuration;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Cache configuration for Spring Boot 3
 * Replaces XML-based ehcache configuration with simple in-memory caching
 */
@Configuration
@EnableCaching
public class CacheConfiguration {

    @Bean
    public CacheManager serviceCacheManager() {
        return new ConcurrentMapCacheManager("com.shopizer.OBJECT_CACHE");
    }
}

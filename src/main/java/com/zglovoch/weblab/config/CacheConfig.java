package com.zglovoch.weblab.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

/**
 * Конфигурация кэширования на базе Caffeine.
 *
 * Кэши:
 *  - menuItems        — все позиции меню
 *  - menuCategories   — список уникальных категорий
 *  - menuByCategory   — позиции по конкретной категории
 *  - galleryItems     — все фото галереи
 *  - dynamicPages     — видимые динамические страницы (список в навбаре)
 *
 * Настройки: максимум 500 записей, TTL = 10 минут,
 *            запись статистики попаданий/промахов.
 */
@Configuration
@EnableCaching
public class CacheConfig {

    @Bean
    public CacheManager cacheManager() {
        CaffeineCacheManager manager = new CaffeineCacheManager(
                "menuItems",
                "menuCategories",
                "menuByCategory",
                "galleryItems",
                "dynamicPages"
        );
        manager.setCaffeine(
                Caffeine.newBuilder()
                        .maximumSize(500)
                        .expireAfterWrite(Duration.ofMinutes(10))
                        .recordStats()           // позволяет смотреть hit/miss через CacheStats
        );
        return manager;
    }
}

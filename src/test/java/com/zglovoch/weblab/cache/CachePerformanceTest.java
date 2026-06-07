package com.zglovoch.weblab.cache;

import com.zglovoch.weblab.model.MenuItem;
import com.zglovoch.weblab.service.GalleryService;
import com.zglovoch.weblab.service.MenuService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCache;

import com.github.benmanes.caffeine.cache.stats.CacheStats;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Тест производительности кэширования.
 *
 * Проверяет:
 *  1. Второй вызов значительно быстрее первого (кэш hit).
 *  2. Статистика Caffeine фиксирует 1 miss и 1 hit.
 *  3. Оба вызова возвращают одинаковый результат.
 */
@SpringBootTest
class CachePerformanceTest {

    @Autowired
    private MenuService menuService;

    @Autowired
    private GalleryService galleryService;

    @Autowired
    private CacheManager cacheManager;

    @Test
    @DisplayName("Второй вызов menuService.findAll() обслуживается из кэша (быстрее DB)")
    void menuFindAll_secondCallIsCached() {
        // Сбрасываем кэш, чтобы тест был воспроизводимым
        cacheManager.getCache("menuItems").clear();

        // --- Первый вызов: идёт в базу данных ---
        long t0 = System.nanoTime();
        List<MenuItem> first = menuService.findAll();
        long timeDb = System.nanoTime() - t0;

        // --- Второй вызов: должен вернуться из Caffeine-кэша ---
        long t1 = System.nanoTime();
        List<MenuItem> second = menuService.findAll();
        long timeCache = System.nanoTime() - t1;

        // Оба вызова возвращают одинаковые данные
        assertThat(second).hasSameSizeAs(first);

        // Второй вызов быстрее первого
        System.out.printf(
                "%n[CachePerformanceTest] menuItems:%n" +
                "  1-й вызов (DB)    : %,d мкс%n" +
                "  2-й вызов (cache) : %,d мкс%n" +
                "  Ускорение         : %.1fx%n",
                timeDb / 1_000,
                timeCache / 1_000,
                timeDb > 0 ? (double) timeDb / Math.max(timeCache, 1) : 0
        );

        assertThat(timeCache).isLessThan(timeDb);
    }

    @Test
    @DisplayName("Caffeine фиксирует 1 miss и ≥1 hit для menuByCategory")
    void menuByCategory_caffeineStatsShowHit() {
        // Сбрасываем кэш
        cacheManager.getCache("menuByCategory").clear();

        // 1-й вызов — промах (miss)
        menuService.findByCategory("Напитки");
        // 2-й вызов — попадание (hit)
        menuService.findByCategory("Напитки");
        // 3-й вызов — ещё одно попадание
        menuService.findByCategory("Напитки");

        // Получаем нативный Caffeine-кэш и его статистику
        Cache springCache = cacheManager.getCache("menuByCategory");
        assertThat(springCache).isInstanceOf(CaffeineCache.class);

        CaffeineCache caffeineCache = (CaffeineCache) springCache;
        CacheStats stats = caffeineCache.getNativeCache().stats();

        System.out.printf(
                "%n[CachePerformanceTest] menuByCategory stats:%n" +
                "  Hits  : %d%n" +
                "  Misses: %d%n" +
                "  Hit rate: %.1f%%%n",
                stats.hitCount(), stats.missCount(),
                stats.hitRate() * 100
        );

        assertThat(stats.missCount()).isEqualTo(1);
        assertThat(stats.hitCount()).isGreaterThanOrEqualTo(2);
    }

    @Test
    @DisplayName("galleryService.findAll() кэшируется — второй вызов быстрее")
    void galleryFindAll_secondCallIsCached() {
        cacheManager.getCache("galleryItems").clear();

        long t0 = System.nanoTime();
        galleryService.findAll();
        long timeDb = System.nanoTime() - t0;

        long t1 = System.nanoTime();
        galleryService.findAll();
        long timeCache = System.nanoTime() - t1;

        System.out.printf(
                "%n[CachePerformanceTest] galleryItems:%n" +
                "  1-й вызов (DB)    : %,d мкс%n" +
                "  2-й вызов (cache) : %,d мкс%n",
                timeDb / 1_000, timeCache / 1_000
        );

        assertThat(timeCache).isLessThan(timeDb);
    }

    @Test
    @DisplayName("После CacheEvict (save) следующий findAll() снова идёт в БД")
    void afterCacheEvict_nextCallHitsDb() {
        cacheManager.getCache("menuItems").clear();

        // Прогрев кэша
        menuService.findAll();

        // Проверяем, что кэш populated
        Cache cache = cacheManager.getCache("menuItems");
        assertThat(cache).isNotNull();

        // Вызываем save — должен сбросить кэш (CacheEvict)
        MenuItem dummy = new MenuItem();
        dummy.setName("Тест-позиция");
        dummy.setCategory("Напитки");
        dummy.setPrice(java.math.BigDecimal.ONE);
        dummy.setAvailable(false);
        MenuItem saved = menuService.save(dummy);

        // После evict кэш пуст — следующий вызов вернёт актуальные данные из БД
        long t0 = System.nanoTime();
        List<MenuItem> afterEvict = menuService.findAll();
        long timeAfterEvict = System.nanoTime() - t0;

        System.out.printf(
                "%n[CachePerformanceTest] after CacheEvict (save):%n" +
                "  Вызов после evict : %,d мкс (идёт в БД)%n",
                timeAfterEvict / 1_000
        );

        // Список содержит сохранённый элемент
        assertThat(afterEvict).anyMatch(m -> m.getName().equals("Тест-позиция"));

        // Cleanup
        menuService.deleteById(saved.getId());
    }
}

package com.zglovoch.weblab.service;

import com.zglovoch.weblab.model.MenuItem;
import com.zglovoch.weblab.repository.MenuItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MenuService {

    private final MenuItemRepository repository;

    /** Все позиции меню — кэшируются на 10 минут. */
    @Cacheable("menuItems")
    public List<MenuItem> findAll() {
        return repository.findAll();
    }

    /** Уникальные категории — кэшируются отдельно. */
    @Cacheable("menuCategories")
    public List<String> findDistinctCategories() {
        return repository.findDistinctCategories();
    }

    /** Позиции конкретной категории — ключ = название категории. */
    @Cacheable(value = "menuByCategory", key = "#category")
    public List<MenuItem> findByCategory(String category) {
        return repository.findByCategoryAndAvailableTrueOrderByName(category);
    }

    public List<MenuItem> findByCategoryAll(String category) {
        return repository.findByCategoryOrderByName(category);
    }

    public Optional<MenuItem> findById(Long id) {
        return repository.findById(id);
    }

    /** При сохранении сбрасываем все кэши меню. */
    @Caching(evict = {
            @CacheEvict(value = "menuItems",       allEntries = true),
            @CacheEvict(value = "menuCategories",  allEntries = true),
            @CacheEvict(value = "menuByCategory",  allEntries = true)
    })
    @Transactional
    public MenuItem save(MenuItem item) {
        return repository.save(item);
    }

    /** При удалении сбрасываем все кэши меню. */
    @Caching(evict = {
            @CacheEvict(value = "menuItems",       allEntries = true),
            @CacheEvict(value = "menuCategories",  allEntries = true),
            @CacheEvict(value = "menuByCategory",  allEntries = true)
    })
    @Transactional
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    public long count() {
        return repository.count();
    }
}

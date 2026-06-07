package com.zglovoch.weblab.service;

import com.zglovoch.weblab.model.DynamicPage;
import com.zglovoch.weblab.repository.DynamicPageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DynamicPageService {

    private final DynamicPageRepository repository;

    public List<DynamicPage> findAll() {
        return repository.findAll();
    }

    /** Видимые страницы (список в навбаре) — кэшируются на 10 минут. */
    @Cacheable("dynamicPages")
    public List<DynamicPage> findVisible() {
        return repository.findByVisibleTrueOrderByCreatedAtAsc();
    }

    public Optional<DynamicPage> findById(Long id) {
        return repository.findById(id);
    }

    public Optional<DynamicPage> findBySlug(String slug) {
        return repository.findBySlug(slug);
    }

    public boolean slugExistsExcept(String slug, Long excludeId) {
        return repository.existsBySlugAndIdNot(slug, excludeId);
    }

    public boolean slugExists(String slug) {
        return repository.existsBySlug(slug);
    }

    @CacheEvict(value = "dynamicPages", allEntries = true)
    @Transactional
    public DynamicPage save(DynamicPage page) {
        return repository.save(page);
    }

    @CacheEvict(value = "dynamicPages", allEntries = true)
    @Transactional
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    public long count() {
        return repository.count();
    }
}

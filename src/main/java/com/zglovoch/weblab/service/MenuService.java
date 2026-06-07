package com.zglovoch.weblab.service;

import com.zglovoch.weblab.model.MenuItem;
import com.zglovoch.weblab.repository.MenuItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MenuService {

    private final MenuItemRepository repository;

    public List<MenuItem> findAll() {
        return repository.findAll();
    }

    public List<String> findDistinctCategories() {
        return repository.findDistinctCategories();
    }

    public List<MenuItem> findByCategory(String category) {
        return repository.findByCategoryAndAvailableTrueOrderByName(category);
    }

    public List<MenuItem> findByCategoryAll(String category) {
        return repository.findByCategoryOrderByName(category);
    }

    public Optional<MenuItem> findById(Long id) {
        return repository.findById(id);
    }

    @Transactional
    public MenuItem save(MenuItem item) {
        return repository.save(item);
    }

    @Transactional
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    public long count() {
        return repository.count();
    }
}

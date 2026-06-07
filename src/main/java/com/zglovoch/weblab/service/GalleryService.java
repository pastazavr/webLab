package com.zglovoch.weblab.service;

import com.zglovoch.weblab.model.GalleryItem;
import com.zglovoch.weblab.repository.GalleryItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GalleryService {

    private final GalleryItemRepository repository;

    public List<GalleryItem> findAll() {
        return repository.findAllByOrderByOrderIndexAscCreatedAtAsc();
    }

    public Optional<GalleryItem> findById(Long id) {
        return repository.findById(id);
    }

    @Transactional
    public GalleryItem save(GalleryItem item) {
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

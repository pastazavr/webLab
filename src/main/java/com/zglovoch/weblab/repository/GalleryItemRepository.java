package com.zglovoch.weblab.repository;

import com.zglovoch.weblab.model.GalleryItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GalleryItemRepository extends JpaRepository<GalleryItem, Long> {
    List<GalleryItem> findAllByOrderByOrderIndexAscCreatedAtAsc();
}

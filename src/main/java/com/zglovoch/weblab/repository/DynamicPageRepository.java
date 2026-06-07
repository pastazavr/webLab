package com.zglovoch.weblab.repository;

import com.zglovoch.weblab.model.DynamicPage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DynamicPageRepository extends JpaRepository<DynamicPage, Long> {
    Optional<DynamicPage> findBySlug(String slug);
    List<DynamicPage> findByVisibleTrueOrderByCreatedAtAsc();
    boolean existsBySlugAndIdNot(String slug, Long id);
    boolean existsBySlug(String slug);
}

package com.company.website.repository;

import com.company.website.entity.Content;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ContentRepository extends JpaRepository<Content, Long> {
    Optional<Content> findBySlug(String slug);
    
    Page<Content> findByStatusAndType(Content.ContentStatus status, Content.ContentType type, Pageable pageable);
    
    Page<Content> findByCategoryIdAndStatus(Long categoryId, Content.ContentStatus status, Pageable pageable);
    
    List<Content> findByStatusAndFeaturedTrueOrderByPublishedAtDesc(Content.ContentStatus status);
    
    @Query("SELECT c FROM Content c JOIN c.translations t WHERE t.languageCode = :lang AND c.status = :status")
    Page<Content> findByLanguageAndStatus(@Param("lang") String lang, @Param("status") Content.ContentStatus status, Pageable pageable);
    
    boolean existsBySlug(String slug);
}

package com.company.website.repository;

import com.company.website.entity.MediaResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MediaResourceRepository extends JpaRepository<MediaResource, Long> {
    List<MediaResource> findByContentId(Long contentId);
    Page<MediaResource> findByType(MediaResource.MediaType type, Pageable pageable);
    List<MediaResource> findByUploadedById(Long userId);
}

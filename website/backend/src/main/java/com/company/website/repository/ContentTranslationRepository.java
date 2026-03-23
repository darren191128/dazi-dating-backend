package com.company.website.repository;

import com.company.website.entity.ContentTranslation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ContentTranslationRepository extends JpaRepository<ContentTranslation, Long> {
    Optional<ContentTranslation> findByContentIdAndLanguageCode(Long contentId, String languageCode);
}

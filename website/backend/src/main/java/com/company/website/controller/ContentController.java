package com.company.website.controller;

import com.company.website.dto.ContentRequest;
import com.company.website.entity.Content;
import com.company.website.entity.ContentTranslation;
import com.company.website.entity.User;
import com.company.website.repository.ContentRepository;
import com.company.website.repository.ContentTranslationRepository;
import com.company.website.repository.UserRepository;
import com.company.website.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api")
public class ContentController {

    @Autowired
    private ContentRepository contentRepository;

    @Autowired
    private ContentTranslationRepository translationRepository;

    @Autowired
    private UserRepository userRepository;

    // Public endpoints
    @GetMapping("/public/contents")
    public Page<Content> getPublishedContents(
            @RequestParam(required = false) String type,
            @RequestParam(required = false) Long categoryId,
            Pageable pageable) {
        if (categoryId != null) {
            return contentRepository.findByCategoryIdAndStatus(categoryId, Content.ContentStatus.PUBLISHED, pageable);
        }
        if (type != null) {
            return contentRepository.findByStatusAndType(Content.ContentStatus.PUBLISHED, 
                    Content.ContentType.valueOf(type.toUpperCase()), pageable);
        }
        return contentRepository.findByStatusAndType(Content.ContentStatus.PUBLISHED, Content.ContentType.PAGE, pageable);
    }

    @GetMapping("/public/contents/{slug}")
    public ResponseEntity<Content> getContentBySlug(@PathVariable String slug) {
        return contentRepository.findBySlug(slug)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/public/contents/featured")
    public ResponseEntity<?> getFeaturedContents() {
        return ResponseEntity.ok(contentRepository.findByStatusAndFeaturedTrueOrderByPublishedAtDesc(
                Content.ContentStatus.PUBLISHED));
    }

    // Admin/Editor endpoints
    @GetMapping("/editor/contents")
    @PreAuthorize("hasAnyRole('ADMIN', 'EDITOR')")
    public Page<Content> getAllContents(Pageable pageable) {
        return contentRepository.findAll(pageable);
    }

    @PostMapping("/editor/contents")
    @PreAuthorize("hasAnyRole('ADMIN', 'EDITOR')")
    public ResponseEntity<?> createContent(@RequestBody ContentRequest request,
                                           @AuthenticationPrincipal UserPrincipal currentUser) {
        User author = userRepository.findById(currentUser.getId()).orElseThrow();

        Content content = new Content();
        content.setSlug(request.getSlug());
        content.setType(Content.ContentType.valueOf(request.getType()));
        content.setStatus(Content.ContentStatus.valueOf(request.getStatus()));
        content.setFeatured(request.getFeatured());
        content.setFeaturedImage(request.getFeaturedImage());
        content.setAuthor(author);
        
        if (request.getStatus().equals("PUBLISHED")) {
            content.setPublishedAt(LocalDateTime.now());
        }

        Content savedContent = contentRepository.save(content);

        // Create translation
        ContentTranslation translation = new ContentTranslation();
        translation.setContent(savedContent);
        translation.setLanguageCode(request.getLanguageCode());
        translation.setTitle(request.getTitle());
        translation.setSummary(request.getSummary());
        translation.setBody(request.getBody());
        translation.setMetaTitle(request.getMetaTitle());
        translation.setMetaDescription(request.getMetaDescription());
        translationRepository.save(translation);

        return ResponseEntity.ok(savedContent);
    }

    @PutMapping("/editor/contents/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'EDITOR')")
    public ResponseEntity<?> updateContent(@PathVariable Long id, @RequestBody ContentRequest request) {
        Content content = contentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Content not found"));

        content.setSlug(request.getSlug());
        content.setType(Content.ContentType.valueOf(request.getType()));
        content.setStatus(Content.ContentStatus.valueOf(request.getStatus()));
        content.setFeatured(request.getFeatured());
        content.setFeaturedImage(request.getFeaturedImage());

        if (request.getStatus().equals("PUBLISHED") && content.getPublishedAt() == null) {
            content.setPublishedAt(LocalDateTime.now());
        }

        Content savedContent = contentRepository.save(content);

        // Update or create translation
        ContentTranslation translation = translationRepository
                .findByContentIdAndLanguageCode(id, request.getLanguageCode())
                .orElse(new ContentTranslation());
        
        translation.setContent(savedContent);
        translation.setLanguageCode(request.getLanguageCode());
        translation.setTitle(request.getTitle());
        translation.setSummary(request.getSummary());
        translation.setBody(request.getBody());
        translation.setMetaTitle(request.getMetaTitle());
        translation.setMetaDescription(request.getMetaDescription());
        translationRepository.save(translation);

        return ResponseEntity.ok(savedContent);
    }

    @DeleteMapping("/admin/contents/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteContent(@PathVariable Long id) {
        contentRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}

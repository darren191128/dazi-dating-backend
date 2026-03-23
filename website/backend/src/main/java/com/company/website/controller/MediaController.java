package com.company.website.controller;

import com.company.website.entity.MediaResource;
import com.company.website.entity.User;
import com.company.website.repository.MediaResourceRepository;
import com.company.website.repository.UserRepository;
import com.company.website.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class MediaController {

    @Autowired
    private MediaResourceRepository mediaRepository;

    @Autowired
    private UserRepository userRepository;

    @Value("${app.upload.dir:uploads/}")
    private String uploadDir;

    @Value("${app.upload.url:/uploads/}")
    private String uploadUrl;

    // Public endpoint
    @GetMapping("/public/media")
    public Page<MediaResource> getAllMedia(Pageable pageable) {
        return mediaRepository.findAll(pageable);
    }

    @GetMapping("/public/media/{id}")
    public ResponseEntity<MediaResource> getMediaById(@PathVariable Long id) {
        return mediaRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Admin/Editor upload endpoint
    @PostMapping("/editor/media/upload")
    @PreAuthorize("hasAnyRole('ADMIN', 'EDITOR')")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file,
                                        @RequestParam(required = false) String alt,
                                        @RequestParam(required = false) String description,
                                        @AuthenticationPrincipal UserPrincipal currentUser) {
        try {
            // Create upload directory if not exists
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // Generate unique filename
            String originalFilename = file.getOriginalFilename();
            String extension = originalFilename != null ? 
                    originalFilename.substring(originalFilename.lastIndexOf(".")) : "";
            String filename = UUID.randomUUID() + extension;
            Path filePath = uploadPath.resolve(filename);

            // Save file
            Files.copy(file.getInputStream(), filePath);

            // Determine media type
            MediaResource.MediaType mediaType = determineMediaType(file.getContentType());

            // Save to database
            MediaResource media = new MediaResource();
            media.setFilename(originalFilename);
            media.setUrl(uploadUrl + filename);
            media.setMimeType(file.getContentType());
            media.setSize(file.getSize());
            media.setAlt(alt);
            media.setDescription(description);
            media.setType(mediaType);
            
            User user = userRepository.findById(currentUser.getId()).orElseThrow();
            media.setUploadedBy(user);

            MediaResource savedMedia = mediaRepository.save(media);
            return ResponseEntity.ok(savedMedia);

        } catch (IOException e) {
            return ResponseEntity.badRequest().body("Failed to upload file: " + e.getMessage());
        }
    }

    @DeleteMapping("/admin/media/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteMedia(@PathVariable Long id) {
        MediaResource media = mediaRepository.findById(id).orElse(null);
        if (media == null) {
            return ResponseEntity.notFound().build();
        }

        try {
            // Delete file from filesystem
            String filename = media.getUrl().substring(media.getUrl().lastIndexOf("/") + 1);
            Path filePath = Paths.get(uploadDir).resolve(filename);
            Files.deleteIfExists(filePath);

            // Delete from database
            mediaRepository.deleteById(id);
            return ResponseEntity.ok().build();
        } catch (IOException e) {
            return ResponseEntity.badRequest().body("Failed to delete file: " + e.getMessage());
        }
    }

    private MediaResource.MediaType determineMediaType(String contentType) {
        if (contentType == null) return MediaResource.MediaType.OTHER;
        
        if (contentType.startsWith("image/")) return MediaResource.MediaType.IMAGE;
        if (contentType.startsWith("video/")) return MediaResource.MediaType.VIDEO;
        if (contentType.startsWith("audio/")) return MediaResource.MediaType.AUDIO;
        if (contentType.contains("pdf") || contentType.contains("document") || 
            contentType.contains("msword") || contentType.contains("officedocument")) {
            return MediaResource.MediaType.DOCUMENT;
        }
        return MediaResource.MediaType.OTHER;
    }
}

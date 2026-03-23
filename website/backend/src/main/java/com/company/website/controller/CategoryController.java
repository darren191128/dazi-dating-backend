package com.company.website.controller;

import com.company.website.dto.CategoryRequest;
import com.company.website.entity.Category;
import com.company.website.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CategoryController {

    @Autowired
    private CategoryRepository categoryRepository;

    // Public endpoints
    @GetMapping("/public/categories")
    public List<Category> getAllCategories() {
        return categoryRepository.findByParentIsNullAndEnabledTrueOrderBySortOrderAsc();
    }

    @GetMapping("/public/categories/{slug}")
    public ResponseEntity<Category> getCategoryBySlug(@PathVariable String slug) {
        return categoryRepository.findBySlug(slug)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Admin endpoints
    @GetMapping("/admin/categories")
    @PreAuthorize("hasRole('ADMIN')")
    public List<Category> getAllCategoriesAdmin() {
        return categoryRepository.findAll();
    }

    @PostMapping("/admin/categories")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Category> createCategory(@RequestBody CategoryRequest request) {
        Category category = new Category();
        category.setSlug(request.getSlug());
        category.setSortOrder(request.getSortOrder());
        category.setEnabled(request.getEnabled());
        
        if (request.getParentId() != null) {
            Category parent = categoryRepository.findById(request.getParentId()).orElse(null);
            category.setParent(parent);
        }

        return ResponseEntity.ok(categoryRepository.save(category));
    }

    @PutMapping("/admin/categories/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Category> updateCategory(@PathVariable Long id, @RequestBody CategoryRequest request) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        category.setSlug(request.getSlug());
        category.setSortOrder(request.getSortOrder());
        category.setEnabled(request.getEnabled());

        if (request.getParentId() != null) {
            Category parent = categoryRepository.findById(request.getParentId()).orElse(null);
            category.setParent(parent);
        } else {
            category.setParent(null);
        }

        return ResponseEntity.ok(categoryRepository.save(category));
    }

    @DeleteMapping("/admin/categories/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteCategory(@PathVariable Long id) {
        categoryRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}

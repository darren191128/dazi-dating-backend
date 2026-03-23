package com.ruoshan.controller;

import com.ruoshan.entity.Lawyer;
import com.ruoshan.service.LawyerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/lawyers")
@CrossOrigin(origins = "*")
public class LawyerController {
    
    @Autowired
    private LawyerService lawyerService;
    
    @GetMapping
    public ResponseEntity<List<Lawyer>> getAllLawyers() {
        return ResponseEntity.ok(lawyerService.findAll());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Lawyer> getLawyerById(@PathVariable Long id) {
        return lawyerService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/search")
    public ResponseEntity<List<Lawyer>> searchBySpecialty(@RequestParam String specialty) {
        return ResponseEntity.ok(lawyerService.findBySpecialty(specialty));
    }
}

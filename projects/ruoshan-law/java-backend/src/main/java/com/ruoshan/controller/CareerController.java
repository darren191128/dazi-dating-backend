package com.ruoshan.controller;

import com.ruoshan.entity.Career;
import com.ruoshan.service.CareerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/careers")
@CrossOrigin(origins = "*")
public class CareerController {
    
    @Autowired
    private CareerService careerService;
    
    @GetMapping
    public ResponseEntity<List<Career>> getActiveCareers() {
        return ResponseEntity.ok(careerService.findActive());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Career> getCareerById(@PathVariable Long id) {
        return careerService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}

package com.ruoshan.service;

import com.ruoshan.entity.Career;
import com.ruoshan.repository.CareerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CareerService {
    
    @Autowired
    private CareerRepository careerRepository;
    
    public List<Career> findActive() {
        return careerRepository.findByActiveTrue();
    }
    
    public Optional<Career> findById(Long id) {
        return careerRepository.findById(id);
    }
}

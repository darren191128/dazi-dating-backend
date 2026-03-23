package com.ruoshan.service;

import com.ruoshan.entity.Lawyer;
import com.ruoshan.repository.LawyerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LawyerService {
    
    @Autowired
    private LawyerRepository lawyerRepository;
    
    public List<Lawyer> findAll() {
        return lawyerRepository.findAll();
    }
    
    public Optional<Lawyer> findById(Long id) {
        return lawyerRepository.findById(id);
    }
    
    public List<Lawyer> findBySpecialty(String specialty) {
        return lawyerRepository.findBySpecialtyContaining(specialty);
    }
}

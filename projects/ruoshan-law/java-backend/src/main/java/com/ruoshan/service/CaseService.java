package com.ruoshan.service;

import com.ruoshan.entity.Case;
import com.ruoshan.repository.CaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CaseService {
    
    @Autowired
    private CaseRepository caseRepository;
    
    public List<Case> findAll() {
        return caseRepository.findAll();
    }
    
    public Optional<Case> findById(Long id) {
        return caseRepository.findById(id);
    }
    
    public List<Case> findByType(String type) {
        return caseRepository.findByTypeContaining(type);
    }
}

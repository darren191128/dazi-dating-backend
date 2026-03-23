package com.ruoshan.service;

import com.ruoshan.entity.News;
import com.ruoshan.repository.NewsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NewsService {
    
    @Autowired
    private NewsRepository newsRepository;
    
    public List<News> findAll() {
        return newsRepository.findAll();
    }
    
    public Optional<News> findById(Long id) {
        return newsRepository.findById(id);
    }
    
    public List<News> findByCategory(String category) {
        return newsRepository.findByCategory(category);
    }
}

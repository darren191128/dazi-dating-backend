package com.dazi.gift.repository;

import com.dazi.gift.entity.Gift;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GiftRepository extends JpaRepository<Gift, Long> {
    
    List<Gift> findByStatusOrderBySortOrderAsc(Integer status);
    
    List<Gift> findByCategoryIdAndStatusOrderBySortOrderAsc(Long categoryId, Integer status);
}

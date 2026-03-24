package com.dazi.gift.repository;

import com.dazi.gift.entity.GiftCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GiftCategoryRepository extends JpaRepository<GiftCategory, Long> {
    
    List<GiftCategory> findByStatusOrderBySortOrderAsc(Integer status);
}

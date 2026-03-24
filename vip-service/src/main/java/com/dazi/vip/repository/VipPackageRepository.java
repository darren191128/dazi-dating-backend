package com.dazi.vip.repository;

import com.dazi.vip.entity.VipPackage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * VIP套餐Repository
 */
@Repository
public interface VipPackageRepository extends JpaRepository<VipPackage, Long> {
    
    /**
     * 查询上架的套餐列表
     */
    List<VipPackage> findByStatusOrderBySortOrderAsc(Integer status);
}

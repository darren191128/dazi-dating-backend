package com.dazi.vip.repository;

import com.dazi.vip.entity.VipPrivilege;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * VIP特权Repository
 */
@Repository
public interface VipPrivilegeRepository extends JpaRepository<VipPrivilege, Long> {
    
    /**
     * 查询有效的特权列表
     */
    List<VipPrivilege> findByStatusOrderBySortOrderAsc(Integer status);
    
    /**
     * 根据编码查询特权
     */
    VipPrivilege findByCode(String code);
}

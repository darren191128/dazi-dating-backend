package com.dazi.vip.repository;

import com.dazi.vip.entity.UserVip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * 用户VIP记录Repository
 */
@Repository
public interface UserVipRepository extends JpaRepository<UserVip, Long> {
    
    /**
     * 根据用户ID查询VIP记录
     */
    Optional<UserVip> findByUserId(Long userId);
    
    /**
     * 查询有效的VIP记录
     */
    Optional<UserVip> findByUserIdAndStatusAndEndTimeAfter(Long userId, Integer status, LocalDateTime now);
    
    /**
     * 查询过期的VIP记录并更新状态
     */
    void deleteByEndTimeBeforeAndStatus(LocalDateTime now, Integer status);
}

package com.dazi.vip.service;

import com.dazi.vip.dto.*;
import com.dazi.vip.entity.UserVip;
import com.dazi.vip.entity.VipPackage;
import com.dazi.vip.entity.VipPrivilege;
import com.dazi.vip.repository.UserVipRepository;
import com.dazi.vip.repository.VipPackageRepository;
import com.dazi.vip.repository.VipPrivilegeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * VIP服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class VipServiceImpl implements VipService {
    
    private final VipPackageRepository vipPackageRepository;
    private final UserVipRepository userVipRepository;
    private final VipPrivilegeRepository vipPrivilegeRepository;
    
    @Override
    public List<VipPackageDTO> getPackages() {
        List<VipPackage> packages = vipPackageRepository.findByStatusOrderBySortOrderAsc(1);
        return packages.stream()
                .map(this::convertToPackageDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public UserVipStatusDTO getVipStatus(Long userId) {
        UserVipStatusDTO statusDTO = new UserVipStatusDTO();
        
        // 查询用户VIP记录
        Optional<UserVip> userVipOpt = userVipRepository.findByUserId(userId);
        
        if (userVipOpt.isPresent()) {
            UserVip userVip = userVipOpt.get();
            
            // 检查是否过期
            if (userVip.isValid()) {
                statusDTO.setIsVip(true);
                statusDTO.setExpireTime(userVip.getEndTime());
                statusDTO.setRemainingDays((int) ChronoUnit.DAYS.between(LocalDateTime.now(), userVip.getEndTime()));
                
                // 获取套餐名称
                vipPackageRepository.findById(userVip.getPackageId())
                        .ifPresent(pkg -> statusDTO.setPackageName(pkg.getName()));
            } else {
                statusDTO.setIsVip(false);
                // 更新过期状态
                if (userVip.getStatus() == 1) {
                    userVip.setStatus(0);
                    userVipRepository.save(userVip);
                }
            }
        } else {
            statusDTO.setIsVip(false);
        }
        
        // 获取特权列表
        List<VipPrivilegeDTO> privilegeDTOs = getPrivileges().stream()
                .map(this::convertToPrivilegeDTO)
                .collect(Collectors.toList());
        statusDTO.setPrivileges(privilegeDTOs);
        
        return statusDTO;
    }
    
    @Override
    @Transactional
    public void subscribe(Long userId, SubscribeVipRequest request) {
        // 查询套餐
        VipPackage vipPackage = vipPackageRepository.findById(request.getPackageId())
                .orElseThrow(() -> new RuntimeException("套餐不存在"));
        
        if (vipPackage.getStatus() != 1) {
            throw new RuntimeException("套餐已下架");
        }
        
        // 计算VIP时间
        LocalDateTime startTime = LocalDateTime.now();
        LocalDateTime endTime = startTime.plusDays(vipPackage.getDurationDays());
        
        // 查询现有VIP记录
        Optional<UserVip> existingVipOpt = userVipRepository.findByUserId(userId);
        
        if (existingVipOpt.isPresent()) {
            UserVip existingVip = existingVipOpt.get();
            
            // 如果当前VIP有效，则在原基础上续期
            if (existingVip.isValid()) {
                endTime = existingVip.getEndTime().plusDays(vipPackage.getDurationDays());
                existingVip.setEndTime(endTime);
                existingVip.setPackageId(vipPackage.getId());
            } else {
                // VIP已过期，重新计算
                existingVip.setStartTime(startTime);
                existingVip.setEndTime(endTime);
                existingVip.setPackageId(vipPackage.getId());
                existingVip.setStatus(1);
            }
            
            userVipRepository.save(existingVip);
        } else {
            // 创建新的VIP记录
            UserVip userVip = new UserVip();
            userVip.setUserId(userId);
            userVip.setPackageId(vipPackage.getId());
            userVip.setStartTime(startTime);
            userVip.setEndTime(endTime);
            userVip.setStatus(1);
            
            userVipRepository.save(userVip);
        }
        
        log.info("用户 {} 订阅VIP套餐 {} 成功", userId, vipPackage.getName());
    }
    
    @Override
    public List<VipPrivilege> getPrivileges() {
        return vipPrivilegeRepository.findByStatusOrderBySortOrderAsc(1);
    }
    
    @Override
    public boolean isVip(Long userId) {
        Optional<UserVip> userVipOpt = userVipRepository
                .findByUserIdAndStatusAndEndTimeAfter(userId, 1, LocalDateTime.now());
        return userVipOpt.isPresent();
    }
    
    @Override
    public boolean hasPrivilege(Long userId, String privilegeCode) {
        // 先检查是否是VIP
        if (!isVip(userId)) {
            return false;
        }
        
        // 检查特权是否存在且有效
        VipPrivilege privilege = vipPrivilegeRepository.findByCode(privilegeCode);
        return privilege != null && privilege.getStatus() == 1;
    }
    
    /**
     * 转换套餐为DTO
     */
    private VipPackageDTO convertToPackageDTO(VipPackage vipPackage) {
        VipPackageDTO dto = new VipPackageDTO();
        dto.setId(vipPackage.getId());
        dto.setName(vipPackage.getName());
        dto.setDescription(vipPackage.getDescription());
        dto.setDurationDays(vipPackage.getDurationDays());
        dto.setPrice(vipPackage.getPrice());
        dto.setOriginalPrice(vipPackage.getOriginalPrice());
        dto.setIsRecommended(vipPackage.getIsRecommended());
        
        // 计算折扣信息
        if (vipPackage.getOriginalPrice() != null && vipPackage.getOriginalPrice().compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal discount = vipPackage.getPrice()
                    .divide(vipPackage.getOriginalPrice(), 2, RoundingMode.HALF_UP)
                    .multiply(new BigDecimal("10"));
            dto.setDiscountInfo(String.format("%.1f折", discount.doubleValue()));
        }
        
        // 计算每日价格
        if (vipPackage.getDurationDays() > 0) {
            BigDecimal dailyPrice = vipPackage.getPrice()
                    .divide(new BigDecimal(vipPackage.getDurationDays()), 2, RoundingMode.HALF_UP);
            dto.setDailyPrice(dailyPrice);
        }
        
        return dto;
    }
    
    /**
     * 转换特权为DTO
     */
    private VipPrivilegeDTO convertToPrivilegeDTO(VipPrivilege privilege) {
        VipPrivilegeDTO dto = new VipPrivilegeDTO();
        dto.setCode(privilege.getCode());
        dto.setName(privilege.getName());
        dto.setDescription(privilege.getDescription());
        dto.setIcon(privilege.getIcon());
        return dto;
    }
}

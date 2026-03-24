package com.dazi.user.service;

import com.dazi.common.result.Result;
import com.dazi.user.dto.CheckinRecordDTO;
import com.dazi.user.dto.CheckinResultDTO;
import com.dazi.user.dto.CheckinStatusDTO;
import com.dazi.user.entity.UserCheckin;
import com.dazi.user.entity.UserCheckinStats;
import com.dazi.user.repository.UserCheckinRepository;
import com.dazi.user.repository.UserCheckinStatsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 签到服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CheckinServiceImpl implements CheckinService {
    
    private final UserCheckinRepository userCheckinRepository;
    private final UserCheckinStatsRepository userCheckinStatsRepository;
    
    // 签到奖励规则：第1-7天对应的积分奖励
    private static final List<Integer> DAILY_REWARDS = Arrays.asList(10, 15, 20, 25, 30, 35, 50);
    
    @Override
    @Transactional
    public Result<CheckinResultDTO> checkin(Long userId) {
        LocalDate today = LocalDate.now();
        
        // 检查今日是否已签到
        UserCheckin todayCheckin = userCheckinRepository.findByUserIdAndDate(userId, today);
        if (todayCheckin != null) {
            CheckinResultDTO result = new CheckinResultDTO();
            result.setSuccess(false);
            result.setMessage("今日已签到");
            result.setConsecutiveDays(todayCheckin.getConsecutiveDays());
            result.setRewardAmount(todayCheckin.getRewardAmount());
            return Result.success(result);
        }
        
        // 获取上次签到记录
        UserCheckin lastCheckin = userCheckinRepository.findLastCheckin(userId);
        
        // 计算连续签到天数
        int consecutiveDays = 1;
        if (lastCheckin != null) {
            LocalDate yesterday = today.minusDays(1);
            if (lastCheckin.getCheckinDate().equals(yesterday)) {
                // 昨天签到了，连续天数+1
                consecutiveDays = lastCheckin.getConsecutiveDays() + 1;
            }
            // 如果超过7天，重置为1
            if (consecutiveDays > 7) {
                consecutiveDays = 1;
            }
        }
        
        // 获取今日奖励
        int rewardIndex = consecutiveDays - 1;
        int rewardAmount = DAILY_REWARDS.get(rewardIndex);
        
        // 保存签到记录
        UserCheckin checkin = new UserCheckin();
        checkin.setUserId(userId);
        checkin.setCheckinDate(today);
        checkin.setConsecutiveDays(consecutiveDays);
        checkin.setRewardType("point");
        checkin.setRewardAmount(rewardAmount);
        userCheckinRepository.insert(checkin);
        
        // 更新签到统计
        updateCheckinStats(userId, consecutiveDays, today);
        
        // TODO: 调用钱包服务增加积分
        // walletService.addPoint(userId, rewardAmount, "签到奖励-第" + consecutiveDays + "天");
        
        log.info("用户签到成功: userId={}, consecutiveDays={}, reward={}", userId, consecutiveDays, rewardAmount);
        
        CheckinResultDTO result = new CheckinResultDTO();
        result.setSuccess(true);
        result.setRewardAmount(rewardAmount);
        result.setConsecutiveDays(consecutiveDays);
        result.setMessage("签到成功，获得" + rewardAmount + "积分");
        result.setCheckinTime(checkin.getCreatedAt());
        
        return Result.success(result);
    }
    
    @Override
    public Result<CheckinStatusDTO> getCheckinStatus(Long userId) {
        LocalDate today = LocalDate.now();
        
        // 检查今日是否已签到
        UserCheckin todayCheckin = userCheckinRepository.findByUserIdAndDate(userId, today);
        boolean hasCheckinToday = todayCheckin != null;
        
        // 获取签到统计
        UserCheckinStats stats = userCheckinStatsRepository.findByUserId(userId);
        int consecutiveDays = 0;
        int totalDays = 0;
        LocalDate lastCheckinDate = null;
        
        if (stats != null) {
            consecutiveDays = stats.getCurrentConsecutiveDays();
            totalDays = stats.getTotalCheckinDays();
            lastCheckinDate = stats.getLastCheckinDate();
            
            // 检查连续性是否已断
            if (lastCheckinDate != null && !lastCheckinDate.equals(today) && 
                !lastCheckinDate.equals(today.minusDays(1))) {
                consecutiveDays = 0;
            }
        }
        
        // 如果今天已签到，使用今天的连续天数
        if (hasCheckinToday && todayCheckin != null) {
            consecutiveDays = todayCheckin.getConsecutiveDays();
        }
        
        // 计算今日应得奖励
        int nextDay = hasCheckinToday ? consecutiveDays : consecutiveDays + 1;
        if (nextDay > 7) nextDay = 1;
        int todayReward = DAILY_REWARDS.get(nextDay - 1);
        
        CheckinStatusDTO status = new CheckinStatusDTO();
        status.setHasCheckinToday(hasCheckinToday);
        status.setConsecutiveDays(consecutiveDays);
        status.setTotalDays(totalDays);
        status.setTodayReward(todayReward);
        status.setWeeklyRewards(DAILY_REWARDS);
        status.setLastCheckinDate(lastCheckinDate);
        
        return Result.success(status);
    }
    
    @Override
    public Result<List<CheckinRecordDTO>> getCheckinRecords(Long userId, String month) {
        LocalDate startDate;
        LocalDate endDate;
        
        if (month != null && !month.isEmpty()) {
            // 解析月份参数 (格式: yyyy-MM)
            YearMonth yearMonth = YearMonth.parse(month, DateTimeFormatter.ofPattern("yyyy-MM"));
            startDate = yearMonth.atDay(1);
            endDate = yearMonth.atEndOfMonth();
        } else {
            // 默认查询当前月
            YearMonth yearMonth = YearMonth.now();
            startDate = yearMonth.atDay(1);
            endDate = yearMonth.atEndOfMonth();
        }
        
        List<UserCheckin> records = userCheckinRepository.findByUserIdAndMonth(userId, startDate, endDate);
        
        List<CheckinRecordDTO> dtoList = new ArrayList<>();
        for (UserCheckin record : records) {
            dtoList.add(convertToDTO(record));
        }
        
        return Result.success(dtoList);
    }
    
    @Override
    @Transactional
    public void initUserCheckinStats(Long userId) {
        UserCheckinStats existing = userCheckinStatsRepository.findByUserId(userId);
        if (existing != null) {
            return;
        }
        
        UserCheckinStats stats = new UserCheckinStats();
        stats.setUserId(userId);
        stats.setTotalCheckinDays(0);
        stats.setMaxConsecutiveDays(0);
        stats.setCurrentConsecutiveDays(0);
        
        userCheckinStatsRepository.insert(stats);
        
        log.info("初始化用户签到统计: userId={}", userId);
    }
    
    @Transactional
    protected void updateCheckinStats(Long userId, int consecutiveDays, LocalDate today) {
        UserCheckinStats stats = userCheckinStatsRepository.findByUserId(userId);
        
        if (stats == null) {
            stats = new UserCheckinStats();
            stats.setUserId(userId);
            stats.setTotalCheckinDays(1);
            stats.setMaxConsecutiveDays(consecutiveDays);
            stats.setCurrentConsecutiveDays(consecutiveDays);
            stats.setLastCheckinDate(today);
            userCheckinStatsRepository.insert(stats);
        } else {
            stats.setTotalCheckinDays(stats.getTotalCheckinDays() + 1);
            stats.setCurrentConsecutiveDays(consecutiveDays);
            if (consecutiveDays > stats.getMaxConsecutiveDays()) {
                stats.setMaxConsecutiveDays(consecutiveDays);
            }
            stats.setLastCheckinDate(today);
            userCheckinStatsRepository.updateById(stats);
        }
    }
    
    private CheckinRecordDTO convertToDTO(UserCheckin record) {
        CheckinRecordDTO dto = new CheckinRecordDTO();
        dto.setId(record.getId());
        dto.setCheckinDate(record.getCheckinDate());
        dto.setConsecutiveDays(record.getConsecutiveDays());
        dto.setRewardType(record.getRewardType());
        dto.setRewardAmount(record.getRewardAmount());
        dto.setCreatedAt(record.getCreatedAt());
        return dto;
    }
}

package com.dazi.admin.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dazi.admin.entity.FinanceDailyReport;
import com.dazi.admin.entity.FinanceRecord;
import com.dazi.admin.repository.FinanceDailyReportRepository;
import com.dazi.admin.repository.FinanceRecordRepository;
import com.dazi.common.result.Result;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 财务管理服务
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class FinanceService {
    
    private final FinanceRecordRepository financeRecordRepository;
    private final FinanceDailyReportRepository financeDailyReportRepository;
    private final RedisTemplate<String, Object> redisTemplate;
    
    private static final String FINANCE_CACHE_PREFIX = "finance:cache:";
    
    /**
     * 获取财务流水列表
     */
    public Result<Page<FinanceRecord>> getFinanceRecords(Integer page, Integer size, 
                                                         Integer type, LocalDateTime startTime, 
                                                         LocalDateTime endTime) {
        Page<FinanceRecord> pageParam = new Page<>(page, size);
        
        com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<FinanceRecord> wrapper = 
            new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<>();
        
        if (type != null) {
            wrapper.eq("type", type);
        }
        if (startTime != null) {
            wrapper.ge("create_time", startTime);
        }
        if (endTime != null) {
            wrapper.le("create_time", endTime);
        }
        
        wrapper.orderByDesc("create_time");
        
        Page<FinanceRecord> result = financeRecordRepository.selectPage(pageParam, wrapper);
        return Result.success(result);
    }
    
    /**
     * 获取财务报表（日报）
     */
    public Result<List<FinanceDailyReport>> getDailyReports(LocalDate startDate, LocalDate endDate) {
        String cacheKey = FINANCE_CACHE_PREFIX + "daily:" + startDate + ":" + endDate;
        
        @SuppressWarnings("unchecked")
        List<FinanceDailyReport> cached = (List<FinanceDailyReport>) redisTemplate.opsForValue().get(cacheKey);
        if (cached != null) {
            return Result.success(cached);
        }
        
        List<FinanceDailyReport> reports = financeDailyReportRepository.findByDateRange(startDate, endDate);
        
        // 缓存1小时
        redisTemplate.opsForValue().set(cacheKey, reports, 1, TimeUnit.HOURS);
        
        return Result.success(reports);
    }
    
    /**
     * 获取财务统计
     */
    public Result<Map<String, Object>> getFinanceStatistics(LocalDate startDate, LocalDate endDate) {
        String cacheKey = FINANCE_CACHE_PREFIX + "stats:" + startDate + ":" + endDate;
        
        @SuppressWarnings("unchecked")
        Map<String, Object> cached = (Map<String, Object>) redisTemplate.opsForValue().get(cacheKey);
        if (cached != null) {
            return Result.success(cached);
        }
        
        LocalDateTime start = startDate.atStartOfDay();
        LocalDateTime end = endDate.atTime(LocalTime.MAX);
        
        // 总收入
        BigDecimal totalIncome = financeRecordRepository.sumAmountByTypeAndTime(1, start, end);
        // 总退款
        BigDecimal totalRefund = financeRecordRepository.sumAmountByTypeAndTime(3, start, end);
        // 净收入
        BigDecimal netIncome = (totalIncome != null ? totalIncome : BigDecimal.ZERO)
                .subtract(totalRefund != null ? totalRefund : BigDecimal.ZERO);
        
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalIncome", totalIncome != null ? totalIncome : BigDecimal.ZERO);
        stats.put("totalRefund", totalRefund != null ? totalRefund : BigDecimal.ZERO);
        stats.put("netIncome", netIncome);
        stats.put("startDate", startDate);
        stats.put("endDate", endDate);
        
        // 缓存30分钟
        redisTemplate.opsForValue().set(cacheKey, stats, 30, TimeUnit.MINUTES);
        
        return Result.success(stats);
    }
    
    /**
     * 生成日报（定时任务调用）
     */
    @Transactional
    public Result<Void> generateDailyReport(LocalDate date) {
        // 检查是否已生成
        FinanceDailyReport existing = financeDailyReportRepository.findByDate(date);
        if (existing != null) {
            return Result.error("该日期报表已生成");
        }
        
        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.atTime(LocalTime.MAX);
        
        // 统计订单数据
        BigDecimal totalIncome = financeRecordRepository.sumAmountByTypeAndTime(1, start, end);
        BigDecimal totalRefund = financeRecordRepository.sumAmountByTypeAndTime(3, start, end);
        
        FinanceDailyReport report = new FinanceDailyReport();
        report.setReportDate(date);
        report.setTotalIncome(totalIncome != null ? totalIncome : BigDecimal.ZERO);
        report.setTotalRefund(totalRefund != null ? totalRefund : BigDecimal.ZERO);
        report.setNetIncome(report.getTotalIncome().subtract(report.getTotalRefund()));
        
        // TODO: 统计其他数据
        report.setTotalOrders(0);
        report.setSuccessOrders(0);
        report.setRefundOrders(0);
        report.setNewUsers(0);
        report.setActiveUsers(0);
        report.setNewActivities(0);
        report.setTotalParticipants(0);
        
        financeDailyReportRepository.insert(report);
        
        log.info("生成财务日报: date={}, income={}, refund={}", 
                date, report.getTotalIncome(), report.getTotalRefund());
        
        return Result.success();
    }
    
    /**
     * 记录财务流水
     */
    @Transactional
    public Result<Long> recordFinance(Long userId, String orderNo, Integer type, 
                                       BigDecimal amount, String description, 
                                       String remark, Long relatedId, String relatedType) {
        FinanceRecord record = new FinanceRecord();
        record.setUserId(userId);
        record.setOrderNo(orderNo);
        record.setType(type);
        record.setAmount(amount);
        record.setDescription(description);
        record.setRemark(remark);
        record.setRelatedId(relatedId);
        record.setRelatedType(relatedType);
        
        // 计算余额（简化实现，实际应该查询用户账户余额）
        record.setBalance(BigDecimal.ZERO);
        
        financeRecordRepository.insert(record);
        
        log.info("记录财务流水: userId={}, type={}, amount={}, orderNo={}", 
                userId, type, amount, orderNo);
        
        return Result.success(record.getId());
    }
}

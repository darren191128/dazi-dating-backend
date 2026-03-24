package com.dazi.admin.controller;

import com.dazi.admin.service.FinanceService;
import com.dazi.common.result.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Map;

/**
 * 财务管理控制器
 */
@RestController
@RequestMapping("/api/admin/finance")
@RequiredArgsConstructor
public class FinanceController {
    
    private final FinanceService financeService;
    
    /**
     * 获取财务流水列表
     */
    @GetMapping("/records")
    public Result<?> getFinanceRecords(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "20") Integer size,
            @RequestParam(required = false) Integer type,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDate startTime,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDate endTime) {
        return financeService.getFinanceRecords(page, size, type, 
                startTime != null ? startTime.atStartOfDay() : null,
                endTime != null ? endTime.atTime(23, 59, 59) : null);
    }
    
    /**
     * 获取财务报表（日报）
     */
    @GetMapping("/daily-reports")
    public Result<?> getDailyReports(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {
        return financeService.getDailyReports(startDate, endDate);
    }
    
    /**
     * 获取财务统计
     */
    @GetMapping("/statistics")
    public Result<Map<String, Object>> getFinanceStatistics(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {
        return financeService.getFinanceStatistics(startDate, endDate);
    }
    
    /**
     * 生成日报
     */
    @PostMapping("/generate-report")
    public Result<Void> generateDailyReport(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
        return financeService.generateDailyReport(date);
    }
}

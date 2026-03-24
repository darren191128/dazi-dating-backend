package com.dazi.admin.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dazi.admin.dto.*;
import com.dazi.admin.entity.UserReport;
import com.dazi.admin.mapper.UserReportMapper;
import com.dazi.common.result.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportService extends ServiceImpl<UserReportMapper, UserReport> {
    
    private final UserReportMapper userReportMapper;
    
    // 举报类型映射
    private static final Map<Integer, String> TYPE_MAP = new HashMap<>();
    // 状态映射
    private static final Map<Integer, String> STATUS_MAP = new HashMap<>();
    
    static {
        TYPE_MAP.put(1, "骚扰辱骂");
        TYPE_MAP.put(2, "欺诈诈骗");
        TYPE_MAP.put(3, "色情低俗");
        TYPE_MAP.put(4, "广告营销");
        TYPE_MAP.put(5, "其他违规");
        
        STATUS_MAP.put(0, "待处理");
        STATUS_MAP.put(1, "已处理");
        STATUS_MAP.put(2, "已驳回");
    }
    
    /**
     * 举报用户
     */
    @Transactional
    public Result<Void> reportUser(Long reporterId, ReportUserRequest request) {
        UserReport report = new UserReport();
        report.setReporterId(reporterId);
        report.setReportedId(request.getReportedId());
        report.setType(request.getType());
        report.setReason(request.getReason());
        
        // 将证据列表转为JSON字符串
        if (request.getEvidence() != null && !request.getEvidence().isEmpty()) {
            report.setEvidence(String.join(",", request.getEvidence()));
        }
        
        report.setStatus(0); // 待处理
        
        userReportMapper.insert(report);
        
        return Result.success();
    }
    
    /**
     * 获取举报列表
     */
    public Result<PageResult<ReportDTO>> getReports(Integer status, Integer page, Integer pageSize) {
        Page<UserReport> pageParam = new Page<>(page, pageSize);
        LambdaQueryWrapper<UserReport> wrapper = new LambdaQueryWrapper<>();
        
        if (status != null) {
            wrapper.eq(UserReport::getStatus, status);
        }
        
        wrapper.orderByDesc(UserReport::getCreatedAt);
        
        Page<UserReport> resultPage = userReportMapper.selectPage(pageParam, wrapper);
        
        List<ReportDTO> dtoList = resultPage.getRecords().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        
        PageResult<ReportDTO> pageResult = new PageResult<>(
                dtoList,
                resultPage.getTotal(),
                page,
                pageSize
        );
        
        return Result.success(pageResult);
    }
    
    /**
     * 处理举报
     */
    @Transactional
    public Result<Void> handleReport(Long reportId, Long handlerId, HandleReportRequest request) {
        UserReport report = userReportMapper.selectById(reportId);
        if (report == null) {
            return Result.error("举报记录不存在");
        }
        
        report.setStatus(request.getStatus());
        report.setHandleResult(request.getHandleResult());
        report.setHandlerId(handlerId);
        report.setHandledAt(LocalDateTime.now());
        
        userReportMapper.updateById(report);
        
        return Result.success();
    }
    
    /**
     * 转换为DTO
     */
    private ReportDTO convertToDTO(UserReport report) {
        ReportDTO dto = new ReportDTO();
        dto.setId(report.getId());
        dto.setReporterId(report.getReporterId());
        dto.setReportedId(report.getReportedId());
        dto.setType(report.getType());
        dto.setTypeName(TYPE_MAP.getOrDefault(report.getType(), "未知"));
        dto.setReason(report.getReason());
        
        // 解析证据
        if (report.getEvidence() != null && !report.getEvidence().isEmpty()) {
            dto.setEvidenceList(List.of(report.getEvidence().split(",")));
        }
        
        dto.setStatus(report.getStatus());
        dto.setStatusName(STATUS_MAP.getOrDefault(report.getStatus(), "未知"));
        dto.setHandleResult(report.getHandleResult());
        dto.setHandlerId(report.getHandlerId());
        dto.setHandledAt(report.getHandledAt());
        dto.setCreatedAt(report.getCreatedAt());
        
        return dto;
    }
}

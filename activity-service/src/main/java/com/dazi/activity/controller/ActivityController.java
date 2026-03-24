package com.dazi.activity.controller;

import com.dazi.activity.service.ActivityService;
import com.dazi.common.annotation.Log;
import com.dazi.common.dto.CreateActivityDTO;
import com.dazi.common.dto.PageQueryDTO;
import com.dazi.common.result.Result;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/activity")
@RequiredArgsConstructor
@Validated
public class ActivityController {
    
    private final ActivityService activityService;
    
    /**
     * 发布活动
     */
    @PostMapping
    @Log(operation = "发布活动", type = "CREATE", logParams = true)
    public Result<Long> createActivity(
            HttpServletRequest request,
            @Valid @RequestBody CreateActivityDTO activityDTO) {
        
        Long userId = (Long) request.getAttribute("currentUserId");
        return activityService.createActivity(userId, activityDTO);
    }
    
    /**
     * 获取活动列表
     */
    @GetMapping("/list")
    public Result<Map<String, Object>> getActivityList(
            @RequestParam(required = false) String type,
            @Valid PageQueryDTO queryDTO) {
        
        return activityService.getActivityList(type, queryDTO.getPage(), queryDTO.getPageSize());
    }
    
    /**
     * 获取活动详情
     */
    @GetMapping("/{activityId}")
    public Result<Map<String, Object>> getActivityDetail(@PathVariable Long activityId) {
        return activityService.getActivityDetail(activityId);
    }
    
    /**
     * 报名活动
     */
    @PostMapping("/{activityId}/join")
    @Log(operation = "报名活动", type = "CREATE", logParams = true)
    public Result<Void> joinActivity(
            HttpServletRequest request,
            @PathVariable @NotNull(message = "活动ID不能为空") Long activityId) {
        
        Long userId = (Long) request.getAttribute("currentUserId");
        return activityService.joinActivity(activityId, userId);
    }
    
    /**
     * 取消报名
     */
    @PostMapping("/{activityId}/cancel")
    @Log(operation = "取消报名", type = "DELETE", logParams = true)
    public Result<Void> cancelJoin(
            HttpServletRequest request,
            @PathVariable @NotNull(message = "活动ID不能为空") Long activityId) {
        
        Long userId = (Long) request.getAttribute("currentUserId");
        return activityService.quitActivity(activityId, userId);
    }
    
    /**
     * 获取活动类型
     */
    @GetMapping("/types")
    public Result<List<Map<String, Object>>> getActivityTypes() {
        return activityService.getActivityTypes();
    }
}

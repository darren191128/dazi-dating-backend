package com.dazi.activity.controller;

import com.dazi.activity.entity.Activity;
import com.dazi.activity.service.ActivityService;
import com.dazi.common.result.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/activity")
@RequiredArgsConstructor
public class ActivityController {
    
    private final ActivityService activityService;
    
    /**
     * 发布活动
     */
    @PostMapping("/create")
    public Result<Long> createActivity(
            @RequestParam Long userId,
            @RequestParam String title,
            @RequestParam String description,
            @RequestParam Integer type,
            @RequestParam String typeName,
            @RequestParam LocalDateTime startTime,
            @RequestParam LocalDateTime endTime,
            @RequestParam String location,
            @RequestParam Integer minParticipants,
            @RequestParam Integer maxParticipants,
            @RequestParam Integer paymentType,
            @RequestParam BigDecimal totalAmount,
            @RequestParam LocalDateTime registrationDeadline) {
        
        Activity activity = new Activity();
        activity.setUserId(userId);
        activity.setTitle(title);
        activity.setDescription(description);
        activity.setType(type);
        activity.setTypeName(typeName);
        activity.setStartTime(startTime);
        activity.setEndTime(endTime);
        activity.setLocation(location);
        activity.setMinParticipants(minParticipants);
        activity.setMaxParticipants(maxParticipants);
        activity.setPaymentType(paymentType);
        activity.setTotalAmount(totalAmount);
        activity.setRegistrationDeadline(registrationDeadline);
        
        return activityService.createActivity(activity);
    }
    
    /**
     * 获取活动列表
     */
    @GetMapping("/list")
    public Result<List<Activity>> getActivityList(
            @RequestParam(required = false) Integer type,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        return activityService.getActivityList(type, page, size);
    }
    
    /**
     * 获取活动详情
     */
    @GetMapping("/detail/{activityId}")
    public Result<Map<String, Object>> getActivityDetail(@PathVariable Long activityId) {
        return activityService.getActivityDetail(activityId);
    }
    
    /**
     * 报名活动
     */
    @PostMapping("/join/{activityId}")
    public Result<Void> joinActivity(
            @PathVariable Long activityId,
            @RequestParam Long userId) {
        return activityService.joinActivity(activityId, userId);
    }
    
    /**
     * 取消报名
     */
    @PostMapping("/quit/{activityId}")
    public Result<Void> quitActivity(
            @PathVariable Long activityId,
            @RequestParam Long userId) {
        return activityService.quitActivity(activityId, userId);
    }
    
    /**
     * 获取我发布的活动
     */
    @GetMapping("/my/published/{userId}")
    public Result<List<Activity>> getMyPublishedActivities(@PathVariable Long userId) {
        return activityService.getMyPublishedActivities(userId);
    }
    
    /**
     * 获取我参与的活动
     */
    @GetMapping("/my/joined/{userId}")
    public Result<List<Activity>> getMyJoinedActivities(@PathVariable Long userId) {
        return activityService.getMyJoinedActivities(userId);
    }
}

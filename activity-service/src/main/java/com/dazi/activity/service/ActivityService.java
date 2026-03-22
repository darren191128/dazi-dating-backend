package com.dazi.activity.service;

import com.dazi.activity.entity.Activity;
import com.dazi.activity.entity.ActivityParticipant;
import com.dazi.activity.repository.ActivityParticipantRepository;
import com.dazi.activity.repository.ActivityRepository;
import com.dazi.common.result.Result;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class ActivityService {
    
    private final ActivityRepository activityRepository;
    private final ActivityParticipantRepository participantRepository;
    
    /**
     * 发布活动
     */
    @Transactional
    public Result<Long> createActivity(Activity activity) {
        // 校验时间
        if (activity.getStartTime().isBefore(LocalDateTime.now())) {
            return Result.error("活动开始时间不能早于当前时间");
        }
        if (activity.getEndTime().isBefore(activity.getStartTime())) {
            return Result.error("活动结束时间不能早于开始时间");
        }
        
        // 设置默认值
        activity.setCurrentParticipants(0);
        activity.setStatus(1);
        
        // 计算人均费用
        if (activity.getPaymentType() == 1) { // AA制
            BigDecimal perPerson = activity.getTotalAmount()
                    .divide(new BigDecimal(activity.getMaxParticipants()), 2, BigDecimal.ROUND_HALF_UP);
            activity.setPerPersonAmount(perPerson);
        } else if (activity.getPaymentType() == 2) { // 男A女免
            // 暂不计算，报名后根据性别计算
            activity.setPerPersonAmount(BigDecimal.ZERO);
        } else if (activity.getPaymentType() == 3) { // 请客
            activity.setPerPersonAmount(BigDecimal.ZERO);
        } else { // 免费
            activity.setPerPersonAmount(BigDecimal.ZERO);
            activity.setTotalAmount(BigDecimal.ZERO);
        }
        
        activityRepository.insert(activity);
        
        log.info("活动发布成功: activityId={}, userId={}", activity.getId(), activity.getUserId());
        
        return Result.success(activity.getId());
    }
    
    /**
     * 获取活动列表
     */
    public Result<List<Activity>> getActivityList(Integer type, Integer page, Integer size) {
        List<Activity> activities;
        if (type == null || type == 0) {
            activities = activityRepository.findActiveActivities();
        } else {
            activities = activityRepository.findByType(type);
        }
        
        // 分页处理
        int start = (page - 1) * size;
        int end = Math.min(start + size, activities.size());
        if (start > activities.size()) {
            return Result.success(List.of());
        }
        
        return Result.success(activities.subList(start, end));
    }
    
    /**
     * 获取活动详情
     */
    public Result<Map<String, Object>> getActivityDetail(Long activityId) {
        Activity activity = activityRepository.selectById(activityId);
        if (activity == null) {
            return Result.error("活动不存在");
        }
        
        // 获取参与者列表
        List<ActivityParticipant> participants = participantRepository.findByActivityId(activityId);
        
        Map<String, Object> result = new HashMap<>();
        result.put("activity", activity);
        result.put("participants", participants);
        result.put("participantCount", participants.size());
        
        return Result.success(result);
    }
    
    /**
     * 报名活动
     */
    @Transactional
    public Result<Void> joinActivity(Long activityId, Long userId) {
        Activity activity = activityRepository.selectById(activityId);
        if (activity == null) {
            return Result.error("活动不存在");
        }
        if (activity.getStatus() != 1) {
            return Result.error("活动已结束或已取消");
        }
        if (activity.getRegistrationDeadline().isBefore(LocalDateTime.now())) {
            return Result.error("报名已截止");
        }
        if (activity.getCurrentParticipants() >= activity.getMaxParticipants()) {
            return Result.error("活动已满员");
        }
        
        // 检查是否已报名
        List<ActivityParticipant> existing = participantRepository.findByActivityId(activityId);
        boolean alreadyJoined = existing.stream()
                .anyMatch(p -> p.getUserId().equals(userId) && p.getStatus() == 1);
        if (alreadyJoined) {
            return Result.error("您已报名该活动");
        }
        
        // 创建报名记录
        ActivityParticipant participant = new ActivityParticipant();
        participant.setActivityId(activityId);
        participant.setUserId(userId);
        participant.setStatus(1);
        participant.setJoinTime(LocalDateTime.now());
        participantRepository.insert(participant);
        
        // 更新活动参与人数
        activity.setCurrentParticipants(activity.getCurrentParticipants() + 1);
        activityRepository.updateById(activity);
        
        log.info("用户报名活动: userId={}, activityId={}", userId, activityId);
        
        return Result.success();
    }
    
    /**
     * 取消报名
     */
    @Transactional
    public Result<Void> quitActivity(Long activityId, Long userId) {
        Activity activity = activityRepository.selectById(activityId);
        if (activity == null) {
            return Result.error("活动不存在");
        }
        
        // 查找报名记录
        List<ActivityParticipant> participants = participantRepository.findByActivityId(activityId);
        ActivityParticipant participant = participants.stream()
                .filter(p -> p.getUserId().equals(userId) && p.getStatus() == 1)
                .findFirst()
                .orElse(null);
        
        if (participant == null) {
            return Result.error("您未报名该活动");
        }
        
        // 更新报名状态
        participant.setStatus(0);
        participant.setQuitTime(LocalDateTime.now());
        participantRepository.updateById(participant);
        
        // 更新活动参与人数
        activity.setCurrentParticipants(activity.getCurrentParticipants() - 1);
        activityRepository.updateById(activity);
        
        log.info("用户取消报名: userId={}, activityId={}", userId, activityId);
        
        return Result.success();
    }
    
    /**
     * 获取我发布的活动
     */
    public Result<List<Activity>> getMyPublishedActivities(Long userId) {
        List<Activity> activities = activityRepository.findByUserId(userId);
        return Result.success(activities);
    }
    
    /**
     * 获取我参与的活动
     */
    public Result<List<Activity>> getMyJoinedActivities(Long userId) {
        List<ActivityParticipant> participants = participantRepository.findByUserId(userId);
        List<Long> activityIds = participants.stream()
                .filter(p -> p.getStatus() == 1)
                .map(ActivityParticipant::getActivityId)
                .toList();
        
        if (activityIds.isEmpty()) {
            return Result.success(List.of());
        }
        
        List<Activity> activities = activityRepository.selectBatchIds(activityIds);
        return Result.success(activities);
    }
}

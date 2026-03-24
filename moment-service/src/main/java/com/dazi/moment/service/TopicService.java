package com.dazi.moment.service;

import com.dazi.common.result.Result;
import com.dazi.moment.entity.Moment;
import com.dazi.moment.entity.MomentTopic;
import com.dazi.moment.entity.Topic;
import com.dazi.moment.entity.UserTopicFollow;
import com.dazi.moment.repository.MomentRepository;
import com.dazi.moment.repository.MomentTopicRepository;
import com.dazi.moment.repository.TopicRepository;
import com.dazi.moment.repository.UserTopicFollowRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TopicService {
    
    private final TopicRepository topicRepository;
    private final MomentTopicRepository momentTopicRepository;
    private final UserTopicFollowRepository userTopicFollowRepository;
    private final MomentRepository momentRepository;
    
    /**
     * 获取话题列表
     */
    public Result<List<Map<String, Object>>> getTopics(String keyword) {
        List<Topic> topics;
        if (keyword != null && !keyword.trim().isEmpty()) {
            topics = topicRepository.searchByName(keyword.trim());
        } else {
            topics = topicRepository.findAllActive();
        }
        
        List<Map<String, Object>> result = topics.stream().map(topic -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", topic.getId());
            map.put("name", topic.getName());
            map.put("description", topic.getDescription());
            map.put("icon", topic.getIcon());
            map.put("cover", topic.getCover());
            map.put("postCount", topic.getPostCount());
            map.put("followCount", topic.getFollowCount());
            return map;
        }).collect(Collectors.toList());
        
        return Result.success(result);
    }
    
    /**
     * 获取话题详情
     */
    public Result<Map<String, Object>> getTopicDetail(Long topicId, Long userId) {
        Topic topic = topicRepository.selectById(topicId);
        if (topic == null || topic.getStatus() != 1) {
            return Result.error("话题不存在");
        }
        
        Map<String, Object> result = new HashMap<>();
        result.put("id", topic.getId());
        result.put("name", topic.getName());
        result.put("description", topic.getDescription());
        result.put("icon", topic.getIcon());
        result.put("cover", topic.getCover());
        result.put("postCount", topic.getPostCount());
        result.put("followCount", topic.getFollowCount());
        
        // 检查当前用户是否已关注
        if (userId != null) {
            UserTopicFollow follow = userTopicFollowRepository.findByUserIdAndTopicId(userId, topicId);
            result.put("isFollowed", follow != null);
        } else {
            result.put("isFollowed", false);
        }
        
        return Result.success(result);
    }
    
    /**
     * 获取话题下的动态
     */
    public Result<List<Map<String, Object>>> getTopicMoments(Long topicId, Long userId, Integer page, Integer pageSize) {
        Topic topic = topicRepository.selectById(topicId);
        if (topic == null || topic.getStatus() != 1) {
            return Result.error("话题不存在");
        }
        
        if (page == null || page < 1) page = 1;
        if (pageSize == null || pageSize < 1 || pageSize > 50) pageSize = 20;
        
        int offset = (page - 1) * pageSize;
        
        List<MomentTopic> momentTopics = momentTopicRepository.findByTopicId(topicId, offset, pageSize);
        List<Map<String, Object>> result = new ArrayList<>();
        
        for (MomentTopic mt : momentTopics) {
            Moment moment = momentRepository.selectById(mt.getMomentId());
            if (moment != null && moment.getDeleted() == 0 && moment.getStatus() == 1) {
                result.add(convertMomentToMap(moment, userId));
            }
        }
        
        return Result.success(result);
    }
    
    /**
     * 关注话题
     */
    @Transactional
    public Result<Void> followTopic(Long userId, Long topicId) {
        Topic topic = topicRepository.selectById(topicId);
        if (topic == null || topic.getStatus() != 1) {
            return Result.error("话题不存在");
        }
        
        // 检查是否已关注
        UserTopicFollow existing = userTopicFollowRepository.findByUserIdAndTopicId(userId, topicId);
        if (existing != null) {
            return Result.error("已经关注过该话题");
        }
        
        UserTopicFollow follow = new UserTopicFollow();
        follow.setUserId(userId);
        follow.setTopicId(topicId);
        userTopicFollowRepository.insert(follow);
        
        // 增加话题关注数
        topicRepository.incrementFollowCount(topicId);
        
        log.info("关注话题成功: userId={}, topicId={}", userId, topicId);
        return Result.success();
    }
    
    /**
     * 取消关注话题
     */
    @Transactional
    public Result<Void> unfollowTopic(Long userId, Long topicId) {
        UserTopicFollow follow = userTopicFollowRepository.findByUserIdAndTopicId(userId, topicId);
        if (follow == null) {
            return Result.error("未关注该话题");
        }
        
        userTopicFollowRepository.deleteById(follow.getId());
        
        // 减少话题关注数
        topicRepository.decrementFollowCount(topicId);
        
        log.info("取消关注话题: userId={}, topicId={}", userId, topicId);
        return Result.success();
    }
    
    /**
     * 将动态转换为Map
     */
    private Map<String, Object> convertMomentToMap(Moment moment, Long currentUserId) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", moment.getId());
        map.put("userId", moment.getUserId());
        map.put("content", moment.getContent());
        map.put("images", moment.getImages());
        map.put("location", moment.getLocation());
        map.put("likeCount", moment.getLikeCount());
        map.put("commentCount", moment.getCommentCount());
        map.put("viewCount", moment.getViewCount());
        map.put("isTop", moment.getIsTop());
        map.put("createdAt", moment.getCreatedAt());
        return map;
    }
}

package com.dazi.moment.controller;

import com.dazi.common.result.Result;
import com.dazi.moment.service.MomentService;
import com.dazi.moment.service.TopicService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/moment")
@RequiredArgsConstructor
public class MomentController {
    
    private final MomentService momentService;
    private final TopicService topicService;
    
    /**
     * 发布动态
     */
    @PostMapping("/publish")
    public Result<Long> publishMoment(
            HttpServletRequest request,
            @RequestBody Map<String, Object> params) {
        
        Long userId = (Long) request.getAttribute("currentUserId");
        String content = (String) params.get("content");
        String images = (String) params.get("images");
        String location = (String) params.get("location");
        
        return momentService.publishMoment(userId, content, images, location);
    }
    
    /**
     * 获取动态列表
     */
    @GetMapping("/list")
    public Result<List<Map<String, Object>>> getMomentList(
            HttpServletRequest request,
            @RequestParam(required = false, defaultValue = "all") String type,
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "20") Integer pageSize) {
        
        Long userId = (Long) request.getAttribute("currentUserId");
        return momentService.getMomentList(userId, type, page, pageSize);
    }
    
    /**
     * 动态详情
     */
    @GetMapping("/detail/{id}")
    public Result<Map<String, Object>> getMomentDetail(
            HttpServletRequest request,
            @PathVariable Long id) {
        
        Long userId = (Long) request.getAttribute("currentUserId");
        return momentService.getMomentDetail(id, userId);
    }
    
    /**
     * 点赞
     */
    @PostMapping("/like")
    public Result<Void> likeMoment(
            HttpServletRequest request,
            @RequestBody Map<String, Object> params) {
        
        Long userId = (Long) request.getAttribute("currentUserId");
        Long momentId = Long.valueOf(params.get("momentId").toString());
        
        return momentService.likeMoment(userId, momentId);
    }
    
    /**
     * 取消点赞
     */
    @PostMapping("/unlike")
    public Result<Void> unlikeMoment(
            HttpServletRequest request,
            @RequestBody Map<String, Object> params) {
        
        Long userId = (Long) request.getAttribute("currentUserId");
        Long momentId = Long.valueOf(params.get("momentId").toString());
        
        return momentService.unlikeMoment(userId, momentId);
    }
    
    /**
     * 评论
     */
    @PostMapping("/comment")
    public Result<Long> commentMoment(
            HttpServletRequest request,
            @RequestBody Map<String, Object> params) {
        
        Long userId = (Long) request.getAttribute("currentUserId");
        Long momentId = Long.valueOf(params.get("momentId").toString());
        Long parentId = params.get("parentId") != null ? Long.valueOf(params.get("parentId").toString()) : null;
        String content = (String) params.get("content");
        
        return momentService.commentMoment(userId, momentId, parentId, content);
    }
    
    /**
     * 获取评论列表
     */
    @GetMapping("/comments/{id}")
    public Result<List<Map<String, Object>>> getComments(
            @PathVariable Long id,
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "20") Integer pageSize) {
        
        return momentService.getComments(id, page, pageSize);
    }
    
    /**
     * 删除动态
     */
    @DeleteMapping("/{id}")
    public Result<Void> deleteMoment(
            HttpServletRequest request,
            @PathVariable Long id) {
        
        Long userId = (Long) request.getAttribute("currentUserId");
        return momentService.deleteMoment(userId, id);
    }
    
    // ==================== 话题相关接口 ====================
    
    /**
     * 获取话题列表
     */
    @GetMapping("/topics")
    public Result<List<Map<String, Object>>> getTopics(
            @RequestParam(required = false) String keyword) {
        return topicService.getTopics(keyword);
    }
    
    /**
     * 获取话题详情
     */
    @GetMapping("/topic/{topicId}")
    public Result<Map<String, Object>> getTopicDetail(
            HttpServletRequest request,
            @PathVariable Long topicId) {
        Long userId = (Long) request.getAttribute("currentUserId");
        return topicService.getTopicDetail(topicId, userId);
    }
    
    /**
     * 获取话题下的动态
     */
    @GetMapping("/topic/{topicId}/moments")
    public Result<List<Map<String, Object>>> getTopicMoments(
            HttpServletRequest request,
            @PathVariable Long topicId,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "20") Integer pageSize) {
        Long userId = (Long) request.getAttribute("currentUserId");
        return topicService.getTopicMoments(topicId, userId, page, pageSize);
    }
    
    /**
     * 关注话题
     */
    @PostMapping("/topic/{topicId}/follow")
    public Result<Void> followTopic(
            HttpServletRequest request,
            @PathVariable Long topicId) {
        Long userId = (Long) request.getAttribute("currentUserId");
        return topicService.followTopic(userId, topicId);
    }
    
    /**
     * 取消关注话题
     */
    @PostMapping("/topic/{topicId}/unfollow")
    public Result<Void> unfollowTopic(
            HttpServletRequest request,
            @PathVariable Long topicId) {
        Long userId = (Long) request.getAttribute("currentUserId");
        return topicService.unfollowTopic(userId, topicId);
    }
    
    // ==================== 热门推荐接口 ====================
    
    /**
     * 获取热门动态
     */
    @GetMapping("/hot")
    public Result<List<Map<String, Object>>> getHotMoments(
            HttpServletRequest request,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "20") Integer pageSize) {
        Long userId = (Long) request.getAttribute("currentUserId");
        return momentService.getHotMoments(userId, page, pageSize);
    }
}

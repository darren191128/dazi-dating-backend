package com.dazi.user.controller;

import com.dazi.common.annotation.Log;
import org.springframework.web.bind.annotation.RequestParam;
import com.dazi.common.dto.FollowDTO;
import com.dazi.common.dto.LoginDTO;
import com.dazi.common.dto.PageQueryDTO;
import com.dazi.common.dto.UpdateProfileDTO;
import com.dazi.common.result.Result;
import com.dazi.user.dto.CheckinRecordDTO;
import com.dazi.user.dto.CheckinResultDTO;
import com.dazi.user.dto.CheckinStatusDTO;
import com.dazi.user.dto.IncreaseIntimacyRequest;
import com.dazi.user.dto.IntimacyDTO;
import com.dazi.user.dto.IntimacyRankingDTO;
import com.dazi.user.service.CheckinService;
import com.dazi.user.service.IntimacyService;
import com.dazi.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Validated
public class UserController {
    
    private final UserService userService;
    private final CheckinService checkinService;
    private final IntimacyService intimacyService;
    
    /**
     * 微信登录
     */
    @PostMapping("/wxLogin")
    @Log(operation = "用户微信登录", type = "LOGIN", logParams = true)
    public Result<Map<String, Object>> wxLogin(@Valid @RequestBody LoginDTO loginDTO) {
        // 实际应该调用微信接口换取openid
        // 这里简化处理，直接使用code作为openid
        String openId = "wx_" + loginDTO.getCode();
        String nickname = loginDTO.getNickname() != null ? loginDTO.getNickname() : "微信用户";
        String avatar = loginDTO.getAvatar() != null ? loginDTO.getAvatar() : "";
        Integer gender = loginDTO.getGender() != null ? loginDTO.getGender() : 0;
        
        return userService.wxLogin(openId, nickname, avatar, gender);
    }
    
    /**
     * 获取用户信息（从token中解析）
     */
    @GetMapping("/info")
    public Result<Map<String, Object>> getUserInfo(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("currentUserId");
        return userService.getUserInfo(userId);
    }
    
    /**
     * 更新用户资料
     */
    @PutMapping("/info")
    @Log(operation = "更新用户资料", type = "UPDATE", logParams = true)
    public Result<Void> updateProfile(
            HttpServletRequest request,
            @Valid @RequestBody UpdateProfileDTO profileDTO) {
        Long userId = (Long) request.getAttribute("currentUserId");
        return userService.updateProfile(userId, profileDTO.getNickname(), profileDTO.getBio(), profileDTO.getAge());
    }
    
    // ==================== 关注相关接口 ====================
    
    /**
     * 关注用户
     */
    @PostMapping("/follow")
    @Log(operation = "关注用户", type = "CREATE", logParams = true)
    public Result<Void> followUser(
            HttpServletRequest request,
            @Valid @RequestBody FollowDTO followDTO) {
        Long userId = (Long) request.getAttribute("currentUserId");
        return userService.followUser(userId, followDTO.getFollowUserId());
    }
    
    /**
     * 取消关注
     */
    @PostMapping("/unfollow")
    @Log(operation = "取消关注", type = "DELETE", logParams = true)
    public Result<Void> unfollowUser(
            HttpServletRequest request,
            @Valid @RequestBody FollowDTO followDTO) {
        Long userId = (Long) request.getAttribute("currentUserId");
        return userService.unfollowUser(userId, followDTO.getFollowUserId());
    }
    
    /**
     * 我的关注列表
     */
    @GetMapping("/followings")
    public Result<List<Map<String, Object>>> getFollowings(
            HttpServletRequest request,
            @Valid PageQueryDTO queryDTO) {
        Long userId = (Long) request.getAttribute("currentUserId");
        return userService.getFollowings(userId, queryDTO.getPage(), queryDTO.getPageSize());
    }
    
    /**
     * 我的粉丝列表
     */
    @GetMapping("/followers")
    public Result<List<Map<String, Object>>> getFollowers(
            HttpServletRequest request,
            @Valid PageQueryDTO queryDTO) {
        Long userId = (Long) request.getAttribute("currentUserId");
        return userService.getFollowers(userId, queryDTO.getPage(), queryDTO.getPageSize());
    }
    
    /**
     * 访客记录
     */
    @GetMapping("/visitors")
    public Result<List<Map<String, Object>>> getVisitors(
            HttpServletRequest request,
            @Valid PageQueryDTO queryDTO) {
        Long userId = (Long) request.getAttribute("currentUserId");
        return userService.getVisitors(userId, queryDTO.getPage(), queryDTO.getPageSize());
    }
    
    // ==================== 匹配相关接口 ====================
    
    /**
     * 喜欢用户（滑动匹配）
     */
    @PostMapping("/match/like")
    @Log(operation = "喜欢用户", type = "CREATE", logParams = true)
    public Result<Map<String, Object>> matchLike(
            HttpServletRequest request,
            @Valid @RequestBody FollowDTO followDTO) {
        Long userId = (Long) request.getAttribute("currentUserId");
        return userService.matchLike(userId, followDTO.getFollowUserId());
    }
    
    /**
     * 不喜欢用户
     */
    @PostMapping("/match/dislike")
    @Log(operation = "不喜欢用户", type = "CREATE", logParams = true)
    public Result<Void> matchDislike(
            HttpServletRequest request,
            @Valid @RequestBody FollowDTO followDTO) {
        Long userId = (Long) request.getAttribute("currentUserId");
        return userService.matchDislike(userId, followDTO.getFollowUserId());
    }
    
    /**
     * 互相喜欢列表
     */
    @GetMapping("/match/mutual")
    public Result<List<Map<String, Object>>> getMutualMatches(
            HttpServletRequest request,
            @Valid PageQueryDTO queryDTO) {
        Long userId = (Long) request.getAttribute("currentUserId");
        return userService.getMutualMatches(userId, queryDTO.getPage(), queryDTO.getPageSize());
    }

    // ==================== 签到相关接口 ====================

    /**
     * 每日签到
     */
    @PostMapping("/checkin")
    @Log(operation = "每日签到", type = "CREATE", logParams = true)
    public Result<CheckinResultDTO> checkin(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("currentUserId");
        return checkinService.checkin(userId);
    }

    /**
     * 查询签到状态
     */
    @GetMapping("/checkin/status")
    public Result<CheckinStatusDTO> getCheckinStatus(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("currentUserId");
        return checkinService.getCheckinStatus(userId);
    }

    /**
     * 获取签到记录
     */
    @GetMapping("/checkin/records")
    public Result<List<CheckinRecordDTO>> getCheckinRecords(
            HttpServletRequest request,
            @RequestParam(required = false) String month) {
        Long userId = (Long) request.getAttribute("currentUserId");
        return checkinService.getCheckinRecords(userId, month);
    }

    // ==================== 亲密度相关接口 ====================

    /**
     * 查询与某用户的亲密度
     */
    @GetMapping("/intimacy/{friendId}")
    public Result<IntimacyDTO> getIntimacy(
            HttpServletRequest request,
            @PathVariable Long friendId) {
        Long userId = (Long) request.getAttribute("currentUserId");
        return intimacyService.getIntimacy(userId, friendId);
    }

    /**
     * 获取亲密度排行榜
     */
    @GetMapping("/intimacy/ranking")
    public Result<List<IntimacyRankingDTO>> getIntimacyRanking(
            HttpServletRequest request,
            @RequestParam(defaultValue = "10") Integer limit) {
        Long userId = (Long) request.getAttribute("currentUserId");
        return intimacyService.getIntimacyRanking(userId, limit);
    }

    /**
     * 增加亲密度（内部调用）
     */
    @PostMapping("/intimacy/increase")
    public Result<Void> increaseIntimacy(
            HttpServletRequest request,
            @RequestBody IncreaseIntimacyRequest requestBody) {
        Long userId = (Long) request.getAttribute("currentUserId");
        return intimacyService.increaseIntimacy(userId, requestBody);
    }
}

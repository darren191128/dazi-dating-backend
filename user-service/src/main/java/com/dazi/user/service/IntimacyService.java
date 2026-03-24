package com.dazi.user.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dazi.common.result.Result;
import com.dazi.user.dto.IncreaseIntimacyRequest;
import com.dazi.user.dto.IntimacyDTO;
import com.dazi.user.dto.IntimacyRankingDTO;
import com.dazi.user.entity.User;
import com.dazi.user.entity.UserIntimacy;
import com.dazi.user.mapper.UserIntimacyMapper;
import com.dazi.user.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class IntimacyService extends ServiceImpl<UserIntimacyMapper, UserIntimacy> {
    
    private final UserIntimacyMapper userIntimacyMapper;
    private final UserMapper userMapper;
    
    // 亲密度等级配置
    private static final Map<Integer, IntimacyLevelConfig> LEVEL_CONFIG = new HashMap<>();
    
    static {
        LEVEL_CONFIG.put(1, new IntimacyLevelConfig(1, 0, 99, "初识"));
        LEVEL_CONFIG.put(2, new IntimacyLevelConfig(2, 100, 299, "熟悉"));
        LEVEL_CONFIG.put(3, new IntimacyLevelConfig(3, 300, 599, "朋友"));
        LEVEL_CONFIG.put(4, new IntimacyLevelConfig(4, 600, 999, "好友"));
        LEVEL_CONFIG.put(5, new IntimacyLevelConfig(5, 1000, 1999, "密友"));
        LEVEL_CONFIG.put(6, new IntimacyLevelConfig(6, 2000, 4999, "知己"));
        LEVEL_CONFIG.put(7, new IntimacyLevelConfig(7, 5000, Integer.MAX_VALUE, "挚友"));
    }
    
    /**
     * 获取亲密度信息
     */
    public Result<IntimacyDTO> getIntimacy(Long userId, Long friendId) {
        UserIntimacy intimacy = userIntimacyMapper.selectByUserAndFriend(userId, friendId);
        
        if (intimacy == null) {
            intimacy = new UserIntimacy();
            intimacy.setUserId(userId);
            intimacy.setFriendId(friendId);
            intimacy.setIntimacyScore(0);
            intimacy.setIntimacyLevel(1);
            intimacy.setChatCount(0);
            intimacy.setGiftValue(0);
        }
        
        IntimacyDTO dto = convertToDTO(intimacy);
        return Result.success(dto);
    }
    
    /**
     * 获取亲密度排行榜
     */
    public Result<List<IntimacyRankingDTO>> getIntimacyRanking(Long userId, Integer limit) {
        List<UserIntimacy> list = userIntimacyMapper.selectRankingByUserId(userId, limit);
        List<IntimacyRankingDTO> result = new ArrayList<>();
        
        for (UserIntimacy intimacy : list) {
            IntimacyRankingDTO dto = new IntimacyRankingDTO();
            dto.setFriendId(intimacy.getFriendId());
            dto.setIntimacyScore(intimacy.getIntimacyScore());
            dto.setIntimacyLevel(intimacy.getIntimacyLevel());
            dto.setLevelName(getLevelName(intimacy.getIntimacyLevel()));
            
            // 查询好友信息
            User friend = userMapper.selectById(intimacy.getFriendId());
            if (friend != null) {
                dto.setFriendNickname(friend.getNickname());
                dto.setFriendAvatar(friend.getAvatar());
            }
            
            result.add(dto);
        }
        
        return Result.success(result);
    }
    
    /**
     * 增加亲密度
     */
    @Transactional
    public Result<Void> increaseIntimacy(Long userId, IncreaseIntimacyRequest request) {
        Long friendId = request.getFriendId();
        Integer score = request.getScore();
        String type = request.getType();
        
        // 查询或创建亲密度记录
        UserIntimacy intimacy = userIntimacyMapper.selectByUserAndFriend(userId, friendId);
        if (intimacy == null) {
            intimacy = new UserIntimacy();
            intimacy.setUserId(userId);
            intimacy.setFriendId(friendId);
            intimacy.setIntimacyScore(0);
            intimacy.setIntimacyLevel(1);
            intimacy.setChatCount(0);
            intimacy.setGiftValue(0);
        }
        
        // 根据类型增加亲密度
        switch (type) {
            case "chat":
                intimacy.setChatCount(intimacy.getChatCount() + 1);
                intimacy.setIntimacyScore(intimacy.getIntimacyScore() + score);
                break;
            case "gift":
                intimacy.setGiftValue(intimacy.getGiftValue() + score);
                intimacy.setIntimacyScore(intimacy.getIntimacyScore() + score);
                break;
            case "follow":
                intimacy.setIntimacyScore(intimacy.getIntimacyScore() + score);
                break;
            case "activity":
                intimacy.setIntimacyScore(intimacy.getIntimacyScore() + score);
                break;
            default:
                intimacy.setIntimacyScore(intimacy.getIntimacyScore() + score);
        }
        
        // 计算等级
        int newLevel = calculateLevel(intimacy.getIntimacyScore());
        intimacy.setIntimacyLevel(newLevel);
        
        // 保存记录
        if (intimacy.getId() == null) {
            userIntimacyMapper.insert(intimacy);
        } else {
            userIntimacyMapper.updateById(intimacy);
        }
        
        return Result.success();
    }
    
    /**
     * 计算等级
     */
    private int calculateLevel(int score) {
        for (int i = 7; i >= 1; i--) {
            IntimacyLevelConfig config = LEVEL_CONFIG.get(i);
            if (score >= config.getMinScore()) {
                return i;
            }
        }
        return 1;
    }
    
    /**
     * 获取等级名称
     */
    private String getLevelName(int level) {
        IntimacyLevelConfig config = LEVEL_CONFIG.get(level);
        return config != null ? config.getName() : "未知";
    }
    
    /**
     * 转换为DTO
     */
    private IntimacyDTO convertToDTO(UserIntimacy intimacy) {
        IntimacyDTO dto = new IntimacyDTO();
        dto.setUserId(intimacy.getUserId());
        dto.setFriendId(intimacy.getFriendId());
        dto.setIntimacyScore(intimacy.getIntimacyScore());
        dto.setIntimacyLevel(intimacy.getIntimacyLevel());
        dto.setLevelName(getLevelName(intimacy.getIntimacyLevel()));
        dto.setChatCount(intimacy.getChatCount());
        dto.setGiftValue(intimacy.getGiftValue());
        
        // 计算下一级所需分数
        IntimacyLevelConfig currentConfig = LEVEL_CONFIG.get(intimacy.getIntimacyLevel());
        dto.setCurrentLevelMinScore(currentConfig.getMinScore());
        
        if (intimacy.getIntimacyLevel() < 7) {
            IntimacyLevelConfig nextConfig = LEVEL_CONFIG.get(intimacy.getIntimacyLevel() + 1);
            dto.setNextLevelScore(nextConfig.getMinScore());
            // 计算进度百分比
            int range = nextConfig.getMinScore() - currentConfig.getMinScore();
            int progress = intimacy.getIntimacyScore() - currentConfig.getMinScore();
            dto.setProgress((int) ((double) progress / range * 100));
        } else {
            dto.setNextLevelScore(Integer.MAX_VALUE);
            dto.setProgress(100);
        }
        
        return dto;
    }
    
    /**
     * 等级配置内部类
     */
    private static class IntimacyLevelConfig {
        private final int level;
        private final int minScore;
        private final int maxScore;
        private final String name;
        
        public IntimacyLevelConfig(int level, int minScore, int maxScore, String name) {
            this.level = level;
            this.minScore = minScore;
            this.maxScore = maxScore;
            this.name = name;
        }
        
        public int getLevel() { return level; }
        public int getMinScore() { return minScore; }
        public int getMaxScore() { return maxScore; }
        public String getName() { return name; }
    }
}

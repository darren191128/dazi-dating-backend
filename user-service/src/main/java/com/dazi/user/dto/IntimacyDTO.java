package com.dazi.user.dto;

import lombok.Data;

@Data
public class IntimacyDTO {
    
    private Long userId;
    private Long friendId;
    private Integer intimacyScore;
    private Integer intimacyLevel;
    private String levelName;
    private Integer chatCount;
    private Integer giftValue;
    private Integer nextLevelScore;
    private Integer currentLevelMinScore;
    private Integer progress;
}

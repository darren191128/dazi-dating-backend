package com.dazi.user.dto;

import lombok.Data;

@Data
public class IntimacyRankingDTO {
    
    private Long friendId;
    private String friendNickname;
    private String friendAvatar;
    private Integer intimacyScore;
    private Integer intimacyLevel;
    private String levelName;
}

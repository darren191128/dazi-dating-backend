package com.dazi.user.dto;

import lombok.Data;

@Data
public class IncreaseIntimacyRequest {
    
    private Long friendId;
    private Integer score;
    private String type;
}

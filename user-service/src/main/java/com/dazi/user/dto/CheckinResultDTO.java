package com.dazi.user.dto;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 签到结果DTO
 */
@Data
public class CheckinResultDTO {
    
    private Boolean success;
    
    private Integer rewardAmount;
    
    private Integer consecutiveDays;
    
    private String message;
    
    private LocalDateTime checkinTime;
}

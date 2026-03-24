package com.dazi.user.dto;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 签到记录DTO
 */
@Data
public class CheckinRecordDTO {
    
    private Long id;
    
    private LocalDate checkinDate;
    
    private Integer consecutiveDays;
    
    private String rewardType;
    
    private Integer rewardAmount;
    
    private LocalDateTime createdAt;
}

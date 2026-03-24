package com.dazi.user.dto;

import lombok.Data;
import java.time.LocalDate;
import java.util.List;

/**
 * 签到状态DTO
 */
@Data
public class CheckinStatusDTO {
    
    private Boolean hasCheckinToday;
    
    private Integer consecutiveDays;
    
    private Integer totalDays;
    
    private Integer todayReward;
    
    private List<Integer> weeklyRewards;
    
    private LocalDate lastCheckinDate;
}

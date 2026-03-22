package com.dazi.user.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("user_profile")
public class UserProfile {
    
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    
    private Long userId;
    
    private String bio;
    
    private LocalDate birthday;
    
    private String zodiac;
    
    private String chineseZodiac;
    
    private BigDecimal longitude;
    
    private BigDecimal latitude;
    
    private String location;
    
    private Integer locationVisible;
    
    private Integer realNameVerified;
    
    private String realName;
    
    private String idCard;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}

package com.dazi.match.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("user_location")
public class UserLocation {
    
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    
    private Long userId;
    
    private String nickname;
    
    private String avatar;
    
    private Integer age;
    
    private Integer gender;
    
    private String bio;
    
    private String zodiac;
    
    private String chineseZodiac;
    
    private String interests;
    
    private java.math.BigDecimal longitude;
    
    private java.math.BigDecimal latitude;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}

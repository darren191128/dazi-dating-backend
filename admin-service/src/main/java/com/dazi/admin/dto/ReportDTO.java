package com.dazi.admin.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ReportDTO {
    
    private Long id;
    private Long reporterId;
    private String reporterNickname;
    private String reporterAvatar;
    private Long reportedId;
    private String reportedNickname;
    private String reportedAvatar;
    private Integer type;
    private String typeName;
    private String reason;
    private List<String> evidenceList;
    private Integer status;
    private String statusName;
    private String handleResult;
    private Long handlerId;
    private String handlerName;
    private LocalDateTime handledAt;
    private LocalDateTime createdAt;
}

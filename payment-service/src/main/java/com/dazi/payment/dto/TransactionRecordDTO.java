package com.dazi.payment.dto;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 交易记录DTO
 */
@Data
public class TransactionRecordDTO {
    
    private Long id;
    
    private Integer type;
    
    private String typeName;
    
    private String currencyType;
    
    private String currencyName;
    
    private Integer amount;
    
    private Integer balance;
    
    private String description;
    
    private LocalDateTime createdAt;
}

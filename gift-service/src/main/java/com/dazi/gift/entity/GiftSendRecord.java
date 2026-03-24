package com.dazi.gift.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "gift_send_record", indexes = {
    @Index(name = "idx_sender_id", columnList = "sender_id"),
    @Index(name = "idx_receiver_id", columnList = "receiver_id"),
    @Index(name = "idx_created_at", columnList = "created_at")
})
public class GiftSendRecord {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "sender_id", nullable = false)
    private Long senderId;
    
    @Column(name = "receiver_id", nullable = false)
    private Long receiverId;
    
    @Column(name = "gift_id", nullable = false)
    private Long giftId;
    
    @Column(name = "gift_name", length = 64)
    private String giftName;
    
    @Column(name = "gift_icon", length = 256)
    private String giftIcon;
    
    @Column(name = "price", nullable = false)
    private Integer price;
    
    @Column(name = "message", length = 256)
    private String message;
    
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}

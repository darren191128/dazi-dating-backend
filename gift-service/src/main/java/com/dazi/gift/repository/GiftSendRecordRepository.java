package com.dazi.gift.repository;

import com.dazi.gift.entity.GiftSendRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GiftSendRecordRepository extends JpaRepository<GiftSendRecord, Long> {
    
    Page<GiftSendRecord> findBySenderIdOrderByCreatedAtDesc(Long senderId, Pageable pageable);
    
    Page<GiftSendRecord> findByReceiverIdOrderByCreatedAtDesc(Long receiverId, Pageable pageable);
}

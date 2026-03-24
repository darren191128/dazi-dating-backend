package com.dazi.gift.service;

import com.dazi.common.result.Result;
import com.dazi.gift.dto.GiftSendRecordDTO;
import com.dazi.gift.dto.PageResult;
import com.dazi.gift.dto.SendGiftRequest;
import com.dazi.gift.entity.Gift;
import com.dazi.gift.entity.GiftCategory;
import com.dazi.gift.entity.GiftSendRecord;
import com.dazi.gift.repository.GiftCategoryRepository;
import com.dazi.gift.repository.GiftRepository;
import com.dazi.gift.repository.GiftSendRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GiftService {
    
    private final GiftCategoryRepository giftCategoryRepository;
    private final GiftRepository giftRepository;
    private final GiftSendRecordRepository giftSendRecordRepository;
    private final RedisTemplate<String, Object> redisTemplate;
    
    private static final String USER_BALANCE_KEY = "user:balance:";
    private static final String USER_VIP_KEY = "user:vip:";
    
    /**
     * 获取礼物分类列表
     */
    public Result<List<GiftCategory>> getCategories() {
        List<GiftCategory> categories = giftCategoryRepository.findByStatusOrderBySortOrderAsc(1);
        return Result.success(categories);
    }
    
    /**
     * 获取礼物列表
     */
    public Result<List<Gift>> getGifts(Long categoryId) {
        List<Gift> gifts;
        if (categoryId != null) {
            gifts = giftRepository.findByCategoryIdAndStatusOrderBySortOrderAsc(categoryId, 1);
        } else {
            gifts = giftRepository.findByStatusOrderBySortOrderAsc(1);
        }
        return Result.success(gifts);
    }
    
    /**
     * 赠送礼物
     */
    @Transactional
    public Result<Void> sendGift(Long senderId, SendGiftRequest request) {
        // 1. 查询礼物信息
        Gift gift = giftRepository.findById(request.getGiftId()).orElse(null);
        if (gift == null) {
            return Result.error("礼物不存在");
        }
        
        if (gift.getStatus() != 1) {
            return Result.error("礼物已下架");
        }
        
        // 2. 检查是否是VIP专属礼物
        if (gift.getVipOnly() == 1) {
            Boolean isVip = (Boolean) redisTemplate.opsForValue().get(USER_VIP_KEY + senderId);
            if (isVip == null || !isVip) {
                return Result.error("该礼物为VIP专属");
            }
        }
        
        // 3. 检查不能送给自己
        if (senderId.equals(request.getReceiverId())) {
            return Result.error("不能给自己赠送礼物");
        }
        
        // 4. 检查余额（从Redis获取，实际应该从用户服务查询）
        Integer balance = (Integer) redisTemplate.opsForValue().get(USER_BALANCE_KEY + senderId);
        if (balance == null) {
            balance = 0;
        }
        
        if (balance < gift.getPrice()) {
            return Result.error("余额不足");
        }
        
        // 5. 扣减余额
        redisTemplate.opsForValue().set(USER_BALANCE_KEY + senderId, balance - gift.getPrice());
        
        // 6. 创建赠送记录
        GiftSendRecord record = new GiftSendRecord();
        record.setSenderId(senderId);
        record.setReceiverId(request.getReceiverId());
        record.setGiftId(gift.getId());
        record.setGiftName(gift.getName());
        record.setGiftIcon(gift.getIcon());
        record.setPrice(gift.getPrice());
        record.setMessage(request.getMessage());
        giftSendRecordRepository.save(record);
        
        // 7. 增加亲密度（异步调用用户服务）
        // TODO: 调用用户服务增加亲密度
        
        return Result.success();
    }
    
    /**
     * 获取赠送记录
     */
    public Result<PageResult<GiftSendRecordDTO>> getRecords(Long userId, String type, Integer page, Integer pageSize) {
        Pageable pageable = PageRequest.of(page - 1, pageSize);
        Page<GiftSendRecord> recordPage;
        
        if ("send".equals(type)) {
            recordPage = giftSendRecordRepository.findBySenderIdOrderByCreatedAtDesc(userId, pageable);
        } else if ("receive".equals(type)) {
            recordPage = giftSendRecordRepository.findByReceiverIdOrderByCreatedAtDesc(userId, pageable);
        } else {
            // 默认查询收到的礼物
            recordPage = giftSendRecordRepository.findByReceiverIdOrderByCreatedAtDesc(userId, pageable);
        }
        
        List<GiftSendRecordDTO> dtoList = recordPage.getContent().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        
        PageResult<GiftSendRecordDTO> result = new PageResult<>(
                dtoList,
                recordPage.getTotalElements(),
                page,
                pageSize
        );
        
        return Result.success(result);
    }
    
    private GiftSendRecordDTO convertToDTO(GiftSendRecord record) {
        GiftSendRecordDTO dto = new GiftSendRecordDTO();
        dto.setId(record.getId());
        dto.setSenderId(record.getSenderId());
        dto.setReceiverId(record.getReceiverId());
        dto.setGiftId(record.getGiftId());
        dto.setGiftName(record.getGiftName());
        dto.setGiftIcon(record.getGiftIcon());
        dto.setPrice(record.getPrice());
        dto.setMessage(record.getMessage());
        dto.setCreatedAt(record.getCreatedAt());
        return dto;
    }
}

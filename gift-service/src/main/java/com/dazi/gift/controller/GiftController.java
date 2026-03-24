package com.dazi.gift.controller;

import com.dazi.common.result.Result;
import com.dazi.gift.dto.GiftSendRecordDTO;
import com.dazi.gift.dto.PageResult;
import com.dazi.gift.dto.SendGiftRequest;
import com.dazi.gift.entity.Gift;
import com.dazi.gift.entity.GiftCategory;
import com.dazi.gift.service.GiftService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/gift")
@RequiredArgsConstructor
@Validated
public class GiftController {
    
    private final GiftService giftService;
    
    /**
     * 获取礼物分类
     */
    @GetMapping("/categories")
    public Result<List<GiftCategory>> getCategories() {
        return giftService.getCategories();
    }
    
    /**
     * 获取礼物列表
     */
    @GetMapping("/list")
    public Result<List<Gift>> getGifts(@RequestParam(required = false) Long categoryId) {
        return giftService.getGifts(categoryId);
    }
    
    /**
     * 赠送礼物
     */
    @PostMapping("/send")
    public Result<Void> sendGift(
            HttpServletRequest request,
            @Valid @RequestBody SendGiftRequest requestBody) {
        Long userId = (Long) request.getAttribute("currentUserId");
        return giftService.sendGift(userId, requestBody);
    }
    
    /**
     * 获取赠送记录
     */
    @GetMapping("/records")
    public Result<PageResult<GiftSendRecordDTO>> getRecords(
            HttpServletRequest request,
            @RequestParam(required = false) String type,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "20") Integer pageSize) {
        Long userId = (Long) request.getAttribute("currentUserId");
        return giftService.getRecords(userId, type, page, pageSize);
    }
}

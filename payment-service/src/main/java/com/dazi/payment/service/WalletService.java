package com.dazi.payment.service;

import com.dazi.common.result.Result;
import com.dazi.payment.dto.*;

import java.util.List;

/**
 * 钱包服务接口
 */
public interface WalletService {
    
    /**
     * 获取用户钱包
     */
    Result<WalletDTO> getWallet(Long userId);
    
    /**
     * 获取交易记录
     */
    Result<List<TransactionRecordDTO>> getTransactions(Long userId, String currencyType, Integer type, Integer page, Integer pageSize);
    
    /**
     * 创建充值订单
     */
    Result<RechargeOrderDTO> createRechargeOrder(Long userId, Integer packageId);
    
    /**
     * 消费（扣除余额）
     */
    Result<Void> consume(Long userId, ConsumeRequest request);
    
    /**
     * 获取充值套餐列表
     */
    Result<List<RechargePackageDTO>> getRechargePackages();
    
    /**
     * 增加金币（用于充值回调等）
     */
    Result<Void> addGoldCoin(Long userId, Integer amount, String description);
    
    /**
     * 增加积分
     */
    Result<Void> addPoint(Long userId, Integer amount, String description);
    
    /**
     * 初始化用户钱包
     */
    void initUserWallet(Long userId);
}

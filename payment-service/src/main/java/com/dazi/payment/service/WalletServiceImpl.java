package com.dazi.payment.service;

import com.dazi.common.result.Result;
import com.dazi.payment.dto.*;
import com.dazi.payment.entity.RechargeOrder;
import com.dazi.payment.entity.TransactionRecord;
import com.dazi.payment.entity.UserWallet;
import com.dazi.payment.repository.RechargeOrderRepository;
import com.dazi.payment.repository.TransactionRecordRepository;
import com.dazi.payment.repository.UserWalletRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 钱包服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WalletServiceImpl implements WalletService {
    
    private final UserWalletRepository userWalletRepository;
    private final TransactionRecordRepository transactionRecordRepository;
    private final RechargeOrderRepository rechargeOrderRepository;
    
    // 充值套餐配置
    private static final List<RechargePackageDTO> RECHARGE_PACKAGES = new ArrayList<>();
    
    static {
        RECHARGE_PACKAGES.add(createPackage(1, "60金币", 6, 60, 0, false));
        RECHARGE_PACKAGES.add(createPackage(2, "330金币", 30, 300, 30, false));
        RECHARGE_PACKAGES.add(createPackage(3, "760金币", 68, 680, 80, true));
        RECHARGE_PACKAGES.add(createPackage(4, "1480金币", 128, 1280, 200, false));
        RECHARGE_PACKAGES.add(createPackage(5, "3880金币", 328, 3280, 600, true));
        RECHARGE_PACKAGES.add(createPackage(6, "7980金币", 648, 6480, 1500, true));
    }
    
    private static RechargePackageDTO createPackage(int id, String name, int price, int coin, int bonus, boolean isHot) {
        RechargePackageDTO dto = new RechargePackageDTO();
        dto.setId(id);
        dto.setName(name);
        dto.setPrice(price);
        dto.setGoldCoin(coin);
        dto.setBonusCoin(bonus);
        dto.setIsHot(isHot);
        if (bonus > 0) {
            dto.setDescription("送" + bonus + "金币");
        }
        return dto;
    }
    
    @Override
    public Result<WalletDTO> getWallet(Long userId) {
        UserWallet wallet = userWalletRepository.findByUserId(userId);
        if (wallet == null) {
            // 初始化钱包
            initUserWallet(userId);
            wallet = userWalletRepository.findByUserId(userId);
        }
        
        WalletDTO dto = new WalletDTO();
        dto.setUserId(userId);
        dto.setGoldCoin(wallet.getGoldCoin());
        dto.setPoint(wallet.getPoint());
        dto.setTotalRecharge(wallet.getTotalRecharge());
        dto.setTotalConsume(wallet.getTotalConsume());
        
        return Result.success(dto);
    }
    
    @Override
    public Result<List<TransactionRecordDTO>> getTransactions(Long userId, String currencyType, Integer type, 
                                                               Integer page, Integer pageSize) {
        if (page == null || page < 1) page = 1;
        if (pageSize == null || pageSize < 1 || pageSize > 100) pageSize = 20;
        
        int offset = (page - 1) * pageSize;
        List<TransactionRecord> records;
        int total;
        
        if (currencyType != null && !currencyType.isEmpty() && type != null) {
            // 同时筛选货币类型和交易类型
            records = transactionRecordRepository.selectList(
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<TransactionRecord>()
                    .eq(TransactionRecord::getUserId, userId)
                    .eq(TransactionRecord::getCurrencyType, currencyType)
                    .eq(TransactionRecord::getType, type)
                    .orderByDesc(TransactionRecord::getCreatedAt)
                    .last("LIMIT " + offset + ", " + pageSize)
            );
            total = (int) transactionRecordRepository.selectCount(
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<TransactionRecord>()
                    .eq(TransactionRecord::getUserId, userId)
                    .eq(TransactionRecord::getCurrencyType, currencyType)
                    .eq(TransactionRecord::getType, type)
            );
        } else if (currencyType != null && !currencyType.isEmpty()) {
            records = transactionRecordRepository.findByUserIdAndCurrencyType(userId, currencyType, offset, pageSize);
            total = transactionRecordRepository.countByUserIdAndCurrencyType(userId, currencyType);
        } else if (type != null) {
            records = transactionRecordRepository.findByUserIdAndType(userId, type, offset, pageSize);
            total = transactionRecordRepository.countByUserIdAndType(userId, type);
        } else {
            records = transactionRecordRepository.findByUserIdWithPagination(userId, offset, pageSize);
            total = transactionRecordRepository.countByUserId(userId);
        }
        
        List<TransactionRecordDTO> dtoList = new ArrayList<>();
        for (TransactionRecord record : records) {
            dtoList.add(convertToTransactionDTO(record));
        }
        
        return Result.success(dtoList);
    }
    
    @Override
    @Transactional
    public Result<RechargeOrderDTO> createRechargeOrder(Long userId, Integer packageId) {
        RechargePackageDTO packageDTO = RECHARGE_PACKAGES.stream()
                .filter(p -> p.getId().equals(packageId))
                .findFirst()
                .orElse(null);
        
        if (packageDTO == null) {
            return Result.error("充值套餐不存在");
        }
        
        String orderNo = generateOrderNo();
        
        RechargeOrder order = new RechargeOrder();
        order.setOrderNo(orderNo);
        order.setUserId(userId);
        order.setAmount(new BigDecimal(packageDTO.getPrice()));
        order.setGoldCoin(packageDTO.getGoldCoin());
        order.setBonusCoin(packageDTO.getBonusCoin());
        order.setStatus(RechargeOrder.STATUS_PENDING);
        order.setPayType("wxpay");
        
        rechargeOrderRepository.insert(order);
        
        log.info("创建充值订单: orderNo={}, userId={}, amount={}", orderNo, userId, packageDTO.getPrice());
        
        return Result.success(convertToRechargeOrderDTO(order));
    }
    
    @Override
    @Transactional
    public Result<Void> consume(Long userId, ConsumeRequest request) {
        String currencyType = request.getCurrencyType();
        Integer amount = request.getAmount();
        
        UserWallet wallet = userWalletRepository.findByUserId(userId);
        if (wallet == null) {
            return Result.error("钱包不存在");
        }
        
        boolean success;
        int balance;
        
        if (TransactionRecord.CURRENCY_GOLD_COIN.equals(currencyType)) {
            if (wallet.getGoldCoin() < amount) {
                return Result.error("金币余额不足");
            }
            success = userWalletRepository.deductGoldCoin(userId, amount, BigDecimal.ZERO) > 0;
            balance = wallet.getGoldCoin() - amount;
        } else if (TransactionRecord.CURRENCY_POINT.equals(currencyType)) {
            if (wallet.getPoint() < amount) {
                return Result.error("积分余额不足");
            }
            success = userWalletRepository.deductPoint(userId, amount) > 0;
            balance = wallet.getPoint() - amount;
        } else {
            return Result.error("不支持的货币类型");
        }
        
        if (!success) {
            return Result.error("消费失败，请重试");
        }
        
        // 记录交易
        TransactionRecord record = new TransactionRecord();
        record.setUserId(userId);
        record.setType(TransactionRecord.TYPE_CONSUME);
        record.setCurrencyType(currencyType);
        record.setAmount(-amount);
        record.setBalance(balance);
        record.setRelatedId(request.getRelatedId());
        record.setDescription(request.getDescription());
        transactionRecordRepository.insert(record);
        
        log.info("用户消费: userId={}, currencyType={}, amount={}", userId, currencyType, amount);
        
        return Result.success();
    }
    
    @Override
    public Result<List<RechargePackageDTO>> getRechargePackages() {
        return Result.success(RECHARGE_PACKAGES);
    }
    
    @Override
    @Transactional
    public Result<Void> addGoldCoin(Long userId, Integer amount, String description) {
        UserWallet wallet = userWalletRepository.findByUserId(userId);
        if (wallet == null) {
            initUserWallet(userId);
            wallet = userWalletRepository.findByUserId(userId);
        }
        
        userWalletRepository.addGoldCoin(userId, amount, BigDecimal.ZERO);
        
        int newBalance = wallet.getGoldCoin() + amount;
        
        // 记录交易
        TransactionRecord record = new TransactionRecord();
        record.setUserId(userId);
        record.setType(TransactionRecord.TYPE_SYSTEM);
        record.setCurrencyType(TransactionRecord.CURRENCY_GOLD_COIN);
        record.setAmount(amount);
        record.setBalance(newBalance);
        record.setDescription(description);
        transactionRecordRepository.insert(record);
        
        log.info("增加金币: userId={}, amount={}, description={}", userId, amount, description);
        
        return Result.success();
    }
    
    @Override
    @Transactional
    public Result<Void> addPoint(Long userId, Integer amount, String description) {
        UserWallet wallet = userWalletRepository.findByUserId(userId);
        if (wallet == null) {
            initUserWallet(userId);
            wallet = userWalletRepository.findByUserId(userId);
        }
        
        userWalletRepository.addPoint(userId, amount);
        
        int newBalance = wallet.getPoint() + amount;
        
        // 记录交易
        TransactionRecord record = new TransactionRecord();
        record.setUserId(userId);
        record.setType(TransactionRecord.TYPE_SYSTEM);
        record.setCurrencyType(TransactionRecord.CURRENCY_POINT);
        record.setAmount(amount);
        record.setBalance(newBalance);
        record.setDescription(description);
        transactionRecordRepository.insert(record);
        
        log.info("增加积分: userId={}, amount={}, description={}", userId, amount, description);
        
        return Result.success();
    }
    
    @Override
    @Transactional
    public void initUserWallet(Long userId) {
        UserWallet existing = userWalletRepository.findByUserId(userId);
        if (existing != null) {
            return;
        }
        
        UserWallet wallet = new UserWallet();
        wallet.setUserId(userId);
        wallet.setGoldCoin(0);
        wallet.setPoint(0);
        wallet.setTotalRecharge(BigDecimal.ZERO);
        wallet.setTotalConsume(BigDecimal.ZERO);
        
        userWalletRepository.insert(wallet);
        
        log.info("初始化用户钱包: userId={}", userId);
    }
    
    /**
     * 充值回调处理（支付成功后调用）
     */
    @Transactional
    public Result<Void> handleRechargeCallback(String orderNo, String wxPayTradeNo) {
        RechargeOrder order = rechargeOrderRepository.findByOrderNo(orderNo);
        if (order == null) {
            return Result.error("订单不存在");
        }
        
        if (order.getStatus() == RechargeOrder.STATUS_PAID) {
            return Result.success(); // 已处理过
        }
        
        if (order.getStatus() != RechargeOrder.STATUS_PENDING) {
            return Result.error("订单状态异常");
        }
        
        Long userId = order.getUserId();
        Integer totalCoin = order.getGoldCoin() + order.getBonusCoin();
        
        // 更新订单状态
        order.setStatus(RechargeOrder.STATUS_PAID);
        order.setWxPayTradeNo(wxPayTradeNo);
        order.setPayTime(LocalDateTime.now());
        rechargeOrderRepository.updateById(order);
        
        // 增加金币
        UserWallet wallet = userWalletRepository.findByUserId(userId);
        if (wallet == null) {
            initUserWallet(userId);
            wallet = userWalletRepository.findByUserId(userId);
        }
        
        userWalletRepository.addGoldCoin(userId, totalCoin, order.getAmount());
        
        int newBalance = wallet.getGoldCoin() + totalCoin;
        
        // 记录交易
        TransactionRecord record = new TransactionRecord();
        record.setUserId(userId);
        record.setType(TransactionRecord.TYPE_RECHARGE);
        record.setCurrencyType(TransactionRecord.CURRENCY_GOLD_COIN);
        record.setAmount(totalCoin);
        record.setBalance(newBalance);
        record.setRelatedId(order.getId());
        record.setDescription("充值" + order.getAmount() + "元，获得" + totalCoin + "金币");
        transactionRecordRepository.insert(record);
        
        log.info("充值成功: orderNo={}, userId={}, amount={}, coin={}", 
                orderNo, userId, order.getAmount(), totalCoin);
        
        return Result.success();
    }
    
    private TransactionRecordDTO convertToTransactionDTO(TransactionRecord record) {
        TransactionRecordDTO dto = new TransactionRecordDTO();
        dto.setId(record.getId());
        dto.setType(record.getType());
        dto.setTypeName(getTypeName(record.getType()));
        dto.setCurrencyType(record.getCurrencyType());
        dto.setCurrencyName(getCurrencyName(record.getCurrencyType()));
        dto.setAmount(record.getAmount());
        dto.setBalance(record.getBalance());
        dto.setDescription(record.getDescription());
        dto.setCreatedAt(record.getCreatedAt());
        return dto;
    }
    
    private RechargeOrderDTO convertToRechargeOrderDTO(RechargeOrder order) {
        RechargeOrderDTO dto = new RechargeOrderDTO();
        dto.setOrderNo(order.getOrderNo());
        dto.setAmount(order.getAmount());
        dto.setGoldCoin(order.getGoldCoin());
        dto.setBonusCoin(order.getBonusCoin());
        dto.setStatus(order.getStatus());
        dto.setStatusName(getOrderStatusName(order.getStatus()));
        dto.setPayTime(order.getPayTime());
        dto.setCreatedAt(order.getCreatedAt());
        return dto;
    }
    
    private String getTypeName(Integer type) {
        return switch (type) {
            case 1 -> "充值";
            case 2 -> "消费";
            case 3 -> "赠送";
            case 4 -> "接收";
            case 5 -> "系统赠送";
            case 6 -> "签到奖励";
            default -> "其他";
        };
    }
    
    private String getCurrencyName(String currencyType) {
        return TransactionRecord.CURRENCY_GOLD_COIN.equals(currencyType) ? "金币" : "积分";
    }
    
    private String getOrderStatusName(Integer status) {
        return switch (status) {
            case 0 -> "待支付";
            case 1 -> "已支付";
            case 2 -> "已取消";
            default -> "未知";
        };
    }
    
    private String generateOrderNo() {
        String dateStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String uuid = UUID.randomUUID().toString().substring(0, 8);
        return "RC" + dateStr + uuid;
    }
}

-- P1阶段：虚拟货币系统 + 每日签到 数据库脚本
-- 创建时间: 2026-03-24

-- ==================== 虚拟货币系统 ====================

-- 1. 货币类型表
CREATE TABLE IF NOT EXISTS virtual_currency (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    code VARCHAR(32) NOT NULL COMMENT '货币编码：gold_coin-金币，point-积分',
    name VARCHAR(64) NOT NULL COMMENT '货币名称',
    description VARCHAR(256) COMMENT '描述',
    exchange_rate DECIMAL(10,2) COMMENT '兑换比例（相对于人民币）',
    status TINYINT DEFAULT 1 COMMENT '状态：1启用 0禁用',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY uk_code (code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='货币类型表';

-- 初始化货币类型
INSERT INTO virtual_currency (code, name, description, exchange_rate, status) VALUES
('gold_coin', '金币', '平台虚拟货币，可用于购买礼物、解锁特权等', 0.10, 1),
('point', '积分', '用户行为奖励，可用于兑换金币或抵扣消费', 0.01, 1)
ON DUPLICATE KEY UPDATE status = 1;

-- 2. 用户钱包表
CREATE TABLE IF NOT EXISTS user_wallet (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL COMMENT '用户ID',
    gold_coin INT DEFAULT 0 COMMENT '金币余额',
    point INT DEFAULT 0 COMMENT '积分余额',
    total_recharge DECIMAL(10,2) DEFAULT 0 COMMENT '累计充值金额（元）',
    total_consume DECIMAL(10,2) DEFAULT 0 COMMENT '累计消费金额（元）',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY uk_user_id (user_id),
    INDEX idx_user_id (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户钱包表';

-- 3. 交易记录表
CREATE TABLE IF NOT EXISTS transaction_record (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL COMMENT '用户ID',
    type TINYINT NOT NULL COMMENT '类型：1充值 2消费 3赠送 4接收 5系统赠送 6签到奖励',
    currency_type VARCHAR(32) NOT NULL COMMENT '货币类型：gold_coin/point',
    amount INT NOT NULL COMMENT '变动金额（正数增加，负数减少）',
    balance INT NOT NULL COMMENT '变动后余额',
    related_id BIGINT COMMENT '关联ID（订单ID、礼物ID等）',
    description VARCHAR(256) COMMENT '交易描述',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_user_id (user_id),
    INDEX idx_type (type),
    INDEX idx_currency_type (currency_type),
    INDEX idx_created_at (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='交易记录表';

-- 4. 充值订单表（扩展原有payment_order）
CREATE TABLE IF NOT EXISTS recharge_order (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    order_no VARCHAR(64) NOT NULL COMMENT '订单号',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    amount DECIMAL(10,2) NOT NULL COMMENT '充值金额（元）',
    gold_coin INT NOT NULL COMMENT '获得金币数量',
    bonus_coin INT DEFAULT 0 COMMENT '赠送金币数量',
    status TINYINT DEFAULT 0 COMMENT '状态：0待支付 1已支付 2已取消',
    pay_type VARCHAR(32) DEFAULT 'wxpay' COMMENT '支付方式：wxpay-微信支付',
    wx_pay_trade_no VARCHAR(64) COMMENT '微信支付交易号',
    pay_time DATETIME COMMENT '支付时间',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY uk_order_no (order_no),
    INDEX idx_user_id (user_id),
    INDEX idx_status (status),
    INDEX idx_created_at (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='充值订单表';

-- 初始化充值套餐配置（插入到系统配置表或作为常量使用）
-- 充值套餐：
-- 6元 = 60金币
-- 30元 = 300金币（送30）
-- 68元 = 680金币（送80）
-- 128元 = 1280金币（送200）
-- 328元 = 3280金币（送600）
-- 648元 = 6480金币（送1500）

-- ==================== 每日签到系统 ====================

-- 5. 签到记录表
CREATE TABLE IF NOT EXISTS user_checkin (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL COMMENT '用户ID',
    checkin_date DATE NOT NULL COMMENT '签到日期',
    consecutive_days INT DEFAULT 1 COMMENT '连续签到天数',
    reward_type VARCHAR(32) COMMENT '奖励类型：point-积分',
    reward_amount INT COMMENT '奖励数量',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY uk_user_date (user_id, checkin_date),
    INDEX idx_user_id (user_id),
    INDEX idx_checkin_date (checkin_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='签到记录表';

-- 签到奖励规则（作为常量使用）：
-- 第1天：10积分
-- 第2天：15积分
-- 第3天：20积分
-- 第4天：25积分
-- 第5天：30积分
-- 第6天：35积分
-- 第7天：50积分（大奖）
-- 连续7天后重置

-- 6. 用户签到统计表（可选，用于快速查询）
CREATE TABLE IF NOT EXISTS user_checkin_stats (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL COMMENT '用户ID',
    total_checkin_days INT DEFAULT 0 COMMENT '累计签到天数',
    max_consecutive_days INT DEFAULT 0 COMMENT '最大连续签到天数',
    current_consecutive_days INT DEFAULT 0 COMMENT '当前连续签到天数',
    last_checkin_date DATE COMMENT '最后签到日期',
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY uk_user_id (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户签到统计表';

CREATE TABLE vip_package (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(64) NOT NULL COMMENT '套餐名称',
    description VARCHAR(256) COMMENT '套餐描述',
    duration_days INT NOT NULL COMMENT '时长（天）',
    price DECIMAL(10,2) NOT NULL COMMENT '价格',
    original_price DECIMAL(10,2) COMMENT '原价',
    sort_order INT DEFAULT 0 COMMENT '排序',
    is_recommended TINYINT DEFAULT 0 COMMENT '是否推荐',
    status TINYINT DEFAULT 1 COMMENT '状态：0下架 1上架',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) COMMENT='VIP套餐表';

CREATE TABLE user_vip (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL COMMENT '用户ID',
    package_id BIGINT NOT NULL COMMENT '套餐ID',
    start_time DATETIME NOT NULL COMMENT '开始时间',
    end_time DATETIME NOT NULL COMMENT '结束时间',
    status TINYINT DEFAULT 1 COMMENT '状态：0过期 1有效',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY uk_user_id (user_id),
    INDEX idx_end_time (end_time)
) COMMENT='用户VIP记录表';

CREATE TABLE vip_privilege (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    code VARCHAR(32) NOT NULL COMMENT '特权编码',
    name VARCHAR(64) NOT NULL COMMENT '特权名称',
    description VARCHAR(256) COMMENT '特权描述',
    icon VARCHAR(256) COMMENT '图标URL',
    sort_order INT DEFAULT 0 COMMENT '排序',
    status TINYINT DEFAULT 1 COMMENT '状态',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY uk_code (code)
) COMMENT='VIP特权表';

-- 初始化VIP特权数据
INSERT INTO vip_privilege (code, name, description, sort_order, status) VALUES
('unlimited_swipe', '无限滑动', '每日滑动次数无限制', 1, 1),
('visitor_stealth', '访客隐身', '访问他人主页不留记录', 2, 1),
('advanced_filter', '高级筛选', '解锁身高/学历/收入筛选', 3, 1),
('read_receipt', '已读回执', '查看消息是否已读', 4, 1),
('vip_badge', '专属标识', 'VIP专属头像框/标识', 5, 1),
('priority_recommend', '优先推荐', '在推荐列表中优先展示', 6, 1),
('view_visitors', '查看访客', '查看谁访问过我的主页', 7, 1),
('super_like', '超级喜欢', '每日3次超级喜欢', 8, 1);

-- 初始化VIP套餐数据
INSERT INTO vip_package (name, description, duration_days, price, original_price, sort_order, is_recommended, status) VALUES
('月度会员', '畅享30天VIP特权', 30, 28.00, 38.00, 1, 0, 1),
('季度会员', '畅享90天VIP特权，立省20%', 90, 68.00, 98.00, 2, 0, 1),
('年度会员', '畅享365天VIP特权，立省40%', 365, 198.00, 298.00, 3, 1, 1);

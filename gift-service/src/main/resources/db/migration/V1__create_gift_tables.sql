-- 礼物分类表
CREATE TABLE IF NOT EXISTS gift_category (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(64) NOT NULL COMMENT '分类名称',
    icon VARCHAR(256) COMMENT '图标',
    sort_order INT DEFAULT 0 COMMENT '排序',
    status TINYINT DEFAULT 1 COMMENT '状态',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='礼物分类表';

-- 礼物表
CREATE TABLE IF NOT EXISTS gift (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    category_id BIGINT NOT NULL COMMENT '分类ID',
    name VARCHAR(64) NOT NULL COMMENT '礼物名称',
    icon VARCHAR(256) NOT NULL COMMENT '礼物图标',
    animation VARCHAR(256) COMMENT '动画效果URL',
    price INT NOT NULL COMMENT '价格（金币）',
    vip_only TINYINT DEFAULT 0 COMMENT '是否VIP专属',
    description VARCHAR(256) COMMENT '描述',
    sort_order INT DEFAULT 0 COMMENT '排序',
    status TINYINT DEFAULT 1 COMMENT '状态',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_category_id (category_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='礼物表';

-- 赠送记录表
CREATE TABLE IF NOT EXISTS gift_send_record (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    sender_id BIGINT NOT NULL COMMENT '赠送者ID',
    receiver_id BIGINT NOT NULL COMMENT '接收者ID',
    gift_id BIGINT NOT NULL COMMENT '礼物ID',
    gift_name VARCHAR(64) COMMENT '礼物名称（冗余）',
    gift_icon VARCHAR(256) COMMENT '礼物图标（冗余）',
    price INT NOT NULL COMMENT '价格',
    message VARCHAR(256) COMMENT '留言',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_sender_id (sender_id),
    INDEX idx_receiver_id (receiver_id),
    INDEX idx_created_at (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='礼物赠送记录表';

-- 初始化礼物分类数据
INSERT INTO gift_category (name, icon, sort_order, status) VALUES
('热门', 'https://cdn.dazi.com/category/hot.png', 1, 1),
('豪华', 'https://cdn.dazi.com/category/luxury.png', 2, 1),
('专属', 'https://cdn.dazi.com/category/exclusive.png', 3, 1);

-- 初始化礼物数据
INSERT INTO gift (category_id, name, icon, price, vip_only, description, sort_order, status) VALUES
(1, '玫瑰', 'https://cdn.dazi.com/gift/rose.png', 10, 0, '一朵美丽的玫瑰花', 1, 1),
(1, '爱心', 'https://cdn.dazi.com/gift/heart.png', 50, 0, '一颗真挚的爱心', 2, 1),
(1, '蛋糕', 'https://cdn.dazi.com/gift/cake.png', 100, 0, '甜蜜的生日蛋糕', 3, 1),
(2, '钻戒', 'https://cdn.dazi.com/gift/diamond.png', 500, 0, '闪耀的钻戒', 4, 1),
(2, '跑车', 'https://cdn.dazi.com/gift/car.png', 1000, 0, '豪华跑车', 5, 1),
(2, '游艇', 'https://cdn.dazi.com/gift/yacht.png', 2000, 0, '私人游艇', 6, 1),
(3, '城堡', 'https://cdn.dazi.com/gift/castle.png', 5000, 1, '梦幻城堡（VIP专属）', 7, 1),
(3, '皇冠', 'https://cdn.dazi.com/gift/crown.png', 10000, 1, '至尊皇冠（VIP专属）', 8, 1);

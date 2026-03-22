-- 创建用户表
CREATE TABLE IF NOT EXISTS `user` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '用户ID',
    `open_id` VARCHAR(64) NOT NULL COMMENT '微信OpenID',
    `union_id` VARCHAR(64) DEFAULT NULL COMMENT '微信UnionID',
    `nickname` VARCHAR(50) DEFAULT NULL COMMENT '昵称',
    `avatar` VARCHAR(255) DEFAULT NULL COMMENT '头像URL',
    `gender` TINYINT DEFAULT 0 COMMENT '性别：0-未知，1-男，2-女',
    `age` INT DEFAULT NULL COMMENT '年龄',
    `phone` VARCHAR(20) DEFAULT NULL COMMENT '手机号',
    `level` INT DEFAULT 1 COMMENT '等级',
    `exp` INT DEFAULT 0 COMMENT '经验值',
    `credit_score` INT DEFAULT 100 COMMENT '信用分',
    `status` TINYINT DEFAULT 1 COMMENT '状态：0-禁用，1-正常',
    `last_login_time` DATETIME DEFAULT NULL COMMENT '最后登录时间',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '是否删除：0-否，1-是',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_open_id` (`open_id`),
    KEY `idx_phone` (`phone`),
    KEY `idx_level` (`level`),
    KEY `idx_credit_score` (`credit_score`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 创建用户资料表
CREATE TABLE IF NOT EXISTS `user_profile` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '资料ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `bio` VARCHAR(500) DEFAULT NULL COMMENT '个性签名',
    `birthday` DATE DEFAULT NULL COMMENT '生日',
    `zodiac` VARCHAR(20) DEFAULT NULL COMMENT '星座',
    `chinese_zodiac` VARCHAR(20) DEFAULT NULL COMMENT '生肖',
    `longitude` DECIMAL(10, 7) DEFAULT NULL COMMENT '经度',
    `latitude` DECIMAL(10, 7) DEFAULT NULL COMMENT '纬度',
    `location` VARCHAR(100) DEFAULT NULL COMMENT '位置名称',
    `location_visible` TINYINT DEFAULT 1 COMMENT '位置可见：0-不可见，1-可见',
    `real_name_verified` TINYINT DEFAULT 0 COMMENT '实名认证：0-未认证，1-已认证',
    `real_name` VARCHAR(50) DEFAULT NULL COMMENT '真实姓名',
    `id_card` VARCHAR(18) DEFAULT NULL COMMENT '身份证号',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户资料表';

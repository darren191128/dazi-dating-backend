-- 创建活动表
CREATE TABLE IF NOT EXISTS `activity` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '活动ID',
    `user_id` BIGINT NOT NULL COMMENT '发布用户ID',
    `title` VARCHAR(100) NOT NULL COMMENT '活动标题',
    `description` TEXT COMMENT '活动描述',
    `type` INT NOT NULL COMMENT '活动类型：1-吃喝玩乐, 2-户外游玩, 3-亲子活动, 4-户外运动, 5-相亲交友',
    `type_name` VARCHAR(50) COMMENT '类型名称',
    `start_time` DATETIME NOT NULL COMMENT '开始时间',
    `end_time` DATETIME NOT NULL COMMENT '结束时间',
    `location` VARCHAR(200) COMMENT '活动地点',
    `longitude` DECIMAL(10, 7) COMMENT '经度',
    `latitude` DECIMAL(10, 7) COMMENT '纬度',
    `min_participants` INT DEFAULT 2 COMMENT '最小人数',
    `max_participants` INT NOT NULL COMMENT '最大人数',
    `current_participants` INT DEFAULT 0 COMMENT '当前人数',
    `payment_type` INT DEFAULT 1 COMMENT '付费类型：1-AA, 2-男A女免, 3-请客, 4-免费',
    `total_amount` DECIMAL(10, 2) DEFAULT 0 COMMENT '总费用',
    `per_person_amount` DECIMAL(10, 2) DEFAULT 0 COMMENT '人均费用',
    `cover_image` VARCHAR(255) COMMENT '封面图',
    `registration_deadline` DATETIME COMMENT '报名截止时间',
    `status` TINYINT DEFAULT 1 COMMENT '状态：0-取消, 1-正常, 2-结束',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '是否删除',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_type` (`type`),
    KEY `idx_status` (`status`),
    KEY `idx_start_time` (`start_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='活动表';

-- 创建活动参与者表
CREATE TABLE IF NOT EXISTS `activity_participant` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `activity_id` BIGINT NOT NULL COMMENT '活动ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `status` TINYINT DEFAULT 1 COMMENT '状态：0-取消, 1-正常',
    `join_time` DATETIME COMMENT '加入时间',
    `quit_time` DATETIME COMMENT '退出时间',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_activity_user` (`activity_id`, `user_id`),
    KEY `idx_activity_id` (`activity_id`),
    KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='活动参与者表';

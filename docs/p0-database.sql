-- ============================================
-- 搭子交友微信小程序 P0 功能数据库脚本
-- 包含动态、关注、访客、匹配等功能的数据库表
-- ============================================

-- ============================================
-- 动态相关表
-- ============================================

-- 动态表
CREATE TABLE IF NOT EXISTS `moment` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `content` TEXT NOT NULL COMMENT '动态内容',
    `images` VARCHAR(2000) DEFAULT NULL COMMENT '图片URL列表，JSON格式',
    `location` VARCHAR(255) DEFAULT NULL COMMENT '位置信息',
    `like_count` INT DEFAULT 0 COMMENT '点赞数',
    `comment_count` INT DEFAULT 0 COMMENT '评论数',
    `view_count` INT DEFAULT 0 COMMENT '浏览数',
    `is_top` TINYINT DEFAULT 0 COMMENT '是否置顶：0-否，1-是',
    `status` TINYINT DEFAULT 1 COMMENT '状态：0-禁用，1-正常',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT DEFAULT 0 COMMENT '是否删除：0-否，1-是',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_status` (`status`),
    KEY `idx_is_top` (`is_top`),
    KEY `idx_created_at` (`created_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='动态表';

-- 动态点赞表
CREATE TABLE IF NOT EXISTS `moment_like` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `moment_id` BIGINT NOT NULL COMMENT '动态ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_moment_user` (`moment_id`, `user_id`),
    KEY `idx_moment_id` (`moment_id`),
    KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='动态点赞表';

-- 动态评论表
CREATE TABLE IF NOT EXISTS `moment_comment` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `moment_id` BIGINT NOT NULL COMMENT '动态ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `parent_id` BIGINT DEFAULT NULL COMMENT '父评论ID，为空表示根评论',
    `content` TEXT NOT NULL COMMENT '评论内容',
    `like_count` INT DEFAULT 0 COMMENT '点赞数',
    `status` TINYINT DEFAULT 1 COMMENT '状态：0-禁用，1-正常',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `deleted` TINYINT DEFAULT 0 COMMENT '是否删除：0-否，1-是',
    PRIMARY KEY (`id`),
    KEY `idx_moment_id` (`moment_id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_parent_id` (`parent_id`),
    KEY `idx_created_at` (`created_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='动态评论表';

-- ============================================
-- 用户关系相关表
-- ============================================

-- 用户关注表
CREATE TABLE IF NOT EXISTS `user_follow` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `follow_user_id` BIGINT NOT NULL COMMENT '被关注用户ID',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_follow` (`user_id`, `follow_user_id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_follow_user_id` (`follow_user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户关注表';

-- 访客记录表
CREATE TABLE IF NOT EXISTS `user_visitor` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `user_id` BIGINT NOT NULL COMMENT '被访问用户ID',
    `visitor_id` BIGINT NOT NULL COMMENT '访客用户ID',
    `visit_count` INT DEFAULT 1 COMMENT '访问次数',
    `last_visit_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '最后访问时间',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_visitor` (`user_id`, `visitor_id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_visitor_id` (`visitor_id`),
    KEY `idx_last_visit_at` (`last_visit_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='访客记录表';

-- 用户匹配表
CREATE TABLE IF NOT EXISTS `user_match` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `target_user_id` BIGINT NOT NULL COMMENT '目标用户ID',
    `action` TINYINT NOT NULL COMMENT '操作：1-喜欢，2-不喜欢',
    `is_mutual` TINYINT DEFAULT 0 COMMENT '是否互相喜欢：0-否，1-是',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_target` (`user_id`, `target_user_id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_target_user_id` (`target_user_id`),
    KEY `idx_is_mutual` (`is_mutual`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户匹配表';

-- ============================================
-- 消息表扩展字段
-- ============================================

-- 为 message 表添加新字段（如果表已存在）
ALTER TABLE `message` 
ADD COLUMN IF NOT EXISTS `message_type` VARCHAR(20) DEFAULT 'text' COMMENT '消息内容类型: text-文本, voice-语音, image-图片' AFTER `type`,
ADD COLUMN IF NOT EXISTS `voice_url` VARCHAR(500) DEFAULT NULL COMMENT '语音URL' AFTER `image_url`,
ADD COLUMN IF NOT EXISTS `voice_duration` INT DEFAULT NULL COMMENT '语音时长(秒)' AFTER `voice_url`;

-- ============================================
-- 初始化数据（可选）
-- ============================================

-- 可以根据需要添加初始化数据

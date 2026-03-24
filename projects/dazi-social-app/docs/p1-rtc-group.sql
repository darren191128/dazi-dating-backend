-- =============================================
-- P1阶段：音视频通话 + 群聊功能数据库脚本
-- =============================================

-- 创建rtc服务数据库
CREATE DATABASE IF NOT EXISTS dazi_rtc CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE dazi_rtc;

-- =============================================
-- 通话记录表
-- =============================================
CREATE TABLE IF NOT EXISTS call_record (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    caller_id BIGINT NOT NULL COMMENT '主叫用户ID',
    callee_id BIGINT NOT NULL COMMENT '被叫用户ID',
    call_type TINYINT NOT NULL COMMENT '通话类型：1语音 2视频',
    status TINYINT NOT NULL COMMENT '状态：0待接听 1通话中 2已结束 3已拒绝 4未接听',
    start_time DATETIME COMMENT '开始时间',
    end_time DATETIME COMMENT '结束时间',
    duration INT COMMENT '通话时长（秒）',
    room_id VARCHAR(64) COMMENT '房间ID',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_caller_id (caller_id),
    INDEX idx_callee_id (callee_id),
    INDEX idx_created_at (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='通话记录表';

-- =============================================
-- 群聊功能表（在message-service数据库中）
-- =============================================

USE dazi_message;

-- 群聊表
CREATE TABLE IF NOT EXISTS chat_group (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(128) NOT NULL COMMENT '群名称',
    avatar VARCHAR(256) COMMENT '群头像',
    owner_id BIGINT NOT NULL COMMENT '群主ID',
    member_count INT DEFAULT 1 COMMENT '成员数量',
    max_member_count INT DEFAULT 200 COMMENT '最大成员数',
    notice VARCHAR(512) COMMENT '群公告',
    status TINYINT DEFAULT 1 COMMENT '状态：0解散 1正常',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_owner_id (owner_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='群聊表';

-- 群成员表
CREATE TABLE IF NOT EXISTS chat_group_member (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    group_id BIGINT NOT NULL COMMENT '群ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    nickname VARCHAR(64) COMMENT '群内昵称',
    role TINYINT DEFAULT 0 COMMENT '角色：0成员 1管理员 2群主',
    mute_until DATETIME COMMENT '禁言截止时间',
    join_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '加入时间',
    UNIQUE KEY uk_group_user (group_id, user_id),
    INDEX idx_group_id (group_id),
    INDEX idx_user_id (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='群成员表';

-- 群消息表
CREATE TABLE IF NOT EXISTS chat_group_message (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    group_id BIGINT NOT NULL COMMENT '群ID',
    sender_id BIGINT NOT NULL COMMENT '发送者ID',
    message_type TINYINT DEFAULT 1 COMMENT '消息类型：1文本 2图片 3语音',
    content TEXT COMMENT '消息内容',
    image_url VARCHAR(512) COMMENT '图片URL',
    voice_url VARCHAR(512) COMMENT '语音URL',
    voice_duration INT COMMENT '语音时长(秒)',
    at_user_ids VARCHAR(512) COMMENT '@成员ID列表，逗号分隔',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_group_id (group_id),
    INDEX idx_created_at (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='群消息表';

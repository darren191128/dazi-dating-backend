# 搭子交友微信小程序 - 管理后台数据库表结构

## 1. 管理员表 (admin_user)

```sql
CREATE TABLE `admin_user` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `username` VARCHAR(64) NOT NULL COMMENT '用户名',
    `password` VARCHAR(255) NOT NULL COMMENT '密码（加密）',
    `real_name` VARCHAR(64) DEFAULT NULL COMMENT '真实姓名',
    `phone` VARCHAR(20) DEFAULT NULL COMMENT '手机号',
    `email` VARCHAR(128) DEFAULT NULL COMMENT '邮箱',
    `avatar` VARCHAR(512) DEFAULT NULL COMMENT '头像URL',
    `role` TINYINT NOT NULL DEFAULT '2' COMMENT '角色：1超级管理员 2普通管理员 3运营',
    `status` TINYINT NOT NULL DEFAULT '1' COMMENT '状态：0禁用 1启用',
    `last_login_time` DATETIME DEFAULT NULL COMMENT '最后登录时间',
    `last_login_ip` VARCHAR(64) DEFAULT NULL COMMENT '最后登录IP',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT NOT NULL DEFAULT '0' COMMENT '软删除：0正常 1删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_username` (`username`),
    KEY `idx_role` (`role`),
    KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='管理员表';

-- 插入默认超级管理员（密码：admin123）
INSERT INTO `admin_user` (`username`, `password`, `real_name`, `role`, `status`) VALUES
('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EO', '超级管理员', 1, 1);
```

## 2. 系统配置表 (system_config)

```sql
CREATE TABLE `system_config` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `config_key` VARCHAR(128) NOT NULL COMMENT '配置键',
    `config_value` TEXT COMMENT '配置值',
    `description` VARCHAR(512) DEFAULT NULL COMMENT '配置说明',
    `category` VARCHAR(64) DEFAULT 'common' COMMENT '分类：payment/user/activity/common',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_config_key` (`config_key`),
    KEY `idx_category` (`category`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统配置表';

-- 初始化配置
INSERT INTO `system_config` (`config_key`, `config_value`, `description`, `category`) VALUES
('site.name', '搭子交友', '网站名称', 'common'),
('site.logo', '/static/logo.png', '网站Logo', 'common'),
('user.register_enabled', 'true', '是否允许注册', 'user'),
('user.default_credit_score', '100', '用户默认信用分', 'user'),
('activity.max_participants', '50', '活动最大参与人数', 'activity'),
('activity.audit_required', 'false', '活动是否需要审核', 'activity'),
('payment.wxpay_enabled', 'true', '是否启用微信支付', 'payment'),
('payment.refund_enabled', 'true', '是否允许退款', 'payment');
```

## 3. 操作日志表 (operation_log)

```sql
CREATE TABLE `operation_log` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `admin_id` BIGINT NOT NULL COMMENT '管理员ID',
    `admin_name` VARCHAR(64) DEFAULT NULL COMMENT '管理员姓名',
    `operation` VARCHAR(64) NOT NULL COMMENT '操作类型',
    `module` VARCHAR(64) NOT NULL COMMENT '操作模块',
    `description` VARCHAR(512) DEFAULT NULL COMMENT '操作描述',
    `request_data` TEXT COMMENT '请求数据',
    `response_data` TEXT COMMENT '响应数据',
    `ip` VARCHAR(64) DEFAULT NULL COMMENT '操作IP',
    `status` TINYINT NOT NULL DEFAULT '1' COMMENT '状态：0失败 1成功',
    `execution_time` BIGINT DEFAULT '0' COMMENT '执行时长(ms)',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_admin_id` (`admin_id`),
    KEY `idx_module` (`module`),
    KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='操作日志表';
```

## 4. 用户位置表 (user_location) - 用于匹配服务

```sql
CREATE TABLE `user_location` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `nickname` VARCHAR(64) DEFAULT NULL COMMENT '昵称',
    `avatar` VARCHAR(512) DEFAULT NULL COMMENT '头像',
    `age` INT DEFAULT NULL COMMENT '年龄',
    `gender` TINYINT DEFAULT '0' COMMENT '性别：0未知 1男 2女',
    `bio` VARCHAR(512) DEFAULT NULL COMMENT '简介',
    `zodiac` VARCHAR(16) DEFAULT NULL COMMENT '星座',
    `chinese_zodiac` VARCHAR(16) DEFAULT NULL COMMENT '生肖',
    `interests` VARCHAR(512) DEFAULT NULL COMMENT '兴趣标签（逗号分隔）',
    `longitude` DECIMAL(10,7) DEFAULT NULL COMMENT '经度',
    `latitude` DECIMAL(10,7) DEFAULT NULL COMMENT '纬度',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_id` (`user_id`),
    KEY `idx_location` (`longitude`, `latitude`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户位置表';
```

## 5. 活动表增加版本号字段（用于乐观锁）

```sql
ALTER TABLE `activity` ADD COLUMN `version` INT NOT NULL DEFAULT '0' COMMENT '乐观锁版本号' AFTER `status`;
```

## 6. 活动参与者表优化索引

```sql
ALTER TABLE `activity_participant` 
ADD UNIQUE KEY `uk_activity_user` (`activity_id`, `user_id`),
ADD KEY `idx_user_id` (`user_id`);
```

## 7. 消息表优化索引

```sql
ALTER TABLE `message` 
ADD KEY `idx_sender_receiver` (`sender_id`, `receiver_id`),
ADD KEY `idx_receiver_status` (`receiver_id`, `status`);
```

## 8. 评价表优化索引

```sql
ALTER TABLE `review` 
ADD UNIQUE KEY `uk_reviewer_activity_reviewee` (`reviewer_id`, `activity_id`, `reviewee_id`),
ADD KEY `idx_reviewee_id` (`reviewee_id`);
```

-- 财务管理相关表

-- 财务流水表
CREATE TABLE `finance_record` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `order_no` VARCHAR(64) DEFAULT NULL COMMENT '关联订单号',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `type` TINYINT NOT NULL COMMENT '类型：1收入 2支出 3退款',
    `amount` DECIMAL(10,2) NOT NULL COMMENT '金额',
    `balance` DECIMAL(10,2) NOT NULL COMMENT '变动后余额',
    `description` VARCHAR(512) DEFAULT NULL COMMENT '描述',
    `remark` VARCHAR(512) DEFAULT NULL COMMENT '备注',
    `related_id` BIGINT DEFAULT NULL COMMENT '关联业务ID',
    `related_type` VARCHAR(32) DEFAULT NULL COMMENT '关联业务类型',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_order_no` (`order_no`),
    KEY `idx_type` (`type`),
    KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='财务流水表';

-- 财务日报表
CREATE TABLE `finance_daily_report` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `report_date` DATE NOT NULL COMMENT '报表日期',
    `total_orders` INT NOT NULL DEFAULT '0' COMMENT '总订单数',
    `success_orders` INT NOT NULL DEFAULT '0' COMMENT '成功订单数',
    `refund_orders` INT NOT NULL DEFAULT '0' COMMENT '退款订单数',
    `total_income` DECIMAL(12,2) NOT NULL DEFAULT '0.00' COMMENT '总收入',
    `total_refund` DECIMAL(12,2) NOT NULL DEFAULT '0.00' COMMENT '总退款',
    `net_income` DECIMAL(12,2) NOT NULL DEFAULT '0.00' COMMENT '净收入',
    `new_users` INT NOT NULL DEFAULT '0' COMMENT '新增用户数',
    `active_users` INT NOT NULL DEFAULT '0' COMMENT '活跃用户数',
    `new_activities` INT NOT NULL DEFAULT '0' COMMENT '新增活动数',
    `total_participants` INT NOT NULL DEFAULT '0' COMMENT '总参与人次',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_report_date` (`report_date`),
    KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='财务日报表';

-- 权限管理相关表

-- 权限表
CREATE TABLE `admin_permission` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `permission_code` VARCHAR(128) NOT NULL COMMENT '权限编码',
    `permission_name` VARCHAR(128) NOT NULL COMMENT '权限名称',
    `module` VARCHAR(64) NOT NULL COMMENT '所属模块',
    `description` VARCHAR(512) DEFAULT NULL COMMENT '描述',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_permission_code` (`permission_code`),
    KEY `idx_module` (`module`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='权限表';

-- 角色权限关联表
CREATE TABLE `admin_role_permission` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `role` TINYINT NOT NULL COMMENT '角色：1超级管理员 2普通管理员 3运营',
    `permission_id` BIGINT NOT NULL COMMENT '权限ID',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_role_permission` (`role`, `permission_id`),
    KEY `idx_role` (`role`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色权限关联表';

-- 初始化权限数据
INSERT INTO `admin_permission` (`permission_code`, `permission_name`, `module`, `description`) VALUES
-- 用户管理权限
('user:view', '查看会员', 'user', '查看会员列表和详情'),
('user:edit', '编辑会员', 'user', '编辑会员信息'),
('user:delete', '删除会员', 'user', '删除会员'),
('user:toggle', '启用/禁用会员', 'user', '启用或禁用会员'),
-- 活动管理权限
('activity:view', '查看活动', 'activity', '查看活动列表和详情'),
('activity:edit', '编辑活动', 'activity', '编辑活动信息'),
('activity:delete', '删除活动', 'activity', '删除活动'),
('activity:audit', '审核活动', 'activity', '审核活动'),
-- 财务管理权限
('finance:view', '查看财务', 'finance', '查看财务流水和报表'),
('finance:report', '生成报表', 'finance', '生成财务报表'),
-- 系统管理权限
('system:config', '系统配置', 'system', '修改系统配置'),
('system:log', '查看日志', 'system', '查看操作日志'),
-- 管理员管理权限
('admin:view', '查看管理员', 'admin', '查看管理员列表'),
('admin:create', '创建管理员', 'admin', '创建管理员'),
('admin:edit', '编辑管理员', 'admin', '编辑管理员'),
('admin:delete', '删除管理员', 'admin', '删除管理员'),
-- 权限管理权限
('permission:view', '查看权限', 'permission', '查看权限列表'),
('permission:assign', '分配权限', 'permission', '为角色分配权限');

-- 初始化角色权限（超级管理员拥有所有权限）
INSERT INTO `admin_role_permission` (`role`, `permission_id`)
SELECT 1, id FROM admin_permission;

-- 普通管理员权限（除管理员管理和权限管理外的所有权限）
INSERT INTO `admin_role_permission` (`role`, `permission_id`)
SELECT 2, id FROM admin_permission 
WHERE permission_code NOT LIKE 'admin:%' AND permission_code NOT LIKE 'permission:%';

-- 运营权限（仅查看和编辑）
INSERT INTO `admin_role_permission` (`role`, `permission_id`)
SELECT 3, id FROM admin_permission 
WHERE permission_code IN ('user:view', 'user:edit', 'activity:view', 'activity:edit', 'activity:audit', 'finance:view', 'system:log');

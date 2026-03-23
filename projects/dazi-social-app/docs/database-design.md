# 搭子交友微信小程序 - 数据库设计文档

**版本:** v1.0  
**创建日期:** 2026-03-22  
**设计:** 云捌  
**数据库:** MySQL 8.0

---

## 目录

1. [设计原则](#1-设计原则)
2. [ER图](#2-er图)
3. [表结构设计](#3-表结构设计)
4. [索引设计](#4-索引设计)
5. [数据字典](#5-数据字典)

---

## 1. 设计原则

### 1.1 命名规范
- 表名：小写字母 + 下划线，如 `user_profile`
- 字段名：小写字母 + 下划线，如 `created_at`
- 主键：`id` (BIGINT AUTO_INCREMENT)
- 时间字段：`created_at`, `updated_at` (DATETIME)
- 软删除：`is_deleted` (TINYINT, 0=正常, 1=删除)

### 1.2 数据类型选择
- 主键：BIGINT
- 外键：BIGINT
- 枚举/状态：TINYINT 或 VARCHAR
- 金额：DECIMAL(10, 2)
- 文本：VARCHAR (变长) / TEXT (大文本)
- 布尔：TINYINT (0=FALSE, 1=TRUE)

### 1.3 性能优化
- 合理使用索引
- 分表分库预留（如 `message` 表）
- 冗余设计（减少JOIN）
- Redis缓存热点数据

---

## 2. ER图

```
┌─────────────┐       ┌─────────────┐       ┌─────────────┐
│   user      │───────│user_profile │───────│  user_tag   │
│ (用户基础)  │ 1   1 │ (用户资料)  │ 1   N │ (用户标签)  │
└─────────────┘       └─────────────┘       └─────────────┘
       │                      │
       │ 1                  1 │
       │                      │
       ▼                      ▼
┌─────────────┐       ┌─────────────┐       ┌─────────────┐
│   review    │───────│activity_part│───────│  activity   │
│  (评价)     │ N   N │(活动参与者) │ N   1 │   (活动)    │
└─────────────┘       └─────────────┘       └─────────────┘
                                                    │
                                                    │ 1
                                                    │
                                              ┌─────┴─────┐
                                              │activity_  │
                                              │   type    │
                                              │ (活动类型) │
                                              └───────────┘

┌─────────────┐       ┌─────────────┐
│  payment    │───────│   message   │
│  (支付)     │ 1   1 │   (消息)    │
└─────────────┘       └─────────────┘
                            │
                            │ 1
                            │
                      ┌─────┴─────┐
                      │conversation│
                      │ (会话)     │
                      └───────────┘

┌─────────────┐       ┌─────────────┐
│credit_record│      │   report    │
│ (信用记录)  │      │   (举报)    │
└─────────────┘       └─────────────┘
```

---

## 3. 表结构设计

### 3.1 用户相关表

#### 3.1.1 user - 用户基础表

**用途:** 存储用户基础信息和认证信息

| 字段名 | 类型 | 长度 | 允许NULL | 默认值 | 说明 |
|--------|------|------|----------|--------|------|
| id | BIGINT | - | NO | AUTO_INCREMENT | 主键 |
| openid | VARCHAR | 128 | NO | - | 微信OpenID（唯一） |
| unionid | VARCHAR | 128 | YES | NULL | 微信UnionID（可选） |
| mobile | VARCHAR | 11 | YES | NULL | 手机号（唯一） |
| nickname | VARCHAR | 64 | YES | NULL | 昵称 |
| avatar | VARCHAR | 512 | YES | NULL | 头像URL |
| gender | TINYINT | - | YES | 0 | 性别：0未知 1男 2女 |
| birthday | DATE | - | YES | NULL | 生日 |
| status | TINYINT | - | NO | 1 | 状态：0禁用 1正常 2待认证 |
| credit_score | INT | - | NO | 100 | 信用分（0-1000） |
| realname_verified | TINYINT | - | NO | 0 | 实名认证：0未认证 1已认证 |
| last_login_at | DATETIME | - | YES | NULL | 最后登录时间 |
| last_login_ip | VARCHAR | 64 | YES | NULL | 最后登录IP |
| created_at | DATETIME | - | NO | CURRENT_TIMESTAMP | 创建时间 |
| updated_at | DATETIME | - | NO | CURRENT_TIMESTAMP | 更新时间 |
| is_deleted | TINYINT | - | NO | 0 | 软删除标记 |

**索引:**
```sql
PRIMARY KEY (id)
UNIQUE KEY uk_openid (openid)
UNIQUE KEY uk_mobile (mobile)
KEY idx_status (status)
KEY idx_created_at (created_at)
```

---

#### 3.1.2 user_profile - 用户资料表

**用途:** 存储用户详细资料和设置

| 字段名 | 类型 | 长度 | 允许NULL | 默认值 | 说明 |
|--------|------|------|----------|--------|------|
| id | BIGINT | - | NO | AUTO_INCREMENT | 主键 |
| user_id | BIGINT | - | NO | - | 用户ID（外键） |
| signature | VARCHAR | 256 | YES | NULL | 个性签名 |
| zodiac | VARCHAR | 16 | YES | NULL | 星座（自动计算） |
| zodiac_animal | VARCHAR | 16 | YES | NULL | 生肖（自动计算） |
| province | VARCHAR | 32 | YES | NULL | 省份 |
| city | VARCHAR | 32 | YES | NULL | 城市 |
| district | VARCHAR | 32 | YES | NULL | 区县 |
| address | VARCHAR | 256 | YES | NULL | 详细地址 |
| latitude | DECIMAL | 10,7 | YES | NULL | 纬度 |
| longitude | DECIMAL | 10,7 | YES | NULL | 经度 |
| location_visible_range | INT | - | NO | 5 | 位置可见范围（km） |
| profile_visible | TINYINT | - | NO | 1 | 资料可见：0私密 1公开 2仅好友 |
| album_images | TEXT | - | YES | NULL | 相册图片（JSON数组） |
| created_at | DATETIME | - | NO | CURRENT_TIMESTAMP | 创建时间 |
| updated_at | DATETIME | - | NO | CURRENT_TIMESTAMP | 更新时间 |

**索引:**
```sql
PRIMARY KEY (id)
UNIQUE KEY uk_user_id (user_id)
KEY idx_location (latitude, longitude)
KEY idx_city (city)
```

---

#### 3.1.3 user_tag - 用户标签关联表

**用途:** 用户兴趣标签多对多关系

| 字段名 | 类型 | 长度 | 允许NULL | 默认值 | 说明 |
|--------|------|------|----------|--------|------|
| id | BIGINT | - | NO | AUTO_INCREMENT | 主键 |
| user_id | BIGINT | - | NO | - | 用户ID |
| tag_name | VARCHAR | 32 | NO | - | 标签名称 |
| created_at | DATETIME | - | NO | CURRENT_TIMESTAMP | 创建时间 |

**索引:**
```sql
PRIMARY KEY (id)
UNIQUE KEY uk_user_tag (user_id, tag_name)
KEY idx_tag_name (tag_name)
```

---

#### 3.1.4 user_blacklist - 用户黑名单表

**用途:** 黑名单管理

| 字段名 | 类型 | 长度 | 允许NULL | 默认值 | 说明 |
|--------|------|------|----------|--------|------|
| id | BIGINT | - | NO | AUTO_INCREMENT | 主键 |
| user_id | BIGINT | - | NO | - | 用户ID |
| blocked_user_id | BIGINT | - | NO | - | 被拉黑用户ID |
| reason | VARCHAR | 256 | YES | NULL | 拉黑原因 |
| created_at | DATETIME | - | NO | CURRENT_TIMESTAMP | 创建时间 |

**索引:**
```sql
PRIMARY KEY (id)
UNIQUE KEY uk_user_blocked (user_id, blocked_user_id)
```

---

### 3.2 活动相关表

#### 3.2.1 activity_type - 活动类型表

**用途:** 活动类型枚举（线上/线下分类）

| 字段名 | 类型 | 长度 | 允许NULL | 默认值 | 说明 |
|--------|------|------|----------|--------|------|
| id | BIGINT | - | NO | AUTO_INCREMENT | 主键 |
| type_code | VARCHAR | 32 | NO | - | 类型编码（如：dining, outdoor） |
| type_name | VARCHAR | 64 | NO | - | 类型名称 |
| category | VARCHAR | 16 | NO | - | 分类：offline/online |
| icon | VARCHAR | 256 | YES | NULL | 图标URL |
| sort_order | INT | - | NO | 0 | 排序序号 |
| is_enabled | TINYINT | - | NO | 1 | 是否启用 |
| created_at | DATETIME | - | NO | CURRENT_TIMESTAMP | 创建时间 |

**索引:**
```sql
PRIMARY KEY (id)
UNIQUE KEY uk_type_code (type_code)
KEY idx_category (category)
```

**初始化数据:**
```sql
INSERT INTO activity_type (type_code, type_name, category, sort_order) VALUES
-- 线下活动
('dining', '吃喝玩乐', 'offline', 1),
('outdoor', '户外游玩', 'offline', 2),
('family', '亲子活动', 'offline', 3),
('sports', '户外运动', 'offline', 4),
('dating', '相亲交友', 'offline', 5),
-- 线上活动
('game', '线上游戏', 'online', 10),
('study', '线上学习', 'online', 11),
('social', '线上社交', 'online', 12);
```

---

#### 3.2.2 activity - 活动表

**用途:** 活动主表

| 字段名 | 类型 | 长度 | 允许NULL | 默认值 | 说明 |
|--------|------|------|----------|--------|------|
| id | BIGINT | - | NO | AUTO_INCREMENT | 主键 |
| title | VARCHAR | 128 | NO | - | 活动标题 |
| description | TEXT | - | YES | NULL | 活动描述 |
| type_id | BIGINT | - | NO | - | 活动类型ID |
| creator_id | BIGINT | - | NO | - | 发起人ID |
| cover_image | VARCHAR | 512 | YES | NULL | 封面图URL |
| start_time | DATETIME | - | NO | - | 开始时间 |
| end_time | DATETIME | - | YES | NULL | 结束时间 |
| location_name | VARCHAR | 128 | YES | NULL | 地点名称 |
| address | VARCHAR | 256 | YES | NULL | 详细地址 |
| latitude | DECIMAL | 10,7 | YES | NULL | 纬度 |
| longitude | DECIMAL | 10,7 | YES | NULL | 经度 |
| min_participants | INT | - | NO | 1 | 最少人数 |
| max_participants | INT | - | YES | NULL | 最多人数 |
| payment_mode | VARCHAR | 16 | NO | - | 付费模式：aa, male_free, treat, free |
| total_fee | DECIMAL | 10,2 | NO | 0.00 | 总费用（元） |
| fee_per_person | DECIMAL | 10,2 | YES | NULL | 人均费用（元） |
| signup_deadline | DATETIME | - | YES | NULL | 报名截止时间 |
| current_participants | INT | - | NO | 0 | 当前参与人数 |
| status | VARCHAR | 16 | NO | 'draft' | 状态：draft(草稿) published(已发布) full(已满员) ongoing(进行中) completed(已结束) cancelled(已取消) |
| is_deleted | TINYINT | - | NO | 0 | 软删除标记 |
| created_at | DATETIME | - | NO | CURRENT_TIMESTAMP | 创建时间 |
| updated_at | DATETIME | - | NO | CURRENT_TIMESTAMP | 更新时间 |

**索引:**
```sql
PRIMARY KEY (id)
KEY idx_creator_id (creator_id)
KEY idx_type_id (type_id)
KEY idx_start_time (start_time)
KEY idx_status (status)
KEY idx_location (latitude, longitude)
KEY idx_created_at (created_at)
```

---

#### 3.2.3 activity_participant - 活动参与者表

**用途:** 活动报名记录

| 字段名 | 类型 | 长度 | 允许NULL | 默认值 | 说明 |
|--------|------|------|----------|--------|------|
| id | BIGINT | - | NO | AUTO_INCREMENT | 主键 |
| activity_id | BIGINT | - | NO | - | 活动ID |
| user_id | BIGINT | - | NO | - | 用户ID |
| fee | DECIMAL | 10,2 | NO | 0.00 | 应付费用 |
| payment_status | VARCHAR | 16 | NO | 'unpaid' | 支付状态：unpaid(未支付) paid(已支付) refunded(已退款) |
| check_in_time | DATETIME | - | YES | NULL | 签到时间 |
| check_in_location | VARCHAR | 256 | YES | NULL | 签到位置描述 |
| created_at | DATETIME | - | NO | CURRENT_TIMESTAMP | 报名时间 |
| cancelled_at | DATETIME | - | YES | NULL | 取消时间 |

**索引:**
```sql
PRIMARY KEY (id)
UNIQUE KEY uk_activity_user (activity_id, user_id)
KEY idx_user_id (user_id)
KEY idx_payment_status (payment_status)
KEY idx_created_at (created_at)
```

---

### 3.3 支付相关表

#### 3.3.1 payment - 支付记录表

**用途:** 支付流水记录

| 字段名 | 类型 | 长度 | 允许NULL | 默认值 | 说明 |
|--------|------|------|----------|--------|------|
| id | BIGINT | - | NO | AUTO_INCREMENT | 主键 |
| order_no | VARCHAR | 64 | NO | - | 订单号（唯一） |
| transaction_id | VARCHAR | 64 | YES | NULL | 微信交易号 |
| user_id | BIGINT | - | NO | - | 用户ID |
| activity_id | BIGINT | - | YES | NULL | 关联活动ID |
| participant_id | BIGINT | - | YES | NULL | 参与者记录ID |
| amount | DECIMAL | 10,2 | NO | 0.00 | 支付金额 |
| payment_type | VARCHAR | 16 | NO | - | 支付类型：wechat(微信支付) |
| payment_status | VARCHAR | 16 | NO | 'pending' | 支付状态：pending(待支付) success(成功) failed(失败) refunding(退款中) refunded(已退款) |
| payment_time | DATETIME | - | YES | NULL | 支付时间 |
| refund_amount | DECIMAL | 10,2 | YES | NULL | 退款金额 |
| refund_time | DATETIME | - | YES | NULL | 退款时间 |
| refund_reason | VARCHAR | 256 | YES | NULL | 退款原因 |
| prepay_id | VARCHAR | 128 | YES | NULL | 预支付ID |
| created_at | DATETIME | - | NO | CURRENT_TIMESTAMP | 创建时间 |
| updated_at | DATETIME | - | NO | CURRENT_TIMESTAMP | 更新时间 |

**索引:**
```sql
PRIMARY KEY (id)
UNIQUE KEY uk_order_no (order_no)
UNIQUE KEY uk_transaction_id (transaction_id)
KEY idx_user_id (user_id)
KEY idx_activity_id (activity_id)
KEY idx_payment_status (payment_status)
KEY idx_created_at (created_at)
```

---

### 3.4 消息相关表

#### 3.4.1 conversation - 会话表

**用途:** 聊天会话（私聊/群聊）

| 字段名 | 类型 | 长度 | 允许NULL | 默认值 | 说明 |
|--------|------|------|----------|--------|------|
| id | BIGINT | - | NO | AUTO_INCREMENT | 主键 |
| conversation_type | TINYINT | - | NO | 1 | 会话类型：1私聊 2群聊 3系统 |
| activity_id | BIGINT | - | YES | NULL | 关联活动ID（群聊） |
| conversation_name | VARCHAR | 64 | YES | NULL | 会话名称（群聊） |
| avatar | VARCHAR | 512 | YES | NULL | 会话头像 |
| user_id_1 | BIGINT | - | YES | NULL | 用户1（私聊） |
| user_id_2 | BIGINT | - | YES | NULL | 用户2（私聊） |
| last_message | VARCHAR | 512 | YES | NULL | 最后一条消息预览 |
| last_message_time | DATETIME | - | YES | NULL | 最后消息时间 |
| created_at | DATETIME | - | NO | CURRENT_TIMESTAMP | 创建时间 |
| updated_at | DATETIME | - | NO | CURRENT_TIMESTAMP | 更新时间 |

**索引:**
```sql
PRIMARY KEY (id)
KEY idx_activity_id (activity_id)
KEY idx_user_id_1 (user_id_1)
KEY idx_user_id_2 (user_id_2)
KEY idx_last_message_time (last_message_time)
```

---

#### 3.4.2 message - 消息表

**用途:** 消息记录（可能需要分表）

| 字段名 | 类型 | 长度 | 允许NULL | 默认值 | 说明 |
|--------|------|------|----------|--------|------|
| id | BIGINT | - | NO | AUTO_INCREMENT | 主键 |
| conversation_id | BIGINT | - | NO | - | 会话ID |
| sender_id | BIGINT | - | NO | - | 发送者ID（系统消息为0） |
| message_type | VARCHAR | 16 | NO | 'text' | 消息类型：text(文本) image(图片) voice(语音) system(系统) |
| content | TEXT | - | YES | NULL | 消息内容 |
| media_url | VARCHAR | 512 | YES | NULL | 媒体文件URL（图片/语音） |
| is_read | TINYINT | - | NO | 0 | 已读标记：0未读 1已读 |
| read_at | DATETIME | - | YES | NULL | 已读时间 |
| created_at | DATETIME | - | NO | CURRENT_TIMESTAMP | 发送时间 |

**索引:**
```sql
PRIMARY KEY (id)
KEY idx_conversation_id (conversation_id)
KEY idx_sender_id (sender_id)
KEY idx_created_at (created_at)
KEY idx_is_read (is_read)
```

---

### 3.5 评价相关表

#### 3.5.1 review - 评价表

**用途:** 用户互评记录

| 字段名 | 类型 | 长度 | 允许NULL | 默认值 | 说明 |
|--------|------|------|----------|--------|------|
| id | BIGINT | - | NO | AUTO_INCREMENT | 主键 |
| activity_id | BIGINT | - | NO | - | 活动ID |
| reviewer_id | BIGINT | - | NO | - | 评价者ID |
| reviewee_id | BIGINT | - | NO | - | 被评价者ID |
| rating | TINYINT | - | NO | 5 | 评分（1-5星） |
| comment | VARCHAR | 512 | YES | NULL | 评价内容 |
| tags | VARCHAR | 256 | YES | NULL | 评价标签（JSON数组，如["守时","有趣"]） |
| created_at | DATETIME | - | NO | CURRENT_TIMESTAMP | 评价时间 |

**索引:**
```sql
PRIMARY KEY (id)
UNIQUE KEY uk_activity_reviewer_reviewee (activity_id, reviewer_id, reviewee_id)
KEY idx_reviewer_id (reviewer_id)
KEY idx_reviewee_id (reviewee_id)
KEY idx_rating (rating)
```

---

#### 3.5.2 credit_record - 信用记录表

**用途:** 信用分变化记录

| 字段名 | 类型 | 长度 | 允许NULL | 默认值 | 说明 |
|--------|------|------|----------|--------|------|
| id | BIGINT | - | NO | AUTO_INCREMENT | 主键 |
| user_id | BIGINT | - | NO | - | 用户ID |
| type | VARCHAR | 16 | NO | - | 类型：increase(增加) decrease(减少) |
| amount | INT | - | NO | 0 | 变化量（正数） |
| before_score | INT | - | NO | 100 | 变化前分数 |
| after_score | INT | - | NO | 100 | 变化后分数 |
| reason | VARCHAR | 256 | NO | - | 原因 |
| related_id | BIGINT | - | YES | NULL | 关联ID（如review_id） |
| created_at | DATETIME | - | NO | CURRENT_TIMESTAMP | 创建时间 |

**索引:**
```sql
PRIMARY KEY (id)
KEY idx_user_id (user_id)
KEY idx_created_at (created_at)
```

---

### 3.6 其他表

#### 3.6.1 report - 举报表

**用途:** 用户举报记录

| 字段名 | 类型 | 长度 | 允许NULL | 默认值 | 说明 |
|--------|------|------|----------|--------|------|
| id | BIGINT | - | NO | AUTO_INCREMENT | 主键 |
| reporter_id | BIGINT | - | NO | - | 举报人ID |
| reported_user_id | BIGINT | - | YES | NULL | 被举报用户ID |
| reported_activity_id | BIGINT | - | YES | NULL | 被举报活动ID |
| reason | VARCHAR | 16 | NO | - | 举报类型：spam(垃圾) fraud(欺诈) harassment(骚扰) other(其他) |
| description | VARCHAR | 512 | YES | NULL | 详细描述 |
| images | TEXT | - | YES | NULL | 证据图片（JSON数组） |
| status | VARCHAR | 16 | NO | 'pending' | 处理状态：pending(待处理) processing(处理中) resolved(已处理) dismissed(已驳回) |
| handler_id | BIGINT | - | YES | NULL | 处理人ID |
| handle_result | VARCHAR | 512 | YES | NULL | 处理结果 |
| created_at | DATETIME | - | NO | CURRENT_TIMESTAMP | 创建时间 |
| handled_at | DATETIME | - | YES | NULL | 处理时间 |

**索引:**
```sql
PRIMARY KEY (id)
KEY idx_reporter_id (reporter_id)
KEY idx_reported_user_id (reported_user_id)
KEY idx_status (status)
```

---

#### 3.6.2 system_config - 系统配置表

**用途:** 系统配置参数

| 字段名 | 类型 | 长度 | 允许NULL | 默认值 | 说明 |
|--------|------|------|----------|--------|------|
| id | BIGINT | - | NO | AUTO_INCREMENT | 主键 |
| config_key | VARCHAR | 64 | NO | - | 配置键（唯一） |
| config_value | TEXT | - | YES | NULL | 配置值 |
| description | VARCHAR | 256 | YES | NULL | 描述 |
| is_public | TINYINT | - | NO | 0 | 是否公开：0私有 1公开 |
| updated_at | DATETIME | - | NO | CURRENT_TIMESTAMP | 更新时间 |

**索引:**
```sql
PRIMARY KEY (id)
UNIQUE KEY uk_config_key (config_key)
```

**初始配置:**
```sql
INSERT INTO system_config (config_key, config_value, description, is_public) VALUES
('app_name', '搭子交友', '应用名称', 1),
('default_credit_score', '100', '初始信用分', 0),
('location_accuracy', '100', '定位精度（米）', 0),
('max_upload_images', '9', '最大上传图片数', 1),
('match_algorithm_weights', '{"distance":0.3,"interest":0.4,"zodiac":0.15,"zodiac_animal":0.15}', '匹配算法权重', 0);
```

---

## 4. 索引设计

### 4.1 核心索引策略

| 表名 | 索引类型 | 索引名 | 字段 | 说明 |
|------|---------|--------|------|------|
| user | UNIQUE | uk_openid | openid | 微信登录 |
| user | UNIQUE | uk_mobile | mobile | 手机号登录 |
| user_profile | INDEX | idx_location | latitude, longitude | 附近的人查询 |
| activity | INDEX | idx_location | latitude, longitude | 附近活动查询 |
| activity | INDEX | idx_status_created | status, created_at | 活动列表筛选 |
| activity_participant | UNIQUE | uk_activity_user | activity_id, user_id | 防止重复报名 |
| payment | UNIQUE | uk_order_no | order_no | 订单查询 |
| payment | INDEX | idx_user_status | user_id, payment_status | 支付状态查询 |
| message | INDEX | idx_conv_created | conversation_id, created_at | 消息分页 |
| review | UNIQUE | uk_activity_reviewer_reviewee | activity_id, reviewer_id, reviewee_id | 防止重复评价 |

### 4.2 性能优化建议

1. **地理位置查询**
   - 使用空间索引（MySQL 5.7+）或 R-tree 索引
   - 计算距离使用 ST_Distance_Sphere 函数
   - Redis缓存附近用户/活动结果

2. **消息表分表**
   - 按 `conversation_id` 取模分表（如 `message_0` ~ `message_9`）
   - 或按时间分表（如 `message_202603`, `message_202604`）

3. **复合索引顺序**
   - 等值查询字段放前面
   - 范围查询字段放后面
   - 排序字段放最后

---

## 5. 数据字典

### 5.1 枚举值定义

#### 5.1.1 user.status - 用户状态
| 值 | 说明 |
|----|------|
| 0 | 禁用 |
| 1 | 正常 |
| 2 | 待认证 |

#### 5.1.2 user.gender - 性别
| 值 | 说明 |
|----|------|
| 0 | 未知 |
| 1 | 男 |
| 2 | 女 |

#### 5.1.3 activity.payment_mode - 付费模式
| 值 | 说明 |
|----|------|
| aa | AA制 |
| male_free | 男A女免 |
| treat | 请客 |
| free | 免费 |

#### 5.1.4 activity.status - 活动状态
| 值 | 说明 |
|----|------|
| draft | 草稿 |
| published | 已发布 |
| full | 已满员 |
| ongoing | 进行中 |
| completed | 已结束 |
| cancelled | 已取消 |

#### 5.1.5 payment.payment_status - 支付状态
| 值 | 说明 |
|----|------|
| pending | 待支付 |
| success | 成功 |
| failed | 失败 |
| refunding | 退款中 |
| refunded | 已退款 |

#### 5.1.6 message.message_type - 消息类型
| 值 | 说明 |
|----|------|
| text | 文本 |
| image | 图片 |
| voice | 语音 |
| system | 系统 |

#### 5.1.7 conversation.conversation_type - 会话类型
| 值 | 说明 |
|----|------|
| 1 | 私聊 |
| 2 | 群聊 |
| 3 | 系统 |

---

### 5.2 Redis缓存设计

| Key Pattern | 说明 | 过期时间 |
|-------------|------|----------|
| `user:{id}` | 用户信息 | 1小时 |
| `user:profile:{id}` | 用户资料 | 1小时 |
| `activity:{id}` | 活动详情 | 30分钟 |
| `nearby:users:{lat}:{lng}:{radius}` | 附近用户 | 5分钟 |
| `nearby:activities:{lat}:{lng}:{radius}` | 附近活动 | 5分钟 |
| `conversation:{id}:unread:{user_id}` | 未读消息数 | 永久 |
| `match:recommend:{user_id}` | 推荐用户 | 10分钟 |

---

## 附录A: 完整建表SQL

```sql
-- ============================================
-- 搭子交友微信小程序 - 数据库建表SQL
-- ============================================

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- 用户相关表
-- ----------------------------

-- 用户基础表
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
  `openid` VARCHAR(128) NOT NULL COMMENT '微信OpenID',
  `unionid` VARCHAR(128) DEFAULT NULL COMMENT '微信UnionID',
  `mobile` VARCHAR(11) DEFAULT NULL COMMENT '手机号',
  `nickname` VARCHAR(64) DEFAULT NULL COMMENT '昵称',
  `avatar` VARCHAR(512) DEFAULT NULL COMMENT '头像URL',
  `gender` TINYINT DEFAULT 0 COMMENT '性别：0未知 1男 2女',
  `birthday` DATE DEFAULT NULL COMMENT '生日',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态：0禁用 1正常 2待认证',
  `credit_score` INT NOT NULL DEFAULT 100 COMMENT '信用分（0-1000）',
  `realname_verified` TINYINT NOT NULL DEFAULT 0 COMMENT '实名认证：0未认证 1已认证',
  `last_login_at` DATETIME DEFAULT NULL COMMENT '最后登录时间',
  `last_login_ip` VARCHAR(64) DEFAULT NULL COMMENT '最后登录IP',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '软删除标记',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_openid` (`openid`),
  UNIQUE KEY `uk_mobile` (`mobile`),
  KEY `idx_status` (`status`),
  KEY `idx_created_at` (`created_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户基础表';

-- 用户资料表
DROP TABLE IF EXISTS `user_profile`;
CREATE TABLE `user_profile` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` BIGINT NOT NULL COMMENT '用户ID',
  `signature` VARCHAR(256) DEFAULT NULL COMMENT '个性签名',
  `zodiac` VARCHAR(16) DEFAULT NULL COMMENT '星座',
  `zodiac_animal` VARCHAR(16) DEFAULT NULL COMMENT '生肖',
  `province` VARCHAR(32) DEFAULT NULL COMMENT '省份',
  `city` VARCHAR(32) DEFAULT NULL COMMENT '城市',
  `district` VARCHAR(32) DEFAULT NULL COMMENT '区县',
  `address` VARCHAR(256) DEFAULT NULL COMMENT '详细地址',
  `latitude` DECIMAL(10,7) DEFAULT NULL COMMENT '纬度',
  `longitude` DECIMAL(10,7) DEFAULT NULL COMMENT '经度',
  `location_visible_range` INT NOT NULL DEFAULT 5 COMMENT '位置可见范围（km）',
  `profile_visible` TINYINT NOT NULL DEFAULT 1 COMMENT '资料可见：0私密 1公开 2仅好友',
  `album_images` TEXT DEFAULT NULL COMMENT '相册图片（JSON数组）',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_id` (`user_id`),
  KEY `idx_location` (`latitude`, `longitude`),
  KEY `idx_city` (`city`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户资料表';

-- 用户标签关联表
DROP TABLE IF EXISTS `user_tag`;
CREATE TABLE `user_tag` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` BIGINT NOT NULL COMMENT '用户ID',
  `tag_name` VARCHAR(32) NOT NULL COMMENT '标签名称',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_tag` (`user_id`, `tag_name`),
  KEY `idx_tag_name` (`tag_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户标签关联表';

-- 用户黑名单表
DROP TABLE IF EXISTS `user_blacklist`;
CREATE TABLE `user_blacklist` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` BIGINT NOT NULL COMMENT '用户ID',
  `blocked_user_id` BIGINT NOT NULL COMMENT '被拉黑用户ID',
  `reason` VARCHAR(256) DEFAULT NULL COMMENT '拉黑原因',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_blocked` (`user_id`, `blocked_user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户黑名单表';

-- ----------------------------
-- 活动相关表
-- ----------------------------

-- 活动类型表
DROP TABLE IF EXISTS `activity_type`;
CREATE TABLE `activity_type` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
  `type_code` VARCHAR(32) NOT NULL COMMENT '类型编码',
  `type_name` VARCHAR(64) NOT NULL COMMENT '类型名称',
  `category` VARCHAR(16) NOT NULL COMMENT '分类：offline/online',
  `icon` VARCHAR(256) DEFAULT NULL COMMENT '图标URL',
  `sort_order` INT NOT NULL DEFAULT 0 COMMENT '排序序号',
  `is_enabled` TINYINT NOT NULL DEFAULT 1 COMMENT '是否启用',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_type_code` (`type_code`),
  KEY `idx_category` (`category`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='活动类型表';

-- 活动表
DROP TABLE IF EXISTS `activity`;
CREATE TABLE `activity` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
  `title` VARCHAR(128) NOT NULL COMMENT '活动标题',
  `description` TEXT DEFAULT NULL COMMENT '活动描述',
  `type_id` BIGINT NOT NULL COMMENT '活动类型ID',
  `creator_id` BIGINT NOT NULL COMMENT '发起人ID',
  `cover_image` VARCHAR(512) DEFAULT NULL COMMENT '封面图URL',
  `start_time` DATETIME NOT NULL COMMENT '开始时间',
  `end_time` DATETIME DEFAULT NULL COMMENT '结束时间',
  `location_name` VARCHAR(128) DEFAULT NULL COMMENT '地点名称',
  `address` VARCHAR(256) DEFAULT NULL COMMENT '详细地址',
  `latitude` DECIMAL(10,7) DEFAULT NULL COMMENT '纬度',
  `longitude` DECIMAL(10,7) DEFAULT NULL COMMENT '经度',
  `min_participants` INT NOT NULL DEFAULT 1 COMMENT '最少人数',
  `max_participants` INT DEFAULT NULL COMMENT '最多人数',
  `payment_mode` VARCHAR(16) NOT NULL COMMENT '付费模式：aa, male_free, treat, free',
  `total_fee` DECIMAL(10,2) NOT NULL DEFAULT 0.00 COMMENT '总费用（元）',
  `fee_per_person` DECIMAL(10,2) DEFAULT NULL COMMENT '人均费用（元）',
  `signup_deadline` DATETIME DEFAULT NULL COMMENT '报名截止时间',
  `current_participants` INT NOT NULL DEFAULT 0 COMMENT '当前参与人数',
  `status` VARCHAR(16) NOT NULL DEFAULT 'draft' COMMENT '状态',
  `is_deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '软删除标记',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_creator_id` (`creator_id`),
  KEY `idx_type_id` (`type_id`),
  KEY `idx_start_time` (`start_time`),
  KEY `idx_status` (`status`),
  KEY `idx_location` (`latitude`, `longitude`),
  KEY `idx_created_at` (`created_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='活动表';

-- 活动参与者表
DROP TABLE IF EXISTS `activity_participant`;
CREATE TABLE `activity_participant` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
  `activity_id` BIGINT NOT NULL COMMENT '活动ID',
  `user_id` BIGINT NOT NULL COMMENT '用户ID',
  `fee` DECIMAL(10,2) NOT NULL DEFAULT 0.00 COMMENT '应付费用',
  `payment_status` VARCHAR(16) NOT NULL DEFAULT 'unpaid' COMMENT '支付状态：unpaid, paid, refunded',
  `check_in_time` DATETIME DEFAULT NULL COMMENT '签到时间',
  `check_in_location` VARCHAR(256) DEFAULT NULL COMMENT '签到位置描述',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '报名时间',
  `cancelled_at` DATETIME DEFAULT NULL COMMENT '取消时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_activity_user` (`activity_id`, `user_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_payment_status` (`payment_status`),
  KEY `idx_created_at` (`created_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='活动参与者表';

-- ----------------------------
-- 支付相关表
-- ----------------------------

-- 支付记录表
DROP TABLE IF EXISTS `payment`;
CREATE TABLE `payment` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
  `order_no` VARCHAR(64) NOT NULL COMMENT '订单号',
  `transaction_id` VARCHAR(64) DEFAULT NULL COMMENT '微信交易号',
  `user_id` BIGINT NOT NULL COMMENT '用户ID',
  `activity_id` BIGINT DEFAULT NULL COMMENT '关联活动ID',
  `participant_id` BIGINT DEFAULT NULL COMMENT '参与者记录ID',
  `amount` DECIMAL(10,2) NOT NULL DEFAULT 0.00 COMMENT '支付金额',
  `payment_type` VARCHAR(16) NOT NULL COMMENT '支付类型：wechat',
  `payment_status` VARCHAR(16) NOT NULL DEFAULT 'pending' COMMENT '支付状态',
  `payment_time` DATETIME DEFAULT NULL COMMENT '支付时间',
  `refund_amount` DECIMAL(10,2) DEFAULT NULL COMMENT '退款金额',
  `refund_time` DATETIME DEFAULT NULL COMMENT '退款时间',
  `refund_reason` VARCHAR(256) DEFAULT NULL COMMENT '退款原因',
  `prepay_id` VARCHAR(128) DEFAULT NULL COMMENT '预支付ID',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_order_no` (`order_no`),
  UNIQUE KEY `uk_transaction_id` (`transaction_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_activity_id` (`activity_id`),
  KEY `idx_payment_status` (`payment_status`),
  KEY `idx_created_at` (`created_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='支付记录表';

-- ----------------------------
-- 消息相关表
-- ----------------------------

-- 会话表
DROP TABLE IF EXISTS `conversation`;
CREATE TABLE `conversation` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
  `conversation_type` TINYINT NOT NULL DEFAULT 1 COMMENT '会话类型：1私聊 2群聊 3系统',
  `activity_id` BIGINT DEFAULT NULL COMMENT '关联活动ID',
  `conversation_name` VARCHAR(64) DEFAULT NULL COMMENT '会话名称',
  `avatar` VARCHAR(512) DEFAULT NULL COMMENT '会话头像',
  `user_id_1` BIGINT DEFAULT NULL COMMENT '用户1（私聊）',
  `user_id_2` BIGINT DEFAULT NULL COMMENT '用户2（私聊）',
  `last_message` VARCHAR(512) DEFAULT NULL COMMENT '最后一条消息预览',
  `last_message_time` DATETIME DEFAULT NULL COMMENT '最后消息时间',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_activity_id` (`activity_id`),
  KEY `idx_user_id_1` (`user_id_1`),
  KEY `idx_user_id_2` (`user_id_2`),
  KEY `idx_last_message_time` (`last_message_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='会话表';

-- 消息表
DROP TABLE IF EXISTS `message`;
CREATE TABLE `message` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
  `conversation_id` BIGINT NOT NULL COMMENT '会话ID',
  `sender_id` BIGINT NOT NULL COMMENT '发送者ID',
  `message_type` VARCHAR(16) NOT NULL DEFAULT 'text' COMMENT '消息类型：text, image, voice, system',
  `content` TEXT DEFAULT NULL COMMENT '消息内容',
  `media_url` VARCHAR(512) DEFAULT NULL COMMENT '媒体文件URL',
  `is_read` TINYINT NOT NULL DEFAULT 0 COMMENT '已读标记',
  `read_at` DATETIME DEFAULT NULL COMMENT '已读时间',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '发送时间',
  PRIMARY KEY (`id`),
  KEY `idx_conversation_id` (`conversation_id`),
  KEY `idx_sender_id` (`sender_id`),
  KEY `idx_created_at` (`created_at`),
  KEY `idx_is_read` (`is_read`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='消息表';

-- ----------------------------
-- 评价相关表
-- ----------------------------

-- 评价表
DROP TABLE IF EXISTS `review`;
CREATE TABLE `review` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
  `activity_id` BIGINT NOT NULL COMMENT '活动ID',
  `reviewer_id` BIGINT NOT NULL COMMENT '评价者ID',
  `reviewee_id` BIGINT NOT NULL COMMENT '被评价者ID',
  `rating` TINYINT NOT NULL DEFAULT 5 COMMENT '评分（1-5星）',
  `comment` VARCHAR(512) DEFAULT NULL COMMENT '评价内容',
  `tags` VARCHAR(256) DEFAULT NULL COMMENT '评价标签（JSON数组）',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '评价时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_activity_reviewer_reviewee` (`activity_id`, `reviewer_id`, `reviewee_id`),
  KEY `idx_reviewer_id` (`reviewer_id`),
  KEY `idx_reviewee_id` (`reviewee_id`),
  KEY `idx_rating` (`rating`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='评价表';

-- 信用记录表
DROP TABLE IF EXISTS `credit_record`;
CREATE TABLE `credit_record` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` BIGINT NOT NULL COMMENT '用户ID',
  `type` VARCHAR(16) NOT NULL COMMENT '类型：increase, decrease',
  `amount` INT NOT NULL DEFAULT 0 COMMENT '变化量',
  `before_score` INT NOT NULL DEFAULT 100 COMMENT '变化前分数',
  `after_score` INT NOT NULL DEFAULT 100 COMMENT '变化后分数',
  `reason` VARCHAR(256) NOT NULL COMMENT '原因',
  `related_id` BIGINT DEFAULT NULL COMMENT '关联ID',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_created_at` (`created_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='信用记录表';

-- ----------------------------
-- 其他表
-- ----------------------------

-- 举报表
DROP TABLE IF EXISTS `report`;
CREATE TABLE `report` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
  `reporter_id` BIGINT NOT NULL COMMENT '举报人ID',
  `reported_user_id` BIGINT DEFAULT NULL COMMENT '被举报用户ID',
  `reported_activity_id` BIGINT DEFAULT NULL COMMENT '被举报活动ID',
  `reason` VARCHAR(16) NOT NULL COMMENT '举报类型',
  `description` VARCHAR(512) DEFAULT NULL COMMENT '详细描述',
  `images` TEXT DEFAULT NULL COMMENT '证据图片（JSON数组）',
  `status` VARCHAR(16) NOT NULL DEFAULT 'pending' COMMENT '处理状态',
  `handler_id` BIGINT DEFAULT NULL COMMENT '处理人ID',
  `handle_result` VARCHAR(512) DEFAULT NULL COMMENT '处理结果',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `handled_at` DATETIME DEFAULT NULL COMMENT '处理时间',
  PRIMARY KEY (`id`),
  KEY `idx_reporter_id` (`reporter_id`),
  KEY `idx_reported_user_id` (`reported_user_id`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='举报表';

-- 系统配置表
DROP TABLE IF EXISTS `system_config`;
CREATE TABLE `system_config` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
  `config_key` VARCHAR(64) NOT NULL COMMENT '配置键',
  `config_value` TEXT DEFAULT NULL COMMENT '配置值',
  `description` VARCHAR(256) DEFAULT NULL COMMENT '描述',
  `is_public` TINYINT NOT NULL DEFAULT 0 COMMENT '是否公开',
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_config_key` (`config_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='系统配置表';

-- ----------------------------
-- 初始化数据
-- ----------------------------

-- 活动类型
INSERT INTO `activity_type` (type_code, type_name, category, icon, sort_order) VALUES
('dining', '吃喝玩乐', 'offline', '/icons/dining.png', 1),
('outdoor', '户外游玩', 'offline', '/icons/outdoor.png', 2),
('family', '亲子活动', 'offline', '/icons/family.png', 3),
('sports', '户外运动', 'offline', '/icons/sports.png', 4),
('dating', '相亲交友', 'offline', '/icons/dating.png', 5),
('game', '线上游戏', 'online', '/icons/game.png', 10),
('study', '线上学习', 'online', '/icons/study.png', 11),
('social', '线上社交', 'online', '/icons/social.png', 12);

-- 系统配置
INSERT INTO `system_config` (config_key, config_value, description, is_public) VALUES
('app_name', '搭子交友', '应用名称', 1),
('default_credit_score', '100', '初始信用分', 0),
('location_accuracy', '100', '定位精度（米）', 0),
('max_upload_images', '9', '最大上传图片数', 1),
('match_algorithm_weights', '{"distance":0.3,"interest":0.4,"zodiac":0.15,"zodiac_animal":0.15}', '匹配算法权重', 0);

SET FOREIGN_KEY_CHECKS = 1;
```

---

**文档状态:** ✅ 已完成  
**下一步:** API接口设计 (api-design.md)

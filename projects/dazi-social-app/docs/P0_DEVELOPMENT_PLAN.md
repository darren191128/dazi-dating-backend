# 搭子交友微信小程序 - P0功能开发计划

**制定日期**: 2026-03-24  
**制定人**: 云壹  
**目标**: 完成P0核心功能，让产品达到可用状态

---

## 一、P0功能清单（12项）

### 1. 社交动态系统（4项）
- [ ] 动态发布（图文）
- [ ] 动态浏览（广场/关注）
- [ ] 点赞功能
- [ ] 评论功能

### 2. 关注/粉丝体系（2项）
- [ ] 关注用户
- [ ] 粉丝列表

### 3. 消息增强（3项）
- [ ] 语音消息
- [ ] 图片消息
- [ ] 已读回执

### 4. 匹配机制（2项）
- [ ] 滑动匹配（喜欢/不喜欢）
- [ ] 互相喜欢才能聊天

### 5. 用户互动（1项）
- [ ] 访客记录

---

## 二、开发阶段规划

### 阶段一：数据库设计 + 后端API（3天）

#### Day 1: 动态系统数据库 + API
**负责人**: 云陆

**数据库表设计**:
```sql
-- moment 动态表
-- moment_like 动态点赞表
-- moment_comment 动态评论表
-- user_follow 用户关注表
-- user_visitor 访客记录表
-- user_match 用户匹配表（喜欢/不喜欢记录）
```

**API接口开发**:
- POST /api/moment/publish - 发布动态
- GET /api/moment/list - 获取动态列表
- GET /api/moment/detail/:id - 动态详情
- POST /api/moment/like - 点赞动态
- POST /api/moment/unlike - 取消点赞
- POST /api/moment/comment - 评论动态
- GET /api/moment/comments/:id - 获取评论列表

#### Day 2: 关注体系 + 访客记录 API
**负责人**: 云陆

**API接口开发**:
- POST /api/user/follow - 关注用户
- POST /api/user/unfollow - 取消关注
- GET /api/user/followings - 我的关注列表
- GET /api/user/followers - 我的粉丝列表
- GET /api/user/visitors - 访客记录
- POST /api/user/match/like - 喜欢用户（滑动匹配）
- POST /api/user/match/dislike - 不喜欢用户
- GET /api/user/match/mutual - 互相喜欢列表

#### Day 3: 消息增强 API
**负责人**: 云陆

**API接口开发**:
- WebSocket消息类型扩展（语音、图片）
- POST /api/message/upload/voice - 上传语音
- POST /api/message/upload/image - 上传图片
- GET /api/message/read/:userId - 标记已读
- GET /api/message/read-status/:messageId - 获取已读状态

---

### 阶段二：前端页面开发（4天）

#### Day 4: 动态系统页面
**负责人**: 云伍

**页面开发**:
1. **src/pages/moment/index.vue** - 动态广场
   - 动态列表展示（图文）
   - 下拉刷新、上拉加载
   - 切换到关注tab

2. **src/pages/moment/publish.vue** - 发布动态
   - 文本输入
   - 图片选择/上传（最多9张）
   - 发布按钮

3. **更新 src/pages/user/index.vue**
   - 添加"我的动态"入口

#### Day 5: 动态详情 + 关注功能
**负责人**: 云伍

**页面开发**:
1. **src/pages/moment/detail.vue** - 动态详情
   - 动态内容展示
   - 点赞列表
   - 评论列表
   - 发表评论

2. **src/pages/user/followings.vue** - 我的关注
3. **src/pages/user/followers.vue** - 我的粉丝

4. **更新用户卡片组件**
   - 添加关注/取消关注按钮

#### Day 6: 滑动匹配页面
**负责人**: 云伍

**页面开发**:
1. **更新 src/pages/match/index.vue**
   - 重构为滑动卡片式界面
   - 左右滑动动画
   - 喜欢/不喜欢按钮
   - 互相匹配提示

2. **src/pages/match/success.vue** - 匹配成功页
   - 展示匹配成功的用户信息
   - 立即聊天按钮

#### Day 7: 消息增强 + 访客记录
**负责人**: 云伍

**页面开发**:
1. **更新 src/pages/message/chat.vue**
   - 添加语音消息发送/播放
   - 添加图片消息发送/预览
   - 已读状态显示

2. **src/pages/user/visitors.vue** - 访客记录
   - 访客列表
   - 时间显示
   - 快速关注按钮

3. **更新 src/pages/user/index.vue**
   - 添加"访客记录"入口

---

### 阶段三：集成测试 + 优化（2天）

#### Day 8: 集成测试
**负责人**: 云柒

**测试内容**:
- 动态发布-浏览-点赞-评论全流程
- 关注-粉丝功能测试
- 滑动匹配-互相喜欢-聊天权限测试
- 语音/图片消息测试
- 访客记录测试

#### Day 9: Bug修复 + 优化
**负责人**: 云伍 + 云陆

**优化内容**:
- 性能优化
- UI细节调整
- 交互体验优化

---

## 三、技术方案

### 3.1 数据库设计

#### moment 动态表
```sql
CREATE TABLE moment (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL COMMENT '发布者ID',
    content TEXT COMMENT '动态内容',
    images JSON COMMENT '图片数组',
    location VARCHAR(128) COMMENT '位置信息',
    like_count INT DEFAULT 0 COMMENT '点赞数',
    comment_count INT DEFAULT 0 COMMENT '评论数',
    view_count INT DEFAULT 0 COMMENT '浏览数',
    is_top TINYINT DEFAULT 0 COMMENT '是否置顶',
    status TINYINT DEFAULT 1 COMMENT '状态：0删除 1正常',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_user_id (user_id),
    INDEX idx_created_at (created_at)
);
```

#### moment_like 动态点赞表
```sql
CREATE TABLE moment_like (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    moment_id BIGINT NOT NULL COMMENT '动态ID',
    user_id BIGINT NOT NULL COMMENT '点赞用户ID',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY uk_moment_user (moment_id, user_id),
    INDEX idx_user_id (user_id)
);
```

#### moment_comment 动态评论表
```sql
CREATE TABLE moment_comment (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    moment_id BIGINT NOT NULL COMMENT '动态ID',
    user_id BIGINT NOT NULL COMMENT '评论用户ID',
    parent_id BIGINT DEFAULT NULL COMMENT '父评论ID（回复）',
    content TEXT NOT NULL COMMENT '评论内容',
    like_count INT DEFAULT 0 COMMENT '点赞数',
    status TINYINT DEFAULT 1 COMMENT '状态：0删除 1正常',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_moment_id (moment_id),
    INDEX idx_user_id (user_id),
    INDEX idx_parent_id (parent_id)
);
```

#### user_follow 用户关注表
```sql
CREATE TABLE user_follow (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL COMMENT '用户ID',
    follow_user_id BIGINT NOT NULL COMMENT '被关注用户ID',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY uk_user_follow (user_id, follow_user_id),
    INDEX idx_follow_user_id (follow_user_id)
);
```

#### user_visitor 访客记录表
```sql
CREATE TABLE user_visitor (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL COMMENT '被访问用户ID',
    visitor_id BIGINT NOT NULL COMMENT '访客用户ID',
    visit_count INT DEFAULT 1 COMMENT '访问次数',
    last_visit_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '最后访问时间',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY uk_user_visitor (user_id, visitor_id),
    INDEX idx_user_id (user_id),
    INDEX idx_visitor_id (visitor_id)
);
```

#### user_match 用户匹配表
```sql
CREATE TABLE user_match (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL COMMENT '用户ID',
    target_user_id BIGINT NOT NULL COMMENT '目标用户ID',
    action TINYINT NOT NULL COMMENT '1喜欢 2不喜欢',
    is_mutual TINYINT DEFAULT 0 COMMENT '是否互相喜欢',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY uk_user_target (user_id, target_user_id),
    INDEX idx_target_user (target_user_id),
    INDEX idx_is_mutual (is_mutual)
);
```

### 3.2 前端技术方案

#### 滑动匹配实现
- 使用 `uni.createAnimation` 实现卡片滑动动画
- 监听 `touchstart`、`touchmove`、`touchend` 事件
- 滑动超过阈值触发喜欢/不喜欢动作

#### 语音消息实现
- 使用 `uni.getRecorderManager()` 录制语音
- 使用 `uni.createInnerAudioContext()` 播放语音
- 语音上传至OSS/云存储

#### 图片消息实现
- 使用 `uni.chooseImage` 选择图片
- 使用 `uni.uploadFile` 上传图片
- 使用 `uni.previewImage` 预览图片

---

## 四、任务分配

| 角色 | 任务 | 时间 |
|------|------|------|
| 云陆（后端） | 数据库设计 + 所有API开发 | Day 1-3 |
| 云伍（前端） | 所有页面开发 | Day 4-7 |
| 云柒（测试） | 集成测试 | Day 8 |
| 云伍+云陆 | Bug修复 | Day 9 |

---

## 五、验收标准

### 5.1 功能验收
- [ ] 用户可以发布图文动态
- [ ] 用户可以浏览广场动态和关注动态
- [ ] 用户可以给动态点赞、评论
- [ ] 用户可以关注/取消关注其他用户
- [ ] 用户可以查看粉丝列表和关注列表
- [ ] 用户可以通过滑动卡片喜欢/不喜欢其他用户
- [ ] 互相喜欢的用户才能开始聊天
- [ ] 聊天支持发送语音消息
- [ ] 聊天支持发送图片消息
- [ ] 消息显示已读状态
- [ ] 用户可以查看谁访问过自己的主页

### 5.2 性能验收
- [ ] 动态列表加载 < 1秒
- [ ] 滑动匹配流畅无卡顿
- [ ] 语音消息录制/播放正常
- [ ] 图片上传/加载正常

### 5.3 体验验收
- [ ] 滑动动画流畅自然
- [ ] 页面跳转无白屏
- [ ] 错误提示友好
- [ ] 空状态处理完善

---

## 六、风险与应对

| 风险 | 应对措施 |
|------|----------|
| 滑动匹配动画复杂 | 使用成熟的开源组件或简化动画 |
| 语音消息存储成本高 | 限制语音时长（60秒），定期清理 |
| 动态内容审核 | 接入内容审核API，敏感词过滤 |
| 并发访问压力大 | 使用Redis缓存，数据库优化 |

---

**计划制定完成，等待老板确认后开始执行。**

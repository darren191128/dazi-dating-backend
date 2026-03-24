# 搭子交友微信小程序 - P1功能开发计划

**制定日期**: 2026-03-24  
**制定人**: 云壹  
**目标**: 完成P1增值功能，提升产品竞争力和商业化能力

---

## 一、P1功能清单（14项）

### 1. 通讯增强（3项）
- [ ] 音视频通话（1对1）
- [ ] 群聊功能
- [ ] 消息免打扰

### 2. 用户互动（4项）
- [ ] 亲密度系统
- [ ] 礼物系统（赠送/接收）
- [ ] 访客隐身（VIP功能）
- [ ] 用户举报

### 3. 匹配优化（2项）
- [ ] 高级筛选（身高/学历/收入）
- [ ] 每日精选推荐

### 4. 内容社区（2项）
- [ ] 话题/标签系统
- [ ] 热门动态推荐

### 5. 商业化（3项）
- [ ] VIP会员体系
- [ ] 虚拟货币系统
- [ ] 每日签到

---

## 二、开发阶段规划

### 阶段一：商业化基础（VIP+货币+签到）- 3天

#### Day 1: VIP会员体系
**负责人**: 云陆 + 云伍

**后端开发**:
- 创建 `vip-service` 模块（端口8009）
- 数据库表：
  - `vip_package` - VIP套餐表
  - `user_vip` - 用户VIP记录表
  - `vip_privilege` - VIP特权表
- API接口：
  - GET /api/vip/packages - 获取VIP套餐
  - POST /api/vip/subscribe - 订阅VIP
  - GET /api/vip/status - 查询VIP状态
  - GET /api/vip/privileges - 获取特权列表

**前端开发**:
- 创建 `src/pages/vip/index.vue` - VIP中心
- 创建 `src/pages/vip/packages.vue` - 套餐选择
- 更新 `src/pages/user/index.vue` - 添加VIP入口

**VIP特权**:
- 无限滑动（非VIP每日限制20次）
- 访客隐身
- 高级筛选
- 已读回执
- 专属标识
- 优先推荐

#### Day 2: 虚拟货币系统
**负责人**: 云陆 + 云伍

**后端开发**:
- 更新 `payment-service`
- 数据库表：
  - `virtual_currency` - 货币类型表
  - `user_wallet` - 用户钱包表
  - `transaction_record` - 交易记录表
- API接口：
  - GET /api/payment/wallet - 查询钱包
  - POST /api/payment/recharge - 充值
  - POST /api/payment/consume - 消费
  - GET /api/payment/transactions - 交易记录

**前端开发**:
- 创建 `src/pages/wallet/index.vue` - 我的钱包
- 创建 `src/pages/wallet/recharge.vue` - 充值页面
- 更新 `src/pages/user/index.vue` - 添加钱包入口

**货币类型**:
- 金币（充值获得）
- 积分（签到/任务获得）

#### Day 3: 每日签到
**负责人**: 云伍

**后端开发**:
- 更新 `user-service`
- 数据库表：
  - `user_checkin` - 签到记录表
- API接口：
  - POST /api/user/checkin - 每日签到
  - GET /api/user/checkin/status - 签到状态
  - GET /api/user/checkin/records - 签到记录

**前端开发**:
- 创建 `src/components/CheckinButton.vue` - 签到按钮组件
- 更新 `src/pages/home/index.vue` - 添加签到入口
- 签到动画效果

---

### 阶段二：互动增强（礼物+亲密度+举报）- 3天

#### Day 4: 礼物系统
**负责人**: 云陆 + 云伍

**后端开发**:
- 创建 `gift-service` 模块（端口8010）
- 数据库表：
  - `gift` - 礼物表
  - `gift_category` - 礼物分类表
  - `gift_send_record` - 赠送记录表
- API接口：
  - GET /api/gift/list - 礼物列表
  - GET /api/gift/categories - 礼物分类
  - POST /api/gift/send - 赠送礼物
  - GET /api/gift/records - 赠送记录

**前端开发**:
- 创建 `src/components/GiftPanel.vue` - 礼物面板组件
- 更新 `src/pages/message/chat.vue` - 添加送礼物按钮
- 礼物动画效果

**礼物类型**:
- 普通礼物（金币）
- 豪华礼物（金币）
- 专属礼物（VIP专属）

#### Day 5: 亲密度系统
**负责人**: 云陆

**后端开发**:
- 更新 `user-service`
- 数据库表：
  - `user_intimacy` - 亲密度表
- API接口：
  - GET /api/user/intimacy/:userId - 查询亲密度
  - GET /api/user/intimacy/list - 亲密度列表
- 亲密度计算规则：
  - 聊天：+1分/条
  - 送礼物：+礼物价值分
  - 互相关注：+50分
  - 一起参加活动：+100分

**前端开发**:
- 更新 `src/pages/message/chat.vue` - 显示亲密度
- 创建 `src/components/IntimacyBadge.vue` - 亲密度标识

#### Day 6: 用户举报
**负责人**: 云伍

**后端开发**:
- 更新 `admin-service`
- 数据库表：
  - `user_report` - 举报记录表
- API接口：
  - POST /api/user/report - 举报用户
  - 举报类型：骚扰、欺诈、色情、广告、其他

**前端开发**:
- 创建 `src/components/ReportPanel.vue` - 举报面板
- 在用户卡片、聊天页面添加举报入口

---

### 阶段三：通讯增强（音视频+群聊）- 4天

#### Day 7-8: 音视频通话
**负责人**: 云陆 + 云伍

**技术方案**:
- 使用腾讯云TRTC（实时音视频）
- 或声网Agora SDK

**后端开发**:
- 创建 `rtc-service` 模块（端口8011）
- API接口：
  - POST /api/rtc/token - 获取通话token
  - POST /api/rtc/call - 发起通话
  - POST /api/rtc/accept - 接受通话
  - POST /api/rtc/reject - 拒绝通话
  - POST /api/rtc/end - 结束通话

**前端开发**:
- 创建 `src/pages/call/index.vue` - 通话页面
- 创建 `src/components/IncomingCall.vue` - 来电弹窗
- 更新 `src/pages/message/chat.vue` - 添加音视频通话按钮

**通话功能**:
- 1对1语音通话
- 1对1视频通话
- 通话记录
- 通话时长统计

#### Day 9-10: 群聊功能
**负责人**: 云陆 + 云伍

**后端开发**:
- 更新 `message-service`
- 数据库表：
  - `chat_group` - 群聊表
  - `chat_group_member` - 群成员表
  - `chat_group_message` - 群消息表
- API接口：
  - POST /api/message/group/create - 创建群聊
  - POST /api/message/group/join - 加入群聊
  - POST /api/message/group/leave - 退出群聊
  - GET /api/message/group/list - 群聊列表
  - GET /api/message/group/:id/messages - 群消息

**前端开发**:
- 创建 `src/pages/message/group-create.vue` - 创建群聊
- 创建 `src/pages/message/group-chat.vue` - 群聊聊天
- 更新 `src/pages/message/index.vue` - 显示群聊列表

---

### 阶段四：匹配优化（高级筛选+推荐）- 2天

#### Day 11: 高级筛选
**负责人**: 云伍

**后端开发**:
- 更新 `match-service`
- 扩展筛选条件：
  - 身高范围
  - 学历
  - 收入范围
  - 职业
  - 是否有房/车
- API接口：
  - GET /api/match/recommendations - 扩展筛选参数

**前端开发**:
- 创建 `src/components/FilterPanel.vue` - 筛选面板
- 更新 `src/pages/match/index.vue` - 添加筛选按钮

#### Day 12: 每日精选
**负责人**: 云伍

**后端开发**:
- 更新 `match-service`
- 每日精选算法：
  - 高质量用户优先
  - 匹配度高优先
  - 活跃度优先
- API接口：
  - GET /api/match/daily-picks - 每日精选

**前端开发**:
- 更新 `src/pages/home/index.vue` - 添加每日精选卡片
- 创建 `src/components/DailyPickCard.vue` - 精选用户卡片

---

### 阶段五：内容社区（话题+热门）- 2天

#### Day 13: 话题/标签系统
**负责人**: 云陆 + 云伍

**后端开发**:
- 更新 `moment-service`
- 数据库表：
  - `topic` - 话题表
  - `moment_topic` - 动态话题关联表
- API接口：
  - GET /api/moment/topics - 话题列表
  - GET /api/moment/topic/:id - 话题详情
  - GET /api/moment/topic/:id/moments - 话题动态

**前端开发**:
- 创建 `src/pages/topic/index.vue` - 话题广场
- 创建 `src/pages/topic/detail.vue` - 话题详情
- 更新 `src/pages/moment/publish.vue` - 添加话题选择

#### Day 14: 热门动态推荐
**负责人**: 云伍

**后端开发**:
- 更新 `moment-service`
- 推荐算法：
  - 点赞数加权
  - 评论数加权
  - 时间衰减
  - 用户兴趣匹配
- API接口：
  - GET /api/moment/hot - 热门动态

**前端开发**:
- 更新 `src/pages/moment/index.vue` - 添加"热门"Tab
- 热门标识

---

### 阶段六：集成测试 + 优化（2天）

#### Day 15: 集成测试
**负责人**: 云柒

测试内容：
- VIP购买流程
- 货币充值/消费
- 礼物赠送
- 音视频通话
- 群聊功能
- 高级筛选
- 话题系统

#### Day 16: Bug修复 + 性能优化
**负责人**: 云伍 + 云陆

优化内容：
- 性能优化
- 内存优化
- 用户体验优化

---

## 三、技术方案

### 3.1 音视频通话
```
方案：腾讯云TRTC
优点：
- 微信小程序原生支持
- 低延迟
- 稳定可靠
- 按量计费

集成步骤：
1. 注册腾讯云账号
2. 创建TRTC应用
3. 后端生成UserSig
4. 前端集成SDK
5. 实现通话逻辑
```

### 3.2 虚拟货币
```
充值方式：
- 微信支付
- 苹果内购（iOS）

消费场景：
- 购买VIP
- 赠送礼物
- 解锁高级功能
```

### 3.3 VIP特权实现
```
实现方式：
- 后端接口返回VIP状态
- 前端根据VIP状态控制功能
- 中间件拦截非VIP操作

存储：
- Redis缓存VIP状态
- 数据库持久化
```

---

## 四、数据库表汇总

### 新增表（11个）

```sql
-- VIP相关
vip_package           -- VIP套餐
user_vip              -- 用户VIP记录
vip_privilege         -- VIP特权

-- 货币相关
virtual_currency      -- 货币类型
user_wallet           -- 用户钱包
transaction_record    -- 交易记录

-- 礼物相关
gift                  -- 礼物
gift_category         -- 礼物分类
gift_send_record      -- 赠送记录

-- 群聊相关
chat_group            -- 群聊
chat_group_member     -- 群成员

-- 其他
user_checkin          -- 签到记录
user_intimacy         -- 亲密度
user_report           -- 举报记录
topic                 -- 话题
moment_topic          -- 动态话题关联
```

---

## 五、任务分配

| 角色 | 主要任务 | 时间 |
|------|----------|------|
| 云陆（后端） | VIP、货币、礼物、音视频、群聊后端 | Day 1-14 |
| 云伍（前端） | VIP、货币、礼物、音视频、群聊前端 | Day 1-14 |
| 云柒（测试） | 集成测试 | Day 15 |
| 云伍+云陆 | Bug修复 | Day 16 |

---

## 六、验收标准

### 6.1 VIP会员
- [ ] 可以购买VIP套餐
- [ ] VIP状态正确显示
- [ ] VIP特权生效（无限滑动、隐身等）
- [ ] VIP到期提醒

### 6.2 虚拟货币
- [ ] 可以充值金币
- [ ] 消费记录正确
- [ ] 余额显示正确

### 6.3 礼物系统
- [ ] 可以赠送礼物
- [ ] 礼物动画正常
- [ ] 接收方收到通知

### 6.4 音视频通话
- [ ] 可以发起语音通话
- [ ] 可以发起视频通话
- [ ] 通话质量良好
- [ ] 通话记录保存

### 6.5 群聊
- [ ] 可以创建群聊
- [ ] 可以发送群消息
- [ ] 群成员管理

### 6.6 高级筛选
- [ ] 筛选条件生效
- [ ] 筛选结果准确

### 6.7 话题系统
- [ ] 可以创建话题
- [ ] 动态可以添加话题
- [ ] 话题聚合展示

---

## 七、风险与应对

| 风险 | 应对措施 |
|------|----------|
| 音视频SDK集成复杂 | 预留2天时间，准备备选方案 |
| 支付审核周期长 | 提前申请微信支付权限 |
| 性能问题 | 提前进行压力测试 |
| 内容审核 | 接入阿里云/腾讯云内容审核 |

---

## 八、里程碑

| 里程碑 | 时间 | 交付物 |
|--------|------|--------|
| M1 | Day 3 | VIP+货币+签到完成 |
| M2 | Day 6 | 礼物+亲密度+举报完成 |
| M3 | Day 10 | 音视频+群聊完成 |
| M4 | Day 14 | 高级筛选+话题完成 |
| M5 | Day 16 | 全部完成，可发布 |

---

**计划制定完成，等待老板确认后开始执行。**

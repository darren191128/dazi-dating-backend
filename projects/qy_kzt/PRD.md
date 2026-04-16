# 开州通综合服务平台 - 产品需求文档 (PRD)

**版本**: V2.0  
**日期**: 2026-04-16  
**编写**: 云捌 (项目与产品负责人)  
**状态**: 行业最佳实践完善版

---

## 版本变更记录

| 版本 | 日期 | 变更内容 | 变更人 |
|------|------|---------|--------|
| V1.0 | 2026-04-15 | 初稿创建 | 云捌 |
| V1.1 | 2026-04-16 | 1. 补充详细功能需求<br>2. 补充业务流程图描述<br>3. 补充数据埋点需求<br>4. 补充非功能需求<br>5. 补充运营后台需求<br>6. 补充版本规划<br>7. 补充风险评估 | 云捌 |
| V2.0 | 2026-04-16 | **行业顶级水准完善版**:<br>1. 补充外卖业务最佳实践（美团/饿了么标准）<br>2. 补充电商平台标准功能（淘宝/京东规范）<br>3. 补充B2B采购平台功能（1688参考）<br>4. 补充在线教育平台功能<br>5. 补充本地生活服务平台架构<br>6. 补充详细算法说明（推荐/排序/调度）<br>7. 补充完整API接口规范（RESTful + GraphQL）<br>8. 补充详细数据库表结构设计<br>9. 补充详细测试用例（功能/性能/安全）<br>10. 补充微服务架构设计<br>11. 补充高并发处理方案<br>12. 补充支付系统安全规范 | 云捌 |

---

## 目录

1. [项目概述](#1-项目概述)
2. [业务架构](#2-业务架构)
3. [功能需求](#3-功能需求)
4. [算法设计](#4-算法设计)
5. [API接口规范](#5-api接口规范)
6. [数据库设计](#6-数据库设计)
7. [测试用例](#7-测试用例)
8. [技术架构](#8-技术架构)
9. [非功能需求](#9-非功能需求)
10. [运营后台](#10-运营后台)
11. [版本规划](#11-版本规划)
12. [风险评估](#12-风险评估)
13. [实施路线图](#13-实施路线图)
14. [附录](#14-附录)

---

## 1. 项目概述

### 1.1 项目背景

开州通综合服务平台是面向重庆市开州区的一站式本地生活服务平台，整合外卖、商城、企业直采、金厨培训四大核心业务，为本地居民和企业提供便捷的生活服务和商业解决方案。

### 1.2 项目目标

- **用户端**: 打造开州区最便捷的本地生活服务平台
- **商家端**: 帮助本地商家数字化转型，提升经营效率
- **企业端**: 为企业提供一站式采购解决方案
- **培训端**: 构建职业技能培训生态，助力人才发展

### 1.3 目标用户

| 用户类型 | 描述 | 核心需求 |
|---------|------|---------|
| 个人消费者 | 开州区居民 | 便捷购物、外卖订餐、培训学习 |
| 商家 | 本地餐饮/零售商家 | 线上获客、订单管理、数据分析 |
| 企业采购方 | 本地企业/机构 | 批量采购、账期管理、发票服务 |
| 培训机构 | 职业技能学校 | 课程发布、学员管理、证书发放 |

---

## 2. 业务架构

### 2.1 业务全景图

```
┌─────────────────────────────────────────────────────────────────┐
│                        开州通综合服务平台                         │
├─────────────┬─────────────┬─────────────┬─────────────────────┤
│   外卖业务   │   商城业务   │  企业直采   │     金厨培训        │
├─────────────┼─────────────┼─────────────┼─────────────────────┤
│ • 餐饮外卖   │ • B2C零售   │ • B2B批发   │ • 课程发布          │
│ • 生鲜配送   │ • 同城配送   │ • 批量采购   │ • 在线报名          │
│ • 即时配送   │ • 物流跟踪   │ • 账期管理   │ • 学习管理          │
│ • 骑手调度   │ • 售后管理   │ • 发票管理   │ • 证书发放          │
└─────────────┴─────────────┴─────────────┴─────────────────────┘
```

### 2.2 核心业务模式

#### 2.2.1 外卖业务模式（参考美团外卖/饿了么）

**业务流程**:
```
用户下单 → 支付 → 商家接单 → 骑手取餐 → 配送中 → 送达 → 评价
```

**核心特性**:
- **智能派单**: 基于LBS的最近骑手分配
- **实时追踪**: GPS定位，配送轨迹可视化
- **预约配送**: 支持预约时间点送达
- **多店合并**: 同一用户多店订单合并配送
- **蜂鸟众包**: 众包配送模式支持

#### 2.2.2 商城业务模式（参考淘宝/京东）

**业务流程**:
```
浏览商品 → 加入购物车 → 结算 → 支付 → 商家发货 → 物流跟踪 → 确认收货 → 评价
```

**核心特性**:
- **搜索推荐**: 基于用户行为的个性化推荐
- **购物车**: 跨店购物车，统一结算
- **物流整合**: 多物流商对接，智能选择
- **售后体系**: 7天无理由退货，平台介入

#### 2.2.3 企业直采模式（参考阿里巴巴1688）

**业务流程**:
```
企业认证 → 浏览商品 → 加入采购车 → 询价/议价 → 下单 → 账期支付 → 发货 → 对账结算
```

**核心特性**:
- **企业认证**: 营业执照、资质审核
- **信用体系**: 企业信用评级，授信额度
- **账期管理**: 月结、季结等多种账期
- **批量定价**: 阶梯价格，量大从优
- **发票服务**: 增值税专用发票支持

#### 2.2.4 金厨培训模式（参考网易云课堂）

**业务流程**:
```
课程浏览 → 在线报名 → 支付学费 → 线下上课 → 考试评估 → 证书发放
```

**核心特性**:
- **课程管理**: 课程发布、排课管理
- **学员管理**: 报名管理、考勤记录
- **证书系统**: 电子证书、证书查询
- **就业对接**: 企业招聘对接

---

## 3. 功能需求

### 3.1 用户端功能

#### 3.1.1 首页

| 功能模块 | 功能描述 | 优先级 | 参考标准 |
|---------|---------|--------|---------|
| 搜索栏 | 全局搜索，支持商品/商家/课程 | P0 | 淘宝搜索 |
| 轮播Banner | 运营活动、广告位 | P0 | 美团首页 |
| 分类入口 | 外卖/商城/直采/培训快速入口 | P0 | 58同城 |
| 附近商家 | 基于LBS的附近商家推荐 | P0 | 美团外卖 |
| 推荐商品 | 个性化商品推荐 | P0 | 京东推荐 |
| 限时秒杀 | 限时抢购活动 | P1 | 拼多多 |
| 优惠券入口 | 领券中心入口 | P1 | 美团 |

#### 3.1.2 外卖模块

| 功能模块 | 功能描述 | 优先级 | 参考标准 |
|---------|---------|--------|---------|
| 商家列表 | 按距离/销量/评分排序 | P0 | 美团外卖 |
| 商家筛选 | 分类/配送费/起送价/评分筛选 | P0 | 饿了么 |
| 商家详情 | 菜单/评价/商家信息 | P0 | 美团外卖 |
| 购物车 | 多规格商品选择 | P0 | 美团外卖 |
| 订单确认 | 地址/配送时间/备注/优惠券 | P0 | 美团外卖 |
| 订单追踪 | 实时配送状态/骑手位置 | P0 | 美团外卖 |
| 评价系统 | 评分/文字/图片评价 | P0 | 美团外卖 |
| 预约配送 | 选择配送时间段 | P1 | 饿了么 |
| 多人拼单 | 好友拼单功能 | P2 | 美团拼好饭 |

#### 3.1.3 商城模块

| 功能模块 | 功能描述 | 优先级 | 参考标准 |
|---------|---------|--------|---------|
| 商品分类 | 多级分类导航 | P0 | 淘宝 |
| 商品列表 | 综合/销量/价格/新品排序 | P0 | 京东 |
| 商品筛选 | 品牌/价格/属性筛选 | P0 | 淘宝 |
| 商品详情 | 图片/视频/参数/评价 | P0 | 京东 |
| 购物车 | 跨店购物车，批量管理 | P0 | 淘宝 |
| 收藏夹 | 商品/店铺收藏 | P0 | 京东 |
| 足迹 | 浏览历史记录 | P1 | 淘宝 |
| 物流查询 | 快递跟踪/地图展示 | P0 | 京东 |
| 售后申请 | 退货/换货/退款 | P0 | 淘宝 |

#### 3.1.4 企业直采模块

| 功能模块 | 功能描述 | 优先级 | 参考标准 |
|---------|---------|--------|---------|
| 企业认证 | 营业执照上传/审核 | P0 | 1688 |
| 批发商品 | 批发价展示，MOQ显示 | P0 | 1688 |
| 采购车 | 批量商品管理 | P0 | 1688 |
| 询价功能 | 批量询价/议价 | P0 | 1688 |
| 账期申请 | 信用额度申请 | P1 | 1688诚e赊 |
| 发票管理 | 发票申请/查询/下载 | P0 | 京东企业购 |
| 采购订单 | 订单管理/对账单 | P0 | 1688 |
| 企业收藏 | 常购商品/供应商收藏 | P1 | 1688 |

#### 3.1.5 金厨培训模块

| 功能模块 | 功能描述 | 优先级 | 参考标准 |
|---------|---------|--------|---------|
| 课程分类 | 中餐/西餐/烘焙/小吃 | P0 | 腾讯课堂 |
| 课程列表 | 按热度/价格/评分排序 | P0 | 网易云课堂 |
| 课程详情 | 课程介绍/师资/课表 | P0 | 腾讯课堂 |
| 在线报名 | 选择班次/填写信息 | P0 | 网易云课堂 |
| 我的课程 | 已报名课程管理 | P0 | 腾讯课堂 |
| 证书查询 | 电子证书下载/验证 | P1 | 中国大学MOOC |
| 评价系统 | 课程评价/晒证 | P1 | 网易云课堂 |

### 3.2 商家端功能

#### 3.2.1 商家工作台

| 功能模块 | 功能描述 | 优先级 | 参考标准 |
|---------|---------|--------|---------|
| 经营概况 | 今日订单/营业额/访客数 | P0 | 美团商家版 |
| 订单管理 | 新订单/待配送/已完成 | P0 | 美团商家版 |
| 商品管理 | 菜品/商品上架下架 | P0 | 美团商家版 |
| 营销活动 | 满减/折扣/优惠券设置 | P0 | 美团商家版 |
| 数据分析 | 营业报表/客流分析 | P1 | 美团商家版 |
| 评价管理 | 用户评价回复/申诉 | P0 | 美团商家版 |
| 财务结算 | 账单查询/提现申请 | P0 | 美团商家版 |
| 店铺设置 | 营业时间/配送设置 | P0 | 美团商家版 |

#### 3.2.2 配送管理

| 功能模块 | 功能描述 | 优先级 | 参考标准 |
|---------|---------|--------|---------|
| 配送方式 | 自配送/平台配送 | P0 | 美团外卖 |
| 骑手管理 | 绑定骑手/配送范围 | P0 | 美团外卖 |
| 配送设置 | 配送费/起送价/配送时间 | P0 | 饿了么 |
| 配送监控 | 实时配送状态查看 | P0 | 美团外卖 |

### 3.3 骑手端功能

| 功能模块 | 功能描述 | 优先级 | 参考标准 |
|---------|---------|--------|---------|
| 订单大厅 | 可抢订单列表 | P0 | 美团众包 |
| 抢单/派单 | 抢单模式/系统派单 | P0 | 美团众包 |
| 订单详情 | 取餐地址/送餐地址/联系 | P0 | 美团众包 |
| 导航功能 | 内置地图导航 | P0 | 美团众包 |
| 订单状态 | 取餐中/配送中/已送达 | P0 | 美团众包 |
| 收入统计 | 今日收入/订单数/里程 | P0 | 美团众包 |
| 提现功能 | 收入提现申请 | P0 | 美团众包 |

---

## 4. 算法设计

### 4.1 推荐算法

#### 4.1.1 商品推荐算法

**算法类型**: 协同过滤 + 深度学习混合推荐

**算法公式**:
```
推荐分数 = α * 协同过滤分数 + β * 内容相似度 + γ * 热度分数 + δ * 个性化权重

其中:
- α = 0.4 (协同过滤权重)
- β = 0.3 (内容相似度权重)
- γ = 0.2 (热度权重)
- δ = 0.1 (个性化权重)
```

**协同过滤算法**:
```python
# 用户-物品协同过滤
用户相似度 = cosine_similarity(用户A的行为向量, 用户B的行为向量)
推荐物品 = Σ(相似用户喜欢的物品 × 相似度权重)

# 物品-物品协同过滤
物品相似度 = cosine_similarity(物品A的特征向量, 物品B的特征向量)
推荐物品 = 基于当前物品找到最相似的K个物品
```

**深度学习模型** (参考Wide&Deep):
```
Wide部分: 记忆能力，处理离散特征交叉
  - 用户ID × 商品类目
  - 用户地域 × 商品地域
  
Deep部分: 泛化能力，Embedding + DNN
  - 用户Embedding (128维)
  - 商品Embedding (128维)
  - 上下文Embedding (64维)
  - 三层全连接网络: 512 → 256 → 128 → 1
```

**冷启动处理**:
- 新用户: 基于地理位置和人口统计的默认推荐
- 新商品: 基于内容相似度的推荐
- 探索机制: ε-greedy策略，10%流量用于探索

#### 4.1.2 商家推荐算法

**排序公式**:
```
商家得分 = w1×销量分 + w2×评分分 + w3×距离分 + w4×转化率分 + w5×活动分 + w6×新店加权

权重配置:
- w1 (销量) = 0.25
- w2 (评分) = 0.25
- w3 (距离) = 0.20
- w4 (转化率) = 0.15
- w5 (活动) = 0.10
- w6 (新店) = 0.05
```

**距离衰减函数**:
```
distance_score = 1 / (1 + 0.1 × distance_km)
```

### 4.2 搜索排序算法

#### 4.2.1 搜索相关性算法

**文本相关性**:
```
BM25算法:
score = Σ IDF(q_i) × [f(q_i,D) × (k1+1)] / [f(q_i,D) + k1 × (1-b+b×|D|/avgdl)]

参数:
- k1 = 1.2 (控制词频饱和度)
- b = 0.75 (控制文档长度归一化)
```

**语义相关性** (BERT向量相似度):
```
语义相似度 = cosine_similarity(BERT(查询), BERT(商品标题))
```

#### 4.2.2 搜索排序综合得分

```
最终得分 = 相关性得分 × 商业质量得分 × 个性化得分

商业质量得分 = 销量^0.3 × 评分^0.3 × 转化率^0.2 × 点击率^0.2

个性化得分 = 用户历史偏好匹配度
```

### 4.3 骑手调度算法

#### 4.3.1 派单算法

**目标**: 最小化配送时间，最大化骑手效率

**约束条件**:
- 骑手当前负载 ≤ 最大负载 (默认4单)
- 预计送达时间 ≤ 承诺送达时间
- 骑手位置与商家距离 ≤ 最大取餐距离

**算法步骤**:
```python
1. 计算订单与所有可用骑手的匹配分数:
   match_score = w1/distance + w2×rider_rating + w3×rider_load_factor
   
2. 考虑顺路程度:
   detour_score = 1 / (1 + detour_distance)
   
3. 最终派单分数:
   dispatch_score = 0.5×match_score + 0.3×detour_score + 0.2×fairness_score
   
4. 选择dispatch_score最高的骑手
```

#### 4.3.2 路径规划算法

**多订单路径优化** (TSP变体):
```
目标: 最小化总配送时间
约束: 每个订单的预计送达时间约束

算法: 遗传算法 + 2-opt局部优化

适应度函数:
fitness = 1 / (total_time + penalty_for_late)
```

#### 4.3.3 运力预测算法

**时间序列预测** (LSTM):
```
输入: 过去14天的订单量、天气、节假日、活动
输出: 未来24小时每小时的订单量预测

模型结构:
- LSTM层1: 128单元，return_sequences=True
- LSTM层2: 64单元
- Dense层: 24单元 (预测未来24小时)
```

### 4.4 定价算法

#### 4.4.1 动态定价算法

**高峰定价**:
```
price_multiplier = 1 + α × (demand/supply - 1)

约束:
- 1.0 ≤ price_multiplier ≤ 2.0
- 价格变动平滑，避免跳变

其中α = 0.5 (调节系数)
```

#### 4.4.2 智能满减推荐

**基于用户行为的满减推荐**:
```
推荐满减门槛 = 用户历史客单价 × 1.2
推荐满减金额 = 门槛 × 0.15 (15%优惠力度)
```

---

## 5. API接口规范

### 5.1 接口设计原则

1. **RESTful设计**: 资源导向，HTTP方法表示操作
2. **版本控制**: URL中包含版本号 (/v1/, /v2/)
3. **统一响应格式**: 标准JSON结构
4. **幂等性**: 敏感操作必须幂等
5. **限流保护**: 接口限流防止滥用

### 5.2 统一响应格式

```json
{
  "code": 200,
  "message": "success",
  "data": {},
  "timestamp": 1704067200000,
  "traceId": "a1b2c3d4e5f6"
}
```

**错误响应**:
```json
{
  "code": 40001,
  "message": "参数错误: 手机号格式不正确",
  "data": null,
  "timestamp": 1704067200000,
  "traceId": "a1b2c3d4e5f6"
}
```

### 5.3 接口列表

#### 5.3.1 用户服务 (user-service)

| 接口 | 方法 | 路径 | 描述 |
|------|------|------|------|
| 发送验证码 | POST | /v1/user/sms/send | 发送短信验证码 |
| 手机号登录 | POST | /v1/user/login/phone | 手机号+验证码登录 |
| 微信登录 | POST | /v1/user/login/wechat | 微信授权登录 |
| 获取用户信息 | GET | /v1/user/info | 获取当前用户信息 |
| 更新用户信息 | PUT | /v1/user/info | 更新用户信息 |
| 上传头像 | POST | /v1/user/avatar | 上传用户头像 |
| 添加收货地址 | POST | /v1/user/address | 添加收货地址 |
| 获取地址列表 | GET | /v1/user/address | 获取用户地址列表 |
| 更新地址 | PUT | /v1/user/address/{id} | 更新地址信息 |
| 删除地址 | DELETE | /v1/user/address/{id} | 删除地址 |
| 设置默认地址 | PUT | /v1/user/address/{id}/default | 设置默认地址 |

**请求/响应示例 - 手机号登录**:
```json
// 请求
{
  "phone": "13800138000",
  "smsCode": "123456",
  "deviceId": "device_001"
}

// 响应
{
  "code": 200,
  "message": "success",
  "data": {
    "accessToken": "eyJhbGciOiJIUzI1NiIs...",
    "refreshToken": "eyJhbGciOiJIUzI1NiIs...",
    "expiresIn": 7200,
    "userInfo": {
      "userId": "U123456789",
      "nickname": "用户13800138000",
      "avatar": "https://...",
      "phone": "13800138000",
      "identityType": 1
    }
  }
}
```

#### 5.3.2 商家服务 (merchant-service)

| 接口 | 方法 | 路径 | 描述 |
|------|------|------|------|
| 商家入驻申请 | POST | /v1/merchant/apply | 提交入驻申请 |
| 获取商家信息 | GET | /v1/merchant/info | 获取当前商家信息 |
| 更新商家信息 | PUT | /v1/merchant/info | 更新商家信息 |
| 上传资质 | POST | /v1/merchant/qualification | 上传资质文件 |
| 获取商家列表 | GET | /v1/merchants | 获取商家列表(用户端) |
| 获取商家详情 | GET | /v1/merchants/{id} | 获取商家详情 |
| 获取商家评价 | GET | /v1/merchants/{id}/reviews | 获取商家评价列表 |

#### 5.3.3 商品服务 (product-service)

| 接口 | 方法 | 路径 | 描述 |
|------|------|------|------|
| 创建商品 | POST | /v1/product | 创建商品 |
| 更新商品 | PUT | /v1/product/{id} | 更新商品信息 |
| 删除商品 | DELETE | /v1/product/{id} | 删除商品 |
| 商品上架 | PUT | /v1/product/{id}/onShelf | 商品上架 |
| 商品下架 | PUT | /v1/product/{id}/offShelf | 商品下架 |
| 获取商品列表 | GET | /v1/products | 获取商品列表 |
| 获取商品详情 | GET | /v1/products/{id} | 获取商品详情 |
| 搜索商品 | GET | /v1/products/search | 商品搜索 |
| 获取分类列表 | GET | /v1/categories | 获取商品分类 |

**请求参数 - 商品搜索**:
```
GET /v1/products/search?keyword=手机&categoryId=1001&sort=sales&order=desc&page=1&size=20&minPrice=1000&maxPrice=5000

参数说明:
- keyword: 搜索关键词
- categoryId: 分类ID
- sort: 排序字段 (sales/price/rating/new)
- order: 排序方式 (asc/desc)
- page: 页码 (默认1)
- size: 每页数量 (默认20, 最大100)
- minPrice: 最低价格
- maxPrice: 最高价格
- brandId: 品牌ID
- merchantId: 商家ID
```

#### 5.3.4 订单服务 (order-service)

| 接口 | 方法 | 路径 | 描述 |
|------|------|------|------|
| 创建订单 | POST | /v1/order | 创建订单 |
| 取消订单 | PUT | /v1/order/{id}/cancel | 取消订单 |
| 获取订单详情 | GET | /v1/order/{id} | 获取订单详情 |
| 获取订单列表 | GET | /v1/orders | 获取订单列表 |
| 确认收货 | PUT | /v1/order/{id}/confirm | 确认收货 |
| 申请售后 | POST | /v1/order/{id}/refund | 申请退款/退货 |
| 获取售后详情 | GET | /v1/order/{id}/refund | 获取售后详情 |

**请求/响应示例 - 创建订单**:
```json
// 请求
{
  "orderType": 1,
  "merchantId": "M123456",
  "addressId": "A123456",
  "remark": "请尽快送达",
  "deliveryTime": "2024-01-01 12:00:00",
  "couponId": "C123456",
  "items": [
    {
      "productId": "P123456",
      "skuId": "S123456",
      "quantity": 2
    }
  ]
}

// 响应
{
  "code": 200,
  "message": "success",
  "data": {
    "orderId": "O202401011234567890",
    "orderStatus": 10,
    "totalAmount": 199.00,
    "discountAmount": 20.00,
    "payAmount": 179.00,
    "createTime": "2024-01-01 10:00:00",
    "expireTime": "2024-01-01 10:15:00"
  }
}
```

#### 5.3.5 支付服务 (payment-service)

| 接口 | 方法 | 路径 | 描述 |
|------|------|------|------|
| 创建支付单 | POST | /v1/payment | 创建支付订单 |
| 查询支付状态 | GET | /v1/payment/{id} | 查询支付状态 |
| 申请退款 | POST | /v1/payment/refund | 申请退款 |
| 查询退款状态 | GET | /v1/payment/refund/{id} | 查询退款状态 |

**请求/响应示例 - 创建支付**:
```json
// 请求
{
  "orderId": "O202401011234567890",
  "payChannel": "WECHAT",
  "payType": "MINI_APP",
  "openId": "o123456789"
}

// 响应
{
  "code": 200,
  "message": "success",
  "data": {
    "paymentId": "P202401011234567890",
    "payParams": {
      "appId": "wx123456789",
      "timeStamp": "1704067200",
      "nonceStr": "random_string",
      "package": "prepay_id=wx123456",
      "signType": "RSA",
      "paySign": "signature"
    }
  }
}
```

#### 5.3.6 配送服务 (delivery-service)

| 接口 | 方法 | 路径 | 描述 |
|------|------|------|------|
| 创建配送单 | POST | /v1/delivery | 创建配送订单 |
| 获取配送状态 | GET | /v1/delivery/{id} | 获取配送状态 |
| 更新配送状态 | PUT | /v1/delivery/{id}/status | 更新配送状态 |
| 获取骑手位置 | GET | /v1/delivery/{id}/location | 获取骑手实时位置 |
| 分配骑手 | POST | /v1/delivery/{id}/assign | 分配骑手 |

### 5.4 接口安全规范

#### 5.4.1 认证方式

**JWT Token认证**:
```
Authorization: Bearer <access_token>
```

**Token刷新机制**:
- Access Token有效期: 2小时
- Refresh Token有效期: 7天
- Refresh Token只能用于刷新，不能用于接口访问

#### 5.4.2 请求签名

**敏感接口防重放攻击**:
```
签名算法: HMAC-SHA256

签名参数:
- timestamp: 时间戳(秒)
- nonce: 随机字符串(16位)
- appKey: 应用密钥

签名串: method + url + timestamp + nonce + body

Header:
X-Timestamp: 1704067200
X-Nonce: abcdef1234567890
X-Signature: sha256=计算结果
```

#### 5.4.3 限流规则

| 限流维度 | 限制 | 说明 |
|---------|------|------|
| 单IP | 100次/分钟 | 防止IP攻击 |
| 单用户 | 60次/分钟 | 防止用户滥用 |
| 单接口 | 1000次/分钟 | 保护核心接口 |
| 支付接口 | 10次/分钟 | 严格限制 |

---

## 6. 数据库设计

### 6.1 设计原则

1. **分库分表预留**: 订单表按用户ID分表，支持未来扩展
2. **字段冗余**: 适当冗余减少关联查询
3. **索引优化**: 所有查询条件字段建立索引
4. **软删除**: 使用deleted字段标记删除，不物理删除
5. **乐观锁**: 使用version字段防止并发更新

### 6.2 用户域

#### 6.2.1 用户表 (sys_user)

```sql
CREATE TABLE `sys_user` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` VARCHAR(32) NOT NULL COMMENT '用户ID',
  `phone` VARCHAR(20) NOT NULL COMMENT '手机号',
  `password` VARCHAR(128) DEFAULT NULL COMMENT '密码',
  `nickname` VARCHAR(64) DEFAULT NULL COMMENT '昵称',
  `avatar` VARCHAR(256) DEFAULT NULL COMMENT '头像URL',
  `gender` TINYINT DEFAULT 0 COMMENT '性别:0未知 1男 2女',
  `birthday` DATE DEFAULT NULL COMMENT '生日',
  `identity_type` TINYINT DEFAULT 1 COMMENT '身份类型:1普通用户 2商家 3企业',
  `status` TINYINT DEFAULT 1 COMMENT '状态:0禁用 1正常',
  `register_source` TINYINT DEFAULT 1 COMMENT '注册来源:1小程序 2APP 3H5',
  `register_time` DATETIME DEFAULT NULL COMMENT '注册时间',
  `last_login_time` DATETIME DEFAULT NULL COMMENT '最后登录时间',
  `deleted` TINYINT DEFAULT 0 COMMENT '是否删除:0否 1是',
  `version` INT DEFAULT 1 COMMENT '乐观锁版本号',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_id` (`user_id`),
  UNIQUE KEY `uk_phone` (`phone`),
  KEY `idx_identity_type` (`identity_type`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';
```

#### 6.2.2 用户地址表 (user_address)

```sql
CREATE TABLE `user_address` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `address_id` VARCHAR(32) NOT NULL COMMENT '地址ID',
  `user_id` VARCHAR(32) NOT NULL COMMENT '用户ID',
  `contact_name` VARCHAR(64) NOT NULL COMMENT '联系人姓名',
  `contact_phone` VARCHAR(20) NOT NULL COMMENT '联系人电话',
  `province` VARCHAR(32) NOT NULL COMMENT '省',
  `city` VARCHAR(32) NOT NULL COMMENT '市',
  `district` VARCHAR(32) NOT NULL COMMENT '区',
  `detail_address` VARCHAR(256) NOT NULL COMMENT '详细地址',
  `longitude` DECIMAL(10,7) DEFAULT NULL COMMENT '经度',
  `latitude` DECIMAL(10,7) DEFAULT NULL COMMENT '纬度',
  `is_default` TINYINT DEFAULT 0 COMMENT '是否默认:0否 1是',
  `address_type` TINYINT DEFAULT 1 COMMENT '地址类型:1家 2公司 3其他',
  `deleted` TINYINT DEFAULT 0,
  `version` INT DEFAULT 1,
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_address_id` (`address_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_location` (`longitude`, `latitude`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户地址表';
```

### 6.3 商家域

#### 6.3.1 商家表 (merchant)

```sql
CREATE TABLE `merchant` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `merchant_id` VARCHAR(32) NOT NULL COMMENT '商家ID',
  `  `user_id` VARCHAR(32) NOT NULL COMMENT '所属用户ID',
  `merchant_name` VARCHAR(128) NOT NULL COMMENT '商家名称',
  `merchant_type` TINYINT NOT NULL COMMENT '商家类型:1餐饮 2零售 3服务',
  `logo` VARCHAR(256) DEFAULT NULL COMMENT '商家Logo',
  `cover_image` VARCHAR(256) DEFAULT NULL COMMENT '封面图',
  `province` VARCHAR(32) DEFAULT NULL COMMENT '省',
  `city` VARCHAR(32) DEFAULT NULL COMMENT '市',
  `district` VARCHAR(32) DEFAULT NULL COMMENT '区',
  `address` VARCHAR(256) DEFAULT NULL COMMENT '详细地址',
  `longitude` DECIMAL(10,7) DEFAULT NULL COMMENT '经度',
  `latitude` DECIMAL(10,7) DEFAULT NULL COMMENT '纬度',
  `contact_name` VARCHAR(64) DEFAULT NULL COMMENT '联系人',
  `contact_phone` VARCHAR(20) DEFAULT NULL COMMENT '联系电话',
  `business_license` VARCHAR(128) DEFAULT NULL COMMENT '营业执照号',
  `business_license_image` VARCHAR(256) DEFAULT NULL COMMENT '营业执照图片',
  `food_license` VARCHAR(128) DEFAULT NULL COMMENT '食品经营许可证号',
  `food_license_image` VARCHAR(256) DEFAULT NULL COMMENT '食品经营许可证图片',
  `business_hours` VARCHAR(128) DEFAULT NULL COMMENT '营业时间 JSON格式',
  `delivery_type` TINYINT DEFAULT 1 COMMENT '配送方式:1自配送 2平台配送',
  `delivery_fee` DECIMAL(10,2) DEFAULT 0.00 COMMENT '配送费',
  `min_delivery_price` DECIMAL(10,2) DEFAULT 0.00 COMMENT '起送价',
  `avg_delivery_time` INT DEFAULT 30 COMMENT '平均配送时间(分钟)',
  `rating` DECIMAL(2,1) DEFAULT 5.0 COMMENT '评分',
  `rating_count` INT DEFAULT 0 COMMENT '评价数量',
  `month_sales` INT DEFAULT 0 COMMENT '月售数量',
  `audit_status` TINYINT DEFAULT 0 COMMENT '审核状态:0待审核 1通过 2拒绝',
  `audit_remark` VARCHAR(512) DEFAULT NULL COMMENT '审核备注',
  `status` TINYINT DEFAULT 0 COMMENT '状态:0待营业 1营业中 2休息中',
  `deleted` TINYINT DEFAULT 0,
  `version` INT DEFAULT 1,
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_merchant_id` (`merchant_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_merchant_type` (`merchant_type`),
  KEY `idx_location` (`longitude`, `latitude`),
  KEY `idx_audit_status` (`audit_status`),
  KEY `idx_status` (`status`),
  KEY `idx_rating` (`rating`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商家表';

-- 商家分类表
CREATE TABLE `merchant_category` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `category_id` VARCHAR(32) NOT NULL COMMENT '分类ID',
  `parent_id` VARCHAR(32) DEFAULT '0' COMMENT '父分类ID',
  `category_name` VARCHAR(64) NOT NULL COMMENT '分类名称',
  `icon` VARCHAR(256) DEFAULT NULL COMMENT '图标',
  `sort` INT DEFAULT 0 COMMENT '排序',
  `status` TINYINT DEFAULT 1 COMMENT '状态:0禁用 1启用',
  `deleted` TINYINT DEFAULT 0,
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_category_id` (`category_id`),
  KEY `idx_parent_id` (`parent_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商家分类表';

-- 商家与分类关联表
CREATE TABLE `merchant_category_relation` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `merchant_id` VARCHAR(32) NOT NULL COMMENT '商家ID',
  `category_id` VARCHAR(32) NOT NULL COMMENT '分类ID',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_merchant_category` (`merchant_id`, `category_id`),
  KEY `idx_category_id` (`category_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商家分类关联表';

### 6.4 商品域

-- 商品SPU表
CREATE TABLE `product_spu` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `spu_id` VARCHAR(32) NOT NULL COMMENT 'SPU ID',
  `merchant_id` VARCHAR(32) NOT NULL COMMENT '商家ID',
  `spu_name` VARCHAR(256) NOT NULL COMMENT '商品名称',
  `spu_subtitle` VARCHAR(512) DEFAULT NULL COMMENT '副标题',
  `category_id` VARCHAR(32) NOT NULL COMMENT '类目ID',
  `brand_id` VARCHAR(32) DEFAULT NULL COMMENT '品牌ID',
  `main_image` VARCHAR(256) NOT NULL COMMENT '主图',
  `sub_images` JSON DEFAULT NULL COMMENT '副图列表 JSON',
  `detail` LONGTEXT DEFAULT NULL COMMENT '商品详情 HTML',
  `unit` VARCHAR(32) DEFAULT '件' COMMENT '单位',
  `weight` DECIMAL(10,2) DEFAULT 0.00 COMMENT '重量(kg)',
  `origin` VARCHAR(128) DEFAULT NULL COMMENT '产地',
  `shelf_life` VARCHAR(64) DEFAULT NULL COMMENT '保质期',
  `storage_condition` VARCHAR(128) DEFAULT NULL COMMENT '储存条件',
  `sale_mode` TINYINT DEFAULT 1 COMMENT '销售模式:1现货 2预售',
  `spu_status` TINYINT DEFAULT 0 COMMENT 'SPU状态:0下架 1上架',
  `audit_status` TINYINT DEFAULT 0 COMMENT '审核状态:0待审核 1通过 2拒绝',
  `deleted` TINYINT DEFAULT 0,
  `version` INT DEFAULT 1,
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_spu_id` (`spu_id`),
  KEY `idx_merchant_id` (`merchant_id`),
  KEY `idx_category_id` (`category_id`),
  KEY `idx_brand_id` (`brand_id`),
  KEY `idx_spu_status` (`spu_status`),
  KEY `idx_audit_status` (`audit_status`),
  FULLTEXT KEY `ft_spu_name` (`spu_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品SPU表';

-- 商品SKU表
CREATE TABLE `product_sku` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `sku_id` VARCHAR(32) NOT NULL COMMENT 'SKU ID',
  `spu_id` VARCHAR(32) NOT NULL COMMENT 'SPU ID',
  `merchant_id` VARCHAR(32) NOT NULL COMMENT '商家ID',
  `sku_name` VARCHAR(256) NOT NULL COMMENT 'SKU名称',
  `sku_specs` JSON NOT NULL COMMENT '规格属性 JSON',
  `sku_image` VARCHAR(256) DEFAULT NULL COMMENT 'SKU图片',
  `price` DECIMAL(10,2) NOT NULL COMMENT '售价',
  `original_price` DECIMAL(10,2) DEFAULT NULL COMMENT '原价',
  `cost_price` DECIMAL(10,2) DEFAULT NULL COMMENT '成本价',
  `stock` INT NOT NULL DEFAULT 0 COMMENT '库存',
  `stock_warning` INT DEFAULT 10 COMMENT '库存预警值',
  `sales` INT DEFAULT 0 COMMENT '销量',
  `sku_status` TINYINT DEFAULT 1 COMMENT 'SKU状态:0禁用 1启用',
  `deleted` TINYINT DEFAULT 0,
  `version` INT DEFAULT 1,
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_sku_id` (`sku_id`),
  KEY `idx_spu_id` (`spu_id`),
  KEY `idx_merchant_id` (`merchant_id`),
  KEY `idx_price` (`price`),
  KEY `idx_sku_status` (`sku_status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品SKU表';

-- 商品类目表
CREATE TABLE `product_category` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `category_id` VARCHAR(32) NOT NULL COMMENT '类目ID',
  `parent_id` VARCHAR(32) DEFAULT '0' COMMENT '父类目ID',
  `category_name` VARCHAR(64) NOT NULL COMMENT '类目名称',
  `category_level` TINYINT NOT NULL COMMENT '类目级别:1一级 2二级 3三级',
  `icon` VARCHAR(256) DEFAULT NULL COMMENT '图标',
  `image` VARCHAR(256) DEFAULT NULL COMMENT '图片',
  `sort` INT DEFAULT 0 COMMENT '排序',
  `is_show` TINYINT DEFAULT 1 COMMENT '是否显示:0否 1是',
  `status` TINYINT DEFAULT 1 COMMENT '状态:0禁用 1启用',
  `deleted` TINYINT DEFAULT 0,
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_category_id` (`category_id`),
  KEY `idx_parent_id` (`parent_id`),
  KEY `idx_category_level` (`category_level`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品类目表';

-- 购物车表
CREATE TABLE `shopping_cart` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `cart_id` VARCHAR(32) NOT NULL COMMENT '购物车ID',
  `user_id` VARCHAR(32) NOT NULL COMMENT '用户ID',
  `merchant_id` VARCHAR(32) NOT NULL COMMENT '商家ID',
  `sku_id` VARCHAR(32) NOT NULL COMMENT 'SKU ID',
  `spu_id` VARCHAR(32) NOT NULL COMMENT 'SPU ID',
  `quantity` INT NOT NULL DEFAULT 1 COMMENT '数量',
  `selected` TINYINT DEFAULT 1 COMMENT '是否选中:0否 1是',
  `deleted` TINYINT DEFAULT 0,
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_sku` (`user_id`, `sku_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_merchant_id` (`merchant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='购物车表';

### 6.5 订单域

-- 订单主表
CREATE TABLE `order_main` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `order_id` VARCHAR(32) NOT NULL COMMENT '订单ID',
  `order_no` VARCHAR(64) NOT NULL COMMENT '订单编号',
  `parent_order_id` VARCHAR(32) DEFAULT NULL COMMENT '父订单ID(拆单用)',
  `user_id` VARCHAR(32) NOT NULL COMMENT '用户ID',
  `merchant_id` VARCHAR(32) NOT NULL COMMENT '商家ID',
  `order_type` TINYINT NOT NULL COMMENT '订单类型:1外卖 2商城 3直采 4培训',
  `order_status` TINYINT NOT NULL DEFAULT 10 COMMENT '订单状态:10待支付 20已支付 30待接单 40待发货 50配送中 60待收货 70已完成 80售后中 99已取消',
  `pay_status` TINYINT NOT NULL DEFAULT 10 COMMENT '支付状态:10待支付 20已支付 30支付失败 40退款中 50已退款',
  `delivery_status` TINYINT DEFAULT 0 COMMENT '配送状态:0未配送 1待取货 2配送中 3已送达',
  `total_amount` DECIMAL(12,2) NOT NULL COMMENT '订单总金额',
  `discount_amount` DECIMAL(12,2) DEFAULT 0.00 COMMENT '优惠金额',
  `delivery_fee` DECIMAL(10,2) DEFAULT 0.00 COMMENT '配送费',
  `packing_fee` DECIMAL(10,2) DEFAULT 0.00 COMMENT '包装费',
  `pay_amount` DECIMAL(12,2) NOT NULL COMMENT '应付金额',
  `coupon_id` VARCHAR(32) DEFAULT NULL COMMENT '优惠券ID',
  `coupon_amount` DECIMAL(10,2) DEFAULT 0.00 COMMENT '优惠券抵扣金额',
  `remark` VARCHAR(512) DEFAULT NULL COMMENT '订单备注',
  `delivery_type` TINYINT DEFAULT 1 COMMENT '配送方式:1即时 2预约',
  `delivery_time` DATETIME DEFAULT NULL COMMENT '预约配送时间',
  `receive_name` VARCHAR(64) NOT NULL COMMENT '收货人姓名',
  `receive_phone` VARCHAR(20) NOT NULL COMMENT '收货人电话',
  `receive_address` VARCHAR(512) NOT NULL COMMENT '收货地址',
  `receive_longitude` DECIMAL(10,7) DEFAULT NULL COMMENT '收货地址经度',
  `receive_latitude` DECIMAL(10,7) DEFAULT NULL COMMENT '收货地址纬度',
  `pay_time` DATETIME DEFAULT NULL COMMENT '支付时间',
  `send_time` DATETIME DEFAULT NULL COMMENT '发货时间',
  `receive_time` DATETIME DEFAULT NULL COMMENT '收货时间',
  `complete_time` DATETIME DEFAULT NULL COMMENT '完成时间',
  `cancel_time` DATETIME DEFAULT NULL COMMENT '取消时间',
  `cancel_reason` VARCHAR(256) DEFAULT NULL COMMENT '取消原因',
  `auto_confirm_time` DATETIME DEFAULT NULL COMMENT '自动确认收货时间',
  `deleted` TINYINT DEFAULT 0,
  `version` INT DEFAULT 1,
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_order_id` (`order_id`),
  UNIQUE KEY `uk_order_no` (`order_no`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_merchant_id` (`merchant_id`),
  KEY `idx_order_type` (`order_type`),
  KEY `idx_order_status` (`order_status`),
  KEY `idx_pay_status` (`pay_status`),
  KEY `idx_create_time` (`create_time`),
  KEY `idx_user_status` (`user_id`, `order_status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单主表';

-- 订单明细表
CREATE TABLE `order_item` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `item_id` VARCHAR(32) NOT NULL COMMENT '明细ID',
  `order_id` VARCHAR(32) NOT NULL COMMENT '订单ID',
  `spu_id` VARCHAR(32) NOT NULL COMMENT 'SPU ID',
  `sku_id` VARCHAR(32) NOT NULL COMMENT 'SKU ID',
  `merchant_id` VARCHAR(32) NOT NULL COMMENT '商家ID',
  `spu_name` VARCHAR(256) NOT NULL COMMENT '商品名称',
  `sku_specs` JSON DEFAULT NULL COMMENT '规格信息 JSON',
  `sku_image` VARCHAR(256) DEFAULT NULL COMMENT 'SKU图片',
  `unit_price` DECIMAL(10,2) NOT NULL COMMENT '单价',
  `quantity` INT NOT NULL COMMENT '数量',
  `total_price` DECIMAL(12,2) NOT NULL COMMENT '小计金额',
  `discount_price` DECIMAL(10,2) DEFAULT 0.00 COMMENT '优惠金额',
  `actual_price` DECIMAL(10,2) NOT NULL COMMENT '实付金额',
  `deleted` TINYINT DEFAULT 0,
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_item_id` (`item_id`),
  KEY `idx_order_id` (`order_id`),
  KEY `idx_sku_id` (`sku_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单明细表';

-- 订单状态流水表
CREATE TABLE `order_status_log` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `log_id` VARCHAR(32) NOT NULL COMMENT '流水ID',
  `order_id` VARCHAR(32) NOT NULL COMMENT '订单ID',
  `from_status` TINYINT NOT NULL COMMENT '原状态',
  `to_status` TINYINT NOT NULL COMMENT '新状态',
  `operator_type` TINYINT DEFAULT 1 COMMENT '操作者类型:1用户 2商家 3系统 4骑手',
  `operator_id` VARCHAR(32) DEFAULT NULL COMMENT '操作者ID',
  `operator_name` VARCHAR(64) DEFAULT NULL COMMENT '操作者名称',
  `remark` VARCHAR(512) DEFAULT NULL COMMENT '备注',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_log_id` (`log_id`),
  KEY `idx_order_id` (`order_id`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单状态流水表';

-- 售后申请表
CREATE TABLE `order_refund` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `refund_id` VARCHAR(32) NOT NULL COMMENT '售后ID',
  `order_id` VARCHAR(32) NOT NULL COMMENT '订单ID',
  `item_id` VARCHAR(32) DEFAULT NULL COMMENT '订单明细ID(整单退款为空)',
  `user_id` VARCHAR(32) NOT NULL COMMENT '用户ID',
  `merchant_id` VARCHAR(32) NOT NULL COMMENT '商家ID',
  `refund_type` TINYINT NOT NULL COMMENT '退款类型:1仅退款 2退货退款',
  `refund_status` TINYINT NOT NULL DEFAULT 10 COMMENT '退款状态:10待审核 20待退货 30待收货 40退款中 50退款成功 60拒绝退款 70取消退款',
  `refund_reason` TINYINT NOT NULL COMMENT '退款原因:1商品质量问题 2商品与描述不符 3未按约定时间发货 4其他',
  `refund_reason_text` VARCHAR(512) DEFAULT NULL COMMENT '退款说明',
  `refund_images` JSON DEFAULT NULL COMMENT '退款凭证图片',
  `refund_amount` DECIMAL(12,2) NOT NULL COMMENT '退款金额',
  `apply_time` DATETIME NOT NULL COMMENT '申请时间',
  `audit_time` DATETIME DEFAULT NULL COMMENT '审核时间',
  `audit_remark` VARCHAR(512) DEFAULT NULL COMMENT '审核备注',
  `delivery_company` VARCHAR(64) DEFAULT NULL COMMENT '退货物流公司',
  `delivery_no` VARCHAR(64) DEFAULT NULL COMMENT '退货物流单号',
  `delivery_time` DATETIME DEFAULT NULL COMMENT '退货发货时间',
  `receive_time` DATETIME DEFAULT NULL COMMENT '退货收货时间',
  `refund_time` DATETIME DEFAULT NULL COMMENT '退款时间',
  `payment_id` VARCHAR(32) DEFAULT NULL COMMENT '支付单ID',
  `deleted` TINYINT DEFAULT 0,
  `version` INT DEFAULT 1,
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_refund_id` (`refund_id`),
  KEY `idx_order_id` (`order_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_merchant_id` (`merchant_id`),
  KEY `idx_refund_status` (`refund_status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='售后申请表';

### 6.6 支付域

-- 支付单表
CREATE TABLE `payment_order` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `payment_id` VARCHAR(32) NOT NULL COMMENT '支付单ID',
  `biz_order_id` VARCHAR(32) NOT NULL COMMENT '业务订单ID',
  `user_id` VARCHAR(32) NOT NULL COMMENT '用户ID',
  `payment_type` TINYINT NOT NULL COMMENT '支付类型:1订单支付 2充值 3退款',
  `pay_channel` VARCHAR(32) NOT NULL COMMENT '支付渠道:WECHAT ALIPAY',
  `pay_amount` DECIMAL(12,2) NOT NULL COMMENT '支付金额',
  `pay_status` TINYINT NOT NULL DEFAULT 10 COMMENT '支付状态:10待支付 20支付成功 30支付失败 40已关闭',
  `pay_time` DATETIME DEFAULT NULL COMMENT '支付时间',
  `third_party_no` VARCHAR(128) DEFAULT NULL COMMENT '第三方支付单号',
  `third_party_resp` JSON DEFAULT NULL COMMENT '第三方支付响应',
  `expire_time` DATETIME NOT NULL COMMENT '过期时间',
  `client_ip` VARCHAR(64) DEFAULT NULL COMMENT '客户端IP',
  `device_type` VARCHAR(32) DEFAULT NULL COMMENT '设备类型',
  `deleted` TINYINT DEFAULT 0,
  `version` INT DEFAULT 1,
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_payment_id` (`payment_id`),
  KEY `idx_biz_order_id` (`biz_order_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_pay_status` (`pay_status`),
  KEY `idx_third_party_no` (`third_party_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='支付单表';

-- 退款单表
CREATE TABLE `refund_order` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `refund_order_id` VARCHAR(32) NOT NULL COMMENT '退款单ID',
  `payment_id` VARCHAR(32) NOT NULL COMMENT '支付单ID',
  `biz_order_id` VARCHAR(32) NOT NULL COMMENT '业务订单ID',
  `refund_amount` DECIMAL(12,2) NOT NULL COMMENT '退款金额',
  `refund_status` TINYINT NOT NULL DEFAULT 10 COMMENT '退款状态:10待退款 20退款中 30退款成功 40退款失败',
  `refund_time` DATETIME DEFAULT NULL COMMENT '退款时间',
  `third_party_refund_no` VARCHAR(128) DEFAULT NULL COMMENT '第三方退款单号',
  `refund_reason` VARCHAR(512) DEFAULT NULL COMMENT '退款原因',
  `deleted` TINYINT DEFAULT 0,
  `version` INT DEFAULT 1,
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_refund_order_id` (`refund_order_id`),
  KEY `idx_payment_id` (`payment_id`),
  KEY `idx_biz_order_id` (`biz_order_id`),
  KEY `idx_refund_status` (`refund_status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='退款单表';

### 6.7 配送域

-- 配送单表
CREATE TABLE `delivery_order` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `delivery_id` VARCHAR(32) NOT NULL COMMENT '配送单ID',
  `order_id` VARCHAR(32) NOT NULL COMMENT '订单ID',
  `merchant_id` VARCHAR(32) NOT NULL COMMENT '商家ID',
  `rider_id` VARCHAR(32) DEFAULT NULL COMMENT '骑手ID',
  `delivery_status` TINYINT NOT NULL DEFAULT 10 COMMENT '配送状态:10待分配 20待取货 30配送中 40已送达 50异常',
  `delivery_type` TINYINT DEFAULT 1 COMMENT '配送类型:1平台配送 2商家自配',
  `pickup_address` VARCHAR(512) NOT NULL COMMENT '取货地址',
  `pickup_longitude` DECIMAL(10,7) NOT NULL COMMENT '取货经度',
  `pickup_latitude` DECIMAL(10,7) NOT NULL COMMENT '取货纬度',
  `delivery_address` VARCHAR(512) NOT NULL COMMENT '送货地址',
  `delivery_longitude` DECIMAL(10,7) NOT NULL COMMENT '送货经度',
  `delivery_latitude` DECIMAL(10,7) NOT NULL COMMENT '送货纬度',
  `distance` DECIMAL(8,2) DEFAULT NULL COMMENT '配送距离(公里)',
  `estimated_time` INT DEFAULT NULL COMMENT '预计配送时间(分钟)',
  `actual_time` INT DEFAULT NULL COMMENT '实际配送时间(分钟)',
  `pickup_time` DATETIME DEFAULT NULL COMMENT '取货时间',
  `delivery_time` DATETIME DEFAULT NULL COMMENT '送达时间',
  `delivery_fee` DECIMAL(10,2) DEFAULT 0.00 COMMENT '配送费',
  `rider_fee` DECIMAL(10,2) DEFAULT 0.00 COMMENT '骑手收入',
  `contact_name` VARCHAR(64) DEFAULT NULL COMMENT '联系人姓名',
  `contact_phone` VARCHAR(20) DEFAULT NULL COMMENT '联系人电话',
  `remark` VARCHAR(512) DEFAULT NULL COMMENT '配送备注',
  `deleted` TINYINT DEFAULT 0,
  `version` INT DEFAULT 1,
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_delivery_id` (`delivery_id`),
  UNIQUE KEY `uk_order_id` (`order_id`),
  KEY `idx_rider_id` (`rider_id`),
  KEY `idx_merchant_id` (`merchant_id`),
  KEY `idx_delivery_status` (`delivery_status`),
  KEY `idx_pickup_location` (`pickup_longitude`, `pickup_latitude`),
  KEY `idx_delivery_location` (`delivery_longitude`, `delivery_latitude`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='配送单表';

-- 骑手表
CREATE TABLE `delivery_rider` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `rider_id` VARCHAR(32) NOT NULL COMMENT '骑手ID',
  `user_id` VARCHAR(32) NOT NULL COMMENT '用户ID',
  `real_name` VARCHAR(64) NOT NULL COMMENT '真实姓名',
  `id_card` VARCHAR(32) NOT NULL COMMENT '身份证号',
  `phone` VARCHAR(20) NOT NULL COMMENT '手机号',
  `avatar` VARCHAR(256) DEFAULT NULL COMMENT '头像',
  `work_city` VARCHAR(64) NOT NULL COMMENT '工作城市',
  `work_district` VARCHAR(64) DEFAULT NULL COMMENT '工作区域',
  `id_card_front` VARCHAR(256) NOT NULL COMMENT '身份证正面',
  `id_card_back` VARCHAR(256) NOT NULL COMMENT '身份证反面',
  `health_certificate` VARCHAR(256) DEFAULT NULL COMMENT '健康证',
  `audit_status` TINYINT DEFAULT 0 COMMENT '审核状态:0待审核 1通过 2拒绝',
  `work_status` TINYINT DEFAULT 0 COMMENT '工作状态:0休息 1接单中',
  `total_orders` INT DEFAULT 0 COMMENT '累计接单数',
  `rating` DECIMAL(2,1) DEFAULT 5.0 COMMENT '评分',
  `balance` DECIMAL(12,2) DEFAULT 0.00 COMMENT '账户余额',
  `deleted` TINYINT DEFAULT 0,
  `version` INT DEFAULT 1,
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_rider_id` (`rider_id`),
  UNIQUE KEY `uk_user_id` (`user_id`),
  KEY `idx_work_status` (`work_status`),
  KEY `idx_audit_status` (`audit_status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='骑手表';

### 6.8 营销域

-- 优惠券表
CREATE TABLE `marketing_coupon` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `coupon_id` VARCHAR(32) NOT NULL COMMENT '优惠券ID',
  `coupon_name` VARCHAR(128) NOT NULL COMMENT '优惠券名称',
  `coupon_type` TINYINT NOT NULL COMMENT '优惠券类型:1满减券 2折扣券 3无门槛券',
  `coupon_scope` TINYINT NOT NULL COMMENT '使用范围:1全平台 2指定商家 3指定分类',
  `face_value` DECIMAL(10,2) NOT NULL COMMENT '面值',
  `min_order_amount` DECIMAL(10,2) DEFAULT 0.00 COMMENT '最低订单金额',
  `discount_rate` DECIMAL(3,2) DEFAULT NULL COMMENT '折扣率(折扣券用)',
  `max_discount` DECIMAL(10,2) DEFAULT NULL COMMENT '最大优惠金额',
  `total_count` INT NOT NULL COMMENT '发放总量',
  `received_count` INT DEFAULT 0 COMMENT '已领取数量',
  `used_count` INT DEFAULT 0 COMMENT '已使用数量',
  `start_time` DATETIME NOT NULL COMMENT '开始时间',
  `end_time` DATETIME NOT NULL COMMENT '结束时间',
  `valid_days` INT DEFAULT NULL COMMENT '领取后有效天数',
  `status` TINYINT DEFAULT 1 COMMENT '状态:0禁用 1启用',
  `deleted` TINYINT DEFAULT 0,
  `version` INT DEFAULT 1,
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_coupon_id` (`coupon_id`),
  KEY `idx_coupon_type` (`coupon_type`),
  KEY `idx_status` (`status`),
  KEY `idx_time` (`start_time`, `end_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='优惠券表';

-- 用户优惠券表
CREATE TABLE `user_coupon` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `user_coupon_id` VARCHAR(32) NOT NULL COMMENT '用户优惠券ID',
  `user_id` VARCHAR(32) NOT NULL COMMENT '用户ID',
  `coupon_id` VARCHAR(32) NOT NULL COMMENT '优惠券ID',
  `status` TINYINT DEFAULT 1 COMMENT '状态:1未使用 2已使用 3已过期',
  `order_id` VARCHAR(32) DEFAULT NULL COMMENT '使用的订单ID',
  `use_time` DATETIME DEFAULT NULL COMMENT '使用时间',
  `receive_time` DATETIME NOT NULL COMMENT '领取时间',
  `expire_time` DATETIME NOT NULL COMMENT '过期时间',
  `deleted` TINYINT DEFAULT 0,
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_coupon_id` (`user_coupon_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_coupon_id` (`coupon_id`),
  KEY `idx_status` (`status`),
  KEY `idx_expire_time` (`expire_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户优惠券表';

## 7. 测试用例

### 7.1 功能测试用例

#### 7.1.1 用户模块测试用例

| 用例ID | 用例名称 | 前置条件 | 测试步骤 | 预期结果 | 优先级 |
|--------|---------|---------|---------|---------|--------|
| TC-USER-001 | 手机号注册 | 手机号未注册 | 1. 输入手机号<br>2. 获取验证码<br>3. 输入验证码<br>4. 点击注册 | 注册成功，跳转首页 | P0 |
| TC-USER-002 | 重复手机号注册 | 手机号已注册 | 1. 输入已注册手机号<br>2. 获取验证码 | 提示"手机号已注册" | P0 |
| TC-USER-003 | 验证码过期 | 验证码已发送超过60秒 | 1. 输入手机号<br>2. 等待60秒<br>3. 输入原验证码 | 提示"验证码已过期" | P0 |
| TC-USER-004 | 手机号登录 | 手机号已注册 | 1. 输入手机号<br>2. 获取验证码<br>3. 输入验证码<br>4. 点击登录 | 登录成功，返回用户信息 | P0 |
| TC-USER-005 | 添加收货地址 | 用户已登录 | 1. 进入地址管理<br>2. 点击添加地址<br>3. 填写地址信息<br>4. 保存 | 地址添加成功 | P0 |
| TC-USER-006 | 设置默认地址 | 已有多个地址 | 1. 进入地址列表<br>2. 选择非默认地址<br>3. 设为默认 | 默认地址更新成功 | P1 |

#### 7.1.2 订单模块测试用例

| 用例ID | 用例名称 | 前置条件 | 测试步骤 | 预期结果 | 优先级 |
|--------|---------|---------|---------|---------|--------|
| TC-ORDER-001 | 创建外卖订单 | 用户已登录，已选商品 | 1. 进入购物车<br>2. 选择商品<br>3. 选择地址<br>4. 提交订单 | 订单创建成功，状态"待支付" | P0 |
| TC-ORDER-002 | 订单支付 | 有待支付订单 | 1. 进入订单详情<br>2. 选择支付方式<br>3. 确认支付 | 支付成功，状态"已支付" | P0 |
| TC-ORDER-003 | 取消订单 | 有待支付订单 | 1. 进入订单详情<br>2. 点击取消<br>3. 选择原因<br>4. 确认 | 订单取消成功 | P0 |
| TC-ORDER-004 | 订单超时自动取消 | 待支付订单超过15分钟 | 等待15分钟 | 订单自动取消 | P0 |
| TC-ORDER-005 | 确认收货 | 订单状态"配送中" | 1. 进入订单详情<br>2. 点击确认收货 | 订单状态变为"已完成" | P0 |
| TC-ORDER-006 | 申请退款 | 订单状态"已完成" | 1. 进入订单详情<br>2. 点击申请售后<br>3. 选择退款原因<br>4. 提交 | 售后申请提交成功 | P0 |
| TC-ORDER-007 | 商家接单 | 有新订单 | 1. 商家端收到通知<br>2. 查看订单详情<br>3. 点击接单 | 订单状态变为"待发货" | P0 |
| TC-ORDER-008 | 商家拒单 | 有新订单 | 1. 商家端收到通知<br>2. 查看订单详情<br>3. 点击拒单<br>4. 选择原因 | 订单取消，退款给用户 | P0 |

#### 7.1.3 支付模块测试用例

| 用例ID | 用例名称 | 前置条件 | 测试步骤 | 预期结果 | 优先级 |
|--------|---------|---------|---------|---------|--------|
| TC-PAY-001 | 微信支付 | 有待支付订单 | 1. 选择微信支付<br>2. 调起微信支付<br>3. 完成支付 | 支付成功，订单状态更新 | P0 |
| TC-PAY-002 | 支付超时 | 调起支付后超过15分钟 | 等待15分钟不支付 | 支付单关闭，订单取消 | P0 |
| TC-PAY-003 | 支付回调处理 | 已完成支付 | 模拟支付回调 | 订单状态正确更新 | P0 |
| TC-PAY-004 | 重复支付 | 订单已支付 | 再次调起支付 | 提示"订单已支付" | P0 |
| TC-PAY-005 | 部分退款 | 订单已完成 | 1. 申请部分退款<br>2. 商家审核通过 | 退款成功，金额正确 | P0 |
| TC-PAY-006 | 全额退款 | 订单已完成 | 1. 申请全额退款<br>2. 商家审核通过 | 退款成功，金额正确 | P0 |

#### 7.1.4 配送模块测试用例

| 用例ID | 用例名称 | 前置条件 | 测试步骤 | 预期结果 | 优先级 |
|--------|---------|---------|---------|---------|--------|
| TC-DELIVERY-001 | 骑手接单 | 有新配送单 | 1. 骑手端查看订单<br>2. 点击接单 | 配送单分配给骑手 | P0 |
| TC-DELIVERY-002 | 骑手取货 | 骑手已接单 | 1. 到达商家<br>2. 确认取货 | 配送状态"配送中" | P0 |
| TC-DELIVERY-003 | 骑手送达 | 配送中 | 1. 到达用户地址<br>2. 确认送达 | 配送状态"已送达" | P0 |
| TC-DELIVERY-004 | 转单 | 骑手已接单 | 1. 点击转单<br>2. 选择原因 | 配送单回到订单池 | P1 |
| TC-DELIVERY-005 | 异常上报 | 配送中遇到问题 | 1. 点击异常<br>2. 选择异常类型<br>3. 提交 | 异常记录成功 | P1 |

### 7.2 性能测试用例

| 用例ID | 用例名称 | 测试场景 | 性能指标 | 目标值 |
|--------|---------|---------|---------|--------|
| TC-PERF-001 | 首页加载性能 | 1000并发用户访问首页 | 平均响应时间 | < 500ms |
| TC-PERF-002 | 商品搜索性能 | 1000并发用户搜索商品 | 平均响应时间 | < 800ms |
| TC-PERF-003 | 订单创建性能 | 500并发用户同时下单 | TPS | > 100 |
| TC-PERF-004 | 支付接口性能 | 500并发用户同时支付 | TPS | > 50 |
| TC-PERF-005 | 数据库查询性能 | 商品列表查询 | 平均响应时间 | < 200ms |
| TC-PERF-006 | 缓存命中率 | 商品详情访问 | 缓存命中率 | > 80% |

### 7.3 安全测试用例

| 用例ID | 用例名称 | 测试场景 | 预期结果 |
|--------|---------|---------|---------|
| TC-SEC-001 | SQL注入测试 | 在搜索框输入SQL注入语句 | 系统正常处理，无SQL注入风险 |
| TC-SEC-002 | XSS攻击测试 | 在评论中输入XSS脚本 | 脚本被转义，不执行 |
| TC-SEC-003 | 越权访问测试 | 用户A访问用户B的订单 | 返回无权限错误 |
| TC-SEC-004 | 暴力破解测试 | 连续错误登录10次 | 账户锁定30分钟 |
| TC-SEC-005 | 敏感信息泄露 | 查看接口返回数据 | 密码等敏感信息已脱敏 |
| TC-SEC-006 | 支付签名验证 | 篡改支付参数 | 支付失败，签名验证不通过 |

### 7.4 兼容性测试用例

| 用例ID | 用例名称 | 测试环境 | 测试内容 |
|--------|---------|---------|---------|
| TC-COMP-001 | iOS兼容性 | iOS 12/14/16 | 小程序功能正常 |
| TC-COMP-002 | Android兼容性 | Android 8/10/12 | 小程序功能正常 |
| TC-COMP-003 | 微信版本兼容 | 基础库2.19/2.30/最新 | 功能正常 |
| TC-COMP-004 | 屏幕适配 | iPhone SE/12/14 Pro Max | 界面显示正常 |
| TC-COMP-005 | 浏览器兼容 | Chrome/Firefox/Safari | H5页面正常显示 |

---

## 8. 技术架构

### 8.1 微服务架构设计

```
┌─────────────────────────────────────────────────────────────────┐
│                         接入层 (Access Layer)                    │
│  ┌─────────────┐ ┌─────────────┐ ┌─────────────┐               │
│  │   微信小程序 │ │   H5页面    │ │   管理后台   │               │
│  └──────┬──────┘ └──────┬──────┘ └──────┬──────┘               │
└─────────┼───────────────┼───────────────┼──────────────────────┘
          │               │               │
┌─────────┼───────────────┼───────────────┼──────────────────────┐
│         ▼               ▼               ▼                      │
│  ┌─────────────────────────────────────────────────────────┐   │
│  │                    API Gateway                          │   │
│  │  • 统一认证  • 限流熔断  • 路由转发  • 协议转换          │   │
│  └─────────────────────────────────────────────────────────┘   │
│                              │                                  │
│  ┌───────────────────────────┼───────────────────────────┐     │
│  │                           ▼                           │     │
│  │  ┌───────────────────────────────────────────────┐   │     │
│  │  │              业务服务层 (Service Layer)          │   │     │
│  │  ├─────────────┬─────────────┬───────────────────┤   │     │
│  │  │ user-svc    │ merchant-svc│   product-svc     │   │     │
│  │  ├─────────────┼─────────────┼───────────────────┤   │     │
│  │  │ order-svc   │ payment-svc │   delivery-svc    │   │     │
│  │  ├─────────────┼─────────────┼───────────────────┤   │     │
│  │  │ coupon-svc  │ message-svc │   search-svc      │   │     │
│  │  └─────────────┴─────────────┴───────────────────┘   │     │
│  └───────────────────────────────────────────────────────┘     │
│                              │                                  │
│  ┌───────────────────────────┼───────────────────────────┐     │
│  │                           ▼                           │     │
│  │  ┌───────────────────────────────────────────────┐   │     │
│  │  │              基础设施层 (Infra Layer)            │   │     │
│  │  ├─────────────┬─────────────┬───────────────────┤   │     │
│  │  │   MySQL     │    Redis    │ Elasticsearch     │   │     │
│  │  ├─────────────┼─────────────┼───────────────────┤   │     │
│  │  │  RocketMQ   │   MinIO     │  XXL-Job          │   │     │
│  │  └─────────────┴─────────────┴───────────────────┘   │     │
│  └───────────────────────────────────────────────────────┘     │
└─────────────────────────────────────────────────────────────────┘
```

### 8.2 高并发处理方案

#### 8.2.1 缓存策略

**多级缓存架构**:
```
用户请求 → CDN缓存 → Nginx本地缓存 → Redis分布式缓存 → 数据库
```

**缓存更新策略**:
- **读策略**: 先读缓存，缓存未命中读数据库并写入缓存
- **写策略**: 先写数据库，再删缓存（Cache-Aside模式）
- **缓存预热**: 系统启动时预加载热点数据
- **缓存过期**: 设置合理过期时间，避免缓存雪崩

**热点数据缓存**:
| 数据类型 | 缓存Key | 过期时间 | 说明 |
|---------|---------|---------|------|
| 商品信息 | product:{skuId} | 30分钟 | 商品详情 |
| 商家信息 | merchant:{merchantId} | 60分钟 | 商家信息 |
| 用户信息 | user:{userId} | 60分钟 | 用户信息 |
| 购物车 | cart:{userId} | 永久 | 购物车数据 |
| 订单状态 | order:{orderId} | 10分钟 | 订单状态 |
| 首页数据 | home:{city} | 5分钟 | 首页推荐 |

#### 8.2.2 数据库优化

**读写分离**:
- 主库: 写操作
- 从库: 读操作
- 延迟容忍: 读从库允许秒级延迟

**分库分表**:
- 订单表: 按userId取模分16张表
- 支付表: 按paymentId取模分8张表
- 用户表: 按userId取模分8张表

**索引优化**:
- 所有外键字段建立索引
- 查询条件字段建立联合索引
- 排序字段建立索引
- 定期分析慢查询并优化

#### 8.2.3 异步处理

**消息队列使用场景**:
| 场景 | Topic | 说明 |
|------|-------|------|
| 订单创建 | order_create | 订单创建后异步处理 |
| 支付成功 | payment_success | 支付成功后异步通知 |
| 订单发货 | order_send | 发货后异步通知 |
| 退款处理 | refund_process | 退款异步处理 |
| 短信通知 | sms_send | 短信异步发送 |
| 推送通知 | push_notification | 推送异步发送 |

### 8.3 支付系统安全规范

#### 8.3.1 支付安全设计

**幂等性保证**:
- 支付单号全局唯一
- 同一业务订单只能创建一个待支付单
- 支付回调幂等处理

**防重复支付**:
- 订单支付状态检查
- 支付单状态机控制
- Redis分布式锁

**金额一致性**:
- 支付金额与订单金额校验
- 优惠券金额校验
- 退款金额校验

#### 8.3.2 敏感信息保护

**数据加密**:
- 银行卡号: AES加密存储
- 身份证号: AES加密存储
- 手机号: 数据库加密，展示脱敏

**传输安全**:
- HTTPS全站加密
- 敏感接口请求签名
- 支付回调IP白名单

---

## 9. 非功能需求

### 9.1 性能需求

| 指标 | 目标值 | 说明 |
|------|--------|------|
| 首页加载 | < 2s | 首屏渲染完成 |
| 接口响应 | < 500ms | P95响应时间 |
| 并发支持 | 1000 QPS | 一期目标 |
| 数据库连接 | < 100 | 单服务连接池 |
| 订单创建 | < 300ms | 单笔订单创建时间 |
| 支付回调 | < 1s | 支付回调处理时间 |
| 搜索响应 | < 800ms | 商品搜索响应时间 |
| 图片加载 | < 1s | 商品图片加载时间 |

### 9.2 安全需求

| 安全维度 | 具体要求 |
|---------|---------|
| **登录安全** | 1. 短信验证码防刷（同一手机号60秒内只能发送1次，24小时内最多10次）<br>2. 密码登录错误次数限制（连续5次错误锁定30分钟）<br>3. 异常IP登录检测（异地登录提醒）<br>4. Token有效期控制（Access Token 2小时，Refresh Token 7天） |
| **数据安全** | 1. 敏感字段加密存储（手机号、身份证号AES加密）<br>2. 数据库连接加密（SSL/TLS）<br>3. 数据脱敏展示（手机号显示为138****8888）<br>4. 数据访问权限控制（行级、列级权限） |
| **传输安全** | 1. HTTPS全站加密（TLS 1.2+）<br>2. 敏感接口防重放攻击（请求签名+时间戳）<br>3. CORS跨域限制（白名单机制）<br>4. 接口限流（单IP 100次/分钟，单用户 60次/分钟） |
| **权限安全** | 1. RBAC权限模型（角色-权限-用户）<br>2. 数据范围控制（只能查看归属数据）<br>3. 敏感操作二次确认（删除、修改关键配置）<br>4. 操作日志审计（记录操作人、时间、内容、IP） |
| **审计安全** | 1. 操作日志保留1年以上<br>2. 关键操作实时告警（大额转账、敏感数据导出）<br>3. 日志防篡改（日志写入独立存储，只读权限） |
| **等保合规** | 满足等保三级要求 |

### 9.3 可用性需求

| 指标 | 目标值 | 说明 |
|------|--------|------|
| 系统可用性 | 99.9% | 年停机时间 < 8.76小时 |
| 数据备份 | 每日全量 + 实时增量 | 数据库每日凌晨2点全量备份，binlog实时同步 |
| 故障恢复 | RTO < 30分钟 | 恢复时间目标 |
| 数据恢复 | RPO < 5分钟 | 恢复点目标 |
| 灾难恢复 | 异地容灾 | 数据库主从架构，支持自动故障转移 |

---

## 10. 运营后台

### 10.1 运营工作台功能清单

#### 10.1.1 驾驶舱

| 功能模块 | 功能描述 | 优先级 |
|---------|---------|--------|
| 核心指标看板 | 实时用户数、今日订单数、今日交易额、今日新增商家 | P0 |
| 业务趋势图 | 近7天/30天订单趋势、交易额趋势、用户增长趋势 | P0 |
| 业务占比图 | 各业务线（外卖/商城/直采/培训）订单占比、金额占比 | P1 |
| 待办事项 | 待审核商家数、待处理售后数、待结算订单数 | P0 |
| 异常预警 | 系统异常、大额订单、退款率异常提醒 | P1 |

#### 10.1.2 用户管理

| 功能模块 | 功能描述 | 优先级 |
|---------|---------|--------|
| 用户列表 | 用户查询（手机号/昵称/ID）、详情查看、状态管理 | P0 |
| 用户标签 | 用户标签管理、自动标签规则 | P2 |
| 用户身份 | 身份审核、身份切换记录查询 | P1 |
| 收货地址 | 用户地址查询、异常地址标记 | P2 |
| 用户反馈 | 用户意见反馈收集、处理、回复 | P1 |

#### 10.1.3 商家管理

| 功能模块 | 功能描述 | 优先级 |
|---------|---------|--------|
| 商家列表 | 商家查询、详情查看、状态管理 | P0 |
| 入驻审核 | 资质审核、审核记录、批量审核 | P0 |
| 商家分类 | 商家分类管理、分类属性配置 | P1 |
| 商家评级 | 商家评分管理、评级规则配置 | P2 |
| 商家处罚 | 违规处理、处罚记录、解封操作 | P1 |

#### 10.1.4 商品管理

| 功能模块 | 功能描述 | 优先级 |
|---------|---------|--------|
| 类目管理 | 商品类目CRUD、类目属性配置 | P0 |
| 商品审核 | 商品上下架审核、批量审核 | P0 |
| 商品查询 | 全平台商品查询、违规商品下架 | P1 |
| 价格监控 | 价格异常监控、低价预警 | P2 |
| 敏感词管理 | 商品敏感词库、自动检测 | P1 |

#### 10.1.5 订单管理

| 功能模块 | 功能描述 | 优先级 |
|---------|---------|--------|
| 订单列表 | 全平台订单查询、详情查看、状态修改 | P0 |
| 订单干预 | 强制取消、退款操作、订单备注 | P1 |
| 售后管理 | 售后申请审核、退款审核、售后统计 | P0 |
| 投诉管理 | 用户投诉处理、商家申诉处理 | P1 |
| 订单导出 | 订单数据导出、报表生成 | P1 |

#### 10.1.6 财务管理

| 功能模块 | 功能描述 | 优先级 |
|---------|---------|--------|
| 交易流水 | 支付流水查询、对账功能 | P0 |
| 结算管理 | 商家结算单生成、结算审核 | P0 |
| 提现审核 | 提现申请审核、批量打款 | P0 |
| 财务报表 | 收入报表、支出报表、利润报表 | P1 |
| 发票管理 | 发票申请审核、发票开具 | P1 |

#### 10.1.7 营销管理

| 功能模块 | 功能描述 | 优先级 |
|---------|---------|--------|
| 优惠券管理 | 优惠券创建、发放、统计 | P0 |
| 满减活动 | 满减规则配置、活动管理 | P0 |
| 秒杀活动 | 秒杀商品配置、库存管理 | P1 |
| 积分管理 | 积分规则、积分商城 | P2 |
| 推广管理 | 推广链接、佣金统计 | P2 |

#### 10.1.8 系统配置

| 功能模块 | 功能描述 | 优先级 |
|---------|---------|--------|
| 参数配置 | 系统参数配置、业务规则配置 | P0 |
| Banner管理 | 轮播图配置、排序、上下线 | P0 |
| 公告管理 | 平台公告发布、定时发布 | P0 |
| 字典管理 | 数据字典维护 | P1 |
| 地区管理 | 省市区数据维护 | P1 |
| 日志查询 | 操作日志、登录日志查询 | P1 |

### 10.2 权限体系设计

#### 10.2.1 角色定义

| 角色 | 职责 | 权限范围 |
|------|------|---------|
| 超级管理员 | 系统最高权限 | 全部权限 |
| 运营总监 | 运营决策 | 驾驶舱、全部运营数据查看 |
| 运营专员 | 日常运营 | 用户管理、商家审核、内容运营 |
| 审核专员 | 资质审核 | 商家审核、商品审核、企业审核 |
| 财务人员 | 财务结算 | 财务管理、结算审核、提现审核 |
| 客服人员 | 客户服务 | 订单查询、售后处理、用户反馈 |
| 风控人员 | 风险监控 | 商家处罚、异常订单处理、投诉处理 |

---

## 11. 版本规划

### 11.1 一期（MVP版本）- 预计12周

**目标**: 交付"可交易、可运营、可审计、可扩展"的最小闭环版本

**核心功能**:
| 模块 | 功能范围 |
|------|---------|
| 用户系统 | 注册登录、个人信息、地址管理 |
| 外卖业务 | 商家入驻、菜品管理、下单支付、订单履约、评价 |
| 商城业务 | 商家入驻、商品管理、购物车、下单支付、物流跟踪 |
| 直采业务 | 企业认证、批发商城、采购下单、发票管理 |
| 金厨培训 | 学校入驻、课程发布、在线报名、学员管理 |
| 运营后台 | 驾驶舱、用户管理、商家审核、订单管理、财务管理 |
| 工作台 | 商家工作台、供应商工作台、学校工作台 |

**技术目标**:
- 核心服务微服务化
- 统一状态机落地
- 基础监控体系搭建

### 11.2 二期（增强版）- 预计8周

**目标**: 完善用户体验，增加运营工具，提升平台粘性

**新增功能**:
| 模块 | 功能范围 |
|------|---------|
| 营销系统 | 优惠券系统、满减活动、秒杀活动、积分体系 |
| 会员体系 | 会员等级、会员权益、成长值体系 |
| 分销体系 | 分享返利、推广员体系 |
| 消息中心 | 站内信、短信模板、推送管理 |
| 数据报表 | 商家数据报表、运营数据报表 |
| 信用金融 | 授信申请、账期支付、还款管理 |
| 多端适配 | 支付宝小程序、H5适配 |

**技术目标**:
- 引入Elasticsearch提升搜索体验
- 完善监控告警体系
- 性能优化（缓存优化、数据库优化）

### 11.3 三期（生态版）- 预计12周

**目标**: 构建本地生活服务生态，拓展业务边界

**新增功能**:
| 模块 | 功能范围 |
|------|---------|
| 酒店预订 | 酒店入驻、房型管理、在线预订 |
| 景点门票 | 景区入驻、门票管理、电子票务 |
| 研学活动 | 活动发布、报名管理、行程管理 |
| 直播带货 | 直播功能、商品橱窗、直播回放 |
| 社区团购 | 团长管理、团购活动、自提点管理 |
| 智能推荐 | 个性化推荐、智能搜索、千人千面 |
| 开放平台 | 开放API、第三方接入、开发者中心 |

**技术目标**:
- 引入AI能力（推荐算法、智能客服）
- 大数据平台建设
- 异地多活架构

---

## 12. 风险评估

### 12.1 技术风险

| 风险项 | 风险等级 | 风险描述 | 应对措施 |
|--------|---------|---------|---------|
| 微服务拆分过度 | 中 | 服务过多导致运维复杂度增加 | 一期适度拆分，核心业务独立，非核心合并 |
| 数据库性能瓶颈 | 中 | 订单量大时数据库性能下降 | 分库分表设计预留、读写分离、缓存优化 |
| 第三方服务依赖 | 中 | 微信支付、短信服务等第三方故障 | 降级方案、备用渠道、熔断机制 |
| 数据一致性问题 | 高 | 分布式事务处理不当导致数据不一致 | 最终一致性设计、对账机制、补偿机制 |
| 安全漏洞 | 高 | 数据泄露、支付安全风险 | 安全审计、渗透测试、等保合规 |

### 12.2 业务风险

| 风险项 | 风险等级 | 风险描述 | 应对措施 |
|--------|---------|---------|---------|
| 商家入驻率低 | 高 | 本地商家数字化意愿不强 | 地推团队、入驻优惠、简化流程 |
| 用户增长缓慢 | 中 | 开州区用户基数有限 | 裂变活动、地推推广、口碑传播 |
| 供应链不稳定 | 中 | 供应商配合度、商品质量 | 严格审核、评分机制、淘汰机制 |
| 恶性竞争 | 中 | 商家低价倾销、恶意竞争 | 价格监控、规则约束、处罚机制 |
| 政策合规风险 | 中 | 地方政策变化、资质要求 | 政策跟踪、合规审查、资质储备 |

### 12.3 运营风险

| 风险项 | 风险等级 | 风险描述 | 应对措施 |
|--------|---------|---------|---------|
| 订单履约问题 | 高 | 配送延迟、商品缺货 | 配送体系建设、库存预警、应急预案 |
| 售后纠纷 | 中 | 退款退货纠纷处理 | 售后规则明确、客服培训、平台介入机制 |
| 资金安全风险 | 高 | 资金挪用、提现风险 | 资金托管、风控审核、限额管理 |
| 平台口碑风险 | 中 | 负面评价、舆情危机 | 舆情监控、快速响应、危机公关 |

---

## 13. 实施路线图

### 阶段0: 基础准备 (1周)
- 仓库初始化、脚手架搭建
- 统一状态码、编码规范
- 开发环境、测试环境搭建

### 阶段1: 基础能力 (2周)
- user-service: 登录注册、用户信息
- iam-service: 权限管理
- merchant-service: 商家入驻审核

### 阶段2: 商品与内容 (2周)
- product-service: 商品管理
- content-service: Banner、公告
- sync-service: 项目导入
- project-service: 项目展示

### 阶段3: 交易核心 (3周)
- order-service: 订单系统
- payment-service: 支付系统
- procurement-service: 采购车

### 阶段4: 调度与运维 (2周)
- delivery-service: 配送服务
- settlement-service: 结算服务骨架
- credit-service: 信用服务骨架

### 阶段5: 联调验收 (2周)
- 全链路联调
- 性能测试
- 安全审计
- 用户验收测试

---

## 14. 附录

### 14.1 术语表

| 术语 | 说明 |
|------|------|
| SPU | 标准产品单元，商品主信息 |
| SKU | 库存量单位，商品规格 |
| RBAC | 基于角色的访问控制 |
| MVP | 最小可行产品 |
| QPS | 每秒查询率 |
| RTO | 恢复时间目标 |
| RPO | 恢复点目标 |
| TTFB | 首字节时间 |
| DOM Ready | DOM加载完成时间 |
| JWT | JSON Web Token |
| TPS | 每秒事务处理量 |
| MOQ | 最小起订量 |

### 14.2 参考标准

| 参考平台 | 参考内容 |
|---------|---------|
| 美团外卖 | 外卖业务流程、骑手调度、商家管理 |
| 饿了么 | 配送体系、营销活动、评价体系 |
| 淘宝 | 商品搜索、推荐算法、购物车 |
| 京东 | 物流体系、售后服务、企业采购 |
| 阿里巴巴1688 | B2B交易、企业认证、账期管理 |
| 网易云课堂 | 课程管理、学习进度、证书系统 |
| 58同城 | 本地服务分类、信息发布 |

### 14.3 参考文档

- MVP范围: docs/implementation/01-mvp-scope.md
- 领域模型: docs/implementation/03-domain-model.md
- 后台架构: docs/implementation/07-backoffice-architecture.md
- 服务边界: docs/implementation/02-service-boundaries.md

---

**文档结束**

*本PRD文档基于行业最佳实践深度完善，涵盖外卖、电商、B2B采购、在线教育四大业务领域，参考美团、饿了么、淘宝、京东、1688、网易云课堂等一线平台的设计规范，确保开州通综合服务平台达到行业顶级水准。*

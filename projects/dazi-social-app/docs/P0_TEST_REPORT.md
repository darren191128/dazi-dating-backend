# 搭子交友微信小程序 P0 功能测试报告

**测试日期**: 2026-03-24  
**测试范围**: 动态系统、关注/粉丝系统、滑动匹配系统、消息系统、访客记录、API接口、配置文件、数据库  
**测试方法**: 代码审查、配置检查、接口检查、依赖检查

---

## 1. 测试概述

本次测试对搭子交友微信小程序的P0核心功能进行全面审查，涵盖前端页面、后端API、配置文件和数据库脚本。测试采用静态代码分析方法，重点检查代码逻辑、语法错误、API一致性、配置正确性等方面。

---

## 2. 各模块测试结果

### 2.1 动态系统测试

#### 测试文件
- `/frontend/src/pages/moment/index.vue` - 动态列表页
- `/frontend/src/pages/moment/publish.vue` - 动态发布页
- `/frontend/src/pages/moment/detail.vue` - 动态详情页
- `/frontend/src/components/MomentCard.vue` - 动态卡片组件

#### 后端API
- `/backend/moment-service/`

#### 测试结果
| 功能点 | 状态 | 备注 |
|--------|------|------|
| 动态发布（文本、图片） | ⚠️ 问题 | 前端API调用与后端接口不匹配 |
| 动态列表加载（广场/关注Tab） | ⚠️ 问题 | API调用方法名不一致 |
| 点赞/取消点赞 | ⚠️ 问题 | 前端调用方式与后端接口不匹配 |
| 评论功能 | ⚠️ 问题 | API参数传递方式不一致 |
| 页面跳转 | ✅ 通过 | 路由配置正确 |

#### 发现的问题
1. **API接口不匹配** (严重)
   - 前端调用: `momentApi.getSquareMoments()` / `momentApi.getFollowingMoments()`
   - 后端实际接口: `GET /api/moment/list?type=all/following`
   - 前端调用: `momentApi.likeMoment(id)` / `momentApi.commentMoment(id, data)`
   - 后端实际接口: `POST /api/moment/like` (参数在body中) / `POST /api/moment/comment`

2. **缺少API定义** (严重)
   - 前端使用了 `momentApi.getMomentDetail()` / `momentApi.getMomentLikes()` 等方法
   - 但 `/frontend/src/api/index.js` 中 `momentApi` 对象缺少这些方法的定义

3. **图片上传路径问题** (中等)
   - `publish.vue` 中使用 `uni.getStorageSync('baseURL')` 获取上传地址
   - 但标准做法是使用 `request.baseURL` 或配置常量

4. **onBackPress未导入** (中等)
   - `publish.vue` 中使用了 `onBackPress` 生命周期钩子但未从 `@dcloudio/uni-app` 导入

---

### 2.2 关注/粉丝系统测试

#### 测试文件
- `/frontend/src/pages/user/followings.vue` - 关注列表页
- `/frontend/src/pages/user/followers.vue` - 粉丝列表页

#### 后端API
- `/backend/user-service/`

#### 测试结果
| 功能点 | 状态 | 备注 |
|--------|------|------|
| 关注用户功能 | ⚠️ 问题 | API调用方式与后端接口不匹配 |
| 取消关注功能 | ⚠️ 问题 | API调用方式与后端接口不匹配 |
| 关注列表展示 | ✅ 通过 | 实现正确 |
| 粉丝列表展示 | ✅ 通过 | 实现正确 |

#### 发现的问题
1. **API调用方式不匹配** (严重)
   - 前端调用: `followApi.follow(userId)` / `followApi.unfollow(userId)` 直接传userId
   - 后端实际接口: `POST /api/user/follow` / `POST /api/user/unfollow` (参数在body中)
   - 前端代码: `followApi.unfollow(user.userId)`
   - 后端期望: `{ "followUserId": xxx }`

2. **缺少API定义** (严重)
   - 前端使用了 `followApi.getFollowings()` / `followApi.getFollowers()`
   - 但 `/frontend/src/api/index.js` 中没有定义 `followApi` 对象

---

### 2.3 滑动匹配系统测试

#### 测试文件
- `/frontend/src/pages/match/index.vue` - 匹配页面
- `/frontend/src/pages/match/success.vue` - 匹配成功页
- `/frontend/src/components/SwipeCard.vue` - 滑动卡片组件

#### 后端API
- `/backend/user-service/` (匹配相关)

#### 测试结果
| 功能点 | 状态 | 备注 |
|--------|------|------|
| 滑动卡片动画 | ✅ 通过 | 实现正确 |
| 喜欢/不喜欢操作 | ⚠️ 问题 | API调用方式与后端接口不匹配 |
| 互相匹配检测 | ✅ 通过 | 逻辑正确 |
| 匹配成功页面 | ⚠️ 问题 | onLoad导入问题 |

#### 发现的问题
1. **API调用方式不匹配** (严重)
   - 前端调用: `matchApi.swipeAction({ targetUserId, action })`
   - 后端实际接口: `POST /api/user/match/like` / `POST /api/user/match/dislike`
   - 后端是两个独立接口，前端是一个统一接口

2. **缺少API定义** (严重)
   - 前端使用了 `matchApi.getSwipeRecommendations()`
   - 但 `/frontend/src/api/index.js` 中没有定义该方法

3. **onLoad导入问题** (中等)
   - `success.vue` 中 `onLoad` 从 `@dcloudio/uni-app` 导入，但写法有误
   - 正确写法: `import { onLoad } from '@dcloudio/uni-app'`

4. **currentUser获取问题** (轻微)
   - `success.vue` 中 `currentUser` 使用 `ref(userStore.userInfo)`
   - 这会导致响应式问题，应该使用 `computed`

---

### 2.4 消息系统测试

#### 测试文件
- `/frontend/src/pages/message/chat.vue` - 聊天页面
- `/frontend/src/components/VoiceMessage.vue` - 语音消息组件

#### 后端API
- `/backend/message-service/`

#### 测试结果
| 功能点 | 状态 | 备注 |
|--------|------|------|
| 语音消息功能 | ⚠️ 问题 | 组件未在chat.vue中使用 |
| 图片消息功能 | ⚠️ 问题 | 仅模拟实现，无真实上传 |
| 已读状态显示 | ⚠️ 问题 | 组件有已读标记但页面未使用 |

#### 发现的问题
1. **VoiceMessage组件未使用** (严重)
   - `VoiceMessage.vue` 组件已实现语音消息UI和播放功能
   - 但 `chat.vue` 中没有使用该组件，仅使用文本消息

2. **消息功能为模拟实现** (严重)
   - `chat.vue` 中的消息列表是硬编码的模拟数据
   - 没有调用实际的API获取历史消息
   - `sendMessage` 仅在前端添加消息，没有发送到后端

3. **图片消息仅模拟** (中等)
   - `chooseImage` 方法仅添加 `[图片]` 文本消息
   - 没有实际上传图片并发送图片消息

4. **缺少API定义** (严重)
   - 前端没有调用任何消息相关的API
   - `/frontend/src/api/index.js` 中定义的 `messageApi` 未被使用

---

### 2.5 访客记录测试

#### 测试文件
- `/frontend/src/pages/user/visitors.vue` - 访客记录页

#### 后端API
- `/backend/user-service/` (访客相关)

#### 测试结果
| 功能点 | 状态 | 备注 |
|--------|------|------|
| 访客记录展示 | ✅ 通过 | 实现正确 |
| 时间显示 | ✅ 通过 | 实现正确 |
| 关注访客功能 | ⚠️ 问题 | API定义缺失 |

#### 发现的问题
1. **缺少API定义** (严重)
   - 前端使用了 `visitorApi.getVisitors()` 和 `followApi.follow()`
   - 但 `/frontend/src/api/index.js` 中没有定义 `visitorApi` 对象

---

### 2.6 API接口测试

#### 测试文件
- `/frontend/src/api/index.js`

#### 测试结果
| 检查项 | 状态 | 备注 |
|--------|------|------|
| API定义完整性 | ❌ 失败 | 大量API缺失 |
| 参数正确性 | ⚠️ 问题 | 部分API参数与后端不匹配 |

#### 发现的问题

**严重问题：API定义缺失**

前端代码中使用了以下API，但在 `index.js` 中没有定义：

| 缺失的API | 使用位置 | 应定义的方法 |
|-----------|----------|--------------|
| `momentApi.getSquareMoments()` | moment/index.vue | 需要添加 |
| `momentApi.getFollowingMoments()` | moment/index.vue | 需要添加 |
| `momentApi.getMomentDetail()` | moment/detail.vue | 需要添加 |
| `momentApi.getMomentLikes()` | moment/detail.vue | 需要添加 |
| `momentApi.commentMoment()` | moment/detail.vue | 需要修改参数 |
| `followApi.getFollowings()` | user/followings.vue | 需要添加 |
| `followApi.getFollowers()` | user/followers.vue | 需要添加 |
| `followApi.follow()` | user/followers.vue | 需要添加 |
| `followApi.unfollow()` | user/followings.vue | 需要添加 |
| `matchApi.getSwipeRecommendations()` | match/index.vue | 需要添加 |
| `matchApi.swipeAction()` | match/index.vue | 需要添加 |
| `visitorApi.getVisitors()` | user/visitors.vue | 需要添加 |

**API参数不匹配问题**

| API方法 | 前端调用 | 后端期望 | 问题 |
|---------|----------|----------|------|
| likeMoment | `likeMoment(id)` (路径参数) | `POST /like` body: `{momentId}` | 不匹配 |
| unlikeMoment | `unlike(id)` | `POST /unlike` body: `{momentId}` | 不匹配 |
| comment | `comment(id, data)` | `POST /comment` body: `{momentId, ...}` | 不匹配 |
| follow | `follow(userId)` | `POST /follow` body: `{followUserId}` | 不匹配 |
| unfollow | `unfollow(userId)` | `POST /unfollow` body: `{followUserId}` | 不匹配 |

---

### 2.7 配置文件测试

#### 测试文件
- `/frontend/pages.json`
- `/backend/gateway-service/src/main/resources/application.yml`
- `/backend/pom.xml`

#### 测试结果
| 检查项 | 状态 | 备注 |
|--------|------|------|
| 页面路由配置 | ✅ 通过 | 配置完整 |
| 网关路由配置 | ⚠️ 问题 | 缺少过滤器配置 |
| Maven模块配置 | ✅ 通过 | 配置正确 |

#### 发现的问题

1. **网关缺少过滤器配置** (中等)
   - `application.yml` 中只有路由配置，没有添加JWT认证过滤器
   - 后端Controller从request中获取 `currentUserId`，需要过滤器设置
   - 建议添加自定义过滤器进行token验证和用户信息注入

2. **网关路由与后端Controller路径前缀不匹配** (严重)
   - 网关路由: `Path=/api/user/**` → `lb://user-service`
   - 后端Controller: `@RequestMapping("/api/user")`
   - 实际请求路径会变成 `/api/user/api/user/xxx`，存在重复前缀问题
   - 建议：网关使用 `StripPrefix=1` 过滤器，或调整Controller路径

---

### 2.8 数据库测试

#### 测试文件
- `/backend/docs/p0-database.sql`

#### 测试结果
| 检查项 | 状态 | 备注 |
|--------|------|------|
| SQL语法正确性 | ✅ 通过 | 语法正确 |
| 表结构完整性 | ✅ 通过 | 结构完整 |
| 索引配置 | ✅ 通过 | 索引合理 |

#### 评价
数据库脚本质量良好：
- 所有表都包含必要的字段（id、创建时间、更新时间等）
- 索引设计合理，考虑了查询场景
- 使用了合适的字符集（utf8mb4）
- 包含软删除字段（deleted）
- 外键关系通过应用层维护，符合微服务设计原则

---

## 3. 发现的问题清单

### 3.1 严重问题（阻塞性问题）

| 序号 | 问题描述 | 影响范围 | 修复优先级 |
|------|----------|----------|------------|
| 1 | API定义缺失：前端使用了大量未定义的API（momentApi.getSquareMoments, followApi.getFollowings等） | 所有P0功能 | P0 |
| 2 | API参数不匹配：前端API调用方式与后端接口期望不一致 | 动态系统、关注系统、匹配系统 | P0 |
| 3 | 消息系统为模拟实现，未对接真实API | 消息系统 | P0 |
| 4 | VoiceMessage组件已实现但未被chat.vue使用 | 消息系统 | P0 |
| 5 | 网关路由前缀重复问题：可能导致请求路径错误 | 所有后端服务 | P0 |

### 3.2 中等问题（功能影响）

| 序号 | 问题描述 | 影响范围 | 修复优先级 |
|------|----------|----------|------------|
| 6 | 网关缺少JWT认证过滤器配置 | 所有后端服务 | P1 |
| 7 | `publish.vue` 中 `onBackPress` 未导入 | 动态发布 | P1 |
| 8 | `match/success.vue` 中 `onLoad` 导入写法有误 | 匹配成功页 | P1 |
| 9 | `match/success.vue` 中 `currentUser` 响应式问题 | 匹配成功页 | P2 |
| 10 | 图片上传使用 `uni.getStorageSync('baseURL')` 不规范 | 动态发布 | P2 |

### 3.3 轻微问题（优化建议）

| 序号 | 问题描述 | 影响范围 | 修复优先级 |
|------|----------|----------|------------|
| 11 | 缺少API错误处理的统一拦截 | 前端全局 | P2 |
| 12 | 部分页面缺少加载状态处理 | 前端多个页面 | P2 |
| 13 | 缺少图片上传失败的重试机制 | 动态发布 | P3 |

---

## 4. 修复建议

### 4.1 严重问题修复方案

#### 问题1 & 2：API定义与参数修复

**方案A：修改前端API定义（推荐）**

```javascript
// /frontend/src/api/index.js

/**
 * 动态相关API（修正版）
 */
export const momentApi = {
  // 发布动态
  publishMoment: (data) => request.post('/moment/publish', data),
  
  // 获取动态列表 - 广场/关注
  getSquareMoments: (params) => request.get('/moment/list', { ...params, type: 'all' }),
  getFollowingMoments: (params) => request.get('/moment/list', { ...params, type: 'following' }),
  
  // 获取动态详情
  getMomentDetail: (id) => request.get(`/moment/detail/${id}`),
  
  // 获取点赞列表
  getMomentLikes: (id) => request.get(`/moment/likes/${id}`), // 后端需要添加此接口
  
  // 获取评论列表
  getMomentComments: (id, params) => request.get(`/moment/comments/${id}`, params),
  
  // 点赞/取消点赞
  likeMoment: (id) => request.post('/moment/like', { momentId: id }),
  unlikeMoment: (id) => request.post('/moment/unlike', { momentId: id }),
  
  // 评论
  commentMoment: (id, data) => request.post('/moment/comment', { momentId: id, ...data }),
  
  // 删除动态
  deleteMoment: (id) => request.delete(`/moment/${id}`)
};

/**
 * 关注相关API（新增）
 */
export const followApi = {
  // 关注用户
  follow: (userId) => request.post('/user/follow', { followUserId: userId }),
  
  // 取消关注
  unfollow: (userId) => request.post('/user/unfollow', { followUserId: userId }),
  
  // 获取关注列表
  getFollowings: (params) => request.get('/user/followings', params),
  
  // 获取粉丝列表
  getFollowers: (params) => request.get('/user/followers', params)
};

/**
 * 匹配相关API（新增）
 */
export const matchApi = {
  // 获取推荐列表
  getSwipeRecommendations: (params) => request.get('/user/match/recommendations', params), // 后端需要添加
  
  // 喜欢
  like: (userId) => request.post('/user/match/like', { targetUserId: userId }),
  
  // 不喜欢
  dislike: (userId) => request.post('/user/match/dislike', { targetUserId: userId }),
  
  // 互相喜欢列表
  getMutualLikes: () => request.get('/user/match/mutual')
};

/**
 * 访客相关API（新增）
 */
export const visitorApi = {
  // 获取访客列表
  getVisitors: (params) => request.get('/user/visitors', params)
};
```

**方案B：修改后端接口（如果前端调用方式更合理）**

如果希望保持前端调用方式，需要修改后端Controller：

```java
// MomentController.java

@PostMapping("/like/{momentId}")
public Result<Void> likeMoment(
        HttpServletRequest request,
        @PathVariable Long momentId) {
    Long userId = (Long) request.getAttribute("currentUserId");
    return momentService.likeMoment(userId, momentId);
}
```

#### 问题3 & 4：消息系统修复

**chat.vue 需要重写以对接真实API：**

```vue
<template>
  <!-- 添加语音消息支持 -->
  <view 
    class="message-item" 
    :class="msg.isSelf ? 'self' : 'other'"
    v-for="(msg, index) in messages" 
    :key="msg.id"
  >
    <image class="avatar" :src="msg.isSelf ? userInfo.avatar : otherAvatar" mode="aspectFill" />
    
    <!-- 文本消息 -->
    <view v-if="msg.messageType === 'text'" class="message-content">
      <text class="message-text">{{ msg.content }}</text>
    </view>
    
    <!-- 语音消息 -->
    <VoiceMessage 
      v-else-if="msg.messageType === 'voice'"
      :src="msg.voiceUrl"
      :duration="msg.voiceDuration"
      :isSelf="msg.isSelf"
      :isRead="msg.isRead"
    />
    
    <!-- 图片消息 -->
    <image 
      v-else-if="msg.messageType === 'image'"
      :src="msg.imageUrl"
      mode="aspectFill"
      class="message-image"
      @click="previewImage(msg.imageUrl)"
    />
  </view>
</template>

<script setup>
import { ref, onMounted, onLoad } from '@dcloudio/uni-app'
import { messageApi, messageExtApi } from '@/api'
import VoiceMessage from '@/components/VoiceMessage.vue'

// 加载历史消息
const loadMessages = async () => {
  const res = await messageApi.getMessages(conversationId.value, { page: 1, pageSize: 50 })
  if (res.code === 200) {
    messages.value = res.data.list
  }
}

// 发送消息
const sendMessage = async () => {
  if (!inputMessage.value.trim()) return
  
  const res = await messageApi.sendMessage(conversationId.value, {
    content: inputMessage.value,
    messageType: 'text'
  })
  
  if (res.code === 200) {
    messages.value.push(res.data)
    inputMessage.value = ''
    scrollToBottom()
  }
}

// 发送语音消息
const sendVoiceMessage = async (voicePath, duration) => {
  // 1. 上传语音文件
  const uploadRes = await messageExtApi.uploadVoice(voicePath)
  if (uploadRes.code !== 200) return
  
  // 2. 发送消息
  const res = await messageApi.sendMessage(conversationId.value, {
    messageType: 'voice',
    voiceUrl: uploadRes.data.url,
    voiceDuration: duration
  })
  
  if (res.code === 200) {
    messages.value.push(res.data)
    scrollToBottom()
  }
}
</script>
```

#### 问题5：网关路由修复

**修改 application.yml：**

```yaml
spring:
  cloud:
    gateway:
      routes:
        - id: user-service
          uri: lb://user-service
          predicates:
            - Path=/api/user/**
          filters:
            - StripPrefix=1  # 去掉 /api 前缀
            - name: AuthFilter  # 自定义认证过滤器
        - id: moment-service
          uri: lb://moment-service
          predicates:
            - Path=/api/moment/**
          filters:
            - StripPrefix=1
            - name: AuthFilter
        # ... 其他路由
```

**或者修改后端Controller（推荐，改动更小）：**

```java
// 将 @RequestMapping("/api/user") 改为 @RequestMapping("/user")
// 这样完整路径就是 /api/user/xxx，与网关路由匹配
```

### 4.2 中等问题修复方案

#### 问题6：添加JWT过滤器

```java
@Component
public class AuthFilter implements GlobalFilter, Ordered {
    
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();
        
        // 放行登录接口
        if (path.contains("/wxLogin")) {
            return chain.filter(exchange);
        }
        
        // 验证token
        String token = request.getHeaders().getFirst("Authorization");
        if (token == null || !token.startsWith("Bearer ")) {
            return unauthorized(exchange);
        }
        
        try {
            // 解析token获取userId
            Long userId = JwtUtil.parseToken(token.substring(7));
            
            // 将userId添加到请求头
            ServerHttpRequest mutatedRequest = request.mutate()
                .header("X-User-Id", userId.toString())
                .build();
            
            return chain.filter(exchange.mutate().request(mutatedRequest).build());
        } catch (Exception e) {
            return unauthorized(exchange);
        }
    }
    
    private Mono<Void> unauthorized(ServerWebExchange exchange) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        return response.setComplete();
    }
    
    @Override
    public int getOrder() {
        return -100;
    }
}
```

#### 问题7 & 8：导入修复

```javascript
// publish.vue
import { ref, computed } from 'vue'
import { onBackPress } from '@dcloudio/uni-app'  // 添加这行

// success.vue
import { ref, computed } from 'vue'
import { onLoad } from '@dcloudio/uni-app'  // 修正这行
```

---

## 5. 总体评估

### 5.1 质量评级

| 模块 | 评级 | 说明 |
|------|------|------|
| 前端UI实现 | ⭐⭐⭐⭐ | UI组件实现良好，交互逻辑完整 |
| 前端API对接 | ⭐⭐ | API定义缺失严重，参数不匹配 |
| 后端API实现 | ⭐⭐⭐⭐ | 后端Controller实现完整，逻辑正确 |
| 数据库设计 | ⭐⭐⭐⭐⭐ | 设计规范，索引合理 |
| 配置文件 | ⭐⭐⭐ | 基本配置正确，但缺少关键过滤器 |

### 5.2 总体结论

**当前状态：不可发布**

虽然前端UI组件和后端API各自实现都较为完整，但前后端API对接存在严重问题：

1. **API定义缺失** 导致前端无法正常调用后端服务
2. **API参数不匹配** 导致即使调用也会失败
3. **消息系统未实现** 核心功能缺失

### 5.3 修复工作量估算

| 任务 | 预估工时 | 负责人 |
|------|----------|--------|
| 完善前端API定义 | 4h | 前端 |
| 修复API参数不匹配问题 | 2h | 前端 |
| 实现消息系统真实对接 | 6h | 前后端 |
| 添加网关JWT过滤器 | 3h | 后端 |
| 修复导入和响应式问题 | 1h | 前端 |
| 联调测试 | 4h | 前后端 |
| **总计** | **20h** | - |

### 5.4 发布建议

**建议完成以下修复后再进行发布：**

1. ✅ 完成所有严重问题的修复
2. ✅ 进行完整的联调测试
3. ✅ 验证所有P0功能正常
4. ✅ 进行一轮回归测试

**预计可发布时间：修复完成后 + 2天（测试缓冲）**

---

## 附录：测试覆盖清单

### 前端文件测试覆盖
- [x] `/frontend/src/pages/moment/index.vue`
- [x] `/frontend/src/pages/moment/publish.vue`
- [x] `/frontend/src/pages/moment/detail.vue`
- [x] `/frontend/src/components/MomentCard.vue`
- [x] `/frontend/src/pages/user/followings.vue`
- [x] `/frontend/src/pages/user/followers.vue`
- [x] `/frontend/src/pages/match/index.vue`
- [x] `/frontend/src/pages/match/success.vue`
- [x] `/frontend/src/components/SwipeCard.vue`
- [x] `/frontend/src/pages/message/chat.vue`
- [x] `/frontend/src/components/VoiceMessage.vue`
- [x] `/frontend/src/pages/user/visitors.vue`
- [x] `/frontend/src/api/index.js`

### 后端文件测试覆盖
- [x] `/backend/moment-service/` (Controller, Service)
- [x] `/backend/user-service/` (Controller)
- [x] `/backend/message-service/` (pom.xml检查)

### 配置文件测试覆盖
- [x] `/frontend/pages.json`
- [x] `/backend/gateway-service/src/main/resources/application.yml`
- [x] `/backend/pom.xml`
- [x] `/backend/moment-service/pom.xml`
- [x] `/backend/user-service/pom.xml`
- [x] `/backend/message-service/pom.xml`

### 数据库测试覆盖
- [x] `/backend/docs/p0-database.sql`

---

*报告生成时间: 2026-03-24*  
*测试人员: AI测试助手*

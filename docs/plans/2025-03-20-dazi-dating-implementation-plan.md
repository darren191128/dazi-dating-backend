# 搭子交友微信小程序 Implementation Plan

> **For Claude:** REQUIRED SUB-SKILL: Use superpowers:executing-plans to implement this plan task-by-task.

**Goal:** 开发一个基于uni-app的搭子交友微信小程序，包含智能匹配、活动发布、即时通讯、信用分系统等功能。

**Architecture:** 采用前后端分离架构，前端使用uni-app(Vue3) + uview-plus UI库开发微信小程序，后端使用Java + Spring Cloud Alibaba微服务架构，数据存储使用MySQL 8.0(关系数据) + Redis(缓存)，消息队列使用RabbitMQ，文件存储使用阿里云OSS。

**Tech Stack:** 
- 前端: uni-app(Vue3+TypeScript) + uview-plus
- 后端: Java 17 + Spring Cloud Alibaba (Nacos, Gateway, Sentinel)
- 数据库: MySQL 8.0 + Redis 7
- 消息队列: RabbitMQ
- 文件存储: 阿里云OSS
- 地图: 腾讯地图SDK
- 支付: 微信支付

---

## Phase 1: 项目初始化与基础架构 (Day 1-2)

### Task 1: 创建uni-app项目

**Files:**
- Create: `dazi-dating/` (项目根目录)

**Step 1: 初始化uni-app项目**
```bash
# 使用HBuilderX或CLI创建项目
npx degit dcloudio/uni-preset-vue#vite-ts dazi-dating
cd dazi-dating
npm install
```

**Step 2: 安装依赖**
```bash
npm install pinia vue-router@4 axios lodash-es dayjs
npm install -D @types/lodash-es
```

**Step 3: 配置manifest.json**
```json
{
  "name": "搭子交友",
  "appid": "__UNI__XXXXXXX",
  "description": "找搭子，一起吃喝玩乐",
  "versionName": "1.0.0",
  "versionCode": "100",
  "transformPx": false,
  "app-plus": {},
  "mp-weixin": {
    "appid": "wxXXXXXXXXXXXXXXXX",
    "setting": {
      "urlCheck": false
    },
    "usingComponents": true
  }
}
```

**Step 4: 配置tsconfig.json**
```json
{
  "compilerOptions": {
    "target": "ESNext",
    "module": "ESNext",
    "moduleResolution": "node",
    "strict": true,
    "jsx": "preserve",
    "sourceMap": true,
    "resolveJsonModule": true,
    "isolatedModules": true,
    "esModuleInterop": true,
    "lib": ["ESNext", "DOM"],
    "skipLibCheck": true,
    "noEmit": true
  },
  "include": ["src/**/*.ts", "src/**/*.d.ts", "src/**/*.tsx", "src/**/*.vue"]
}
```

**Step 5: 提交代码**
```bash
git init
git add .
git commit -m "init: 创建uni-app项目"
```

---

### Task 2: 搭建项目目录结构

**Files:**
- Create: `dazi-dating/src/pages/` (页面目录)
- Create: `dazi-dating/src/components/` (组件目录)
- Create: `dazi-dating/src/store/` (状态管理)
- Create: `dazi-dating/src/api/` (API接口)
- Create: `dazi-dating/src/utils/` (工具函数)
- Create: `dazi-dating/src/types/` (类型定义)
- Create: `dazi-dating/src/static/` (静态资源)

**Step 1: 创建目录结构**
```bash
mkdir -p src/pages/{index,match,activity,profile,chat}
mkdir -p src/components/{common,activity,user}
mkdir -p src/store/modules
mkdir -p src/api/modules
mkdir -p src/utils
mkdir -p src/types
mkdir -p src/static/images
```

**Step 2: 创建pages.json**
```json
{
  "pages": [
    {
      "path": "pages/index/index",
      "style": {
        "navigationBarTitleText": "搭子交友"
      }
    },
    {
      "path": "pages/match/match",
      "style": {
        "navigationBarTitleText": "找搭子"
      }
    },
    {
      "path": "pages/activity/activity",
      "style": {
        "navigationBarTitleText": "活动"
      }
    },
    {
      "path": "pages/profile/profile",
      "style": {
        "navigationBarTitleText": "我的"
      }
    }
  ],
  "tabBar": {
    "list": [
      {
        "pagePath": "pages/index/index",
        "text": "首页",
        "iconPath": "static/images/home.png",
        "selectedIconPath": "static/images/home-active.png"
      },
      {
        "pagePath": "pages/match/match",
        "text": "匹配",
        "iconPath": "static/images/match.png",
        "selectedIconPath": "static/images/match-active.png"
      },
      {
        "pagePath": "pages/activity/activity",
        "text": "活动",
        "iconPath": "static/images/activity.png",
        "selectedIconPath": "static/images/activity-active.png"
      },
      {
        "pagePath": "pages/profile/profile",
        "text": "我的",
        "iconPath": "static/images/profile.png",
        "selectedIconPath": "static/images/profile-active.png"
      }
    ]
  }
}
```

**Step 3: 提交代码**
```bash
git add .
git commit -m "chore: 搭建项目目录结构"
```

---

### Task 3: 配置状态管理(Pinia)

**Files:**
- Create: `dazi-dating/src/store/index.ts`
- Create: `dazi-dating/src/store/modules/user.ts`
- Create: `dazi-dating/src/store/modules/activity.ts`
- Modify: `dazi-dating/src/main.ts`

**Step 1: 安装Pinia**
```bash
npm install pinia
```

**Step 2: 创建store入口**
```typescript
// src/store/index.ts
import { createPinia } from 'pinia'

export default createPinia()
```

**Step 3: 创建user模块**
```typescript
// src/store/modules/user.ts
import { defineStore } from 'pinia'
import { ref, computed } from 'vue'

export const useUserStore = defineStore('user', () => {
  // State
  const token = ref('')
  const userInfo = ref<UserInfo | null>(null)
  const isLogin = computed(() => !!token.value)

  // Actions
  const setToken = (newToken: string) => {
    token.value = newToken
    uni.setStorageSync('token', newToken)
  }

  const setUserInfo = (info: UserInfo) => {
    userInfo.value = info
    uni.setStorageSync('userInfo', info)
  }

  const logout = () => {
    token.value = ''
    userInfo.value = null
    uni.removeStorageSync('token')
    uni.removeStorageSync('userInfo')
  }

  return {
    token,
    userInfo,
    isLogin,
    setToken,
    setUserInfo,
    logout
  }
})

interface UserInfo {
  id: string
  nickname: string
  avatar: string
  gender: number
  age: number
  creditScore: number
  interests: string[]
}
```

**Step 4: 在main.ts中引入**
```typescript
// src/main.ts
import { createSSRApp } from 'vue'
import App from './App.vue'
import store from './store'

export function createApp() {
  const app = createSSRApp(App)
  app.use(store)
  return {
    app
  }
}
```

**Step 5: 提交代码**
```bash
git add .
git commit -m "feat: 配置Pinia状态管理"
```

---

## Phase 2: 后端API服务搭建 (Day 2-3)

### Task 4: 创建Spring Cloud Alibaba后端项目

**Files:**
- Create: `dazi-dating-server/` (后端项目目录)

**Step 1: 初始化Spring Boot项目**
```bash
# 使用Spring Initializr或手动创建
mkdir dazi-dating-server
cd dazi-dating-server

# 创建Maven项目结构
mkdir -p src/main/{java/com/dazidating/{controller,service,mapper,entity,config,utils},resources}
mkdir -p src/test/java/com/dazidating
```

**Step 2: 配置pom.xml**
```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 
         http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>3.2.0</version>
    </parent>
    
    <groupId>com.dazidating</groupId>
    <artifactId>dazi-dating-server</artifactId>
    <version>1.0.0</version>
    <packaging>pom</packaging>
    
    <properties>
        <java.version>17</java.version>
        <spring-cloud-alibaba.version>2022.0.0.0</spring-cloud-alibaba.version>
    </properties>
    
    <modules>
        <module>dazi-gateway</module>
        <module>dazi-user-service</module>
        <module>dazi-activity-service</module>
        <module>dazi-match-service</module>
        <module>dazi-pay-service</module>
    </modules>
    
    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>com.alibaba.cloud</groupId>
                <artifactId>spring-cloud-alibaba-dependencies</artifactId>
                <version>${spring-cloud-alibaba.version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>
</project>
```

**Step 3: 创建微服务模块**
```bash
# API网关
cd dazi-gateway
# 配置路由、限流、鉴权

cd ../dazi-user-service  
# 用户服务：注册、登录、认证、信用分

cd ../dazi-activity-service
# 活动服务：发布、报名、管理

cd ../dazi-match-service
# 匹配服务：推荐算法、匹配逻辑

cd ../dazi-pay-service
# 支付服务：资金托管、支付、退款
```

**Step 3: 配置TypeScript**
```bash
npx tsc --init
```

**Step 4: 创建目录结构**
```bash
mkdir -p src/{routes,controllers,models,middleware,utils,types}
mkdir -p src/config
```

**Step 5: 提交代码**
```bash
git add .
git commit -m "init: 创建Node.js后端项目"
```

---

### Task 5: 配置Spring Cloud Alibaba基础设施

**Files:**
- Create: `dazi-dating-server/dazi-gateway/src/main/resources/application.yml`
- Create: `dazi-dating-server/dazi-user-service/src/main/resources/application.yml`
- Create: `dazi-dating-server/docker-compose.yml`

**Step 1: 配置Nacos服务注册中心**
```yaml
# docker-compose.yml
version: '3.8'
services:
  nacos:
    image: nacos/nacos-server:v2.2.3
    environment:
      - MODE=standalone
      - SPRING_DATASOURCE_PLATFORM=mysql
      - MYSQL_SERVICE_HOST=mysql
      - MYSQL_SERVICE_DB_NAME=nacos
      - MYSQL_SERVICE_PORT=3306
    ports:
      - "8848:8848"
      
  mysql:
    image: mysql:8.0
    environment:
      MYSQL_ROOT_PASSWORD: root123
      MYSQL_DATABASE: dazi_dating
    volumes:
      - mysql_data:/var/lib/mysql
      - ./sql/init.sql:/docker-entrypoint-initdb.d/init.sql
      
  redis:
    image: redis:7-alpine
    command: redis-server --appendonly yes
    volumes:
      - redis_data:/data
      
  rabbitmq:
    image: rabbitmq:3-management
    environment:
      RABBITMQ_DEFAULT_USER: admin
      RABBITMQ_DEFAULT_PASS: admin123
    ports:
      - "5672:5672"
      - "15672:15672"

volumes:
  mysql_data:
  redis_data:
```

**Step 2: 配置Gateway路由**
```yaml
# dazi-gateway/src/main/resources/application.yml
server:
  port: 8080

spring:
  application:
    name: dazi-gateway
  cloud:
    nacos:
      discovery:
        server-addr: localhost:8848
    gateway:
      routes:
        - id: user-service
          uri: lb://dazi-user-service
          predicates:
            - Path=/api/user/**
          filters:
            - StripPrefix=2
        - id: activity-service
          uri: lb://dazi-activity-service
          predicates:
            - Path=/api/activity/**
          filters:
            - StripPrefix=2
        - id: match-service
          uri: lb://dazi-match-service
          predicates:
            - Path=/api/match/**
          filters:
            - StripPrefix=2
        - id: pay-service
          uri: lb://dazi-pay-service
          predicates:
            - Path=/api/pay/**
          filters:
            - StripPrefix=2
      globalcors:
        cors-configurations:
          '[/**]':
            allowedOrigins: "*"
            allowedMethods: "*"
            allowedHeaders: "*"
```

**Step 3: 配置MyBatis-Plus**
```yaml
# dazi-user-service/src/main/resources/application.yml
server:
  port: 8081

spring:
  application:
    name: dazi-user-service
  cloud:
    nacos:
      discovery:
        server-addr: localhost:8848
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://localhost:3306/dazi_dating?useUnicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai
    username: root
    password: root123
  redis:
    host: localhost
    port: 6379
    database: 0
  rabbitmq:
    host: localhost
    port: 5672
    username: admin
    password: admin123

mybatis-plus:
  configuration:
    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl
  global-config:
    db-config:
      logic-delete-field: deleted
      logic-delete-value: 1
      logic-not-delete-value: 0
  mapper-locations: classpath*:/mapper/**/*.xml

jwt:
  secret: your-256-bit-secret-key-here
  expiration: 604800000  # 7 days
```

**Step 4: 启动基础设施**
```bash
cd dazi-dating-server
docker-compose up -d

# 验证服务
curl http://localhost:8848/nacos
# 访问RabbitMQ管理界面: http://localhost:15672
```

**Step 5: 提交代码**
```bash
git add .
git commit -m "feat: 配置Spring Cloud Alibaba基础设施"
```

---

## Phase 3: 用户系统开发 (Day 3-5)

### Task 6: 实现用户注册登录

**Files:**
- Create: `dazi-dating-server/dazi-user-service/src/main/java/com/dazidating/entity/User.java`
- Create: `dazi-dating-server/dazi-user-service/src/main/java/com/dazidating/service/AuthService.java`
- Create: `dazi-dating-server/dazi-user-service/src/main/java/com/dazidating/controller/AuthController.java`
- Create: `dazi-dating-server/dazi-user-service/src/main/java/com/dazidating/config/JwtConfig.java`

**Step 1: 创建用户实体类**
```java
// entity/User.java
@Data
@TableName("t_user")
public class User {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String openid;
    private String nickname;
    private String avatar;
    private Integer gender; // 0:未知 1:男 2:女
    private Integer age;
    private String phone;
    private String idCard;
    private Boolean isVerified;
    private Integer creditScore;
    private String interests; // JSON格式存储
    private String zodiac;
    private String chineseZodiac;
    private Double latitude;
    private Double longitude;
    private String city;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    @TableLogic
    private Integer deleted;
}
```

**Step 2: 实现微信登录Service**
```java
// service/AuthService.java
@Service
@Slf4j
public class AuthService {
    
    @Autowired
    private UserMapper userMapper;
    
    @Autowired
    private JwtUtils jwtUtils;
    
    @Autowired
    private StringRedisTemplate redisTemplate;
    
    @Value("${wx.appid}")
    private String wxAppId;
    
    @Value("${wx.secret}")
    private String wxSecret;
    
    public LoginVO wxLogin(String code, WxUserInfo userInfo) {
        // 1. 调用微信接口获取openid
        String url = String.format(
            "https://api.weixin.qq.com/sns/jscode2session?appid=%s&secret=%s&js_code=%s&grant_type=authorization_code",
            wxAppId, wxSecret, code
        );
        
        WxSessionResponse wxRes = restTemplate.getForObject(url, WxSessionResponse.class);
        if (wxRes == null || wxRes.getErrcode() != null) {
            throw new BusinessException("微信登录失败");
        }
        
        String openid = wxRes.getOpenid();
        
        // 2. 查询或创建用户
        User user = userMapper.selectOne(
            new LambdaQueryWrapper<User>().eq(User::getOpenid, openid)
        );
        
        if (user == null) {
            // 创建新用户
            user = new User();
            user.setOpenid(openid);
            user.setNickname(userInfo.getNickName());
            user.setAvatar(userInfo.getAvatarUrl());
            user.setGender(userInfo.getGender());
            user.setIsVerified(false);
            user.setCreditScore(500); // 初始信用分
            userMapper.insert(user);
        }
        
        // 3. 生成JWT
        String token = jwtUtils.generateToken(user.getId(), openid);
        
        // 4. 缓存用户信息
        redisTemplate.opsForValue().set(
            "user:" + user.getId(),
            JSON.toJSONString(user),
            7,
            TimeUnit.DAYS
        );
        
        return LoginVO.builder()
            .token(token)
            .userInfo(convertToVO(user))
            .build();
    }
}
```

**Step 3: 实现JWT工具类**
```java
// utils/JwtUtils.java
@Component
public class JwtUtils {
    
    @Value("${jwt.secret}")
    private String secret;
    
    @Value("${jwt.expiration}")
    private Long expiration;
    
    public String generateToken(Long userId, String openid) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration);
        
        return Jwts.builder()
            .setSubject(userId.toString())
            .claim("openid", openid)
            .setIssuedAt(now)
            .setExpiration(expiryDate)
            .signWith(SignatureAlgorithm.HS512, secret)
            .compact();
    }
    
    public Claims parseToken(String token) {
        return Jwts.parser()
            .setSigningKey(secret)
            .parseClaimsJws(token)
            .getBody();
    }
}
```

**Step 4: 提交代码**
```bash
git add .
git commit -m "feat: 实现微信登录功能(Java+Spring Boot)"
```

---

### Task 7: 实现实名认证

**Files:**
- Create: `dazi-dating-server/src/controllers/verifyController.ts`
- Create: `dazi-dating-server/src/routes/verify.ts`

**Step 1: 实现身份证+人脸识别**
```typescript
// src/controllers/verifyController.ts
import { Request, Response } from 'express'
import { db } from '../config/database'

export const realNameVerify = async (req: Request, res: Response) => {
  try {
    const { idCard, realName } = req.body
    const userId = req.userId
    
    // 调用阿里云/腾讯云实名认证API
    const verifyResult = await callVerifyAPI(idCard, realName)
    
    if (verifyResult.success) {
      // 更新用户认证状态
      await db.execute(
        'UPDATE users SET id_card = ?, real_name = ?, is_verified = 1, credit_score = credit_score + 100 WHERE id = ?',
        [idCard, realName, userId]
      )
      
      res.json({ success: true, message: '认证成功' })
    } else {
      res.status(400).json({ error: '认证失败，请检查信息' })
    }
  } catch (error) {
    console.error('Verify error:', error)
    res.status(500).json({ error: '认证失败' })
  }
}
```

**Step 2: 提交代码**
```bash
git add .
git commit -m "feat: 实现实名认证功能"
```

---

## Phase 4: 匹配系统开发 (Day 5-7)

### Task 8: 实现智能匹配算法

**Files:**
- Create: `dazi-dating-server/src/controllers/matchController.ts`
- Create: `dazi-dating-server/src/routes/match.ts`
- Create: `dazi-dating-server/src/utils/matchAlgorithm.ts`

**Step 1: 实现匹配算法**
```typescript
// src/utils/matchAlgorithm.ts
import { db } from '../config/database'

interface MatchParams {
  userId: string
  latitude: number
  longitude: number
  interests: string[]
  gender?: number
  ageMin?: number
  ageMax?: number
}

export const findMatches = async (params: MatchParams) => {
  const { userId, latitude, longitude, interests } = params
  
  // 获取候选用户
  const [candidates] = await db.execute(
    `SELECT u.*, 
      (6371 * acos(cos(radians(?)) * cos(radians(latitude)) * 
      cos(radians(longitude) - radians(?)) + sin(radians(?)) * sin(radians(latitude)))) AS distance
     FROM users u
     WHERE u.id != ? AND u.is_verified = 1
     HAVING distance < 50
     ORDER BY distance
     LIMIT 100`,
    [latitude, longitude, latitude, userId]
  )
  
  // 计算匹配分数
  const scoredCandidates = (candidates as any[]).map(candidate => {
    let score = 0
    
    // 兴趣匹配 (40%)
    const commonInterests = interests.filter(i => candidate.interests?.includes(i))
    score += (commonInterests.length / Math.max(interests.length, 1)) * 400
    
    // 距离 (30%)
    const distanceScore = Math.max(0, 300 - candidate.distance * 6)
    score += distanceScore
    
    // 信用分 (15%)
    score += (candidate.credit_score / 1000) * 150
    
    // 其他因素...
    
    return {
      ...candidate,
      matchScore: Math.round(score),
      commonInterests
    }
  })
  
  // 按匹配分数排序
  return scoredCandidates.sort((a, b) => b.matchScore - a.matchScore).slice(0, 20)
}
```

**Step 2: 提交代码**
```bash
git add .
git commit -m "feat: 实现智能匹配算法"
```

---

## Phase 5: 活动系统开发 (Day 7-10)

### Task 9: 实现活动发布功能

**Files:**
- Create: `dazi-dating-server/src/models/Activity.ts`
- Create: `dazi-dating-server/src/controllers/activityController.ts`
- Create: `dazi-dating-server/src/routes/activity.ts`

**Step 1: 创建活动模型**
```typescript
// src/models/Activity.ts
import mongoose, { Schema, Document } from 'mongoose'

export interface IActivity extends Document {
  title: string
  type: 'food' | 'outdoor' | 'parent' | 'sport' | 'dating'
  subtype: string
  isOnline: boolean
  startTime: Date
  endTime: Date
  location: {
    name: string
    address: string
    latitude: number
    longitude: number
  }
  feeType: 'AA' | 'MALE_PAY' | 'TREAT' | 'FREE'
  totalFee: number
  maxParticipants: number
  currentParticipants: number
  maleCount: number
  femaleCount: number
  description: string
  images: string[]
  creator: mongoose.Types.ObjectId
  participants: mongoose.Types.ObjectId[]
  status: 'open' | 'full' | 'closed' | 'cancelled'
  createdAt: Date
  updatedAt: Date
}

const ActivitySchema = new Schema<IActivity>({
  title: { type: String, required: true },
  type: { type: String, required: true },
  subtype: { type: String },
  isOnline: { type: Boolean, default: false },
  startTime: { type: Date, required: true },
  endTime: { type: Date, required: true },
  location: {
    name: String,
    address: String,
    latitude: Number,
    longitude: Number
  },
  feeType: { type: String, required: true },
  totalFee: { type: Number, default: 0 },
  maxParticipants: { type: Number, required: true },
  currentParticipants: { type: Number, default: 0 },
  maleCount: { type: Number, default: 0 },
  femaleCount: { type: Number, default: 0 },
  description: { type: String },
  images: [{ type: String }],
  creator: { type: Schema.Types.ObjectId, ref: 'User', required: true },
  participants: [{ type: Schema.Types.ObjectId, ref: 'User' }],
  status: { type: String, default: 'open' }
}, { timestamps: true })

export const Activity = mongoose.model<IActivity>('Activity', ActivitySchema)
```

**Step 2: 实现活动发布API**
```typescript
// src/controllers/activityController.ts
import { Request, Response } from 'express'
import { Activity } from '../models/Activity'

export const createActivity = async (req: Request, res: Response) => {
  try {
    const activityData = req.body
    const userId = req.userId
    
    // 验证用户权限（实名认证+信用分）
    const user = await getUserById(userId)
    if (!user.is_verified) {
      return res.status(403).json({ error: '请先完成实名认证' })
    }
    if (user.credit_score < 500) {
      return res.status(403).json({ error: '信用分不足，无法发布活动' })
    }
    
    // 创建活动
    const activity = new Activity({
      ...activityData,
      creator: userId,
      status: 'open'
    })
    
    await activity.save()
    
    res.json({
      success: true,
      activityId: activity._id,
      message: '活动发布成功'
    })
  } catch (error) {
    console.error('Create activity error:', error)
    res.status(500).json({ error: '发布失败' })
  }
}
```

**Step 3: 提交代码**
```bash
git add .
git commit -m "feat: 实现活动发布功能"
```

---

## Phase 6: 前端页面开发 (Day 10-15)

### Task 10: 安装uview-plus并配置

**Files:**
- Modify: `dazi-dating/src/main.ts`
- Create: `dazi-dating/src/App.vue`

**Step 1: 安装uview-plus**
```bash
cd dazi-dating
npm install uview-plus
npm install dayjs
```

**Step 2: 配置uview-plus**
```typescript
// src/main.ts
import { createSSRApp } from 'vue'
import App from './App.vue'
import store from './store'
import uviewPlus from 'uview-plus'

export function createApp() {
  const app = createSSRApp(App)
  app.use(store)
  app.use(uviewPlus)
  return {
    app
  }
}
```

**Step 3: 配置全局样式**
```scss
/* src/App.vue */
<style lang="scss">
@import "uview-plus/index.scss";

/* 主题色 */
$primary-color: #ff6b6b;
$secondary-color: #4ecdc4;
$bg-color: #f5f5f5;

page {
  background-color: $bg-color;
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;
}
</style>
```

**Step 4: 提交代码**
```bash
git add .
git commit -m "feat: 安装并配置uview-plus UI库"
```

---

### Task 11: 实现首页和匹配页面

**Files:**
- Create: `dazi-dating/src/pages/index/index.vue`
- Create: `dazi-dating/src/pages/match/match.vue`
- Create: `dazi-dating/src/components/UserCard.vue`
- Create: `dazi-dating/src/components/ActivityCard.vue`

**Step 1: 创建用户卡片组件（使用uview-plus）**
```vue
<!-- src/components/UserCard.vue -->
<template>
  <view class="user-card">
    <u-image 
      :src="user.avatar" 
      width="120rpx" 
      height="120rpx" 
      shape="circle"
      class="avatar"
    />
    <view class="info">
      <text class="name">{{ user.nickname }}</text>
      <text class="meta">{{ user.age }}岁 · {{ user.distance }}km</text>
      <view class="interests">
        <u-tag 
          v-for="interest in user.commonInterests" 
          :key="interest"
          :text="interest"
          size="mini"
          type="error"
          plain
          class="tag"
        />
      </view>
    </view>
    <view class="match-score">
      <u-circle-progress 
        :percent="user.matchScore" 
        width="80"
        active-color="#ff6b6b"
      >
        <text class="score">{{ user.matchScore }}%</text>
      </u-circle-progress>
      <text class="label">匹配度</text>
    </view>
  </view>
</template>

<script setup lang="ts">
interface User {
  id: string
  nickname: string
  avatar: string
  age: number
  distance: number
  matchScore: number
  commonInterests: string[]
}

defineProps<{
  user: User
}>()
</script>

<style scoped lang="scss">
.user-card {
  display: flex;
  padding: 20rpx;
  background: #fff;
  border-radius: 16rpx;
  margin-bottom: 20rpx;
  box-shadow: 0 2rpx 12rpx rgba(0,0,0,0.1);
  
  .avatar {
    margin-right: 20rpx;
  }
  
  .info {
    flex: 1;
    
    .name {
      font-size: 32rpx;
      font-weight: bold;
      color: #333;
    }
    
    .meta {
      font-size: 24rpx;
      color: #666;
      margin-top: 8rpx;
    }
    
    .interests {
      display: flex;
      flex-wrap: wrap;
      margin-top: 12rpx;
      
      .tag {
        margin-right: 8rpx;
        margin-bottom: 8rpx;
      }
    }
  }
  
  .match-score {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    
    .score {
      font-size: 24rpx;
      font-weight: bold;
      color: #ff6b6b;
    }
    
    .label {
      font-size: 20rpx;
      color: #999;
      margin-top: 8rpx;
    }
  }
}
</style>
```

**Step 2: 创建首页**
```vue
<!-- src/pages/index/index.vue -->
<template>
  <view class="container">
    <!-- 搜索栏 -->
    <u-sticky>
      <view class="search-bar">
        <u-search 
          v-model="searchKeyword" 
          placeholder="搜索搭子或活动"
          :show-action="false"
          @search="onSearch"
        />
      </view>
    </u-sticky>
    
    <!-- 分类标签 -->
    <view class="category-tabs">
      <u-tabs 
        :list="categoryList" 
        :current="currentCategory"
        @change="onCategoryChange"
        active-color="#ff6b6b"
      />
    </view>
    
    <!-- 推荐列表 -->
    <view class="content">
      <u-skeleton rows="3" :loading="loading" title>
        <!-- 推荐用户 -->
        <view class="section" v-if="recommendedUsers.length > 0">
          <view class="section-header">
            <text class="title">推荐搭子</text>
            <u-button text="查看更多" type="primary" plain size="mini" />
          </view>
          <UserCard 
            v-for="user in recommendedUsers" 
            :key="user.id" 
            :user="user"
            @click="goToUserDetail(user.id)"
          />
        </view>
        
        <!-- 推荐活动 -->
        <view class="section" v-if="recommendedActivities.length > 0">
          <view class="section-header">
            <text class="title">附近活动</text>
            <u-button text="查看更多" type="primary" plain size="mini" />
          </view>
          <ActivityCard 
            v-for="activity in recommendedActivities" 
            :key="activity.id" 
            :activity="activity"
            @click="goToActivityDetail(activity.id)"
          />
        </view>
      </u-skeleton>
    </view>
    
    <!-- 发布按钮 -->
    <u-fab 
      icon="plus" 
      @click="goToPublish"
      :custom-style="{ backgroundColor: '#ff6b6b' }"
    />
  </view>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import UserCard from '@/components/UserCard.vue'
import ActivityCard from '@/components/ActivityCard.vue'

const searchKeyword = ref('')
const currentCategory = ref(0)
const loading = ref(true)
const recommendedUsers = ref([])
const recommendedActivities = ref([])

const categoryList = [
  { name: '全部' },
  { name: '吃喝玩乐' },
  { name: '户外游玩' },
  { name: '亲子活动' },
  { name: '户外运动' },
  { name: '相亲交友' }
]

onMounted(() => {
  loadData()
})

const loadData = async () => {
  loading.value = true
  // 调用API加载数据
  // const res = await api.getRecommendations()
  // recommendedUsers.value = res.users
  // recommendedActivities.value = res.activities
  loading.value = false
}

const onSearch = () => {
  uni.navigateTo({
    url: `/pages/search/search?keyword=${searchKeyword.value}`
  })
}

const onCategoryChange = (index: number) => {
  currentCategory.value = index
  loadData()
}

const goToUserDetail = (id: string) => {
  uni.navigateTo({ url: `/pages/user/detail?id=${id}` })
}

const goToActivityDetail = (id: string) => {
  uni.navigateTo({ url: `/pages/activity/detail?id=${id}` })
}

const goToPublish = () => {
  uni.navigateTo({ url: '/pages/activity/publish' })
}
</script>

<style scoped lang="scss">
.container {
  min-height: 100vh;
  
  .search-bar {
    padding: 20rpx;
    background: #fff;
  }
  
  .category-tabs {
    background: #fff;
    padding-bottom: 20rpx;
  }
  
  .content {
    padding: 20rpx;
    
    .section {
      margin-bottom: 40rpx;
      
      .section-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: 20rpx;
        
        .title {
          font-size: 32rpx;
          font-weight: bold;
          color: #333;
        }
      }
    }
  }
}
</style>
```

**Step 3: 提交代码**
```bash
git add .
git commit -m "feat: 实现首页和匹配页面UI（使用uview-plus）"
```

---

## Phase 7: 测试与优化 (Day 15-17)

### Task 11: 编写测试用例

**Files:**
- Create: `dazi-dating-server/src/tests/auth.test.ts`
- Create: `dazi-dating-server/src/tests/match.test.ts`
- Create: `dazi-dating-server/src/tests/activity.test.ts`

**Step 1: 安装测试框架**
```bash
npm install jest @types/jest supertest @types/supertest --save-dev
```

**Step 2: 编写认证测试**
```typescript
// src/tests/auth.test.ts
import request from 'supertest'
import app from '../app'

describe('Auth API', () => {
  test('POST /api/auth/wx-login - 微信登录', async () => {
    const res = await request(app)
      .post('/api/auth/wx-login')
      .send({
        code: 'test_code',
        userInfo: {
          nickname: '测试用户',
          avatarUrl: 'https://example.com/avatar.jpg',
          gender: 1
        }
      })
    
    expect(res.status).toBe(200)
    expect(res.body.token).toBeDefined()
    expect(res.body.userInfo).toBeDefined()
  })
})
```

**Step 3: 运行测试**
```bash
npm test
```

**Step 4: 提交代码**
```bash
git add .
git commit -m "test: 添加单元测试"
```

---

## Phase 8: 部署上线 (Day 17-20)

### Task 12: 配置生产环境

**Files:**
- Create: `dazi-dating-server/docker-compose.prod.yml`
- Create: `dazi-dating-server/Jenkinsfile`
- Create: `.github/workflows/deploy.yml`

**Step 1: 创建生产环境docker-compose**
```yaml
# docker-compose.prod.yml
version: '3.8'

services:
  # Nacos
  nacos:
    image: nacos/nacos-server:v2.2.3
    environment:
      - MODE=standalone
      - SPRING_DATASOURCE_PLATFORM=mysql
      - MYSQL_SERVICE_HOST=mysql
      - MYSQL_SERVICE_DB_NAME=nacos
      - MYSQL_SERVICE_PORT=3306
      - MYSQL_SERVICE_USER=root
      - MYSQL_SERVICE_PASSWORD=${MYSQL_ROOT_PASSWORD}
    ports:
      - "8848:8848"
    volumes:
      - nacos_data:/home/nacos/data
    networks:
      - dazi-network

  # MySQL
  mysql:
    image: mysql:8.0
    environment:
      MYSQL_ROOT_PASSWORD: ${MYSQL_ROOT_PASSWORD}
      MYSQL_DATABASE: dazi_dating
    volumes:
      - mysql_data:/var/lib/mysql
      - ./sql/init.sql:/docker-entrypoint-initdb.d/init.sql
    ports:
      - "3306:3306"
    networks:
      - dazi-network

  # Redis
  redis:
    image: redis:7-alpine
    command: redis-server --appendonly yes --requirepass ${REDIS_PASSWORD}
    volumes:
      - redis_data:/data
    ports:
      - "6379:6379"
    networks:
      - dazi-network

  # RabbitMQ
  rabbitmq:
    image: rabbitmq:3-management
    environment:
      RABBITMQ_DEFAULT_USER: ${RABBITMQ_USER}
      RABBITMQ_DEFAULT_PASS: ${RABBITMQ_PASSWORD}
    ports:
      - "5672:5672"
      - "15672:15672"
    volumes:
      - rabbitmq_data:/var/lib/rabbitmq
    networks:
      - dazi-network

  # Gateway
  gateway:
    image: dazi-gateway:latest
    ports:
      - "8080:8080"
    environment:
      - SPRING_CLOUD_NACOS_DISCOVERY_SERVER_ADDR=nacos:8848
      - SPRING_CLOUD_NACOS_CONFIG_SERVER_ADDR=nacos:8848
    depends_on:
      - nacos
    networks:
      - dazi-network

  # User Service
  user-service:
    image: dazi-user-service:latest
    environment:
      - SPRING_CLOUD_NACOS_DISCOVERY_SERVER_ADDR=nacos:8848
      - SPRING_DATASOURCE_URL=jdbc:mysql://mysql:3306/dazi_dating
      - SPRING_REDIS_HOST=redis
      - SPRING_RABBITMQ_HOST=rabbitmq
    depends_on:
      - nacos
      - mysql
      - redis
      - rabbitmq
    networks:
      - dazi-network

  # Activity Service
  activity-service:
    image: dazi-activity-service:latest
    environment:
      - SPRING_CLOUD_NACOS_DISCOVERY_SERVER_ADDR=nacos:8848
      - SPRING_DATASOURCE_URL=jdbc:mysql://mysql:3306/dazi_dating
      - SPRING_REDIS_HOST=redis
      - SPRING_RABBITMQ_HOST=rabbitmq
    depends_on:
      - nacos
      - mysql
      - redis
      - rabbitmq
    networks:
      - dazi-network

  # Match Service
  match-service:
    image: dazi-match-service:latest
    environment:
      - SPRING_CLOUD_NACOS_DISCOVERY_SERVER_ADDR=nacos:8848
      - SPRING_DATASOURCE_URL=jdbc:mysql://mysql:3306/dazi_dating
      - SPRING_REDIS_HOST=redis
    depends_on:
      - nacos
      - mysql
      - redis
    networks:
      - dazi-network

  # Pay Service
  pay-service:
    image: dazi-pay-service:latest
    environment:
      - SPRING_CLOUD_NACOS_DISCOVERY_SERVER_ADDR=nacos:8848
      - SPRING_DATASOURCE_URL=jdbc:mysql://mysql:3306/dazi_dating
      - SPRING_REDIS_HOST=redis
      - SPRING_RABBITMQ_HOST=rabbitmq
    depends_on:
      - nacos
      - mysql
      - redis
      - rabbitmq
    networks:
      - dazi-network

volumes:
  nacos_data:
  mysql_data:
  redis_data:
  rabbitmq_data:

networks:
  dazi-network:
    driver: bridge
```

**Step 2: 创建Jenkinsfile**
```groovy
pipeline {
    agent any
    
    environment {
        DOCKER_REGISTRY = 'your-registry.com'
        IMAGE_TAG = "${BUILD_NUMBER}"
    }
    
    stages {
        stage('Build') {
            steps {
                sh 'mvn clean package -DskipTests'
            }
        }
        
        stage('Test') {
            steps {
                sh 'mvn test'
            }
        }
        
        stage('Build Docker Images') {
            steps {
                script {
                    def services = ['gateway', 'user-service', 'activity-service', 'match-service', 'pay-service']
                    services.each { service ->
                        sh """
                            docker build -t ${DOCKER_REGISTRY}/dazi-${service}:${IMAGE_TAG} \
                                -t ${DOCKER_REGISTRY}/dazi-${service}:latest \
                                ./dazi-${service}
                        """
                    }
                }
            }
        }
        
        stage('Push Docker Images') {
            steps {
                script {
                    def services = ['gateway', 'user-service', 'activity-service', 'match-service', 'pay-service']
                    services.each { service ->
                        sh """
                            docker push ${DOCKER_REGISTRY}/dazi-${service}:${IMAGE_TAG}
                            docker push ${DOCKER_REGISTRY}/dazi-${service}:latest
                        """
                    }
                }
            }
        }
        
        stage('Deploy') {
            steps {
                sh '''
                    docker-compose -f docker-compose.prod.yml down
                    docker-compose -f docker-compose.prod.yml up -d
                '''
            }
        }
    }
}
```

**Step 3: 提交代码**
```bash
git add .
git commit -m "deploy: 配置Spring Cloud Alibaba生产环境部署"
```

---

## 项目时间线

| 阶段 | 时间 | 任务 |
|------|------|------|
| Phase 1 | Day 1-3 | 项目初始化与基础架构（uni-app + Spring Cloud Alibaba） |
| Phase 2 | Day 3-5 | 后端微服务搭建（Nacos + Gateway + 服务注册） |
| Phase 3 | Day 5-8 | 用户系统开发（微信登录 + 实名认证 + 信用分） |
| Phase 4 | Day 8-12 | 匹配系统 + 活动系统开发 |
| Phase 5 | Day 12-16 | 前端页面开发（uview-plus） |
| Phase 6 | Day 16-20 | 测试与优化（单元测试 + 集成测试 + 性能测试 + 安全测试） |
| Phase 7 | Day 20-23 | 部署上线（Docker + Jenkins CI/CD） |
| Phase 8 | Day 23-28 | 缓冲时间（问题修复 + 性能优化） |

**总计：28天**

## 技术栈总结

### 前端
- **框架**: uni-app (Vue3 + TypeScript)
- **UI库**: uview-plus
- **状态管理**: Pinia
- **HTTP客户端**: axios

### 后端
- **语言**: Java 17
- **框架**: Spring Boot 3.2 + Spring Cloud Alibaba 2022
- **微服务组件**: 
  - Nacos（服务注册与配置中心）
  - Gateway（API网关）
  - Sentinel（限流熔断）
  - Seata（分布式事务）
- **数据库**: MySQL 8.0
- **缓存**: Redis 7
- **消息队列**: RabbitMQ
- **ORM**: MyBatis-Plus

### 基础设施
- **容器化**: Docker + Docker Compose
- **CI/CD**: Jenkins
- **云服务**: 阿里云（ECS + RDS + OSS）
- **监控**: Prometheus + Grafana（可选）

---

## 下一步

1. 确认实施计划
2. 使用 `executing-plans` 技能开始开发
3. 按照任务顺序逐步实施

**老板，实施计划已完成！是否开始开发？**

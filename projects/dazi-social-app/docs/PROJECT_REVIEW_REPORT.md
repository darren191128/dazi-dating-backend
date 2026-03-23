# 搭子交友微信小程序 - 上线前审查报告

**审查日期**: 2026-03-23  
**审查人**: 云壹  
**项目状态**: 🔴 **不建议立即上线** - 存在关键问题需修复

---

## 一、审查概览

| 审查维度 | 状态 | 严重问题数 | 一般问题数 |
|---------|------|-----------|-----------|
| 代码质量 | 🟡 一般 | 0 | 5 |
| 安全漏洞 | 🔴 严重 | 3 | 2 |
| 性能优化 | 🟡 一般 | 0 | 3 |
| 接口一致性 | 🔴 严重 | 2 | 4 |
| 数据库设计 | 🟢 良好 | 0 | 1 |
| 部署配置 | 🟡 一般 | 0 | 2 |

---

## 二、🔴 严重问题（必须修复）

### 2.1 安全漏洞

#### ❌ 问题1: JWT密钥硬编码
**位置**: `backend/user-service/src/main/resources/application.yml`
```yaml
jwt:
  secret: dazi-social-app-secret-key-2024  # 硬编码密钥
```
**风险**: 密钥泄露导致任意用户可伪造token  
**修复**: 使用环境变量或配置中心注入

#### ❌ 问题2: 数据库密码明文存储
**位置**: `backend/*/src/main/resources/application.yml`
```yaml
spring:
  datasource:
    password: 123456  # 明文密码
```
**风险**: 密码泄露导致数据库被入侵  
**修复**: 使用环境变量或密钥管理服务

#### ❌ 问题3: 缺少接口鉴权
**位置**: 所有Controller
**现象**: 所有接口均未验证JWT token，可直接访问  
**风险**: 未授权访问、数据泄露  
**修复**: 添加JWT过滤器验证token

### 2.2 接口不一致

#### ❌ 问题4: 前后端接口路径不匹配
| 前端调用 | 后端接口 | 状态 |
|---------|---------|------|
| POST /user/wxLogin | POST /user/login/wx | ❌ 不匹配 |
| GET /user/info | GET /user/info/{userId} | ❌ 不匹配 |
| GET /match/nearby | GET /match/nearby | ✅ 匹配 |
| GET /match/recommendations | POST /match/recommend | ❌ 不匹配 |

#### ❌ 问题5: 请求参数格式不一致
**前端期望**:
```javascript
matchApi.getNearbyUsers({
  longitude,
  latitude,
  page,
  pageSize
})
```

**后端实际**:
```java
@GetMapping("/nearby")
public Result<List<Map<String, Object>>> getNearbyUsers(
    @RequestParam BigDecimal longitude,
    @RequestParam BigDecimal latitude,
    @RequestParam(defaultValue = "10.0") Double radiusKm)
```
**问题**: 后端缺少page/pageSize参数，前端无法分页

---

## 三、🟡 一般问题（建议修复）

### 3.1 代码质量问题

#### ⚠️ 问题6: 缺少参数校验
**位置**: 所有Controller接口  
**现象**: 未使用@Valid进行参数校验，可能接收到非法数据  
**修复**: 添加JSR-303校验注解

#### ⚠️ 问题7: 异常处理不完善
**位置**: Service层  
**现象**: 部分异常未捕获，直接抛出  
**修复**: 完善GlobalExceptionHandler

#### ⚠️ 问题8: 日志记录不规范
**位置**: 部分Service  
**现象**: 敏感信息可能记录在日志中  
**修复**: 审查日志内容，脱敏处理

### 3.2 性能问题

#### ⚠️ 问题9: 附近的人查询未实现
**位置**: `MatchService.getNearbyUsers()`
```java
// 简化实现：实际应该使用Redis Geo或Elasticsearch
return Result.success(new ArrayList<>());
```
**影响**: 核心功能无法使用  
**修复**: 实现基于Redis Geo的附近查询

#### ⚠️ 问题10: 消息表缺少索引
**位置**: `message`表  
**现象**: 按用户查询消息时可能全表扫描  
**修复**: 添加(sender_id, receiver_id)复合索引

#### ⚠️ 问题11: 分页实现有问题
**位置**: `ActivityService.getActivityList()`
```java
// 内存分页，数据量大时性能差
return Result.success(activities.subList(start, end));
```
**修复**: 使用MyBatis Plus分页插件

### 3.3 其他问题

#### ⚠️ 问题12: 缺少健康检查接口
**影响**: 无法监控服务状态  
**修复**: 添加/actuator/health端点

#### ⚠️ 问题13: 配置文件缺少生产环境配置
**位置**: 所有application.yml  
**现象**: 只有开发环境配置  
**修复**: 添加application-prod.yml

---

## 四、🟢 良好实践

✅ 统一返回结果封装 (Result)  
✅ 使用Lombok简化代码  
✅ 数据库设计规范  
✅ 使用MyBatis Plus简化CRUD  
✅ 微服务架构设计合理  
✅ 前端API封装规范  

---

## 五、上线前必须完成清单

### 5.1 安全加固
- [ ] JWT密钥改为环境变量注入
- [ ] 数据库密码改为环境变量注入
- [ ] 添加JWT鉴权过滤器
- [ ] 添加接口防刷限制
- [ ] 敏感数据加密存储

### 5.2 接口对齐
- [ ] 统一前后端接口路径
- [ ] 统一请求参数格式
- [ ] 统一返回数据结构
- [ ] 补充缺失的分页参数

### 5.3 功能完善
- [ ] 实现附近的人查询（Redis Geo）
- [ ] 完善消息已读状态同步
- [ ] 添加支付回调签名验证

### 5.4 性能优化
- [ ] 数据库索引优化
- [ ] 分页改为数据库分页
- [ ] 添加Redis缓存

### 5.5 运维准备
- [ ] 添加健康检查接口
- [ ] 配置生产环境参数
- [ ] 完善日志配置
- [ ] 准备部署脚本

---

## 六、修复建议优先级

### P0（阻塞上线）
1. 修复JWT密钥硬编码
2. 修复数据库密码明文
3. 添加接口鉴权
4. 对齐前后端接口

### P1（一周内修复）
5. 实现附近的人查询
6. 添加参数校验
7. 优化分页实现

### P2（一个月内修复）
8. 添加Redis缓存
9. 完善监控告警
10. 性能压测

---

## 七、结论

**当前项目状态**: 🔴 **不建议上线**

**主要原因**:
1. 存在严重安全漏洞（密钥硬编码、无鉴权）
2. 前后端接口不匹配，无法正常调用
3. 核心功能（附近的人）未实现

**建议**:
- 完成P0级别修复后方可上线
- 建议进行安全渗透测试
- 建议进行压力测试

**预计修复时间**: 3-5个工作日

---

**报告生成时间**: 2026-03-23 18:15  
**下次审查时间**: 修复完成后

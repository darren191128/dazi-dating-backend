# 飞书群组角色路由使用说明

## 📋 配置说明

配置文件：`config/feishu-routes.yaml`

### 当前路由映射

| 飞书群组 | 角色 | 模型 | 职责 |
|---------|------|------|------|
| oc_product | 产品经理 | kimi-k2.5 | 需求分析、产品规划 |
| oc_design | 设计经理 | kimi-k2.5 | UI/UX设计、视觉创作 |
| oc_frontend | 前端经理 | kimi-k2.5 | 前端开发、组件设计 |
| oc_backend | 后端经理 | kimi-k2.5 | 架构设计、API开发 |
| oc_qa | 测试经理 | kimi-k2.5 | 测试用例、质量保障 |
| 其他群组 | CEO助理 (云壹) | kimi-k2.5 | 统筹一切 |

## 🔧 使用方法

### 1. 配置飞书群组白名单

需要在 `openclaw.json` 中添加群组到 `groupAllowFrom`：

```json
"channels": {
  "feishu": {
    "enabled": true,
    "appId": "cli_a9249470163a1cd3",
    "appSecret": "...",
    "domain": "feishu",
    "connectionMode": "websocket",
    "dmPolicy": "pairing",
    "groupPolicy": "allowlist",
    "allowFrom": ["ou_b49e8f0d35ef61c3edde32792119a1d3"],
    "groupAllowFrom": ["oc_product", "oc_design", "oc_frontend", "oc_backend", "oc_qa"]
  }
}
```

### 2. 获取真实群组 chat_id

在飞书中发送消息，查看日志获取真实的 `chat_id`，然后更新路由配置。

### 3. 测试路由

发送消息到不同群组，系统会自动：
1. 识别群组 chat_id
2. 查找对应角色
3. 创建该角色的子代理
4. 子代理处理消息后回复

## 📝 添加新群组

编辑 `config/feishu-routes.yaml`：

```yaml
routes:
  - chat_id: "oc_newgroup"  # 飞书群组ID
    role: "运维经理"         # 角色名称
    description: "系统运维、监控告警"  # 角色描述
    model: "moonshot/kimi-k2.5"  # 使用模型
```

## 🔄 工作流程

```
飞书群组消息
    ↓
主代理 (云壹) 接收
    ↓
识别 chat_id → 查找路由配置
    ↓
创建对应角色子代理
    ↓
子代理处理任务
    ↓
返回结果 → 主代理回复飞书
```

## ⚠️ 注意事项

1. **群组需要先添加到白名单** - 否则消息会被丢弃
2. **chat_id 需要真实有效** - 从飞书日志中获取
3. **子代理会自动清理** - 任务完成后子代理退出
4. **保持会话可选** - 如需连续对话，可将 mode 改为 "session"

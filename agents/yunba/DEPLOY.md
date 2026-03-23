# 云捌 Bot 部署指南

## 架构

```
飞书用户消息 → 飞书开放平台 → 云捌 Bot (server.js)
                      ↓
              处理 (GLM-4.7)
                      ↓
              回复用户 + 抄送云壹
```

## 部署步骤

### 1. 创建飞书自建应用

1. 访问 [飞书开放平台](https://open.feishu.cn/)
2. 创建企业自建应用
3. 应用名称: **云捌**
4. 应用描述: 项目与产品负责人

### 2. 配置应用权限

在"权限管理"中添加：
- `im:chat:readonly` (读取群信息)
- `im:message:send` (发送消息)
- `im:message.group_msg` (接收群消息)
- `im:message.p2p_msg` (接收单聊消息)

### 3. 配置事件订阅

在"事件订阅"中：
- 请求地址: `http://your-server:3001/webhook`
- 订阅事件:
  - `im.message.receive_v1` (接收消息)

### 4. 发布应用

1. 在"版本管理与发布"中创建版本
2. 申请发布
3. 管理员审批

### 5. 服务器部署

```bash
cd /root/.openclaw/workspace/agents/yunba

# 安装依赖
npm install

# 配置环境变量
cp .env.example .env
# 编辑 .env 填入 FEISHU_APP_ID 和 FEISHU_APP_SECRET

# 启动服务
npm start
```

### 6. 使用 PM2 守护进程

```bash
npm install -g pm2
pm2 start server.js --name yunba-bot
pm2 save
pm2 startup
```

## 测试

```bash
# 本地测试 Bot 逻辑
npm test

# 测试 webhook
curl -X POST http://localhost:3001/webhook \
  -H "Content-Type: application/json" \
  -d '{
    "challenge": "test"
  }'
```

## 功能验证

1. 在飞书添加云捌 Bot
2. 发送消息测试
3. 检查云壹是否收到抄送

## 故障排查

| 问题 | 排查 |
|------|------|
| 收不到消息 | 检查事件订阅 URL 是否可访问 |
| 无法回复 | 检查 im:message:send 权限 |
| 云壹未收到 | 检查 YUNYI_USER_ID 是否正确 |

## 联系

配置问题请联系云壹 (CEO)

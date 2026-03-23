# 飞书应用事件订阅配置指南

## 问题说明

飞书应用需要配置**事件订阅请求URL**，飞书服务器才能将消息推送到你的服务器。

错误信息：
> 未检测到应用连接信息，请确保长连接建立成功后再保存配置

## 解决方案

### 方案1：使用内网穿透（推荐测试用）

如果你本地开发测试，可以使用内网穿透工具：

#### 使用 ngrok
```bash
# 安装 ngrok
npm install -g ngrok

# 启动 ngrok，将本地端口映射到公网
# 假设 OpenClaw 运行在 3000 端口
ngrok http 3000

# 会输出类似：
# Forwarding: https://abc123.ngrok.io -> http://localhost:3000
```

#### 配置飞书应用
1. 在飞书开放平台 → 你的应用 → 事件订阅
2. 请求地址填写：`https://abc123.ngrok.io/webhook/feishu`
3. 点击「保存」

---

### 方案2：使用服务器公网IP（生产环境）

如果你的服务器有公网IP：

#### 配置飞书应用
1. 事件订阅 → 请求地址
2. 填写：`http://你的服务器IP:3000/webhook/feishu`
3. 点击「保存」

#### 配置防火墙
```bash
# 开放 3000 端口
ufw allow 3000
```

---

### 方案3：使用 OpenClaw 内置的 Webhook 接收

OpenClaw 默认监听端口可以在配置中指定：

```yaml
# config.yaml
server:
  port: 3000
  host: "0.0.0.0"

plugins:
  feishu:
    enabled: true
    webhookPath: "/webhook/feishu"
```

---

## 配置步骤

### Step 1: 启动 OpenClaw

```bash
# 先启动一个实例测试
openclaw --config ~/.openclaw/yuner/config.yaml gateway start

# 查看监听端口
# 默认是 3000，如果占用会自动递增
```

### Step 2: 配置内网穿透（如需要）

```bash
ngrok http 3000
```

### Step 3: 配置飞书应用事件订阅

1. 登录 https://open.feishu.cn/
2. 进入你的应用 → 「事件订阅」
3. 填写请求地址：
   - 使用 ngrok: `https://xxx.ngrok.io/webhook/feishu`
   - 使用公网IP: `http://你的IP:3000/webhook/feishu`
4. 点击「保存」

### Step 4: 验证连接

如果配置正确，飞书会显示：
> ✅ 连接成功

---

## 为7个应用配置事件订阅

每个应用都需要独立的事件订阅URL：

| 应用 | 本地端口 | ngrok URL | 飞书配置URL |
|------|---------|-----------|-------------|
| 云贰-PM | 3000 | https://a.ngrok.io | https://a.ngrok.io/webhook/feishu |
| 云叁-PD | 3001 | https://b.ngrok.io | https://b.ngrok.io/webhook/feishu |
| 云肆-UI | 3002 | https://c.ngrok.io | https://c.ngrok.io/webhook/feishu |
| 云伍-FE | 3003 | https://d.ngrok.io | https://d.ngrok.io/webhook/feishu |
| 云陆-BE | 3004 | https://e.ngrok.io | https://e.ngrok.io/webhook/feishu |
| 云柒-QA | 3005 | https://f.ngrok.io | https://f.ngrok.io/webhook/feishu |

**注意：** 每个应用需要独立的 ngrok 隧道（或独立端口）

---

## 简化方案：使用一个OpenClaw实例

如果不想配置7个独立实例，可以：

1. 只启动 **云壹-CEO** 一个实例
2. CEO 内部通过 `sessions_spawn` 调用其他角色
3. 所有消息都通过 CEO 中转

这样只需要配置一个飞书应用（云壹）的事件订阅。

---

## 推荐配置流程

### 测试阶段（现在）
1. 安装 ngrok
2. 启动云贰：`openclaw --config ~/.openclaw/yuner/config.yaml gateway start`
3. 启动 ngrok：`ngrok http 3000`
4. 复制 ngrok URL 到飞书应用事件订阅
5. 测试通过后，依次配置其他6个应用

### 生产阶段
1. 使用有公网IP的服务器
2. 配置域名 + HTTPS
3. 使用 Docker 部署 7 个 OpenClaw 实例
4. 配置反向代理（Nginx）

---

## 快速测试命令

```bash
# 1. 安装 ngrok
npm install -g ngrok

# 2. 启动云贰
openclaw --config ~/.openclaw/yuner/config.yaml gateway start &

# 3. 启动 ngrok（新开终端）
ngrok http 3000

# 4. 复制 https URL 到飞书应用配置
```

---

需要帮助配置 ngrok 或启动服务吗？

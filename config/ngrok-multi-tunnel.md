# ngrok 配置指南（7个实例）

## 问题

ngrok 免费版一次只能创建一个隧道，无法同时为7个实例提供公网访问。

## 解决方案

### 方案1：使用 ngrok 付费版（推荐生产环境）
- ngrok Pro 支持多隧道
- 价格：$8/月

### 方案2：使用其他内网穿透工具

#### 方案2.1：使用 localtunnel（免费，多实例）
```bash
# 安装 localtunnel
npm install -g localtunnel

# 启动7个隧道（分别在不同终端）
lt --port 3000 --subdomain yunyi-ceo
lt --port 3001 --subdomain yuner-pm
lt --port 3002 --subdomain yunsan-pd
lt --port 3003 --subdomain yunsi-ui
lt --port 3004 --subdomain yunwu-fe
lt --port 3005 --subdomain yunlu-be
lt --port 3006 --subdomain yunqi-qa
```

#### 方案2.2：使用 cloudflared tunnel（推荐）
```bash
# 安装 cloudflared
wget https://github.com/cloudflare/cloudflared/releases/latest/download/cloudflared-linux-amd64
chmod +x cloudflared
sudo mv cloudflared /usr/local/bin/

# 登录 Cloudflare
cloudflared tunnel login

# 创建7个隧道
cloudflared tunnel create yunyi-ceo
cloudflared tunnel create yuner-pm
# ... 以此类推

# 配置并启动隧道
```

#### 方案2.3：使用 frp（自建服务器）
如果你有公网服务器，可以使用 frp 自建内网穿透。

### 方案3：使用服务器公网IP（最简单）

如果你的服务器有公网IP，直接开放端口即可：

```bash
# 开放 3000-3006 端口
ufw allow 3000:3006/tcp

# 飞书配置URL：
# http://你的服务器IP:3000/webhook/feishu  (云壹)
# http://你的服务器IP:3001/webhook/feishu  (云贰)
# ...
```

---

## 推荐方案

### 测试阶段：使用 localtunnel

```bash
# 1. 安装
npm install -g localtunnel

# 2. 启动 OpenClaw 7个实例
./scripts/start-team-ports.sh

# 3. 启动7个 localtunnel（在7个不同终端）
lt --port 3000 --subdomain yunyi-ceo-xxx
lt --port 3001 --subdomain yuner-pm-xxx
# ...

# 4. 将生成的URL配置到飞书应用
```

### 生产阶段：使用服务器公网IP

直接开放端口，无需内网穿透工具。

---

## 快速启动脚本

```bash
# 1. 安装 localtunnel
npm install -g localtunnel

# 2. 启动7个 OpenClaw 实例
./scripts/start-team-ports.sh

# 3. 等待服务启动
sleep 5

# 4. 启动 localtunnel（在新终端窗口执行）
echo "请在7个新终端中分别执行："
echo "lt --port 3000"
echo "lt --port 3001"
echo "lt --port 3002"
echo "lt --port 3003"
echo "lt --port 3004"
echo "lt --port 3005"
echo "lt --port 3006"
```

---

## 配置检查清单

- [ ] 安装 localtunnel: `npm install -g localtunnel`
- [ ] 启动7个 OpenClaw 实例: `./scripts/start-team-ports.sh`
- [ ] 启动7个 localtunnel 隧道
- [ ] 复制每个隧道URL到对应飞书应用的事件订阅配置
- [ ] 测试每个机器人是否回复消息

---

需要帮助启动服务吗？

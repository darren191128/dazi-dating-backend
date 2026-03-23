# 七人智能体团队 - 快速启动指南

## 🚀 快速开始

### 1. 创建飞书应用

在 [飞书开发者后台](https://open.feishu.cn/app) 创建7个企业自建应用：

| 应用名称 | 角色 | 用途 |
|---------|------|------|
| 云壹-CEO助理 | CEO助理 | 统筹管理 |
| 云贰-项目经理 | 项目经理 | 项目管理群 |
| 云叁-产品经理 | 产品经理 | 产品需求群 |
| 云肆-UI设计师 | UI设计师 | 设计评审群 |
| 云伍-前端工程师 | 前端工程师 | 前端开发群 |
| 云陆-后端工程师 | 后端工程师 | 后端开发群 |
| 云柒-测试工程师 | 测试工程师 | 测试质量群 |

每个应用需要：
- ✅ 启用机器人功能
- ✅ 订阅事件：`im.message.receive_v1`
- ✅ 权限：获取与发送单聊、群组消息

### 2. 配置环境变量

```bash
cd /root/.openclaw/workspace

# 复制模板
cp config/.env.template config/.env

# 编辑填入凭证
nano config/.env
```

填入每个应用的 App ID 和 App Secret：

```env
# 云壹 (已有)
FEISHU_YUNYI_APP_ID=cli_a9249470163a1cd3
FEISHU_YUNYI_APP_SECRET=7BT9O2aTu2YLts9V84Md5bBTiqludlza

# 云贰 (新应用)
FEISHU_YUNER_APP_ID=cli_xxxxxxxxxxxxxxxx
FEISHU_YUNER_APP_SECRET=xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx

# ... 其他角色
```

### 3. 重新生成配置

```bash
python3 utils/agent_launcher.py all
```

### 4. 启动智能体

```bash
# 启动所有智能体
./scripts/start-agents.sh all

# 或单独启动某个智能体
./scripts/start-agents.sh 云贰

# 查看状态
./scripts/start-agents.sh status

# 停止所有智能体
./scripts/start-agents.sh stop
```

## 📁 目录结构

```
/root/.openclaw/
├── agents/
│   ├── 云壹/openclaw.json      # 云壹配置
│   ├── 云贰/openclaw.json      # 云贰配置
│   ├── 云叁/openclaw.json      # 云叁配置
│   ├── 云肆/openclaw.json      # 云肆配置
│   ├── 云伍/openclaw.json      # 云伍配置
│   ├── 云陆/openclaw.json      # 云陆配置
│   └── 云柒/openclaw.json      # 云柒配置
└── workspace/
    ├── agents/
    │   ├── 云壹/               # 云壹工作目录
    │   ├── 云贰/               # 云贰工作目录
    │   └── ...
    ├── config/
    │   ├── agents-feishu.yaml  # 智能体配置
    │   └── .env                # 环境变量（需创建）
    ├── scripts/
    │   └── start-agents.sh     # 启动脚本
    └── utils/
        └── agent_launcher.py   # 配置生成器
```

## 🔧 管理命令

### 查看单个智能体状态

```bash
# 云壹
openclaw --config /root/.openclaw/agents/云壹 status

# 云贰
openclaw --config /root/.openclaw/agents/云贰 status
```

### 重启单个智能体

```bash
# 停止
pkill -f "openclaw.*云贰"

# 启动
OPENCLAW_CONFIG_DIR=/root/.openclaw/agents/云贰 openclaw gateway start
```

## 🎯 使用方式

### 群组绑定

将每个机器人邀请加入对应的群组：

| 群组 | 机器人 | 角色 |
|------|--------|------|
| 项目管理群 | @云贰-项目经理 | 项目经理 |
| 产品需求群 | @云叁-产品经理 | 产品经理 |
| 设计评审群 | @云肆-UI设计师 | UI设计师 |
| 前端开发群 | @云伍-前端工程师 | 前端工程师 |
| 后端开发群 | @云陆-后端工程师 | 后端工程师 |
| 测试质量群 | @云柒-测试工程师 | 测试工程师 |
| 综合群 | @云壹-CEO助理 | CEO助理 |

### 消息交互

在群组中 @对应的机器人：

```
@云叁-产品经理 这个需求需要重新评估一下
    ↓
云叁 (独立实例) 接收并处理
    ↓
以产品经理身份回复
```

## 📊 端口分配

| 智能体 | 端口 | 状态检查 |
|--------|------|---------|
| 云壹 | 18789 | `curl http://127.0.0.1:18789/__openclaw__/health` |
| 云贰 | 18790 | `curl http://127.0.0.1:18790/__openclaw__/health` |
| 云叁 | 18791 | `curl http://127.0.0.1:18791/__openclaw__/health` |
| 云肆 | 18792 | `curl http://127.0.0.1:18792/__openclaw__/health` |
| 云伍 | 18793 | `curl http://127.0.0.1:18793/__openclaw__/health` |
| 云陆 | 18794 | `curl http://127.0.0.1:18794/__openclaw__/health` |
| 云柒 | 18795 | `curl http://127.0.0.1:18795/__openclaw__/health` |

## ⚠️ 注意事项

1. **端口冲突** - 确保 18789-18795 端口未被占用
2. **内存占用** - 7个实例同时运行约需 2-4GB 内存
3. **飞书应用** - 每个应用需要单独发布和审核
4. **环境变量** - 修改 `.env` 后需要重新生成配置

## 🆘 故障排查

### 智能体无法启动

```bash
# 检查配置
openclaw --config /root/.openclaw/agents/云贰 doctor

# 查看日志
tail -f /tmp/openclaw/openclaw-*.log
```

### 飞书消息未接收

1. 检查应用是否已发布
2. 检查机器人是否在群组中
3. 检查事件订阅配置
4. 检查环境变量是否正确

### 端口被占用

```bash
# 查看端口占用
lsof -i :18790

# 释放端口
kill -9 <PID>
```

# 云字辈7人团队 - 飞书配置指南

## 一、需要创建的飞书应用（7个）

### 1. 云壹-CEO
- **应用名称**: 云壹-CEO
- **应用描述**: CEO统筹决策机器人
- **机器人类型**: 企业自建应用

### 2. 云贰-项目经理
- **应用名称**: 云贰-PM
- **应用描述**: 项目管理机器人

### 3. 云叁-产品经理
- **应用名称**: 云叁-PD
- **应用描述**: 产品需求分析机器人

### 4. 云肆-UI设计师
- **应用名称**: 云肆-UI
- **应用描述**: UI设计机器人

### 5. 云伍-前端工程师
- **应用名称**: 云伍-FE
- **应用描述**: 前端开发机器人

### 6. 云陆-后端工程师
- **应用名称**: 云陆-BE
- **应用描述**: 后端开发机器人

### 7. 云柒-测试工程师
- **应用名称**: 云柒-QA
- **应用描述**: 质量保障机器人

---

## 二、飞书应用创建步骤

### Step 1: 登录飞书开放平台
1. 访问 https://open.feishu.cn/
2. 使用企业管理员账号登录

### Step 2: 创建企业自建应用
1. 点击「开发者后台」
2. 点击「创建企业自建应用」
3. 填写应用名称（如：云壹-CEO）
4. 选择应用类型：「机器人」
5. 点击「创建应用」

### Step 3: 获取凭证
1. 进入应用详情页
2. 点击「凭证与基础信息」
3. 记录以下信息：
   - App ID (cli_xxxxxxxxxx)
   - App Secret

### Step 4: 启用机器人能力
1. 点击「机器人」菜单
2. 开启「启用机器人」开关
3. 配置机器人信息：
   - 机器人名称：云壹
   - 机器人头像：上传
   - 描述：CEO统筹决策

### Step 5: 配置权限
1. 点击「权限管理」
2. 添加以下权限：
   - `im:chat:readonly` (读取群组信息)
   - `im:message:send` (发送消息)
   - `im:message:receive` (接收消息)
   - `im:chat` (群组管理)

### Step 6: 发布应用
1. 点击「版本管理与发布」
2. 点击「创建版本」
3. 填写版本信息
4. 点击「申请发布」
5. 等待管理员审核通过

---

## 三、需要创建的飞书群组

| 群组名称 | 成员 | 用途 |
|----------|------|------|
| CEO决策群 | 云壹、老板 | 战略决策、任务下发 |
| 项目管理群 | 云贰、云壹 | 项目计划、进度管理 |
| 产品需求群 | 云叁、云贰 | 需求分析、PRD评审 |
| 设计评审群 | 云肆、云叁 | UI设计、交互评审 |
| 前端开发群 | 云伍、云肆 | 前端开发、组件库 |
| 后端开发群 | 云陆、云伍 | 后端开发、API设计 |
| 质量保障群 | 云柒、云陆 | 测试执行、质量报告 |

---

## 四、群组创建和机器人邀请步骤

### 创建群组
1. 在飞书客户端点击「+」→「创建群组」
2. 设置群组名称（如：CEO决策群）
3. 设置群组头像
4. 点击「创建」

### 邀请机器人入群
1. 进入群组聊天界面
2. 点击右上角「...」→「群设置」
3. 点击「群机器人」→「添加机器人」
4. 搜索并选择对应的机器人（如：云壹-CEO）
5. 点击「添加」

### 获取群组ID
1. 在群组中发送任意消息
2. 在OpenClaw日志中查看群组ID
3. 或使用飞书API获取

---

## 五、OpenClaw配置

### 配置文件位置
```
~/.openclaw/
├── yunyi/           # 云壹-CEO
│   └── config.yaml
├── yuner/           # 云贰-PM
│   └── config.yaml
├── yunsan/          # 云叁-PD
│   └── config.yaml
├── yunsi/           # 云肆-UI
│   └── config.yaml
├── yunwu/           # 云伍-FE
│   └── config.yaml
├── yunlu/           # 云陆-BE
│   └── config.yaml
└── yunqi/           # 云柒-QA
    └── config.yaml
```

### 配置模板

每个config.yaml内容：
```yaml
plugins:
  feishu:
    enabled: true
    appId: "cli_xxxxxxxxxxxx"      # 替换为实际App ID
    appSecret: "xxxxxxxxxx"         # 替换为实际App Secret
    encryptKey: ""                  # 可选
    verificationToken: ""           # 可选

agents:
  default: "yunyi"                  # 角色名称
  
models:
  default: "moonshot/kimi-k2.5"     # 默认模型
```

---

## 六、配置清单

### 需要记录的信息

| 角色 | App ID | App Secret | 群组ID | 状态 |
|------|--------|------------|--------|------|
| 云壹-CEO | cli_xxx | xxx | xxx | ⬜ |
| 云贰-PM | cli_xxx | xxx | xxx | ⬜ |
| 云叁-PD | cli_xxx | xxx | xxx | ⬜ |
| 云肆-UI | cli_xxx | xxx | xxx | ⬜ |
| 云伍-FE | cli_xxx | xxx | xxx | ⬜ |
| 云陆-BE | cli_xxx | xxx | xxx | ⬜ |
| 云柒-QA | cli_xxx | xxx | xxx | ⬜ |

---

## 七、启动命令

```bash
# 启动7个OpenClaw实例
openclaw --config ~/.openclaw/yunyi/config.yaml gateway start
openclaw --config ~/.openclaw/yuner/config.yaml gateway start
openclaw --config ~/.openclaw/yunsan/config.yaml gateway start
openclaw --config ~/.openclaw/yunsi/config.yaml gateway start
openclaw --config ~/.openclaw/yunwu/config.yaml gateway start
openclaw --config ~/.openclaw/yunlu/config.yaml gateway start
openclaw --config ~/.openclaw/yunqi/config.yaml gateway start
```

或使用脚本：
```bash
./scripts/start-team.sh
```

---

## 八、验证步骤

1. 在CEO决策群发送消息："测试"
2. 确认云壹机器人回复
3. 依次测试其他群组和机器人
4. 测试跨群组消息转发

---

## 九、故障排查

### 机器人不回复
1. 检查App ID和App Secret是否正确
2. 检查机器人是否已启用
3. 检查权限是否配置完整
4. 查看OpenClaw日志

### 消息转发失败
1. 检查群组ID是否正确
2. 检查机器人是否在目标群组中
3. 检查网络连接

---

**完成以上步骤后，云字辈7人团队即可开始协同工作！**

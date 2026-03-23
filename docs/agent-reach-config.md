# Agent Reach 平台配置指南

## 配置状态总览

| 平台 | 状态 | 说明 |
|------|------|------|
| 🌐 **任意网页** | ✅ 已配置 | Jina Reader 可用 |
| 🔍 **全网搜索 (Exa)** | ✅ 已配置 | mcporter 已添加 |
| 📺 **YouTube** | ✅ 已配置 | JS Runtime 已设置 |
| 📺 **B站** | ✅ 可用 | 无需额外配置 |
| 📡 **RSS** | ✅ 可用 | 无需额外配置 |
| 📦 **GitHub** | ⚠️ 需手动认证 | 运行 `gh auth login` |
| 📕 **小红书** | ❌ 需 Docker | Docker 安装失败，需手动安装 |
| 🎵 **抖音** | ✅ 已配置 | MCP 已添加 |
| 🐦 **Twitter/X** | ❌ 需安装 | 运行 `npm install -g xreach-cli` |
| 📖 **Reddit** | ❌ 需代理 | 服务器 IP 被封锁 |
| 💬 **微博** | ❌ 需配置 | 运行微博 MCP 安装命令 |
| 🎙️ **小宇宙** | ❌ 需 ffmpeg | 运行 `apt install ffmpeg` |

---

## 已完成的配置

### 1. ✅ 全网搜索 (Exa)
```bash
mcporter config add exa https://mcp.exa.ai/mcp
```
**状态**: 已配置，可以使用 `mcporter call 'exa.web_search_exa(query: "关键词")'`

### 2. ✅ YouTube
```bash
mkdir -p ~/.config/yt-dlp
echo '--js-runtimes node' >> ~/.config/yt-dlp/config
```
**状态**: 已配置，可以使用 `yt-dlp` 下载字幕

### 3. ✅ 抖音
```bash
pip install douyin-mcp-server --break-system-packages
mcporter config add douyin http://localhost:18070/mcp
```
**状态**: 已配置，需要启动服务后使用

---

## 需要手动完成的配置

### 1. GitHub 认证
```bash
gh auth login
```
**步骤**:
1. 运行 `gh auth login`
2. 选择 `GitHub.com`
3. 选择 `HTTPS`
4. 选择浏览器登录或粘贴 token

### 2. 小红书 (需要 Docker)
```bash
# 安装 Docker
curl -fsSL https://get.docker.com -o get-docker.sh
sh get-docker.sh

# 启动小红书 MCP
docker run -d --name xiaohongshu-mcp -p 18060:18060 xpzouying/xiaohongshu-mcp

# 配置 mcporter
mcporter config add xiaohongshu http://localhost:18060/mcp
```

### 3. Twitter/X
```bash
npm install -g xreach-cli
```

### 4. 微博
```bash
pip install git+https://github.com/Panniantong/mcp-server-weibo.git --break-system-packages
mcporter config add weibo --command 'mcp-server-weibo'
```

### 5. 小宇宙播客
```bash
apt install -y ffmpeg
```

---

## 使用方法

### 网页搜索
```bash
# 使用 Exa 搜索
mcporter call 'exa.web_search_exa(query: "OpenClaw AI", numResults: 5)'

# 使用 Jina Reader 读取网页
curl -s "https://r.jina.ai/https://example.com"
```

### YouTube
```bash
# 下载字幕
yt-dlp --write-sub --write-auto-sub --sub-lang "zh-Hans,zh,en" --skip-download "URL"

# 搜索视频
yt-dlp --dump-json "ytsearch5:关键词"
```

### B站
```bash
# 下载视频信息
yt-dlp --dump-json "https://www.bilibili.com/video/BVxxx"

# 下载字幕
yt-dlp --write-sub --write-auto-sub --sub-lang "zh-Hans,zh,en" --skip-download "URL"
```

### GitHub
```bash
# 搜索仓库
gh search repos "关键词" --sort stars --limit 10

# 查看仓库
gh repo view owner/repo
```

---

## 故障排除

### Exa 搜索不工作
- 检查 mcporter 配置：`mcporter config list`
- 重新添加：`mcporter config add exa https://mcp.exa.ai/mcp`

### YouTube 下载失败
- 检查 yt-dlp 版本：`yt-dlp --version`
- 更新 yt-dlp：`pip install -U yt-dlp --break-system-packages`
- 检查 JS runtime 配置：`cat ~/.config/yt-dlp/config`

### GitHub 认证失败
- 检查状态：`gh auth status`
- 重新登录：`gh auth login`
- 或使用 token：`gh auth login --with-token < token.txt`

### 小红书/抖音连接失败
- 检查服务是否运行：`docker ps` 或查看进程
- 检查端口：`netstat -tlnp | grep 18060`
- 重新配置 mcporter：`mcporter config add xiaohongshu http://localhost:18060/mcp`

---

## 当前可用功能

现在可以立即使用的功能：

1. ✅ **任意网页读取** - `curl https://r.jina.ai/URL`
2. ✅ **全网搜索** - `mcporter call 'exa.web_search_exa(...)'`
3. ✅ **YouTube 字幕** - `yt-dlp --write-sub ...`
4. ✅ **B站视频** - `yt-dlp ... bilibili.com`
5. ✅ **RSS 订阅** - `agent-reach rss URL`
6. ⚠️ **GitHub** - 需要认证后可用

---

## 配置完成时间

- Exa 搜索: ✅ 完成
- YouTube: ✅ 完成
- 抖音: ✅ 完成 (需启动服务)
- GitHub: ⏳ 等待手动认证
- 小红书: ⏳ 等待 Docker 安装
- 其他: ⏳ 按需配置

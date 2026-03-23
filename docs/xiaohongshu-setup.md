# Agent Reach - 小红书配置指南

## 当前状态

GitHub 已认证 ✅
Docker 已安装 ✅
Docker 服务未启动 ❌

## 启动 Docker 服务

请运行以下命令启动 Docker：

```bash
# 启动 Docker 服务
sudo systemctl start docker

# 验证 Docker 运行状态
sudo systemctl status docker

# 测试 Docker
sudo docker run hello-world
```

## 配置小红书 MCP

Docker 启动后，运行：

```bash
# 1. 启动小红书 MCP 容器
docker run -d --name xiaohongshu-mcp -p 18060:18060 xpzouying/xiaohongshu-mcp

# 2. 验证容器运行状态
docker ps | grep xiaohongshu

# 3. 配置 mcporter
mcporter config add xiaohongshu http://localhost:18060/mcp

# 4. 验证配置
mcporter list
```

## 使用方法

配置完成后，可以使用：

```bash
# 搜索小红书内容
mcporter call 'xiaohongshu.search_feeds(keyword: "关键词")'

# 获取笔记详情
mcporter call 'xiaohongshu.get_feed_detail(feed_id: "xxx", xsec_token: "yyy")'

# 发布内容（需登录）
mcporter call 'xiaohongshu.publish_content(title: "标题", content: "正文", images: ["/path/img.jpg"])'
```

## Cookie 配置

如需发布内容，需要配置 Cookie：

1. 浏览器登录小红书
2. 使用 Cookie-Editor 插件导出 Cookie
3. 将 Cookie 发送给 Agent 配置

## 故障排除

### Docker 无法启动
```bash
# 检查日志
sudo journalctl -u docker.service

# 重启服务
sudo systemctl restart docker
```

### 容器无法启动
```bash
# 查看容器日志
docker logs xiaohongshu-mcp

# 删除并重新创建
docker rm -f xiaohongshu-mcp
docker run -d --name xiaohongshu-mcp -p 18060:18060 xpzouying/xiaohongshu-mcp
```

### mcporter 连接失败
```bash
# 检查端口
netstat -tlnp | grep 18060

# 重新配置
mcporter config remove xiaohongshu
mcporter config add xiaohongshu http://localhost:18060/mcp
```

## 验证配置

全部配置完成后，运行：

```bash
agent-reach doctor
```

应该显示：
- 小红书笔记: ✅ 可用

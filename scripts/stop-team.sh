#!/bin/bash
# 云字辈7人团队停止脚本
# 停止所有OpenClaw实例

echo "🛑 停止云字辈7人团队..."

# 停止所有openclaw gateway进程
pkill -f "openclaw.*gateway start"

echo "✅ 云字辈7人团队已停止"

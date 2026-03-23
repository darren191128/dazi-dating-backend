#!/bin/bash
# 检查云字辈团队连接状态

echo "🔍 检查云字辈团队连接状态..."
echo ""

# 检查进程
for role in yuner yunsan yunsi yunwu yunlu yunqi; do
    pid=$(pgrep -f "openclaw.*$role" | head -1)
    if [ -n "$pid" ]; then
        echo "✅ $role: 运行中 (PID: $pid)"
    else
        echo "❌ $role: 未运行"
    fi
done

echo ""
echo "📊 查看日志（检查WebSocket连接）："
echo ""

# 显示最新日志
echo "云贰-PM:"
tail -5 ~/.openclaw/yuner/logs/*.log 2>/dev/null | grep -E "(websocket|connected|error)" || echo "  暂无连接日志"

echo ""
echo "云叁-PD:"
tail -5 ~/.openclaw/yunsan/logs/*.log 2>/dev/null | grep -E "(websocket|connected|error)" || echo "  暂无连接日志"

echo ""
echo "💡 如果显示 "connected" 或 "websocket"，说明长连接已建立"
echo "💡 然后可以在飞书平台启用「长连接接收事件」模式"

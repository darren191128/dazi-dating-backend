#!/bin/bash
# 启动云字辈7人团队 - 使用不同端口
# 注意：需要配合 ngrok 或其他内网穿透工具

echo "🚀 启动云字辈7人团队..."
echo ""

# 定义端口
YUNYI_PORT=3000
YUNER_PORT=3001
YUNSAN_PORT=3002
YUNSI_PORT=3003
YUNWU_PORT=3004
YUNLU_PORT=3005
YUNQI_PORT=3006

# 检查配置目录
for role in yunyi yuner yunsan yunsi yunwu yunlu yunqi; do
    if [ ! -d "$HOME/.openclaw/$role" ]; then
        echo "❌ 配置目录不存在: ~/.openclaw/$role"
        exit 1
    fi
done

echo "📋 端口分配："
echo "  云壹-CEO: $YUNYI_PORT"
echo "  云贰-PM:  $YUNER_PORT"
echo "  云叁-PD:  $YUNSAN_PORT"
echo "  云肆-UI:  $YUNSI_PORT"
echo "  云伍-FE:  $YUNWU_PORT"
echo "  云陆-BE:  $YUNLU_PORT"
echo "  云柒-QA:  $YUNQI_PORT"
echo ""

# 启动云壹-CEO
echo "▶️  启动 云壹-CEO (端口: $YUNYI_PORT)..."
OPENCLAW_PORT=$YUNYI_PORT openclaw --config "$HOME/.openclaw/yunyi/config.yaml" gateway start &
YUNYI_PID=$!
sleep 2

# 启动云贰-PM
echo "▶️  启动 云贰-PM (端口: $YUNER_PORT)..."
OPENCLAW_PORT=$YUNER_PORT openclaw --config "$HOME/.openclaw/yuner/config.yaml" gateway start &
YUNER_PID=$!
sleep 2

# 启动云叁-PD
echo "▶️  启动 云叁-PD (端口: $YUNSAN_PORT)..."
OPENCLAW_PORT=$YUNSAN_PORT openclaw --config "$HOME/.openclaw/yunsan/config.yaml" gateway start &
YUNSAN_PID=$!
sleep 2

# 启动云肆-UI
echo "▶️  启动 云肆-UI (端口: $YUNSI_PORT)..."
OPENCLAW_PORT=$YUNSI_PORT openclaw --config "$HOME/.openclaw/yunsi/config.yaml" gateway start &
YUNSI_PID=$!
sleep 2

# 启动云伍-FE
echo "▶️  启动 云伍-FE (端口: $YUNWU_PORT)..."
OPENCLAW_PORT=$YUNWU_PORT openclaw --config "$HOME/.openclaw/yunwu/config.yaml" gateway start &
YUNWU_PID=$!
sleep 2

# 启动云陆-BE
echo "▶️  启动 云陆-BE (端口: $YUNLU_PORT)..."
OPENCLAW_PORT=$YUNLU_PORT openclaw --config "$HOME/.openclaw/yunlu/config.yaml" gateway start &
YUNLU_PID=$!
sleep 2

# 启动云柒-QA
echo "▶️  启动 云柒-QA (端口: $YUNQI_PORT)..."
OPENCLAW_PORT=$YUNQI_PORT openclaw --config "$HOME/.openclaw/yunqi/config.yaml" gateway start &
YUNQI_PID=$!

echo ""
echo "✅ 云字辈7人团队已启动！"
echo ""
echo "进程ID:"
echo "  云壹-CEO: $YUNYI_PID (端口: $YUNYI_PORT)"
echo "  云贰-PM:  $YUNER_PID (端口: $YUNER_PORT)"
echo "  云叁-PD:  $YUNSAN_PID (端口: $YUNSAN_PORT)"
echo "  云肆-UI:  $YUNSI_PID (端口: $YUNSI_PORT)"
echo "  云伍-FE:  $YUNWU_PID (端口: $YUNWU_PORT)"
echo "  云陆-BE:  $YUNLU_PID (端口: $YUNLU_PORT)"
echo "  云柒-QA:  $YUNQI_PID (端口: $YUNQI_PORT)"
echo ""
echo "📡 下一步：配置 ngrok 或其他内网穿透工具"
echo ""
echo "查看日志: tail -f ~/.openclaw/yunyi/logs/*.log"
echo "停止团队: ./scripts/stop-team.sh"

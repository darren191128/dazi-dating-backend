#!/bin/bash
# 云字辈7人团队启动脚本
# 启动7个OpenClaw实例，每个对应一个角色

echo "🚀 启动云字辈7人团队..."

# 检查配置目录是否存在
for role in yunyi yuner yunsan yunsi yunwu yunlu yunqi; do
    if [ ! -d "$HOME/.openclaw/$role" ]; then
        echo "❌ 配置目录不存在: ~/.openclaw/$role"
        echo "请先创建配置目录和config.yaml"
        exit 1
    fi
done

# 启动云壹-CEO
echo "▶️  启动 云壹-CEO..."
openclaw --config "$HOME/.openclaw/yunyi/config.yaml" gateway start &
YUNYI_PID=$!

# 启动云贰-PM
echo "▶️  启动 云贰-PM..."
openclaw --config "$HOME/.openclaw/yuner/config.yaml" gateway start &
YUNER_PID=$!

# 启动云叁-PD
echo "▶️  启动 云叁-PD..."
openclaw --config "$HOME/.openclaw/yunsan/config.yaml" gateway start &
YUNSAN_PID=$!

# 启动云肆-UI
echo "▶️  启动 云肆-UI..."
openclaw --config "$HOME/.openclaw/yunsi/config.yaml" gateway start &
YUNSI_PID=$!

# 启动云伍-FE
echo "▶️  启动 云伍-FE..."
openclaw --config "$HOME/.openclaw/yunwu/config.yaml" gateway start &
YUNWU_PID=$!

# 启动云陆-BE
echo "▶️  启动 云陆-BE..."
openclaw --config "$HOME/.openclaw/yunlu/config.yaml" gateway start &
YUNLU_PID=$!

# 启动云柒-QA
echo "▶️  启动 云柒-QA..."
openclaw --config "$HOME/.openclaw/yunqi/config.yaml" gateway start &
YUNQI_PID=$!

echo ""
echo "✅ 云字辈7人团队已启动！"
echo ""
echo "进程ID:"
echo "  云壹-CEO: $YUNYI_PID"
echo "  云贰-PM:  $YUNER_PID"
echo "  云叁-PD:  $YUNSAN_PID"
echo "  云肆-UI:  $YUNSI_PID"
echo "  云伍-FE:  $YUNWU_PID"
echo "  云陆-BE:  $YUNLU_PID"
echo "  云柒-QA:  $YUNQI_PID"
echo ""
echo "查看日志: tail -f ~/.openclaw/yunyi/logs/*.log"
echo "停止团队: ./scripts/stop-team.sh"

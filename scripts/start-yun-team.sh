#!/bin/bash
# 启动云字辈7人团队 - 使用 profile 模式
# 每个角色使用独立的 profile

echo "🚀 启动云字辈7人团队..."
echo ""

# 检查配置目录
for role in yuner yunsan yunsi yunwu yunlu yunqi; do
    if [ ! -d "$HOME/.openclaw/$role" ]; then
        echo "❌ 配置目录不存在: ~/.openclaw/$role"
        exit 1
    fi
done

echo "📋 启动顺序："
echo "  1. 云贰-PM (项目经理)"
echo "  2. 云叁-PD (产品经理)"
echo "  3. 云肆-UI (UI设计师)"
echo "  4. 云伍-FE (前端工程师)"
echo "  5. 云陆-BE (后端工程师)"
echo "  6. 云柒-QA (测试工程师)"
echo ""

# 启动云贰-PM
echo "▶️  启动 云贰-PM..."
OPENCLAW_CONFIG_PATH="$HOME/.openclaw/yuner/config.yaml" openclaw --profile yuner gateway start &
YUNER_PID=$!
echo "   PID: $YUNER_PID"
sleep 5

# 启动云叁-PD
echo "▶️  启动 云叁-PD..."
OPENCLAW_CONFIG_PATH="$HOME/.openclaw/yunsan/config.yaml" openclaw --profile yunsan gateway start &
YUNSAN_PID=$!
echo "   PID: $YUNSAN_PID"
sleep 5

# 启动云肆-UI
echo "▶️  启动 云肆-UI..."
OPENCLAW_CONFIG_PATH="$HOME/.openclaw/yunsi/config.yaml" openclaw --profile yunsi gateway start &
YUNSI_PID=$!
echo "   PID: $YUNSI_PID"
sleep 5

# 启动云伍-FE
echo "▶️  启动 云伍-FE..."
OPENCLAW_CONFIG_PATH="$HOME/.openclaw/yunwu/config.yaml" openclaw --profile yunwu gateway start &
YUNWU_PID=$!
echo "   PID: $YUNWU_PID"
sleep 5

# 启动云陆-BE
echo "▶️  启动 云陆-BE..."
OPENCLAW_CONFIG_PATH="$HOME/.openclaw/yunlu/config.yaml" openclaw --profile yunlu gateway start &
YUNLU_PID=$!
echo "   PID: $YUNLU_PID"
sleep 5

# 启动云柒-QA
echo "▶️  启动 云柒-QA..."
OPENCLAW_CONFIG_PATH="$HOME/.openclaw/yunqi/config.yaml" openclaw --profile yunqi gateway start &
YUNQI_PID=$!
echo "   PID: $YUNQI_PID"

echo ""
echo "✅ 云字辈6人团队已启动！"
echo ""
echo "📡 下一步：在飞书平台启用长连接模式"
echo ""
echo "1. 登录飞书开放平台：https://open.feishu.cn/"
echo "2. 进入每个应用 → 「事件订阅」"
echo "3. 选择「长连接接收事件」模式"
echo "4. 点击「保存」"
echo ""
echo "💡 提示：如果显示"未检测到应用连接信息"，请等待10-20秒后重试"
echo ""
echo "查看连接状态："
echo "  ./scripts/check-connection.sh"
echo ""
echo "停止团队："
echo "  ./scripts/stop-team.sh"

# 保存PID到文件
echo "$YUNER_PID $YUNSAN_PID $YUNSI_PID $YUNWU_PID $YUNLU_PID $YUNQI_PID" > /tmp/yun-team.pids

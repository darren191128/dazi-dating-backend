#!/bin/bash
# 七人智能体团队启动脚本
# 用法: ./start-agents.sh [all|智能体名称]

set -e

AGENTS=("云壹" "云贰" "云叁" "云肆" "云伍" "云陆" "云柒")

start_agent() {
    local agent=$1
    local config_dir="/root/.openclaw/agents/$agent"
    
    echo "========================================"
    echo "启动智能体: $agent"
    echo "========================================"
    
    # 检查配置文件
    if [ ! -f "$config_dir/openclaw.json" ]; then
        echo "❌ 配置文件不存在: $config_dir/openclaw.json"
        return 1
    fi
    
    # 检查配置中的 appId
    local app_id=$(grep -o '"appId": "[^"]*"' "$config_dir/openclaw.json" | head -1 | cut -d'"' -f4)
    if [ -z "$app_id" ]; then
        echo "⚠️  警告: 配置文件中未找到 appId"
        return 1
    fi
    
    echo "飞书应用: ${app_id:0:20}..."
    
    # 启动 Gateway
    echo "启动 Gateway..."
    OPENCLAW_CONFIG_DIR="$config_dir" openclaw gateway start &
    sleep 2
    
    echo "✅ $agent 已启动"
    echo ""
}

stop_agent() {
    local agent=$1
    echo "停止智能体: $agent"
    # 查找并停止进程
    pkill -f "openclaw.*$agent" || true
}

status_agent() {
    local agent=$1
    local port=$((18789 + $(echo "${AGENTS[@]}" | tr ' ' '\n' | grep -n "^$agent$" | cut -d: -f1) - 1))
    
    echo -n "$agent (端口 $port): "
    if curl -s "http://127.0.0.1:$port/__openclaw__/health" > /dev/null 2>&1; then
        echo "✅ 运行中"
    else
        echo "❌ 未运行"
    fi
}

case "${1:-all}" in
    all)
        echo "启动所有智能体..."
        for agent in "${AGENTS[@]}"; do
            start_agent "$agent"
        done
        echo "所有智能体启动完成！"
        ;;
    云壹|云贰|云叁|云肆|云伍|云陆|云柒)
        start_agent "$1"
        ;;
    stop)
        echo "停止所有智能体..."
        for agent in "${AGENTS[@]}"; do
            stop_agent "$agent"
        done
        ;;
    status)
        echo "智能体状态:"
        for agent in "${AGENTS[@]}"; do
            status_agent "$agent"
        done
        ;;
    *)
        echo "用法: $0 [all|云壹|云贰|云叁|云肆|云伍|云陆|云柒|stop|status]"
        exit 1
        ;;
esac

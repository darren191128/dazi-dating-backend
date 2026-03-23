#!/bin/bash
# 启动所有七人智能体团队
# 后台运行模式

cd /root/.openclaw/workspace

# 定义智能体和端口
AGENTS=("云壹" "云贰" "云叁" "云肆" "云伍" "云陆" "云柒")
PORTS=(18789 18790 18791 18792 18793 18794 18795)

# 启动函数
start_agent_bg() {
    local agent=$1
    local port=$2
    local config_dir="/root/.openclaw/agents/$agent"
    local log_file="/tmp/openclaw-$agent.log"
    
    echo "启动 $agent (端口 $port)..."
    
    # 使用 nohup 后台启动
    nohup bash -c "export OPENCLAW_CONFIG_DIR=$config_dir; openclaw gateway start" > "$log_file" 2>&1 &
    
    echo $! > "/tmp/openclaw-$agent.pid"
    echo "  PID: $!, 日志: $log_file"
}

# 启动所有智能体
echo "========================================"
echo "启动七人智能体团队"
echo "========================================"
echo ""

for i in "${!AGENTS[@]}"; do
    start_agent_bg "${AGENTS[$i]}" "${PORTS[$i]}"
    sleep 2
done

echo ""
echo "========================================"
echo "所有智能体已启动"
echo "========================================"
echo ""
echo "检查状态:"
sleep 3

for i in "${!AGENTS[@]}"; do
    agent="${AGENTS[$i]}"
    port="${PORTS[$i]}"
    if curl -s "http://127.0.0.1:$port/__openclaw__/health" > /dev/null 2>&1; then
        echo "✅ $agent (端口 $port): 运行中"
    else
        echo "⏳ $agent (端口 $port): 启动中..."
    fi
done

echo ""
echo "查看详细日志:"
echo "  tail -f /tmp/openclaw-云壹.log"
echo "  tail -f /tmp/openclaw-云贰.log"
echo "  ..."

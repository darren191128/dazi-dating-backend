#!/bin/bash
#
# 完美工作流快速启动脚本
# 用法: ./start-workflow.sh <项目名> [工作流类型]
#

set -e

PROJECT_NAME=$1
WORKFLOW_TYPE=${2:-"full"}

if [ -z "$PROJECT_NAME" ]; then
    echo "❌ 错误: 请提供项目名称"
    echo "用法: ./start-workflow.sh <项目名> [工作流类型]"
    echo ""
    echo "示例:"
    echo "  ./start-workflow.sh \"启云官网\""
    echo "  ./start-workflow.sh \"启云官网\" quick"
    exit 1
fi

echo "╔══════════════════════════════════════════════════════════╗"
echo "║           🤖 完美工作流启动器                            ║"
echo "╚══════════════════════════════════════════════════════════╝"
echo ""
echo "📋 项目: $PROJECT_NAME"
echo "🔧 工作流: $WORKFLOW_TYPE"
echo ""

# 检查记忆系统
echo "🔍 检查共享记忆系统..."
if [ ! -f "memory/db/memory.json" ]; then
    echo "   初始化记忆数据库..."
    mkdir -p memory/db
    echo '{"conversations": [], "projects": [], "knowledge": []}' > memory/db/memory.json
fi
echo "   ✅ 记忆系统正常"
echo ""

# 检查项目是否存在
echo "🔍 检查项目配置..."
PROJECT_EXISTS=$(node memory/db/memory.js get-project "$PROJECT_NAME" 2>/dev/null | grep -c "name" || true)

if [ "$PROJECT_EXISTS" -eq 0 ]; then
    echo "   项目不存在，创建新项目..."
    node memory/db/memory.js add-project "$PROJECT_NAME" "" "" ""
    echo "   ✅ 项目已创建"
else
    echo "   ✅ 项目已存在"
fi
echo ""

# 启动工作流
echo "🚀 启动工作流引擎..."
echo ""

case $WORKFLOW_TYPE in
    "full")
        echo "📊 执行完整开发流程..."
        node memory/workflow-engine.js run "$PROJECT_NAME"
        ;;
    "quick")
        echo "⚡ 执行快速迭代流程..."
        node memory/workflow-engine.js run "$PROJECT_NAME" "需求分析,UI设计,前端开发,测试验收"
        ;;
    "frontend")
        echo "🎨 执行前端开发流程..."
        node memory/workflow-engine.js run "$PROJECT_NAME" "UI设计,前端开发,测试验收"
        ;;
    "backend")
        echo "⚙️  执行后端开发流程..."
        node memory/workflow-engine.js run "$PROJECT_NAME" "需求分析,架构设计,后端开发,测试验收"
        ;;
    *)
        echo "❌ 未知工作流类型: $WORKFLOW_TYPE"
        echo "可用类型: full, quick, frontend, backend"
        exit 1
        ;;
esac

echo ""
echo "✅ 工作流执行完成!"
echo ""
echo "📊 查看报告:"
echo "   node memory/db/memory.js get-project \"$PROJECT_NAME\""
echo ""

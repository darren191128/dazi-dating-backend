# 多模型智能体系统 - 使用指南

## 配置完成状态 ✅

### 已完成的配置

1. **AGENTS.md 已更新** - 添加了模型路由配置章节
2. **models.yaml 已创建** - 完整的模型配置文件
3. **model-router.js 已创建** - 模型路由模块

### 角色-模型映射表

| 角色 | 默认模型 | 图像任务模型 | 备用模型 |
|------|---------|-------------|---------|
| 产品经理 | moonshot/kimi-k2.5 | - | baidu/ernie |
| 设计经理 | moonshot/kimi-k2.5 | tongyi/wanxiang | moonshot/kimi-k2.5 |
| 前端经理 | moonshot/kimi-k2.5 | - | baidu/ernie |
| 后端经理 | moonshot/kimi-k2.5 | - | baidu/ernie |
| 测试经理 | moonshot/kimi-k2.5 | - | baidu/ernie |

## 测试方法

### 1. 查看系统状态
```bash
cd /root/.openclaw/workspace/config
node model-router.js status
```

### 2. 获取角色对应的模型
```bash
# 获取产品经理的默认模型
node model-router.js get-model "产品经理"
# 输出: moonshot/kimi-k2.5

# 获取设计经理的图像生成模型
node model-router.js get-model "设计经理" image_generation
# 输出: tongyi/wanxiang
```

### 3. 获取完整的子智能体配置
```bash
node model-router.js get-config "测试经理"
```

### 4. 验证配置
```bash
node model-router.js validate
```

### 5. 列出所有模型
```bash
node model-router.js list-models
```

## 在代码中使用

### JavaScript/Node.js

```javascript
const modelRouter = require('./config/model-router.js');

// 获取角色对应的模型
const model = modelRouter.getModelForRole('设计经理', 'image_generation');
console.log(model); // tongyi/wanxiang

// 获取完整的子智能体配置
const agentConfig = modelRouter.createAgentConfig('产品经理', {
  taskType: 'planning'
});
console.log(agentConfig);

// 获取系统状态
const status = modelRouter.getSystemStatus();
console.log(status);
```

## 环境变量配置

在使用模型 API 前，需要设置以下环境变量：

```bash
# Moonshot API Key
export MOONSHOT_API_KEY="your-moonshot-api-key"

# 阿里云 DashScope API Key (通义万相)
export DASHSCOPE_API_KEY="your-dashscope-api-key"

# 百度 API Key (文心一言)
export BAIDU_API_KEY="your-baidu-api-key"
```

## 子智能体调用示例

创建子智能体时，使用路由模块获取模型配置：

```javascript
const modelRouter = require('./config/model-router.js');

// 为特定角色创建子智能体配置
function createSubAgent(role, task) {
  const config = modelRouter.createAgentConfig(role, {
    taskType: task,
    customParam: 'value'
  });
  
  return {
    name: `${role}_Agent`,
    model: config.model,
    fallback: config.fallback_models,
    max_tokens: config.model_info.max_tokens,
    temperature: config.model_info.temperature,
    // ... 其他配置
  };
}

// 使用示例
const pmAgent = createSubAgent('产品经理', 'requirement_analysis');
const designAgent = createSubAgent('设计经理', 'image_generation');
```

## 故障排除

### 1. 模块未找到错误
确保已安装 js-yaml：
```bash
cd /root/.openclaw/workspace
npm install js-yaml
```

### 2. 配置验证失败
运行验证命令查看详细错误：
```bash
node model-router.js validate
```

### 3. 模型配置未生效
检查 models.yaml 文件语法是否正确，可使用在线 YAML 验证工具检查。

## 扩展配置

如需添加新角色或模型，编辑 `config/models.yaml`：

1. 在 `models` 部分添加新模型定义
2. 在 `role_model_mapping` 部分添加角色映射
3. 在 `routing_rules` 部分添加路由规则
4. 重新加载配置即可生效

## 注意事项

1. 所有 API Key 通过环境变量注入，不硬编码在配置文件中
2. 模型路由支持故障自动转移
3. 任务类型路由优先级高于角色默认路由
4. 配置缓存 1 分钟，修改后需要等待缓存过期或重启进程

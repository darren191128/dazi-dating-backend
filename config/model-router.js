/**
 * 多模型智能体路由模块
 * Multi-Model Agent Routing Module
 * 
 * 为子智能体系统提供模型选择、路由和故障转移功能
 */

const fs = require('fs');
const path = require('path');
const yaml = require('js-yaml');

// 配置文件路径
const CONFIG_PATH = path.join(__dirname, 'models.yaml');

// 缓存配置
let configCache = null;
let configCacheTime = null;
const CACHE_TTL = 60000; // 1分钟缓存

/**
 * 加载模型配置
 */
function loadConfig() {
  const now = Date.now();
  if (configCache && configCacheTime && (now - configCacheTime) < CACHE_TTL) {
    return configCache;
  }

  try {
    const content = fs.readFileSync(CONFIG_PATH, 'utf8');
    configCache = yaml.load(content);
    configCacheTime = now;
    return configCache;
  } catch (error) {
    console.error('Failed to load models.yaml:', error.message);
    // 返回默认配置
    return getDefaultConfig();
  }
}

/**
 * 获取默认配置
 */
function getDefaultConfig() {
  return {
    models: {
      'moonshot/kimi-k2.5': {
        name: 'Kimi K2.5',
        provider: 'moonshot',
        priority: 1
      }
    },
    role_model_mapping: {},
    system: {
      default_model: 'moonshot/kimi-k2.5'
    }
  };
}

/**
 * 根据角色获取默认模型
 * @param {string} role - 角色名称
 * @param {string} taskType - 任务类型 (可选)
 * @returns {string} 模型标识
 */
function getModelForRole(role, taskType = null) {
  const config = loadConfig();
  const mapping = config.role_model_mapping?.[role];
  
  if (!mapping) {
    return config.system?.default_model || 'moonshot/kimi-k2.5';
  }

  // 如果有任务类型且存在特定路由，使用任务特定模型
  if (taskType && mapping.task_routing?.[taskType]) {
    return mapping.task_routing[taskType];
  }

  return mapping.default || config.system?.default_model || 'moonshot/kimi-k2.5';
}

/**
 * 获取角色的备用模型列表
 * @param {string} role - 角色名称
 * @returns {string[]} 备用模型列表
 */
function getFallbackModels(role) {
  const config = loadConfig();
  const mapping = config.role_model_mapping?.[role];
  
  if (!mapping?.fallback) {
    return [config.system?.default_model || 'moonshot/kimi-k2.5'];
  }

  return mapping.fallback;
}

/**
 * 获取模型详细信息
 * @param {string} modelId - 模型标识
 * @returns {object|null} 模型配置
 */
function getModelInfo(modelId) {
  const config = loadConfig();
  return config.models?.[modelId] || null;
}

/**
 * 获取所有可用模型
 * @returns {object} 模型配置对象
 */
function getAllModels() {
  const config = loadConfig();
  return config.models || {};
}

/**
 * 根据任务类型获取推荐模型
 * @param {string} taskType - 任务类型
 * @returns {string} 模型标识
 */
function getModelForTask(taskType) {
  const config = loadConfig();
  const rules = config.routing_rules?.task_based;
  
  if (rules?.[taskType]?.primary) {
    return rules[taskType].primary;
  }

  return config.system?.default_model || 'moonshot/kimi-k2.5';
}

/**
 * 创建子智能体配置
 * @param {string} role - 角色名称
 * @param {object} options - 配置选项
 * @returns {object} 子智能体配置
 */
function createAgentConfig(role, options = {}) {
  const config = loadConfig();
  const taskType = options.taskType || options.task_type;
  
  const primaryModel = getModelForRole(role, taskType);
  const fallbackModels = getFallbackModels(role);
  
  const modelInfo = getModelInfo(primaryModel);
  
  return {
    role,
    model: primaryModel,
    model_info: {
      name: modelInfo?.name || primaryModel,
      provider: modelInfo?.provider || 'unknown',
      max_tokens: modelInfo?.max_tokens || 4096,
      temperature: modelInfo?.temperature || 0.7
    },
    fallback_models: fallbackModels.filter(m => m !== primaryModel),
    task_type: taskType || 'default',
    routing_decision: {
      based_on: taskType ? 'task_type' : 'role_default',
      timestamp: new Date().toISOString()
    },
    // 扩展选项
    ...options
  };
}

/**
 * 获取API配置
 * @param {string} modelId - 模型标识
 * @returns {object|null} API配置
 */
function getApiConfig(modelId) {
  const config = loadConfig();
  const modelInfo = getModelInfo(modelId);
  
  if (!modelInfo) return null;

  const provider = modelInfo.provider;
  const template = config.api_templates?.[provider];
  
  if (!template) return null;

  // 替换环境变量
  const apiKey = process.env[modelInfo.api_key_env];
  
  return {
    base_url: modelInfo.base_url,
    headers: replaceEnvVars(template.headers, { [modelInfo.api_key_env]: apiKey }),
    timeout: template.timeout || 60,
    retry_count: template.retry_count || 3,
    model_id: modelInfo.model_id,
    max_tokens: modelInfo.max_tokens,
    temperature: modelInfo.temperature
  };
}

/**
 * 替换环境变量
 */
function replaceEnvVars(obj, envVars) {
  const result = {};
  for (const [key, value] of Object.entries(obj)) {
    if (typeof value === 'string') {
      result[key] = value.replace(/\$\{(\w+)\}/g, (match, varName) => {
        return envVars[varName] || process.env[varName] || match;
      });
    } else {
      result[key] = value;
    }
  }
  return result;
}

/**
 * 验证模型配置
 * @returns {object} 验证结果
 */
function validateConfig() {
  const config = loadConfig();
  const errors = [];
  const warnings = [];

  // 验证模型定义
  if (!config.models || Object.keys(config.models).length === 0) {
    errors.push('No models defined in configuration');
  }

  // 验证角色映射
  if (!config.role_model_mapping || Object.keys(config.role_model_mapping).length === 0) {
    warnings.push('No role-model mapping defined');
  }

  // 验证每个角色映射的模型是否存在
  for (const [role, mapping] of Object.entries(config.role_model_mapping || {})) {
    if (mapping.default && !config.models[mapping.default]) {
      errors.push(`Role "${role}" references undefined model: ${mapping.default}`);
    }
    
    for (const fallback of mapping.fallback || []) {
      if (!config.models[fallback]) {
        errors.push(`Role "${role}" references undefined fallback model: ${fallback}`);
      }
    }
  }

  return {
    valid: errors.length === 0,
    errors,
    warnings
  };
}

/**
 * 获取系统状态
 * @returns {object} 系统状态
 */
function getSystemStatus() {
  const config = loadConfig();
  const validation = validateConfig();
  
  return {
    status: validation.valid ? 'ok' : 'error',
    default_model: config.system?.default_model,
    available_models: Object.keys(config.models || {}),
    configured_roles: Object.keys(config.role_model_mapping || {}),
    validation,
    timestamp: new Date().toISOString()
  };
}

// 导出模块
module.exports = {
  loadConfig,
  getModelForRole,
  getFallbackModels,
  getModelInfo,
  getAllModels,
  getModelForTask,
  createAgentConfig,
  getApiConfig,
  validateConfig,
  getSystemStatus
};

// CLI 支持
if (require.main === module) {
  const command = process.argv[2];
  const arg1 = process.argv[3];
  const arg2 = process.argv[4];

  switch (command) {
    case 'get-model':
      console.log(getModelForRole(arg1, arg2));
      break;
    case 'get-config':
      console.log(JSON.stringify(createAgentConfig(arg1, { taskType: arg2 }), null, 2));
      break;
    case 'status':
      console.log(JSON.stringify(getSystemStatus(), null, 2));
      break;
    case 'validate':
      console.log(JSON.stringify(validateConfig(), null, 2));
      break;
    case 'list-models':
      console.log(JSON.stringify(getAllModels(), null, 2));
      break;
    default:
      console.log(`
多模型智能体路由模块

用法:
  node model-router.js <command> [args]

命令:
  get-model <role> [task-type]  - 获取角色对应的模型
  get-config <role> [task-type] - 获取完整的子智能体配置
  status                        - 获取系统状态
  validate                      - 验证配置
  list-models                   - 列出所有可用模型

示例:
  node model-router.js get-model "设计经理" image_generation
  node model-router.js get-config "产品经理"
  node model-router.js status
      `);
  }
}

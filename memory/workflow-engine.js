#!/usr/bin/env node
/**
 * 完美工作流调度器 - 智能体协同工作流优化版
 * 
 * 核心优化点：
 * 1. 任务依赖分析 - 自动识别任务依赖关系，优化执行顺序
 * 2. 并行调度 - 无依赖任务并行执行，提高效率
 * 3. 结果聚合 - 自动收集和整合各智能体输出
 * 4. 质量检查 - 自动验证交付物质量
 * 5. 错误恢复 - 失败任务自动重试和降级
 */

const path = require('path');
const { getProjectContext, addProject, updateProject, addConversation, addKnowledge } = require(path.join(__dirname, 'db/memory.js'));

// ==================== 配置 ====================

const AGENT_CONFIG = {
  '云捌': {
    name: '项目与产品负责人',
    role: '项目与产品负责人',
    model: 'zhipu/glm-4.7',
    description: '统筹项目管理、需求分析、产品规划',
    capabilities: ['planning', 'analysis', 'coordination'],
    priority: 1,
    timeout: 180
  },
  '云肆': {
    name: '设计经理',
    role: 'UI设计师',
    model: 'aliyun/qwen-plus',
    fallback_model: 'moonshot/kimi-k2.5',
    description: '负责视觉设计和交互设计',
    capabilities: ['design', 'ui', 'visual'],
    priority: 2,
    timeout: 120
  },
  '云伍': {
    name: '前端经理',
    role: '前端工程师',
    model: 'aliyun/qwen-plus',
    description: '负责前端开发和界面实现',
    capabilities: ['frontend', 'react', 'typescript'],
    priority: 3,
    timeout: 120
  },
  '云陆': {
    name: '后端经理',
    role: '后端工程师',
    model: 'aliyun/qwen-plus',
    description: '负责后端开发和API设计',
    capabilities: ['backend', 'api', 'database'],
    priority: 3,
    timeout: 120
  },
  '云柒': {
    name: '测试经理',
    role: '测试工程师',
    model: 'aliyun/qwen-plus',
    description: '负责质量保障和测试',
    capabilities: ['testing', 'qa', 'automation'],
    priority: 4,
    timeout: 90
  }
};

// ==================== 任务定义 ====================

const TASK_TEMPLATES = {
  '需求分析': {
    agent: '云捌',
    description: '分析项目需求，制定产品规划',
    dependencies: [],
    outputs: ['PRD文档', '产品规划'],
    check: (result) => result.includes('PRD') || result.includes('需求')
  },
  'UI设计': {
    agent: '云肆',
    description: '设计用户界面和交互方案',
    dependencies: ['需求分析'],
    outputs: ['设计稿', 'UI规范'],
    check: (result) => result.includes('设计') || result.includes('UI')
  },
  '前端开发': {
    agent: '云伍',
    description: '实现前端界面和交互功能',
    dependencies: ['UI设计'],
    outputs: ['前端代码', '组件库'],
    check: (result) => result.includes('组件') || result.includes('前端')
  },
  '后端开发': {
    agent: '云陆',
    description: '设计和实现后端API',
    dependencies: ['需求分析'],
    outputs: ['API文档', '后端代码'],
    check: (result) => result.includes('API') || result.includes('后端')
  },
  '接口联调': {
    agent: '云伍',
    description: '前后端接口对接和调试',
    dependencies: ['前端开发', '后端开发'],
    outputs: ['联调报告'],
    check: (result) => result.includes('联调') || result.includes('对接')
  },
  '测试验收': {
    agent: '云柒',
    description: '执行测试用例，质量验收',
    dependencies: ['接口联调'],
    outputs: ['测试报告', 'Bug列表'],
    check: (result) => result.includes('测试') || result.includes('Bug')
  }
};

// ==================== 工作流引擎 ====================

class WorkflowEngine {
  constructor(projectName) {
    this.projectName = projectName;
    this.project = getProjectContext(projectName);
    this.results = new Map();
    this.errors = new Map();
    this.executionLog = [];
  }

  // 分析任务依赖，生成执行计划
  analyzeDependencies(tasks) {
    const plan = [];
    const completed = new Set();
    const remaining = new Set(tasks);

    while (remaining.size > 0) {
      const batch = [];
      
      for (const task of remaining) {
        const config = TASK_TEMPLATES[task];
        if (!config) continue;

        // 检查依赖是否满足
        const depsSatisfied = config.dependencies.every(dep => completed.has(dep));
        
        if (depsSatisfied) {
          batch.push(task);
        }
      }

      if (batch.length === 0 && remaining.size > 0) {
        throw new Error(`依赖循环或缺失: ${Array.from(remaining).join(', ')}`);
      }

      plan.push(batch);
      batch.forEach(task => {
        completed.add(task);
        remaining.delete(task);
      });
    }

    return plan;
  }

  // 执行单个任务
  async executeTask(taskName, context = {}) {
    const config = TASK_TEMPLATES[taskName];
    if (!config) {
      throw new Error(`未知任务: ${taskName}`);
    }

    const agentConfig = AGENT_CONFIG[config.agent];
    const startTime = Date.now();

    console.log(`\n🚀 [${taskName}] 开始执行`);
    console.log(`   负责: ${agentConfig.name} (${config.agent})`);
    console.log(`   模型: ${agentConfig.model}`);

    try {
      // 构建任务提示
      const prompt = this.buildTaskPrompt(taskName, config, context);
      
      // 调用智能体
      const result = await this.callAgent(config.agent, prompt, agentConfig);
      
      // 质量检查
      const quality = this.checkQuality(taskName, result);
      
      const duration = Date.now() - startTime;
      
      this.results.set(taskName, {
        success: true,
        result,
        quality,
        duration,
        agent: config.agent,
        model: agentConfig.model
      });

      this.executionLog.push({
        task: taskName,
        agent: config.agent,
        status: 'success',
        duration,
        quality
      });

      console.log(`✅ [${taskName}] 完成 (${duration}ms) - 质量: ${quality.score}/100`);
      
      return result;

    } catch (error) {
      const duration = Date.now() - startTime;
      
      this.errors.set(taskName, error);
      this.results.set(taskName, {
        success: false,
        error: error.message,
        duration,
        agent: config.agent
      });

      this.executionLog.push({
        task: taskName,
        agent: config.agent,
        status: 'failed',
        duration,
        error: error.message
      });

      console.log(`❌ [${taskName}] 失败 (${duration}ms): ${error.message}`);
      
      // 尝试重试或降级
      return await this.handleFailure(taskName, config, context, error);
    }
  }

  // 构建任务提示
  buildTaskPrompt(taskName, config, context) {
    let prompt = `【角色】${AGENT_CONFIG[config.agent].description}\n\n`;
    prompt += `【任务】${config.description}\n\n`;
    
    if (this.project) {
      prompt += `【项目上下文】\n`;
      prompt += `- 项目名称: ${this.project.name}\n`;
      prompt += `- 项目状态: ${this.project.status}\n`;
      if (this.project.prd) prompt += `- PRD文档: ${this.project.prd}\n`;
      if (this.project.design) prompt += `- 设计文档: ${this.project.design}\n`;
      prompt += `\n`;
    }

    // 添加上游任务结果作为上下文
    if (config.dependencies.length > 0) {
      prompt += `【上游任务结果】\n`;
      for (const dep of config.dependencies) {
        const depResult = this.results.get(dep);
        if (depResult && depResult.success) {
          prompt += `- ${dep}: 已完成\n`;
          // 可以添加关键摘要
        }
      }
      prompt += `\n`;
    }

    prompt += `【要求】\n`;
    prompt += `1. 使用 ${AGENT_CONFIG[config.agent].model} 模型\n`;
    prompt += `2. 交付物: ${config.outputs.join(', ')}\n`;
    prompt += `3. 将关键结果写入文件\n`;
    prompt += `4. 返回简洁的执行摘要\n`;

    return prompt;
  }

  // 调用智能体
  async callAgent(agentId, prompt, config) {
    // 这里集成实际的智能体调用逻辑
    // 目前使用 sessions_spawn 的简化版本
    
    const { spawnAgent } = require('./agent-spawner.js');
    
    // 模拟调用，实际应该使用 OpenClaw 的 sessions_spawn
    return new Promise((resolve, reject) => {
      // 这里应该调用实际的智能体API
      // 简化版本，实际实现需要集成 OpenClaw runtime
      resolve(`任务执行完成: ${prompt.substring(0, 50)}...`);
    });
  }

  // 质量检查
  checkQuality(taskName, result) {
    const config = TASK_TEMPLATES[taskName];
    const checks = [];
    
    // 基础检查
    checks.push({
      name: '非空检查',
      pass: result && result.length > 100,
      score: result && result.length > 100 ? 20 : 0
    });

    // 内容检查
    if (config.check) {
      checks.push({
        name: '内容检查',
        pass: config.check(result),
        score: config.check(result) ? 30 : 0
      });
    }

    // 格式检查
    checks.push({
      name: '格式检查',
      pass: result.includes('【') || result.includes('#'),
      score: result.includes('【') || result.includes('#') ? 20 : 0
    });

    const totalScore = checks.reduce((sum, c) => sum + c.score, 0);
    
    return {
      score: Math.min(totalScore + 30, 100), // 基础分30
      checks,
      passed: checks.filter(c => c.pass).length,
      total: checks.length
    };
  }

  // 失败处理
  async handleFailure(taskName, config, context, error) {
    console.log(`🔄 [${taskName}] 尝试恢复...`);

    // 策略1: 使用备用模型重试
    const agentConfig = AGENT_CONFIG[config.agent];
    if (agentConfig.fallback_model) {
      console.log(`   切换到备用模型: ${agentConfig.fallback_model}`);
      try {
        const result = await this.callAgentWithModel(
          config.agent, 
          this.buildTaskPrompt(taskName, config, context),
          agentConfig.fallback_model
        );
        
        this.results.set(taskName, {
          success: true,
          result,
          fallback: true,
          agent: config.agent,
          model: agentConfig.fallback_model
        });

        console.log(`✅ [${taskName}] 使用备用模型完成`);
        return result;
      } catch (fallbackError) {
        console.log(`❌ [${taskName}] 备用模型也失败`);
      }
    }

    // 策略2: 简化任务
    console.log(`   尝试简化任务...`);
    const simplifiedPrompt = this.buildTaskPrompt(taskName, config, context) + 
      '\n【注意】由于技术问题，请简化输出，只保留核心内容。';
    
    try {
      const result = await this.callAgent(config.agent, simplifiedPrompt, agentConfig);
      
      this.results.set(taskName, {
        success: true,
        result,
        simplified: true,
        agent: config.agent
      });

      console.log(`✅ [${taskName}] 简化任务完成`);
      return result;
    } catch (simplifiedError) {
      throw new Error(`任务无法完成: ${error.message}`);
    }
  }

  // 使用指定模型调用
  async callAgentWithModel(agentId, prompt, model) {
    // 实际实现需要支持模型切换
    return this.callAgent(agentId, prompt, { ...AGENT_CONFIG[agentId], model });
  }

  // 执行工作流
  async execute(tasks) {
    console.log(`\n╔══════════════════════════════════════════════════════════╗`);
    console.log(`║           🤖 完美工作流执行引擎                          ║`);
    console.log(`╚══════════════════════════════════════════════════════════╝\n`);
    console.log(`📋 项目: ${this.projectName}`);
    console.log(`📊 任务数: ${tasks.length}`);
    console.log(`\n`);

    const plan = this.analyzeDependencies(tasks);
    
    console.log(`📈 执行计划 (${plan.length} 个批次):`);
    plan.forEach((batch, idx) => {
      console.log(`   批次 ${idx + 1}: ${batch.join(', ')}`);
    });
    console.log(`\n`);

    const startTime = Date.now();

    // 按批次执行
    for (let i = 0; i < plan.length; i++) {
      const batch = plan[i];
      console.log(`\n📦 执行批次 ${i + 1}/${plan.length}: [${batch.join(', ')}]`);
      
      // 并行执行批次内任务
      const batchResults = await Promise.all(
        batch.map(task => this.executeTask(task))
      );
    }

    const totalDuration = Date.now() - startTime;

    return this.generateReport(totalDuration);
  }

  // 生成执行报告
  generateReport(totalDuration) {
    const successCount = Array.from(this.results.values()).filter(r => r.success).length;
    const failCount = this.errors.size;
    const totalTasks = this.results.size;

    console.log(`\n╔══════════════════════════════════════════════════════════╗`);
    console.log(`║              📊 工作流执行报告                           ║`);
    console.log(`╚══════════════════════════════════════════════════════════╝\n`);

    console.log(`⏱️  总耗时: ${(totalDuration / 1000).toFixed(2)}秒`);
    console.log(`✅ 成功: ${successCount}/${totalTasks}`);
    console.log(`❌ 失败: ${failCount}/${totalTasks}`);
    console.log(`📈 成功率: ${((successCount / totalTasks) * 100).toFixed(1)}%`);
    console.log(`\n`);

    console.log(`📋 执行详情:`);
    this.executionLog.forEach(log => {
      const icon = log.status === 'success' ? '✅' : '❌';
      console.log(`   ${icon} ${log.task} (${log.duration}ms) - ${log.agent}`);
    });

    console.log(`\n`);

    // 保存报告到记忆系统
    const report = {
      project: this.projectName,
      timestamp: new Date().toISOString(),
      duration: totalDuration,
      success: successCount,
      failed: failCount,
      total: totalTasks,
      log: this.executionLog,
      results: Object.fromEntries(this.results)
    };

    addKnowledge(
      `workflow-report-${this.projectName}-${Date.now()}`,
      JSON.stringify(report),
      'workflow'
    );

    return report;
  }
}

// ==================== 快捷函数 ====================

// 执行完整项目工作流
async function runProjectWorkflow(projectName, customTasks = null) {
  const engine = new WorkflowEngine(projectName);
  
  // 默认任务序列
  const defaultTasks = [
    '需求分析',
    'UI设计',
    '后端开发',
    '前端开发',
    '接口联调',
    '测试验收'
  ];

  const tasks = customTasks || defaultTasks;
  return await engine.execute(tasks);
}

// 执行并行任务
async function runParallelTasks(projectName, taskNames) {
  const engine = new WorkflowEngine(projectName);
  
  // 强制作为单个批次执行
  console.log(`🚀 并行执行 ${taskNames.length} 个任务`);
  
  const results = await Promise.all(
    taskNames.map(task => engine.executeTask(task))
  );

  return results;
}

// 执行串行任务
async function runSequentialTasks(projectName, taskNames) {
  const engine = new WorkflowEngine(projectName);
  
  console.log(`🚀 串行执行 ${taskNames.length} 个任务`);
  
  const results = [];
  for (const task of taskNames) {
    const result = await engine.executeTask(task);
    results.push(result);
  }

  return results;
}

// ==================== CLI接口 ====================

const [,, cmd, ...args] = process.argv;

switch (cmd) {
  case 'run':
    // node workflow-engine.js run "项目名称" [任务1,任务2,...]
    const projectName = args[0];
    const tasks = args[1] ? args[1].split(',') : null;
    runProjectWorkflow(projectName, tasks).then(report => {
      console.log('✅ 工作流执行完成');
      process.exit(0);
    }).catch(err => {
      console.error('❌ 工作流执行失败:', err.message);
      process.exit(1);
    });
    break;

  case 'parallel':
    // node workflow-engine.js parallel "项目名称" "任务1,任务2,..."
    runParallelTasks(args[0], args[1].split(',')).then(() => {
      console.log('✅ 并行任务执行完成');
      process.exit(0);
    }).catch(err => {
      console.error('❌ 并行任务执行失败:', err.message);
      process.exit(1);
    });
    break;

  case 'sequential':
    // node workflow-engine.js sequential "项目名称" "任务1,任务2,..."
    runSequentialTasks(args[0], args[1].split(',')).then(() => {
      console.log('✅ 串行任务执行完成');
      process.exit(0);
    }).catch(err => {
      console.error('❌ 串行任务执行失败:', err.message);
      process.exit(1);
    });
    break;

  default:
    console.log('完美工作流调度器 - 用法:');
    console.log('');
    console.log('  执行完整工作流:');
    console.log('    node workflow-engine.js run "项目名称"');
    console.log('    node workflow-engine.js run "项目名称" "需求分析,UI设计,前端开发"');
    console.log('');
    console.log('  并行执行任务:');
    console.log('    node workflow-engine.js parallel "项目名称" "任务1,任务2,..."');
    console.log('');
    console.log('  串行执行任务:');
    console.log('    node workflow-engine.js sequential "项目名称" "任务1,任务2,..."');
    console.log('');
}

module.exports = {
  WorkflowEngine,
  runProjectWorkflow,
  runParallelTasks,
  runSequentialTasks,
  AGENT_CONFIG,
  TASK_TEMPLATES
};

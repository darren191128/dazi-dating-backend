const { YunBaAgent } = require('./yunba/agent.js');

/**
 * 云壹网关 - 智能路由
 * 
 * 职责：
 * 1. 接收所有消息
 * 2. 识别意图，路由到对应智能体
 * 3. 汇总回复，统一输出
 * 4. 维护对话上下文
 */

class YunYiGateway {
  constructor() {
    this.name = '云壹';
    this.role = 'CEO/网关';
    
    // 初始化子智能体
    this.agents = {
      yunba: new YunBaAgent()  // 云捌 - 项目与产品 (GLM-4.7)
      // 云伍、云陆、云柒 使用 Qwen3.5-Plus，通过配置路由
    };
    
    // 路由规则
    this.routingRules = [
      {
        name: '云捌-显式调用',
        pattern: /@云捌|@yunba|云捌[，,:\s]/i,
        target: 'yunba',
        priority: 1
      },
      {
        name: '云捌-项目管理',
        pattern: /项目.*计划|进度.*跟踪|里程碑|风险.*评估|资源.*协调|项目管理|项目进度|排期|甘特图/i,
        target: 'yunba',
        priority: 2
      },
      {
        name: '云捌-产品分析',
        pattern: /需求.*分析|PRD|用户.*研究|竞品.*分析|产品.*规划|功能.*设计|写.*PRD|产品.*需求/i,
        target: 'yunba',
        priority: 2
      },
      {
        name: '云捌-数据分析',
        pattern: /数据.*分析|数据.*驱动|指标|报表|复盘|数据.*统计/i,
        target: 'yunba',
        priority: 3
      }
    ];
  }

  // 智能路由
  route(message) {
    // 按优先级排序规则
    const sortedRules = this.routingRules.sort((a, b) => a.priority - b.priority);
    
    for (const rule of sortedRules) {
      if (rule.pattern.test(message)) {
        console.log(`[云壹网关] 匹配规则: ${rule.name} → 路由到 ${rule.target}`);
        return rule.target;
      }
    }
    
    // 默认自己处理
    console.log('[云壹网关] 无匹配规则 → 云壹处理');
    return 'self';
  }

  // 处理消息（核心入口）
  async handleMessage(message, context = {}) {
    console.log(`[云壹网关] 收到消息: ${message.substring(0, 50)}...`);
    
    const target = this.route(message);
    
    if (target === 'self') {
      // 云壹自己处理
      return this.handleSelf(message, context);
    } else if (this.agents[target]) {
      // 调度给子智能体
      const result = await this.agents[target].handleTask(message, context);
      
      // 包装回复，标明身份
      return {
        role: '云壹（网关）',
        content: `【${result.role}回复】\n\n${result.content}`,
        delegate: result,
        timestamp: new Date().toISOString()
      };
    } else {
      return {
        role: '云壹',
        content: '抱歉，该功能暂未配置',
        timestamp: new Date().toISOString()
      };
    }
  }

  // 云壹自己处理
  async handleSelf(message, context) {
    // 这里可以调用 Kimi 或其他模型
    return {
      role: '云壹',
      content: `收到，我来处理：${message}`,
      timestamp: new Date().toISOString()
    };
  }

  // 快速调用云捌
  async callYunBa(task, context) {
    return this.agents.yunba.handleTask(task, context);
  }

  // 项目管理快捷方法
  async projectManage(projectName, action) {
    return this.agents.yunba.manageProject(projectName, action);
  }

  // 产品分析快捷方法
  async productAnalyze(requirement) {
    return this.agents.yunba.analyzeProduct(requirement);
  }
}

module.exports = { YunYiGateway };

// 测试
if (require.main === module) {
  const gateway = new YunYiGateway();
  
  console.log('云壹网关已加载');
  console.log('可用智能体:', Object.keys(gateway.agents));
  console.log('');
  
  // 测试路由
  const testMessages = [
    '@云捌 制定项目计划',
    '分析一下这个需求',
    '项目进度怎么样了',
    '帮我写个PRD',
    '云壹，你好'
  ];
  
  for (const msg of testMessages) {
    const target = gateway.route(msg);
    console.log(`"${msg}" → ${target}`);
  }
}

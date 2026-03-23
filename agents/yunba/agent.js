const axios = require('axios');
const path = require('path');
require('dotenv').config({ path: path.join(__dirname, '../../config/.env') });

/**
 * 云捌 - 项目与产品负责人 (子智能体)
 * 
 * 特点：
 * - 独立使用 GLM-4.7 模型
 * - 由云壹网关调度
 * - 以独立身份执行操作
 * - 所有操作抄送云壹
 */

class YunBaAgent {
  constructor(config = {}) {
    this.name = '云捌';
    this.role = '项目与产品负责人';
    this.model = 'glm-4.7';
    this.zhipuApiKey = config.zhipuApiKey || process.env.ZHIPU_API_KEY;
    this.baseUrl = 'https://open.bigmodel.cn/api/paas/v4';
    
    // 飞书凭证（用于以云捌身份执行操作）
    this.feishuAppId = config.feishuAppId || process.env.YUNBA_FEISHU_APP_ID;
    this.feishuAppSecret = config.feishuAppSecret || process.env.YUNBA_FEISHU_APP_SECRET;
    this.feishuBaseUrl = 'https://open.feishu.cn/open-apis';
    this.accessToken = null;
    this.tokenExpireTime = 0;
  }

  // 调用 GLM-4.7
  async callGLM(messages, options = {}) {
    const systemPrompt = options.system || `你是云捌，项目与产品负责人。

【核心职责】
1. 项目管理：计划制定、进度跟踪、风险预警、资源协调
2. 产品管理：需求分析、PRD输出、用户研究、竞品分析
3. 配合云壹（CEO）执行决策

【工作原则】
- 统筹全局，既管进度也管方向
- 数据说话，基于事实做决策
- 风险前置，提前识别问题
- 结果导向，关注交付价值

【回复风格】
- 专业、简洁、actionable
- 复杂问题先拆解，再逐一分析
- 提供多方案对比，给出推荐和理由

当前时间：${new Date().toLocaleString('zh-CN')}`;

    const res = await axios.post(`${this.baseUrl}/chat/completions`, {
      model: 'glm-4.7',
      messages: [
        { role: 'system', content: systemPrompt },
        ...messages
      ],
      max_tokens: options.maxTokens || 4000,
      temperature: options.temperature || 0.7
    }, {
      headers: {
        'Authorization': `Bearer ${this.zhipuApiKey}`,
        'Content-Type': 'application/json'
      }
    });

    return {
      content: res.data.choices[0].message.content,
      reasoning: res.data.choices[0].message.reasoning_content,
      usage: res.data.usage
    };
  }

  // 处理任务（核心入口）
  async handleTask(task, context = {}) {
    console.log(`[云捌] 处理任务: ${task.substring(0, 50)}...`);

    const messages = [{ role: 'user', content: task }];
    
    // 如果有项目上下文，添加进去
    if (context.project) {
      messages.unshift({
        role: 'system',
        content: `【项目上下文】\n名称: ${context.project.name}\n状态: ${context.project.status}\nPRD: ${context.project.prd || '无'}`
      });
    }

    const response = await this.callGLM(messages);
    
    console.log(`[云捌] 任务完成，token消耗: ${response.usage?.total_tokens || 'unknown'}`);
    
    return {
      role: this.name,
      content: response.content,
      reasoning: response.reasoning,
      timestamp: new Date().toISOString()
    };
  }

  // 项目管理专用方法
  async manageProject(projectName, action) {
    const task = `【项目管理】项目: ${projectName}\n行动: ${action}`;
    return this.handleTask(task, { project: { name: projectName, status: 'active' } });
  }

  // 产品分析专用方法
  async analyzeProduct(requirement) {
    const task = `【产品分析】\n需求: ${requirement}\n请输出:\n1. 需求理解\n2. 用户场景分析\n3. 功能建议\n4. PRD大纲`;
    return this.handleTask(task);
  }

  // 风险评估专用方法
  async assessRisk(projectContext) {
    const task = `【风险评估】\n项目背景: ${projectContext}\n请识别:\n1. 潜在风险点\n2. 风险等级\n3. 应对策略\n4. 预警指标`;
    return this.handleTask(task);
  }
}

module.exports = { YunBaAgent };

// 测试
if (require.main === module) {
  const agent = new YunBaAgent();
  console.log('云捌智能体已加载');
  console.log('模型:', agent.model);
  console.log('API Key:', agent.zhipuApiKey ? '✓ 已配置' : '✗ 未配置');
  
  // 简单测试
  agent.handleTask('你好，简短自我介绍').then(res => {
    console.log('\n测试回复:', res.content.substring(0, 100) + '...');
  }).catch(err => {
    console.error('测试失败:', err.message);
  });
}

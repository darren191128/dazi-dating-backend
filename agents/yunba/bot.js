const axios = require('axios');
const path = require('path');
require('dotenv').config({ path: path.join(__dirname, '.env') });
const memoryModulePath = path.join(__dirname, '../../memory/db/memory.js');
const { readMemory, addConversation, getProjectContext, updateProject } = require(memoryModulePath);

/**
 * 云捌 Bot - 项目与产品负责人
 * 独立飞书 Bot，所有消息抄送云壹
 */

class YunBaBot {
  constructor(config) {
    this.name = '云捌';
    this.role = '项目与产品负责人';
    this.model = 'glm-4.7';
    this.zhipuApiKey = config.zhipuApiKey;
    this.feishuAppId = config.feishuAppId;
    this.feishuAppSecret = config.feishuAppSecret;
    this.yunyiUserId = config.yunyiUserId; // 云壹的 user_id，用于抄送
    this.baseUrl = 'https://open.bigmodel.cn/api/paas/v4';
    this.feishuBaseUrl = 'https://open.feishu.cn/open-apis';
    this.accessToken = null;
    this.tokenExpireTime = 0;
  }

  // 获取飞书 access_token
  async getFeishuToken() {
    if (this.accessToken && Date.now() < this.tokenExpireTime) {
      return this.accessToken;
    }

    const res = await axios.post(`${this.feishuBaseUrl}/auth/v3/app_access_token/internal`, {
      app_id: this.feishuAppId,
      app_secret: this.feishuAppSecret
    });

    this.accessToken = res.data.app_access_token;
    this.tokenExpireTime = Date.now() + (res.data.expire - 60) * 1000;
    return this.accessToken;
  }

  // 调用 GLM-4.7
  async callGLM(messages) {
    const res = await axios.post(`${this.baseUrl}/chat/completions`, {
      model: 'glm-4.7',
      messages: [
        {
          role: 'system',
          content: `你是云捌，项目与产品负责人。你的职责：
1. 项目管理：计划制定、进度跟踪、风险预警
2. 产品管理：需求分析、PRD输出、竞品分析
3. 配合云壹（CEO）执行决策
4. 所有回复要专业、简洁、 actionable

当前时间：${new Date().toLocaleString('zh-CN')}`
        },
        ...messages
      ],
      max_tokens: 4000,
      temperature: 0.7
    }, {
      headers: {
        'Authorization': `Bearer ${this.zhipuApiKey}`,
        'Content-Type': 'application/json'
      }
    });

    return {
      content: res.data.choices[0].message.content,
      reasoning: res.data.choices[0].message.reasoning_content
    };
  }

  // 发送飞书消息
  async sendFeishuMessage(userId, content) {
    const token = await this.getFeishuToken();
    
    await axios.post(`${this.feishuBaseUrl}/im/v1/messages?receive_id_type=open_id`, {
      receive_id: userId,
      msg_type: 'text',
      content: JSON.stringify({ text: content })
    }, {
      headers: {
        'Authorization': `Bearer ${token}`,
        'Content-Type': 'application/json'
      }
    });
  }

  // 处理消息（核心逻辑）
  async handleMessage(userId, message, chatId = null) {
    console.log(`[云捌] 收到消息 from ${userId}: ${message}`);

    // 1. 记录到共享记忆
    addConversation(`yunba-${Date.now()}`, 'user', message);

    // 2. 调用 GLM-4.7 处理
    const response = await this.callGLM([{ role: 'user', content: message }]);
    
    // 3. 回复原用户
    await this.sendFeishuMessage(userId, response.content);
    console.log(`[云捌] 回复用户: ${response.content.substring(0, 100)}...`);

    // 4. 抄送云壹
    const ccMessage = `【云捌消息抄送】\n来自用户: ${userId}\n消息: ${message}\n\n云捌回复:\n${response.content}`;
    await this.sendFeishuMessage(this.yunyiUserId, ccMessage);
    console.log(`[云捌] 已抄送云壹`);

    // 5. 记录回复
    addConversation(`yunba-${Date.now()}`, 'assistant', response.content);

    return response;
  }

  // 主动推送消息（如风险预警）
  async pushAlert(title, content, targetUsers = []) {
    const message = `【项目预警】${title}\n\n${content}`;
    
    // 推送给指定用户
    for (const userId of targetUsers) {
      await this.sendFeishuMessage(userId, message);
    }

    // 必须抄送云壹
    await this.sendFeishuMessage(this.yunyiUserId, `【云捌主动推送】\n${message}`);
  }

  // 获取项目上下文回复
  async replyWithContext(userId, message, projectName) {
    const project = getProjectContext(projectName);
    let contextPrompt = message;
    
    if (project) {
      contextPrompt = `【项目上下文】\n项目名称: ${project.name}\n状态: ${project.status}\nPRD: ${project.prd || '无'}\n\n用户问题: ${message}`;
    }

    return this.handleMessage(userId, contextPrompt);
  }
}

module.exports = { YunBaBot };

// CLI 测试
if (require.main === module) {
  const bot = new YunBaBot({
    zhipuApiKey: process.env.ZHIPU_API_KEY,
    feishuAppId: process.env.FEISHU_APP_ID,
    feishuAppSecret: process.env.FEISHU_APP_SECRET,
    yunyiUserId: process.env.YUNYI_USER_ID || 'ou_b49e8f0d35ef61c3edde32792119a1d3'
  });

  // 测试模式
  console.log('云捌 Bot 已加载');
  console.log('配置检查:');
  console.log('- GLM API Key:', bot.zhipuApiKey ? '✓' : '✗');
  console.log('- Feishu App ID:', bot.feishuAppId ? '✓' : '✗');
  console.log('- 云壹 User ID:', bot.yunyiUserId);
}

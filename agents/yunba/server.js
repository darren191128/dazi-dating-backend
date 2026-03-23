const express = require('express');
const crypto = require('crypto');
const path = require('path');
require('dotenv').config({ path: path.join(__dirname, '.env') });
const { YunBaBot } = require('./bot.js');

/**
 * 云捌飞书 Bot 服务
 * 接收飞书事件推送，处理消息并回复
 */

const app = express();
app.use(express.json({ verify: (req, res, buf) => { req.rawBody = buf; } }));

// 初始化 Bot
const bot = new YunBaBot({
  zhipuApiKey: process.env.ZHIPU_API_KEY,
  feishuAppId: process.env.FEISHU_APP_ID,
  feishuAppSecret: process.env.FEISHU_APP_SECRET,
  yunyiUserId: process.env.YUNYI_USER_ID || 'ou_b49e8f0d35ef61c3edde32792119a1d3'
});

// 验证飞书请求签名
function verifySignature(nonce, timestamp, token, signature, body) {
  const signStr = `${timestamp}\n${nonce}\n${token}\n${body}`;
  const hash = crypto.createHmac('sha256', token).update(signStr).digest('hex');
  return hash === signature;
}

// 健康检查
app.get('/health', (req, res) => {
  res.json({ status: 'ok', bot: '云捌', role: '项目与产品负责人' });
});

// 飞书事件回调
app.post('/webhook', async (req, res) => {
  try {
    const { header, event } = req.body;

    // URL 验证（首次配置时需要）
    if (req.body.challenge) {
      console.log('[云捌] 收到验证请求');
      return res.json({ challenge: req.body.challenge });
    }

    // 处理消息事件
    if (event && event.message) {
      const { message } = event;
      const userId = message.sender.sender_id.open_id;
      const chatId = message.message.chat_id;
      const msgType = message.message.message_type;
      
      // 只处理文本消息
      if (msgType === 'text') {
        const content = JSON.parse(message.message.content);
        const text = content.text;

        console.log(`[云捌] 收到: ${text}`);

        // 异步处理消息
        bot.handleMessage(userId, text, chatId).catch(err => {
          console.error('[云捌] 处理消息失败:', err);
        });
      }
    }

    res.json({ code: 0 });
  } catch (err) {
    console.error('[云捌] Webhook 错误:', err);
    res.json({ code: 0 }); // 飞书要求始终返回 200
  }
});

// 启动服务
const PORT = process.env.PORT || 3001;
app.listen(PORT, () => {
  console.log(`
╔════════════════════════════════════╗
║  云捌 Bot 服务已启动                ║
║  端口: ${PORT}                      ║
║  角色: 项目与产品负责人              ║
║  模型: GLM-4.7                      ║
║  抄送: 云壹 (CEO)                   ║
╚════════════════════════════════════╝
  `);
});

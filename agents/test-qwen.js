const axios = require('axios');

/**
 * Qwen-Plus 模型测试
 * 测试云伍、云陆、云柒使用的模型
 */

async function testQwenPlus() {
  console.log('╔════════════════════════════════════════╗');
  console.log('║     Qwen-Plus 模型测试                 ║');
  console.log('╚════════════════════════════════════════╝\n');

  const apiKey = process.env.DASHSCOPE_API_KEY;
  const baseUrl = 'https://dashscope.aliyuncs.com/api/v1';

  const tests = [
    {
      name: '云伍 (前端)',
      system: '你是云伍，前端工程师。擅长React、Vue、TypeScript。',
      prompt: '写一个React函数组件，展示用户列表，要求使用TypeScript和Hooks。'
    },
    {
      name: '云陆 (后端)',
      system: '你是云陆，后端工程师。擅长Node.js、数据库设计、API架构。',
      prompt: '设计一个用户认证系统的API接口，包括注册、登录、token刷新。'
    },
    {
      name: '云柒 (测试)',
      system: '你是云柒，测试工程师。擅长测试用例设计、自动化测试。',
      prompt: '为登录功能设计5个测试用例，包括正常和异常场景。'
    }
  ];

  for (const test of tests) {
    console.log(`【${test.name}】`);
    console.log('─────────────────────────────────────');
    
    try {
      const start = Date.now();
      const res = await axios.post(
        `${baseUrl}/services/aigc/text-generation/generation`,
        {
          model: 'qwen-plus',
          input: {
            messages: [
              { role: 'system', content: test.system },
              { role: 'user', content: test.prompt }
            ]
          }
        },
        {
          headers: {
            'Authorization': `Bearer ${apiKey}`,
            'Content-Type': 'application/json'
          }
        }
      );
      
      const duration = Date.now() - start;
      const content = res.data.output.text;
      const usage = res.data.usage;
      
      console.log(`✓ 响应成功 (${duration}ms)`);
      console.log(`  Token: ${usage.total_tokens} (输入:${usage.input_tokens} 输出:${usage.output_tokens})`);
      console.log(`  回复预览: ${content.substring(0, 80)}...`);
    } catch (err) {
      console.log(`✗ 失败: ${err.message}`);
    }
    console.log('');
  }

  console.log('╔════════════════════════════════════════╗');
  console.log('║        Qwen-Plus 测试完成              ║');
  console.log('╚════════════════════════════════════════╝');
}

testQwenPlus().catch(console.error);

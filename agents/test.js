const { YunYiGateway } = require('./gateway.js');

/**
 * 多智能体系统测试脚本
 */

async function testGateway() {
  console.log('╔════════════════════════════════════════╗');
  console.log('║     多智能体系统 - 功能测试            ║');
  console.log('╚════════════════════════════════════════╝\n');

  const gateway = new YunYiGateway();

  // 测试1: 路由规则
  console.log('【测试1】路由规则测试');
  console.log('─────────────────────────────────────');
  const routeTests = [
    { msg: '@云捌 制定项目计划', expected: 'yunba' },
    { msg: '项目进度怎么样了', expected: 'yunba' },
    { msg: '帮我写个PRD', expected: 'yunba' },
    { msg: '分析一下需求', expected: 'self' },
    { msg: '云壹，你好', expected: 'self' }
  ];

  for (const test of routeTests) {
    const result = gateway.route(test.msg);
    const status = result === test.expected ? '✓' : '✗';
    console.log(`${status} "${test.msg.substring(0, 20)}..." → ${result}`);
  }
  console.log('');

  // 测试2: 云捌调用
  console.log('【测试2】云捌智能体调用 (GLM-4.7)');
  console.log('─────────────────────────────────────');
  try {
    const yunbaResult = await gateway.callYunBa('简短自我介绍');
    console.log('✓ 云捌响应成功');
    console.log(`  回复: ${yunbaResult.content.substring(0, 60)}...`);
    console.log(`  Token: ${yunbaResult.usage?.total_tokens || 'unknown'}`);
  } catch (err) {
    console.log('✗ 云捌调用失败:', err.message);
  }
  console.log('');

  // 测试3: 网关完整流程
  console.log('【测试3】网关完整流程测试');
  console.log('─────────────────────────────────────');
  try {
    const result = await gateway.handleMessage('@云捌 制定一个官网项目的3个里程碑');
    console.log('✓ 网关处理成功');
    console.log(`  路由: ${result.delegate ? '云捌' : '云壹'}`);
    console.log(`  回复长度: ${result.content.length} 字符`);
  } catch (err) {
    console.log('✗ 网关处理失败:', err.message);
  }
  console.log('');

  console.log('╔════════════════════════════════════════╗');
  console.log('║           测试完成                     ║');
  console.log('╚════════════════════════════════════════╝');
}

testGateway().catch(console.error);

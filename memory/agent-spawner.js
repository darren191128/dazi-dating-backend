#!/usr/bin/env node
/**
 * 智能体调度器 - 集成共享记忆
 * CEO调用子智能体时使用
 */

const path = require('path');
const { getProjectContext, addConversation } = require(path.join(__dirname, 'db/memory.js'));
const { YunYiGateway } = require(path.join(__dirname, '../agents/gateway.js'));

// 生成带上下文的任务描述
function buildTaskWithContext(role, task, projectName) {
  let context = '';
  
  // 获取项目上下文
  if (projectName) {
    const project = getProjectContext(projectName);
    if (project) {
      context += `\n【项目上下文】\n`;
      context += `- 项目名称: ${project.name}\n`;
      context += `- 项目状态: ${project.status}\n`;
      context += `- PRD文档: ${project.prd}\n`;
      context += `- 设计文档: ${project.design}\n`;
      context += `- 代码位置: ${project.code}\n`;
    }
  }
  
  // 获取团队信息
  const team = require('./memory/db/memory.js').getKnowledge('team_structure');
  if (team) {
    context += `\n【团队结构】\n${team.value}\n`;
  }
  
  // 角色定义
  const roleMap = {
    '云肆': 'UI设计师 - 负责视觉设计和交互设计',
    '云伍': '前端工程师 - 负责前端开发和界面实现',
    '云陆': '后端工程师 - 负责后端开发和API设计',
    '云柒': '测试工程师 - 负责质量保障和测试',
    '云捌': '项目与产品负责人 - 统筹项目管理、需求分析、产品规划，配合云壹执行CEO级决策'
  };
  
  const roleDesc = roleMap[role] || role;
  
  return `【角色】${roleDesc}${context}\n\n【任务】${task}\n\n【要求】\n1. 使用共享记忆系统读取项目上下文\n2. 完成任务后，将关键结果写入memory.json\n3. 返回简洁的执行结果`;
}

// 调用子智能体
async function spawnAgent(role, task, projectName, timeout = 120) {
  // 记录到共享记忆
  addConversation(`task-${Date.now()}`, 'CEO', `分配任务给${role}: ${task}`);
  
  console.log(`🚀 调度 ${role} 执行任务...`);
  
  // 使用网关调度
  const gateway = new YunYiGateway();
  const context = projectName ? { project: getProjectContext(projectName) } : {};
  
  // 构建带角色标识的消息
  const message = role === '云捌' ? `@云捌 ${task}` : task;
  
  const result = await gateway.handleMessage(message, context);
  
  console.log('\n=== 执行结果 ===');
  console.log(`角色: ${result.role}`);
  console.log(`回复: ${result.content.substring(0, 200)}...`);
  console.log('================\n');
  
  return result;
}

// CLI接口
const [,, cmd, role, task, projectName] = process.argv;

if (cmd === 'spawn') {
  spawnAgent(role, task, projectName).catch(console.error);
} else {
  console.log('用法: node agent-spawner.js spawn <角色> <任务> [项目名称]');
  console.log('示例: node agent-spawner.js spawn "云捌" "制定项目计划" "启云官网"');
}

module.exports = { spawnAgent, buildTaskWithContext };

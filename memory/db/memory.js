#!/usr/bin/env node
/**
 * 共享记忆系统 - 内存读写模块
 * 供云壹和子智能体使用
 */

const fs = require('fs');
const path = require('path');

const MEMORY_FILE = '/root/.openclaw/workspace/memory/db/memory.json';

// 读取记忆
function readMemory() {
  try {
    const data = fs.readFileSync(MEMORY_FILE, 'utf8');
    return JSON.parse(data);
  } catch (e) {
    return { conversations: [], projects: [], knowledge: [] };
  }
}

// 写入记忆
function writeMemory(memory) {
  fs.writeFileSync(MEMORY_FILE, JSON.stringify(memory, null, 2));
}

// 添加对话
function addConversation(sessionId, role, content) {
  const memory = readMemory();
  memory.conversations.push({
    id: Date.now(),
    session_id: sessionId,
    role: role,
    content: content,
    timestamp: new Date().toISOString()
  });
  writeMemory(memory);
}

// 获取项目上下文
function getProjectContext(projectName) {
  const memory = readMemory();
  return memory.projects.find(p => p.name === projectName);
}

// 更新项目
function updateProject(projectName, updates) {
  const memory = readMemory();
  const idx = memory.projects.findIndex(p => p.name === projectName);
  if (idx >= 0) {
    memory.projects[idx] = { ...memory.projects[idx], ...updates };
    writeMemory(memory);
  }
}

// 添加项目
function addProject(projectName, prd = '', design = '', code = '') {
  const memory = readMemory();
  memory.projects.push({
    id: Date.now(),
    name: projectName,
    status: "规划中",
    prd: prd,
    design: design,
    code: code,
    timestamp: new Date().toISOString()
  });
  writeMemory(memory);
}

// 获取知识
function getKnowledge(key) {
  const memory = readMemory();
  return memory.knowledge.find(k => k.key === key);
}

// 添加知识
function addKnowledge(key, value, type = 'general') {
  const memory = readMemory();
  const existing = memory.knowledge.findIndex(k => k.key === key);
  if (existing >= 0) {
    memory.knowledge[existing] = { ...memory.knowledge[existing], value, timestamp: new Date().toISOString() };
  } else {
    memory.knowledge.push({
      id: Date.now(),
      key: key,
      value: value,
      type: type,
      timestamp: new Date().toISOString()
    });
  }
  writeMemory(memory);
}

// CLI接口
const [,, cmd, ...args] = process.argv;

switch (cmd) {
  case 'read':
    console.log(JSON.stringify(readMemory(), null, 2));
    break;
  case 'get-project':
    console.log(JSON.stringify(getProjectContext(args[0]), null, 2));
    break;
  case 'get-knowledge':
    console.log(JSON.stringify(getKnowledge(args[0]), null, 2));
    break;
  case 'add-conversation':
    addConversation(args[0], args[1], args.slice(2).join(' '));
    console.log('✅ 对话已记录');
    break;
  case 'add-project':
    addProject(args[0], args[1] || '', args[2] || '', args[3] || '');
    console.log('✅ 项目已添加');
    break;
  case 'update-project':
    const updates = JSON.parse(args.slice(1).join(' '));
    updateProject(args[0], updates);
    console.log('✅ 项目已更新');
    break;
  default:
    console.log('用法:');
    console.log('  node memory.js read');
    console.log('  node memory.js get-project "项目名称"');
    console.log('  node memory.js get-knowledge "key"');
    console.log('  node memory.js add-conversation <session_id> <role> <content>');
    console.log('  node memory.js add-project <项目名称> [PRD路径] [设计路径] [代码路径]');
    console.log('  node memory.js update-project <项目名称> \'{JSON格式的更新内容}\'');
}

module.exports = { readMemory, addConversation, getProjectContext, updateProject, addProject, getKnowledge, addKnowledge };

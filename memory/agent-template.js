/**
 * 子智能体模板 - 集成共享记忆
 * 供云贰~云柒使用
 */

const { readMemory, getProjectContext, addConversation, updateProject } = require('../db/memory.js');

class Agent {
  constructor(role) {
    this.role = role;
    this.sessionId = `session-${Date.now()}`;
  }
  
  // 读取项目上下文
  getContext(projectName) {
    return getProjectContext(projectName);
  }
  
  // 记录工作日志
  log(message) {
    console.log(`[${this.role}] ${message}`);
    addConversation(this.sessionId, this.role, message);
  }
  
  // 更新项目状态
  updateProjectStatus(projectName, updates) {
    updateProject(projectName, updates);
    this.log(`更新项目状态: ${JSON.stringify(updates)}`);
  }
  
  // 完成任务
  complete(task, result) {
    this.log(`完成任务: ${task}`);
    addConversation(this.sessionId, 'system', `任务完成: ${result}`);
    return {
      role: this.role,
      task: task,
      result: result,
      timestamp: new Date().toISOString()
    };
  }
}

// 导出供子智能体使用
module.exports = { Agent };

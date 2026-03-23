# 云壹 - 核心记忆文件

**身份:** CEO助理  
**创建时间:** 2026-03-22  
**最后更新:** 2026-03-22 23:26

---

## 老板信息

- **称呼:** 老板
- **时区:** Asia/Shanghai (GMT+8)
- **期望:** 高效专业的协助

---

## 核心规则（必须遵守）

1. **每次对话后立即记录** - 写入 `memory/YYYY-MM-DD.md`
2. **任务必须追踪** - 更新 `tasks.md`
3. **使用技能系统** - 根据任务类型调用适配技能
4. **多智能体协作** - 复杂任务使用并行调度
5. **GitHub版本控制** - 代码必须推送

---

## 当前进行中的项目

### 搭子交友微信小程序 ✅ 已完成
- 状态: 100% 完成
- 位置: `projects/dazi-social-app/`
- GitHub: 
  - 前端: https://github.com/darren191128/dazi-dating-frontend
  - 后端: https://github.com/darren191128/dazi-dating-backend

---

## 重要文件位置

| 文件 | 路径 | 用途 |
|------|------|------|
| 每日日志 | `memory/YYYY-MM-DD.md` | 记录每日沟通 |
| 任务追踪 | `memory/tasks.md` | 追踪所有任务 |
| 老板偏好 | `memory/preferences.md` | 记录偏好习惯 |
| 技能配置 | `CORE_SKILLS.md` | 技能调用规则 |
| 项目档案 | `PROJECT_*.md` | 项目信息 |

---

## 技能调用速查

| 任务类型 | 技能 | 模型 |
|----------|------|------|
| PRD编写 | ai-prompt-engineering-safety-review | GLM-4.7 |
| 数据库设计 | architecture-blueprint-generator | Qwen-Plus |
| API设计 | api-design-principles | Qwen-Plus |
| 前端开发 | coding-agent | Qwen-Plus |
| 后端开发 | coding-agent | Qwen-Plus |
| 代码审查 | code-simplifier | Kimi-K2.5 |
| 部署文档 | cicd-pipeline | Qwen-Plus |

---

## 智能体角色

| 角色 | 模型 | 职责 |
|------|------|------|
| 云壹 | Kimi-K2.5 | CEO助理、统筹 |
| 云捌 | GLM-4.7 | 项目负责、规划 |
| 云肆 | Qwen-Plus | 设计经理 |
| 云伍 | Qwen-Plus | 前端经理 |
| 云陆 | Qwen-Plus | 后端经理 |
| 云柒 | Qwen-Plus | 测试经理 |

---

**唤醒后首先读取此文件，了解上下文。**

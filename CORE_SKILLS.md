# 核心技能配置文件 (完整版)

**创建时间:** 2026-03-22  
**用途:** 根据任务类型自动调用适配技能  
**技能总数:** 192个

---

## 一、产品开发类技能

### PRD/需求文档
| 技能名 | 用途 | 模型 | 触发词 |
|--------|------|------|--------|
| `ai-prompt-engineering-safety-review` | PRD/需求文档编写 | GLM-4.7 | "写PRD", "需求文档" |
| `write-a-prd` | PRD生成 | GLM-4.7 | "PRD", "产品文档" |
| `brainstorming` | 需求 brainstorm | Kimi-K2.5 | "头脑风暴", "想法" |

### 架构设计
| 技能名 | 用途 | 模型 | 触发词 |
|--------|------|------|--------|
| `architecture-blueprint-generator` | 架构蓝图生成 | Qwen-Plus | "架构设计", "技术架构" |
| `architecture-patterns` | 架构模式 | Qwen-Plus | "架构模式", "设计模式" |
| `api-design-principles` | API设计 | Qwen-Plus | "API设计", "接口设计" |

### 数据库设计
| 技能名 | 用途 | 模型 | 触发词 |
|--------|------|------|--------|
| `supabase-postgres-best-practices` | PostgreSQL设计 | Qwen-Plus | "PostgreSQL", "数据库" |

### UI/UX设计
| 技能名 | 用途 | 模型 | 触发词 |
|--------|------|------|--------|
| `canvas-design` | UI设计 | Kimi-K2.5 | "UI设计", "界面设计" |
| `web-design-guidelines` | 网页设计规范 | Kimi-K2.5 | "网页设计", "设计规范" |
| `brand-guidelines` | 品牌设计 | Kimi-K2.5 | "品牌", "Logo" |
| `algorithmic-art` | 算法艺术 | Kimi-K2.5 | "生成艺术", "算法艺术" |

---

## 二、代码开发类技能

### 前端开发
| 技能名 | 用途 | 模型 | 触发词 |
|--------|------|------|--------|
| `coding-agent` | 通用代码开发 | Qwen-Plus | "开发", "编码" |
| `vue` | Vue开发 | Qwen-Plus | "Vue", "Vue3" |
| `vue-best-practices` | Vue最佳实践 | Qwen-Plus | "Vue最佳实践" |
| `vue-pinia-best-practices` | Pinia状态管理 | Qwen-Plus | "Pinia", "状态管理" |
| `vue-router-best-practices` | Vue Router | Qwen-Plus | "Vue Router", "路由" |
| `vue-testing-best-practices` | Vue测试 | Qwen-Plus | "Vue测试", "单元测试" |
| `react` | React开发 | Qwen-Plus | "React" |
| `react-best-practices` | React最佳实践 | Qwen-Plus | "React最佳实践" |
| `nextjs` | Next.js开发 | Qwen-Plus | "Next.js", "Next" |
| `typescript-advanced-types` | TypeScript高级类型 | Qwen-Plus | "TypeScript", "TS" |
| `vite` | Vite构建工具 | Qwen-Plus | "Vite" |
| `unocss` | UnoCSS原子CSS | Qwen-Plus | "UnoCSS", "原子CSS" |
| `tailwind-setup` | Tailwind配置 | Qwen-Plus | "Tailwind", "CSS" |

### 后端开发
| 技能名 | 用途 | 模型 | 触发词 |
|--------|------|------|--------|
| `better-auth-best-practices` | 认证最佳实践 | Qwen-Plus | "认证", "Auth" |
| `create-auth-skill` | 创建认证技能 | Qwen-Plus | "创建认证" |
| `supabase-postgres-best-practices` | Supabase/Postgres | Qwen-Plus | "Supabase" |

### 移动端开发
| 技能名 | 用途 | 模型 | 触发词 |
|--------|------|------|--------|
| `expo-building-native-ui` | Expo原生UI | Qwen-Plus | "Expo", "React Native" |
| `expo-tailwind-setup` | Expo Tailwind | Qwen-Plus | "Expo Tailwind" |
| `upgrading-expo` | Expo升级 | Qwen-Plus | "升级Expo" |
| `upgrading-react-native` | RN升级 | Qwen-Plus | "升级RN" |

### 代码质量
| 技能名 | 用途 | 模型 | 触发词 |
|--------|------|------|--------|
| `code-simplifier` | 代码简化 | Kimi-K2.5 | "简化代码", "重构" |
| `code-exemplars-blueprint-generator` | 代码示例生成 | Qwen-Plus | "代码示例", "示例代码" |
| `add-educational-comments` | 添加教育注释 | Qwen-Plus | "注释", "代码注释" |
| `test-driven-development` | TDD测试驱动 | Qwen-Plus | "TDD", "测试驱动" |
| `systematic-debugging` | 系统调试 | Qwen-Plus | "调试", "Debug" |

---

## 三、测试部署类技能

### 测试
| 技能名 | 用途 | 模型 | 触发词 |
|--------|------|------|--------|
| `agentic-eval` | AI评估 | Qwen-Plus | "评估", "评测" |
| `vitest` | Vitest测试 | Qwen-Plus | "Vitest", "测试" |
| `webapp-testing` | Web应用测试 | Qwen-Plus | "Web测试", "E2E" |
| `apple-appstore-reviewer` | App Store审核 | Qwen-Plus | "App Store", "审核" |

### 部署/DevOps
| 技能名 | 用途 | 模型 | 触发词 |
|--------|------|------|--------|
| `cicd-pipeline` | CI/CD流水线 | Qwen-Plus | "CI/CD", "流水线" |
| `vercel-skills` | Vercel部署 | Qwen-Plus | "Vercel", "部署" |
| `docker` | Docker容器化 | Qwen-Plus | "Docker", "容器" |
| `kubernetes` | K8s编排 | Qwen-Plus | "K8s", "Kubernetes" |
| `using-git-worktrees` | Git工作树 | Qwen-Plus | "Git", "Worktree" |

---

## 四、项目管理类技能

| 技能名 | 用途 | 模型 | 触发词 |
|--------|------|------|--------|
| `breakdown-feature-implementation` | 任务分解 | GLM-4.7 | "任务分解", "排期" |
| `dispatching-parallel-agents` | 并行智能体调度 | GLM-4.7 | "并行", "多任务" |
| `executing-plans` | 执行计划 | GLM-4.7 | "执行计划" |
| `verification-before-completion` | 完成前验证 | GLM-4.7 | "验证", "检查" |

---

## 五、文档写作类技能

| 技能名 | 用途 | 模型 | 触发词 |
|--------|------|------|--------|
| `doc-coauthoring` | 文档协作 | Kimi-K2.5 | "写文档", "技术文档" |
| `docx` | Word文档 | Kimi-K2.5 | "Word", "docx" |
| `xlsx` | Excel表格 | Kimi-K2.5 | "Excel", "表格" |
| `writing-skills` | 写作技能 | Kimi-K2.5 | "写作", "文案" |
| `writing-plans` | 写作计划 | Kimi-K2.5 | "写作计划" |
| `communication-playbook` | 沟通手册 | Kimi-K2.5 | "沟通", "手册" |

---

## 六、内容创作类技能

### 营销内容
| 技能名 | 用途 | 模型 | 触发词 |
|--------|------|------|--------|
| `copywriting` | 营销文案 | Kimi-K2.5 | "文案", "营销" |
| `copy-editing` | 文案编辑 | Kimi-K2.5 | "编辑文案" |
| `content-strategy` | 内容策略 | Kimi-K2.5 | "内容策略" |
| `competitor-alternatives` | 竞品对比 | Kimi-K2.5 | "竞品", "对比" |
| `ab-test-setup` | A/B测试 | Kimi-K2.5 | "A/B测试", "实验" |
| `analytics-tracking` | 分析追踪 | Kimi-K2.5 | "分析", "追踪" |

### 社交媒体
| 技能名 | 用途 | 模型 | 触发词 |
|--------|------|------|--------|
| `xhs-note-creator` | 小红书笔记 | Kimi-K2.5 | "小红书", "笔记" |
| `xhs-toolkit` | 小红书工具 | Kimi-K2.5 | "小红书工具" |
| `tiktok-viral-predictor` | TikTok病毒预测 | Kimi-K2.5 | "TikTok", "病毒" |
| `weibo-trending-bot` | 微博热搜 | Kimi-K2.5 | "微博", "热搜" |
| `youtube-auto-captions` | YouTube字幕 | Kimi-K2.5 | "YouTube", "字幕" |

---

## 七、数据分析类技能

| 技能名 | 用途 | 模型 | 触发词 |
|--------|------|------|--------|
| `data-analyst` | 数据分析 | Qwen-Plus | "数据分析" |
| `tech-data-playbook` | 技术数据手册 | Qwen-Plus | "技术数据" |
| `tavily-search` | Tavily搜索 | Qwen-Plus | "搜索", "Tavily" |
| `clean-content-fetch` | 内容抓取 | Qwen-Plus | "抓取", "爬虫" |

---

## 八、AI/智能体类技能

| 技能名 | 用途 | 模型 | 触发词 |
|--------|------|------|--------|
| `agent-governance` | 智能体治理 | GLM-4.7 | "治理", "安全" |
| `agentic-eval` | 智能体评估 | Qwen-Plus | "评估" |
| `ai-travel` | AI旅行 | Kimi-K2.5 | "旅行", "AI旅行" |
| `ai-prompt-generator` | Prompt生成 | Kimi-K2.5 | "Prompt", "提示词" |
| `swarmclaw` | 群体智能 | GLM-4.7 | "群体", "Swarm" |
| `superpowers` | 超能力 | GLM-4.7 | "超能力" |
| `using-superpowers` | 使用超能力 | GLM-4.7 | "使用超能力" |

---

## 九、浏览器/工具类技能

| 技能名 | 用途 | 模型 | 触发词 |
|--------|------|------|--------|
| `browser-use` | 浏览器自动化 | Qwen-Plus | "浏览器", "自动化" |
| `chrome-devtools` | Chrome开发者工具 | Qwen-Plus | "DevTools", "调试" |
| `dev-browser` | 开发浏览器 | Qwen-Plus | "开发浏览器" |
| `audit-website` | 网站审计 | Qwen-Plus | "审计", "检测" |

---

## 十、其他专业类技能

| 技能名 | 用途 | 模型 | 触发词 |
|--------|------|------|--------|
| `clankers-world` | Clankers World | Kimi-K2.5 | "Clankers" |
| `claude-scientific-skills` | 科学技能 | Kimi-K2.5 | "科学" |
| `trader-daily` | 交易员日常 | Qwen-Plus | "交易", "股票" |
| `theme-factory` | 主题工厂 | Kimi-K2.5 | "主题" |
| `widget` | 小部件 | Qwen-Plus | "Widget", "小组件" |
| `web-artifacts-builder` | Web构件构建 | Qwen-Plus | "构件", "Artifacts" |
| `web-component-design` | Web组件设计 | Qwen-Plus | "Web组件" |

---

## 智能体角色-模型映射

| 角色 | 模型 | 用途 |
|------|------|------|
| 云壹 (CEO助理) | moonshot/kimi-k2.5 | 统筹、决策 |
| 云捌 (项目负责) | zhipu/glm-4.7 | 规划、分析 |
| 云肆 (设计经理) | aliyun/qwen-plus | 设计、创意 |
| 云伍 (前端经理) | aliyun/qwen-plus | 前端开发 |
| 云陆 (后端经理) | aliyun/qwen-plus | 后端开发 |
| 云柒 (测试经理) | aliyun/qwen-plus | 测试、质量 |

---

## 任务处理流程

1. **接收任务** → 分析任务类型
2. **匹配技能** → 根据技能配置选择
3. **选择模型** → 根据角色选择模型
4. **执行任务** → 调用技能处理
5. **记录结果** → 保存到记忆文件

---

**重要:** 每次任务执行后，必须更新 `communication-log.md` 和 `tasks.md`

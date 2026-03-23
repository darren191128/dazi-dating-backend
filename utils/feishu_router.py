# Feishu Group Router
# 七人智能体团队 - 云壹至云柒
# 根据飞书群组自动路由到不同角色子代理

import yaml
import json
from pathlib import Path

# 加载路由配置
ROUTES_PATH = Path("/root/.openclaw/workspace/config/feishu-routes.yaml")
TEAM_PATH = Path("/root/.openclaw/workspace/config/agents-team.yaml")

def load_routes():
    """加载群组路由配置"""
    if ROUTES_PATH.exists():
        with open(ROUTES_PATH, 'r', encoding='utf-8') as f:
            return yaml.safe_load(f)
    return {"routes": [], "default": {"agent": "云壹", "role": "CEO助理", "model": "moonshot/kimi-k2.5"}}

def load_team():
    """加载团队角色定义"""
    if TEAM_PATH.exists():
        with open(TEAM_PATH, 'r', encoding='utf-8') as f:
            return yaml.safe_load(f)
    return {}

def get_agent_for_chat(chat_id: str) -> dict:
    """
    根据 chat_id 获取对应智能体配置
    
    Returns:
        {
            "agent": "云贰",
            "role": "项目经理",
            "description": "...",
            "model": "moonshot/kimi-k2.5"
        }
    """
    routes_config = load_routes()
    team_config = load_team()
    
    # 查找匹配的路由
    for route in routes_config.get("routes", []):
        if route.get("chat_id") == chat_id:
            return {
                "agent": route.get("agent", "云壹"),
                "role": route["role"],
                "description": route.get("description", ""),
                "model": route.get("model", "moonshot/kimi-k2.5"),
                "fallback_model": route.get("fallback_model")
            }
    
    # 返回默认配置 (云壹 - CEO助理)
    default = routes_config.get("default", {})
    return {
        "agent": default.get("agent", "云壹"),
        "role": default.get("role", "CEO助理"),
        "description": default.get("description", "统筹一切，替老板分忧解难"),
        "model": default.get("model", "moonshot/kimi-k2.5"),
        "fallback_model": None
    }

def build_system_prompt(agent_config: dict) -> str:
    """
    构建智能体系统提示词
    
    Args:
        agent_config: 智能体配置
    
    Returns:
        系统提示词
    """
    agent_name = agent_config["agent"]
    role = agent_config["role"]
    description = agent_config["description"]
    
    prompts = {
        "云壹": f"""你是云壹，CEO助理。专业、高效、可靠 — 像真正的CEO一样思考和行动。
专业不是口号，是行动。老板指方向，你负责落地。

核心能力：统筹规划、资源协调、决策支持、执行监督

回复风格：简洁专业，直击要点，给出可执行的方案。""",
        
        "云贰": f"""你是云贰，项目经理。擅长项目规划、进度管理、资源协调、风险控制。
确保项目按时、按质、按预算交付。善于沟通协调，推动团队高效协作。

核心能力：项目规划、进度跟踪、风险管理、团队协调

回复风格：结构化思维，关注时间节点，强调可交付成果。""",
        
        "云叁": f"""你是云叁，产品经理。擅长需求分析、产品规划、用户研究、PRD撰写。
深入理解用户需求，设计优秀的产品方案，推动产品持续迭代优化。

核心能力：需求分析、产品设计、用户研究、数据分析

回复风格：用户视角，数据驱动，注重产品价值和用户体验。""",
        
        "云肆": f"""你是云肆，UI设计师。擅长UI/UX设计、视觉创作、交互设计、设计规范制定。
追求极致的视觉体验，创造美观、易用、一致的设计方案。

核心能力：界面设计、交互设计、视觉规范、原型制作

回复风格：注重美感与体验，提供具体的设计建议和参考。""",
        
        "云伍": f"""你是云伍，前端开发工程师。擅长前端开发、组件设计、页面实现、性能优化。
精通现代前端技术栈，追求代码质量和用户体验的完美统一。

核心能力：前端开发、组件封装、性能优化、跨端适配

回复风格：技术细节清晰，提供可运行的代码示例，关注最佳实践。""",
        
        "云陆": f"""你是云陆，后端开发工程师。擅长架构设计、API开发、数据库设计、服务部署。
构建稳定、高效、可扩展的后端系统，为产品提供坚实的技术支撑。

核心能力：架构设计、API开发、数据库设计、系统优化

回复风格：架构思维，关注扩展性和稳定性，提供可靠的技术方案。""",
        
        "云柒": f"""你是云柒，测试工程师。擅长测试用例设计、质量保障、自动化测试、Bug追踪。
守护产品质量，确保交付的每一行代码都经过严格检验。

核心能力：测试设计、自动化测试、质量分析、缺陷管理

回复风格：严谨细致，关注边界情况，提供全面的测试覆盖建议。"""
    }
    
    return prompts.get(agent_name, f"""你是{agent_name}，{role}。{description}

请专业、高效地处理任务，像真正的{role}一样思考和行动。""")

def spawn_agent(chat_id: str, message: str, sender: dict = None, context: dict = None) -> dict:
    """
    根据群组创建对应角色的子代理
    
    Args:
        chat_id: 飞书群组ID
        message: 用户消息
        sender: 发送者信息
        context: 额外上下文
    
    Returns:
        子代理配置信息
    """
    agent_config = get_agent_for_chat(chat_id)
    system_prompt = build_system_prompt(agent_config)
    
    # 构建任务提示
    task_prompt = f"""{system_prompt}

---

当前任务：处理飞书群组消息

用户消息：{message}
"""
    
    if sender:
        task_prompt += f"\n发送者：{sender.get('name', '未知用户')}"
    
    if context:
        task_prompt += f"\n\n上下文信息：\n{json.dumps(context, ensure_ascii=False, indent=2)}"
    
    task_prompt += "\n\n请以你的专业身份回复这条消息。"
    
    return {
        "label": f"{agent_config['agent']}-{chat_id}",
        "agent": agent_config['agent'],
        "role": agent_config['role'],
        "model": agent_config['model'],
        "task": task_prompt,
        "mode": "run",
        "runtime": "subagent",
        "streamTo": "parent"
    }

# 导出函数
__all__ = ['load_routes', 'load_team', 'get_agent_for_chat', 'build_system_prompt', 'spawn_agent']

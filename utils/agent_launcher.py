# 独立智能体启动器
# 每个角色作为独立的 OpenClaw 实例运行

import os
import yaml
import subprocess
from pathlib import Path

# 加载环境变量
def load_env_file(env_path: Path):
    """手动加载 .env 文件"""
    if not env_path.exists():
        return
    with open(env_path, 'r', encoding='utf-8') as f:
        for line in f:
            line = line.strip()
            if not line or line.startswith('#'):
                continue
            if '=' in line:
                key, value = line.split('=', 1)
                os.environ[key] = value

# 加载环境变量
load_env_file(Path(__file__).parent.parent / "config" / ".env")

CONFIG_PATH = Path("/root/.openclaw/workspace/config/agents-feishu.yaml")

def load_agent_config():
    """加载智能体配置"""
    with open(CONFIG_PATH, 'r', encoding='utf-8') as f:
        return yaml.safe_load(f)

def get_agent_env(agent_name: str) -> dict:
    """
    获取指定智能体的环境变量
    
    Args:
        agent_name: 智能体名称 (云壹, 云贰, ...)
    
    Returns:
        环境变量字典
    """
    config = load_agent_config()
    agent_config = config['agents'].get(agent_name)
    
    if not agent_config:
        raise ValueError(f"未找到智能体配置: {agent_name}")
    
    feishu = agent_config['feishu']
    
    # 获取环境变量（优先从 os.environ，其次从配置）
    env_var_name = agent_name.upper()
    app_id = os.getenv(f'FEISHU_{env_var_name}_APP_ID', '')
    app_secret = os.getenv(f'FEISHU_{env_var_name}_APP_SECRET', '')
    
    # 如果环境变量为空，尝试从配置读取
    if not app_id:
        app_id = feishu.get('app_id', '')
    if not app_secret:
        app_secret = feishu.get('app_secret', '')
    
    # 构建环境变量
    env = {
        # 飞书配置
        'FEISHU_APP_ID': app_id,
        'FEISHU_APP_SECRET': app_secret,
        'FEISHU_DOMAIN': feishu.get('domain', 'feishu'),
        'FEISHU_CONNECTION_MODE': feishu.get('connection_mode', 'websocket'),
        
        # 智能体配置
        'AGENT_NAME': agent_name,
        'AGENT_ROLE': agent_config['role'],
        'AGENT_DESCRIPTION': agent_config['description'],
        'AGENT_MODEL': agent_config['model'],
        
        # OpenClaw 配置
        'OPENCLAW_WORKSPACE': f"/root/.openclaw/workspace/agents/{agent_name}",
        'OPENCLAW_CONFIG_DIR': f"/root/.openclaw/agents/{agent_name}",
    }
    
    return env

def generate_openclaw_config(agent_name: str) -> dict:
    """
    生成指定智能体的 OpenClaw 配置
    
    Args:
        agent_name: 智能体名称
    
    Returns:
        OpenClaw 配置字典
    """
    env = get_agent_env(agent_name)
    
    config = {
        "meta": {
            "lastTouchedVersion": "2026.3.7",
            "lastTouchedAt": "2026-03-11T00:00:00.000Z"
        },
        "auth": {
            "profiles": {
                "moonshot:default": {
                    "provider": "moonshot",
                    "mode": "api_key"
                }
            }
        },
        "models": {
            "mode": "merge",
            "providers": {
                "moonshot": {
                    "baseUrl": "https://api.moonshot.cn/v1",
                    "api": "openai-completions",
                    "models": [
                        {
                            "id": "kimi-k2.5",
                            "name": "Kimi K2.5",
                            "reasoning": False,
                            "input": ["text", "image"],
                            "cost": {"input": 0, "output": 0, "cacheRead": 0, "cacheWrite": 0},
                            "contextWindow": 256000,
                            "maxTokens": 8192
                        }
                    ]
                }
            }
        },
        "agents": {
            "defaults": {
                "model": {"primary": env['AGENT_MODEL']},
                "models": {env['AGENT_MODEL']: {"alias": agent_name}},
                "workspace": env['OPENCLAW_WORKSPACE']
            }
        },
        "tools": {"profile": "full"},
        "commands": {
            "native": "auto",
            "nativeSkills": "auto",
            "restart": True,
            "ownerDisplay": "raw"
        },
        "session": {"dmScope": "per-channel-peer"},
        "hooks": {
            "internal": {
                "enabled": True,
                "entries": {"session-memory": {"enabled": True}}
            }
        },
        "channels": {
            "feishu": {
                "enabled": True,
                "appId": env['FEISHU_APP_ID'],
                "appSecret": env['FEISHU_APP_SECRET'],
                "domain": env['FEISHU_DOMAIN'],
                "connectionMode": env['FEISHU_CONNECTION_MODE'],
                "dmPolicy": "pairing",
                "groupPolicy": "allowlist",
                "allowFrom": []
            }
        },
        "gateway": {
            "port": 18789 + list(load_agent_config()['agents'].keys()).index(agent_name),  # 每个智能体不同端口
            "mode": "local",
            "bind": "loopback",
            "auth": {"mode": "token", "token": "auto-generated"},
            "tailscale": {"mode": "off", "resetOnExit": False},
            "nodes": {
                "denyCommands": [
                    "camera.snap", "camera.clip", "screen.record",
                    "contacts.add", "calendar.add", "reminders.add", "sms.send"
                ]
            }
        },
        "plugins": {
            "allow": ["feishu"],
            "entries": {"feishu": {"enabled": True}}
        }
    }
    
    return config

def start_agent(agent_name: str):
    """
    启动指定智能体
    
    Args:
        agent_name: 智能体名称
    """
    import json
    
    # 获取配置
    config = generate_openclaw_config(agent_name)
    env = get_agent_env(agent_name)
    
    # 创建工作目录
    workspace = Path(env['OPENCLAW_WORKSPACE'])
    config_dir = Path(env['OPENCLAW_CONFIG_DIR'])
    workspace.mkdir(parents=True, exist_ok=True)
    config_dir.mkdir(parents=True, exist_ok=True)
    
    # 写入配置文件
    config_file = config_dir / "openclaw.json"
    with open(config_file, 'w', encoding='utf-8') as f:
        json.dump(config, f, indent=2, ensure_ascii=False)
    
    print(f"✅ 智能体 {agent_name} 配置已生成")
    print(f"   配置文件: {config_file}")
    print(f"   工作目录: {workspace}")
    print(f"   网关端口: {config['gateway']['port']}")
    print(f"   飞书应用: {env['FEISHU_APP_ID'][:20]}...")
    
    return config_file, env

def start_all_agents():
    """启动所有智能体"""
    config = load_agent_config()
    
    print("=" * 60)
    print("七人智能体团队 - 独立飞书配置")
    print("=" * 60)
    
    for agent_name in config['agents'].keys():
        print()
        try:
            start_agent(agent_name)
        except Exception as e:
            print(f"❌ 智能体 {agent_name} 配置失败: {e}")
    
    print()
    print("=" * 60)
    print("配置完成！请检查每个智能体的飞书应用凭证是否正确。")
    print("=" * 60)

if __name__ == "__main__":
    import sys
    
    if len(sys.argv) > 1:
        agent_name = sys.argv[1]
        if agent_name == "all":
            start_all_agents()
        else:
            start_agent(agent_name)
    else:
        print("用法: python agent_launcher.py [智能体名称|all]")
        print("示例: python agent_launcher.py 云贰")
        print("      python agent_launcher.py all")

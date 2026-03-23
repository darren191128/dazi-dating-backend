#!/usr/bin/env python3
"""
Skill Vetter - Validate and evaluate OpenClaw skills
"""

import sys
import os
import re
import json
import yaml
from pathlib import Path

def validate_skill(skill_path):
    """Validate skill structure and content"""
    skill_path = Path(skill_path)
    skill_md = skill_path / "SKILL.md"
    
    if not skill_md.exists():
        print(f"❌ Error: SKILL.md not found in {skill_path}")
        return False
    
    with open(skill_md, 'r') as f:
        content = f.read()
    
    issues = []
    warnings = []
    
    # Check YAML frontmatter
    if content.startswith('---'):
        try:
            yaml_end = content.find('---', 3)
            if yaml_end == -1:
                issues.append("YAML frontmatter not properly closed")
            else:
                yaml_content = content[3:yaml_end].strip()
                metadata = yaml.safe_load(yaml_content)
                
                if 'name' not in metadata:
                    issues.append("Missing 'name' field in YAML frontmatter")
                if 'description' not in metadata:
                    issues.append("Missing 'description' field in YAML frontmatter")
                else:
                    desc = metadata.get('description', '')
                    if len(desc) < 50:
                        warnings.append(f"Description is short ({len(desc)} chars), consider expanding")
                    if len(desc) > 1000:
                        warnings.append(f"Description is very long ({len(desc)} chars), consider condensing")
        except yaml.YAMLError as e:
            issues.append(f"YAML parsing error: {e}")
    else:
        issues.append("Missing YAML frontmatter (should start with '---')")
    
    # Check markdown content
    if '## ' not in content and '# ' not in content:
        warnings.append("No headers found in SKILL.md, consider adding structure")
    
    if '```' not in content:
        warnings.append("No code blocks found, consider adding examples")
    
    # Check for common anti-patterns
    if re.search(r'\b(click here|read more|learn more)\b', content, re.I):
        warnings.append("Avoid generic link text like 'click here'")
    
    # Print results
    print(f"\n📋 Validation Report for: {skill_path.name}")
    print("=" * 60)
    
    if issues:
        print("\n❌ Issues Found:")
        for issue in issues:
            print(f"  - {issue}")
    
    if warnings:
        print("\n⚠️  Warnings:")
        for warning in warnings:
            print(f"  - {warning}")
    
    if not issues and not warnings:
        print("\n✅ All checks passed!")
    
    print(f"\n📊 Score: {max(0, 10 - len(issues) * 2 - len(warnings) * 0.5)}/10")
    
    return len(issues) == 0

def optimize_description(skill_path):
    """Analyze and suggest description improvements"""
    skill_path = Path(skill_path)
    skill_md = skill_path / "SKILL.md"
    
    with open(skill_md, 'r') as f:
        content = f.read()
    
    # Extract YAML frontmatter
    if content.startswith('---'):
        yaml_end = content.find('---', 3)
        if yaml_end != -1:
            yaml_content = content[3:yaml_end].strip()
            metadata = yaml.safe_load(yaml_content)
            desc = metadata.get('description', '')
            
            print(f"\n✨ Description Analysis for: {skill_path.name}")
            print("=" * 60)
            print(f"\nCurrent description ({len(desc)} chars):")
            print(f'  "{desc[:200]}..."' if len(desc) > 200 else f'  "{desc}"')
            
            suggestions = []
            
            # Check for trigger phrases
            if 'when' not in desc.lower():
                suggestions.append("Add 'when to use' context (e.g., 'Use when...')")
            
            # Check for specificity
            if len(desc.split()) < 20:
                suggestions.append("Consider adding more specific trigger phrases")
            
            # Check for "pushiness"
            if 'make sure' not in desc.lower() and 'always' not in desc.lower():
                suggestions.append("Consider making description more 'pushy' to ensure triggering")
            
            if suggestions:
                print("\n💡 Suggestions:")
                for s in suggestions:
                    print(f"  - {s}")
            else:
                print("\n✅ Description looks good!")

def main():
    if len(sys.argv) < 3:
        print("Usage: skill-vetter <command> <skill-path> [options]")
        print("\nCommands:")
        print("  validate <path>     - Validate skill structure")
        print("  optimize-desc <path> - Analyze description")
        print("\nExamples:")
        print("  skill-vetter validate ~/.openclaw/workspace/skills/my-skill")
        print("  skill-vetter optimize-desc ~/.openclaw/workspace/skills/my-skill")
        sys.exit(1)
    
    command = sys.argv[1]
    skill_path = sys.argv[2]
    
    if command == 'validate':
        validate_skill(skill_path)
    elif command == 'optimize-desc':
        optimize_description(skill_path)
    else:
        print(f"Unknown command: {command}")
        sys.exit(1)

if __name__ == '__main__':
    main()

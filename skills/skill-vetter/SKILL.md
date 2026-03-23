---
name: skill-vetter
description: Evaluate, validate, and improve OpenClaw skills. Use when you need to check skill quality, validate SKILL.md syntax, test skill functionality, benchmark performance, or optimize skill descriptions for better triggering. This skill helps ensure skills are well-written, properly structured, and effective.
---

# Skill Vetter

A comprehensive tool for evaluating and validating OpenClaw skills.

## Overview

Skill Vetter helps you:
- ✅ Validate SKILL.md syntax and structure
- 🧪 Test skill functionality
- 📊 Benchmark performance
- 🔍 Review code quality
- ✨ Optimize descriptions for better triggering
- 📝 Generate improvement recommendations

## When to Use

Use Skill Vetter when:
- Creating a new skill and want to validate it
- Reviewing an existing skill for quality
- Before publishing a skill to ClawHub
- Skills are not triggering correctly
- Want to improve skill performance

## Commands

### Validate Skill Structure

```bash
# Check SKILL.md syntax and required fields
skill-vetter validate <skill-path>

# Example:
skill-vetter validate ~/.openclaw/workspace/skills/my-skill
```

**Checks:**
- YAML frontmatter (name, description)
- Required fields present
- Markdown structure
- File organization

### Test Skill Functionality

```bash
# Run test prompts against the skill
skill-vetter test <skill-path> --prompts <test-file>

# Example:
skill-vetter test ~/.openclaw/workspace/skills/my-skill --prompts tests.json
```

### Benchmark Performance

```bash
# Measure skill execution time and accuracy
skill-vetter benchmark <skill-path> --iterations 10

# Example:
skill-vetter benchmark ~/.openclaw/workspace/skills/my-skill --iterations 10
```

### Review Code Quality

```bash
# Analyze scripts and code in the skill
skill-vetter review-code <skill-path>
```

### Optimize Description

```bash
# Analyze and suggest improvements for skill description
skill-vetter optimize-desc <skill-path>
```

## Validation Checklist

### Structure Validation

- [ ] **YAML Frontmatter**
  - [ ] `name` field present and valid
  - [ ] `description` field present (100-500 words ideal)
  - [ ] No syntax errors in YAML

- [ ] **SKILL.md Content**
  - [ ] Clear purpose statement
  - [ ] Usage instructions
  - [ ] Examples provided
  - [ ] Prerequisites documented

- [ ] **File Organization**
  - [ ] SKILL.md in root
  - [ ] Scripts in `scripts/` (if any)
  - [ ] Assets in `assets/` (if any)

### Content Quality

- [ ] **Description Quality**
  - [ ] Specific trigger phrases included
  - [ ] "Pushy" enough to ensure triggering
  - [ ] Clear about what the skill does
  - [ ] Mentions when to use it

- [ ] **Instructions Clear**
  - [ ] Step-by-step where needed
  - [ ] Code examples work
  - [ ] Edge cases covered

- [ ] **No Anti-patterns**
  - [ ] No overly broad triggers
  - [ ] No conflicting with other skills
  - [ ] No security vulnerabilities

## Quick Start

```bash
# 1. Validate a skill
skill-vetter validate ~/.openclaw/workspace/skills/my-skill

# 2. Run tests
skill-vetter test ~/.openclaw/workspace/skills/my-skill

# 3. Get improvement suggestions
skill-vetter optimize-desc ~/.openclaw/workspace/skills/my-skill
```

## Best Practices

### Writing Good Descriptions

**Before:**
```yaml
description: How to build a dashboard.
```

**After:**
```yaml
description: How to build a simple fast dashboard to display internal data. Make sure to use this skill whenever the user mentions dashboards, data visualization, internal metrics, or wants to display any kind of company data, even if they don't explicitly ask for a 'dashboard.'
```

### Testing Skills

Create a `tests.json` file:
```json
{
  "tests": [
    {
      "prompt": "Create a React component",
      "expected_contains": ["import", "export", "function"]
    },
    {
      "prompt": "How do I fetch data?",
      "expected_contains": ["fetch", "useEffect"]
    }
  ]
}
```

## Output Format

Skill Vetter generates a report:

```
📋 Skill Vetter Report
======================

Skill: my-skill
Path: ~/.openclaw/workspace/skills/my-skill

✅ Structure Validation: PASSED
✅ Description Quality: PASSED  
⚠️  Test Coverage: WARNING (3/5 tests passed)
❌ Code Quality: FAILED (2 issues found)

📊 Performance Benchmark
------------------------
Average execution time: 1.2s
Success rate: 80% (8/10)

📝 Recommendations
------------------
1. Add more test cases for edge cases
2. Fix error handling in scripts/deploy.sh
3. Optimize description to include "React" keyword

Overall Score: 7.5/10
```

## Installation

```bash
# Clone the skill
git clone https://github.com/your-org/skill-vetter ~/.openclaw/workspace/skills/skill-vetter

# Or use npx skills
npx skills add https://github.com/your-org/skill-vetter
```

## Related Skills

- **skill-creator** - Create new skills from scratch
- **agentic-eval** - Evaluate and improve AI agent outputs
- **find-skill-dual** - Search for existing skills

## License

MIT

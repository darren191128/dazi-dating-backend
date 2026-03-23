---
name: proactive-agent
description: Proactive AI agent that anticipates needs, monitors systems, and takes initiative. Use when you want the agent to be proactive - checking status, monitoring changes, suggesting actions, automating routine tasks, or working autonomously without constant prompting.
---

# Proactive Agent

A skill for building proactive, autonomous AI agents that anticipate needs and take initiative.

## Philosophy

Traditional reactive agents wait for commands. Proactive agents:
- 🔍 **Monitor** - Watch systems, data, and events
- 🧠 **Anticipate** - Predict needs and issues
- ⚡ **Act** - Take initiative without prompting
- 📢 **Communicate** - Report findings and suggest actions

## Core Capabilities

### 1. System Monitoring
- File system changes
- Process status
- Network connectivity
- Resource usage (CPU, memory, disk)
- Log file analysis

### 2. Data Monitoring
- Database changes
- API status checks
- Data quality validation
- Trend detection
- Anomaly detection

### 3. Task Automation
- Scheduled tasks (cron-like)
- Event-driven actions
- Conditional workflows
- Self-healing systems

### 4. Intelligent Notifications
- Context-aware alerts
- Escalation patterns
- Digest summaries
- Smart filtering

## Usage Patterns

### Pattern 1: Health Monitor
```python
# Continuously monitor system health
while True:
    health = check_system_health()
    if health.status != 'healthy':
        notify(f"System issue detected: {health.issue}")
        if health.severity == 'critical':
            auto_remediation(health.issue)
    sleep(60)
```

### Pattern 2: Data Quality Guardian
```python
# Monitor data quality
def check_data_quality():
    issues = []
    
    # Check for anomalies
    if detect_anomaly(data):
        issues.append("Anomaly detected in metrics")
    
    # Check for missing data
    if has_missing_data(data):
        issues.append("Missing data detected")
    
    # Check for schema drift
    if schema_changed(data):
        issues.append("Schema drift detected")
    
    if issues:
        alert_team(issues)
        suggest_fixes(issues)
```

### Pattern 3: Smart Assistant
```python
# Anticipate user needs
class SmartAssistant:
    def monitor_context(self):
        # Track what user is working on
        # Predict what they might need next
        # Offer suggestions proactively
        
    def suggest_actions(self):
        # Based on context, suggest:
        # - Related files to open
        # - Commands to run
        # - Information to look up
        # - Tasks to complete
```

## Proactive Behaviors

### When to Be Proactive

✅ **DO be proactive when:**
- System errors or failures occur
- Security issues are detected
- Performance degrades
- Deadlines are approaching
- Dependencies are missing
- Best practices are violated
- Opportunities for improvement exist

❌ **DON'T be proactive when:**
- User is in focused work mode
- Action would be destructive
- Uncertainty is high
- User preferences are unknown
- It would be annoying

### Proactive Communication

**Good proactive message:**
```
🔍 I noticed the test suite is failing on the main branch.
   Failed tests: 3 in auth module
   Likely cause: Recent commit abc123 changed auth logic
   Suggested action: Run tests locally or check the commit
   [View Logs] [Run Tests] [Ignore]
```

**Bad proactive message:**
```
Hey, something might be wrong maybe? 🤷
```

## Implementation Examples

### Example 1: File Watcher
```python
from watchdog.observers import Observer
from watchdog.events import FileSystemEventHandler

class ProactiveFileWatcher(FileSystemEventHandler):
    def on_modified(self, event):
        if event.src_path.endswith('.py'):
            # Proactively run linter
            run_linter(event.src_path)
            
            # Proactively check for tests
            test_file = find_test_file(event.src_path)
            if test_file:
                suggest(f"Run tests for {event.src_path}?")
```

### Example 2: Deadline Monitor
```python
from datetime import datetime, timedelta

class DeadlineMonitor:
    def check_upcoming_deadlines(self):
        deadlines = get_all_deadlines()
        for deadline in deadlines:
            days_until = (deadline.date - datetime.now()).days
            
            if days_until <= 1:
                alert(f"🚨 URGENT: {deadline.name} due tomorrow!")
            elif days_until <= 3:
                notify(f"⚠️ {deadline.name} due in {days_until} days")
            elif days_until <= 7:
                remind(f"📅 {deadline.name} coming up in {days_until} days")
```

### Example 3: Dependency Monitor
```python
class DependencyMonitor:
    def check_for_updates(self):
        outdated = check_outdated_packages()
        if outdated:
            summary = format_update_summary(outdated)
            suggest(f"📦 Package updates available:\n{summary}\n\nRun update?")
    
    def check_for_vulnerabilities(self):
        vulns = scan_for_vulnerabilities()
        if vulns:
            alert(f"🛡️ Security vulnerabilities found:\n{vulns}")
            suggest_fixes(vulns)
```

## Configuration

### Proactive Settings
```yaml
proactive:
  enabled: true
  
  monitoring:
    system_health: true
    file_changes: true
    data_quality: true
    
  notifications:
    critical: immediate
    warning: digest_every_30min
    info: digest_daily
    
  automation:
    auto_fix_minor_issues: true
    suggest_fixes: true
    ask_before_destructive: true
```

### User Preferences
```yaml
user_preferences:
  work_hours: 9-18
  focus_mode: do_not_disturb
  notification_channels: [email, slack]
  proactive_level: high  # low, medium, high
```

## Best Practices

### 1. Context Awareness
- Know what user is working on
- Understand current priorities
- Respect focus time
- Learn from past interactions

### 2. Relevance Filtering
- Only notify about relevant issues
- Batch non-urgent notifications
- Provide clear action items
- Include context in messages

### 3. Gradual Escalation
```
Level 1: Log only
Level 2: Show in UI
Level 3: Send notification
Level 4: Alert immediately
Level 5: Auto-remediate + notify
```

### 4. Feedback Loop
- Track which proactive actions were helpful
- Learn user preferences over time
- Adjust sensitivity based on feedback
- Allow easy configuration

## Integration with Other Skills

- **HEARTBEAT.md** - Periodic checks and notifications
- **cron** - Scheduled proactive tasks
- **memory_search** - Learn from past interactions
- **sessions_spawn** - Run proactive tasks in background

## Example Workflows

### Workflow 1: Development Guardian
1. Monitor code changes
2. Run tests proactively
3. Check for linting issues
4. Verify dependencies
5. Suggest improvements

### Workflow 2: System Health Monitor
1. Check system resources
2. Monitor log files
3. Detect anomalies
4. Alert on issues
5. Auto-remediate when safe

### Workflow 3: Project Assistant
1. Track project deadlines
2. Monitor task assignments
3. Check for blockers
4. Suggest next actions
5. Prepare meeting agendas

## Anti-Patterns

❌ **Avoid:**
- Spamming with low-value notifications
- Taking actions without confirmation when risky
- Being proactive about everything (noise)
- Ignoring user feedback
- Acting during focus time

✅ **Instead:**
- Prioritize and filter notifications
- Ask before destructive actions
- Focus on high-value proactive behaviors
- Continuously learn and adapt
- Respect user's context and time

## Getting Started

To enable proactive behavior:

1. Configure monitoring settings
2. Set user preferences
3. Start with high-value, low-noise behaviors
4. Gather feedback and iterate
5. Gradually increase proactivity

## Related Skills

- **HEARTBEAT.md** - Periodic task scheduling
- **cron** - Job scheduling
- **agentic-eval** - Self-improvement
- **AutoResearchClaw** - Autonomous research
- **ARIS** - Self-improving research agent

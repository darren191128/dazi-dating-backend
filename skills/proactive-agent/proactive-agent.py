#!/usr/bin/env python3
"""
Proactive Agent - Monitor and take initiative
"""

import sys
import os
import time
from datetime import datetime

def monitor_system():
    """Basic system monitoring"""
    print("🔍 System Monitor")
    print("=" * 60)
    
    # Check disk usage
    try:
        import shutil
        total, used, free = shutil.disk_usage("/")
        print(f"\n💾 Disk Usage:")
        print(f"   Total: {total // (2**30)} GB")
        print(f"   Used: {used // (2**30)} GB ({used/total*100:.1f}%)")
        print(f"   Free: {free // (2**30)} GB")
        
        if used/total > 0.9:
            print("   ⚠️  WARNING: Disk usage above 90%!")
    except Exception as e:
        print(f"   Error checking disk: {e}")
    
    # Check memory
    try:
        with open('/proc/meminfo', 'r') as f:
            meminfo = f.read()
        
        mem_total = int([line for line in meminfo.split('\n') if 'MemTotal' in line][0].split()[1])
        mem_available = int([line for line in meminfo.split('\n') if 'MemAvailable' in line][0].split()[1])
        
        print(f"\n🧠 Memory:")
        print(f"   Total: {mem_total // 1024} MB")
        print(f"   Available: {mem_available // 1024} MB")
        print(f"   Usage: {(mem_total - mem_available)/mem_total*100:.1f}%")
    except Exception as e:
        print(f"   Error checking memory: {e}")
    
    # Check load average
    try:
        with open('/proc/loadavg', 'r') as f:
            loadavg = f.read().split()
        print(f"\n⚡ Load Average: {loadavg[0]} {loadavg[1]} {loadavg[2]}")
    except Exception as e:
        print(f"   Error checking load: {e}")

def check_git_status():
    """Check git repository status"""
    import subprocess
    
    print("\n📁 Git Status")
    print("=" * 60)
    
    try:
        # Check if in git repo
        result = subprocess.run(['git', 'rev-parse', '--git-dir'], 
                              capture_output=True, text=True)
        if result.returncode != 0:
            print("   Not a git repository")
            return
        
        # Check for uncommitted changes
        result = subprocess.run(['git', 'status', '--short'], 
                              capture_output=True, text=True)
        if result.stdout.strip():
            print(f"   ⚠️  Uncommitted changes:\n{result.stdout}")
        else:
            print("   ✅ Working tree clean")
        
        # Check for unpushed commits
        result = subprocess.run(['git', 'log', '@{u}..', '--oneline'], 
                              capture_output=True, text=True)
        if result.stdout.strip():
            print(f"   📤 Unpushed commits:\n{result.stdout}")
        
    except Exception as e:
        print(f"   Error: {e}")

def check_recent_files():
    """Check recently modified files"""
    print("\n📄 Recent Files (last 24h)")
    print("=" * 60)
    
    try:
        import subprocess
        result = subprocess.run(
            ['find', '.', '-type', 'f', '-mtime', '-1', '-not', '-path', '*/\.*'],
            capture_output=True, text=True
        )
        
        files = result.stdout.strip().split('\n')[:10]
        if files and files[0]:
            for f in files:
                print(f"   {f}")
        else:
            print("   No recently modified files")
    except Exception as e:
        print(f"   Error: {e}")

def proactive_check():
    """Run all proactive checks"""
    print(f"\n🤖 Proactive Agent Check - {datetime.now().strftime('%Y-%m-%d %H:%M:%S')}")
    print("=" * 80)
    
    monitor_system()
    check_git_status()
    check_recent_files()
    
    print("\n" + "=" * 80)
    print("✅ Proactive check complete")
    print("\n💡 Suggestions:")
    print("   - Review uncommitted changes before they pile up")
    print("   - Push completed work to remote")
    print("   - Clean up old files if disk space is low")

def show_help():
    print("""
🤖 Proactive Agent CLI
=======================

Usage: proactive-agent <command>

Commands:
  check       - Run all proactive checks
  monitor     - Monitor system resources
  git         - Check git status
  files       - Check recent files
  help        - Show this help

Examples:
  proactive-agent check
  proactive-agent monitor
  proactive-agent git

This tool helps you stay on top of:
- System health (disk, memory, load)
- Git repository status
- Recent file changes
- Potential issues before they become problems
""")

def main():
    if len(sys.argv) < 2:
        show_help()
        sys.exit(1)
    
    command = sys.argv[1]
    
    if command == 'check':
        proactive_check()
    elif command == 'monitor':
        monitor_system()
    elif command == 'git':
        check_git_status()
    elif command == 'files':
        check_recent_files()
    elif command == 'help':
        show_help()
    else:
        print(f"Unknown command: {command}")
        show_help()
        sys.exit(1)

if __name__ == '__main__':
    main()

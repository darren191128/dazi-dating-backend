#!/usr/bin/env python3
"""
Email Management CLI - Simple interface for common email tasks
"""

import sys
import os
import subprocess

def run_gog_command(args):
    """Run gog command if available"""
    try:
        result = subprocess.run(['gog'] + args, capture_output=True, text=True)
        if result.returncode == 0:
            print(result.stdout)
        else:
            print(f"Error: {result.stderr}")
        return result.returncode == 0
    except FileNotFoundError:
        print("❌ gog CLI not found. Install with: brew install steipete/tap/gogcli")
        return False

def check_inbox():
    """Check recent emails"""
    print("📧 Checking inbox (last 24 hours)...")
    return run_gog_command(['gmail', 'search', 'newer_than:1d', '--max', '10'])

def check_unread():
    """Check unread emails"""
    print("📧 Checking unread emails...")
    return run_gog_command(['gmail', 'search', 'is:unread', '--max', '20'])

def send_email(to, subject, body):
    """Send an email"""
    print(f"📧 Sending email to {to}...")
    return run_gog_command(['gmail', 'send', '--to', to, '--subject', subject, '--body', body])

def search_emails(query):
    """Search emails"""
    print(f"🔍 Searching for: {query}")
    return run_gog_command(['gmail', 'search', query, '--max', '20'])

def show_help():
    """Show help information"""
    print("""
📧 Email Management CLI
=======================

Usage: email-management <command> [options]

Commands:
  inbox              Check recent emails (last 24h)
  unread             Check unread emails
  search <query>     Search emails with query
  send <to> <subject> <body>  Send email
  help               Show this help

Examples:
  email-management inbox
  email-management unread
  email-management search "from:boss@company.com newer_than:7d"
  email-management send "recipient@example.com" "Hello" "Message body"

Note: Requires gog CLI to be installed and authenticated.
      Run: brew install steipete/tap/gogcli
""")

def main():
    if len(sys.argv) < 2:
        show_help()
        sys.exit(1)
    
    command = sys.argv[1]
    
    if command == 'inbox':
        check_inbox()
    elif command == 'unread':
        check_unread()
    elif command == 'search':
        if len(sys.argv) < 3:
            print("Usage: email-management search <query>")
            sys.exit(1)
        search_emails(sys.argv[2])
    elif command == 'send':
        if len(sys.argv) < 5:
            print("Usage: email-management send <to> <subject> <body>")
            sys.exit(1)
        send_email(sys.argv[2], sys.argv[3], sys.argv[4])
    elif command == 'help':
        show_help()
    else:
        print(f"Unknown command: {command}")
        show_help()
        sys.exit(1)

if __name__ == '__main__':
    main()

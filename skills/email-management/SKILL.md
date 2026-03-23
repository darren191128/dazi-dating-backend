---
name: email-management
description: Comprehensive email management for Gmail, Outlook, and other providers. Use when the user wants to check emails, send emails, search emails, manage inbox, organize emails with labels/folders, set up email filters, or handle email automation. Works with gog CLI for Gmail and supports multiple email providers.
---

# Email Management

A comprehensive skill for managing emails across multiple providers.

## Supported Providers

- **Gmail** (via gog CLI)
- **Outlook/Office 365** (via Microsoft Graph API)
- **IMAP/SMTP** (generic providers)

## Prerequisites

### Gmail Setup
```bash
# Install gog
brew install steipete/tap/gogcli

# Authenticate
gog auth credentials /path/to/client_secret.json
gog auth add you@gmail.com --services gmail,calendar,drive
gog auth list
```

### Outlook Setup
```bash
# Set environment variables
export OUTLOOK_CLIENT_ID="your-client-id"
export OUTLOOK_CLIENT_SECRET="your-client-secret"
export OUTLOOK_TENANT_ID="your-tenant-id"
```

## Common Tasks

### 1. Check Inbox

```bash
# Gmail - search recent emails
gog gmail search 'newer_than:1d' --max 10

# Search unread emails
gog gmail search 'is:unread newer_than:7d' --max 20

# Search from specific sender
gog gmail search 'from:boss@company.com newer_than:7d'
```

### 2. Send Emails

```bash
# Send simple email
gog gmail send --to recipient@example.com --subject "Hello" --body "Message body"

# Send with HTML
gog gmail send --to recipient@example.com --subject "Hello" --body "<h1>Hello</h1>" --html
```

### 3. Search Emails

```bash
# Search by keyword
gog gmail search 'subject:meeting newer_than:30d'

# Search with multiple criteria
gog gmail search 'from:client@company.com subject:project has:attachment'

# Search in specific label
gog gmail search 'in:important newer_than:7d'
```

### 4. Manage Labels

```bash
# List labels
gog gmail labels list

# Apply label to emails matching search
gog gmail labels add "Project-A" --search "from:client@company.com"

# Remove label
gog gmail labels remove "Project-A" --search "older_than:30d"
```

### 5. Archive & Delete

```bash
# Archive emails (remove from inbox)
gog gmail archive --search "older_than:30d"

# Delete emails
gog gmail delete --search "in:spam older_than:30d"

# Empty trash
gog gmail empty-trash
```

## Email Automation Workflows

### Daily Inbox Cleanup
```bash
# Archive emails older than 7 days
gog gmail archive --search "older_than:7d -in:important"

# Mark as read: newsletters
gog gmail mark-read --search "from:newsletter@company.com older_than:3d"
```

### Weekly Summary
```bash
# Count unread emails
gog gmail search 'is:unread' --count

# Find emails needing reply
gog gmail search 'is:unread from:boss@company.com OR from:client@company.com'
```

### Auto-Labeling
```bash
# Label client emails
gog gmail labels add "Clients" --search "from:*@client-domain.com"

# Label internal emails
gog gmail labels add "Internal" --search "from:*@mycompany.com"
```

## Advanced Search Operators

| Operator | Description | Example |
|----------|-------------|---------|
| `from:` | Sender | `from:boss@company.com` |
| `to:` | Recipient | `to:team@company.com` |
| `subject:` | Subject line | `subject:meeting` |
| `has:attachment` | Has attachments | `has:attachment` |
| `is:unread` | Unread emails | `is:unread` |
| `is:important` | Important emails | `is:important` |
| `newer_than:` | Time range | `newer_than:7d` |
| `older_than:` | Time range | `older_than:30d` |
| `in:` | In label/folder | `in:spam` |
| `filename:` | Attachment name | `filename:pdf` |

## Email Templates

### Meeting Request
```
Subject: Meeting Request - [Topic]

Hi [Name],

I'd like to schedule a meeting to discuss [topic].

Proposed times:
- [Option 1]
- [Option 2]
- [Option 3]

Please let me know what works best for you.

Best regards,
[Your name]
```

### Follow-up
```
Subject: Follow-up: [Original Subject]

Hi [Name],

I wanted to follow up on my previous email about [topic].

[Key points or questions]

Looking forward to hearing from you.

Best regards,
[Your name]
```

## Best Practices

### Inbox Zero
1. Process emails in batches (2-3 times/day)
2. Delete/archive immediately
3. Reply to quick emails (< 2 min) immediately
4. Delegate or schedule longer responses
5. Use labels/folders for organization

### Email Security
- Never send passwords or sensitive data via email
- Verify sender addresses
- Be cautious with attachments
- Use BCC for mass emails

### Productivity Tips
- Turn off email notifications
- Use keyboard shortcuts
- Create filters for automatic organization
- Schedule email time blocks
- Unsubscribe from unwanted newsletters

## Troubleshooting

### Authentication Issues
```bash
# Re-authenticate
gog auth remove you@gmail.com
gog auth add you@gmail.com --services gmail
```

### Rate Limits
- Gmail: 250 emails/day (send), unlimited read
- Outlook: 10,000 emails/day
- Use delays between bulk operations

### Common Errors
- `401 Unauthorized`: Re-authenticate
- `429 Too Many Requests`: Wait and retry
- `404 Not Found`: Check email ID

## Related Skills

- **email-sequence** - Create email drip campaigns
- **gog** - Google Workspace CLI (Gmail, Calendar, Drive)
- **calendar-management** - Schedule meetings from emails

## Resources

- [Gmail Search Operators](https://support.google.com/mail/answer/7190)
- [gog CLI Documentation](https://gogcli.sh)
- [Microsoft Graph API](https://docs.microsoft.com/en-us/graph/)

---
name: realtime-collaboration
description: Real-time collaborative editing and team coordination tools. Use when setting up live collaborative workspaces, real-time document editing, team whiteboards, live coding sessions, or synchronous team collaboration environments.
---

# Realtime Collaboration

Real-time collaborative editing and team coordination.

## Overview

Enable teams to work together synchronously:
- 📝 **Live Document Editing** - Real-time collaborative documents
- 🎨 **Collaborative Whiteboards** - Shared visual workspaces
- 💻 **Live Coding** - Pair programming and code reviews
- 📹 **Video Conferencing** - Integrated video/audio calls
- 💬 **Team Chat** - Real-time messaging and threads

## Supported Tools

### Live Document Editing

#### Notion (Real-time)
```bash
# Already configured via gog skill
# Supports real-time collaborative editing
# Team workspaces and permissions
```

#### Feishu Docs (Real-time)
```bash
# Already configured via feishu-doc skill
# Real-time collaborative editing
# Comments and @mentions
```

#### HackMD / Etherpad
```bash
# Self-hosted real-time markdown collaboration
# Instant synchronization
# Version history
```

### Collaborative Whiteboards

#### Excalidraw
```bash
# Open-source virtual whiteboard
# Real-time collaboration
# Hand-drawn style diagrams
# Export to PNG/SVG
```

#### tldraw
```bash
# Modern collaborative whiteboard
# Real-time multiplayer
# Shape libraries
# Export options
```

### Live Coding

#### VS Code Live Share
```bash
# Real-time collaborative coding
# Shared terminals
# Shared servers
# Audio calls
```

#### CodeSandbox
```bash
# Cloud-based collaborative IDE
# Real-time editing
# Live preview
# Team workspaces
```

#### GitHub Codespaces
```bash
# Cloud development environments
# Real-time collaboration
# Pre-configured workspaces
```

## Implementation Patterns

### Pattern 1: Live Document Session
```
1. Create shared document (Notion/Feishu)
2. Invite team members
3. Real-time editing with cursors
4. Comments and suggestions
5. Version history
```

### Pattern 2: Collaborative Design Session
```
1. Open whiteboard (Excalidraw/tldraw)
2. Share link with team
3. Draw and annotate together
4. Export final design
5. Create implementation tasks
```

### Pattern 3: Pair Programming
```
1. Start Live Share session
2. Share link with pair
3. Code together in real-time
4. Use shared terminal
5. Commit changes
```

### Pattern 4: Sprint Planning
```
1. Open collaborative board
2. Create sticky notes for tasks
3. Team votes and prioritizes
4. Assign owners
5. Export to project management tool
```

## Setup Guides

### Setup Excalidraw (Self-hosted)
```bash
# Using Docker
docker run -d --name excalidraw -p 3000:3000 excalidraw/excalidraw

# Access at http://localhost:3000
# Share room link for collaboration
```

### Setup tldraw (Self-hosted)
```bash
# Clone repository
git clone https://github.com/tldraw/tldraw.git
cd tldraw

# Install dependencies
npm install

# Start development server
npm run dev

# Access at http://localhost:3000
```

### Setup HackMD
```bash
# Using Docker Compose
curl -o docker-compose.yml https://raw.githubusercontent.com/hackmdio/codimd/main/docker-compose.yml
docker-compose up -d

# Access at http://localhost:3000
```

## Collaboration Workflows

### Design Sprint
```
Day 1: Understand
- Collaborative whiteboard for research
- Real-time note taking
- Share insights

Day 2: Sketch
- Individual sketching
- Real-time critique
- Dot voting

Day 3: Decide
- Collaborative decision making
- Storyboard creation
- Task assignment

Day 4: Prototype
- Live coding/writing
- Real-time feedback
- Iteration

Day 5: Test
- Collaborative observation
- Real-time synthesis
- Next steps
```

### Code Review Session
```
1. Open code in Live Share
2. Walk through changes together
3. Real-time comments
4. Make edits collaboratively
5. Approve and merge
```

### Content Creation
```
1. Create shared document
2. Outline together
3. Write sections simultaneously
4. Real-time editing and feedback
5. Final review and publish
```

## Best Practices

### For Facilitators
- Set clear agenda and timebox
- Assign roles (scribe, timekeeper, etc.)
- Use breakout rooms for large groups
- Record sessions for async review
- Follow up with action items

### For Participants
- Join with video when possible
- Use reactions instead of interrupting
- Contribute to shared notes
- Respect speaking time
- Stay focused (no multitasking)

### Technical Setup
- Test audio/video before session
- Have backup communication channel
- Prepare templates in advance
- Ensure stable internet connection
- Record important sessions

## Integration with Other Skills

- **brainstorming** - Collaborative ideation
- **writing-plans** - Collaborative planning
- **canvas-design** - Collaborative design
- **cicd-pipeline** - Collaborative deployment
- **proactive-agent** - Session reminders

## Security Considerations

- Use password-protected rooms
- Limit access to invited participants
- Clear sensitive data after sessions
- Use enterprise versions for sensitive work
- Audit access logs

## Resources

- [Excalidraw Documentation](https://docs.excalidraw.com)
- [tldraw Documentation](https://tldraw.dev)
- [VS Code Live Share](https://visualstudio.microsoft.com/services/live-share/)
- [Notion Collaboration Guide](https://www.notion.so/help/collaborate-with-others)

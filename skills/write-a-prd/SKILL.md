---
name: write-a-prd
description: Write a comprehensive Product Requirements Document (PRD) from scratch. Use when you need to create a structured PRD that defines product features, user stories, acceptance criteria, and success metrics. Guides you through problem definition, solution design, and engineering-ready documentation.
---

# Write a PRD

A skill for creating comprehensive Product Requirements Documents.

## When to Use

Use this skill when:
- Starting a new product or feature
- Documenting requirements for engineering handoff
- Aligning stakeholders on scope and goals
- Creating a source of truth for product development
- Moving from discovery to implementation

## PRD Structure

The PRD will include:

1. **Executive Summary** - One-paragraph overview
2. **Problem Statement** - Who, what, why it's painful
3. **Target Users** - Personas and jobs-to-be-done
4. **Strategic Context** - Business goals, market opportunity
5. **Solution Overview** - High-level description, user flows
6. **Success Metrics** - Primary and secondary metrics
7. **User Stories & Requirements** - With acceptance criteria
8. **Out of Scope** - What's NOT being built
9. **Dependencies & Risks** - Technical and external
10. **Open Questions** - Unresolved decisions

## Process

### Step 1: Problem Definition
- What problem are we solving?
- Who has this problem?
- Why is it painful?
- What evidence do we have?

### Step 2: User Understanding
- Primary and secondary personas
- Jobs-to-be-done
- Current workarounds
- User research insights

### Step 3: Solution Design
- Proposed solution overview
- Key features and functionality
- User flows
- Wireframes (if available)

### Step 4: Requirements Definition
- User stories with acceptance criteria
- Functional requirements
- Non-functional requirements
- Edge cases and constraints

### Step 5: Success Metrics
- Primary metric (north star)
- Secondary metrics
- Current baseline
- Target goals

### Step 6: Scope & Constraints
- What's in scope
- What's out of scope (and why)
- Dependencies
- Risks and mitigations

## Output Format

The PRD will be saved as:
```
docs/prd/YYYY-MM-DD-<feature-name>-prd.md
```

## Example PRD Outline

```markdown
# AI-Powered Search Feature PRD

## 1. Executive Summary
We're building an AI-powered search feature to help users find relevant content faster, reducing search time by 50% and improving content discovery.

## 2. Problem Statement
**Who:** Content platform users
**What:** Difficulty finding relevant content in large libraries
**Why:** Current keyword search returns too many irrelevant results
**Evidence:** 60% of users abandon search after first attempt; support tickets up 40%

## 3. Target Users
**Primary:** Content consumers (80% of users)
- Goal: Quickly find specific content
- Frustration: Too many irrelevant results

**Secondary:** Content creators (20% of users)
- Goal: Understand what content performs well
- Frustration: No insights into search patterns

## 4. Strategic Context
- **Business Goal:** Increase user engagement by 30%
- **Market:** $10B content discovery market growing 25% YoY
- **Competitive:** Competitors launching AI features; we need parity
- **Why Now:** Q3 OKR focus on engagement; engineering capacity available

## 5. Solution Overview
**AI Search with:**
- Natural language queries
- Semantic understanding
- Personalized results
- Related content suggestions

**Key Features:**
1. Smart search bar with autocomplete
2. AI-generated result summaries
3. "Did you mean" suggestions
4. Search history and favorites

## 6. Success Metrics
| Metric | Current | Target | Timeline |
|--------|---------|--------|----------|
| Search success rate | 45% | 70% | 3 months |
| Time to find content | 3.5 min | 1.5 min | 3 months |
| User satisfaction | 3.2/5 | 4.2/5 | 3 months |

## 7. User Stories

**US-1: Natural Language Search**
As a user, I want to search using natural language so that I can find content without knowing exact keywords.
- **AC1:** Accept queries like "best practices for remote teams"
- **AC2:** Return relevant results even without keyword matches
- **AC3:** Show AI-generated summary of top results

**US-2: Personalized Results**
As a returning user, I want personalized search results so that I see content relevant to my interests.
- **AC1:** Consider user's past behavior
- **AC2:** Prioritize content from followed creators
- **AC3:** Allow users to customize preferences

## 8. Out of Scope
- **Voice search** - Will consider in v2 based on usage
- **Multi-language support** - English only for MVP
- **Advanced filters** - Basic filters only; advanced in v2

## 9. Dependencies & Risks
**Dependencies:**
- AI/ML infrastructure (ETA: 2 weeks)
- Search index upgrade (ETA: 1 week)

**Risks:**
- **AI accuracy** - Mitigation: Start with hybrid search (AI + keyword)
- **Performance** - Mitigation: Caching layer, gradual rollout

## 10. Open Questions
- [ ] Should we include image search in MVP?
- [ ] What's the max query length?
- [ ] Do we need admin controls for search results?
```

## Best Practices

### DO:
- Start with the problem, not the solution
- Include specific, measurable success criteria
- Define what's out of scope
- Use job stories for user needs
- Keep it living—update as you learn

### DON'T:
- Specify UI pixel-by-pixel (that's for design)
- Make it a contract that can't change
- Skip the "why" and go straight to features
- Forget edge cases and error states

## Getting Started

To create a PRD, tell me:
1. What product/feature are you building?
2. What problem does it solve?
3. Who are the target users?

I'll guide you through the rest!

## Related Skills

- **prd-development** - Extended PRD creation workflow
- **brainstorming** - Explore ideas before writing PRD
- **user-story** - Create detailed user stories
- **epic-hypothesis** - Frame epics with hypothesis

## Resources

- [Atlassian PRD Template](https://www.atlassian.com/software/confluence/templates/product-requirements)
- [Intercom's Product Management Guides](https://www.intercom.com/blog/product-management-guides/)
- [Mind the Product PRD Guide](https://www.mindtheproduct.com/product-requirements-document-prd/)

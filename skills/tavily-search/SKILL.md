---
name: tavily-search
description: Tavily AI Search API - High-quality web search with intelligent result ranking and content extraction. Use when you need accurate, up-to-date web search results with source citations.
allowed-tools: Bash(tavily-search:*), WebFetch
---

# Tavily Search Skill

High-quality AI-powered web search using Tavily API.

## Prerequisites

You need a Tavily API key. Get one free at https://tavily.com

Set environment variable:
```bash
export TAVILY_API_KEY="your-api-key"
```

## Features

- 🔍 High-quality web search with AI ranking
- 📰 Content extraction from search results
- 📊 Intelligent result filtering
- 🔗 Source citations included
- 💰 Free tier: 1,000 requests/month

## Usage

### Basic Search

```bash
# Search with Tavily
tavily-search "your search query"
```

### Advanced Options

```bash
# Search with specific parameters
tavily-search "query" --max-results 10 --include-raw-content

# Search news only
tavily-search "query" --search-depth advanced --include-domains bbc.com,cnn.com
```

## API Reference

### Search Parameters

| Parameter | Type | Description |
|-----------|------|-------------|
| query | string | Search query (required) |
| max_results | int | Number of results (1-20, default: 5) |
| search_depth | string | "basic" or "advanced" |
| include_answer | bool | Include AI-generated answer |
| include_raw_content | bool | Include full page content |
| include_domains | array | Limit to specific domains |
| exclude_domains | array | Exclude specific domains |

## Example Output

```json
{
  "query": "OpenClaw AI framework",
  "results": [
    {
      "title": "OpenClaw - AI Agent Framework",
      "url": "https://openclaw.ai",
      "content": "OpenClaw is an open-source AI agent framework...",
      "score": 0.95
    }
  ],
  "answer": "OpenClaw is an open-source AI agent framework that..."
}
```

## Installation

```bash
# Install Python package
pip install tavily-python

# Or use the skill directly - it will handle API calls
```

## Pricing

- **Free**: 1,000 API calls/month
- **Pro**: $0.025 per 1,000 API calls

More info: https://tavily.com/#pricing

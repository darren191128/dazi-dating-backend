#!/usr/bin/env python3
"""
Tavily Search CLI - Simple command line interface for Tavily API
"""

import sys
import os
import json
import urllib.request
import urllib.error

def search_tavily(query, max_results=5, search_depth="basic"):
    """
    Search using Tavily API
    """
    api_key = os.environ.get("TAVILY_API_KEY")
    
    if not api_key:
        print("❌ Error: TAVILY_API_KEY not set")
        print("Get your free API key at: https://tavily.com")
        print("Then set: export TAVILY_API_KEY='your-key'")
        sys.exit(1)
    
    url = "https://api.tavily.com/search"
    
    payload = {
        "api_key": api_key,
        "query": query,
        "max_results": max_results,
        "search_depth": search_depth,
        "include_answer": True,
        "include_raw_content": False
    }
    
    headers = {
        "Content-Type": "application/json"
    }
    
    try:
        data = json.dumps(payload).encode('utf-8')
        req = urllib.request.Request(url, data=data, headers=headers, method='POST')
        
        with urllib.request.urlopen(req, timeout=30) as response:
            result = json.loads(response.read().decode('utf-8'))
            return result
            
    except urllib.error.HTTPError as e:
        print(f"❌ HTTP Error: {e.code} - {e.reason}")
        try:
            error_body = e.read().decode('utf-8')
            print(f"Details: {error_body}")
        except:
            pass
        sys.exit(1)
    except Exception as e:
        print(f"❌ Error: {e}")
        sys.exit(1)

def format_results(result):
    """
    Format search results for display
    """
    print(f"\n🔍 Query: {result.get('query', 'N/A')}")
    print("=" * 80)
    
    # AI Answer
    if 'answer' in result and result['answer']:
        print(f"\n🤖 AI Answer:\n{result['answer']}\n")
        print("-" * 80)
    
    # Results
    results = result.get('results', [])
    print(f"\n📊 Found {len(results)} results:\n")
    
    for i, item in enumerate(results, 1):
        title = item.get('title', 'No title')
        url = item.get('url', 'No URL')
        content = item.get('content', 'No content')
        score = item.get('score', 0)
        
        print(f"{i}. {title}")
        print(f"   🔗 {url}")
        print(f"   📈 Score: {score:.2f}")
        print(f"   📝 {content[:200]}..." if len(content) > 200 else f"   📝 {content}")
        print()

def main():
    if len(sys.argv) < 2:
        print("Usage: tavily-search <query> [max_results] [search_depth]")
        print("Example: tavily-search 'OpenClaw AI framework' 5 advanced")
        sys.exit(1)
    
    query = sys.argv[1]
    max_results = int(sys.argv[2]) if len(sys.argv) > 2 else 5
    search_depth = sys.argv[3] if len(sys.argv) > 3 else "basic"
    
    print(f"🔍 Searching Tavily for: {query}")
    
    result = search_tavily(query, max_results, search_depth)
    format_results(result)
    
    # Also output raw JSON for piping
    print("\n" + "=" * 80)
    print("📄 Raw JSON output:")
    print(json.dumps(result, indent=2, ensure_ascii=False))

if __name__ == "__main__":
    main()

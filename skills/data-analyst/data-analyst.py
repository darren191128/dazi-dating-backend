#!/usr/bin/env python3
"""
Data Analyst CLI - Quick data analysis and visualization
"""

import sys
import os
import json

def show_help():
    print("""
📊 Data Analyst CLI
===================

Usage: data-analyst <command> [options]

Commands:
  explore <file>        - Quick data exploration
  stats <file>          - Statistical summary
  visualize <file> <col> - Create chart
  sql "<query>"         - Generate SQL from natural language
  clean <file>          - Clean data (handle missing, duplicates)
  report <file>         - Generate full analysis report

Examples:
  data-analyst explore data.csv
  data-analyst stats sales.xlsx
  data-analyst visualize data.csv revenue
  data-analyst sql "top 10 customers by revenue"
  data-analyst clean raw_data.csv
  data-analyst report dataset.csv

Note: Requires Python with pandas, matplotlib, seaborn installed.
      Install with: pip install pandas matplotlib seaborn scipy
""")

def explore_data(filepath):
    """Quick data exploration"""
    try:
        import pandas as pd
        df = pd.read_csv(filepath)
        
        print(f"\n📊 Data Exploration: {filepath}")
        print("=" * 60)
        print(f"\nShape: {df.shape[0]} rows × {df.shape[1]} columns")
        print(f"\nColumns: {list(df.columns)}")
        print(f"\nFirst 5 rows:")
        print(df.head())
        print(f"\nData types:")
        print(df.dtypes)
        print(f"\nMissing values:")
        print(df.isnull().sum())
        print(f"\nBasic statistics:")
        print(df.describe())
        
    except Exception as e:
        print(f"❌ Error: {e}")
        print("Make sure pandas is installed: pip install pandas")

def generate_sql(natural_query):
    """Generate SQL from natural language"""
    # Simple template-based generation
    templates = {
        "top": "SELECT {columns} FROM {table} ORDER BY {order_col} DESC LIMIT {limit}",
        "count": "SELECT {column}, COUNT(*) as count FROM {table} GROUP BY {column}",
        "sum": "SELECT {column}, SUM({value_col}) as total FROM {table} GROUP BY {column}",
        "average": "SELECT AVG({column}) as average FROM {table}",
        "filter": "SELECT * FROM {table} WHERE {condition}",
    }
    
    print(f"\n📝 SQL Query for: '{natural_query}'")
    print("=" * 60)
    
    # This is a simplified version - in practice, you'd use an LLM
    print("\n-- Example SQL templates:")
    print("\n-- Top N records:")
    print("SELECT * FROM table_name ORDER BY column_name DESC LIMIT 10;")
    
    print("\n-- Group by with aggregation:")
    print("SELECT category, COUNT(*) as count, AVG(value) as avg_value")
    print("FROM table_name")
    print("GROUP BY category;")
    
    print("\n-- Filter and sort:")
    print("SELECT * FROM table_name")
    print("WHERE date >= '2024-01-01'")
    print("ORDER BY created_at DESC;")
    
    print("\n💡 Tip: Replace table_name and column_name with your actual names")

def main():
    if len(sys.argv) < 2:
        show_help()
        sys.exit(1)
    
    command = sys.argv[1]
    
    if command == 'help':
        show_help()
    elif command == 'explore' and len(sys.argv) > 2:
        explore_data(sys.argv[2])
    elif command == 'sql' and len(sys.argv) > 2:
        generate_sql(sys.argv[2])
    else:
        print(f"Unknown command or missing arguments: {command}")
        show_help()
        sys.exit(1)

if __name__ == '__main__':
    main()

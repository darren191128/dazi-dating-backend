---
name: data-analyst
description: Comprehensive data analysis and visualization skill. Use when you need to analyze datasets, create charts and graphs, perform statistical analysis, generate insights from data, write SQL queries, or create data reports. Supports CSV, Excel, JSON data formats and integrates with popular databases.
---

# Data Analyst

A comprehensive skill for data analysis, visualization, and reporting.

## Capabilities

### 1. Data Loading & Exploration
- Load data from CSV, Excel, JSON, SQL databases
- Explore data structure, types, and quality
- Generate data profiles and summaries

### 2. Data Cleaning & Transformation
- Handle missing values
- Remove duplicates
- Data type conversion
- Feature engineering
- Outlier detection

### 3. Statistical Analysis
- Descriptive statistics (mean, median, std, etc.)
- Correlation analysis
- Hypothesis testing
- Regression analysis
- A/B test analysis

### 4. Data Visualization
- Line charts, bar charts, pie charts
- Scatter plots, histograms
- Heatmaps, correlation matrices
- Time series plots
- Interactive dashboards

### 5. SQL Query Generation
- Natural language to SQL
- Query optimization suggestions
- Support for PostgreSQL, MySQL, BigQuery, SQLite

### 6. Reporting
- Automated insight generation
- Executive summaries
- Data-driven recommendations

## Quick Start

### Analyze a CSV file
```python
import pandas as pd
import matplotlib.pyplot as plt
import seaborn as sns

# Load data
df = pd.read_csv('data.csv')

# Quick overview
print(df.head())
print(df.describe())
print(df.info())

# Visualize
plt.figure(figsize=(10, 6))
sns.histplot(df['column_name'])
plt.title('Distribution')
plt.savefig('output.png')
```

### Generate SQL Query
```sql
-- Natural language: "Find top 10 customers by revenue"
SELECT customer_id, SUM(revenue) as total_revenue
FROM orders
GROUP BY customer_id
ORDER BY total_revenue DESC
LIMIT 10;
```

### Statistical Analysis
```python
from scipy import stats

# T-test
result = stats.ttest_ind(group_a, group_b)
print(f"T-statistic: {result.statistic}")
print(f"P-value: {result.pvalue}")

# Correlation
correlation = df.corr()
sns.heatmap(correlation, annot=True)
```

## Common Tasks

### Task 1: Exploratory Data Analysis (EDA)
```python
# 1. Load data
# 2. Check missing values
# 3. Distribution analysis
# 4. Correlation analysis
# 5. Outlier detection
```

### Task 2: A/B Test Analysis
```python
# 1. Calculate conversion rates
# 2. Statistical significance test
# 3. Confidence intervals
# 4. Sample size validation
# 5. Recommendation
```

### Task 3: Cohort Analysis
```python
# 1. Define cohorts (e.g., by signup month)
# 2. Calculate retention rates
# 3. Create retention matrix
# 4. Visualize cohort table
```

### Task 4: Time Series Analysis
```python
# 1. Load time series data
# 2. Decompose trend/seasonality
# 3. Forecast future values
# 4. Anomaly detection
```

## Tools & Libraries

### Python Stack
- **pandas**: Data manipulation
- **numpy**: Numerical computing
- **matplotlib/seaborn**: Visualization
- **scipy**: Statistical analysis
- **scikit-learn**: Machine learning
- **plotly**: Interactive charts

### SQL Databases
- PostgreSQL
- MySQL
- BigQuery
- SQLite
- Snowflake

### File Formats
- CSV
- Excel (.xlsx, .xls)
- JSON
- Parquet
- SQL dumps

## Best Practices

### Data Quality
1. Always check for missing values
2. Validate data types
3. Look for outliers
4. Check for duplicates
5. Document data sources

### Analysis
1. Start with descriptive statistics
2. Visualize before modeling
3. Check assumptions
4. Validate results
5. Document methodology

### Visualization
1. Choose appropriate chart types
2. Use clear labels and titles
3. Add context/annotations
4. Keep it simple
5. Ensure accessibility

## Example Workflows

### Workflow 1: Sales Analysis
1. Load sales data
2. Clean and validate
3. Calculate KPIs (revenue, growth, etc.)
4. Segment analysis (by region, product, time)
5. Create dashboard
6. Generate insights report

### Workflow 2: User Behavior Analysis
1. Load user event data
2. Create user segments
3. Funnel analysis
4. Cohort retention analysis
5. Predict churn risk
6. Recommend actions

### Workflow 3: Financial Analysis
1. Load financial data
2. Calculate financial metrics
3. Trend analysis
4. Variance analysis
5. Forecasting
6. Risk assessment

## Output Formats

### Charts & Visualizations
- PNG/JPG images
- Interactive HTML (Plotly)
- PDF reports
- Dashboards

### Data Export
- Cleaned CSV
- Excel with multiple sheets
- JSON
- SQL inserts

### Reports
- Markdown summaries
- Jupyter notebooks
- Executive presentations

## Related Skills

- **pm-data-analytics** - PM-focused data analysis (cohort, A/B test, SQL)
- **ab-test-setup** - Design and implement A/B tests
- **analytics-tracking** - Set up analytics and tracking
- **python-testing-patterns** - Test data pipelines

## Resources

- [Pandas Documentation](https://pandas.pydata.org/docs/)
- [Seaborn Tutorial](https://seaborn.pydata.org/tutorial.html)
- [SQL Tutorial](https://www.w3schools.com/sql/)
- [Statistical Thinking](https://www.khanacademy.org/math/statistics-probability)

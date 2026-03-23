---
name: cicd-pipeline
description: Complete CI/CD pipeline automation for continuous integration and deployment. Use when setting up automated build, test, and deployment pipelines, configuring GitHub Actions, GitLab CI, or other CI/CD tools, automating software delivery workflows, or implementing DevOps best practices.
---

# CI/CD Pipeline Automation

Complete continuous integration and deployment automation.

## Overview

This skill provides end-to-end CI/CD pipeline setup and management:
- 🔧 **Build Automation** - Automated builds on every commit
- 🧪 **Test Automation** - Automated testing pipelines
- 🚀 **Deployment Automation** - Automated deployments to staging/production
- 📊 **Monitoring** - Pipeline health and deployment tracking
- 🔄 **Rollback** - Automated rollback on failure

## Supported Platforms

### GitHub Actions
```yaml
# .github/workflows/ci.yml
name: CI/CD Pipeline

on:
  push:
    branches: [ main, develop ]
  pull_request:
    branches: [ main ]

jobs:
  build:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3
      - name: Setup Node.js
        uses: actions/setup-node@v3
        with:
          node-version: '18'
      - name: Install dependencies
        run: npm ci
      - name: Run tests
        run: npm test
      - name: Build
        run: npm run build
      - name: Deploy to staging
        if: github.ref == 'refs/heads/develop'
        run: npm run deploy:staging
      - name: Deploy to production
        if: github.ref == 'refs/heads/main'
        run: npm run deploy:production
```

### GitLab CI
```yaml
# .gitlab-ci.yml
stages:
  - build
  - test
  - deploy

build:
  stage: build
  script:
    - npm ci
    - npm run build
  artifacts:
    paths:
      - dist/

test:
  stage: test
  script:
    - npm test
  coverage: '/All files[^|]*\\|[^|]*\\s+([\\d\\.]+)/'

deploy:staging:
  stage: deploy
  script:
    - npm run deploy:staging
  only:
    - develop

deploy:production:
  stage: deploy
  script:
    - npm run deploy:production
  only:
    - main
  when: manual
```

### Docker-based CI/CD
```dockerfile
# Dockerfile
FROM node:18-alpine AS builder
WORKDIR /app
COPY package*.json ./
RUN npm ci
COPY . .
RUN npm run build

FROM nginx:alpine
COPY --from=builder /app/dist /usr/share/nginx/html
EXPOSE 80
```

## Pipeline Stages

### 1. Build Stage
```bash
# Install dependencies
npm ci

# Run linter
npm run lint

# Build application
npm run build

# Generate build artifacts
npm run package
```

### 2. Test Stage
```bash
# Unit tests
npm run test:unit

# Integration tests
npm run test:integration

# E2E tests
npm run test:e2e

# Coverage report
npm run test:coverage
```

### 3. Security Scan
```bash
# Dependency audit
npm audit

# SAST (Static Application Security Testing)
npx sonarqube-scanner

# Container scan
docker run --rm -v $(pwd):/app securecodewarrior/docker-security-scan
```

### 4. Deploy Stage
```bash
# Deploy to staging
npm run deploy:staging

# Smoke tests
npm run test:smoke

# Deploy to production
npm run deploy:production

# Health check
curl -f https://api.example.com/health || exit 1
```

## Best Practices

### Pipeline as Code
- Store pipeline config in version control
- Use templates for consistency
- Parameterize environment-specific values

### Security
- Never hardcode secrets
- Use secret management (GitHub Secrets, Vault)
- Scan for vulnerabilities
- Implement least privilege

### Performance
- Use caching for dependencies
- Parallelize independent jobs
- Optimize Docker images
- Use appropriate runner sizes

### Reliability
- Implement health checks
- Set up monitoring and alerts
- Plan rollback procedures
- Test pipeline in staging first

## Common Patterns

### Blue-Green Deployment
```yaml
deploy:
  script:
    - deploy_to_green_environment
    - run_smoke_tests
    - switch_traffic_to_green
    - keep_blue_for_rollback
```

### Canary Deployment
```yaml
deploy:
  script:
    - deploy_to_canary_group (5% traffic)
    - monitor_metrics
    - gradually_increase_traffic
    - full_rollout_or_rollback
```

### Feature Flags
```yaml
test:
  script:
    - deploy_with_feature_flags_off
    - enable_flag_for_test_users
    - run_ab_tests
    - gradual_rollout
```

## Monitoring & Observability

### Pipeline Metrics
- Build duration
- Test pass rate
- Deployment frequency
- Lead time for changes
- Mean time to recovery

### Alerts
```yaml
# Alert on pipeline failure
- name: Pipeline Failed
  condition: pipeline_status == "failed"
  action: notify_slack

# Alert on slow builds
- name: Slow Build
  condition: build_duration > 10_minutes
  action: notify_team
```

## Integration with Other Skills

- **writing-plans** - Plan CI/CD implementation
- **executing-plans** - Execute pipeline setup
- **proactive-agent** - Monitor pipeline health
- **skill-vetter** - Validate pipeline configuration

## Resources

- [GitHub Actions Documentation](https://docs.github.com/en/actions)
- [GitLab CI Documentation](https://docs.gitlab.com/ee/ci/)
- [Docker Best Practices](https://docs.docker.com/develop/dev-best-practices/)

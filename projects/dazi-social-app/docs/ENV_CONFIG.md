# 搭子交友微信小程序 - 环境变量配置指南

## 后端服务环境变量

### 通用配置（所有服务共用）

```bash
# MySQL配置
export MYSQL_HOST=localhost
export MYSQL_PORT=3306
export MYSQL_USER=root
export MYSQL_PASSWORD=your_secure_password_here

# Redis配置
export REDIS_HOST=localhost
export REDIS_PORT=6379
export REDIS_PASSWORD=your_redis_password

# Nacos配置
export NACOS_HOST=localhost
export NACOS_PORT=8848

# JWT配置（重要：生产环境请使用强密码）
export JWT_SECRET=your-256-bit-secret-key-here-change-in-production
export JWT_EXPIRATION=86400000

# 日志级别
export LOG_LEVEL=INFO
export MYBATIS_LOG_IMPL=org.apache.ibatis.logging.nologging.NoLoggingImpl
```

### 微信支付配置（payment-service）

```bash
export WXPAY_APP_ID=your_wx_app_id
export WXPAY_MCH_ID=your_merchant_id
export WXPAY_API_KEY=your_api_key
export WXPAY_NOTIFY_URL=https://api.yourdomain.com/api/payment/callback
```

## 生产环境配置建议

### 1. 使用Docker Compose

```yaml
version: '3.8'
services:
  user-service:
    image: dazi/user-service:latest
    environment:
      - MYSQL_HOST=mysql
      - MYSQL_PASSWORD=${MYSQL_PASSWORD}
      - JWT_SECRET=${JWT_SECRET}
    env_file:
      - .env.production
```

### 2. 使用Kubernetes Secret

```yaml
apiVersion: v1
kind: Secret
metadata:
  name: dazi-secrets
type: Opaque
stringData:
  MYSQL_PASSWORD: <base64-encoded>
  JWT_SECRET: <base64-encoded>
  WXPAY_API_KEY: <base64-encoded>
```

### 3. 使用配置中心（Nacos）

在Nacos控制台添加配置：
- Data ID: `dazi-common.yaml`
- Group: `DEFAULT_GROUP`
- 配置格式: YAML

```yaml
jwt:
  secret: ${JWT_SECRET}
  expiration: 86400000
```

## 安全注意事项

1. **永远不要将.env文件提交到Git**
2. **生产环境JWT密钥至少32位随机字符串**
3. **数据库密码使用强密码（12位以上，包含大小写+数字+特殊字符）**
4. **定期轮换密钥和密码**
5. **使用HTTPS传输敏感数据**

## 本地开发配置

创建 `.env` 文件在项目根目录：

```bash
# .env
MYSQL_HOST=localhost
MYSQL_PORT=3306
MYSQL_USER=root
MYSQL_PASSWORD=123456
REDIS_HOST=localhost
REDIS_PORT=6379
JWT_SECRET=dazi-dev-secret-key-2024
```

**注意**: .env文件已添加到.gitignore，不会被提交

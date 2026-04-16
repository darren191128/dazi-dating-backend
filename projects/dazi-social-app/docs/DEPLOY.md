# 搭子交友微信小程序 - 部署文档

**文档版本**: v1.0  
**创建日期**: 2026-03-24  
**创建人**: 云壹  
**适用环境**: 生产环境

---

## 一、部署架构

### 1.1 架构图

```
用户 -> 微信小程序 -> 微信服务器 -> 负载均衡 -> API网关 -> 微服务集群
                                              |
                                              -> MySQL主从
                                              -> Redis集群
                                              -> 对象存储
```

### 1.2 服务器规划

| 服务器 | 配置 | 数量 | 用途 |
|--------|------|------|------|
| 应用服务器 | 8核16G | 3台 | 运行微服务 |
| 数据库服务器 | 8核32G | 2台 | MySQL主从 |
| 缓存服务器 | 4核8G | 3台 | Redis集群 |
| 文件服务器 | 4核8G | 1台 | 对象存储 |

---

## 二、环境准备

### 2.1 系统环境

**操作系统**: CentOS 7.9 / Ubuntu 20.04 LTS

**基础软件**:
```bash
# 安装JDK 17
yum install -y java-17-openjdk java-17-openjdk-devel

# 安装Maven
yum install -y maven

# 安装Git
yum install -y git

# 安装Nginx
yum install -y nginx

# 安装MySQL 8.0
wget https://dev.mysql.com/get/mysql80-community-release-el7-11.noarch.rpm
rpm -Uvh mysql80-community-release-el7-11.noarch.rpm
yum install -y mysql-community-server

# 安装Redis 6.0
yum install -y redis

# 安装Docker
curl -fsSL https://get.docker.com | bash
systemctl start docker
systemctl enable docker
```

### 2.2 网络配置

**端口开放**:
- 80/443: Nginx
- 3306: MySQL
- 6379: Redis
- 8080: API网关
- 8001-8011: 微服务

**域名配置**:
```
api.dazidating.com -> 负载均衡IP
admin.dazidating.com -> 后台管理IP
static.dazidating.com -> CDN/对象存储
```

---

## 三、数据库部署

### 3.1 MySQL部署

#### 主库配置

```bash
# 启动MySQL
systemctl start mysqld
systemctl enable mysqld

# 获取临时密码
grep 'temporary password' /var/log/mysqld.log

# 登录并修改密码
mysql -u root -p
ALTER USER 'root'@'localhost' IDENTIFIED BY 'YourStrongPassword123!';

# 创建数据库
CREATE DATABASE dazi_dating CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

# 创建应用用户
CREATE USER 'dazi_app'@'%' IDENTIFIED BY 'DaziAppPassword123!';
GRANT ALL PRIVILEGES ON dazi_dating.* TO 'dazi_app'@'%';
FLUSH PRIVILEGES;
```

#### 数据库初始化

```bash
# 克隆代码
git clone https://github.com/darren191128/dazi-dating-backend.git
cd dazi-dating-backend

# 执行数据库脚本
mysql -u dazi_app -p dazi_dating < docs/database-design.sql
mysql -u dazi_app -p dazi_dating < docs/p0-database.sql
mysql -u dazi_app -p dazi_dating < docs/p1-wallet-checkin.sql
mysql -u dazi_app -p dazi_dating < docs/p1-gift-intimacy-report.sql
mysql -u dazi_app -p dazi_dating < docs/p1-rtc-group.sql
mysql -u dazi_app -p dazi_dating < docs/p1-filter-topic-hot.sql
```

### 3.2 Redis部署

```bash
# 配置Redis
vi /etc/redis.conf

# 修改配置
bind 0.0.0.0
port 6379
requirepass YourRedisPassword123!
maxmemory 4gb
maxmemory-policy allkeys-lru

# 启动Redis
systemctl start redis
systemctl enable redis
```

---

## 四、后端部署

### 4.1 代码准备

```bash
# 创建应用目录
mkdir -p /opt/dazi-dating
cd /opt/dazi-dating

# 克隆代码
git clone https://github.com/darren191128/dazi-dating-backend.git backend
cd backend

# 切换到主分支
git checkout main
```

### 4.2 配置文件

#### 环境变量配置

创建 `/opt/dazi-dating/.env`:

```bash
# 数据库配置
DB_HOST=localhost
DB_PORT=3306
DB_NAME=dazi_dating
DB_USER=dazi_app
DB_PASSWORD=DaziAppPassword123!

# Redis配置
REDIS_HOST=localhost
REDIS_PORT=6379
REDIS_PASSWORD=YourRedisPassword123!

# JWT配置
JWT_SECRET=YourJWTSecretKeyHere12345678901234567890
JWT_EXPIRATION=86400000

# 微信配置
WX_APPID=your_wechat_appid
WX_SECRET=your_wechat_secret
WX_MCH_ID=your_merchant_id
WX_MCH_KEY=your_merchant_key

# 腾讯云TRTC配置
TRTC_SDK_APPID=your_trtc_sdk_appid
TRTC_SECRET_KEY=your_trtc_secret_key

# 阿里云OSS配置
OSS_ENDPOINT=oss-cn-hangzhou.aliyuncs.com
OSS_ACCESS_KEY=your_access_key
OSS_SECRET_KEY=your_secret_key
OSS_BUCKET_NAME=dazi-dating

# Nacos配置
NACOS_SERVER=localhost:8848
```

### 4.3 编译打包

```bash
cd /opt/dazi-dating/backend

# 编译所有模块
mvn clean package -DskipTests

# 检查编译结果
ls -la */target/*.jar
```

### 4.4 服务启动脚本

创建 `/opt/dazi-dating/start-services.sh`:

```bash
#!/bin/bash

# 加载环境变量
source /opt/dazi-dating/.env

# 启动网关
cd /opt/dazi-dating/backend/gateway-service/target
nohup java -jar gateway-service-1.0.0.jar \
  --server.port=8080 \
  --spring.cloud.nacos.discovery.server-addr=${NACOS_SERVER} \
  > /var/log/dazi/gateway.log 2>&1 &

# 启动用户服务
cd /opt/dazi-dating/backend/user-service/target
nohup java -jar user-service-1.0.0.jar \
  --server.port=8001 \
  --spring.datasource.url=jdbc:mysql://${DB_HOST}:${DB_PORT}/${DB_NAME} \
  --spring.datasource.username=${DB_USER} \
  --spring.datasource.password=${DB_PASSWORD} \
  --spring.redis.host=${REDIS_HOST} \
  --spring.redis.port=${REDIS_PORT} \
  --spring.redis.password=${REDIS_PASSWORD} \
  --jwt.secret=${JWT_SECRET} \
  --jwt.expiration=${JWT_EXPIRATION} \
  > /var/log/dazi/user-service.log 2>&1 &

# 启动其他服务（类似配置）
# ...

echo "所有服务已启动"
```

### 4.5 服务管理脚本

创建 `/opt/dazi-dating/manage.sh`:

```bash
#!/bin/bash

SERVICES="gateway user activity match payment message review admin moment vip gift rtc"

case $1 in
  start)
    ./start-services.sh
    ;;
  stop)
    for service in $SERVICES; do
      pkill -f "${service}-service"
    done
    echo "所有服务已停止"
    ;;
  restart)
    $0 stop
    sleep 5
    $0 start
    ;;
  status)
    for service in $SERVICES; do
      pid=$(pgrep -f "${service}-service")
      if [ -n "$pid" ]; then
        echo "$service: 运行中 (PID: $pid)"
      else
        echo "$service: 未运行"
      fi
    done
    ;;
  *)
    echo "用法: $0 {start|stop|restart|status}"
    exit 1
    ;;
esac
```

---

## 五、前端部署

### 5.1 代码准备

```bash
cd /opt/dazi-dating
git clone https://github.com/darren191128/dazi-dating-frontend.git frontend
cd frontend
```

### 5.2 配置修改

修改 `src/config/index.js`:

```javascript
export const config = {
  // 生产环境API地址
  baseURL: 'https://api.dazidating.com',
  
  // 图片上传地址
  uploadURL: 'https://static.dazidating.com',
  
  // 腾讯云TRTC配置
  trtc: {
    sdkAppId: your_trtc_sdk_appid
  }
}
```

### 5.3 小程序上传

```bash
# 安装依赖
npm install

# 编译
npm run build:mp-weixin

# 使用微信开发者工具上传
# 1. 打开微信开发者工具
# 2. 导入 dist/build/mp-weixin 目录
# 3. 点击上传按钮
```

---

## 六、Nginx配置

### 6.1 安装Nginx

```bash
yum install -y nginx
```

### 6.2 配置文件

创建 `/etc/nginx/conf.d/dazi-dating.conf`:

```nginx
# API网关
server {
    listen 80;
    server_name api.dazidating.com;
    
    location / {
        proxy_pass http://localhost:8080;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
    }
}

# 后台管理
server {
    listen 80;
    server_name admin.dazidating.com;
    
    location / {
        proxy_pass http://localhost:8007;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
    }
}

# 静态资源
server {
    listen 80;
    server_name static.dazidating.com;
    
    location / {
        alias /opt/dazi-dating/static/;
        expires 30d;
    }
}
```

### 6.3 启动Nginx

```bash
nginx -t
systemctl start nginx
systemctl enable nginx
```

---

## 七、SSL证书配置

### 7.1 申请证书

使用 Let's Encrypt 免费证书:

```bash
# 安装certbot
yum install -y certbot python3-certbot-nginx

# 申请证书
certbot --nginx -d api.dazidating.com -d admin.dazidating.com -d static.dazidating.com

# 自动续期
echo "0 0,12 * * * root python3 -c 'import random; import time; time.sleep(random.random() * 3600)' && certbot renew -q" | sudo tee -a /etc/crontab > /dev/null
```

---

## 八、监控配置

### 8.1 安装Prometheus + Grafana

```bash
# 使用Docker安装
docker run -d \
  --name prometheus \
  -p 9090:9090 \
  -v /opt/prometheus.yml:/etc/prometheus/prometheus.yml \
  prom/prometheus

docker run -d \
  --name grafana \
  -p 3000:3000 \
  grafana/grafana
```

### 8.2 应用监控

在微服务中添加Actuator依赖，配置Prometheus端点。

---

## 九、备份策略

### 9.1 数据库备份

创建 `/opt/backup/backup-mysql.sh`:

```bash
#!/bin/bash

BACKUP_DIR=/opt/backup/mysql
DATE=$(date +%Y%m%d_%H%M%S)

# 全量备份
mysqldump -u dazi_app -p'DaziAppPassword123!' dazi_dating > $BACKUP_DIR/dazi_dating_$DATE.sql

# 保留最近7天备份
find $BACKUP_DIR -name "dazi_dating_*.sql" -mtime +7 -delete
```

添加定时任务:
```bash
0 2 * * * /opt/backup/backup-mysql.sh
```

---

## 十、验证部署

### 10.1 服务健康检查

```bash
# 检查所有服务状态
curl http://localhost:8080/actuator/health

# 检查数据库连接
mysql -u dazi_app -p -e "SELECT 1"

# 检查Redis连接
redis-cli ping
```

### 10.2 功能验证

1. 微信小程序登录
2. 发布动态
3. 滑动匹配
4. 发送消息
5. 充值金币
6. 音视频通话

---

## 十一、故障排查

### 11.1 查看日志

```bash
# 查看服务日志
tail -f /var/log/dazi/gateway.log

# 查看Nginx日志
tail -f /var/log/nginx/error.log

# 查看MySQL日志
tail -f /var/log/mysqld.log
```

### 11.2 常见问题

**问题1: 服务启动失败**
- 检查端口是否被占用
- 检查数据库连接配置
- 检查日志文件权限

**问题2: 接口返回500错误**
- 检查微服务是否正常运行
- 检查数据库连接
- 查看具体错误日志

**问题3: 微信小程序无法连接**
- 检查域名备案
- 检查HTTPS证书
- 检查服务器防火墙

---

## 十二、维护操作

### 12.1 更新部署

```bash
cd /opt/dazi-dating/backend
git pull origin main
mvn clean package -DskipTests
./manage.sh restart
```

### 12.2 扩容操作

增加应用服务器，修改Nginx配置添加upstream。

---

**部署完成时间**: _______________  
**部署人**: _______________  
**验证结果**: _______________

---

*本文档为搭子交友微信小程序生产环境部署指南，请根据实际情况调整配置。*

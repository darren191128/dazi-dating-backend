# 部署文档

## 一、环境准备

### 1. 服务器要求
- CPU: 4核+
- 内存: 8GB+
- 磁盘: 50GB+
- 系统: CentOS 7+ / Ubuntu 20.04+

### 2. 软件安装
```bash
# 安装JDK 17
wget https://download.java.net/openjdk/jdk17/ri/openjdk-17+35_linux-x64_bin.tar.gz
tar -xzf openjdk-17+35_linux-x64_bin.tar.gz
export JAVA_HOME=/path/to/jdk-17
export PATH=$JAVA_HOME/bin:$PATH

# 安装MySQL 8.0
wget https://dev.mysql.com/get/mysql80-community-release-el7-11.noarch.rpm
rpm -Uvh mysql80-community-release-el7-11.noarch.rpm
yum install mysql-community-server
systemctl start mysqld

# 安装Redis
yum install redis
systemctl start redis

# 安装RabbitMQ
yum install rabbitmq-server
systemctl start rabbitmq-server

# 安装Nacos
wget https://github.com/alibaba/nacos/releases/download/2.2.3/nacos-server-2.2.3.tar.gz
tar -xzf nacos-server-2.2.3.tar.gz
cd nacos/bin
sh startup.sh -m standalone
```

## 二、数据库初始化

```sql
-- 创建数据库
CREATE DATABASE IF NOT EXISTS dazi_user DEFAULT CHARSET utf8mb4;
CREATE DATABASE IF NOT EXISTS dazi_activity DEFAULT CHARSET utf8mb4;
CREATE DATABASE IF NOT EXISTS dazi_payment DEFAULT CHARSET utf8mb4;

-- 创建用户
CREATE USER 'dazi'@'%' IDENTIFIED BY 'Dazi@2024';
GRANT ALL PRIVILEGES ON dazi_*.* TO 'dazi'@'%';
FLUSH PRIVILEGES;
```

## 三、后端部署

### 1. 编译打包
```bash
cd backend
mvn clean package -DskipTests
```

### 2. 启动服务
```bash
# 启动脚本
#!/bin/bash

# 启动user-service
nohup java -jar user-service/target/user-service-1.0.0.jar > logs/user.log 2>&1 &

# 启动activity-service
nohup java -jar activity-service/target/activity-service-1.0.0.jar > logs/activity.log 2>&1 &

# 启动match-service
nohup java -jar match-service/target/match-service-1.0.0.jar > logs/match.log 2>&1 &

# 启动payment-service
nohup java -jar payment-service/target/payment-service-1.0.0.jar > logs/payment.log 2>&1 &

# 启动gateway-service
nohup java -jar gateway-service/target/gateway-service-1.0.0.jar > logs/gateway.log 2>&1 &

echo "All services started!"
```

### 3. 服务检查
```bash
# 检查服务状态
curl http://localhost:8080/actuator/health

# 查看Nacos注册
登录 http://localhost:8848/nacos
```

## 四、前端部署

### 1. H5部署
```bash
cd frontend
npm install
npm run build:h5

# 部署到Nginx
cp -r dist/build/h5/* /usr/share/nginx/html/
```

### 2. 微信小程序
```bash
# 使用HBuilderX
# 1. 导入项目
# 2. 点击发行 -> 小程序-微信
# 3. 上传代码
# 4. 微信开发者工具提交审核
```

## 五、Nginx配置

```nginx
server {
    listen 80;
    server_name dazi.example.com;
    
    # 前端静态资源
    location / {
        root /usr/share/nginx/html;
        index index.html;
        try_files $uri $uri/ /index.html;
    }
    
    # API代理
    location /api/ {
        proxy_pass http://localhost:8080/;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
    }
}
```

## 六、监控配置

### 1. 日志收集
```bash
# 安装ELK
docker run -d -p 9200:9200 -p 5601:5601 elasticsearch:8.0.0
```

### 2. 告警配置
```yaml
# Prometheus配置
scrape_configs:
  - job_name: 'dazi-services'
    static_configs:
      - targets: ['localhost:8001', 'localhost:8002']
```

## 七、备份策略

```bash
# 数据库备份
0 2 * * * mysqldump -u root -p dazi_user > /backup/dazi_user_$(date +\%Y\%m\%d).sql

# 代码备份
0 3 * * * tar -czf /backup/code_$(date +\%Y\%m\%d).tar.gz /opt/dazi/
```

## 八、故障处理

| 问题 | 解决方案 |
|------|----------|
| 服务无法启动 | 检查端口占用、日志查看 |
| 数据库连接失败 | 检查网络、用户名密码 |
| Nacos注册失败 | 检查Nacos服务、网络 |
| 接口404 | 检查网关路由配置 |

## 九、联系方式

运维支持：ops@dazi.com

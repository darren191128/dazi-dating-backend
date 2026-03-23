# 若山律师事务所官网 - 部署指南

## 📋 部署前检查清单

### 1. 环境要求
- Node.js 18+
- Java 17+
- MySQL 8.0+
- Maven 3.8+

### 2. 文件完整性检查
确保所有文件已正确生成：
```bash
# 前端文件
cd /root/.openclaw/workspace/projects/ruoshan-law/vue-frontend
ls -la

# 后端文件
cd /root/.openclaw/workspace/projects/ruoshan-law/java-backend
ls -la

# 数据库文件
cd /root/.openclaw/workspace/projects/ruoshan-law/database
ls -la
```

---

## 🚀 部署步骤

### 步骤1: 部署数据库

```bash
# 1. 登录MySQL
mysql -u root -p

# 2. 执行初始化脚本
source /root/.openclaw/workspace/projects/ruoshan-law/database/init.sql

# 3. 执行补充数据脚本（可选）
source /root/.openclaw/workspace/projects/ruoshan-law/database/init-supplement.sql

# 4. 验证数据库
describe ruoshan_law.lawyers;
describe ruoshan_law.cases;
describe ruoshan_law.news;
describe ruoshan_law.careers;
describe ruoshan_law.contacts;
```

### 步骤2: 部署后端

```bash
# 1. 进入后端目录
cd /root/.openclaw/workspace/projects/ruoshan-law/java-backend

# 2. 修改数据库配置（如果需要）
vim src/main/resources/application.properties
# 修改以下配置：
# spring.datasource.username=your_username
# spring.datasource.password=your_password

# 3. 编译打包
mvn clean package -DskipTests

# 4. 运行后端服务
java -jar target/ruoshan-law-backend-1.0.0.jar

# 5. 验证后端服务
# 访问 http://localhost:8080/api/lawyers
```

### 步骤3: 部署前端

```bash
# 1. 进入前端目录
cd /root/.openclaw/workspace/projects/ruoshan-law/vue-frontend

# 2. 安装依赖
npm install

# 3. 开发模式运行（测试）
npm run dev

# 4. 生产构建
npm run build

# 5. 部署dist目录到Web服务器
# 将dist/目录复制到Nginx/Apache的web根目录
```

---

## ⚙️ Nginx配置示例

```nginx
server {
    listen 80;
    server_name ruoshan-law.com;
    
    # 前端静态文件
    location / {
        root /var/www/ruoshan-law/dist;
        index index.html;
        try_files $uri $uri/ /index.html;
    }
    
    # API代理
    location /api/ {
        proxy_pass http://localhost:8080/api/;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
    }
}
```

---

## 🔧 常见问题排查

### 问题1: 前端样式不生效
**解决:**
```bash
# 确保postcss.config.js存在
cat postcss.config.js

# 重新安装依赖
rm -rf node_modules package-lock.json
npm install

# 重新构建
npm run build
```

### 问题2: 后端数据库连接失败
**解决:**
```bash
# 检查MySQL服务
sudo systemctl status mysql

# 检查数据库是否存在
mysql -u root -p -e "SHOW DATABASES;"

# 检查用户权限
mysql -u root -p -e "SELECT user, host FROM mysql.user;"

# 检查防火墙
sudo ufw status
```

### 问题3: 跨域问题
**解决:**
- 确保CorsConfig.java已正确配置
- 检查前端请求的API地址是否正确
- 确保前后端都在运行

### 问题4: 端口被占用
**解决:**
```bash
# 查找占用8080端口的进程
lsof -i :8080

# 杀死进程
kill -9 <PID>

# 或使用其他端口
# 修改application.properties中的server.port
```

---

## ✅ 部署验证

### 验证前端
- [ ] 首页正常显示
- [ ] 导航菜单正常
- [ ] 所有页面可访问
- [ ] 响应式布局正常
- [ ] 样式正常加载

### 验证后端
- [ ] API接口可访问
- [ ] 数据库连接正常
- [ ] 跨域配置正确
- [ ] 日志无错误

### 验证数据库
- [ ] 所有表已创建
- [ ] 测试数据已导入
- [ ] 索引已创建

---

## 📞 技术支持

如有部署问题，请检查：
1. 所有配置文件是否正确
2. 数据库连接信息是否正确
3. 端口是否被占用
4. 防火墙设置

---

**部署日期**: 2026-03-12
**版本**: v1.0.0

# 数据库部署指南

## MySQL数据库配置

### 1. 安装MySQL

**Ubuntu/Debian:**
```bash
sudo apt update
sudo apt install mysql-server
sudo systemctl start mysql
sudo systemctl enable mysql
```

**CentOS/RHEL:**
```bash
sudo yum install mysql-server
sudo systemctl start mysqld
sudo systemctl enable mysqld
```

### 2. 创建数据库

```bash
# 登录MySQL
mysql -u root -p

# 执行初始化脚本
source /root/.openclaw/workspace/projects/ruoshan-law/database/init.sql
```

或者命令行执行：
```bash
mysql -u root -p < /root/.openclaw/workspace/projects/ruoshan-law/database/init.sql
```

### 3. 创建应用用户（推荐）

```sql
-- 创建专用用户
CREATE USER 'ruoshan_app'@'localhost' IDENTIFIED BY 'your_secure_password';

-- 授权
GRANT ALL PRIVILEGES ON ruoshan_law.* TO 'ruoshan_app'@'localhost';

-- 刷新权限
FLUSH PRIVILEGES;
```

### 4. 更新后端配置

修改 `java-backend/src/main/resources/application.properties`：

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/ruoshan_law?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
spring.datasource.username=ruoshan_app
spring.datasource.password=your_secure_password
```

### 5. 数据库表结构说明

| 表名 | 说明 | 主要字段 |
|------|------|---------|
| lawyers | 律师信息表 | id, name, title, introduction, specialty |
| cases | 案例表 | id, title, type, summary, result, lawyer |
| news | 新闻表 | id, title, category, summary, content |
| careers | 招聘表 | id, title, location, experience, salary |
| contacts | 联系记录表 | id, name, phone, type, content, status |

### 6. 测试数据

初始化脚本已包含测试数据：
- 6位律师信息
- 6个成功案例
- 6条新闻资讯
- 3个招聘岗位

### 7. 备份与恢复

**备份数据库：**
```bash
mysqldump -u root -p ruoshan_law > ruoshan_law_backup.sql
```

**恢复数据库：**
```bash
mysql -u root -p ruoshan_law < ruoshan_law_backup.sql
```

---

## 部署步骤总结

1. **安装MySQL**
2. **执行init.sql创建数据库和表**
3. **创建应用用户**
4. **更新后端数据库配置**
5. **启动Java后端服务**
6. **部署Vue前端**

---

**数据库文件位置：**
`/root/.openclaw/workspace/projects/ruoshan-law/database/init.sql`

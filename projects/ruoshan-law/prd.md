# 若山律师事务所官网 - 完整项目需求文档 (PRD v2.0)

**项目名称**: 若山律师事务所官方网站  
**文档版本**: v2.0  
**创建日期**: 2026-03-12  
**负责人**: 云捌 (项目与产品负责人)  
**技术栈**: Vue3 + Java Spring Boot + MySQL  

---

## 1. 项目概述

### 1.1 项目背景
若山律师事务所是一家专业的法律服务机构，需要重新开发官方网站以建立现代化的线上品牌形象，提升客户转化率，并建立高效的内容管理机制。

### 1.2 项目目标
1. **品牌升级**: 建立现代化、专业的线上品牌形象
2. **客户获取**: 优化用户体验，提升潜在客户咨询转化率
3. **内容管理**: 建立高效的内容更新和管理机制
4. **多端适配**: 确保在PC、平板、手机等各端都有优秀的用户体验
5. **运营效率**: 通过后台CMS系统提升内容运营效率

### 1.3 核心价值主张
- **专业可信**: 体现律师事务所的专业性和权威性
- **便捷高效**: 让客户能够快速找到所需信息和联系方式
- **现代美观**: 采用现代化设计，符合当代审美标准
- **移动优先**: 移动端用户体验优先，满足用户主要访问需求

---

## 2. 完整项目分析

### 2.1 前台界面及操作动效分析

#### 2.1.1 前台界面架构

**核心页面结构**:
```
若山律师事务所官网前台
├── 首页 (Home)
│   ├── Hero区域 (品牌展示 + CTA)
│   ├── 服务领域预览
│   ├── 团队风采展示
│   ├── 成功案例精选
│   ├── 客户评价轮播
│   └── 新闻动态
├── 关于我们 (About)
│   ├── 律所简介
│   ├── 发展历程
│   ├── 资质荣誉
│   └── 企业文化
├── 服务领域 (Services)
│   ├── 法律服务分类
│   ├── 服务详情页
│   └── 服务流程展示
├── 专业团队 (Team)
│   ├── 合伙人团队
│   ├── 资深律师
│   ├── 律师筛选功能
│   └── 律师详情页
├── 成功案例 (Cases)
│   ├── 案例分类浏览
│   ├── 案例筛选器
│   ├── 案例列表页
│   └── 案例详情页
├── 新闻资讯 (News)
│   ├── 新闻分类
│   ├── 新闻列表
│   ├── 新闻详情
│   └── 搜索功能
├── 招贤纳士 (Careers)
│   ├── 招聘职位
│   ├── 职位详情
│   ├── 投递表单
│   └── 招聘流程
└── 联系我们 (Contact)
    ├── 联系方式
    ├── 在线咨询表单
    ├── 地图展示
    └── 常见问题
```

#### 2.1.2 响应式设计策略

**断点设计**:
```css
/* 移动端优先设计 */
- 手机: < 768px (单列布局，触摸优化)
- 平板: 768px - 1024px (双列布局)
- 桌面: > 1024px (多列布局)
```

**布局适配原则**:
1. **移动端优先**: 从小屏幕开始设计，逐步增强
2. **弹性布局**: 使用Flexbox和Grid实现自适应
3. **图片响应**: 使用srcset和picture标签
4. **字体响应**: 使用rem单位和媒体查询

#### 2.1.3 交互动效设计

**滚动动效系统**:
- **触发机制**: Intersection Observer API
- **动效类型**: 
  - 滚入淡入 (fadeInUp)
  - 缩放显现 (scaleIn)
  - 滑动进入 (slideInLeft/Right)
  - 横向滚动 (horizontalScroll)

**具体动效实现**:
```javascript
// 滚动动效配置
const scrollAnimations = {
  threshold: 0.1,
  rootMargin: '0px 0px -100px 0px',
  animationDuration: '0.8s',
  easing: 'cubic-bezier(0.4, 0, 0.2, 1)'
}

// GSAP ScrollTrigger配置
gsap.registerPlugin(ScrollTrigger);

// 卡片滚动动效
gsap.utils.toArray('.service-card').forEach((card, index) => {
  gsap.from(card, {
    scrollTrigger: {
      trigger: card,
      start: 'top 80%',
      end: 'bottom 20%',
      toggleActions: 'play none none reverse'
    },
    y: 50,
    opacity: 0,
    duration: 0.8,
    delay: index * 0.1,
    ease: 'power2.out'
  });
});
```

**hover动效系统**:
- **按钮动效**: 颜色变化 + 微小位移 + 阴影变化
- **卡片动效**: 上浮效果 + 阴影加深 + 缩放
- **图片动效**: 悬浮放大 + 滤镜变化
- **文字动效**: 颜色渐变 + 下划线动画

#### 2.1.4 性能优化策略

**前端性能优化**:
1. **代码分割**: 使用Vue Router懒加载
2. **图片优化**: WebP格式 + 懒加载 + 响应式图片
3. **缓存策略**: Service Worker + HTTP缓存
4. **CDN加速**: 静态资源CDN分发
5. **Tree Shaking**: 移除未使用的代码

**加载性能优化**:
```javascript
// 图片懒加载
const lazyLoadImages = () => {
  const images = document.querySelectorAll('img[data-src]');
  const imageObserver = new IntersectionObserver((entries, observer) => {
    entries.forEach(entry => {
      if (entry.isIntersecting) {
        const img = entry.target;
        img.src = img.dataset.src;
        img.classList.remove('lazy');
        imageObserver.unobserve(img);
      }
    });
  });

  images.forEach(img => imageObserver.observe(img));
};

// 预加载关键资源
const preloadCriticalResources = () => {
  const criticalResources = [
    '/css/styles.css',
    '/js/main.js'
  ];
  
  criticalResources.forEach(url => {
    const link = document.createElement('link');
    link.rel = 'preload';
    link.href = url;
    link.as = url.includes('.css') ? 'style' : 'script';
    document.head.appendChild(link);
  });
};
```

#### 2.1.5 用户体验优化

**无障碍设计**:
- ARIA标签支持
- 键盘导航友好
- 屏幕阅读器兼容
- 色彩对比度符合WCAG标准

**用户交互优化**:
- 触摸手势支持 (滑动、缩放)
- 加载状态提示
- 错误友好提示
- 表单验证实时反馈

---

### 2.2 后台管理系统界面及功能分析

#### 2.2.1 CMS系统架构

**后台管理系统设计**:
```
若山律师事务所后台管理系统
├── 仪表板 (Dashboard)
│   ├── 数据概览
│   ├── 快捷操作
│   └── 系统通知
├── 内容管理
│   ├── 律师管理
│   ├── 案例管理
│   ├── 新闻管理
│   ├── 招聘管理
│   └── 页面内容管理
├── 用户管理
│   ├── 管理员账户
│   ├── 权限管理
│   └── 操作日志
├── 系统设置
│   ├── 网站配置
│   ├── SEO设置
│   ├── 联系方式设置
│   └── 备份恢复
└── 数据统计
    ├── 访问统计
    ├── 咨询统计
    └── 内容统计
```

#### 2.2.2 核心功能模块

**1. 仪表板模块**
```javascript
// 仪表板数据结构
const dashboardData = {
  overview: {
    totalViews: 15420,
    totalContacts: 156,
    activeCases: 23,
    recentNews: 5
  },
  charts: {
    visitsByDay: [], // 7天访问量
    contactsByType: [], // 咨询类型分布
    popularPages: [] // 热门页面
  },
  recentActivities: [
    { type: 'news', title: '发布新文章', time: '2小时前' },
    { type: 'contact', title: '新客户咨询', time: '4小时前' },
    { type: 'case', title: '更新案例', time: '6小时前' }
  ]
};
```

**2. 内容管理模块**

**律师管理功能**:
- 律师信息增删改查
- 律师照片上传和管理
- 专业标签管理
- 排序和展示控制
- 批量操作支持

**案例管理功能**:
- 案例信息管理
- 案例分类管理
- 案例图片上传
- 案例详情编辑
- 案例状态管理

**新闻管理功能**:
- 新闻发布和编辑
- 新闻分类管理
- 新闻图片上传
- 发布时间设置
- 新闻置顶功能

**3. 用户权限管理**
```javascript
// 权限体系设计
const permissionSystem = {
  roles: {
    SUPER_ADMIN: ['*'], // 超级管理员
    CONTENT_ADMIN: ['content:*'], // 内容管理员
    LAWYER_ADMIN: ['lawyer:*'], // 律师管理员
    VIEWER: ['read:*'] // 只读用户
  },
  permissions: {
    'content:news': ['create', 'read', 'update', 'delete'],
    'content:cases': ['create', 'read', 'update', 'delete'],
    'content:lawyers': ['create', 'read', 'update', 'delete'],
    'system:settings': ['read', 'update']
  }
};
```

#### 2.2.3 界面设计规范

**后台设计风格**:
- **设计语言**: 现代化扁平设计
- **色彩方案**: 
  - 主色调: #2c3e50 (深蓝灰)
  - 辅助色: #3498db (蓝色)
  - 成功色: #27ae60 (绿色)
  - 警告色: #f39c12 (橙色)
  - 错误色: #e74c3c (红色)
- **字体**: Inter + 思源黑体
- **布局**: 侧边栏 + 主内容区

**组件设计规范**:
```css
/* 后台按钮样式 */
.btn {
  padding: 8px 16px;
  border-radius: 6px;
  font-weight: 500;
  transition: all 0.2s ease;
}

.btn-primary {
  background: #3498db;
  color: white;
}

.btn-success {
  background: #27ae60;
  color: white;
}

/* 表格样式 */
.table {
  border-collapse: collapse;
  width: 100%;
}

.table th,
.table td {
  padding: 12px;
  border-bottom: 1px solid #ecf0f1;
  text-align: left;
}

/* 表单样式 */
.form-group {
  margin-bottom: 20px;
}

.form-label {
  display: block;
  margin-bottom: 6px;
  font-weight: 500;
}

.form-input {
  width: 100%;
  padding: 10px 12px;
  border: 1px solid #ddd;
  border-radius: 6px;
  font-size: 14px;
}
```

#### 2.2.4 交互功能设计

**富文本编辑器集成**:
- 使用 TinyMCE 或 Quill.js
- 支持图片上传
- 支持视频嵌入
- 支持代码块
- 支持表格

**文件上传功能**:
- 拖拽上传支持
- 多文件同时上传
- 上传进度显示
- 图片压缩和裁剪
- 文件格式验证

**数据导入导出**:
- Excel格式导入导出
- CSV格式支持
- 批量数据操作
- 数据模板下载

---

### 2.3 数据库设计分析

#### 2.3.1 数据库架构设计

**数据库设计原则**:
1. **规范化设计**: 避免数据冗余，确保数据一致性
2. **性能优化**: 合理的索引设计，优化查询性能
3. **扩展性**: 预留扩展字段，支持未来功能扩展
4. **安全性**: 敏感数据加密，访问权限控制

**完整数据库架构**:
```sql
-- 数据库创建
CREATE DATABASE IF NOT EXISTS ruoshan_law 
CHARACTER SET utf8mb4 
COLLATE utf8mb4_unicode_ci;

USE ruoshan_law;

-- 核心业务表
-- 1. 用户认证表
CREATE TABLE admin_users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    email VARCHAR(100) UNIQUE,
    full_name VARCHAR(100),
    role ENUM('SUPER_ADMIN', 'CONTENT_ADMIN', 'LAWYER_ADMIN', 'VIEWER') DEFAULT 'VIEWER',
    status ENUM('ACTIVE', 'INACTIVE') DEFAULT 'ACTIVE',
    last_login TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 2. 律师表（增强版）
CREATE TABLE lawyers (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    title VARCHAR(100) NOT NULL,
    introduction TEXT,
    bio TEXT,
    photo_url VARCHAR(500),
    avatar_url VARCHAR(500),
    specialties JSON, -- 专业领域数组
    education JSON, -- 教育背景
    experience JSON, -- 工作经验
    achievements JSON, -- 荣誉成就
    email VARCHAR(100),
    phone VARCHAR(20),
    wechat VARCHAR(50),
    order_index INT DEFAULT 0, -- 展示顺序
    is_featured BOOLEAN DEFAULT FALSE, -- 是否推荐
    status ENUM('ACTIVE', 'INACTIVE') DEFAULT 'ACTIVE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_name (name),
    INDEX idx_specialties ((CAST(specialties AS CHAR(255)))),
    INDEX idx_order (order_index)
);

-- 3. 案例表（增强版）
CREATE TABLE cases (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    slug VARCHAR(200) UNIQUE, -- URL友好标识
    type VARCHAR(50) NOT NULL,
    category VARCHAR(50),
    summary TEXT,
    content TEXT,
    result TEXT,
    lawyer_id BIGINT, -- 关联律师ID
    image_url VARCHAR(500),
    gallery JSON, -- 图片库
    tags JSON, -- 标签
    view_count INT DEFAULT 0,
    is_featured BOOLEAN DEFAULT FALSE,
    publish_date DATE,
    status ENUM('DRAFT', 'PUBLISHED', 'ARCHIVED') DEFAULT 'DRAFT',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (lawyer_id) REFERENCES lawyers(id),
    INDEX idx_title (title),
    INDEX idx_type (type),
    INDEX idx_category (category),
    INDEX idx_publish_date (publish_date),
    INDEX idx_featured (is_featured)
);

-- 4. 新闻表（增强版）
CREATE TABLE news (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    slug VARCHAR(200) UNIQUE,
    category VARCHAR(50) NOT NULL,
    summary TEXT,
    content TEXT,
    excerpt TEXT, -- 摘要
    image_url VARCHAR(500),
    gallery JSON, -- 图片库
    author VARCHAR(100),
    view_count INT DEFAULT 0,
    is_featured BOOLEAN DEFAULT FALSE,
    is_sticky BOOLEAN DEFAULT FALSE, -- 是否置顶
    publish_date DATETIME,
    meta_title VARCHAR(200), -- SEO标题
    meta_description TEXT, -- SEO描述
    meta_keywords TEXT, -- SEO关键词
    status ENUM('DRAFT', 'PUBLISHED', 'ARCHIVED') DEFAULT 'DRAFT',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_title (title),
    INDEX idx_category (category),
    INDEX idx_publish_date (publish_date),
    INDEX idx_featured (is_featured),
    INDEX idx_sticky (is_sticky),
    FULLTEXT INDEX ft_content (title, content, excerpt)
);

-- 5. 招聘表（增强版）
CREATE TABLE careers (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(100) NOT NULL,
    slug VARCHAR(100) UNIQUE,
    location VARCHAR(100) NOT NULL,
    department VARCHAR(100),
    experience VARCHAR(50),
    education VARCHAR(50),
    salary VARCHAR(100),
    requirements TEXT,
    responsibilities TEXT,
    benefits TEXT, -- 福利待遇
    application_email VARCHAR(100),
    application_url VARCHAR(500),
    contact_person VARCHAR(100),
    contact_phone VARCHAR(20),
    deadline DATE, -- 截止日期
    is_urgent BOOLEAN DEFAULT FALSE, -- 是否急聘
    order_index INT DEFAULT 0,
    status ENUM('ACTIVE', 'CLOSED', 'EXPIRED') DEFAULT 'ACTIVE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_title (title),
    INDEX idx_location (location),
    INDEX idx_deadline (deadline),
    INDEX idx_status (status)
);

-- 6. 联系表（增强版）
CREATE TABLE contacts (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    phone VARCHAR(20) NOT NULL,
    email VARCHAR(100),
    type VARCHAR(50) NOT NULL, -- CONSULTATION, COOPERATION, CAREER, OTHER
    service_type VARCHAR(100), -- 咨询服务类型
    content TEXT NOT NULL,
    status ENUM('NEW', 'CONTACTED', 'PROCESSING', 'COMPLETED', 'CLOSED') DEFAULT 'NEW',
    priority ENUM('LOW', 'MEDIUM', 'HIGH') DEFAULT 'MEDIUM',
    assigned_to VARCHAR(100), -- 分配给谁
    follow_up_date DATE, -- 跟进日期
    feedback TEXT, -- 反馈记录
    source VARCHAR(50), -- 来源: website, phone, referral
    ip_address VARCHAR(45),
    user_agent TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_status (status),
    INDEX idx_type (type),
    INDEX idx_priority (priority),
    INDEX idx_created_at (created_at)
);

-- 7. 页面配置表
CREATE TABLE page_configs (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    page_name VARCHAR(50) UNIQUE NOT NULL,
    page_title VARCHAR(200),
    meta_description TEXT,
    meta_keywords TEXT,
    hero_title VARCHAR(200),
    hero_subtitle TEXT,
    hero_image_url VARCHAR(500),
    hero_cta_text VARCHAR(100),
    hero_cta_url VARCHAR(200),
    content JSON, -- 页面内容配置
    status ENUM('ACTIVE', 'INACTIVE') DEFAULT 'ACTIVE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 8. 系统设置表
CREATE TABLE system_settings (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    setting_key VARCHAR(100) UNIQUE NOT NULL,
    setting_value TEXT,
    setting_type ENUM('STRING', 'NUMBER', 'BOOLEAN', 'JSON') DEFAULT 'STRING',
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 9. 操作日志表
CREATE TABLE operation_logs (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT,
    action VARCHAR(100) NOT NULL,
    resource_type VARCHAR(50),
    resource_id BIGINT,
    details TEXT,
    ip_address VARCHAR(45),
    user_agent TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES admin_users(id),
    INDEX idx_user_id (user_id),
    INDEX idx_action (action),
    INDEX idx_created_at (created_at)
);

-- 10. 访问统计表
CREATE TABLE visit_stats (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    page_url VARCHAR(500) NOT NULL,
    page_title VARCHAR(200),
    ip_address VARCHAR(45),
    user_agent TEXT,
    referer VARCHAR(500),
    country VARCHAR(50),
    city VARCHAR(50),
    device_type ENUM('DESKTOP', 'MOBILE', 'TABLET'),
    browser VARCHAR(50),
    os VARCHAR(50),
    visit_date DATE,
    visit_hour TINYINT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_page_url (page_url),
    INDEX idx_visit_date (visit_date),
    INDEX idx_device_type (device_type)
);
```

#### 2.3.2 数据库优化策略

**索引优化**:
- 主键索引: 所有表的主键自动创建
- 外键索引: 外键字段自动创建索引
- 复合索引: 针对常用查询条件创建复合索引
- 全文索引: 新闻和案例内容搜索使用全文索引

**查询优化**:
```sql
-- 常用查询优化
-- 1. 获取推荐律师
SELECT * FROM lawyers 
WHERE status = 'ACTIVE' AND is_featured = TRUE
ORDER BY order_index ASC, created_at DESC
LIMIT 6;

-- 2. 获取最新案例
SELECT c.*, l.name as lawyer_name 
FROM cases c 
LEFT JOIN lawyers l ON c.lawyer_id = l.id
WHERE c.status = 'PUBLISHED' AND c.is_featured = TRUE
ORDER BY c.publish_date DESC
LIMIT 3;

-- 3. 获取热门新闻
SELECT * FROM news 
WHERE status = 'PUBLISHED' 
ORDER BY is_sticky DESC, publish_date DESC
LIMIT 6;
```

**数据安全策略**:
1. **密码加密**: 使用BCrypt加密存储密码
2. **SQL注入防护**: 使用参数化查询
3. **数据备份**: 定期自动备份
4. **访问控制**: 数据库用户权限最小化

---

### 2.4 API接口设计分析

#### 2.4.1 API架构设计

**API设计原则**:
1. **RESTful风格**: 遵循REST API设计规范
2. **统一响应**: 标准化的响应格式
3. **版本控制**: API版本管理
4. **权限控制**: 基于角色的访问控制
5. **数据验证**: 严格的输入数据验证

**API基础架构**:
```javascript
// API基础配置
const apiConfig = {
  baseUrl: '/api/v1',
  timeout: 30000,
  headers: {
    'Content-Type': 'application/json',
    'Accept': 'application/json'
  }
};

// 统一响应格式
const standardResponse = {
  success: true, // 是否成功
  data: null, // 响应数据
  message: '', // 响应消息
  error: null, // 错误信息
  timestamp: new Date().toISOString(), // 时间戳
  path: '', // 请求路径
  duration: 0 // 响应时间
};
```

#### 2.4.2 前台API接口设计

**1. 首页相关接口**
```javascript
// 获取首页配置
GET /api/v1/home/config
Response: {
  data: {
    hero: {
      title: "若山律师事务所",
      subtitle: "专业法律服务，值得信赖的合作伙伴",
      ctaText: "立即咨询",
      ctaUrl: "/contact"
    },
    featuredServices: [...],
    featuredLawyers: [...],
    featuredCases: [...],
    testimonials: [...]
  }
}

// 获取统计数据
GET /api/v1/home/stats
Response: {
  data: {
    totalCases: 156,
    totalLawyers: 12,
    yearsExperience: 20,
    satisfactionRate: 98
  }
}
```

**2. 律师相关接口**
```javascript
// 获取律师列表
GET /api/v1/lawyers
Query: {
  page: 1,
  limit: 12,
  specialty: null,
  search: null
}
Response: {
  data: {
    lawyers: [...],
    pagination: {
      total: 12,
      page: 1,
      limit: 12,
      totalPages: 1
    }
  }
}

// 获取律师详情
GET /api/v1/lawyers/{id}
Response: {
  data: {
    id: 1,
    name: "张律师",
    title: "高级合伙人",
    bio: "20年执业经验...",
    specialties: ["民商事诉讼", "合同纠纷"],
    education: [...],
    experience: [...],
    achievements: [...],
    contactInfo: {
      email: "zhang@ruoshan.com",
      phone: "13800138001"
    }
  }
}
```

**3. 案例相关接口**
```javascript
// 获取案例列表
GET /api/v1/cases
Query: {
  page: 1,
  limit: 9,
  type: null,
  category: null,
  search: null
}
Response: {
  data: {
    cases: [...],
    pagination: {...}
  }
}

// 获取案例详情
GET /api/v1/cases/{id}
Response: {
  data: {
    id: 1,
    title: "某上市公司股权纠纷案",
    type: "公司法务",
    category: "商事诉讼",
    content: "案例详情内容...",
    result: "和解成功，为客户挽回损失3000万元",
    lawyer: {
      id: 1,
      name: "张律师",
      title: "高级合伙人"
    },
    gallery: [...],
    tags: ["股权纠纷", "上市公司", "和解"]
  }
}
```

**4. 新闻相关接口**
```javascript
// 获取新闻列表
GET /api/v1/news
Query: {
  page: 1,
  limit: 10,
  category: null,
  search: null
}
Response: {
  data: {
    news: [...],
    pagination: {...}
  }
}

// 获取新闻详情
GET /api/v1/news/{id}
Response: {
  data: {
    id: 1,
    title: "新《公司法》解读：企业需要注意的十大变化",
    category: "法律解读",
    content: "文章内容...",
    author: "法律编辑部",
    publishDate: "2024-03-10",
    viewCount: 1250,
    relatedNews: [...]
  }
}
```

**5. 联系表单接口**
```javascript
// 提交联系表单
POST /api/v1/contacts
Body: {
  name: "张三",
  phone: "13800138000",
  email: "zhangsan@example.com",
  type: "CONSULTATION",
  serviceType: "民商事诉讼",
  content: "我想咨询一个合同纠纷问题..."
}
Response: {
  data: {
    id: 1,
    message: "提交成功，我们会尽快与您联系"
  }
}
```

#### 2.4.3 后台管理API接口设计

**1. 认证相关接口**
```javascript
// 管理员登录
POST /api/v1/admin/login
Body: {
  username: "admin",
  password: "password123"
}
Response: {
  data: {
    token: "eyJhbGciOiJIUzI1NiIs...",
    user: {
      id: 1,
      username: "admin",
      fullName: "系统管理员",
      role: "SUPER_ADMIN"
    },
    expiresIn: 7200
  }
}

// 获取用户信息
GET /api/v1/admin/profile
Headers: {
  Authorization: "Bearer token"
}
Response: {
  data: {
    user: {...},
    permissions: ["*"]
  }
}
```

**2. 内容管理接口**

**律师管理**:
```javascript
// 获取律师列表（后台）
GET /api/v1/admin/lawyers
Query: {
  page: 1,
  limit: 20,
  search: null,
  status: null
}
Response: {
  data: {
    lawyers: [...],
    pagination: {...}
  }
}

// 创建律师
POST /api/v1/admin/lawyers
Headers: { Authorization: "Bearer token" }
Body: {
  name: "新律师",
  title: "合伙人",
  introduction: "简介...",
  specialties: ["知识产权", "商标专利"],
  orderIndex: 0,
  isFeatured: false
}
Response: {
  data: {
    id: 13,
    message: "创建成功"
  }
}

// 更新律师
PUT /api/v1/admin/lawyers/{id}
Headers: { Authorization: "Bearer token" }
Body: { ...更新数据... }

// 删除律师
DELETE /api/v1/admin/lawyers/{id}
Headers: { Authorization: "Bearer token" }
```

**案例管理**:
```javascript
// 获取案例列表（后台）
GET /api/v1/admin/cases
Query: {
  page: 1,
  limit: 20,
  status: null,
  type: null
}

// 创建案例
POST /api/v1/admin/cases
Headers: { Authorization: "Bearer token" }
Body: {
  title: "新案例",
  type: "公司法务",
  category: "商事诉讼",
  content: "案例内容...",
  result: "处理结果...",
  lawyerId: 1,
  isFeatured: true,
  publishDate: "2024-03-15"
}
```

**新闻管理**:
```javascript
// 获取新闻列表（后台）
GET /api/v1/admin/news
Query: {
  page: 1,
  limit: 20,
  status: null,
  category: null
}

// 创建新闻
POST /api/v1/admin/news
Headers: { Authorization: "Bearer token" }
Body: {
  title: "新新闻",
  category: "律所动态",
  content: "新闻内容...",
  excerpt: "新闻摘要...",
  metaTitle: "SEO标题",
  metaDescription: "SEO描述",
  publishDate: "2024-03-15T10:00:00Z"
}
```

**3. 系统管理接口**

**用户管理**:
```javascript
// 获取用户列表
GET /api/v1/admin/users
Headers: { Authorization: "Bearer token" }

// 创建用户
POST /api/v1/admin/users
Headers: { Authorization: "Bearer token" }
Body: {
  username: "newadmin",
  email: "admin@example.com",
  fullName: "新管理员",
  role: "CONTENT_ADMIN",
  password: "password123"
}

// 更新用户
PUT /api/v1/admin/users/{id}
Headers: { Authorization: "Bearer token" }

// 删除用户
DELETE /api/v1/admin/users/{id}
Headers: { Authorization: "Bearer token" }
```

**系统设置**:
```javascript
// 获取系统设置
GET /api/v1/admin/settings
Headers: { Authorization: "Bearer token" }
Response: {
  data: {
    siteName: "若山律师事务所",
    siteDescription: "专业法律服务...",
    contactInfo: {
      phone: "400-123-4567",
      email: "info@ruoshan.com",
      address: "北京市朝阳区..."
    },
    socialLinks: {
      wechat: "ruoshan_law",
      weibo: "@若山律师事务所"
    }
  }
}

// 更新系统设置
PUT /api/v1/admin/settings
Headers: { Authorization: "Bearer token" }
Body: {
  siteName: "新名称",
  contactInfo: { ... }
}
```

**4. 统计分析接口**

```javascript
// 获取访问统计
GET /api/v1/admin/analytics/visits
Query: {
  startDate: "2024-01-01",
  endDate: "2024-03-12",
  groupBy: "day"
}
Response: {
  data: {
    visits: [
      { date: "2024-03-12", count: 156 },
      { date: "2024-03-11", count: 203 }
    ],
    totalVisits: 15420,
    uniqueVisitors: 8950
  }
}

// 获取咨询统计
GET /api/v1/admin/analytics/contacts
Query: {
  startDate: "2024-01-01",
  endDate: "2024-03-12",
  status: null
}
Response: {
  data: {
    contacts: [
      { date: "2024-03-12", count: 8 },
      { date: "2024-03-11", count: 12 }
    ],
    totalContacts: 156,
    newContacts: 45,
    processedContacts: 89
  }
}
```

#### 2.4.4 API安全设计

**认证机制**:
```javascript
// JWT Token配置
const jwtConfig = {
  secret: 'your-secret-key',
  expiresIn: '2h',
  algorithm: 'HS256'
};

// 权限中间件
const requireAuth = (req, res, next) => {
  const token = req.headers.authorization?.replace('Bearer ', '');
  
  if (!token) {
    return res.status(401).json({
      success: false,
      message: '未提供认证令牌'
    });
  }
  
  try {
    const decoded = jwt.verify(token, jwtConfig.secret);
    req.user = decoded;
    next();
  } catch (error) {
    return res.status(401).json({
      success: false,
      message: '无效的认证令牌'
    });
  }
};

// 角色权限中间件
const requireRole = (roles) => {
  return (req, res, next) => {
    if (!req.user) {
      return res.status(401).json({
        success: false,
        message: '未认证'
      });
    }
    
    if (!roles.includes(req.user.role)) {
      return res.status(403).json({
        success: false,
        message: '权限不足'
      });
    }
    
    next();
  };
};
```

**数据验证**:
```javascript
// 请求体验证规则
const validationRules = {
  lawyer: {
    name: { required: true, minLength: 2, maxLength: 100 },
    title: { required: true, minLength: 2, maxLength: 100 },
    specialties: { required: true, type: 'array' },
    email: { required: false, type: 'email' }
  },
  contact: {
    name: { required: true, minLength: 2, maxLength: 100 },
    phone: { required: true, pattern: /^1[3-9]\d{9}$/ },
    type: { required: true, enum: ['CONSULTATION', 'COOPERATION', 'CAREER', 'OTHER'] },
    content: { required: true, minLength: 10, maxLength: 2000 }
  }
};
```

**API限流**:
```javascript
// Redis限流配置
const rateLimitConfig = {
  windowMs: 15 * 60 * 1000, // 15分钟
  max: 100, // 限制100次请求
  message: {
    success: false,
    message: '请求过于频繁，请稍后再试'
  }
};

// 登录限流
const loginRateLimit = {
  windowMs: 15 * 60 * 1000, // 15分钟
  max: 5, // 限制5次登录尝试
  skipSuccessfulRequests: true
};
```

---

## 3. 技术实现方案

### 3.1 前端技术栈详细配置

**核心框架**:
```javascript
// package.json 核心依赖
{
  "dependencies": {
    "vue": "^3.4.0",
    "vue-router": "^4.2.5",
    "pinia": "^2.1.7",
    "axios": "^1.6.0",
    "gsap": "^3.12.2",
    "vue-use": "^10.7.0",
    "@vueuse/core": "^10.7.0"
  },
  "devDependencies": {
    "@vitejs/plugin-vue": "^4.5.0",
    "vite": "^5.0.0",
    "tailwindcss": "^3.4.0",
    "autoprefixer": "^10.4.16",
    "postcss": "^8.4.32",
    "typescript": "^5.3.0",
    "@types/node": "^20.10.0"
  }
}
```

**项目结构**:
```
vue-frontend/
├── public/
│   ├── favicon.ico
│   └── images/
├── src/
│   ├── assets/
│   │   ├── css/
│   │   ├── images/
│   │   └── fonts/
│   ├── components/
│   │   ├── common/
│   │   │   ├── Header.vue
│   │   │   ├── Footer.vue
│   │   │   ├── Button.vue
│   │   │   └── Card.vue
│   │   ├── layout/
│   │   │   ├── TheHeader.vue
│   │   │   └── TheFooter.vue
│   │   └── ui/
│   │       ├── ServiceCard.vue
│   │       ├── LawyerCard.vue
│   │       └── CaseCard.vue
│   ├── views/
│   │   ├── Home.vue
│   │   ├── About.vue
│   │   ├── Services.vue
│   │   ├── Team.vue
│   │   ├── Cases.vue
│   │   ├── News.vue
│   │   ├── Careers.vue
│   │   └── Contact.vue
│   ├── stores/
│   │   ├── useAuth.js
│   │   ├── useContent.js
│   │   └── useUI.js
│   ├── composables/
│   │   ├── useScrollAnimation.js
│   │   ├── useImageLazy.js
│   │   └── useApi.js
│   ├── utils/
│   │   ├── api.js
│   │   ├── validators.js
│   │   └── helpers.js
│   ├── plugins/
│   │   ├── gsap.js
│   │   └── lazyload.js
│   ├── App.vue
│   └── main.js
├── tailwind.config.js
├── vite.config.js
└── index.html
```

### 3.2 后端技术栈详细配置

**核心依赖**:
```xml
<!-- pom.xml 核心依赖 -->
<dependencies>
    <!-- Spring Boot Starters -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-security</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-validation</artifactId>
    </dependency>
    
    <!-- Database -->
    <dependency>
        <groupId>mysql</groupId>
        <artifactId>mysql-connector-java</artifactId>
        <version>8.0.33</version>
    </dependency>
    <dependency>
        <groupId>com.alibaba</groupId>
        <artifactId>druid-spring-boot-starter</artifactId>
        <version>1.2.20</version>
    </dependency>
    
    <!-- JWT -->
    <dependency>
        <groupId>io.jsonwebtoken</groupId>
        <artifactId>jjwt-api</artifactId>
        <version>0.12.3</version>
    </dependency>
    <dependency>
        <groupId>io.jsonwebtoken</groupId>
        <artifactId>jjwt-impl</artifactId>
        <version>0.12.3</version>
        <scope>runtime</scope>
    </dependency>
    <dependency>
        <groupId>io.jsonwebtoken</groupId>
        <artifactId>jjwt-jackson</artifactId>
        <version>0.12.3</version>
        <scope>runtime</scope>
    </dependency>
    
    <!-- File Upload -->
    <dependency>
        <groupId>commons-fileupload</groupId>
        <artifactId>commons-fileupload</artifactId>
        <version>1.5</version>
    </dependency>
    
    <!-- Redis -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-redis</artifactId>
    </dependency>
    
    <!-- Tools -->
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
        <optional>true</optional>
    </dependency>
    <dependency>
        <groupId>com.fasterxml.jackson.core</groupId>
        <artifactId>jackson-databind</artifactId>
    </dependency>
</dependencies>
```

**项目结构**:
```
java-backend/
├── src/
│   ├── main/
│   │   ├── java/com/ruoshan/
│   │   │   ├── RuoshanLawApplication.java
│   │   │   ├── config/
│   │   │   │   ├── SecurityConfig.java
│   │   │   │   ├── CorsConfig.java
│   │   │   │   ├── JpaConfig.java
│   │   │   │   └── RedisConfig.java
│   │   │   ├── controller/
│   │   │   │   ├── HomeController.java
│   │   │   │   ├── LawyerController.java
│   │   │   │   ├── CaseController.java
│   │   │   │   ├── NewsController.java
│   │   │   │   ├── ContactController.java
│   │   │   │   ├── CareerController.java
│   │   │   │   ├── AdminAuthController.java
│   │   │   │   ├── AdminUserController.java
│   │   │   │   ├── AdminContentController.java
│   │   │   │   └── AdminSystemController.java
│   │   │   ├── service/
│   │   │   │   ├── LawyerService.java
│   │   │   │   ├── CaseService.java
│   │   │   │   ├── NewsService.java
│   │   │   │   ├── ContactService.java
│   │   │   │   ├── CareerService.java
│   │   │   │   ├── AdminAuthService.java
│   │   │   │   ├── AdminUserService.java
│   │   │   │   ├── AdminContentService.java
│   │   │   │   └── AdminSystemService.java
│   │   │   ├── repository/
│   │   │   │   ├── LawyerRepository.java
│   │   │   │   ├── CaseRepository.java
│   │   │   │   ├── NewsRepository.java
│   │   │   │   ├── ContactRepository.java
│   │   │   │   ├── CareerRepository.java
│   │   │   │   ├── AdminUserRepository.java
│   │   │   │   └── SystemSettingRepository.java
│   │   │   ├── entity/
│   │   │   │   ├── Lawyer.java
│   │   │   │   ├── Case.java
│   │   │   │   ├── News.java
│   │   │   │   ├── Contact.java
│   │   │   │   ├── Career.java
│   │   │   │   ├── AdminUser.java
│   │   │   │   └── SystemSetting.java
│   │   │   ├── dto/
│   │   │   │   ├── request/
│   │   │   │   │   ├── LoginRequest.java
│   │   │   │   │   ├── LawyerRequest.java
│   │   │   │   │   ├── CaseRequest.java
│   │   │   │   │   └── NewsRequest.java
│   │   │   │   └── response/
│   │   │   │       ├── LoginResponse.java
│   │   │   │       ├── ApiResponse.java
│   │   │   │       └── DashboardResponse.java
│   │   │   ├── exception/
│   │   │   │   ├── GlobalExceptionHandler.java
│   │   │   │   ├── ResourceNotFoundException.java
│   │   │   │   └── UnauthorizedException.java
│   │   │   └── util/
│   │   │       ├── JwtUtil.java
│   │   │       ├── FileUploadUtil.java
│   │   │       └── DateUtil.java
│   │   └── resources/
│   │       ├── application.properties
│   │       ├── application-dev.properties
│   │       ├── application-prod.properties
│   │       ├── static/
│   │       └── templates/
│   └── test/
│       └── java/com/ruoshan/
└── pom.xml
```

---

## 4. 项目实施计划

### 4.1 开发阶段划分

**第一阶段：需求分析与设计 (第1-2周)**
- ✅ 需求文档完善 (已完成)
- ✅ UI设计完成 (已完成)
- 🔄 技术方案确认
- 🔄 数据库设计确认

**第二阶段：后端开发 (第3-5周)**
- 🔄 数据库搭建与初始化
- 🔄 后端API开发
- 🔄 管理系统开发
- 🔄 单元测试

**第三阶段：前端开发 (第4-6周)**
- 🔄 前端框架搭建
- 🔄 页面组件开发
- 🔄 交互动效实现
- 🔄 响应式适配

**第四阶段：系统集成 (第7周)**
- 🔄 前后端联调
- 🔄 功能测试
- 🔄 性能优化
- 🔄 安全测试

**第五阶段：部署上线 (第8周)**
- 🔄 生产环境部署
- 🔄 数据迁移
- 🔄 上线验证
- 🔄 监控配置

### 4.2 资源配置

**开发团队配置**:
- 项目经理 (云捌): 1人
- 前端开发 (云伍): 2人
- 后端开发 (云陆): 2人
- UI设计师 (云肆): 1人
- 测试工程师: 1人

**技术资源**:
- 开发环境: Docker + Docker Compose
- 代码管理: GitLab
- CI/CD: Jenkins
- 测试环境: 阿里云ECS
- 生产环境: 阿里云ECS + RDS

---

## 5. 质量保障

### 5.1 测试策略

**测试类型**:
1. **单元测试**: JUnit + Mockito
2. **集成测试**: Spring Boot Test
3. **端到端测试**: Cypress
4. **性能测试**: JMeter
5. **安全测试**: OWASP ZAP

**测试覆盖率要求**:
- 单元测试覆盖率 > 80%
- 集成测试覆盖率 > 70%
- 关键功能端到端测试 100%

### 5.2 性能指标

**前端性能**:
- Lighthouse评分 > 90
- 首屏加载时间 < 2.5s
- 移动端友好度 > 95
- 可访问性评分 > 90

**后端性能**:
- API响应时间 < 200ms
- 并发用户数 > 1000
- 系统可用性 > 99.9%
- 数据库查询效率优化

---

## 6. 风险管理

### 6.1 技术风险

**潜在风险**:
1. **性能瓶颈**: 大量图片和内容影响加载速度
2. **浏览器兼容性**: 新特性在旧浏览器不支持
3. **数据安全**: 客户信息泄露风险
4. **系统扩展性**: 未来功能扩展困难

**应对措施**:
1. **性能优化**: 图片压缩、CDN加速、代码分割
2. **兼容性处理**: Polyfill、渐进增强
3. **安全加固**: HTTPS、数据加密、权限控制
4. **架构设计**: 模块化设计、微服务预留

### 6.2 项目风险

**潜在风险**:
1. **需求变更**: 客户需求频繁变更
2. **资源不足**: 开发人员不足
3. **时间延误**: 开发进度延误
4. **质量风险**: 代码质量不达标

**应对措施**:
1. **需求管理**: 需求评审、变更控制流程
2. **资源规划**: 提前规划、备用人员
3. **进度管理**: 敏捷开发、每日站会
4. **质量控制**: 代码审查、自动化测试

---

## 7. 成功标准

### 7.1 业务指标

**核心业务指标**:
- 网站访问量月增长 > 30%
- 客户咨询转化率 > 5%
- 内容更新效率提升 > 50%
- 移动端访问占比 > 70%

**用户体验指标**:
- 页面跳出率 < 40%
- 平均访问时长 > 3分钟
- 用户满意度 > 90%
- 投诉率 < 1%

### 7.2 技术指标

**系统性能**:
- 系统响应时间 < 200ms
- 页面加载时间 < 2.5s
- 并发用户支持 > 1000
- 系统可用性 > 99.9%

**代码质量**:
- 代码覆盖率 > 80%
- Bug密度 < 1/千行代码
- 安全漏洞 = 0
- 代码重复率 < 5%

---

## 8. 总结

本PRD文档为若山律师事务所官网项目提供了完整的需求分析和实施方案。项目采用Vue3 + Java Spring Boot技术栈，具备以下核心特点：

### 8.1 核心优势
1. **现代化技术栈**: 采用最新的Vue3和Spring Boot技术
2. **完整的CMS系统**: 功能完善的后台管理系统
3. **优秀的用户体验**: 精美的界面设计和流畅的交互动效
4. **多端适配**: 完美支持PC、平板、手机等各种设备
5. **高性能**: 优化的性能和加载速度
6. **安全性完善**: 完善的权限控制和数据安全保护

### 8.2 项目价值
1. **品牌提升**: 建立现代化的线上品牌形象
2. **业务增长**: 提升客户咨询转化率
3. **运营效率**: 通过CMS系统提升内容运营效率
4. **技术先进**: 采用先进的技术架构，支持未来扩展

### 8.3 实施保障
1. **专业团队**: 配备经验丰富的开发团队
2. **科学管理**: 采用敏捷开发方法，确保项目质量
3. **风险控制**: 完善的风险管理机制
4. **质量保障**: 全面的测试和质量控制体系

本项目的成功实施将为若山律师事务所建立现代化的线上业务平台，显著提升品牌影响力和业务运营效率。

---

**文档结束**

*本PRD文档由项目与产品负责人 云捌 编写*
*完成时间: 2026-03-12*

---

**通知**: 项目需求分析已完成，请云肆进行UI设计确认。确认完成后将进入开发阶段。
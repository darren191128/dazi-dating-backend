# 用户认证API设计

## 1. API端点设计

- `POST /api/auth/register` - 用户注册
- `POST /api/auth/login` - 用户登录
- `POST /api/auth/logout` - 用户登出
- `GET /api/auth/profile` - 获取用户信息（需要认证）
- `POST /api/auth/refresh` - 刷新访问令牌

## 2. 请求/响应格式

### 注册请求
```json
{
  "email": "user@example.com",
  "password": "secure-password",
  "name": "用户名"
}
```

### 注册响应 (201 Created)
```json
{
  "success": true,
  "message": "用户注册成功",
  "user": {
    "id": "123",
    "email": "user@example.com",
    "name": "用户名"
  }
}
```

### 登录请求
```json
{
  "email": "user@example.com",
  "password": "secure-password"
}
```

### 登录响应 (200 OK)
```json
{
  "success": true,
  "message": "登录成功",
  "tokens": {
    "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "expiresIn": 3600
  },
  "user": {
    "id": "123",
    "email": "user@example.com",
    "name": "用户名"
  }
}
```

### 认证保护的Profile响应 (200 OK)
```json
{
  "success": true,
  "user": {
    "id": "123",
    "email": "user@example.com",
    "name": "用户名",
    "createdAt": "2026-03-12T11:34:00.000Z"
  }
}
```

## 3. 核心代码框架

### app.js
```javascript
const express = require('express');
const cors = require('cors');
const helmet = require('helmet');
const rateLimit = require('express-rate-limit');

const authRoutes = require('./routes/auth');

const app = express();

// 安全中间件
app.use(helmet());
app.use(cors({
  origin: process.env.FRONTEND_URL || 'http://localhost:3000',
  credentials: true
}));

// 解析中间件
app.use(express.json({ limit: '10mb' }));
app.use(express.urlencoded({ extended: true, limit: '10mb' }));

// 速率限制
const limiter = rateLimit({
  windowMs: 15 * 60 * 1000, // 15分钟
  max: 100 // 限制每个IP 100个请求
});
app.use('/api/', limiter);

// 路由
app.use('/api/auth', authRoutes);

// 健康检查
app.get('/health', (req, res) => {
  res.json({ status: 'OK', timestamp: new Date().toISOString() });
});

module.exports = app;
```

### routes/auth.js
```javascript
const express = require('express');
const router = express.Router();
const { register, login, logout, profile, refresh } = require('../controllers/auth');

// 公共路由
router.post('/register', register);
router.post('/login', login);

// 需要认证的路由
router.post('/logout', authenticate, logout);
router.get('/profile', authenticate, profile);
router.post('/refresh', refresh);

module.exports = router;
```

### controllers/auth.js
```javascript
const jwt = require('jsonwebtoken');
const bcrypt = require('bcryptjs');
const User = require('../models/user');

// JWT密钥
const JWT_SECRET = process.env.JWT_SECRET || 'your-secret-key';
const JWT_EXPIRE = 3600; // 1小时
const REFRESH_EXPIRE = 7 * 24 * 3600; // 7天

// 注册控制器
exports.register = async (req, res) => {
  try {
    const { email, password, name } = req.body;
    
    // 检查用户是否已存在
    const existingUser = await User.findOne({ email });
    if (existingUser) {
      return res.status(400).json({ 
        success: false, 
        message: '用户已存在' 
      });
    }
    
    // 创建新用户
    const user = await User.create({
      email,
      password,
      name
    });
    
    res.status(201).json({
      success: true,
      message: '用户注册成功',
      user: {
        id: user._id,
        email: user.email,
        name: user.name
      }
    });
    
  } catch (error) {
    res.status(500).json({ 
      success: false, 
      message: '服务器错误' 
    });
  }
};

// 登录控制器
exports.login = async (req, res) => {
  try {
    const { email, password } = req.body;
    
    // 查找用户
    const user = await User.findOne({ email }).select('+password');
    if (!user) {
      return res.status(401).json({ 
        success: false, 
        message: '邮箱或密码错误' 
      });
    }
    
    // 验证密码
    const isMatch = await user.comparePassword(password);
    if (!isMatch) {
      return res.status(401).json({ 
        success: false, 
        message: '邮箱或密码错误' 
      });
    }
    
    // 生成JWT令牌
    const accessToken = jwt.sign(
      { userId: user._id }, 
      JWT_SECRET, 
      { expiresIn: JWT_EXPIRE }
    );
    
    const refreshToken = jwt.sign(
      { userId: user._id }, 
      JWT_SECRET + 'refresh', 
      { expiresIn: REFRESH_EXPIRE }
    );
    
    res.json({
      success: true,
      message: '登录成功',
      tokens: {
        accessToken,
        refreshToken,
        expiresIn: JWT_EXPIRE
      },
      user: {
        id: user._id,
        email: user.email,
        name: user.name
      }
    });
    
  } catch (error) {
    res.status(500).json({ 
      success: false, 
      message: '服务器错误' 
    });
  }
};

// 认证中间件
exports.authenticate = (req, res, next) => {
  try {
    const token = req.headers.authorization?.split(' ')[1];
    
    if (!token) {
      return res.status(401).json({ 
        success: false, 
        message: '访问被拒绝，缺少令牌' 
      });
    }
    
    const decoded = jwt.verify(token, JWT_SECRET);
    req.user = decoded;
    next();
    
  } catch (error) {
    res.status(401).json({ 
      success: false, 
      message: '令牌无效或已过期' 
    });
  }
};

// 获取用户信息
exports.profile = (req, res) => {
  res.json({
    success: true,
    user: {
      id: req.user.userId,
      email: req.user.email,
      name: req.user.name,
      createdAt: new Date().toISOString()
    }
  });
};

// 刷新令牌
exports.refresh = async (req, res) => {
  try {
    const { refreshToken } = req.body;
    
    if (!refreshToken) {
      return res.status(400).json({ 
        success: false, 
        message: '缺少刷新令牌' 
      });
    }
    
    const decoded = jwt.verify(refreshToken, JWT_SECRET + 'refresh');
    
    // 生成新的访问令牌
    const accessToken = jwt.sign(
      { userId: decoded.userId }, 
      JWT_SECRET, 
      { expiresIn: JWT_EXPIRE }
    );
    
    res.json({
      success: true,
      tokens: {
        accessToken,
        expiresIn: JWT_EXPIRE
      }
    });
    
  } catch (error) {
    res.status(401).json({ 
      success: false, 
      message: '刷新令牌无效' 
    });
  }
};
```

### models/user.js
```javascript
const mongoose = require('mongoose');
const bcrypt = require('bcryptjs');

const userSchema = new mongoose.Schema({
  email: {
    type: String,
    required: [true, '邮箱是必需的'],
    unique: true,
    lowercase: true
  },
  password: {
    type: String,
    required: [true, '密码是必需的'],
    minlength: [6, '密码至少需要6个字符']
  },
  name: {
    type: String,
    required: [true, '姓名是必需的']
  },
  createdAt: {
    type: Date,
    default: Date.now
  }
});

// 密码加密中间件
userSchema.pre('save', async function(next) {
  if (!this.isModified('password')) return next();
  
  this.password = await bcrypt.hash(this.password, 12);
  next();
});

// 密码比较方法
userSchema.methods.comparePassword = async function(candidatePassword) {
  return await bcrypt.compare(candidatePassword, this.password);
};

module.exports = mongoose.model('User', userSchema);
```

## 技术栈说明
- **认证方式**: JWT (JSON Web Token)
- **密码哈希**: bcryptjs
- **数据库**: MongoDB (Mongoose ODM)
- **安全防护**: Helmet, CORS, Rate Limiting
- **环境变量**: 使用dotenv管理敏感配置

该设计满足基本的用户认证需求，并具有良好的可扩展性和安全性基础。
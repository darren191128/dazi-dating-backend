# 公司官网设计规范 Design System

## 1. 色彩系统 Color System

### 主色 Primary Colors
| 名称 | 色值 | 用途 |
|------|------|------|
| 主色-深 | `#0A1628` | 深色背景、导航、页脚 |
| 主色-中 | `#1E3A5F` | 次级深色背景、卡片 |
| 主色-亮 | `#2563EB` | 主按钮、链接、高亮 |
| 主色-渐变起点 | `#0A1628` | Hero背景渐变起点 |
| 主色-渐变终点 | `#1E3A5F` | Hero背景渐变终点 |

### 辅助色 Secondary Colors
| 名称 | 色值 | 用途 |
|------|------|------|
| 科技青 | `#06B6D4` | 装饰元素、数据高亮 |
| 科技紫 | `#8B5CF6` | 特殊强调、图标 |
| 成功绿 | `#10B981` | 成功状态、正向指标 |
| 警示橙 | `#F59E0B` | 警示、重要提示 |

### 中性色 Neutral Colors
| 名称 | 色值 | 用途 |
|------|------|------|
| 文字-主色 | `#1F2937` | 正文标题 |
| 文字-次色 | `#4B5563` | 正文内容 |
| 文字-辅助 | `#9CA3AF` | 辅助说明、占位符 |
| 文字-浅色 | `#FFFFFF` | 深色背景上的文字 |
| 文字-浅色-次 | `rgba(255,255,255,0.8)` | 深色背景上次要文字 |

### 背景色 Background Colors
| 名称 | 色值 | 用途 |
|------|------|------|
| 背景-主色 | `#FFFFFF` | 主页面背景 |
| 背景-次色 | `#F8FAFC` | 交替区块背景 |
| 背景-深色 | `#0A1628` | Hero区、CTA区、页脚 |
| 背景-卡片 | `#FFFFFF` | 卡片背景 |
| 背景-悬浮 | `#F1F5F9` | 悬浮状态背景 |

### 边框色 Border Colors
| 名称 | 色值 | 用途 |
|------|------|------|
| 边框-浅色 | `#E5E7EB` | 卡片边框、分割线 |
| 边框-深色 | `rgba(255,255,255,0.1)` | 深色背景边框 |
| 边框-高亮 | `#2563EB` | 聚焦状态边框 |

---

## 2. 字体规范 Typography

### 字体家族 Font Family
```css
--font-primary: 'Inter', -apple-system, BlinkMacSystemFont, 'Segoe UI', 'PingFang SC', 'Microsoft YaHei', sans-serif;
--font-display: 'Inter', 'SF Pro Display', -apple-system, sans-serif;
```

### 字号层级 Type Scale

| 层级 | 大小 | 字重 | 行高 | 字间距 | 用途 |
|------|------|------|------|--------|------|
| Display | 56px | 700 | 1.1 | -0.02em | Hero主标题 |
| H1 | 42px | 600 | 1.2 | -0.01em | 页面标题 |
| H2 | 36px | 600 | 1.3 | -0.01em | 区块标题 |
| H3 | 28px | 600 | 1.4 | 0 | 子区块标题 |
| H4 | 22px | 600 | 1.4 | 0 | 卡片标题 |
| H5 | 18px | 600 | 1.5 | 0 | 小标题 |
| Body Large | 18px | 400 | 1.7 | 0 | 大段正文 |
| Body | 16px | 400 | 1.7 | 0 | 标准正文 |
| Body Small | 14px | 400 | 1.6 | 0 | 辅助文字 |
| Caption | 12px | 500 | 1.5 | 0.02em | 标签、时间 |
| Overline | 12px | 600 | 1.4 | 0.1em | 小标签、分类 |

---

## 3. 间距规范 Spacing System

### 栅格系统 Grid System
- **容器最大宽度**: 1280px
- **容器内边距**: 80px (桌面) / 40px (平板) / 20px (移动)
- **栅格列数**: 12列
- **列间距**: 24px (桌面) / 16px (移动)

### 间距层级 Spacing Scale
| Token | 值 | 用途 |
|-------|-----|------|
| space-1 | 4px | 图标间距、紧凑内边距 |
| space-2 | 8px | 小间距、行内元素 |
| space-3 | 12px | 按钮内边距(垂直) |
| space-4 | 16px | 标准间距、卡片内边距 |
| space-5 | 20px | 中等间距 |
| space-6 | 24px | 组件间距、栅格间隙 |
| space-8 | 32px | 大间距、区块内边距 |
| space-10 | 40px | 大区块间距 |
| space-12 | 48px | 大组件间距 |
| space-16 | 64px | 区块间距 |
| space-20 | 80px | 大区块内边距 |
| space-24 | 96px | 超大区块间距 |

### 区块间距 Section Spacing
- **标准区块**: padding: 96px 0
- **紧凑区块**: padding: 64px 0
- **Hero区块**: padding: 120px 0
- **CTA区块**: padding: 100px 0

---

## 4. 圆角规范 Border Radius

| Token | 值 | 用途 |
|-------|-----|------|
| radius-sm | 4px | 小按钮、标签 |
| radius-md | 8px | 标准按钮、输入框、小卡片 |
| radius-lg | 12px | 大卡片、图片 |
| radius-xl | 16px | 大组件、模态框 |
| radius-2xl | 24px | 特殊大卡片 |
| radius-full | 9999px | 圆形按钮、头像、标签 |

---

## 5. 阴影规范 Shadows

| Token | 值 | 用途 |
|-------|-----|------|
| shadow-sm | 0 1px 2px rgba(0,0,0,0.05) | 轻微悬浮 |
| shadow-md | 0 4px 6px -1px rgba(0,0,0,0.1) | 卡片默认 |
| shadow-lg | 0 10px 15px -3px rgba(0,0,0,0.1) | 卡片悬浮 |
| shadow-xl | 0 20px 25px -5px rgba(0,0,0,0.1) | 模态框、下拉菜单 |
| shadow-glow | 0 0 20px rgba(37,99,235,0.3) | 主色发光效果 |

---

## 6. 组件规范 Components

### 按钮 Buttons

#### 主按钮 Primary Button
```css
background: #2563EB;
color: #FFFFFF;
padding: 14px 32px;
border-radius: 8px;
font-size: 16px;
font-weight: 500;
transition: all 0.2s ease;
/* Hover */
background: #1D4ED8;
transform: translateY(-1px);
box-shadow: 0 4px 12px rgba(37,99,235,0.3);
```

#### 次按钮 Secondary Button
```css
background: transparent;
color: #2563EB;
border: 1.5px solid #2563EB;
padding: 14px 32px;
border-radius: 8px;
/* Hover */
background: rgba(37,99,235,0.05);
```

#### 幽灵按钮 Ghost Button (深色背景)
```css
background: rgba(255,255,255,0.1);
color: #FFFFFF;
border: 1px solid rgba(255,255,255,0.2);
/* Hover */
background: rgba(255,255,255,0.2);
```

### 卡片 Cards

#### 业务卡片 Business Card
```css
background: #FFFFFF;
border: 1px solid #E5E7EB;
border-radius: 12px;
padding: 40px;
transition: all 0.3s ease;
/* Hover */
border-color: #2563EB;
box-shadow: 0 10px 40px rgba(0,0,0,0.1);
transform: translateY(-4px);
```

#### 案例卡片 Case Card
```css
background: #FFFFFF;
border-radius: 12px;
overflow: hidden;
box-shadow: 0 4px 6px rgba(0,0,0,0.05);
transition: all 0.3s ease;
/* Hover */
box-shadow: 0 20px 25px rgba(0,0,0,0.1);
transform: translateY(-4px);
```

### 导航 Navigation

#### 主导航 Main Nav
```css
height: 72px;
background: rgba(255,255,255,0.95);
backdrop-filter: blur(10px);
border-bottom: 1px solid rgba(0,0,0,0.05);
position: fixed;
top: 0;
left: 0;
right: 0;
z-index: 1000;
```

#### 导航链接 Nav Link
```css
color: #4B5563;
font-size: 15px;
font-weight: 500;
padding: 8px 16px;
transition: color 0.2s;
/* Hover & Active */
color: #2563EB;
```

### 表单输入 Inputs

#### 文本输入 Text Input
```css
background: #FFFFFF;
border: 1.5px solid #E5E7EB;
border-radius: 8px;
padding: 12px 16px;
font-size: 16px;
transition: border-color 0.2s;
/* Focus */
border-color: #2563EB;
box-shadow: 0 0 0 3px rgba(37,99,235,0.1);
```

---

## 7. 动效规范 Animation

### 过渡时间 Transition Duration
| 名称 | 值 | 用途 |
|------|-----|------|
| fast | 150ms | 按钮、链接 |
| normal | 200ms | 标准过渡 |
| slow | 300ms | 卡片悬浮、展开 |
| slower | 500ms | 页面过渡、大动画 |

### 缓动函数 Easing Functions
| 名称 | 值 | 用途 |
|------|-----|------|
| ease | ease | 通用 |
| ease-out | cubic-bezier(0,0,0.2,1) | 入场动画 |
| ease-in-out | cubic-bezier(0.4,0,0.2,1) | 对称动画 |
| spring | cubic-bezier(0.34,1.56,0.64,1) | 弹性效果 |

### 常用动画
- **淡入**: opacity 0→1, 300ms ease-out
- **上浮**: translateY(20px→0), opacity 0→1, 400ms ease-out
- **缩放**: scale(0.95→1), 200ms ease-out
- **悬浮**: translateY(-4px), 200ms ease

---

## 8. 响应式断点 Breakpoints

| 断点 | 宽度 | 说明 |
|------|------|------|
| sm | 640px | 大手机 |
| md | 768px | 平板 |
| lg | 1024px | 小桌面 |
| xl | 1280px | 标准桌面 |
| 2xl | 1536px | 大桌面 |

---

## 9. 图标规范 Icons

- **图标库**: Lucide Icons / Heroicons
- **默认尺寸**: 20px (按钮内), 24px (独立使用)
- **线条粗细**: 1.5px
- **颜色**: 继承父元素颜色

---

## 10. 设计原则

1. **简约至上**: 减少视觉噪音，保持界面清爽
2. **层次清晰**: 通过色彩、字号、间距建立视觉层级
3. **一致性强**: 相同元素保持一致的样式和交互
4. **科技感**: 使用渐变、微光、圆角等现代设计语言
5. **留白充足**: 给内容呼吸空间，避免拥挤

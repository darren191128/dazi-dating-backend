<!-- ============================================ -->
<!-- 重庆启云网络科技有限公司官网 - Vue3组件示例 -->
<!-- ============================================ -->

<!-- 1. 导航组件 Navbar.vue -->
<template>
  <nav 
    class="navbar" 
    :class="{ 'navbar--scrolled': isScrolled }"
  >
    <div class="navbar__container">
      <!-- Logo -->
      <a href="/" class="navbar__logo">
        <img src="/logo.svg" alt="启云网络" />
      </a>
      
      <!-- 桌面导航 -->
      <div class="navbar__menu">
        <a 
          v-for="item in navItems" 
          :key="item.path"
          :href="item.path"
          class="navbar__link"
          :class="{ 'navbar__link--active': isActive(item.path) }"
        >
          {{ item.name }}
        </a>
      </div>
      
      <!-- CTA按钮 -->
      <button class="btn btn--primary navbar__cta">
        免费咨询
      </button>
      
      <!-- 移动端菜单按钮 -->
      <button 
        class="navbar__toggle"
        @click="isMobileMenuOpen = !isMobileMenuOpen"
      >
        <span></span>
        <span></span>
        <span></span>
      </button>
    </div>
    
    <!-- 移动端菜单 -->
    <div 
      class="navbar__mobile-menu"
      :class="{ 'navbar__mobile-menu--open': isMobileMenuOpen }"
    >
      <a 
        v-for="item in navItems" 
        :key="item.path"
        :href="item.path"
        class="navbar__mobile-link"
      >
        {{ item.name }}
      </a>
    </div>
  </nav>
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted } from 'vue'

interface NavItem {
  name: string
  path: string
}

const navItems: NavItem[] = [
  { name: '首页', path: '/' },
  { name: '关于我们', path: '/about' },
  { name: '服务', path: '/services' },
  { name: '案例', path: '/cases' },
  { name: '团队', path: '/team' },
  { name: '新闻', path: '/news' },
  { name: '联系', path: '/contact' },
]

const isScrolled = ref(false)
const isMobileMenuOpen = ref(false)

const handleScroll = () => {
  isScrolled.value = window.scrollY > 50
}

const isActive = (path: string) => {
  return window.location.pathname === path
}

onMounted(() => {
  window.addEventListener('scroll', handleScroll)
})

onUnmounted(() => {
  window.removeEventListener('scroll', handleScroll)
})
</script>

<style scoped lang="scss">
.navbar {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  z-index: 1000;
  height: 72px;
  transition: all 0.3s ease;
  background: transparent;
  
  &--scrolled {
    background: rgba(255, 255, 255, 0.95);
    backdrop-filter: blur(10px);
    box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
  }
  
  &__container {
    max-width: 1280px;
    margin: 0 auto;
    padding: 0 24px;
    height: 100%;
    display: flex;
    align-items: center;
    justify-content: space-between;
  }
  
  &__logo {
    img {
      height: 40px;
    }
  }
  
  &__menu {
    display: flex;
    gap: 32px;
    
    @media (max-width: 768px) {
      display: none;
    }
  }
  
  &__link {
    font-size: 16px;
    font-weight: 500;
    color: #111827;
    text-decoration: none;
    transition: color 0.2s;
    
    &:hover,
    &--active {
      color: #2563eb;
    }
  }
  
  &__cta {
    @media (max-width: 768px) {
      display: none;
    }
  }
  
  &__toggle {
    display: none;
    flex-direction: column;
    gap: 5px;
    background: none;
    border: none;
    cursor: pointer;
    
    @media (max-width: 768px) {
      display: flex;
    }
    
    span {
      width: 24px;
      height: 2px;
      background: #111827;
      transition: all 0.3s;
    }
  }
  
  &__mobile-menu {
    display: none;
    position: absolute;
    top: 72px;
    left: 0;
    right: 0;
    background: white;
    padding: 24px;
    box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
    
    @media (max-width: 768px) {
      display: block;
      transform: translateY(-100%);
      opacity: 0;
      visibility: hidden;
      transition: all 0.3s;
      
      &--open {
        transform: translateY(0);
        opacity: 1;
        visibility: visible;
      }
    }
  }
  
  &__mobile-link {
    display: block;
    padding: 16px 0;
    font-size: 18px;
    color: #111827;
    text-decoration: none;
    border-bottom: 1px solid #e5e7eb;
    
    &:last-child {
      border-bottom: none;
    }
  }
}
</style>

<!-- ============================================ -->
<!-- 2. Hero组件 HeroSection.vue -->
<!-- ============================================ -->
<template>
  <section class="hero">
    <div class="hero__background">
      <div class="hero__grid"></div>
      <div class="hero__gradient"></div>
    </div>
    
    <div class="hero__container">
      <div class="hero__content">
        <h1 class="hero__title">
          数字化转型的<br />
          <span class="hero__highlight">专业伙伴</span>
        </h1>
        <p class="hero__subtitle">
          专注小程序、APP、网站定制开发<br />
          助力企业数字化升级
        </p>
        <div class="hero__actions">
          <button class="btn btn--primary btn--large">
            免费咨询
          </button>
          <button class="btn btn--secondary btn--large">
            查看案例
          </button>
        </div>
      </div>
      
      <div class="hero__visual">
        <!-- 代码片段装饰 -->
        <div class="hero__code-block">
          <pre><code>{{ codeSnippet }}</code></pre>
        </div>
      </div>
    </div>
    
    <div class="hero__scroll">
      <span>向下滚动</span>
      <div class="hero__scroll-arrow"></div>
    </div>
  </section>
</template>

<script setup lang="ts">
const codeSnippet = `// 启云网络 - 专业定制开发
const solution = {
  type: 'custom',
  services: ['小程序', 'APP', '网站'],
  quality: 'premium',
  support: '7×24小时'
}

export default solution`
</script>

<style scoped lang="scss">
.hero {
  position: relative;
  min-height: 100vh;
  display: flex;
  align-items: center;
  overflow: hidden;
  background: linear-gradient(135deg, #0f2440 0%, #1c487a 50%, #2563eb 100%);
  
  &__background {
    position: absolute;
    inset: 0;
    overflow: hidden;
  }
  
  &__grid {
    position: absolute;
    inset: 0;
    background-image: 
      linear-gradient(rgba(255,255,255,0.03) 1px, transparent 1px),
      linear-gradient(90deg, rgba(255,255,255,0.03) 1px, transparent 1px);
    background-size: 50px 50px;
  }
  
  &__container {
    position: relative;
    z-index: 1;
    max-width: 1280px;
    margin: 0 auto;
    padding: 120px 24px;
    display: grid;
    grid-template-columns: 1fr 1fr;
    gap: 64px;
    align-items: center;
    
    @media (max-width: 768px) {
      grid-template-columns: 1fr;
      text-align: center;
    }
  }
  
  &__title {
    font-size: 48px;
    font-weight: 700;
    color: white;
    line-height: 1.2;
    margin-bottom: 24px;
    
    @media (max-width: 768px) {
      font-size: 32px;
    }
  }
  
  &__highlight {
    background: linear-gradient(90deg, #60a5fa, #22d3ee);
    -webkit-background-clip: text;
    -webkit-text-fill-color: transparent;
    background-clip: text;
  }
  
  &__subtitle {
    font-size: 20px;
    color: rgba(255, 255, 255, 0.8);
    line-height: 1.6;
    margin-bottom: 40px;
    
    @media (max-width: 768px) {
      font-size: 16px;
    }
  }
  
  &__actions {
    display: flex;
    gap: 16px;
    
    @media (max-width: 768px) {
      justify-content: center;
      flex-wrap: wrap;
    }
  }
  
  &__visual {
    @media (max-width: 768px) {
      display: none;
    }
  }
  
  &__code-block {
    background: rgba(0, 0, 0, 0.3);
    border-radius: 12px;
    padding: 24px;
    backdrop-filter: blur(10px);
    border: 1px solid rgba(255, 255, 255, 0.1);
    
    pre {
      margin: 0;
      font-family: 'JetBrains Mono', monospace;
      font-size: 14px;
      line-height: 1.6;
      color: #22d3ee;
    }
  }
  
  &__scroll {
    position: absolute;
    bottom: 40px;
    left: 50%;
    transform: translateX(-50%);
    display: flex;
    flex-direction: column;
    align-items: center;
    gap: 8px;
    color: rgba(255, 255, 255, 0.6);
    font-size: 14px;
    
    &-arrow {
      width: 24px;
      height: 24px;
      border-right: 2px solid rgba(255, 255, 255, 0.6);
      border-bottom: 2px solid rgba(255, 255, 255, 0.6);
      transform: rotate(45deg);
      animation: bounce 2s infinite;
    }
  }
}

@keyframes bounce {
  0%, 20%, 50%, 80%, 100% {
    transform: rotate(45deg) translateY(0);
  }
  40% {
    transform: rotate(45deg) translateY(-10px);
  }
  60% {
    transform: rotate(45deg) translateY(-5px);
  }
}
</style>

<!-- ============================================ -->
<!-- 3. 服务卡片组件 ServiceCard.vue -->
<!-- ============================================ -->
<template>
  <div class="service-card">
    <div class="service-card__icon">
      <slot name="icon">
        <svg viewBox="0 0 24 24" fill="none" stroke="currentColor">
          <path d="M12 2L2 7l10 5 10-5-10-5zM2 17l10 5 10-5M2 12l10 5 10-5"/>
        </svg>
      </slot>
    </div>
    <h3 class="service-card__title">{{ title }}</h3>
    <p class="service-card__description">{{ description }}</p>
    <a :href="link" class="service-card__link">
      了解更多
      <svg viewBox="0 0 24 24" fill="none" stroke="currentColor">
        <path d="M5 12h14M12 5l7 7-7 7"/>
      </svg>
    </a>
  </div>
</template>

<script setup lang="ts">
interface Props {
  title: string
  description: string
  link: string
}

defineProps<Props>()
</script>

<style scoped lang="scss">
.service-card {
  background: white;
  border-radius: 12px;
  padding: 32px;
  box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1);
  transition: all 0.3s ease;
  
  &:hover {
    transform: translateY(-4px);
    box-shadow: 0 20px 25px -5px rgba(0, 0, 0, 0.1);
  }
  
  &__icon {
    width: 56px;
    height: 56px;
    background: linear-gradient(135deg, #2563eb, #06b6d4);
    border-radius: 12px;
    display: flex;
    align-items: center;
    justify-content: center;
    margin-bottom: 24px;
    
    svg {
      width: 28px;
      height: 28px;
      color: white;
    }
  }
  
  &__title {
    font-size: 20px;
    font-weight: 600;
    color: #111827;
    margin-bottom: 12px;
  }
  
  &__description {
    font-size: 15px;
    color: #4b5563;
    line-height: 1.6;
    margin-bottom: 20px;
  }
  
  &__link {
    display: inline-flex;
    align-items: center;
    gap: 8px;
    font-size: 15px;
    font-weight: 500;
    color: #2563eb;
    text-decoration: none;
    transition: gap 0.2s;
    
    svg {
      width: 16px;
      height: 16px;
      transition: transform 0.2s;
    }
    
    &:hover {
      gap: 12px;
      
      svg {
        transform: translateX(4px);
      }
    }
  }
}
</style>

<!-- ============================================ -->
<!-- 4. 按钮组件 Button.vue -->
<!-- ============================================ -->
<template>
  <button 
    class="btn"
    :class="[
      `btn--${variant}`,
      `btn--${size}`,
      { 'btn--loading': loading }
    ]"
    :disabled="disabled || loading"
    @click="$emit('click')"
  >
    <span v-if="loading" class="btn__spinner"></span>
    <slot />
  </button>
</template>

<script setup lang="ts">
interface Props {
  variant?: 'primary' | 'secondary' | 'text'
  size?: 'small' | 'medium' | 'large'
  loading?: boolean
  disabled?: boolean
}

withDefaults(defineProps<Props>(), {
  variant: 'primary',
  size: 'medium',
  loading: false,
  disabled: false
})

defineEmits<{
  click: []
}>()
</script>

<style scoped lang="scss">
.btn {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  font-weight: 500;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.2s ease;
  border: none;
  
  &:disabled {
    opacity: 0.6;
    cursor: not-allowed;
  }
  
  // 尺寸
  &--small {
    height: 32px;
    padding: 0 16px;
    font-size: 14px;
  }
  
  &--medium {
    height: 40px;
    padding: 0 24px;
    font-size: 15px;
  }
  
  &--large {
    height: 48px;
    padding: 0 32px;
    font-size: 16px;
  }
  
  // 变体
  &--primary {
    background: #2563eb;
    color: white;
    
    &:hover:not(:disabled) {
      background: #1c487a;
      transform: translateY(-2px);
      box-shadow: 0 4px 12px rgba(37, 99, 235, 0.3);
    }
  }
  
  &--secondary {
    background: transparent;
    color: #2563eb;
    border: 1.5px solid #2563eb;
    
    &:hover:not(:disabled) {
      background: #2563eb;
      color: white;
    }
  }
  
  &--text {
    background: transparent;
    color: #2563eb;
    padding: 0;
    
    &:hover:not(:disabled) {
      color: #1c487a;
      text-decoration: underline;
    }
  }
  
  // 加载状态
  &__spinner {
    width: 16px;
    height: 16px;
    border: 2px solid currentColor;
    border-right-color: transparent;
    border-radius: 50%;
    animation: spin 0.8s linear infinite;
  }
}

@keyframes spin {
  to {
    transform: rotate(360deg);
  }
}
</style>

<!-- ============================================ -->
<!-- 5. 页脚组件 Footer.vue -->
<!-- ============================================ -->
<template>
  <footer class="footer">
    <div class="footer__container">
      <div class="footer__grid">
        <!-- 公司信息 -->
        <div class="footer__brand">
          <img src="/logo-white.svg" alt="启云网络" class="footer__logo" />
          <p class="footer__description">
            专注软件定制开发，为企业提供小程序、APP、网站等全方位数字化解决方案。
          </p>
          <div class="footer__social">
            <a v-for="social in socials" :key="social.name" :href="social.url">
              <component :is="social.icon" />
            </a>
          </div>
        </div>
        
        <!-- 快速链接 -->
        <div class="footer__column">
          <h4 class="footer__title">快速链接</h4>
          <ul class="footer__links">
            <li v-for="link in quickLinks" :key="link.name">
              <a :href="link.url">{{ link.name }}</a>
            </li>
          </ul>
        </div>
        
        <!-- 服务 -->
        <div class="footer__column">
          <h4 class="footer__title">服务</h4>
          <ul class="footer__links">
            <li v-for="service in services" :key="service">
              <a :href="`/services#${service}`">{{ service }}</a>
            </li>
          </ul>
        </div>
        
        <!-- 联系我们 -->
        <div class="footer__column">
          <h4 class="footer__title">联系我们</h4>
          <ul class="footer__contact">
            <li>
              <svg><!-- 电话图标 --></svg>
              <span>400-XXX-XXXX</span>
            </li>
            <li>
              <svg><!-- 邮箱图标 --></svg>
              <span>contact@qiyunsoft.com</span>
            </li>
            <li>
              <svg><!-- 地址图标 --></svg>
              <span>重庆市XX区XX路XX号</span>
            </li>
          </ul>
        </div>
      </div>
      
      <div class="footer__bottom">
        <p>© 2024 重庆启云网络科技有限公司 版权所有 | 渝ICP备XXXXXXXX号</p>
      </div>
    </div>
  </footer>
</template>

<script setup lang="ts">
const quickLinks = [
  { name: '关于我们', url: '/about' },
  { name: '服务介绍', url: '/services' },
  { name: '案例展示', url: '/cases' },
  { name: '新闻动态', url: '/news' },
  { name: '联系我们', url: '/contact' },
]

const services = ['小程序开发', 'APP开发', '网站开发']

const socials = [
  { name: '微信', url: '#', icon: 'WechatIcon' },
  { name: '微博', url: '#', icon: 'WeiboIcon' },
  { name: 'GitHub', url: '#', icon: 'GithubIcon' },
]
</script>

<style scoped lang="scss">
.footer {
  background: #0a1628;
  color: white;
  padding: 64px 0 32px;
  
  &__container {
    max-width: 1280px;
    margin: 0 auto;
    padding: 0 24px;
  }
  
  &__grid {
    display: grid;
    grid-template-columns: 2fr 1fr 1fr 1fr;
    gap: 48px;
    margin-bottom: 48px;
    
    @media (max-width: 768px) {
      grid-template-columns: 1fr 1fr;
    }
    
    @media (max-width: 480px) {
      grid-template-columns: 1fr;
    }
  }
  
  &__logo {
    height: 40px;
    margin-bottom: 16px;
  }
  
  &__description {
    color: rgba(255, 255, 255, 0.7);
    font-size: 14px;
    line-height: 1.6;
    margin-bottom: 24px;
  }
  
  &__social {
    display: flex;
    gap: 16px;
    
    a {
      width: 40px;
      height: 40px;
      background: rgba(255, 255, 255, 0.1);
      border-radius: 8px;
      display: flex;
      align-items: center;
      justify-content: center;
      transition: all 0.2s;
      
      &:hover {
        background: #2563eb;
      }
    }
  }
  
  &__title {
    font-size: 16px;
    font-weight: 600;
    margin-bottom: 20px;
  }
  
  &__links,
  &__contact {
    list-style: none;
    padding: 0;
    margin: 0;
    
    li {
      margin-bottom: 12px;
    }
    
    a {
      color: rgba(255, 255, 255, 0.7);
      text-decoration: none;
      font-size: 14px;
      transition: color 0.2s;
      
      &:hover {
        color: #60a5fa;
      }
    }
  }
  
  &__contact {
    li {
      display: flex;
      align-items: center;
      gap: 12px;
      color: rgba(255, 255, 255, 0.7);
      font-size: 14px;
    }
  }
  
  &__bottom {
    border-top: 1px solid rgba(255, 255, 255, 0.1);
    padding-top: 32px;
    text-align: center;
    
    p {
      color: rgba(255, 255, 255, 0.5);
      font-size: 14px;
    }
  }
}
</style>

<!-- ============================================ -->
<!-- 6. 全局样式 variables.scss -->
<!-- ============================================ -->

// 主色调
$color-primary-900: #0a1628;
$color-primary-800: #0f2440;
$color-primary-700: #15355c;
$color-primary-600: #1c487a;
$color-primary-500: #2563eb;
$color-primary-400: #3b82f6;
$color-primary-300: #60a5fa;
$color-primary-200: #93c5fd;
$color-primary-100: #dbeafe;
$color-primary-50: #eff6ff;

// 辅助色
$color-secondary-500: #06b6d4;
$color-accent-500: #f97316;

// 中性色
$color-text-primary: #111827;
$color-text-secondary: #4b5563;
$color-text-tertiary: #9ca3af;

$color-bg-primary: #ffffff;
$color-bg-secondary: #f9fafb;
$color-bg-tertiary: #f3f4f6;

$color-border-light: #e5e7eb;
$color-border-medium: #d1d5db;

// 状态色
$color-success: #10b981;
$color-warning: #f59e0b;
$color-error: #ef4444;
$color-info: #3b82f6;

// 字体
$font-chinese: "PingFang SC", "Microsoft YaHei", "Hiragino Sans GB", sans-serif;
$font-english: "Inter", "SF Pro Display", -apple-system, BlinkMacSystemFont, sans-serif;

// 间距
$spacing-xs: 4px;
$spacing-sm: 8px;
$spacing-md: 16px;
$spacing-lg: 24px;
$spacing-xl: 32px;
$spacing-2xl: 48px;
$spacing-3xl: 64px;

// 断点
$breakpoint-sm: 640px;
$breakpoint-md: 768px;
$breakpoint-lg: 1024px;
$breakpoint-xl: 1280px;

// 容器
$container-max-width: 1280px;
$container-padding: 24px;

// 圆角
$radius-sm: 4px;
$radius-md: 8px;
$radius-lg: 12px;
$radius-xl: 16px;
$radius-full: 9999px;

// 阴影
$shadow-sm: 0 1px 2px rgba(0, 0, 0, 0.05);
$shadow-md: 0 4px 6px -1px rgba(0, 0, 0, 0.1);
$shadow-lg: 0 10px 15px -3px rgba(0, 0, 0, 0.1);
$shadow-xl: 0 20px 25px -5px rgba(0, 0, 0, 0.1);
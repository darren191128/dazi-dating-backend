<template>
  <header class="app-header">
    <div class="header-container">
      <div class="logo">
        <router-link to="/">
          <img src="@/assets/logo.svg" alt="Logo" class="logo-img" />
          <span class="logo-text">Company</span>
        </router-link>
      </div>

      <nav class="nav-menu">
        <router-link to="/" class="nav-item">{{ $t('nav.home') }}</router-link>
        <router-link to="/about" class="nav-item">{{ $t('nav.about') }}</router-link>
        <router-link to="/products" class="nav-item">{{ $t('nav.products') }}</router-link>
        <router-link to="/services" class="nav-item">{{ $t('nav.services') }}</router-link>
        <router-link to="/contact" class="nav-item">{{ $t('nav.contact') }}</router-link>
      </nav>

      <div class="header-actions">
        <el-button circle size="small" @click="toggleLanguage" class="lang-btn">
          {{ currentLang === 'zh' ? 'EN' : '中' }}
        </el-button>
      </div>
    </div>
  </header>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { useI18n } from 'vue-i18n'

const { locale } = useI18n()

const currentLang = computed(() => locale.value)

const toggleLanguage = () => {
  locale.value = locale.value === 'zh' ? 'en' : 'zh'
}
</script>

<style scoped>
.app-header {
  background: #fff;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  z-index: 1000;
}

.header-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
  height: 64px;
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.logo a {
  display: flex;
  align-items: center;
  text-decoration: none;
  color: #333;
}

.logo-img {
  width: 40px;
  height: 40px;
  margin-right: 10px;
}

.logo-text {
  font-size: 20px;
  font-weight: 600;
}

.nav-menu {
  display: flex;
  gap: 30px;
}

.nav-item {
  text-decoration: none;
  color: #666;
  font-size: 15px;
  transition: color 0.3s;
  position: relative;
}

.nav-item:hover,
.nav-item.router-link-active {
  color: #409eff;
}

.nav-item.router-link-active::after {
  content: '';
  position: absolute;
  bottom: -20px;
  left: 0;
  right: 0;
  height: 2px;
  background: #409eff;
}

.header-actions {
  display: flex;
  align-items: center;
  gap: 15px;
}

.lang-btn {
  font-weight: 600;
}

@media (max-width: 768px) {
  .nav-menu {
    display: none;
  }
}
</style>

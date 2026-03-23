<template>
  <header class="fixed top-0 left-0 right-0 z-50 bg-slate-900/95 backdrop-blur-sm border-b border-white/10 transition-all duration-300"
    :class="{ 'shadow-lg': isScrolled }">
    <div class="container mx-auto px-4 sm:px-6 lg:px-8">
      <div class="flex items-center justify-between h-16">
        <!-- Logo -->
        <RouterLink to="/" class="flex items-center space-x-2">
          <div class="w-8 h-8 bg-gradient-to-br from-blue-500 to-cyan-400 rounded-lg flex items-center justify-center">
            <i class="fas fa-cloud text-white text-sm"></i>
          </div>
          <span class="text-white font-bold text-lg">启云科技</span>
        </RouterLink>

        <!-- Desktop Navigation -->
        <nav class="hidden md:flex items-center space-x-8">
          <RouterLink
            v-for="item in navItems"
            :key="item.path"
            :to="item.path"
            class="text-white/80 hover:text-white text-sm font-medium transition-colors"
          >
            {{ item.name }}
          </RouterLink>
        </nav>

        <!-- CTA Button -->
        <div class="hidden md:block">
          <RouterLink to="/contact" class="px-5 py-2 bg-blue-600 hover:bg-blue-500 text-white text-sm font-medium rounded-lg transition-colors">
            免费咨询
          </RouterLink>
        </div>

        <!-- Mobile Menu Button -->
        <button
          @click="toggleMobileMenu"
          class="md:hidden p-2 text-white"
        >
          <svg v-if="!isMobileMenuOpen" class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 6h16M4 12h16M4 18h16" />
          </svg>
          <svg v-else class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" />
          </svg>
        </button>
      </div>
    </div>

    <!-- Mobile Menu -->
    <div v-if="isMobileMenuOpen" class="md:hidden bg-slate-900 border-t border-white/10">
      <div class="container mx-auto px-4 py-4 space-y-2">
        <RouterLink
          v-for="item in navItems"
          :key="item.path"
          :to="item.path"
          @click="closeMobileMenu"
          class="block text-white/80 hover:text-white py-2 text-sm"
        >
          {{ item.name }}
        </RouterLink>
        <RouterLink
          to="/contact"
          @click="closeMobileMenu"
          class="block w-full text-center px-5 py-2 bg-blue-600 text-white rounded-lg text-sm font-medium mt-4"
        >
          免费咨询
        </RouterLink>
      </div>
    </div>
  </header>
  <div class="h-16"></div>
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted } from 'vue'

const navItems = [
  { name: '首页', path: '/' },
  { name: '关于我们', path: '/about' },
  { name: '服务介绍', path: '/services' },
  { name: '案例展示', path: '/cases' },
  { name: '团队介绍', path: '/team' },
  { name: '新闻动态', path: '/news' },
  { name: '招贤纳士', path: '/careers' },
  { name: '联系我们', path: '/contact' }
]

const isMobileMenuOpen = ref(false)
const isScrolled = ref(false)

const toggleMobileMenu = () => {
  isMobileMenuOpen.value = !isMobileMenuOpen.value
}

const closeMobileMenu = () => {
  isMobileMenuOpen.value = false
}

const handleScroll = () => {
  isScrolled.value = window.scrollY > 10
}

onMounted(() => {
  window.addEventListener('scroll', handleScroll)
  handleScroll()
})

onUnmounted(() => {
  window.removeEventListener('scroll', handleScroll)
})
</script>
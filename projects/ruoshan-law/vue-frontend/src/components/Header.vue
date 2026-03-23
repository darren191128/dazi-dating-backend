<template>
  <header class="fixed top-0 left-0 right-0 z-50 bg-white/95 backdrop-blur-sm shadow-sm">
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
      <div class="flex justify-between items-center h-16">
        <!-- Logo -->
        <router-link to="/" class="flex items-center space-x-2">
          <span class="text-2xl font-bold text-primary">若山</span>
          <span class="text-sm text-gray-600 hidden sm:block">律师事务所</span>
        </router-link>
        
        <!-- Desktop Navigation -->
        <nav class="hidden md:flex space-x-8">
          <router-link 
            v-for="item in navItems" 
            :key="item.path"
            :to="item.path"
            class="text-gray-700 hover:text-primary transition-colors duration-200"
            :class="{ 'text-primary font-semibold': $route.path === item.path }"
          >
            {{ item.name }}
          </router-link>
        </nav>
        
        <!-- CTA Button -->
        <router-link 
          to="/contact"
          class="hidden md:block btn-primary text-sm"
        >
          立即咨询
        </router-link>
        
        <!-- Mobile Menu Button -->
        <button 
          class="md:hidden p-2"
          @click="isMenuOpen = !isMenuOpen"
        >
          <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path 
              v-if="!isMenuOpen"
              stroke-linecap="round" 
              stroke-linejoin="round" 
              stroke-width="2" 
              d="M4 6h16M4 12h16M4 18h16"
            />
            <path 
              v-else
              stroke-linecap="round" 
              stroke-linejoin="round" 
              stroke-width="2" 
              d="M6 18L18 6M6 6l12 12"
            />
          </svg>
        </button>
      </div>
    </div>
    
    <!-- Mobile Menu -->
    <transition
      enter-active-class="transition duration-200 ease-out"
      enter-from-class="opacity-0 -translate-y-2"
      enter-to-class="opacity-100 translate-y-0"
      leave-active-class="transition duration-150 ease-in"
      leave-from-class="opacity-100 translate-y-0"
      leave-to-class="opacity-0 -translate-y-2"
    >
      <div v-if="isMenuOpen" class="md:hidden bg-white border-t">
        <div class="px-4 py-2 space-y-1">
          <router-link
            v-for="item in navItems"
            :key="item.path"
            :to="item.path"
            class="block px-3 py-2 text-gray-700 hover:text-primary hover:bg-gray-50 rounded-md"
            @click="isMenuOpen = false"
          >
            {{ item.name }}
          </router-link>
          <router-link
            to="/contact"
            class="block px-3 py-2 btn-primary text-center mt-4"
            @click="isMenuOpen = false"
          >
            立即咨询
          </router-link>
        </div>
      </div>
    </transition>
  </header>
</template>

<script setup lang="ts">
import { ref } from 'vue'

const isMenuOpen = ref(false)

const navItems = [
  { name: '首页', path: '/' },
  { name: '关于我们', path: '/about' },
  { name: '服务领域', path: '/services' },
  { name: '专业团队', path: '/team' },
  { name: '成功案例', path: '/cases' },
  { name: '新闻资讯', path: '/news' }
]
</script>

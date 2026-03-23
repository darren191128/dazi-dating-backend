<template>
  <div class="min-h-screen bg-slate-50">
    <AppHeader />
    
    <!-- 页面标题 -->
    <section class="relative py-20 md:py-28 bg-gradient-to-br from-slate-900 via-blue-900 to-blue-800 overflow-hidden">
      <div class="absolute inset-0 opacity-10">
        <div class="absolute top-20 left-10 w-72 h-72 bg-white rounded-full blur-3xl"></div>
        <div class="absolute bottom-20 right-10 w-96 h-96 bg-blue-400 rounded-full blur-3xl"></div>
      </div>
      <div class="container mx-auto px-4 sm:px-6 lg:px-8 relative z-10">
        <div class="text-center">
          <nav class="flex justify-center mb-6">
            <ol class="flex items-center space-x-2 text-sm text-white/60">
              <li><RouterLink to="/" class="hover:text-white transition-colors">首页</RouterLink></li>
              <li><span class="text-white/40">/</span></li>
              <li class="text-white">团队介绍</li>
            </ol>
          </nav>
          <h1 class="text-4xl md:text-5xl font-bold text-white mb-4">我们的团队</h1>
          <p class="text-xl text-white/80 max-w-2xl mx-auto">专业的技术团队，为您提供最优质的服务</p>
        </div>
      </div>
    </section>

    <!-- 团队数据 -->
    <section class="py-16 bg-white">
      <div class="container mx-auto px-4 sm:px-6 lg:px-8">
        <div class="grid grid-cols-2 md:grid-cols-4 gap-8">
          <div v-for="(stat, index) in stats" :key="index" class="text-center">
            <div class="text-4xl md:text-5xl font-bold text-blue-600 mb-2">{{ stat.value }}</div>
            <div class="text-slate-600">{{ stat.label }}</div>
          </div>
        </div>
      </div>
    </section>

    <!-- 核心成员 -->
    <section class="py-16 md:py-24">
      <div class="container mx-auto px-4 sm:px-6 lg:px-8">
        <div class="text-center mb-12">
          <h2 class="text-3xl font-bold text-slate-900 mb-4">核心成员</h2>
          <p class="text-slate-600">经验丰富的管理团队，引领技术创新</p>
        </div>
        
        <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-8">
          <div v-for="(member, index) in team" :key="index" class="bg-white rounded-2xl p-6 shadow-lg text-center group hover:shadow-xl transition-shadow">
            <div class="w-24 h-24 mx-auto mb-4 rounded-full bg-gradient-to-br from-blue-400 to-blue-600 flex items-center justify-center text-white text-2xl font-bold">
              {{ member.name[0] }}
            </div>
            <h3 class="text-lg font-bold text-slate-900 mb-1">{{ member.name }}</h3>
            <p class="text-blue-600 text-sm mb-3">{{ member.position }}</p>
            <p class="text-slate-500 text-sm">{{ member.description }}</p>
          </div>
        </div>
      </div>
    </section>

    <!-- 团队文化 -->
    <section class="py-16 md:py-24 bg-white">
      <div class="container mx-auto px-4 sm:px-6 lg:px-8">
        <div class="text-center mb-12">
          <h2 class="text-3xl font-bold text-slate-900 mb-4">团队文化</h2>
          <p class="text-slate-600">我们崇尚创新、协作、成长的工作氛围</p>
        </div>
        
        <div class="grid grid-cols-2 md:grid-cols-4 gap-4">
          <div v-for="i in 8" :key="i" class="aspect-square bg-slate-200 rounded-xl overflow-hidden">
            <img :src="`https://picsum.photos/300/300?random=${i}`" :alt="`团队活动${i}`" class="w-full h-full object-cover hover:scale-110 transition-transform" />
          </div>
        </div>
      </div>
    </section>

    <AppFooter />
  </div>
</template>

<script setup lang="ts">
import AppHeader from '@/components/layout/AppHeader.vue'
import AppFooter from '@/components/layout/AppFooter.vue'

const stats = [
  { value: '50+', label: '团队成员' },
  { value: '30+', label: '技术人员' },
  { value: '5', label: '核心部门' },
  { value: '100+', label: '交付项目' }
]

const team = [
  {
    name: '张总',
    position: '创始人 & CEO',
    description: '10年+互联网经验，曾任职知名互联网公司高管'
  },
  {
    name: '李工',
    position: '技术总监',
    description: '全栈技术专家，主导多个大型项目架构设计'
  },
  {
    name: '王经理',
    position: '产品总监',
    description: '资深产品经理，擅长用户体验设计和产品规划'
  },
  {
    name: '陈经理',
    position: '运营总监',
    description: '丰富的项目管理和客户沟通经验'
  }
]
</script>
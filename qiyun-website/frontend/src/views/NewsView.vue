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
              <li class="text-white">新闻动态</li>
            </ol>
          </nav>
          <h1 class="text-4xl md:text-5xl font-bold text-white mb-4">新闻动态</h1>
          <p class="text-xl text-white/80 max-w-2xl mx-auto">了解启云科技的最新动态和行业资讯</p>
        </div>
      </div>
    </section>

    <!-- 新闻列表 -->
    <section class="py-16 md:py-24">
      <div class="container mx-auto px-4 sm:px-6 lg:px-8">
        <div class="flex flex-wrap gap-4 mb-8">
          <button 
            v-for="cat in categories" 
            :key="cat"
            @click="activeCategory = cat"
            :class="['px-4 py-2 rounded-lg text-sm font-medium transition-colors', activeCategory === cat ? 'bg-blue-600 text-white' : 'bg-white text-slate-600 hover:bg-slate-100']"
          >
            {{ cat }}
          </button>
        </div>
        
        <div class="space-y-6">
          <RouterLink 
            v-for="(news, index) in filteredNews" 
            :key="index"
            :to="`/news/${news.id}`"
            class="flex flex-col md:flex-row gap-6 bg-white rounded-2xl p-6 shadow-lg hover:shadow-xl transition-shadow group"
          >
            <div class="w-full md:w-48 h-32 bg-slate-200 rounded-xl overflow-hidden flex-shrink-0">
              <img :src="news.image" :alt="news.title" class="w-full h-full object-cover group-hover:scale-110 transition-transform" />
            </div>
            <div class="flex-1">
              <div class="flex items-center gap-3 mb-2">
                <span class="px-3 py-1 bg-blue-100 text-blue-700 text-xs font-medium rounded-full">{{ news.category }}</span>
                <span class="text-slate-400 text-sm">{{ news.date }}</span>
              </div>
              <h3 class="text-xl font-bold text-slate-900 mb-2 group-hover:text-blue-600 transition-colors">{{ news.title }}</h3>
              <p class="text-slate-600 text-sm line-clamp-2">{{ news.summary }}</p>
            </div>
          </RouterLink>
        </div>
        
        <!-- 分页 -->
        <div class="flex justify-center mt-12 gap-2">
          <button class="w-10 h-10 rounded-lg bg-blue-600 text-white font-medium">1</button>
          <button class="w-10 h-10 rounded-lg bg-white text-slate-600 hover:bg-slate-100">2</button>
          <button class="w-10 h-10 rounded-lg bg-white text-slate-600 hover:bg-slate-100">3</button>
          <button class="w-10 h-10 rounded-lg bg-white text-slate-600 hover:bg-slate-100">
            <svg class="w-5 h-5 mx-auto" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5l7 7-7 7" />
            </svg>
          </button>
        </div>
      </div>
    </section>

    <AppFooter />
  </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'
import AppHeader from '@/components/layout/AppHeader.vue'
import AppFooter from '@/components/layout/AppFooter.vue'

const categories = ['全部', '公司新闻', '行业资讯', '技术分享']
const activeCategory = ref('全部')

const newsList = [
  {
    id: 1,
    title: '启云科技荣获2024年度优秀软件企业称号',
    category: '公司新闻',
    date: '2024-03-11',
    summary: '凭借出色的技术实力和优质的服务，启云科技在2024年度评选中脱颖而出，荣获优秀软件企业称号...',
    image: 'https://picsum.photos/400/300?random=1'
  },
  {
    id: 2,
    title: '2024年软件开发行业趋势分析报告',
    category: '行业资讯',
    date: '2024-03-08',
    summary: '随着AI技术的发展，软件开发行业正在经历深刻变革，低代码、云原生成为主流趋势...',
    image: 'https://picsum.photos/400/300?random=2'
  },
  {
    id: 3,
    title: 'Vue3 + TypeScript最佳实践分享',
    category: '技术分享',
    date: '2024-03-05',
    summary: '本文分享了在大型项目中使用Vue3和TypeScript的经验，包括项目架构、状态管理、性能优化等方面...',
    image: 'https://picsum.photos/400/300?random=3'
  },
  {
    id: 4,
    title: '启云科技成功签约某大型零售企业',
    category: '公司新闻',
    date: '2024-03-01',
    summary: '经过多轮竞标，启云科技凭借专业的技术方案和丰富的行业经验，成功签约某大型零售企业数字化转型项目...',
    image: 'https://picsum.photos/400/300?random=4'
  },
  {
    id: 5,
    title: '小程序开发常见问题及解决方案',
    category: '技术分享',
    date: '2024-02-28',
    summary: '总结小程序开发过程中常见的问题，包括性能优化、兼容性处理、审核规范等方面的解决方案...',
    image: 'https://picsum.photos/400/300?random=5'
  }
]

const filteredNews = computed(() => {
  if (activeCategory.value === '全部') return newsList
  return newsList.filter(n => n.category === activeCategory.value)
})
</script>
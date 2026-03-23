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
              <li class="text-white">招贤纳士</li>
            </ol>
          </nav>
          <h1 class="text-4xl md:text-5xl font-bold text-white mb-4">加入我们</h1>
          <p class="text-xl text-white/80 max-w-2xl mx-auto">与优秀的人一起，创造无限可能</p>
        </div>
      </div>
    </section>

    <!-- 员工福利 -->
    <section class="py-16 bg-white">
      <div class="container mx-auto px-4 sm:px-6 lg:px-8">
        <div class="text-center mb-12">
          <h2 class="text-2xl font-bold text-slate-900 mb-4">员工福利</h2>
        </div>
        <div class="grid grid-cols-2 md:grid-cols-4 gap-6">
          <div v-for="(benefit, index) in benefits" :key="index" class="text-center p-6">
            <div class="w-12 h-12 mx-auto mb-4 bg-blue-100 rounded-xl flex items-center justify-center">
              <svg class="w-6 h-6 text-blue-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 13l4 4L19 7" />
              </svg>
            </div>
            <h3 class="font-semibold text-slate-900">{{ benefit }}</h3>
          </div>
        </div>
      </div>
    </section>

    <!-- 职位列表 -->
    <section class="py-16 md:py-24">
      <div class="container mx-auto px-4 sm:px-6 lg:px-8">
        <div class="flex flex-wrap gap-4 mb-8">
          <button 
            v-for="dept in departments" 
            :key="dept"
            @click="activeDept = dept"
            :class="['px-4 py-2 rounded-lg text-sm font-medium transition-colors', activeDept === dept ? 'bg-blue-600 text-white' : 'bg-white text-slate-600 hover:bg-slate-100']"
          >
            {{ dept }}
          </button>
        </div>
        
        <div class="space-y-4">
          <div 
            v-for="(job, index) in filteredJobs" 
            :key="index"
            class="bg-white rounded-2xl p-6 shadow-lg hover:shadow-xl transition-shadow"
          >
            <div class="flex flex-col md:flex-row md:items-center md:justify-between gap-4">
              <div>
                <div class="flex items-center gap-3 mb-2">
                  <h3 class="text-xl font-bold text-slate-900">{{ job.title }}</h3>
                  <span v-if="job.urgent" class="px-2 py-1 bg-red-100 text-red-600 text-xs font-medium rounded">急招</span>
                </div>
                <div class="flex flex-wrap items-center gap-4 text-sm text-slate-500">
                  <span>{{ job.department }}</span>
                  <span class="w-1 h-1 bg-slate-300 rounded-full"></span>
                  <span>{{ job.location }}</span>
                  <span class="w-1 h-1 bg-slate-300 rounded-full"></span>
                  <span class="text-blue-600 font-medium">{{ job.salary }}</span>
                </div>
              </div>
              <RouterLink 
                :to="`/careers/${job.id}`"
                class="px-6 py-2 bg-blue-600 text-white font-medium rounded-lg hover:bg-blue-700 transition-colors text-center"
              >
                查看详情
              </RouterLink>
            </div>
          </div>
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

const benefits = [
  '有竞争力的薪资',
  '五险一金',
  '带薪年假',
  '弹性工作',
  '节日福利',
  '团队活动',
  '培训学习',
  '晋升空间'
]

const departments = ['全部', '技术', '产品', '设计', '运营']
const activeDept = ref('全部')

const jobs = [
  {
    id: 1,
    title: '高级前端工程师',
    department: '技术',
    location: '重庆',
    salary: '15K-25K',
    urgent: true
  },
  {
    id: 2,
    title: 'Java后端工程师',
    department: '技术',
    location: '重庆',
    salary: '15K-25K',
    urgent: true
  },
  {
    id: 3,
    title: '产品经理',
    department: '产品',
    location: '重庆',
    salary: '12K-20K',
    urgent: false
  },
  {
    id: 4,
    title: 'UI设计师',
    department: '设计',
    location: '重庆',
    salary: '10K-18K',
    urgent: false
  },
  {
    id: 5,
    title: '运营专员',
    department: '运营',
    location: '重庆',
    salary: '6K-10K',
    urgent: false
  }
]

const filteredJobs = computed(() => {
  if (activeDept.value === '全部') return jobs
  return jobs.filter(j => j.department === activeDept.value)
})
</script>
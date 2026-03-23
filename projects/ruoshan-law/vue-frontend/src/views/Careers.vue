<template>
  <div class="min-h-screen">
    <Header />
    <main class="pt-16">
      <section class="py-20 bg-gray-50">
        <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div class="text-center mb-16">
            <h1 class="section-title">招贤纳士</h1>
            <p class="section-subtitle">加入我们，共创法律事业新篇章</p>
          </div>
          
          <!-- Company Culture -->
          <div class="bg-white rounded-xl p-8 mb-12">
            <h2 class="text-2xl font-bold text-primary mb-6">律所文化</h2>
            <div class="grid grid-cols-1 md:grid-cols-3 gap-8">
              <div class="text-center">
                <div class="w-16 h-16 bg-primary/10 rounded-full flex items-center justify-center mx-auto mb-4">
                  <svg class="w-8 h-8 text-primary" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z"/>
                  </svg>
                </div>
                <h3 class="text-lg font-semibold text-primary mb-2">专业至上</h3>
                <p class="text-gray-600 text-sm">追求卓越的法律服务品质</p>
              </div>
              <div class="text-center">
                <div class="w-16 h-16 bg-primary/10 rounded-full flex items-center justify-center mx-auto mb-4">
                  <svg class="w-8 h-8 text-primary" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17 20h5v-2a3 3 0 00-5.356-1.857M17 20H7m10 0v-2c0-.656-.126-1.283-.356-1.857M7 20H2v-2a3 3 0 015.356-1.857M7 20v-2c0-.656.126-1.283.356-1.857m0 0a5.002 5.002 0 019.288 0M15 7a3 3 0 11-6 0 3 3 0 016 0zm6 3a2 2 0 11-4 0 2 2 0 014 0zM7 10a2 2 0 11-4 0 2 2 0 014 0z"/>
                  </svg>
                </div>
                <h3 class="text-lg font-semibold text-primary mb-2">团队协作</h3>
                <p class="text-gray-600 text-sm">共同成长，互相成就</p>
              </div>
              <div class="text-center">
                <div class="w-16 h-16 bg-primary/10 rounded-full flex items-center justify-center mx-auto mb-4">
                  <svg class="w-8 h-8 text-primary" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13 7h8m0 0v8m0-8l-8 8-4-4-6 6"/>
                  </svg>
                </div>
                <h3 class="text-lg font-semibold text-primary mb-2">持续发展</h3>
                <p class="text-gray-600 text-sm">完善的培训体系和晋升通道</p>
              </div>
            </div>
          </div>
          
          <!-- Job Listings -->
          <h2 class="text-2xl font-bold text-primary mb-8">招聘岗位</h2>
          <div class="space-y-6">
            <div 
              v-for="job in jobs" 
              :key="job.id"
              class="bg-white rounded-xl p-6 shadow-sm hover:shadow-lg transition-all duration-300"
            >
              <div class="flex flex-col md:flex-row md:items-center md:justify-between">
                <div class="mb-4 md:mb-0">
                  <h3 class="text-xl font-bold text-primary mb-2">{{ job.title }}</h3>
                  <div class="flex flex-wrap gap-4 text-sm text-gray-600">
                    <span class="flex items-center">
                      <svg class="w-4 h-4 mr-1" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17.657 16.657L13.414 20.9a1.998 1.998 0 01-2.827 0l-4.244-4.243a8 8 0 1111.314 0z"/>
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 11a3 3 0 11-6 0 3 3 0 016 0z"/>
                      </svg>
                      {{ job.location }}
                    </span>
                    <span class="flex items-center">
                      <svg class="w-4 h-4 mr-1" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z"/>
                      </svg>
                      {{ job.experience }}
                    </span>
                    <span class="flex items-center">
                      <svg class="w-4 h-4 mr-1" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8c-1.657 0-3 .895-3 2s1.343 2 3 2 3 .895 3 2-1.343 2-3 2m0-8c1.11 0 2.08.402 2.599 1M12 8V7m0 1v8m0 0v1m0-1c-1.11 0-2.08-.402-2.599-1M21 12a9 9 0 11-18 0 9 9 0 0118 0z"/>
                      </svg>
                      {{ job.salary }}
                    </span>
                  </div>
                </div>
                <button class="btn-primary whitespace-nowrap">
                  申请职位
                </button>
              </div>
              <div class="mt-4 pt-4 border-t">
                <h4 class="font-semibold text-primary mb-2">职位要求：</h4>
                <ul class="space-y-1">
                  <li 
                    v-for="(req, index) in job.requirements" 
                    :key="index"
                    class="text-gray-600 text-sm flex items-start"
                  >
                    <span class="text-accent mr-2">•</span>
                    {{ req }}
                  </li>
                </ul>
              </div>
            </div>
          </div>
          
          <!-- Benefits -->
          <div class="mt-12 bg-white rounded-xl p-8">
            <h2 class="text-2xl font-bold text-primary mb-6">福利待遇</h2>
            <div class="grid grid-cols-2 md:grid-cols-4 gap-6">
              <div class="flex items-center">
                <svg class="w-5 h-5 text-accent mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 13l4 4L19 7"/>
                </svg>
                <span class="text-gray-700">五险一金</span>
              </div>
              <div class="flex items-center">
                <svg class="w-5 h-5 text-accent mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 13l4 4L19 7"/>
                </svg>
                <span class="text-gray-700">带薪年假</span>
              </div>
              <div class="flex items-center">
                <svg class="w-5 h-5 text-accent mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 13l4 4L19 7"/>
                </svg>
                <span class="text-gray-700">专业培训</span>
              </div>
              <div class="flex items-center">
                <svg class="w-5 h-5 text-accent mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 13l4 4L19 7"/>
                </svg>
                <span class="text-gray-700">晋升机会</span>
              </div>
              <div class="flex items-center">
                <svg class="w-5 h-5 text-accent mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 13l4 4L19 7"/>
                </svg>
                <span class="text-gray-700">节日福利</span>
              </div>
              <div class="flex items-center">
                <svg class="w-5 h-5 text-accent mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 13l4 4L19 7"/>
                </svg>
                <span class="text-gray-700">团建活动</span>
              </div>
              <div class="flex items-center">
                <svg class="w-5 h-5 text-accent mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 13l4 4L19 7"/>
                </svg>
                <span class="text-gray-700">弹性工作</span>
              </div>
              <div class="flex items-center">
                <svg class="w-5 h-5 text-accent mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 13l4 4L19 7"/>
                </svg>
                <span class="text-gray-700">绩效奖金</span>
              </div>
            </div>
          </div>
        </div>
      </section>
    </main>
    <Footer />
  </div>
</template>

<script setup lang="ts">
import Header from '../components/Header.vue'
import Footer from '../components/Footer.vue'

const jobs = [
  {
    id: 1,
    title: '执业律师',
    location: '北京市',
    experience: '1-3年',
    salary: '15K-25K',
    requirements: [
      '法学本科及以上学历，持有律师执业证',
      '1-3年执业经验，有民商事诉讼经验优先',
      '良好的沟通能力和团队协作精神',
      '责任心强，工作认真负责'
    ]
  },
  {
    id: 2,
    title: '资深律师',
    location: '北京市',
    experience: '3-5年',
    salary: '25K-40K',
    requirements: [
      '法学本科及以上学历，持有律师执业证',
      '3-5年执业经验，能独立承办案件',
      '有专业领域特长，如知识产权、公司法务等',
      '具备团队管理和业务拓展能力'
    ]
  },
  {
    id: 3,
    title: '律师助理',
    location: '北京市',
    experience: '应届生',
    salary: '8K-12K',
    requirements: [
      '法学本科及以上学历，通过司法考试优先',
      '良好的法律功底和文字表达能力',
      '学习能力强，工作积极主动',
      '有志于长期从事律师职业'
    ]
  }
]
</script>

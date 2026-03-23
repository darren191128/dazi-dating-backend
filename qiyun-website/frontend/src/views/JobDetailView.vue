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
              <li><RouterLink to="/careers" class="hover:text-white transition-colors">招贤纳士</RouterLink></li>
              <li><span class="text-white/40">/</span></li>
              <li class="text-white">职位详情</li>
            </ol>
          </nav>
          <h1 class="text-3xl md:text-4xl font-bold text-white mb-4">高级前端工程师</h1>
          <div class="flex flex-wrap justify-center gap-4 text-white/80 text-sm">
            <span>技术部</span>
            <span>·</span>
            <span>重庆</span>
            <span>·</span>
            <span>15K-25K</span>
            <span>·</span>
            <span>3-5年经验</span>
          </div>
        </div>
      </div>
    </section>

    <!-- 职位详情 -->
    <section class="py-16 md:py-24">
      <div class="container mx-auto px-4 sm:px-6 lg:px-8">
        <div class="max-w-3xl mx-auto">
          <div class="bg-white rounded-2xl p-8 md:p-12 shadow-lg">
            <!-- 岗位职责 -->
            <div class="mb-10">
              <h2 class="text-xl font-bold text-slate-900 mb-4 flex items-center">
                <span class="w-8 h-8 bg-blue-100 rounded-lg flex items-center justify-center mr-3">
                  <svg class="w-4 h-4 text-blue-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5H7a2 2 0 00-2 2v12a2 2 0 002 2h10a2 2 0 002-2V7a2 2 0 00-2-2h-2M9 5a2 2 0 002 2h2a2 2 0 002-2M9 5a2 2 0 012-2h2a2 2 0 012 2" />
                  </svg>
                </span>
                岗位职责
              </h2>
              <ul class="space-y-3 text-slate-600">
                <li class="flex items-start">
                  <span class="w-1.5 h-1.5 bg-blue-600 rounded-full mt-2 mr-3 flex-shrink-0"></span>
                  负责公司前端项目的架构设计和核心功能开发
                </li>
                <li class="flex items-start">
                  <span class="w-1.5 h-1.5 bg-blue-600 rounded-full mt-2 mr-3 flex-shrink-0"></span>
                  优化前端性能，提升用户体验
                </li>
                <li class="flex items-start">
                  <span class="w-1.5 h-1.5 bg-blue-600 rounded-full mt-2 mr-3 flex-shrink-0"></span>
                  参与技术选型，推动技术创新
                </li>
                <li class="flex items-start">
                  <span class="w-1.5 h-1.5 bg-blue-600 rounded-full mt-2 mr-3 flex-shrink-0"></span>
                  指导初中级工程师，提升团队技术水平
                </li>
              </ul>
            </div>

            <!-- 任职要求 -->
            <div class="mb-10">
              <h2 class="text-xl font-bold text-slate-900 mb-4 flex items-center">
                <span class="w-8 h-8 bg-blue-100 rounded-lg flex items-center justify-center mr-3">
                  <svg class="w-4 h-4 text-blue-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z" />
                  </svg>
                </span>
                任职要求
              </h2>
              <ul class="space-y-3 text-slate-600">
                <li class="flex items-start">
                  <span class="w-1.5 h-1.5 bg-blue-600 rounded-full mt-2 mr-3 flex-shrink-0"></span>
                  本科及以上学历，计算机相关专业
                </li>
                <li class="flex items-start">
                  <span class="w-1.5 h-1.5 bg-blue-600 rounded-full mt-2 mr-3 flex-shrink-0"></span>
                  3年以上前端开发经验，精通Vue3/React
                </li>
                <li class="flex items-start">
                  <span class="w-1.5 h-1.5 bg-blue-600 rounded-full mt-2 mr-3 flex-shrink-0"></span>
                  熟悉TypeScript、Webpack/Vite等工具
                </li>
                <li class="flex items-start">
                  <span class="w-1.5 h-1.5 bg-blue-600 rounded-full mt-2 mr-3 flex-shrink-0"></span>
                  具备良好的代码规范和文档编写习惯
                </li>
              </ul>
            </div>

            <!-- 薪资福利 -->
            <div class="mb-10">
              <h2 class="text-xl font-bold text-slate-900 mb-4 flex items-center">
                <span class="w-8 h-8 bg-blue-100 rounded-lg flex items-center justify-center mr-3">
                  <svg class="w-4 h-4 text-blue-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8c-1.657 0-3 .895-3 2s1.343 2 3 2 3 .895 3 2-1.343 2-3 2m0-8c1.11 0 2.08.402 2.599 1M12 8V7m0 1v8m0 0v1m0-1c-1.11 0-2.08-.402-2.599-1M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
                  </svg>
                </span>
                薪资福利
              </h2>
              <ul class="space-y-3 text-slate-600">
                <li class="flex items-start">
                  <span class="w-1.5 h-1.5 bg-blue-600 rounded-full mt-2 mr-3 flex-shrink-0"></span>
                  薪资范围：15K-25K，具体面议
                </li>
                <li class="flex items-start">
                  <span class="w-1.5 h-1.5 bg-blue-600 rounded-full mt-2 mr-3 flex-shrink-0"></span>
                  五险一金，带薪年假
                </li>
                <li class="flex items-start">
                  <span class="w-1.5 h-1.5 bg-blue-600 rounded-full mt-2 mr-3 flex-shrink-0"></span>
                  弹性工作制，周末双休
                </li>
                <li class="flex items-start">
                  <span class="w-1.5 h-1.5 bg-blue-600 rounded-full mt-2 mr-3 flex-shrink-0"></span>
                  定期团建，技术培训
                </li>
              </ul>
            </div>

            <!-- 申请方式 -->
            <div class="bg-blue-50 rounded-xl p-6 mb-8">
              <h3 class="font-bold text-slate-900 mb-2">申请方式</h3>
              <p class="text-slate-600 mb-4">请将简历发送至招聘邮箱，邮件标题请注明应聘职位</p>
              <div class="flex items-center gap-2 text-blue-600 font-medium">
                <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 8l7.89 5.26a2 2 0 002.22 0L21 8M5 19h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v10a2 2 0 002 2z" />
                </svg>
                hr@qiyun.com
              </div>
            </div>

            <!-- 按钮 -->
            <div class="flex flex-col sm:flex-row gap-4">
              <a 
                href="mailto:hr@qiyun.com?subject=应聘高级前端工程师" 
                class="flex-1 px-6 py-3 bg-blue-600 text-white font-medium rounded-lg hover:bg-blue-700 transition-colors text-center"
              >
                投递简历
              </a>
              <RouterLink 
                to="/careers" 
                class="flex-1 px-6 py-3 bg-slate-200 text-slate-700 font-medium rounded-lg hover:bg-slate-300 transition-colors text-center"
              >
                返回职位列表
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
import AppHeader from '@/components/layout/AppHeader.vue'
import AppFooter from '@/components/layout/AppFooter.vue'
</script>
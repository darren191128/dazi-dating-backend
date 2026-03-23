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
              <li><RouterLink to="/news" class="hover:text-white transition-colors">新闻动态</RouterLink></li>
              <li><span class="text-white/40">/</span></li>
              <li class="text-white">新闻详情</li>
            </ol>
          </nav>
          <h1 class="text-3xl md:text-4xl font-bold text-white mb-4">启云科技荣获2024年度优秀软件企业称号</h1>
          <div class="flex justify-center gap-4 text-white/80 text-sm">
            <span>公司新闻</span>
            <span>·</span>
            <span>2024-03-11</span>
          </div>
        </div>
      </div>
    </section>

    <!-- 文章内容 -->
    <section class="py-16 md:py-24">
      <div class="container mx-auto px-4 sm:px-6 lg:px-8">
        <div class="max-w-3xl mx-auto">
          <div class="bg-white rounded-2xl p-8 md:p-12 shadow-lg">
            <div class="aspect-video bg-slate-200 rounded-xl mb-8 overflow-hidden">
              <img src="https://picsum.photos/800/450?random=10" alt="新闻配图" class="w-full h-full object-cover" />
            </div>
            
            <div class="prose prose-slate max-w-none">
              <p class="text-lg text-slate-600 leading-relaxed mb-6">
                近日，在2024年度软件行业评选活动中，重庆启云网络科技有限公司凭借出色的技术实力和优质的服务质量，
                荣获"优秀软件企业"称号。这是对公司多年来坚持技术创新、服务客户的高度认可。
              </p>
              
              <h3 class="text-xl font-bold text-slate-900 mb-4">评选背景</h3>
              <p class="text-slate-600 leading-relaxed mb-6">
                本次评选活动由重庆市软件行业协会主办，旨在表彰在软件研发、技术创新、服务质量等方面表现突出的企业。
                评选过程严格，从企业资质、技术实力、项目案例、客户评价等多个维度进行综合评估。
              </p>
              
              <h3 class="text-xl font-bold text-slate-900 mb-4">公司优势</h3>
              <p class="text-slate-600 leading-relaxed mb-6">
                启云科技自成立以来，始终专注于软件定制开发领域，累计服务客户超过200家，交付项目100余个。
                公司拥有一支经验丰富的技术团队，精通小程序开发、APP开发、网站开发等多种技术栈，
                能够为客户提供从需求分析到上线运维的一站式服务。
              </p>
              
              <h3 class="text-xl font-bold text-slate-900 mb-4">未来展望</h3>
              <p class="text-slate-600 leading-relaxed">
                此次获奖是对启云科技的肯定，更是激励。未来，公司将继续秉承"让数字化更简单"的使命，
                不断提升技术实力和服务质量，为更多企业提供优质的软件开发服务，助力企业数字化转型。
              </p>
            </div>
            
            <!-- 分享 -->
            <div class="mt-12 pt-8 border-t border-slate-100">
              <div class="flex items-center justify-between">
                <span class="text-slate-500">分享到：</span>
                <div class="flex gap-4">
                  <button class="w-10 h-10 bg-green-500 rounded-lg flex items-center justify-center text-white hover:bg-green-600 transition-colors">
                    微
                  </button>
                  <button class="w-10 h-10 bg-red-500 rounded-lg flex items-center justify-center text-white hover:bg-red-600 transition-colors">
                    博
                  </button>
                  <button class="w-10 h-10 bg-slate-200 rounded-lg flex items-center justify-center text-slate-600 hover:bg-slate-300 transition-colors">
                    <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                      <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M8 16H6a2 2 0 01-2-2V6a2 2 0 012-2h8a2 2 0 012 2v2m-6 12h8a2 2 0 002-2v-8a2 2 0 00-2-2h-8a2 2 0 00-2 2v8a2 2 0 002 2z" />
                    </svg>
                  </button>
                </div>
              </div>
            </div>
          </div>
          
          <!-- 返回按钮 -->
          <div class="flex justify-center mt-8">
            <RouterLink 
              to="/news" 
              class="inline-flex items-center px-6 py-3 bg-slate-200 text-slate-700 font-medium rounded-lg hover:bg-slate-300 transition-colors"
            >
              <svg class="w-5 h-5 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M10 19l-7-7m0 0l7-7m-7 7h18" />
              </svg>
              返回新闻列表
            </RouterLink>
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
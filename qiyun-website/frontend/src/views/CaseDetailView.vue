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
              <li><RouterLink to="/cases" class="hover:text-white transition-colors">案例展示</RouterLink></li>
              <li><span class="text-white/40">/</span></li>
              <li class="text-white">案例详情</li>
            </ol>
          </nav>
          <h1 class="text-4xl md:text-5xl font-bold text-white mb-4">智慧商城小程序</h1>
          <div class="flex flex-wrap justify-center gap-4 text-white/80">
            <span class="px-4 py-1 bg-white/10 rounded-full text-sm">电商</span>
            <span class="px-4 py-1 bg-white/10 rounded-full text-sm">小程序</span>
            <span class="px-4 py-1 bg-white/10 rounded-full text-sm">3个月交付</span>
          </div>
        </div>
      </div>
    </section>

    <!-- 案例详情 -->
    <section class="py-16 md:py-24">
      <div class="container mx-auto px-4 sm:px-6 lg:px-8">
        <div class="max-w-4xl mx-auto">
          <!-- 项目信息卡片 -->
          <div class="bg-white rounded-2xl p-8 shadow-lg mb-12">
            <div class="grid grid-cols-2 md:grid-cols-4 gap-6">
              <div>
                <div class="text-slate-500 text-sm mb-1">客户名称</div>
                <div class="font-semibold text-slate-900">某知名零售品牌</div>
              </div>
              <div>
                <div class="text-slate-500 text-sm mb-1">项目周期</div>
                <div class="font-semibold text-slate-900">3个月</div>
              </div>
              <div>
                <div class="text-slate-500 text-sm mb-1">技术栈</div>
                <div class="font-semibold text-slate-900">微信小程序 + Java</div>
              </div>
              <div>
                <div class="text-slate-500 text-sm mb-1">项目规模</div>
                <div class="font-semibold text-slate-900">中型</div>
              </div>
            </div>
          </div>

          <!-- 项目背景 -->
          <div class="mb-12">
            <h2 class="text-2xl font-bold text-slate-900 mb-4">项目背景</h2>
            <p class="text-slate-600 leading-relaxed">
              客户是一家拥有50+线下门店的零售品牌，希望通过小程序实现线上线下一体化运营，
              提升会员粘性，增加复购率。主要需求包括：会员系统、积分商城、优惠券、
              在线下单、门店自提等功能。
            </p>
          </div>

          <!-- 解决方案 -->
          <div class="mb-12">
            <h2 class="text-2xl font-bold text-slate-900 mb-4">解决方案</h2>
            <div class="space-y-4">
              <div class="flex gap-4">
                <div class="w-8 h-8 bg-blue-100 rounded-lg flex items-center justify-center flex-shrink-0">
                  <span class="text-blue-600 font-bold">1</span>
                </div>
                <div>
                  <h3 class="font-semibold text-slate-900 mb-1">会员系统</h3>
                  <p class="text-slate-600">完善的会员等级体系，支持积分累积、等级权益、生日特权等功能</p>
                </div>
              </div>
              <div class="flex gap-4">
                <div class="w-8 h-8 bg-blue-100 rounded-lg flex items-center justify-center flex-shrink-0">
                  <span class="text-blue-600 font-bold">2</span>
                </div>
                <div>
                  <h3 class="font-semibold text-slate-900 mb-1">积分商城</h3>
                  <p class="text-slate-600">积分兑换商品、积分+现金混合支付，提升用户活跃度</p>
                </div>
              </div>
              <div class="flex gap-4">
                <div class="w-8 h-8 bg-blue-100 rounded-lg flex items-center justify-center flex-shrink-0">
                  <span class="text-blue-600 font-bold">3</span>
                </div>
                <div>
                  <h3 class="font-semibold text-slate-900 mb-1">营销工具</h3>
                  <p class="text-slate-600">优惠券、满减活动、限时折扣、拼团等多种营销玩法</p>
                </div>
              </div>
            </div>
          </div>

          <!-- 项目成果 -->
          <div class="mb-12">
            <h2 class="text-2xl font-bold text-slate-900 mb-4">项目成果</h2>
            <div class="grid grid-cols-1 md:grid-cols-3 gap-6">
              <div class="bg-blue-50 rounded-xl p-6 text-center">
                <div class="text-3xl font-bold text-blue-600 mb-2">10万+</div>
                <div class="text-slate-600">注册用户</div>
              </div>
              <div class="bg-blue-50 rounded-xl p-6 text-center">
                <div class="text-3xl font-bold text-blue-600 mb-2">30%</div>
                <div class="text-slate-600">复购率提升</div>
              </div>
              <div class="bg-blue-50 rounded-xl p-6 text-center">
                <div class="text-3xl font-bold text-blue-600 mb-2">50万+</div>
                <div class="text-slate-600">月交易额</div>
              </div>
            </div>
          </div>

          <!-- 客户评价 -->
          <div class="bg-slate-100 rounded-2xl p-8 mb-12">
            <div class="flex items-center mb-4">
              <svg v-for="i in 5" :key="i" class="w-5 h-5 text-yellow-400" fill="currentColor" viewBox="0 0 20 20">
                <path d="M9.049 2.927c.3-.921 1.603-.921 1.902 0l1.07 3.292a1 1 0 00.95.69h3.462c.969 0 1.371 1.24.588 1.81l-2.8 2.034a1 1 0 00-.364 1.118l1.07 3.292c.3.921-.755 1.688-1.54 1.118l-2.8-2.034a1 1 0 00-1.175 0l-2.8 2.034c-.784.57-1.838-.197-1.539-1.118l1.07-3.292a1 1 0 00-.364-1.118L2.98 8.72c-.783-.57-.38-1.81.588-1.81h3.461a1 1 0 00.951-.69l1.07-3.292z" />
              </svg>
            </div>
            <p class="text-slate-700 text-lg mb-4">"启云团队非常专业，项目按时交付，功能完全符合我们的需求。小程序上线后，我们的线上订单增长了很多！"</p>
            <div class="flex items-center">
              <div class="w-10 h-10 bg-blue-200 rounded-full flex items-center justify-center mr-3">
                <span class="text-blue-700 font-bold">李</span>
              </div>
              <div>
                <div class="font-semibold text-slate-900">李经理</div>
                <div class="text-slate-500 text-sm">运营总监</div>
              </div>
            </div>
          </div>

          <!-- 返回按钮 -->
          <div class="flex justify-center">
            <RouterLink 
              to="/cases" 
              class="inline-flex items-center px-6 py-3 bg-slate-200 text-slate-700 font-medium rounded-lg hover:bg-slate-300 transition-colors"
            >
              <svg class="w-5 h-5 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M10 19l-7-7m0 0l7-7m-7 7h18" />
              </svg>
              返回案例列表
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
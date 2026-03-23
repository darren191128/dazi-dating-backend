<template>
  <div class="min-h-screen">
    <!-- 导航栏 -->
    <AppHeader />
    
    <!-- Hero区域 -->
    <section class="relative min-h-screen flex items-center justify-center bg-gradient-to-br from-slate-900 via-blue-900 to-blue-800 overflow-hidden">
      <!-- 背景装饰 -->
      <div class="absolute inset-0">
        <div class="absolute top-20 left-10 w-96 h-96 bg-blue-500/20 rounded-full blur-3xl animate-pulse"></div>
        <div class="absolute bottom-20 right-10 w-80 h-80 bg-cyan-500/20 rounded-full blur-3xl animate-pulse" style="animation-delay: 1s;"></div>
        <div class="absolute top-1/2 left-1/2 -translate-x-1/2 -translate-y-1/2 w-[600px] h-[600px] bg-blue-600/10 rounded-full blur-3xl"></div>
      </div>
      
      <!-- 网格背景 -->
      <div class="absolute inset-0 bg-[url('data:image/svg+xml,%3Csvg width=\"60\" height=\"60\" viewBox=\"0 0 60 60\" xmlns=\"http://www.w3.org/2000/svg\"%3E%3Cg fill=\"none\" fill-rule=\"evenodd\"%3E%3Cg fill=\"%23ffffff\" fill-opacity=\"0.03\"%3E%3Cpath d=\"M36 34v-4h-2v4h-4v2h4v4h2v-4h4v-2h-4zm0-30V0h-2v4h-4v2h4v4h2V6h4V4h-4zM6 34v-4H4v4H0v2h4v4h2v-4h4v-2H6zM6 4V0H4v4H0v2h4v4h2V6h4V4H6z\"/%3E%3C/g%3E%3C/g%3E%3C/svg%3E')] opacity-20"></div>
      
      <div class="container mx-auto px-4 sm:px-6 lg:px-8 relative z-10">
        <div class="text-center max-w-4xl mx-auto">
          <!-- 标签 -->
          <div class="inline-flex items-center px-4 py-2 bg-white/10 backdrop-blur-sm rounded-full border border-white/20 mb-8 animate-fade-in">
            <span class="w-2 h-2 bg-cyan-400 rounded-full mr-2 animate-pulse"></span>
            <span class="text-white/80 text-sm">重庆本土专业开发团队</span>
          </div>
          
          <!-- 主标题 -->
          <h1 class="text-4xl sm:text-5xl md:text-6xl lg:text-7xl font-bold text-white mb-6 leading-tight animate-slide-up">
            专业的软件定制<br/>
            <span class="bg-gradient-to-r from-cyan-400 to-blue-400 bg-clip-text text-transparent">开发服务商</span>
          </h1>
          
          <!-- 副标题 -->
          <p class="text-lg sm:text-xl text-white/70 mb-10 max-w-2xl mx-auto animate-slide-up" style="animation-delay: 0.1s;">
            小程序 · APP · 网站 一站式开发解决方案<br/>
            让数字化更简单，助力企业转型升级
          </p>
          
          <!-- CTA按钮 -->
          <div class="flex flex-col sm:flex-row gap-4 justify-center animate-slide-up" style="animation-delay: 0.2s;">
            <RouterLink 
              to="/contact" 
              class="group relative px-8 py-4 bg-blue-600 hover:bg-blue-500 text-white font-semibold rounded-xl transition-all duration-300 overflow-hidden shadow-lg shadow-blue-600/30 hover:shadow-blue-500/40 hover:-translate-y-1"
            >
              <span class="relative z-10 flex items-center justify-center gap-2">
                免费咨询
                <svg class="w-5 h-5 group-hover:translate-x-1 transition-transform" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17 8l4 4m0 0l-4 4m4-4H3" />
                </svg>
              </span>
              <div class="absolute inset-0 bg-gradient-to-r from-blue-500 to-cyan-500 opacity-0 group-hover:opacity-100 transition-opacity"></div>
            </RouterLink>
            <RouterLink 
              to="/cases" 
              class="px-8 py-4 bg-white/10 hover:bg-white/20 backdrop-blur-sm text-white font-semibold rounded-xl border border-white/30 transition-all duration-300 hover:-translate-y-1"
            >
              查看案例
            </RouterLink>
          </div>
          
          <!-- 信任指标 -->
          <div class="flex flex-wrap justify-center gap-8 mt-12 text-white/60 text-sm animate-fade-in" style="animation-delay: 0.4s;">
            <div class="flex items-center gap-2">
              <svg class="w-5 h-5 text-cyan-400" fill="currentColor" viewBox="0 0 20 20">
                <path fill-rule="evenodd" d="M10 18a8 8 0 100-16 8 8 0 000 16zm3.707-9.293a1 1 0 00-1.414-1.414L9 10.586 7.707 9.293a1 1 0 00-1.414 1.414l2 2a1 1 0 001.414 0l4-4z" clip-rule="evenodd" />
              </svg>
              <span>5年+行业经验</span>
            </div>
            <div class="flex items-center gap-2">
              <svg class="w-5 h-5 text-cyan-400" fill="currentColor" viewBox="0 0 20 20">
                <path fill-rule="evenodd" d="M10 18a8 8 0 100-16 8 8 0 000 16zm3.707-9.293a1 1 0 00-1.414-1.414L9 10.586 7.707 9.293a1 1 0 00-1.414 1.414l2 2a1 1 0 001.414 0l4-4z" clip-rule="evenodd" />
              </svg>
              <span>200+服务客户</span>
            </div>
            <div class="flex items-center gap-2">
              <svg class="w-5 h-5 text-cyan-400" fill="currentColor" viewBox="0 0 20 20">
                <path fill-rule="evenodd" d="M10 18a8 8 0 100-16 8 8 0 000 16zm3.707-9.293a1 1 0 00-1.414-1.414L9 10.586 7.707 9.293a1 1 0 00-1.414 1.414l2 2a1 1 0 001.414 0l4-4z" clip-rule="evenodd" />
              </svg>
              <span>98%客户满意度</span>
            </div>
          </div>
        </div>
      </div>
      
      <!-- 滚动提示 -->
      <div class="absolute bottom-8 left-1/2 -translate-x-1/2 animate-bounce">
        <svg class="w-6 h-6 text-white/50" fill="none" stroke="currentColor" viewBox="0 0 24 24">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 14l-7 7m0 0l-7-7m7 7V3" />
        </svg>
      </div>
    </section>

    <!-- 服务概览 -->
    <section class="py-20 md:py-32 bg-slate-50">
      <div class="container mx-auto px-4 sm:px-6 lg:px-8">
        <div class="text-center mb-16">
          <span class="text-blue-600 font-semibold text-sm uppercase tracking-wider">Our Services</span>
          <h2 class="text-3xl md:text-4xl font-bold text-slate-900 mt-2 mb-4">我们的服务</h2>
          <p class="text-slate-600 max-w-2xl mx-auto">全方位满足您的数字化需求，提供从需求分析到上线运维的一站式服务</p>
        </div>
        
        <div class="grid grid-cols-1 md:grid-cols-3 gap-8">
          <!-- 小程序开发 -->
          <div class="group bg-white rounded-2xl p-8 shadow-lg hover:shadow-2xl transition-all duration-300 hover:-translate-y-2 border border-slate-100">
            <div class="w-16 h-16 bg-gradient-to-br from-blue-500 to-blue-600 rounded-xl flex items-center justify-center mb-6 group-hover:scale-110 transition-transform">
              <svg class="w-8 h-8 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 18h.01M8 21h8a2 2 0 002-2V5a2 2 0 00-2-2H8a2 2 0 00-2 2v14a2 2 0 002 2z" />
              </svg>
            </div>
            <h3 class="text-xl font-bold text-slate-900 mb-3">小程序开发</h3>
            <p class="text-slate-600 mb-6">微信/支付宝/抖音小程序定制开发，快速触达用户，降低获客成本</p>
            <RouterLink to="/services" class="inline-flex items-center text-blue-600 font-semibold hover:text-blue-700 transition-colors">
              了解详情
              <svg class="w-4 h-4 ml-2 group-hover:translate-x-1 transition-transform" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5l7 7-7 7" />
              </svg>
            </RouterLink>
          </div>
          
          <!-- APP开发 -->
          <div class="group bg-white rounded-2xl p-8 shadow-lg hover:shadow-2xl transition-all duration-300 hover:-translate-y-2 border border-slate-100">
            <div class="w-16 h-16 bg-gradient-to-br from-cyan-500 to-cyan-600 rounded-xl flex items-center justify-center mb-6 group-hover:scale-110 transition-transform">
              <svg class="w-8 h-8 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 18h.01M8 21h8a2 2 0 002-2V5a2 2 0 00-2-2H8a2 2 0 00-2 2v14a2 2 0 002 2z" />
              </svg>
            </div>
            <h3 class="text-xl font-bold text-slate-900 mb-3">APP开发</h3>
            <p class="text-slate-600 mb-6">iOS/Android原生开发、Flutter跨平台开发，打造极致用户体验</p>
            <RouterLink to="/services" class="inline-flex items-center text-cyan-600 font-semibold hover:text-cyan-700 transition-colors">
              了解详情
              <svg class="w-4 h-4 ml-2 group-hover:translate-x-1 transition-transform" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5l7 7-7 7" />
              </svg>
            </RouterLink>
          </div>
          
          <!-- 网站开发 -->
          <div class="group bg-white rounded-2xl p-8 shadow-lg hover:shadow-2xl transition-all duration-300 hover:-translate-y-2 border border-slate-100">
            <div class="w-16 h-16 bg-gradient-to-br from-indigo-500 to-indigo-600 rounded-xl flex items-center justify-center mb-6 group-hover:scale-110 transition-transform">
              <svg class="w-8 h-8 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M21 12a9 9 0 01-9 9m9-9a9 9 0 00-9-9m9 9H3m9 9a9 9 0 01-9-9m9 9c1.657 0 3-4.03 3-9s-1.343-9-3-9m0 18c-1.657 0-3-4.03-3-9s1.343-9 3-9m-9 9a9 9 0 019-9" />
              </svg>
            </div>
            <h3 class="text-xl font-bold text-slate-900 mb-3">网站开发</h3>
            <p class="text-slate-600 mb-6">企业官网、电商平台、管理系统定制开发，响应式设计，SEO优化</p>
            <RouterLink to="/services" class="inline-flex items-center text-indigo-600 font-semibold hover:text-indigo-700 transition-colors">
              了解详情
              <svg class="w-4 h-4 ml-2 group-hover:translate-x-1 transition-transform" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5l7 7-7 7" />
              </svg>
            </RouterLink>
          </div>
        </div>
      </div>
    </section>

    <!-- 精选案例 -->
    <section class="py-20 md:py-32 bg-white">
      <div class="container mx-auto px-4 sm:px-6 lg:px-8">
        <div class="flex flex-col md:flex-row md:items-end md:justify-between mb-12">
          <div>
            <span class="text-blue-600 font-semibold text-sm uppercase tracking-wider">Portfolio</span>
            <h2 class="text-3xl md:text-4xl font-bold text-slate-900 mt-2">精选案例</h2>
            <p class="text-slate-600 mt-2">用实力说话，见证每一次成功交付</p>
          </div>
          <RouterLink to="/cases" class="mt-4 md:mt-0 inline-flex items-center text-blue-600 font-semibold hover:text-blue-700 transition-colors">
            查看更多案例
            <svg class="w-5 h-5 ml-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17 8l4 4m0 0l-4 4m4-4H3" />
            </svg>
          </RouterLink>
        </div>
        
        <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-8">
          <div v-for="(item, index) in cases" :key="index" class="group cursor-pointer">
            <div class="relative overflow-hidden rounded-2xl mb-4">
              <div class="aspect-[4/3] bg-slate-200">
                <img :src="item.image" :alt="item.title" class="w-full h-full object-cover group-hover:scale-110 transition-transform duration-500" />
              </div>
              <div class="absolute inset-0 bg-gradient-to-t from-slate-900/80 via-transparent to-transparent opacity-0 group-hover:opacity-100 transition-opacity duration-300"></div>
              <div class="absolute bottom-4 left-4 right-4 transform translate-y-4 group-hover:translate-y-0 opacity-0 group-hover:opacity-100 transition-all duration-300">
                <span class="text-white text-sm font-medium">查看详情 →</span>
              </div>
            </div>
            <div class="flex gap-2 mb-2">
              <span class="px-3 py-1 bg-blue-100 text-blue-700 text-xs font-medium rounded-full">{{ item.tag1 }}</span>
              <span class="px-3 py-1 bg-cyan-100 text-cyan-700 text-xs font-medium rounded-full">{{ item.tag2 }}</span>
            </div>
            <h3 class="text-lg font-bold text-slate-900 group-hover:text-blue-600 transition-colors">{{ item.title }}</h3>
            <p class="text-slate-500 text-sm">{{ item.client }}</p>
          </div>
        </div>
      </div>
    </section>

    <!-- 数据统计 -->
    <section class="py-20 md:py-24 bg-gradient-to-r from-blue-900 to-blue-800">
      <div class="container mx-auto px-4 sm:px-6 lg:px-8">
        <div class="grid grid-cols-2 md:grid-cols-4 gap-8 text-center">
          <div v-for="(stat, index) in stats" :key="index" class="text-white">
            <div class="text-4xl md:text-5xl font-bold mb-2">{{ stat.value }}</div>
            <div class="text-white/70">{{ stat.label }}</div>
          </div>
        </div>
      </div>
    </section>

    <!-- 客户评价 -->
    <section class="py-20 md:py-32 bg-slate-50">
      <div class="container mx-auto px-4 sm:px-6 lg:px-8">
        <div class="text-center mb-12">
          <span class="text-blue-600 font-semibold text-sm uppercase tracking-wider">Testimonials</span>
          <h2 class="text-3xl md:text-4xl font-bold text-slate-900 mt-2">客户评价</h2>
        </div>
        
        <div class="max-w-4xl mx-auto">
          <div class="bg-white rounded-2xl p-8 md:p-12 shadow-xl">
            <div class="flex items-center mb-6">
              <svg v-for="i in 5" :key="i" class="w-5 h-5 text-yellow-400" fill="currentColor" viewBox="0 0 20 20">
                <path d="M9.049 2.927c.3-.921 1.603-.921 1.902 0l1.07 3.292a1 1 0 00.95.69h3.462c.969 0 1.371 1.24.588 1.81l-2.8 2.034a1 1 0 00-.364 1.118l1.07 3.292c.3.921-.755 1.688-1.54 1.118l-2.8-2.034a1 1 0 00-1.175 0l-2.8 2.034c-.784.57-1.838-.197-1.539-1.118l1.07-3.292a1 1 0 00-.364-1.118L2.98 8.72c-.783-.57-.38-1.81.588-1.81h3.461a1 1 0 00.951-.69l1.07-3.292z" />
              </svg>
            </div>
            <p class="text-lg md:text-xl text-slate-700 mb-6 leading-relaxed">
              "启云团队非常专业，从需求沟通到最终交付，整个过程都很顺畅。项目按时保质完成，后期维护也很及时。强烈推荐给需要软件开发的企业！"
            </p>
            <div class="flex items-center">
              <div class="w-12 h-12 bg-blue-100 rounded-full flex items-center justify-center mr-4">
                <span class="text-blue-600 font-bold">张</span>
              </div>
              <div>
                <div class="font-semibold text-slate-900">张先生</div>
                <div class="text-slate-500 text-sm">某电商公司 CEO</div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </section>

    <!-- 合作伙伴 -->
    <section class="py-16 bg-white border-t border-slate-100">
      <div class="container mx-auto px-4 sm:px-6 lg:px-8">
        <p class="text-center text-slate-500 text-sm mb-8">值得信赖的合作伙伴</p>
        <div class="flex flex-wrap justify-center items-center gap-8 md:gap-16 opacity-50">
          <div v-for="i in 6" :key="i" class="w-24 h-12 bg-slate-200 rounded flex items-center justify-center text-slate-400 text-xs">
            LOGO {{ i }}
          </div>
        </div>
      </div>
    </section>

    <!-- CTA区域 -->
    <section class="py-20 md:py-32 bg-gradient-to-br from-slate-900 via-blue-900 to-blue-800">
      <div class="container mx-auto px-4 sm:px-6 lg:px-8 text-center">
        <h2 class="text-3xl md:text-4xl font-bold text-white mb-4">准备好开始您的项目了吗？</h2>
        <p class="text-white/70 text-lg mb-8 max-w-2xl mx-auto">联系我们，获取专业的软件开发咨询服务，让数字化更简单</p>
        <RouterLink 
          to="/contact" 
          class="inline-flex items-center px-8 py-4 bg-white text-blue-900 font-semibold rounded-xl hover:bg-blue-50 transition-colors shadow-lg"
        >
          立即咨询
          <svg class="w-5 h-5 ml-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17 8l4 4m0 0l-4 4m4-4H3" />
          </svg>
        </RouterLink>
      </div>
    </section>

    <!-- 页脚 -->
    <AppFooter />
  </div>
</template>

<script setup lang="ts">
import AppHeader from '@/components/layout/AppHeader.vue'
import AppFooter from '@/components/layout/AppFooter.vue'

const cases = [
  {
    title: '智慧商城小程序',
    client: '某知名零售品牌',
    tag1: '电商',
    tag2: '小程序',
    image: 'https://images.unsplash.com/photo-1556742049-0cfed4f6a45d?w=600&h=450&fit=crop'
  },
  {
    title: '企业管理系统',
    client: '某制造企业',
    tag1: '企业',
    tag2: '网站',
    image: 'https://images.unsplash.com/photo-1460925895917-afdab827c52f?w=600&h=450&fit=crop'
  },
  {
    title: '在线教育APP',
    client: '某教育机构',
    tag1: '教育',
    tag2: 'APP',
    image: 'https://images.unsplash.com/photo-1501504905252-473c47e087f8?w=600&h=450&fit=crop'
  },
  {
    title: '餐饮点餐系统',
    client: '某连锁餐饮品牌',
    tag1: '餐饮',
    tag2: '小程序',
    image: 'https://images.unsplash.com/photo-1555396273-367ea4eb4db5?w=600&h=450&fit=crop'
  },
  {
    title: '医疗预约平台',
    client: '某医疗机构',
    tag1: '医疗',
    tag2: '网站',
    image: 'https://images.unsplash.com/photo-1576091160399-112ba8d25d1d?w=600&h=450&fit=crop'
  },
  {
    title: '物流追踪APP',
    client: '某物流公司',
    tag1: '物流',
    tag2: 'APP',
    image: 'https://images.unsplash.com/photo-1586528116311-ad8dd3c8310d?w=600&h=450&fit=crop'
  }
]

const stats = [
  { value: '200+', label: '服务客户' },
  { value: '98%', label: '客户满意度' },
  { value: '50+', label: '成功案例' },
  { value: '5年+', label: '行业经验' }
]
</script>

<style scoped>
@keyframes fade-in {
  from {
    opacity: 0;
    transform: translateY(20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

@keyframes slide-up {
  from {
    opacity: 0;
    transform: translateY(30px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.animate-fade-in {
  animation: fade-in 0.8s ease-out forwards;
}

.animate-slide-up {
  animation: slide-up 0.8s ease-out forwards;
  opacity: 0;
}
</style>
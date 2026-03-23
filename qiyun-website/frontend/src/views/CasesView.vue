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
              <li class="text-white">案例展示</li>
            </ol>
          </nav>
          <h1 class="text-4xl md:text-5xl font-bold text-white mb-4">精选案例</h1>
          <p class="text-xl text-white/80 max-w-2xl mx-auto">用实力说话，见证每一次成功交付</p>
        </div>
      </div>
    </section>

    <!-- 筛选器 -->
    <section class="py-8 bg-white border-b border-slate-100">
      <div class="container mx-auto px-4 sm:px-6 lg:px-8">
        <div class="flex flex-wrap gap-4">
          <div class="flex items-center gap-2">
            <span class="text-slate-500 text-sm">行业：</span>
            <div class="flex flex-wrap gap-2">
              <button 
                v-for="cat in categories" 
                :key="cat"
                @click="activeCategory = cat"
                :class="['px-4 py-2 rounded-lg text-sm font-medium transition-colors', activeCategory === cat ? 'bg-blue-600 text-white' : 'bg-slate-100 text-slate-600 hover:bg-slate-200']"
              >
                {{ cat }}
              </button>
            </div>
          </div>
        </div>
      </div>
    </section>

    <!-- 案例列表 -->
    <section class="py-16 md:py-24">
      <div class="container mx-auto px-4 sm:px-6 lg:px-8">
        <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-8">
          <RouterLink 
            v-for="(item, index) in filteredCases" 
            :key="index"
            :to="`/cases/${item.id}`"
            class="group bg-white rounded-2xl overflow-hidden shadow-lg hover:shadow-2xl transition-all duration-300 hover:-translate-y-2"
          >
            <div class="relative overflow-hidden">
              <div class="aspect-[4/3]">
                <img :src="item.image" :alt="item.title" class="w-full h-full object-cover group-hover:scale-110 transition-transform duration-500" />
              </div>
              <div class="absolute top-4 left-4 flex gap-2">
                <span class="px-3 py-1 bg-white/90 backdrop-blur-sm text-blue-700 text-xs font-medium rounded-full">{{ item.industry }}</span>
                <span class="px-3 py-1 bg-white/90 backdrop-blur-sm text-cyan-700 text-xs font-medium rounded-full">{{ item.type }}</span>
              </div>
            </div>
            <div class="p-6">
              <h3 class="text-xl font-bold text-slate-900 mb-2 group-hover:text-blue-600 transition-colors">{{ item.title }}</h3>
              <p class="text-slate-500 text-sm mb-4">{{ item.client }}</p>
              <p class="text-slate-600 text-sm line-clamp-2">{{ item.description }}</p>
              <div class="mt-4 flex items-center text-blue-600 font-medium text-sm">
                查看详情
                <svg class="w-4 h-4 ml-2 group-hover:translate-x-1 transition-transform" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5l7 7-7 7" />
                </svg>
              </div>
            </div>
          </RouterLink>
        </div>
        
        <!-- 加载更多 -->
        <div class="text-center mt-12">
          <button class="px-8 py-3 bg-white border border-slate-200 text-slate-600 font-medium rounded-lg hover:bg-slate-50 transition-colors">
            加载更多案例
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

const categories = ['全部', '电商', '教育', '医疗', '餐饮', '物流', '企业']
const activeCategory = ref('全部')

const cases = [
  {
    id: 1,
    title: '智慧商城小程序',
    client: '某知名零售品牌',
    industry: '电商',
    type: '小程序',
    description: '为零售品牌打造的全渠道智慧商城，支持线上线下一体化运营，会员管理、积分商城、优惠券系统等功能齐全。',
    image: 'https://images.unsplash.com/photo-1556742049-0cfed4f6a45d?w=600&h=450&fit=crop'
  },
  {
    id: 2,
    title: '企业管理系统',
    client: '某制造企业',
    industry: '企业',
    type: '网站',
    description: '定制化ERP系统，涵盖生产管理、库存管理、销售管理、财务管理等模块，提升企业运营效率。',
    image: 'https://images.unsplash.com/photo-1460925895917-afdab827c52f?w=600&h=450&fit=crop'
  },
  {
    id: 3,
    title: '在线教育APP',
    client: '某教育机构',
    industry: '教育',
    type: 'APP',
    description: '支持直播授课、录播课程、在线测试、作业提交等功能，服务超过10万学员。',
    image: 'https://images.unsplash.com/photo-1501504905252-473c47e087f8?w=600&h=450&fit=crop'
  },
  {
    id: 4,
    title: '餐饮点餐系统',
    client: '某连锁餐饮品牌',
    industry: '餐饮',
    type: '小程序',
    description: '扫码点餐、在线支付、会员积分、优惠券发放，帮助餐饮企业实现数字化转型。',
    image: 'https://images.unsplash.com/photo-1555396273-367ea4eb4db5?w=600&h=450&fit=crop'
  },
  {
    id: 5,
    title: '医疗预约平台',
    client: '某医疗机构',
    industry: '医疗',
    type: '网站',
    description: '在线预约挂号、报告查询、健康档案管理，优化就医流程，提升患者体验。',
    image: 'https://images.unsplash.com/photo-1576091160399-112ba8d25d1d?w=600&h=450&fit=crop'
  },
  {
    id: 6,
    title: '物流追踪APP',
    client: '某物流公司',
    industry: '物流',
    type: 'APP',
    description: '实时物流追踪、智能路线规划、电子签收，提升物流效率和客户满意度。',
    image: 'https://images.unsplash.com/photo-1586528116311-ad8dd3c8310d?w=600&h=450&fit=crop'
  }
]

const filteredCases = computed(() => {
  if (activeCategory.value === '全部') return cases
  return cases.filter(c => c.industry === activeCategory.value)
})
</script>
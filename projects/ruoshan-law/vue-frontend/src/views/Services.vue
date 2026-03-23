<template>
  <div class="min-h-screen">
    <Header />
    <main class="pt-16">
      <section class="py-20 bg-gray-50">
        <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div class="text-center mb-16">
            <h1 class="section-title">服务领域</h1>
            <p class="section-subtitle">全方位专业法律服务，满足您的各类法律需求</p>
          </div>
          
          <!-- Service Categories -->
          <div class="grid grid-cols-1 md:grid-cols-2 gap-8">
            <div 
              v-for="service in services" 
              :key="service.id"
              class="bg-white rounded-xl p-8 shadow-sm hover:shadow-lg transition-all duration-300"
            >
              <div class="flex items-start">
                <div class="w-16 h-16 bg-primary/10 rounded-xl flex items-center justify-center mr-6 flex-shrink-0">
                  <svg class="w-8 h-8 text-primary" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" :d="service.icon"/>
                  </svg>
                </div>
                <div>
                  <h3 class="text-xl font-bold text-primary mb-3">{{ service.name }}</h3>
                  <p class="text-gray-600 mb-4">{{ service.description }}</p>
                  <ul class="space-y-2">
                    <li 
                      v-for="item in service.items" 
                      :key="item"
                      class="flex items-center text-sm text-gray-700"
                    >
                      <svg class="w-4 h-4 text-accent mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 13l4 4L19 7"/>
                      </svg>
                      {{ item }}
                    </li>
                  </ul>
                </div>
              </div>
            </div>
          </div>
          
          <!-- Service Process -->
          <div class="mt-20">
            <h2 class="text-2xl font-bold text-primary text-center mb-12">服务流程</h2>
            <div class="flex flex-col md:flex-row justify-between items-center space-y-8 md:space-y-0 md:space-x-8">
              <div 
                v-for="(step, index) in process" 
                :key="index"
                class="flex flex-col items-center text-center w-full md:w-1/4"
              >
                <div class="w-16 h-16 bg-accent rounded-full flex items-center justify-center text-white text-2xl font-bold mb-4">
                  {{ index + 1 }}
                </div>
                <h3 class="text-lg font-semibold text-primary mb-2">{{ step.title }}</h3>
                <p class="text-gray-600 text-sm">{{ step.description }}</p>
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

const services = [
  {
    id: 1,
    name: '民商事诉讼',
    description: '专业代理各类民商事纠纷案件，维护当事人合法权益',
    icon: 'M3 6l3 1m0 0l-3 9a5.002 5.002 0 006.001 0M6 7l3 9M6 7l6-2m6 2l3-1m-3 1l-3 9a5.002 5.002 0 006.001 0M18 7l3 9m-3-9l-6-2m0-2v2m0 16V5m0 16H9m3 0h3',
    items: ['合同纠纷', '债权债务', '房产纠纷', '侵权责任']
  },
  {
    id: 2,
    name: '刑事辩护',
    description: '为犯罪嫌疑人、被告人提供专业辩护服务',
    icon: 'M9 12l2 2 4-4m5.618-4.016A11.955 11.955 0 0112 2.944a11.955 11.955 0 01-8.618 3.04A12.02 12.02 0 003 9c0 5.591 3.824 10.29 9 11.622 5.176-1.332 9-6.03 9-11.622 0-1.042-.133-2.052-.382-3.016z',
    items: ['侦查阶段辩护', '审查起诉辩护', '审判阶段辩护', '刑事合规']
  },
  {
    id: 3,
    name: '知识产权',
    description: '全方位知识产权保护与维权服务',
    icon: 'M9.663 17h4.673M12 3v1m6.364 1.636l-.707.707M21 12h-1M4 12H3m3.343-5.657l-.707-.707m2.828 9.9a5 5 0 117.072 0l-.548.547A3.374 3.374 0 0014 18.469V19a2 2 0 11-4 0v-.531c0-.895-.356-1.754-.988-2.386l-.548-.547z',
    items: ['商标注册', '专利申请', '著作权保护', '侵权维权']
  },
  {
    id: 4,
    name: '公司法务',
    description: '为企业提供全方位法律顾问服务',
    icon: 'M19 21V5a2 2 0 00-2-2H7a2 2 0 00-2 2v16m14 0h2m-2 0h-5m-9 0H3m2 0h5M9 7h1m-1 4h1m4-4h1m-1 4h1m-5 10v-5a1 1 0 011-1h2a1 1 0 011 1v5m-4 0h4',
    items: ['合同审查', '股权设计', '劳动人事', '合规管理']
  }
]

const process = [
  { title: '咨询预约', description: '通过电话或在线方式预约咨询' },
  { title: '案件评估', description: '专业律师评估案件情况和可行性' },
  { title: '委托签约', description: '签订委托协议，明确双方权利义务' },
  { title: '案件办理', description: '专业团队全程跟进案件进展' }
]
</script>

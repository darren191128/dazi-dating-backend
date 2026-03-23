<template>
  <div class="min-h-screen">
    <Header />
    <main class="pt-16">
      <section class="py-20 bg-gray-50">
        <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div class="text-center mb-16">
            <h1 class="section-title">成功案例</h1>
            <p class="section-subtitle">专业实力，赢得客户信赖</p>
          </div>
          
          <!-- Filter -->
          <div class="flex flex-wrap justify-center gap-4 mb-12">
            <button 
              v-for="filter in filters" 
              :key="filter"
              @click="activeFilter = filter"
              class="px-6 py-2 rounded-full transition-all duration-300"
              :class="activeFilter === filter ? 'bg-primary text-white' : 'bg-white text-gray-700 hover:bg-gray-100'"
            >
              {{ filter }}
            </button>
          </div>
          
          <!-- Cases Grid -->
          <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-8">
            <article 
              v-for="caseItem in filteredCases" 
              :key="caseItem.id"
              class="bg-white rounded-xl overflow-hidden shadow-sm hover:shadow-lg transition-all duration-300"
            >
              <div class="p-6">
                <div class="flex items-center justify-between mb-4">
                  <span class="px-3 py-1 bg-accent/10 text-accent text-sm rounded-full">
                    {{ caseItem.type }}
                  </span>
                  <span class="text-gray-400 text-sm">{{ caseItem.date }}</span>
                </div>
                <h3 class="text-lg font-bold text-primary mb-3">{{ caseItem.title }}</h3>
                <p class="text-gray-600 text-sm mb-4 line-clamp-3">{{ caseItem.summary }}</p>
                <div class="bg-gray-50 rounded-lg p-4 mb-4">
                  <div class="text-sm text-gray-600 mb-1">案件结果</div>
                  <div class="text-accent font-semibold">{{ caseItem.result }}</div>
                </div>
                <div class="flex items-center justify-between pt-4 border-t">
                  <span class="text-sm text-gray-500">承办律师: {{ caseItem.lawyer }}</span>
                  <button class="text-primary text-sm font-medium hover:underline">
                    查看详情
                  </button>
                </div>
              </div>
            </article>
          </div>
        </div>
      </section>
    </main>
    <Footer />
  </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'
import Header from '../components/Header.vue'
import Footer from '../components/Footer.vue'

const activeFilter = ref('全部')

const filters = ['全部', '民商事诉讼', '刑事辩护', '知识产权', '公司法务']

const cases = ref([
  {
    id: 1,
    title: '某上市公司股权纠纷案',
    type: '公司法务',
    summary: '成功帮助客户解决股权纠纷，维护股东合法权益，涉案金额5000万元。通过专业的法律分析和谈判技巧，最终达成和解协议。',
    result: '和解成功，为客户挽回损失3000万元',
    lawyer: '张律师',
    date: '2024-01'
  },
  {
    id: 2,
    title: '某知名企业商标维权案',
    type: '知识产权',
    summary: '代理知名企业进行商标维权，成功制止侵权行为并获得赔偿。涉及跨区域侵权调查和法律诉讼。',
    result: '胜诉，获赔500万元',
    lawyer: '王律师',
    date: '2024-02'
  },
  {
    id: 3,
    title: '某合同纠纷案',
    type: '民商事诉讼',
    summary: '代理客户处理复杂合同纠纷，通过调解方式快速解决争议，避免了长期诉讼带来的时间和经济成本。',
    result: '调解成功，合同继续履行',
    lawyer: '李律师',
    date: '2024-03'
  },
  {
    id: 4,
    title: '某经济犯罪辩护案',
    type: '刑事辩护',
    summary: '为涉嫌经济犯罪的被告人提供辩护，通过充分的证据分析和法律论证，成功为当事人争取从轻处罚。',
    result: '从轻处罚，缓刑执行',
    lawyer: '李律师',
    date: '2024-02'
  },
  {
    id: 5,
    title: '某专利侵权案',
    type: '知识产权',
    summary: '代理科技公司进行专利维权，成功制止竞争对手的侵权行为，维护了客户的知识产权。',
    result: '胜诉，停止侵权并赔偿',
    lawyer: '王律师',
    date: '2024-01'
  },
  {
    id: 6,
    title: '某企业并购重组案',
    type: '公司法务',
    summary: '为某大型企业的并购重组提供全程法律服务，包括尽职调查、交易结构设计、合同起草等。',
    result: '交易顺利完成，金额2亿元',
    lawyer: '赵律师',
    date: '2023-12'
  }
])

const filteredCases = computed(() => {
  if (activeFilter.value === '全部') return cases.value
  return cases.value.filter(c => c.type === activeFilter.value)
})
</script>

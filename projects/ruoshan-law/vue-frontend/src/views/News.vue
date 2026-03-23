<template>
  <div class="min-h-screen">
    <Header />
    <main class="pt-16">
      <section class="py-20 bg-gray-50">
        <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div class="text-center mb-16">
            <h1 class="section-title">新闻资讯</h1>
            <p class="section-subtitle">了解最新法律动态和律所新闻</p>
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
          
          <!-- News Grid -->
          <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-8">
            <article 
              v-for="news in filteredNews" 
              :key="news.id"
              class="bg-white rounded-xl overflow-hidden shadow-sm hover:shadow-lg transition-all duration-300"
            >
              <div class="aspect-w-16 aspect-h-9 bg-gray-200">
                <img 
                  :src="news.image || '/default-news.jpg'" 
                  :alt="news.title"
                  class="w-full h-full object-cover"
                />
              </div>
              <div class="p-6">
                <div class="flex items-center text-sm text-gray-500 mb-2">
                  <span class="px-2 py-1 bg-primary/10 text-primary rounded">{{ news.category }}</span>
                  <span class="mx-2">•</span>
                  <span>{{ news.date }}</span>
                </div>
                <h3 class="text-lg font-bold text-primary mb-2 line-clamp-2 hover:text-accent transition-colors cursor-pointer">
                  {{ news.title }}
                </h3>
                <p class="text-gray-600 text-sm line-clamp-2 mb-4">{{ news.summary }}</p>
                <button class="text-primary text-sm font-medium hover:underline">
                  阅读更多
                </button>
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

const filters = ['全部', '律所动态', '成功案例', '法律解读']

const newsList = ref([
  {
    id: 1,
    title: '若山律师事务所荣获2024年度优秀律师事务所称号',
    summary: '凭借专业的法律服务和良好的客户口碑，若山律师事务所荣获年度优秀律师事务所称号...',
    category: '律所动态',
    date: '2024-03-15',
    image: ''
  },
  {
    id: 2,
    title: '新《公司法》解读：企业需要注意的十大变化',
    summary: '新修订的《公司法》即将实施，本文为您解读企业需要重点关注的十大变化...',
    category: '法律解读',
    date: '2024-03-10',
    image: ''
  },
  {
    id: 3,
    title: '张律师受邀参加国际法律论坛并发表演讲',
    summary: '本所高级合伙人张律师受邀参加国际法律论坛，就跨境投资法律风险发表主题演讲...',
    category: '律所动态',
    date: '2024-03-05',
    image: ''
  },
  {
    id: 4,
    title: '成功代理某上市公司股权纠纷案',
    summary: '本所成功代理某上市公司股权纠纷案，为客户挽回经济损失3000万元...',
    category: '成功案例',
    date: '2024-02-28',
    image: ''
  },
  {
    id: 5,
    title: '劳动合同解除的法律风险与防范',
    summary: '本文详细分析劳动合同解除过程中的法律风险，并提供相应的防范建议...',
    category: '法律解读',
    date: '2024-02-20',
    image: ''
  },
  {
    id: 6,
    title: '若山律师事务所开展公益法律咨询活动',
    summary: '为回馈社会，本所组织开展公益法律咨询活动，为社区居民提供免费法律咨询服务...',
    category: '律所动态',
    date: '2024-02-15',
    image: ''
  }
])

const filteredNews = computed(() => {
  if (activeFilter.value === '全部') return newsList.value
  return newsList.value.filter(n => n.category === activeFilter.value)
})
</script>

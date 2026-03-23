<template>
  <section class="py-20">
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
      <div class="text-center mb-16">
        <h2 class="section-title">新闻资讯</h2>
        <p class="section-subtitle">了解最新法律动态和律所新闻</p>
      </div>
      
      <div class="grid grid-cols-1 md:grid-cols-3 gap-8">
        <article 
          v-for="news in newsList" 
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
              <span>{{ news.category }}</span>
              <span class="mx-2">•</span>
              <span>{{ news.date }}</span>
            </div>
            <h3 class="text-lg font-semibold text-primary mb-2 line-clamp-2">
              {{ news.title }}
            </h3>
            <p class="text-gray-600 text-sm line-clamp-2">{{ news.summary }}</p>
          </div>
        </article>
      </div>
      
      <div class="text-center mt-12">
        <router-link to="/news" class="btn-secondary">
          查看更多资讯
        </router-link>
      </div>
    </div>
  </section>
</template>

<script setup lang="ts">
const newsList = [
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
  }
]
</script>

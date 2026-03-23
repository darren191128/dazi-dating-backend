<template>
  <div class="min-h-screen">
    <Header />
    <main class="pt-16">
      <section class="py-20 bg-gray-50">
        <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div class="text-center mb-16">
            <h1 class="section-title">专业团队</h1>
            <p class="section-subtitle">资深律师团队，为您提供专业法律服务</p>
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
          
          <!-- Team Grid -->
          <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-8">
            <div 
              v-for="lawyer in filteredLawyers" 
              :key="lawyer.id"
              class="bg-white rounded-xl overflow-hidden shadow-sm hover:shadow-lg transition-all duration-300"
            >
              <div class="aspect-w-3 aspect-h-4 bg-gray-200">
                <img 
                  :src="lawyer.photoUrl || '/default-avatar.jpg'" 
                  :alt="lawyer.name"
                  class="w-full h-full object-cover"
                />
              </div>
              <div class="p-6">
                <h3 class="text-xl font-bold text-primary mb-1">{{ lawyer.name }}</h3>
                <p class="text-accent font-medium mb-3">{{ lawyer.title }}</p>
                <p class="text-gray-600 text-sm mb-4 line-clamp-3">{{ lawyer.introduction }}</p>
                <div class="flex flex-wrap gap-2 mb-4">
                  <span 
                    v-for="tag in lawyer.specialty.split(',')" 
                    :key="tag"
                    class="px-3 py-1 bg-primary/10 text-primary text-xs rounded-full"
                  >
                    {{ tag.trim() }}
                  </span>
                </div>
                <div class="flex items-center justify-between pt-4 border-t">
                  <a :href="'mailto:' + lawyer.email" class="text-gray-500 hover:text-primary">
                    <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                      <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 8l7.89 5.26a2 2 0 002.22 0L21 8M5 19h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v10a2 2 0 002 2z"/>
                    </svg>
                  </a>
                  <a :href="'tel:' + lawyer.phone" class="text-gray-500 hover:text-primary">
                    <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                      <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 5a2 2 0 012-2h3.28a1 1 0 01.948.684l1.498 4.493a1 1 0 01-.502 1.21l-2.257 1.13a11.042 11.042 0 005.516 5.516l1.13-2.257a1 1 0 011.21-.502l4.493 1.498a1 1 0 01.684.949V19a2 2 0 01-2 2h-1C9.716 21 3 14.284 3 6V5z"/>
                    </svg>
                  </a>
                </div>
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
import { ref, computed } from 'vue'
import Header from '../components/Header.vue'
import Footer from '../components/Footer.vue'

const activeFilter = ref('全部')

const filters = ['全部', '合伙人', '资深律师', '律师']

const lawyers = ref([
  {
    id: 1,
    name: '张律师',
    title: '高级合伙人',
    introduction: '20年执业经验，专注民商事诉讼领域，曾代理多起重大疑难案件',
    photoUrl: '',
    specialty: '民商事诉讼,合同纠纷',
    email: 'zhang@ruoshan.com',
    phone: '13800138001'
  },
  {
    id: 2,
    name: '李律师',
    title: '合伙人',
    introduction: '15年执业经验，专注刑事辩护，具有丰富的刑事案件办理经验',
    photoUrl: '',
    specialty: '刑事辩护,经济犯罪',
    email: 'li@ruoshan.com',
    phone: '13800138002'
  },
  {
    id: 3,
    name: '王律师',
    title: '资深律师',
    introduction: '10年执业经验，专注知识产权领域，服务众多知名企业',
    photoUrl: '',
    specialty: '知识产权,商标专利',
    email: 'wang@ruoshan.com',
    phone: '13800138003'
  },
  {
    id: 4,
    name: '赵律师',
    title: '资深律师',
    introduction: '12年执业经验，专注公司法务，为多家上市公司提供法律顾问服务',
    photoUrl: '',
    specialty: '公司法务,股权设计',
    email: 'zhao@ruoshan.com',
    phone: '13800138004'
  },
  {
    id: 5,
    name: '陈律师',
    title: '律师',
    introduction: '8年执业经验，专注劳动人事和婚姻家庭领域',
    photoUrl: '',
    specialty: '劳动人事,婚姻家庭',
    email: 'chen@ruoshan.com',
    phone: '13800138005'
  },
  {
    id: 6,
    name: '刘律师',
    title: '律师',
    introduction: '6年执业经验，专注房地产和建设工程领域',
    photoUrl: '',
    specialty: '房地产,建设工程',
    email: 'liu@ruoshan.com',
    phone: '13800138006'
  }
])

const filteredLawyers = computed(() => {
  if (activeFilter.value === '全部') return lawyers.value
  return lawyers.value.filter(lawyer => lawyer.title.includes(activeFilter.value))
})
</script>

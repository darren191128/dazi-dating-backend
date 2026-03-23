<template>
  <section class="py-20">
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
      <div class="text-center mb-16">
        <h2 class="section-title">专业团队</h2>
        <p class="section-subtitle">资深律师团队，为您提供专业法律服务</p>
      </div>
      
      <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-8">
        <div 
          v-for="lawyer in lawyers" 
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
            <h3 class="text-xl font-semibold text-primary mb-1">{{ lawyer.name }}</h3>
            <p class="text-accent font-medium mb-2">{{ lawyer.title }}</p>
            <p class="text-gray-600 text-sm mb-4 line-clamp-2">{{ lawyer.introduction }}</p>
            <div class="flex flex-wrap gap-2">
              <span 
                v-for="tag in lawyer.specialty.split(',')" 
                :key="tag"
                class="px-3 py-1 bg-primary/10 text-primary text-xs rounded-full"
              >
                {{ tag.trim() }}
              </span>
            </div>
          </div>
        </div>
      </div>
      
      <div class="text-center mt-12">
        <router-link to="/team" class="btn-secondary">
          查看全部律师
        </router-link>
      </div>
    </div>
  </section>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { lawyerApi } from '../api/lawyer'
import type { Lawyer } from '../api/lawyer'

const lawyers = ref<Lawyer[]>([])

onMounted(async () => {
  try {
    const response = await lawyerApi.getAll()
    lawyers.value = response.data?.slice(0, 3) || []
  } catch (error) {
    console.error('Failed to load lawyers:', error)
    // Use default data if API fails
    lawyers.value = [
      {
        id: 1,
        name: '张律师',
        title: '高级合伙人',
        introduction: '20年执业经验，专注民商事诉讼领域',
        photoUrl: '',
        specialty: '民商事诉讼,合同纠纷',
        email: 'zhang@ruoshan.com',
        phone: '13800138001'
      },
      {
        id: 2,
        name: '李律师',
        title: '合伙人',
        introduction: '15年执业经验，专注刑事辩护',
        photoUrl: '',
        specialty: '刑事辩护,经济犯罪',
        email: 'li@ruoshan.com',
        phone: '13800138002'
      },
      {
        id: 3,
        name: '王律师',
        title: '资深律师',
        introduction: '10年执业经验，专注知识产权',
        photoUrl: '',
        specialty: '知识产权,商标专利',
        email: 'wang@ruoshan.com',
        phone: '13800138003'
      }
    ]
  }
})
</script>

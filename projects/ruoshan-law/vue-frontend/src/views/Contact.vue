<template>
  <div class="min-h-screen">
    <Header />
    <main class="pt-16">
      <section class="py-20 bg-gray-50">
        <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div class="text-center mb-16">
            <h1 class="section-title">联系我们</h1>
            <p class="section-subtitle">如需法律咨询，请随时与我们联系</p>
          </div>
          
          <div class="grid grid-cols-1 lg:grid-cols-2 gap-12">
            <!-- Contact Info -->
            <div>
              <h2 class="text-2xl font-bold text-primary mb-6">联系方式</h2>
              
              <div class="space-y-6">
                <div class="flex items-start">
                  <div class="w-12 h-12 bg-primary/10 rounded-lg flex items-center justify-center mr-4">
                    <svg class="w-6 h-6 text-primary" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                      <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17.657 16.657L13.414 20.9a1.998 1.998 0 01-2.827 0l-4.244-4.243a8 8 0 1111.314 0z"/>
                      <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 11a3 3 0 11-6 0 3 3 0 016 0z"/>
                    </svg>
                  </div>
                  <div>
                    <h3 class="font-semibold text-primary">办公地址</h3>
                    <p class="text-gray-600">北京市朝阳区XX路XX号XX大厦XX层</p>
                  </div>
                </div>
                
                <div class="flex items-start">
                  <div class="w-12 h-12 bg-primary/10 rounded-lg flex items-center justify-center mr-4">
                    <svg class="w-6 h-6 text-primary" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                      <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 5a2 2 0 012-2h3.28a1 1 0 01.948.684l1.498 4.493a1 1 0 01-.502 1.21l-2.257 1.13a11.042 11.042 0 005.516 5.516l1.13-2.257a1 1 0 011.21-.502l4.493 1.498a1 1 0 01.684.949V19a2 2 0 01-2 2h-1C9.716 21 3 14.284 3 6V5z"/>
                    </svg>
                  </div>
                  <div>
                    <h3 class="font-semibold text-primary">咨询电话</h3>
                    <p class="text-gray-600">400-XXX-XXXX</p>
                    <p class="text-sm text-gray-500">工作时间：周一至周五 9:00-18:00</p>
                  </div>
                </div>
                
                <div class="flex items-start">
                  <div class="w-12 h-12 bg-primary/10 rounded-lg flex items-center justify-center mr-4">
                    <svg class="w-6 h-6 text-primary" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                      <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 8l7.89 5.26a2 2 0 002.22 0L21 8M5 19h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v10a2 2 0 002 2z"/>
                    </svg>
                  </div>
                  <div>
                    <h3 class="font-semibold text-primary">电子邮箱</h3>
                    <p class="text-gray-600">contact@ruoshan.com</p>
                  </div>
                </div>
              </div>
            </div>
            
            <!-- Contact Form -->
            <div class="bg-white rounded-xl p-8 shadow-sm">
              <h2 class="text-2xl font-bold text-primary mb-6">在线咨询</h2>
              
              <form @submit.prevent="submitForm" class="space-y-6">
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-2">您的姓名</label>
                  <input 
                    v-model="form.name"
                    type="text"
                    required
                    class="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-primary focus:border-transparent"
                    placeholder="请输入您的姓名"
                  />
                </div>
                
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-2">联系电话</label>
                  <input 
                    v-model="form.phone"
                    type="tel"
                    required
                    class="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-primary focus:border-transparent"
                    placeholder="请输入您的联系电话"
                  />
                </div>
                
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-2">咨询类型</label>
                  <select 
                    v-model="form.type"
                    required
                    class="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-primary focus:border-transparent"
                  >
                    <option value="">请选择咨询类型</option>
                    <option value="民商事诉讼">民商事诉讼</option>
                    <option value="刑事辩护">刑事辩护</option>
                    <option value="知识产权">知识产权</option>
                    <option value="公司法务">公司法务</option>
                    <option value="其他">其他</option>
                  </select>
                </div>
                
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-2">咨询内容</label>
                  <textarea 
                    v-model="form.content"
                    rows="4"
                    required
                    class="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-primary focus:border-transparent"
                    placeholder="请简要描述您的法律问题"
                  ></textarea>
                </div>
                
                <button 
                  type="submit"
                  class="w-full btn-primary py-4"
                  :disabled="isSubmitting"
                >
                  {{ isSubmitting ? '提交中...' : '提交咨询' }}
                </button>
              </form>
            </div>
          </div>
        </div>
      </section>
    </main>
    <Footer />
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import Header from '../components/Header.vue'
import Footer from '../components/Footer.vue'

const form = ref({
  name: '',
  phone: '',
  type: '',
  content: ''
})

const isSubmitting = ref(false)

const submitForm = async () => {
  isSubmitting.value = true
  try {
    // TODO: Implement API call
    console.log('Form submitted:', form.value)
    alert('感谢您的咨询，我们会尽快与您联系！')
    form.value = { name: '', phone: '', type: '', content: '' }
  } catch (error) {
    alert('提交失败，请稍后重试')
  } finally {
    isSubmitting.value = false
  }
}
</script>

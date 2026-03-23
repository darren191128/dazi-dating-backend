<template>
  <div class="min-h-screen">
    <Header />
    <main class="pt-16">
      <section class="py-20">
        <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div class="text-center mb-16">
            <h1 class="section-title">关于我们</h1>
            <p class="section-subtitle">专业、诚信、卓越的律师事务所</p>
          </div>
          
          <div class="grid grid-cols-1 lg:grid-cols-2 gap-12 items-center">
            <div>
              <h2 class="text-2xl font-bold text-primary mb-4">律所简介</h2>
              <p class="text-gray-600 mb-6 leading-relaxed">
                若山律师事务所成立于2005年，是一家综合性律师事务所。经过多年的发展，
                我们已成为区域内最具影响力的律师事务所之一，为众多企业和个人客户提供专业、
                高效的法律服务。
              </p>
              <p class="text-gray-600 mb-6 leading-relaxed">
                我们拥有一支经验丰富、专业精湛的律师团队，在民商事诉讼、刑事辩护、
                知识产权、公司法务等领域具有深厚的专业积累和丰富的实践经验。
              </p>
              
              <div class="grid grid-cols-3 gap-6 mt-8">
                <div class="text-center">
                  <div class="text-3xl font-bold text-accent mb-1">20+</div>
                  <div class="text-gray-600 text-sm">年执业经验</div>
                </div>
                <div class="text-center">
                  <div class="text-3xl font-bold text-accent mb-1">50+</div>
                  <div class="text-gray-600 text-sm">专业律师</div>
                </div>
                <div class="text-center">
                  <div class="text-3xl font-bold text-accent mb-1">10000+</div>
                  <div class="text-gray-600 text-sm">服务客户</div>
                </div>
              </div>
            </div>
            
            <div class="bg-gray-100 rounded-2xl p-8">
              <h3 class="text-xl font-bold text-primary mb-6">我们的优势</h3>
              <ul class="space-y-4">
                <li class="flex items-start">
                  <svg class="w-6 h-6 text-accent mr-3 mt-0.5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 13l4 4L19 7"/>
                  </svg>
                  <span class="text-gray-700">专业团队：资深律师团队，丰富的执业经验</span>
                </li>
                <li class="flex items-start">
                  <svg class="w-6 h-6 text-accent mr-3 mt-0.5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 13l4 4L19 7"/>
                  </svg>
                  <span class="text-gray-700">优质服务：以客户为中心，提供个性化解决方案</span>
                </li>
                <li class="flex items-start">
                  <svg class="w-6 h-6 text-accent mr-3 mt-0.5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 13l4 4L19 7"/>
                  </svg>
                  <span class="text-gray-700">高效办案：科学的案件管理，确保办案质量</span>
                </li>
                <li class="flex items-start">
                  <svg class="w-6 h-6 text-accent mr-3 mt-0.5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 13l4 4L19 7"/>
                  </svg>
                  <span class="text-gray-700">良好口碑：众多成功案例，客户满意度高</span>
                </li>
              </ul>
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
</script>

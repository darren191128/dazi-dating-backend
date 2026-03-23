<template>
  <section class="py-20 bg-gray-50">
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
      <div class="text-center mb-16">
        <h2 class="section-title">成功案例</h2>
        <p class="section-subtitle">专业实力，赢得客户信赖</p>
      </div>
      
      <div class="grid grid-cols-1 md:grid-cols-3 gap-8">
        <div 
          v-for="caseItem in cases" 
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
            <h3 class="text-lg font-semibold text-primary mb-3">{{ caseItem.title }}</h3>
            <p class="text-gray-600 text-sm mb-4 line-clamp-3">{{ caseItem.summary }}</p>
            <div class="flex items-center justify-between pt-4 border-t">
              <span class="text-sm text-gray-500">承办律师: {{ caseItem.lawyer }}</span>
              <span class="text-accent font-semibold text-sm">{{ caseItem.result }}</span>
            </div>
          </div>
        </div>
      </div>
      
      <div class="text-center mt-12">
        <router-link to="/cases" class="btn-secondary">
          查看更多案例
        </router-link>
      </div>
    </div>
  </section>
</template>

<script setup lang="ts">
const cases = [
  {
    id: 1,
    title: '某上市公司股权纠纷案',
    type: '公司法务',
    summary: '成功帮助客户解决股权纠纷，维护股东合法权益，涉案金额5000万元',
    result: '胜诉',
    lawyer: '张律师',
    date: '2024-01'
  },
  {
    id: 2,
    title: '某知名企业商标维权案',
    type: '知识产权',
    summary: '代理知名企业进行商标维权，成功制止侵权行为并获得赔偿',
    result: '胜诉',
    lawyer: '王律师',
    date: '2024-02'
  },
  {
    id: 3,
    title: '某合同纠纷案',
    type: '民商事诉讼',
    summary: '代理客户处理复杂合同纠纷，通过调解方式快速解决争议',
    result: '调解成功',
    lawyer: '李律师',
    date: '2024-03'
  }
]
</script>

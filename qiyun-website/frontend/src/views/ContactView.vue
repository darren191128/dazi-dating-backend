<template>
  <div class="min-h-screen bg-secondary-50">
    <!-- 页面标题区 -->
    <section class="relative py-20 md:py-28 bg-gradient-to-br from-primary-900 via-primary-800 to-primary-700 overflow-hidden">
      <div class="absolute inset-0 opacity-10">
        <div class="absolute top-20 left-10 w-72 h-72 bg-white rounded-full blur-3xl"></div>
        <div class="absolute bottom-20 right-10 w-96 h-96 bg-primary-400 rounded-full blur-3xl"></div>
      </div>
      <div class="container-custom relative z-10">
        <div class="text-center">
          <nav class="flex justify-center mb-6" aria-label="Breadcrumb">
            <ol class="flex items-center space-x-2 text-sm text-white/60">
              <li><RouterLink to="/" class="hover:text-white transition-colors">首页</RouterLink></li>
              <li><span class="text-white/40">/</span></li>
              <li class="text-white">联系我们</li>
            </ol>
          </nav>
          <h1 class="text-4xl md:text-5xl font-bold text-white mb-4">联系我们</h1>
          <p class="text-xl text-white/80 max-w-2xl mx-auto">
            期待与您的合作，让我们一起创造无限可能
          </p>
        </div>
      </div>
    </section>

    <!-- 联系信息卡片 -->
    <section class="py-16 md:py-20 -mt-10 relative z-10">
      <div class="container-custom">
        <div class="grid grid-cols-1 md:grid-cols-3 gap-6">
          <div class="bg-white rounded-2xl p-8 shadow-card card-hover text-center">
            <div class="w-16 h-16 mx-auto bg-primary-100 rounded-2xl flex items-center justify-center mb-6">
              <svg class="w-8 h-8 text-primary-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17.657 16.657L13.414 20.9a1.998 1.998 0 01-2.827 0l-4.244-4.243a8 8 0 1111.314 0z" />
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 11a3 3 0 11-6 0 3 3 0 016 0z" />
              </svg>
            </div>
            <h3 class="text-lg font-semibold text-secondary-900 mb-2">公司地址</h3>
            <p class="text-secondary-600">重庆市渝北区XX路XX号<br>XX大厦XX层</p>
          </div>
          
          <div class="bg-white rounded-2xl p-8 shadow-card card-hover text-center">
            <div class="w-16 h-16 mx-auto bg-primary-100 rounded-2xl flex items-center justify-center mb-6">
              <svg class="w-8 h-8 text-primary-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 5a2 2 0 012-2h3.28a1 1 0 01.948.684l1.498 4.493a1 1 0 01-.502 1.21l-2.257 1.13a11.042 11.042 0 005.516 5.516l1.13-2.257a1 1 0 011.21-.502l4.493 1.498a1 1 0 01.684.949V19a2 2 0 01-2 2h-1C9.716 21 3 14.284 3 6V5z" />
              </svg>
            </div>
            <h3 class="text-lg font-semibold text-secondary-900 mb-2">联系电话</h3>
            <p class="text-secondary-600">400-XXX-XXXX<br>周一至周五 9:00-18:00</p>
          </div>
          
          <div class="bg-white rounded-2xl p-8 shadow-card card-hover text-center">
            <div class="w-16 h-16 mx-auto bg-primary-100 rounded-2xl flex items-center justify-center mb-6">
              <svg class="w-8 h-8 text-primary-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 8l7.89 5.26a2 2 0 002.22 0L21 8M5 19h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v10a2 2 0 002 2z" />
              </svg>
            </div>
            <h3 class="text-lg font-semibold text-secondary-900 mb-2">电子邮箱</h3>
            <p class="text-secondary-600">contact@qiyun.com<br>business@qiyun.com</p>
          </div>
        </div>
      </div>
    </section>

    <!-- 地图区域 -->
    <section class="py-8 md:py-12">
      <div class="container-custom">
        <div class="bg-white rounded-2xl overflow-hidden shadow-card">
          <div class="aspect-[21/9] bg-secondary-100 relative">
            <iframe
              src="https://www.google.com/maps/embed?pb=!1m18!1m12!1m3!1d109744.0590591553!2d106.5046127790422!3d29.56321438336392!2m3!1f0!2f0!3f0!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0x369334d6f8555555%3A0xa8c76b4c5f3d6e9!2sYubei%20District%2C%20Chongqing%2C%20China!5e0!3m2!1sen!2sus!4v1700000000000!5m2!1sen!2sus"
              width="100%"
              height="100%"
              style="border:0;"
              allowfullscreen=""
              loading="lazy"
              referrerpolicy="no-referrer-when-downgrade"
              class="absolute inset-0"
            ></iframe>
          </div>
        </div>
      </div>
    </section>

    <!-- 在线留言表单 -->
    <section class="py-16 md:py-24 bg-white">
      <div class="container-custom">
        <div class="max-w-4xl mx-auto">
          <div class="text-center mb-12">
            <span class="inline-block px-4 py-1 bg-primary-50 text-primary-600 rounded-full text-sm font-medium mb-4">
              在线留言
            </span>
            <h2 class="text-3xl md:text-4xl font-bold text-secondary-900 mb-4">免费咨询</h2>
            <p class="text-secondary-600">
              填写以下表单，我们的专业顾问将在24小时内与您联系
            </p>
          </div>

          <form @submit.prevent="handleSubmit" class="space-y-6">
            <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
              <div>
                <label class="block text-sm font-medium text-secondary-700 mb-2">
                  姓名 <span class="text-red-500">*</span>
                </label>
                <input
                  v-model="form.name"
                  type="text"
                  required
                  class="form-input w-full px-4 py-3 border border-secondary-200 rounded-xl focus:outline-none focus:border-primary-500 focus:ring-2 focus:ring-primary-100 transition-all"
                  placeholder="请输入您的姓名"
                />
              </div>
              <div>
                <label class="block text-sm font-medium text-secondary-700 mb-2">
                  电话 <span class="text-red-500">*</span>
                </label>
                <input
                  v-model="form.phone"
                  type="tel"
                  required
                  class="form-input w-full px-4 py-3 border border-secondary-200 rounded-xl focus:outline-none focus:border-primary-500 focus:ring-2 focus:ring-primary-100 transition-all"
                  placeholder="请输入您的电话"
                />
              </div>
            </div>

            <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
              <div>
                <label class="block text-sm font-medium text-secondary-700 mb-2">
                  邮箱
                </label>
                <input
                  v-model="form.email"
                  type="email"
                  class="form-input w-full px-4 py-3 border border-secondary-200 rounded-xl focus:outline-none focus:border-primary-500 focus:ring-2 focus:ring-primary-100 transition-all"
                  placeholder="请输入您的邮箱"
                />
              </div>
              <div>
                <label class="block text-sm font-medium text-secondary-700 mb-2">
                  需求类型 <span class="text-red-500">*</span>
                </label>
                <select
                  v-model="form.type"
                  required
                  class="form-input w-full px-4 py-3 border border-secondary-200 rounded-xl focus:outline-none focus:border-primary-500 focus:ring-2 focus:ring-primary-100 transition-all bg-white"
                >
                  <option value="">请选择需求类型</option>
                  <option value="miniapp">小程序开发</option>
                  <option value="app">APP开发</option>
                  <option value="website">网站开发</option>
                  <option value="other">其他</option>
                </select>
              </div>
            </div>

            <div>
              <label class="block text-sm font-medium text-secondary-700 mb-2">
                需求描述 <span class="text-red-500">*</span>
              </label>
              <textarea
                v-model="form.message"
                required
                rows="5"
                class="form-input w-full px-4 py-3 border border-secondary-200 rounded-xl focus:outline-none focus:border-primary-500 focus:ring-2 focus:ring-primary-100 transition-all resize-none"
                placeholder="请详细描述您的需求，包括项目背景、功能要求、预算范围等..."
              ></textarea>
            </div>

            <div class="flex items-start gap-3">
              <input
                v-model="form.agree"
                type="checkbox"
                id="agree"
                required
                class="w-5 h-5 text-primary-600 border-secondary-300 rounded focus:ring-primary-500 mt-0.5"
              />
              <label for="agree" class="text-sm text-secondary-600">
                我已阅读并同意 <a href="#" class="text-primary-600 hover:underline">隐私政策</a> 和 <a href="#" class="text-primary-600 hover:underline">服务条款</a>
              </label>
            </div>

            <div class="text-center">
              <button
                type="submit"
                :disabled="isSubmitting"
                class="btn-primary text-lg px-12 py-4 disabled:opacity-50 disabled:cursor-not-allowed"
              >
                <span v-if="isSubmitting" class="flex items-center gap-2">
                  <svg class="animate-spin h-5 w-5" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24">
                    <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle>
                    <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
                  </svg>
                  提交中...
                </span>
                <span v-else>提交咨询</span>
              </button>
            </div>
          </form>
        </div>
      </div>
    </section>

    <!-- 社交媒体 -->
    <section class="py-16 md:py-20 bg-secondary-50">
      <div class="container-custom">
        <div class="text-center mb-12">
          <h2 class="text-2xl md:text-3xl font-bold text-secondary-900 mb-4">关注我们</h2>
          <p class="text-secondary-600">获取最新动态和行业资讯</p>
        </div>

        <div class="flex flex-col md:flex-row items-center justify-center gap-8 md:gap-16">
          <!-- 微信公众号 -->
          <div class="text-center">
            <div class="w-40 h-40 bg-white rounded-2xl shadow-card p-4 mb-4 mx-auto">
              <div class="w-full h-full bg-secondary-100 rounded-xl flex items-center justify-center">
                <svg class="w-16 h-16 text-secondary-400" fill="currentColor" viewBox="0 0 24 24">
                  <path d="M8.691 2.188C3.891 2.188 0 5.476 0 9.53c0 2.212 1.17 4.203 3.002 5.55a.59.59 0 01.213.665l-.39 1.48c-.019.07-.048.141-.048.213 0 .163.13.295.29.295a.326.326 0 00.167-.054l1.903-1.114a.864.864 0 01.717-.098 10.16 10.16 0 002.837.403c.276 0 .543-.027.811-.05-.857-2.578.157-4.972 1.932-6.446 1.703-1.415 3.882-1.98 5.853-1.838-.576-3.583-4.196-6.348-8.596-6.348z"/>
                </svg>
              </div>
            </div>
            <h3 class="font-semibold text-secondary-900">微信公众号</h3>
            <p class="text-sm text-secondary-500">扫码关注启云科技</p>
          </div>

          <!-- 社交媒体链接 -->
          <div class="flex gap-6">
            <a href="#" class="w-16 h-16 bg-white rounded-2xl shadow-card flex items-center justify-center text-secondary-400 hover:text-primary-600 hover:shadow-lg transition-all">
              <svg class="w-8 h-8" fill="currentColor" viewBox="0 0 24 24">
                <path d="M20.447 20.452h-3.554v-5.569c0-1.328-.027-3.037-1.852-3.037-1.853 0-2.136 1.445-2.136 2.939v5.667H9.351V9h3.414v1.561h.046c.477-.9 1.637-1.85 3.37-1.85 3.601 0 4.267 2.37 4.267 5.455v6.286zM5.337 7.433c-1.144 0-2.063-.926-2.063-2.065 0-1.138.92-2.063 2.063-2.063 1.14 0 2.064.925 2.064 2.063 0 1.139-.925 2.065-2.064 2.065zm1.782 13.019H3.555V9h3.564v11.452zM22.225 0H1.771C.792 0 0 .774 0 1.729v20.542C0 23.227.792 24 1.771 24h20.451C23.2 24 24 23.227 24 22.271V1.729C24 .774 23.2 0 22.222 0h.003z"/>
              </svg>
            </a>
            <a href="#" class="w-16 h-16 bg-white rounded-2xl shadow-card flex items-center justify-center text-secondary-400 hover:text-primary-600 hover:shadow-lg transition-all">
              <svg class="w-8 h-8" fill="currentColor" viewBox="0 0 24 24">
                <path d="M12 0C5.374 0 0 5.373 0 12c0 5.302 3.438 9.8 8.207 11.387.599.111.793-.261.793-.577v-2.234c-3.338.726-4.033-1.416-4.033-1.416-.546-1.387-1.333-1.756-1.333-1.756-1.089-.745.083-.729.083-.729 1.205.084 1.839 1.237 1.839 1.237 1.07 1.834 2.807 1.304 3.492.997.107-.775.418-1.305.762-1.604-2.665-.305-5.467-1.334-5.467-5.931 0-1.311.469-2.381 1.236-3.221-.124-.303-.535-1.524.117-3.176 0 0 1.008-.322 3.301 1.23A11.509 11.509 0 0112 5.803c1.02.005 2.047.138 3.006.404 2.291-1.552 3.297-1.23 3.297-1.23.653 1.653.242 2.874.118 3.176.77.84 1.235 1.911 1.235 3.221 0 4.609-2.807 5.624-5.479 5.921.43.372.823 1.102.823 2.222v3.293c0 .319.192.694.801.576C20.566 21.797 24 17.3 24 12c0-6.627-5.373-12-12-12z"/>
              </svg>
            </a>
            <a href="#" class="w-16 h-16 bg-white rounded-2xl shadow-card flex items-center justify-center text-secondary-400 hover:text-primary-600 hover:shadow-lg transition-all">
              <svg class="w-8 h-8" fill="currentColor" viewBox="0 0 24 24">
                <path d="M23.953 4.57a10 10 0 01-2.825.775 4.958 4.958 0 002.163-2.723c-.951.555-2.005.959-3.127 1.184a4.92 4.92 0 00-8.384 4.482C7.69 8.095 4.067 6.13 1.64 3.162a4.822 4.822 0 00-.666 2.475c0 1.71.87 3.213 2.188 4.096a4.904 4.904 0 01-2.228-.616v.06a4.923 4.923 0 003.946 4.827 4.996 4.996 0 01-2.212.085 4.936 4.936 0 004.604 3.417 9.867 9.867 0 01-6.102 2.105c-.39 0-.779-.023-1.17-.067a13.995 13.995 0 007.557 2.209c9.053 0 13.998-7.496 13.998-13.985 0-.21 0-.42-.015-.63A9.935 9.935 0 0024 4.59z"/>
              </svg>
            </a>
          </div>
        </div>
      </div>
    </section>

    <!-- 常见问题 -->
    <section class="py-16 md:py-24 bg-white">
      <div class="container-custom">
        <div class="max-w-3xl mx-auto">
          <div class="text-center mb-12">
            <h2 class="text-2xl md:text-3xl font-bold text-secondary-900 mb-4">常见问题</h2>
            <p class="text-secondary-600">快速了解我们的服务</p>
          </div>

          <div class="space-y-4">
            <div v-for="(faq, index) in faqs" :key="index" class="border border-secondary-200 rounded-xl overflow-hidden">
              <button
                @click="toggleFaq(index)"
                class="w-full flex items-center justify-between p-6 text-left hover:bg-secondary-50 transition-colors"
              >
                <span class="font-medium text-secondary-900">{{ faq.question }}</span>
                <svg
                  class="w-5 h-5 text-secondary-400 transition-transform"
                  :class="{ 'rotate-180': openFaq === index }"
                  fill="none"
                  stroke="currentColor"
                  viewBox="0 0 24 24"
                >
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 9l-7 7-7-7" />
                </svg>
              </button>
              <div
                v-show="openFaq === index"
                class="px-6 pb-6 text-secondary-600"
              >
                {{ faq.answer }}
              </div>
            </div>
          </div>
        </div>
      </div>
    </section>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'

const form = ref({
  name: '',
  phone: '',
  email: '',
  type: '',
  message: '',
  agree: false
})

const isSubmitting = ref(false)

const handleSubmit = async () => {
  isSubmitting.value = true
  // 模拟提交
  await new Promise(resolve => setTimeout(resolve, 1500))
  alert('提交成功！我们将尽快与您联系。')
  isSubmitting.value = false
  // 重置表单
  form.value = {
    name: '',
    phone: '',
    email: '',
    type: '',
    message: '',
    agree: false
  }
}

const openFaq = ref<number | null>(null)

const toggleFaq = (index: number) => {
  openFaq.value = openFaq.value === index ? null : index
}

const faqs = [
  {
    question: '开发一个项目需要多长时间？',
    answer: '项目周期取决于功能复杂度和需求范围。一般来说，小程序开发需要4-8周，APP开发需要8-16周，网站开发需要3-6周。我们会在需求沟通后提供详细的项目排期。'
  },
  {
    question: '项目开发费用如何计算？',
    answer: '我们根据项目功能复杂度、技术难度、开发周期等因素综合报价。在需求明确后，我们会提供详细的报价单，确保价格透明合理。'
  },
  {
    question: '项目上线后提供维护服务吗？',
    answer: '是的，我们提供项目上线后的维护服务。包括Bug修复、性能优化、功能迭代等。具体维护方案可以根据您的需求定制。'
  },
  {
    question: '如何保证项目质量？',
    answer: '我们采用敏捷开发模式，每个阶段都有严格的代码审查和测试流程。项目交付前会经过功能测试、性能测试、安全测试等多轮测试，确保产品质量。'
  },
  {
    question: '可以查看你们之前的案例吗？',
    answer: '当然可以！您可以访问我们的案例展示页面，查看我们过往的成功案例。也可以联系我们，我们会根据您的行业推荐相关案例。'
  }
]
</script>
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
              <li class="text-white">关于我们</li>
            </ol>
          </nav>
          <h1 class="text-4xl md:text-5xl font-bold text-white mb-4">关于启云科技</h1>
          <p class="text-xl text-white/80 max-w-2xl mx-auto">
            专业的软件定制开发服务商，致力于让数字化更简单
          </p>
        </div>
      </div>
    </section>

    <!-- 公司介绍 -->
    <section class="py-16 md:py-24 bg-white">
      <div class="container-custom">
        <div class="grid grid-cols-1 lg:grid-cols-2 gap-12 lg:gap-16 items-center">
          <!-- 左侧图片 -->
          <div class="relative">
            <div class="aspect-[4/3] rounded-2xl overflow-hidden shadow-xl">
              <img 
                src="https://images.unsplash.com/photo-1497366216548-37526070297c?w=800&h=600&fit=crop" 
                alt="启云科技办公环境"
                class="w-full h-full object-cover"
              />
            </div>
            <!-- 浮动数据卡片 -->
            <div class="absolute -bottom-6 -right-6 bg-white rounded-xl shadow-lg p-6 hidden md:block">
              <div class="flex items-center space-x-4">
                <div class="w-12 h-12 bg-primary-100 rounded-lg flex items-center justify-center">
                  <svg class="w-6 h-6 text-primary-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z" />
                  </svg>
                </div>
                <div>
                  <div class="text-2xl font-bold text-secondary-900">5年+</div>
                  <div class="text-sm text-secondary-500">行业深耕</div>
                </div>
              </div>
            </div>
          </div>
          
          <!-- 右侧内容 -->
          <div>
            <span class="inline-block px-4 py-1 bg-primary-50 text-primary-600 rounded-full text-sm font-medium mb-4">
              公司介绍
            </span>
            <h2 class="text-3xl md:text-4xl font-bold text-secondary-900 mb-6">
              让数字化更简单
            </h2>
            <div class="space-y-4 text-secondary-600 leading-relaxed">
              <p>
                重庆启云网络科技有限公司成立于2020年，是一家专注于软件定制开发的高新技术企业。我们致力于为企业提供小程序、APP、网站等一站式数字化解决方案。
              </p>
              <p>
                公司拥有一支经验丰富的技术团队，核心成员均来自知名互联网企业，具备深厚的技术功底和丰富的项目经验。我们坚持以客户需求为导向，以技术创新为驱动，为客户提供高质量、高效率的软件开发服务。
              </p>
              <p>
                多年来，我们服务了超过200家企业客户，涵盖电商、教育、医疗、餐饮等多个行业，积累了丰富的行业经验和成功案例。我们相信，通过专业的技术服务和真诚的合作态度，能够帮助更多企业实现数字化转型。
              </p>
            </div>
            
            <!-- 核心数据 -->
            <div class="grid grid-cols-2 md:grid-cols-4 gap-6 mt-8 pt-8 border-t border-secondary-100">
              <div class="text-center">
                <div class="text-3xl md:text-4xl font-bold text-primary-600">200+</div>
                <div class="text-sm text-secondary-500 mt-1">服务客户</div>
              </div>
              <div class="text-center">
                <div class="text-3xl md:text-4xl font-bold text-primary-600">50+</div>
                <div class="text-sm text-secondary-500 mt-1">成功案例</div>
              </div>
              <div class="text-center">
                <div class="text-3xl md:text-4xl font-bold text-primary-600">50+</div>
                <div class="text-sm text-secondary-500 mt-1">团队成员</div>
              </div>
              <div class="text-center">
                <div class="text-3xl md:text-4xl font-bold text-primary-600">98%</div>
                <div class="text-sm text-secondary-500 mt-1">客户满意度</div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </section>

    <!-- 发展历程 -->
    <section class="py-16 md:py-24 bg-secondary-50">
      <div class="container-custom">
        <div class="text-center mb-16">
          <span class="inline-block px-4 py-1 bg-primary-50 text-primary-600 rounded-full text-sm font-medium mb-4">
            发展历程
          </span>
          <h2 class="text-3xl md:text-4xl font-bold text-secondary-900">我们的成长之路</h2>
        </div>
        
        <div class="relative">
          <!-- 时间轴线 -->
          <div class="hidden md:block absolute left-1/2 top-0 bottom-0 w-0.5 bg-gradient-to-b from-primary-500 to-primary-300 transform -translate-x-1/2"></div>
          
          <div class="space-y-12 md:space-y-0">
            <div v-for="(milestone, index) in milestones" :key="index" 
                 class="relative md:flex md:items-center"
                 :class="index % 2 === 0 ? 'md:flex-row' : 'md:flex-row-reverse'">
              <!-- 内容 -->
              <div class="md:w-1/2" :class="index % 2 === 0 ? 'md:pr-12 md:text-right' : 'md:pl-12'">
                <div class="bg-white rounded-xl p-6 shadow-card card-hover">
                  <div class="text-primary-600 font-bold text-lg mb-2">{{ milestone.year }}</div>
                  <h3 class="text-xl font-semibold text-secondary-900 mb-2">{{ milestone.title }}</h3>
                  <p class="text-secondary-600">{{ milestone.description }}</p>
                </div>
              </div>
              
              <!-- 节点 -->
              <div class="hidden md:flex absolute left-1/2 transform -translate-x-1/2 w-4 h-4 bg-primary-500 rounded-full border-4 border-white shadow-lg z-10"></div>
              
              <!-- 占位 -->
              <div class="md:w-1/2"></div>
            </div>
          </div>
        </div>
      </div>
    </section>

    <!-- 企业文化 -->
    <section class="py-16 md:py-24 bg-white">
      <div class="container-custom">
        <div class="text-center mb-16">
          <span class="inline-block px-4 py-1 bg-primary-50 text-primary-600 rounded-full text-sm font-medium mb-4">
            企业文化
          </span>
          <h2 class="text-3xl md:text-4xl font-bold text-secondary-900 mb-4">我们的价值观</h2>
          <p class="text-secondary-600 max-w-2xl mx-auto">
            以客户为中心，以技术为驱动，以创新为灵魂
          </p>
        </div>
        
        <div class="grid grid-cols-1 md:grid-cols-3 gap-8">
          <div v-for="(value, index) in values" :key="index" class="text-center p-8 rounded-2xl bg-secondary-50 card-hover">
            <div class="w-16 h-16 mx-auto bg-primary-100 rounded-2xl flex items-center justify-center mb-6">
              <component :is="value.icon" class="w-8 h-8 text-primary-600" />
            </div>
            <h3 class="text-xl font-semibold text-secondary-900 mb-3">{{ value.title }}</h3>
            <p class="text-secondary-600">{{ value.description }}</p>
          </div>
        </div>
      </div>
    </section>

    <!-- 资质荣誉 -->
    <section class="py-16 md:py-24 bg-secondary-50">
      <div class="container-custom">
        <div class="text-center mb-16">
          <span class="inline-block px-4 py-1 bg-primary-50 text-primary-600 rounded-full text-sm font-medium mb-4">
            资质荣誉
          </span>
          <h2 class="text-3xl md:text-4xl font-bold text-secondary-900">权威认证，值得信赖</h2>
        </div>
        
        <div class="grid grid-cols-2 md:grid-cols-4 gap-6">
          <div v-for="(cert, index) in certificates" :key="index" 
               class="bg-white rounded-xl p-6 text-center shadow-card card-hover">
            <div class="w-16 h-16 mx-auto bg-primary-50 rounded-xl flex items-center justify-center mb-4">
              <component :is="cert.icon" class="w-8 h-8 text-primary-600" />
            </div>
            <h4 class="font-semibold text-secondary-900 mb-1">{{ cert.title }}</h4>
            <p class="text-sm text-secondary-500">{{ cert.subtitle }}</p>
          </div>
        </div>
      </div>
    </section>

    <!-- CTA -->
    <section class="py-16 md:py-20 bg-gradient-to-r from-primary-600 to-primary-700">
      <div class="container-custom text-center">
        <h2 class="text-3xl md:text-4xl font-bold text-white mb-4">准备好开始您的项目了吗？</h2>
        <p class="text-xl text-white/80 mb-8 max-w-2xl mx-auto">
          让我们携手合作，共同打造卓越的数字化产品
        </p>
        <div class="flex flex-col sm:flex-row items-center justify-center gap-4">
          <RouterLink to="/contact" class="bg-white text-primary-600 px-8 py-4 rounded-xl font-semibold hover:bg-secondary-50 transition-colors shadow-lg">
            立即咨询
          </RouterLink>
          <RouterLink to="/cases" class="border-2 border-white text-white px-8 py-4 rounded-xl font-semibold hover:bg-white/10 transition-colors">
            查看案例
          </RouterLink>
        </div>
      </div>
    </section>
  </div>
</template>

<script setup lang="ts">
import { h } from 'vue'

// 发展历程数据
const milestones = [
  {
    year: '2020',
    title: '公司成立',
    description: '启云科技在重庆成立，开始为企业提供软件开发服务'
  },
  {
    year: '2021',
    title: '业务拓展',
    description: '团队规模扩大至20人，服务客户突破50家'
  },
  {
    year: '2022',
    title: '技术升级',
    description: '引入新技术栈，成立移动端开发专项团队'
  },
  {
    year: '2023',
    title: '荣誉认证',
    description: '获得高新技术企业认证，成为行业标杆企业'
  },
  {
    year: '2024',
    title: '稳步发展',
    description: '服务客户突破200家，团队规模达到50+人'
  }
]

// 价值观图标组件
const TargetIcon = () => h('svg', { class: 'w-8 h-8', fill: 'none', stroke: 'currentColor', viewBox: '0 0 24 24' }, [
  h('path', { 'stroke-linecap': 'round', 'stroke-linejoin': 'round', 'stroke-width': '2', d: 'M13 10V3L4 14h7v7l9-11h-7z' })
])

const EyeIcon = () => h('svg', { class: 'w-8 h-8', fill: 'none', stroke: 'currentColor', viewBox: '0 0 24 24' }, [
  h('path', { 'stroke-linecap': 'round', 'stroke-linejoin': 'round', 'stroke-width': '2', d: 'M15 12a3 3 0 11-6 0 3 3 0 016 0z' }),
  h('path', { 'stroke-linecap': 'round', 'stroke-linejoin': 'round', 'stroke-width': '2', d: 'M2.458 12C3.732 7.943 7.523 5 12 5c4.478 0 8.268 2.943 9.542 7-1.274 4.057-5.064 7-9.542 7-4.477 0-8.268-2.943-9.542-7z' })
])

const HeartIcon = () => h('svg', { class: 'w-8 h-8', fill: 'none', stroke: 'currentColor', viewBox: '0 0 24 24' }, [
  h('path', { 'stroke-linecap': 'round', 'stroke-linejoin': 'round', 'stroke-width': '2', d: 'M4.318 6.318a4.5 4.5 0 000 6.364L12 20.364l7.682-7.682a4.5 4.5 0 00-6.364-6.364L12 7.636l-1.318-1.318a4.5 4.5 0 00-6.364 0z' })
])

const values = [
  {
    title: '企业使命',
    description: '让数字化更简单，帮助企业实现业务增长和转型升级',
    icon: TargetIcon
  },
  {
    title: '企业愿景',
    description: '成为西南地区领先的软件定制开发服务商',
    icon: EyeIcon
  },
  {
    title: '核心价值观',
    description: '专业、创新、诚信、共赢，以客户成功为己任',
    icon: HeartIcon
  }
]

// 资质图标
const AwardIcon = () => h('svg', { class: 'w-8 h-8', fill: 'none', stroke: 'currentColor', viewBox: '0 0 24 24' }, [
  h('path', { 'stroke-linecap': 'round', 'stroke-linejoin': 'round', 'stroke-width': '2', d: 'M9 12l2 2 4-4M7.835 4.697a3.42 3.42 0 001.946-.806 3.42 3.42 0 014.438 0 3.42 3.42 0 001.946.806 3.42 3.42 0 013.138 3.138 3.42 3.42 0 00.806 1.946 3.42 3.42 0 010 4.438 3.42 3.42 0 00-.806 1.946 3.42 3.42 0 01-3.138 3.138 3.42 3.42 0 00-1.946.806 3.42 3.42 0 01-4.438 0 3.42 3.42 0 00-1.946-.806 3.42 3.42 0 01-3.138-3.138 3.42 3.42 0 00-.806-1.946 3.42 3.42 0 010-4.438 3.42 3.42 0 00.806-1.946 3.42 3.42 0 013.138-3.138z' })
])

const CertificateIcon = () => h('svg', { class: 'w-8 h-8', fill: 'none', stroke: 'currentColor', viewBox: '0 0 24 24' }, [
  h('path', { 'stroke-linecap': 'round', 'stroke-linejoin': 'round', 'stroke-width': '2', d: 'M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z' })
])

const BuildingIcon = () => h('svg', { class: 'w-8 h-8', fill: 'none', stroke: 'currentColor', viewBox: '0 0 24 24' }, [
  h('path', { 'stroke-linecap': 'round', 'stroke-linejoin': 'round', 'stroke-width': '2', d: 'M19 21V5a2 2 0 00-2-2H7a2 2 0 00-2 2v16m14 0h2m-2 0h-5m-9 0H3m2 0h5M9 7h1m-1 4h1m4-4h1m-1 4h1m-5 10v-5a1 1 0 011-1h2a1 1 0 011 1v5m-4 0h4' })
])

const ShieldIcon = () => h('svg', { class: 'w-8 h-8', fill: 'none', stroke: 'currentColor', viewBox: '0 0 24 24' }, [
  h('path', { 'stroke-linecap': 'round', 'stroke-linejoin': 'round', 'stroke-width': '2', d: 'M9 12l2 2 4-4m5.618-4.016A11.955 11.955 0 0112 2.944a11.955 11.955 0 01-8.618 3.04A12.02 12.02 0 003 9c0 5.591 3.824 10.29 9 11.622 5.176-1.332 9-6.03 9-11.622 0-1.042-.133-2.052-.382-3.016z' })
])

const certificates = [
  { title: '高新技术企业', subtitle: '官方认证', icon: AwardIcon },
  { title: '软件企业证书', subtitle: '行业资质', icon: CertificateIcon },
  { title: 'ISO9001认证', subtitle: '质量管理', icon: BuildingIcon },
  { title: '信息安全认证', subtitle: '安全合规', icon: ShieldIcon }
]
</script>
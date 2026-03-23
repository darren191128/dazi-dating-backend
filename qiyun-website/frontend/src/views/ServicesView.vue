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
              <li class="text-white">服务介绍</li>
            </ol>
          </nav>
          <h1 class="text-4xl md:text-5xl font-bold text-white mb-4">我们的服务</h1>
          <p class="text-xl text-white/80 max-w-2xl mx-auto">
            全方位满足您的数字化需求，让技术赋能业务增长
          </p>
        </div>
      </div>
    </section>

    <!-- 服务导航标签 -->
    <section class="sticky top-16 md:top-20 z-40 bg-white border-b border-secondary-100 shadow-sm">
      <div class="container-custom">
        <div class="flex overflow-x-auto py-4 gap-2 no-scrollbar">
          <button
            v-for="service in services"
            :key="service.id"
            @click="activeService = service.id"
            class="flex-shrink-0 px-6 py-3 rounded-xl font-medium transition-all whitespace-nowrap"
            :class="activeService === service.id 
              ? 'bg-primary-600 text-white shadow-lg' 
              : 'bg-secondary-50 text-secondary-600 hover:bg-primary-50 hover:text-primary-600'"
          >
            <span class="flex items-center gap-2">
              <component :is="service.icon" class="w-5 h-5" />
              {{ service.name }}
            </span>
          </button>
        </div>
      </div>
    </section>

    <!-- 服务详情 -->
    <section class="py-16 md:py-24 bg-white">
      <div class="container-custom">
        <div v-for="service in services" :key="service.id" v-show="activeService === service.id">
          <!-- 服务头部 -->
          <div class="grid grid-cols-1 lg:grid-cols-2 gap-12 lg:gap-16 items-center mb-16">
            <div>
              <div class="w-20 h-20 bg-gradient-to-br from-primary-500 to-primary-700 rounded-2xl flex items-center justify-center mb-6 shadow-lg">
                <component :is="service.icon" class="w-10 h-10 text-white" />
              </div>
              <h2 class="text-3xl md:text-4xl font-bold text-secondary-900 mb-4">{{ service.name }}</h2>
              <p class="text-lg text-secondary-600 leading-relaxed mb-6">{{ service.description }}</p>
              <div class="flex flex-wrap gap-3">
                <span v-for="tag in service.tags" :key="tag" class="px-4 py-1.5 bg-primary-50 text-primary-600 rounded-full text-sm font-medium">
                  {{ tag }}
                </span>
              </div>
            </div>
            <div class="relative">
              <div class="aspect-[4/3] rounded-2xl overflow-hidden shadow-xl">
                <img :src="service.image" :alt="service.name" class="w-full h-full object-cover" />
              </div>
            </div>
          </div>

          <!-- 适用场景 -->
          <div class="mb-16">
            <h3 class="text-2xl font-bold text-secondary-900 mb-8 text-center">适用场景</h3>
            <div class="grid grid-cols-2 md:grid-cols-4 gap-6">
              <div v-for="(scenario, idx) in service.scenarios" :key="idx" class="text-center p-6 rounded-xl bg-secondary-50 card-hover">
                <div class="w-14 h-14 mx-auto bg-white rounded-xl shadow-md flex items-center justify-center mb-4">
                  <component :is="scenario.icon" class="w-7 h-7 text-primary-600" />
                </div>
                <h4 class="font-semibold text-secondary-900 mb-1">{{ scenario.title }}</h4>
                <p class="text-sm text-secondary-500">{{ scenario.desc }}</p>
              </div>
            </div>
          </div>

          <!-- 技术优势 -->
          <div class="mb-16">
            <h3 class="text-2xl font-bold text-secondary-900 mb-8 text-center">技术优势</h3>
            <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
              <div v-for="(advantage, idx) in service.advantages" :key="idx" class="flex items-start gap-4 p-6 rounded-xl bg-secondary-50">
                <div class="w-10 h-10 bg-primary-100 rounded-lg flex items-center justify-center flex-shrink-0">
                  <svg class="w-5 h-5 text-primary-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 13l4 4L19 7" />
                  </svg>
                </div>
                <div>
                  <h4 class="font-semibold text-secondary-900 mb-1">{{ advantage.title }}</h4>
                  <p class="text-secondary-600 text-sm">{{ advantage.desc }}</p>
                </div>
              </div>
            </div>
          </div>

          <!-- 技术栈 -->
          <div class="mb-16">
            <h3 class="text-2xl font-bold text-secondary-900 mb-8 text-center">技术栈</h3>
            <div class="flex flex-wrap justify-center gap-4">
              <div v-for="(tech, idx) in service.techStack" :key="idx" 
                   class="px-6 py-3 bg-white border border-secondary-200 rounded-xl shadow-sm flex items-center gap-3">
                <div class="w-8 h-8 rounded-lg flex items-center justify-center" :class="tech.bgColor">
                  <component :is="tech.icon" class="w-5 h-5" :class="tech.textColor" />
                </div>
                <span class="font-medium text-secondary-700">{{ tech.name }}</span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </section>

    <!-- 服务流程 -->
    <section class="py-16 md:py-24 bg-secondary-50">
      <div class="container-custom">
        <div class="text-center mb-16">
          <span class="inline-block px-4 py-1 bg-primary-50 text-primary-600 rounded-full text-sm font-medium mb-4">
            服务流程
          </span>
          <h2 class="text-3xl md:text-4xl font-bold text-secondary-900 mb-4">标准化开发流程</h2>
          <p class="text-secondary-600 max-w-2xl mx-auto">
            从需求沟通到上线运维，我们提供全生命周期的专业服务
          </p>
        </div>

        <div class="relative">
          <!-- 流程连接线 - 桌面端 -->
          <div class="hidden lg:block absolute top-16 left-0 right-0 h-0.5 bg-gradient-to-r from-primary-200 via-primary-400 to-primary-200"></div>
          
          <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-7 gap-6">
            <div v-for="(step, index) in processSteps" :key="index" class="relative">
              <div class="bg-white rounded-xl p-6 shadow-card card-hover text-center h-full">
                <div class="w-12 h-12 mx-auto bg-primary-600 text-white rounded-full flex items-center justify-center text-lg font-bold mb-4 relative z-10">
                  {{ index + 1 }}
                </div>
                <h4 class="font-semibold text-secondary-900 mb-2">{{ step.title }}</h4>
                <p class="text-sm text-secondary-500">{{ step.desc }}</p>
              </div>
            </div>
          </div>
        </div>
      </div>
    </section>

    <!-- 服务统计 -->
    <section class="py-16 md:py-20 bg-gradient-to-r from-primary-600 to-primary-700">
      <div class="container-custom">
        <div class="grid grid-cols-2 md:grid-cols-4 gap-8 text-center">
          <div>
            <div class="text-4xl md:text-5xl font-bold text-white mb-2">200+</div>
            <div class="text-white/80">服务项目</div>
          </div>
          <div>
            <div class="text-4xl md:text-5xl font-bold text-white mb-2">50+</div>
            <div class="text-white/80">技术专家</div>
          </div>
          <div>
            <div class="text-4xl md:text-5xl font-bold text-white mb-2">98%</div>
            <div class="text-white/80">按时交付</div>
          </div>
          <div>
            <div class="text-4xl md:text-5xl font-bold text-white mb-2">24/7</div>
            <div class="text-white/80">技术支持</div>
          </div>
        </div>
      </div>
    </section>

    <!-- CTA -->
    <section class="py-16 md:py-24 bg-white">
      <div class="container-custom">
        <div class="bg-gradient-to-br from-secondary-900 to-primary-900 rounded-3xl p-8 md:p-16 text-center relative overflow-hidden">
          <div class="absolute inset-0 opacity-20">
            <div class="absolute top-0 left-0 w-64 h-64 bg-primary-500 rounded-full blur-3xl"></div>
            <div class="absolute bottom-0 right-0 w-64 h-64 bg-cyan-500 rounded-full blur-3xl"></div>
          </div>
          <div class="relative z-10">
            <h2 class="text-3xl md:text-4xl font-bold text-white mb-4">开始您的数字化之旅</h2>
            <p class="text-xl text-white/80 mb-8 max-w-2xl mx-auto">
              无论您是初创企业还是成熟企业，我们都能为您提供专业的技术解决方案
            </p>
            <div class="flex flex-col sm:flex-row items-center justify-center gap-4">
              <RouterLink to="/contact" class="bg-white text-primary-600 px-8 py-4 rounded-xl font-semibold hover:bg-secondary-50 transition-colors shadow-lg">
                免费咨询
              </RouterLink>
              <RouterLink to="/cases" class="border-2 border-white text-white px-8 py-4 rounded-xl font-semibold hover:bg-white/10 transition-colors">
                查看案例
              </RouterLink>
            </div>
          </div>
        </div>
      </div>
    </section>
  </div>
</template>

<script setup lang="ts">
import { ref, h } from 'vue'

const activeService = ref('miniapp')

// 图标组件
const MobileIcon = () => h('svg', { class: 'w-5 h-5', fill: 'none', stroke: 'currentColor', viewBox: '0 0 24 24' }, [
  h('path', { 'stroke-linecap': 'round', 'stroke-linejoin': 'round', 'stroke-width': '2', d: 'M12 18h.01M8 21h8a2 2 0 002-2V5a2 2 0 00-2-2H8a2 2 0 00-2 2v14a2 2 0 002 2z' })
])

const DevicePhoneIcon = () => h('svg', { class: 'w-5 h-5', fill: 'none', stroke: 'currentColor', viewBox: '0 0 24 24' }, [
  h('path', { 'stroke-linecap': 'round', 'stroke-linejoin': 'round', 'stroke-width': '2', d: 'M12 18h.01M7 21h10a2 2 0 002-2V5a2 2 0 00-2-2H7a2 2 0 00-2 2v14a2 2 0 002 2z' })
])

const GlobeIcon = () => h('svg', { class: 'w-5 h-5', fill: 'none', stroke: 'currentColor', viewBox: '0 0 24 24' }, [
  h('path', { 'stroke-linecap': 'round', 'stroke-linejoin': 'round', 'stroke-width': '2', d: 'M21 12a9 9 0 01-9 9m9-9a9 9 0 00-9-9m9 9H3m9 9a9 9 0 01-9-9m9 9c1.657 0 3-4.03 3-9s-1.343-9-3-9m0 18c-1.657 0-3-4.03-3-9s1.343-9 3-9m-9 9a9 9 0 019-9' })
])

// 场景图标
const ShoppingIcon = () => h('svg', { class: 'w-7 h-7', fill: 'none', stroke: 'currentColor', viewBox: '0 0 24 24' }, [
  h('path', { 'stroke-linecap': 'round', 'stroke-linejoin': 'round', 'stroke-width': '2', d: 'M16 11V7a4 4 0 00-8 0v4M5 9h14l1 12H4L5 9z' })
])

const RestaurantIcon = () => h('svg', { class: 'w-7 h-7', fill: 'none', stroke: 'currentColor', viewBox: '0 0 24 24' }, [
  h('path', { 'stroke-linecap': 'round', 'stroke-linejoin': 'round', 'stroke-width': '2', d: 'M12 6.253v13m0-13C10.832 5.477 9.246 5 7.5 5S4.168 5.477 3 6.253v13C4.168 18.477 5.754 18 7.5 18s3.332.477 4.5 1.253m0-13C13.168 5.477 14.754 5 16.5 5c1.747 0 3.332.477 4.5 1.253v13C19.832 18.477 18.247 18 16.5 18c-1.746 0-3.332.477-4.5 1.253' })
])

const EducationIcon = () => h('svg', { class: 'w-7 h-7', fill: 'none', stroke: 'currentColor', viewBox: '0 0 24 24' }, [
  h('path', { 'stroke-linecap': 'round', 'stroke-linejoin': 'round', 'stroke-width': '2', d: 'M12 14l9-5-9-5-9 5 9 5z' }),
  h('path', { 'stroke-linecap': 'round', 'stroke-linejoin': 'round', 'stroke-width': '2', d: 'M12 14l6.16-3.422a12.083 12.083 0 01.665 6.479A11.952 11.952 0 0012 20.055a11.952 11.952 0 00-6.824-2.998 12.078 12.078 0 01.665-6.479L12 14z' })
])

const MedicalIcon = () => h('svg', { class: 'w-7 h-7', fill: 'none', stroke: 'currentColor', viewBox: '0 0 24 24' }, [
  h('path', { 'stroke-linecap': 'round', 'stroke-linejoin': 'round', 'stroke-width': '2', d: 'M4.318 6.318a4.5 4.5 0 000 6.364L12 20.364l7.682-7.682a4.5 4.5 0 00-6.364-6.364L12 7.636l-1.318-1.318a4.5 4.5 0 00-6.364 0z' })
])

const BriefcaseIcon = () => h('svg', { class: 'w-7 h-7', fill: 'none', stroke: 'currentColor', viewBox: '0 0 24 24' }, [
  h('path', { 'stroke-linecap': 'round', 'stroke-linejoin': 'round', 'stroke-width': '2', d: 'M21 13.255A23.931 23.931 0 0112 15c-3.183 0-6.22-.62-9-1.745M16 6V4a2 2 0 00-2-2h-4a2 2 0 00-2 2v2m4 6h.01M5 20h14a2 2 0 002-2V8a2 2 0 00-2-2H5a2 2 0 00-2 2v10a2 2 0 002 2z' })
])

const SocialIcon = () => h('svg', { class: 'w-7 h-7', fill: 'none', stroke: 'currentColor', viewBox: '0 0 24 24' }, [
  h('path', { 'stroke-linecap': 'round', 'stroke-linejoin': 'round', 'stroke-width': '2', d: 'M17 20h5v-2a3 3 0 00-5.356-1.857M17 20H7m10 0v-2c0-.656-.126-1.283-.356-1.857M7 20H2v-2a3 3 0 015.356-1.857M7 20v-2c0-.656.126-1.283.356-1.857m0 0a5.002 5.002 0 019.288 0M15 7a3 3 0 11-6 0 3 3 0 016 0zm6 3a2 2 0 11-4 0 2 2 0 014 0zM7 10a2 2 0 11-4 0 2 2 0 014 0z' })
])

const ToolIcon = () => h('svg', { class: 'w-7 h-7', fill: 'none', stroke: 'currentColor', viewBox: '0 0 24 24' }, [
  h('path', { 'stroke-linecap': 'round', 'stroke-linejoin': 'round', 'stroke-width': '2', d: 'M10.325 4.317c.426-1.756 2.924-1.756 3.35 0a1.724 1.724 0 002.573 1.066c1.543-.94 3.31.826 2.37 2.37a1.724 1.724 0 001.065 2.572c1.756.426 1.756 2.924 0 3.35a1.724 1.724 0 00-1.066 2.573c.94 1.543-.826 3.31-2.37 2.37a1.724 1.724 0 00-2.572 1.065c-.426 1.756-2.924 1.756-3.35 0a1.724 1.724 0 00-2.573-1.066c-1.543.94-3.31-.826-2.37-2.37a1.724 1.724 0 00-1.065-2.572c-1.756-.426-1.756-2.924 0-3.35a1.724 1.724 0 001.066-2.573c-.94-1.543.826-3.31 2.37-2.37.996.608 2.296.07 2.572-1.065z' }),
  h('path', { 'stroke-linecap': 'round', 'stroke-linejoin': 'round', 'stroke-width': '2', d: 'M15 12a3 3 0 11-6 0 3 3 0 016 0z' })
])

const ChartIcon = () => h('svg', { class: 'w-7 h-7', fill: 'none', stroke: 'currentColor', viewBox: '0 0 24 24' }, [
  h('path', { 'stroke-linecap': 'round', 'stroke-linejoin': 'round', 'stroke-width': '2', d: 'M9 19v-6a2 2 0 00-2-2H5a2 2 0 00-2 2v6a2 2 0 002 2h2a2 2 0 002-2zm0 0V9a2 2 0 012-2h2a2 2 0 012 2v10m-6 0a2 2 0 002 2h2a2 2 0 002-2m0 0V5a2 2 0 012-2h2a2 2 0 012 2v14a2 2 0 01-2 2h-2a2 2 0 01-2-2z' })
])

const services = [
  {
    id: 'miniapp',
    name: '小程序开发',
    icon: MobileIcon,
    description: '我们提供微信、支付宝、抖音等多平台小程序定制开发服务，帮助企业快速触达用户，实现业务增长。无论是电商购物、餐饮服务还是企业展示，我们都能为您量身打造专属的小程序解决方案。',
    tags: ['微信小程序', '支付宝小程序', '抖音小程序', '跨平台'],
    image: 'https://images.unsplash.com/photo-1512941937669-90a1b58e7e9c?w=800&h=600&fit=crop',
    scenarios: [
      { title: '电商零售', desc: '在线商城', icon: ShoppingIcon },
      { title: '餐饮服务', desc: '点餐外卖', icon: RestaurantIcon },
      { title: '教育培训', desc: '在线学习', icon: EducationIcon },
      { title: '医疗健康', desc: '预约挂号', icon: MedicalIcon }
    ],
    advantages: [
      { title: '原生性能体验', desc: '采用原生渲染技术，确保流畅的用户体验' },
      { title: '跨平台兼容', desc: '一套代码多端运行，降低开发和维护成本' },
      { title: '快速迭代上线', desc: '敏捷开发模式，快速响应需求变化' },
      { title: '完整生态对接', desc: '深度集成支付、分享、消息推送等能力' }
    ],
    techStack: [
      { name: 'Taro', icon: () => h('span', { class: 'text-xs font-bold' }, 'T'), bgColor: 'bg-blue-100', textColor: 'text-blue-600' },
      { name: 'UniApp', icon: () => h('span', { class: 'text-xs font-bold' }, 'U'), bgColor: 'bg-green-100', textColor: 'text-green-600' },
      { name: '微信小程序', icon: () => h('span', { class: 'text-xs font-bold' }, 'W'), bgColor: 'bg-emerald-100', textColor: 'text-emerald-600' },
      { name: 'Flutter', icon: () => h('span', { class: 'text-xs font-bold' }, 'F'), bgColor: 'bg-cyan-100', textColor: 'text-cyan-600' }
    ]
  },
  {
    id: 'app',
    name: 'APP开发',
    icon: DevicePhoneIcon,
    description: '提供iOS、Android原生开发及Flutter跨平台开发服务，打造高性能、高体验的移动应用。从需求分析到上架发布，提供一站式APP开发解决方案。',
    tags: ['iOS开发', 'Android开发', 'Flutter', '原生开发'],
    image: 'https://images.unsplash.com/photo-1512941937669-90a1b58e7e9c?w=800&h=600&fit=crop',
    scenarios: [
      { title: '企业应用', desc: '内部管理', icon: BriefcaseIcon },
      { title: '社交应用', desc: '即时通讯', icon: SocialIcon },
      { title: '工具应用', desc: '效率工具', icon: ToolIcon },
      { title: '数据应用', desc: '数据分析', icon: ChartIcon }
    ],
    advantages: [
      { title: '原生性能优化', desc: '充分利用设备能力，提供最佳性能表现' },
      { title: '精美UI设计', desc: '遵循平台设计规范，打造极致用户体验' },
      { title: '安全防护机制', desc: '多层安全防护，保障应用和数据安全' },
      { title: '持续运维支持', desc: '提供长期技术支持和版本迭代服务' }
    ],
    techStack: [
      { name: 'Swift', icon: () => h('span', { class: 'text-xs font-bold' }, 'S'), bgColor: 'bg-orange-100', textColor: 'text-orange-600' },
      { name: 'Kotlin', icon: () => h('span', { class: 'text-xs font-bold' }, 'K'), bgColor: 'bg-purple-100', textColor: 'text-purple-600' },
      { name: 'Flutter', icon: () => h('span', { class: 'text-xs font-bold' }, 'F'), bgColor: 'bg-cyan-100', textColor: 'text-cyan-600' },
      { name: 'React Native', icon: () => h('span', { class: 'text-xs font-bold' }, 'R'), bgColor: 'bg-blue-100', textColor: 'text-blue-600' }
    ]
  },
  {
    id: 'website',
    name: '网站开发',
    icon: GlobeIcon,
    description: '提供企业官网、电商平台、管理系统、H5活动页等多种类型的网站开发服务。采用现代化技术栈，确保网站的高性能、高可用和良好的SEO表现。',
    tags: ['企业官网', '电商平台', '管理系统', 'H5页面'],
    image: 'https://images.unsplash.com/photo-1460925895917-afdab827c52f?w=800&h=600&fit=crop',
    scenarios: [
      { title: '品牌展示', desc: '企业官网', icon: BriefcaseIcon },
      { title: '在线销售', desc: '电商平台', icon: ShoppingIcon },
      { title: '业务管理', desc: '管理系统', icon: ToolIcon },
      { title: '营销推广', desc: 'H5活动', icon: ChartIcon }
    ],
    advantages: [
      { title: '响应式设计', desc: '完美适配各种设备，提供一致的用户体验' },
      { title: 'SEO优化', desc: '遵循搜索引擎最佳实践，提升网站排名' },
      { title: '高性能架构', desc: '优化的代码和架构，确保快速加载' },
      { title: '易于维护', desc: '模块化开发，便于后期维护和扩展' }
    ],
    techStack: [
      { name: 'Vue.js', icon: () => h('span', { class: 'text-xs font-bold' }, 'V'), bgColor: 'bg-green-100', textColor: 'text-green-600' },
      { name: 'React', icon: () => h('span', { class: 'text-xs font-bold' }, 'R'), bgColor: 'bg-blue-100', textColor: 'text-blue-600' },
      { name: 'Next.js', icon: () => h('span', { class: 'text-xs font-bold' }, 'N'), bgColor: 'bg-gray-100', textColor: 'text-gray-600' },
      { name: 'Node.js', icon: () => h('span', { class: 'text-xs font-bold' }, 'N'), bgColor: 'bg-green-100', textColor: 'text-green-700' }
    ]
  }
]

const processSteps = [
  { title: '需求沟通', desc: '深入了解业务需求' },
  { title: '方案设计', desc: '制定技术方案' },
  { title: '合同签订', desc: '明确合作细节' },
  { title: '项目开发', desc: '敏捷迭代开发' },
  { title: '测试验收', desc: '全面质量保障' },
  { title: '上线部署', desc: '平稳发布上线' },
  { title: '售后维护', desc: '持续技术支持' }
]
</script>

<style scoped>
.no-scrollbar::-webkit-scrollbar {
  display: none;
}
.no-scrollbar {
  -ms-overflow-style: none;
  scrollbar-width: none;
}
</style>
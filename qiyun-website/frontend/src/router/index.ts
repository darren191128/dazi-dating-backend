import { createRouter, createWebHistory } from 'vue-router'
import type { RouteRecordRaw } from 'vue-router'

const routes: RouteRecordRaw[] = [
  {
    path: '/',
    name: 'Home',
    component: () => import('@views/HomeView.vue'),
    meta: {
      title: '启云科技 - 让数字化更简单',
      description: '重庆启云网络科技有限公司 - 专业的软件定制开发服务商，提供小程序、APP、网站一站式开发解决方案'
    }
  },
  {
    path: '/about',
    name: 'About',
    component: () => import('@views/AboutView.vue'),
    meta: {
      title: '关于我们 - 启云科技',
      description: '了解启云科技的发展历程、企业文化和团队实力'
    }
  },
  {
    path: '/services',
    name: 'Services',
    component: () => import('@views/ServicesView.vue'),
    meta: {
      title: '服务介绍 - 启云科技',
      description: '小程序定制开发、APP定制开发、网站定制开发，全方位满足您的数字化需求'
    }
  },
  {
    path: '/cases',
    name: 'Cases',
    component: () => import('@views/CasesView.vue'),
    meta: {
      title: '案例展示 - 启云科技',
      description: '查看启云科技的精选案例，用实力说话，见证每一次成功交付'
    }
  },
  {
    path: '/cases/:id',
    name: 'CaseDetail',
    component: () => import('@views/CaseDetailView.vue'),
    meta: {
      title: '案例详情 - 启云科技',
      description: '了解项目详情、解决方案和项目成果'
    }
  },
  {
    path: '/team',
    name: 'Team',
    component: () => import('@views/TeamView.vue'),
    meta: {
      title: '团队介绍 - 启云科技',
      description: '认识启云科技的核心团队，了解我们的专业实力'
    }
  },
  {
    path: '/news',
    name: 'News',
    component: () => import('@views/NewsView.vue'),
    meta: {
      title: '新闻动态 - 启云科技',
      description: '了解启云科技的最新动态和行业资讯'
    }
  },
  {
    path: '/news/:id',
    name: 'NewsDetail',
    component: () => import('@views/NewsDetailView.vue'),
    meta: {
      title: '新闻详情 - 启云科技',
      description: '阅读详细内容，了解最新动态'
    }
  },
  {
    path: '/careers',
    name: 'Careers',
    component: () => import('@views/CareersView.vue'),
    meta: {
      title: '招贤纳士 - 启云科技',
      description: '加入启云科技，与优秀的人一起创造未来'
    }
  },
  {
    path: '/careers/:id',
    name: 'JobDetail',
    component: () => import('@views/JobDetailView.vue'),
    meta: {
      title: '职位详情 - 启云科技',
      description: '了解职位详情，投递简历加入我们'
    }
  },
  {
    path: '/contact',
    name: 'Contact',
    component: () => import('@views/ContactView.vue'),
    meta: {
      title: '联系我们 - 启云科技',
      description: '联系启云科技，获取专业的软件开发咨询服务'
    }
  },
  {
    path: '/:pathMatch(.*)*',
    name: 'NotFound',
    component: () => import('@views/NotFoundView.vue'),
    meta: {
      title: '页面未找到 - 启云科技',
      description: '抱歉，您访问的页面不存在'
    }
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes,
  scrollBehavior(to, from, savedPosition) {
    if (savedPosition) {
      return savedPosition
    } else {
      return { top: 0 }
    }
  }
})

// 路由守卫：更新页面标题和meta
router.beforeEach((to, from, next) => {
  const meta = to.meta
  if (meta.title) {
    document.title = meta.title as string
  }
  if (meta.description) {
    const metaDescription = document.querySelector('meta[name="description"]')
    if (metaDescription) {
      metaDescription.setAttribute('content', meta.description as string)
    }
  }
  next()
})

export default router
import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import type { Service, Case, News, Job, TeamMember, Milestone, NavItem } from '@types'

// 导航数据
export const useNavStore = defineStore('nav', () => {
  const navItems = ref<NavItem[]>([
    { name: '首页', path: '/' },
    { name: '关于我们', path: '/about' },
    { name: '服务介绍', path: '/services' },
    { name: '案例展示', path: '/cases' },
    { name: '团队介绍', path: '/team' },
    { name: '新闻动态', path: '/news' },
    { name: '招贤纳士', path: '/careers' },
    { name: '联系我们', path: '/contact' },
  ])

  const isMobileMenuOpen = ref(false)

  const toggleMobileMenu = () => {
    isMobileMenuOpen.value = !isMobileMenuOpen.value
  }

  const closeMobileMenu = () => {
    isMobileMenuOpen.value = false
  }

  return {
    navItems,
    isMobileMenuOpen,
    toggleMobileMenu,
    closeMobileMenu,
  }
})

// 服务数据
export const useServiceStore = defineStore('service', () => {
  const services = ref<Service[]>([
    {
      id: 'miniapp',
      title: '小程序定制开发',
      subtitle: '微信/支付宝/抖音小程序',
      description: '为电商、餐饮、教育、医疗等行业提供定制化小程序解决方案，助力企业快速触达用户，实现业务增长。',
      icon: 'MiniProgram',
      features: [
        '多端适配，一次开发多端运行',
        '原生性能体验，流畅不卡顿',
        '快速迭代，支持热更新',
        '丰富的API和组件库',
      ],
      process: [
        { step: 1, title: '需求分析', description: '深入了解业务需求' },
        { step: 2, title: 'UI设计', description: '打造精美界面' },
        { step: 3, title: '开发实现', description: '高质量代码开发' },
        { step: 4, title: '测试上线', description: '全面测试后发布' },
        { step: 5, title: '运维支持', description: '持续技术支持' },
      ],
    },
    {
      id: 'app',
      title: 'APP定制开发',
      subtitle: 'iOS/Android原生开发',
      description: '提供iOS/Android原生开发和Flutter跨平台开发服务，为企业应用、社交应用、工具类应用打造极致体验。',
      icon: 'App',
      features: [
        '原生性能，极致用户体验',
        '跨平台开发，降低维护成本',
        '安全防护，数据加密传输',
        '离线功能，无网也能使用',
      ],
      process: [
        { step: 1, title: '需求分析', description: '深入了解业务需求' },
        { step: 2, title: 'UI设计', description: '打造精美界面' },
        { step: 3, title: '开发实现', description: '高质量代码开发' },
        { step: 4, title: '测试上线', description: '全面测试后发布' },
        { step: 5, title: '运维支持', description: '持续技术支持' },
      ],
    },
    {
      id: 'website',
      title: '网站定制开发',
      subtitle: '企业官网/电商平台/管理系统',
      description: '提供企业官网、电商平台、管理系统、H5活动页等网站开发服务，响应式设计，SEO优化，高性能。',
      icon: 'Website',
      features: [
        '响应式设计，适配各种设备',
        'SEO优化，提升搜索排名',
        '高性能加载，极速体验',
        '后台管理系统，便捷维护',
      ],
      process: [
        { step: 1, title: '需求分析', description: '深入了解业务需求' },
        { step: 2, title: 'UI设计', description: '打造精美界面' },
        { step: 3, title: '开发实现', description: '高质量代码开发' },
        { step: 4, title: '测试上线', description: '全面测试后发布' },
        { step: 5, title: '运维支持', description: '持续技术支持' },
      ],
    },
  ])

  const currentService = ref<string>('miniapp')

  const getServiceById = computed(() => (id: string) => {
    return services.value.find(s => s.id === id)
  })

  const setCurrentService = (id: string) => {
    currentService.value = id
  }

  return {
    services,
    currentService,
    getServiceById,
    setCurrentService,
  }
})

// 案例数据
export const useCaseStore = defineStore('case', () => {
  const cases = ref<Case[]>([
    {
      id: '1',
      title: '智慧零售电商平台',
      client: '某知名连锁品牌',
      industry: '电商',
      type: 'miniapp',
      cover: '/images/case-1.jpg',
      description: '为连锁品牌打造全渠道零售解决方案，实现线上线下一体化运营。',
      background: '客户拥有200+线下门店，希望打通线上线下会员体系，提升用户复购率。',
      solution: '开发微信小程序商城，集成会员系统、积分商城、优惠券功能，支持门店自提和配送。',
      result: '上线3个月，小程序月活用户突破50万，线上订单占比提升至35%。',
      techStack: ['微信小程序', 'Vue3', 'Node.js', 'MySQL'],
      duration: '2个月',
      testimonial: {
        content: '启云团队非常专业，从需求分析到上线交付都很高效，小程序运行稳定，用户体验很好。',
        author: '张经理',
        position: '数字化负责人',
        company: '某知名连锁品牌',
      },
    },
    {
      id: '2',
      title: '在线教育学习平台',
      client: '某教育科技公司',
      industry: '教育',
      type: 'app',
      cover: '/images/case-2.jpg',
      description: '打造K12在线教育平台，支持直播授课、录播课程、作业批改等功能。',
      background: '客户需要一款支持万人同时在线的教育APP，要求低延迟、高并发。',
      solution: '采用Flutter跨平台开发，集成实时音视频SDK，实现直播互动、白板共享、在线答题等功能。',
      result: '支持单场直播10万+并发，用户满意度达98%，成为区域头部教育平台。',
      techStack: ['Flutter', 'Dart', 'Go', 'Redis', 'MongoDB'],
      duration: '4个月',
      testimonial: {
        content: '技术实力过硬，解决了我们高并发的难题，直播体验非常流畅。',
        author: '李总监',
        position: '技术总监',
        company: '某教育科技公司',
      },
    },
    {
      id: '3',
      title: '企业数字化管理平台',
      client: '某制造企业',
      industry: '企业',
      type: 'website',
      cover: '/images/case-3.jpg',
      description: '为制造企业定制ERP管理系统，实现生产、库存、销售全流程数字化。',
      background: '企业生产流程复杂，数据分散在多个系统中，需要统一管理平台。',
      solution: '开发B/S架构管理系统，整合生产计划、物料管理、质量检测、销售订单等模块。',
      result: '生产效率提升30%，库存周转率提升25%，数据准确率提升至99%。',
      techStack: ['Vue3', 'TypeScript', 'Spring Boot', 'PostgreSQL'],
      duration: '6个月',
    },
    {
      id: '4',
      title: '智慧医疗预约系统',
      client: '某三甲医院',
      industry: '医疗',
      type: 'miniapp',
      cover: '/images/case-4.jpg',
      description: '开发医院预约挂号小程序，支持在线问诊、报告查询、缴费等功能。',
      background: '医院门诊量大，患者排队时间长，需要线上化解决方案。',
      solution: '对接医院HIS系统，开发微信小程序实现预约挂号、在线支付、报告查询等功能。',
      result: '线上预约率达80%，平均候诊时间缩短60%，患者满意度大幅提升。',
      techStack: ['微信小程序', 'Java', 'Oracle'],
      duration: '3个月',
    },
    {
      id: '5',
      title: '餐饮外卖配送平台',
      client: '某餐饮连锁',
      industry: '餐饮',
      type: 'app',
      cover: '/images/case-5.jpg',
      description: '打造餐饮外卖平台，包含用户端、商家端、骑手端三端应用。',
      background: '客户希望自建外卖渠道，降低平台抽成，提升利润空间。',
      solution: '开发三端APP，集成地图定位、智能派单、实时追踪等功能。',
      result: '自建渠道订单占比达45%，每单利润提升15%。',
      techStack: ['React Native', 'Node.js', 'MongoDB'],
      duration: '5个月',
    },
    {
      id: '6',
      title: '品牌官网 redesign',
      client: '某科技公司',
      industry: '企业',
      type: 'website',
      cover: '/images/case-6.jpg',
      description: '为科技公司重新设计品牌官网，提升品牌形象和用户体验。',
      background: '原官网设计老旧，移动端体验差，需要全新升级。',
      solution: '采用现代设计风格，响应式布局，优化页面加载速度，提升SEO。',
      result: '网站跳出率降低40%，平均停留时间提升2倍，询盘量增长60%。',
      techStack: ['Vue3', 'Nuxt.js', 'Tailwind CSS'],
      duration: '2个月',
    },
  ])

  const filterIndustry = ref<string>('all')
  const filterType = ref<string>('all')

  const filteredCases = computed(() => {
    return cases.value.filter(c => {
      const matchIndustry = filterIndustry.value === 'all' || c.industry === filterIndustry.value
      const matchType = filterType.value === 'all' || c.type === filterType.value
      return matchIndustry && matchType
    })
  })

  const getCaseById = computed(() => (id: string) => {
    return cases.value.find(c => c.id === id)
  })

  const relatedCases = computed(() => (id: string) => {
    const currentCase = cases.value.find(c => c.id === id)
    if (!currentCase) return []
    return cases.value
      .filter(c => c.id !== id && (c.industry === currentCase.industry || c.type === currentCase.type))
      .slice(0, 3)
  })

  const setFilter = (industry: string, type: string) => {
    filterIndustry.value = industry
    filterType.value = type
  }

  return {
    cases,
    filterIndustry,
    filterType,
    filteredCases,
    getCaseById,
    relatedCases,
    setFilter,
  }
})

// 新闻数据
export const useNewsStore = defineStore('news', () => {
  const news = ref<News[]>([
    {
      id: '1',
      title: '启云科技荣获"2024年度优秀软件企业"称号',
      summary: '凭借卓越的技术实力和优质的服务，启云科技在年度评选中脱颖而出，荣获优秀软件企业称号。',
      content: '详细内容...',
      cover: '/images/news-1.jpg',
      category: 'company',
      publishDate: '2024-03-01',
      author: '启云科技',
    },
    {
      id: '2',
      title: '2024年企业数字化转型趋势报告',
      summary: '深入分析当前企业数字化转型的新趋势，为企业的数字化升级提供参考。',
      content: '详细内容...',
      cover: '/images/news-2.jpg',
      category: 'industry',
      publishDate: '2024-02-28',
      author: '启云研究院',
    },
    {
      id: '3',
      title: 'Vue3 Composition API最佳实践',
      summary: '分享在大型项目中使用Vue3 Composition API的经验和最佳实践。',
      content: '详细内容...',
      cover: '/images/news-3.jpg',
      category: 'tech',
      publishDate: '2024-02-20',
      author: '技术团队',
    },
    {
      id: '4',
      title: '启云科技乔迁新址，开启发展新篇章',
      summary: '随着业务规模不断扩大，启云科技正式迁入新办公地址，为员工提供更好的工作环境。',
      content: '详细内容...',
      cover: '/images/news-4.jpg',
      category: 'company',
      publishDate: '2024-02-15',
      author: '启云科技',
    },
    {
      id: '5',
      title: '小程序开发新规范解读',
      summary: '解读微信小程序最新开发规范，帮助开发者更好地适配新规则。',
      content: '详细内容...',
      cover: '/images/news-5.jpg',
      category: 'tech',
      publishDate: '2024-02-10',
      author: '技术团队',
    },
    {
      id: '6',
      title: 'AI技术在软件开发中的应用前景',
      summary: '探讨人工智能技术如何改变软件开发流程，提升开发效率。',
      content: '详细内容...',
      cover: '/images/news-6.jpg',
      category: 'industry',
      publishDate: '2024-02-05',
      author: '启云研究院',
    },
  ])

  const filterCategory = ref<string>('all')

  const filteredNews = computed(() => {
    if (filterCategory.value === 'all') return news.value
    return news.value.filter(n => n.category === filterCategory.value)
  })

  const getNewsById = computed(() => (id: string) => {
    return news.value.find(n => n.id === id)
  })

  const relatedNews = computed(() => (id: string) => {
    const currentNews = news.value.find(n => n.id === id)
    if (!currentNews) return []
    return news.value
      .filter(n => n.id !== id && n.category === currentNews.category)
      .slice(0, 3)
  })

  const setFilter = (category: string) => {
    filterCategory.value = category
  }

  return {
    news,
    filterCategory,
    filteredNews,
    getNewsById,
    relatedNews,
    setFilter,
  }
})

// 职位数据
export const useJobStore = defineStore('job', () => {
  const jobs = ref<Job[]>([
    {
      id: '1',
      title: '高级前端工程师',
      department: '技术部',
      location: '重庆',
      salary: '15K-25K',
      tags: ['急招', '新职位'],
      responsibilities: [
        '负责公司核心产品的前端架构设计和开发',
        '优化前端性能，提升用户体验',
        '参与技术选型和代码评审',
        '指导初中级工程师成长',
      ],
      requirements: [
        '本科及以上学历，计算机相关专业',
        '5年以上前端开发经验',
        '精通Vue3/React，熟悉TypeScript',
        '有大型项目架构经验者优先',
      ],
      benefits: [
        '五险一金',
        '年终奖',
        '带薪年假',
        '技术分享会',
        '团建活动',
      ],
    },
    {
      id: '2',
      title: 'Java后端工程师',
      department: '技术部',
      location: '重庆',
      salary: '12K-20K',
      tags: ['热招'],
      responsibilities: [
        '负责后端服务的设计和开发',
        '数据库设计和优化',
        'API接口开发和文档编写',
        '系统性能优化',
      ],
      requirements: [
        '本科及以上学历，计算机相关专业',
        '3年以上Java开发经验',
        '熟悉Spring Boot、MySQL、Redis',
        '有微服务架构经验者优先',
      ],
      benefits: [
        '五险一金',
        '年终奖',
        '带薪年假',
        '技术培训',
        '弹性工作',
      ],
    },
    {
      id: '3',
      title: 'UI设计师',
      department: '设计部',
      location: '重庆',
      salary: '10K-18K',
      tags: [],
      responsibilities: [
        '负责产品界面设计和视觉优化',
        '参与产品需求讨论，提供设计建议',
        '制定设计规范，维护设计系统',
        '与开发团队协作，确保设计落地',
      ],
      requirements: [
        '本科及以上学历，设计相关专业',
        '3年以上UI设计经验',
        '熟练使用Figma、Sketch等设计工具',
        '有完整的设计作品集',
      ],
      benefits: [
        '五险一金',
        '年终奖',
        '带薪年假',
        '设计分享会',
        '节日福利',
      ],
    },
    {
      id: '4',
      title: '产品经理',
      department: '产品部',
      location: '重庆',
      salary: '15K-25K',
      tags: ['急招'],
      responsibilities: [
        '负责产品规划和需求分析',
        '撰写PRD文档，推进项目落地',
        '协调设计、开发、测试等团队',
        '跟踪产品数据，持续优化',
      ],
      requirements: [
        '本科及以上学历',
        '3年以上B端产品经验',
        '有软件开发行业背景优先',
        '优秀的沟通和协调能力',
      ],
      benefits: [
        '五险一金',
        '年终奖',
        '带薪年假',
        '产品培训',
        '晋升空间',
      ],
    },
    {
      id: '5',
      title: '测试工程师',
      department: '技术部',
      location: '重庆',
      salary: '8K-15K',
      tags: [],
      responsibilities: [
        '负责产品功能测试和性能测试',
        '编写测试用例和测试报告',
        '推动自动化测试建设',
        '跟踪Bug修复，保障产品质量',
      ],
      requirements: [
        '本科及以上学历',
        '2年以上软件测试经验',
        '熟悉测试流程和方法',
        '有自动化测试经验者优先',
      ],
      benefits: [
        '五险一金',
        '年终奖',
        '带薪年假',
        '技能培训',
        '团队活动',
      ],
    },
    {
      id: '6',
      title: '运营专员',
      department: '运营部',
      location: '重庆',
      salary: '6K-10K',
      tags: ['新职位'],
      responsibilities: [
        '负责公司新媒体账号运营',
        '策划和执行线上推广活动',
        '撰写宣传文案和案例包装',
        '分析运营数据，优化策略',
      ],
      requirements: [
        '本科及以上学历',
        '1年以上运营经验',
        '良好的文案撰写能力',
        '熟悉社交媒体运营',
      ],
      benefits: [
        '五险一金',
        '年终奖',
        '带薪年假',
        '运营培训',
        '年轻团队',
      ],
    },
  ])

  const filterDepartment = ref<string>('all')

  const departments = computed(() => {
    const depts = new Set(jobs.value.map(j => j.department))
    return ['all', ...Array.from(depts)]
  })

  const filteredJobs = computed(() => {
    if (filterDepartment.value === 'all') return jobs.value
    return jobs.value.filter(j => j.department === filterDepartment.value)
  })

  const getJobById = computed(() => (id: string) => {
    return jobs.value.find(j => j.id === id)
  })

  const setFilter = (department: string) => {
    filterDepartment.value = department
  }

  return {
    jobs,
    departments,
    filterDepartment,
    filteredJobs,
    getJobById,
    setFilter,
  }
})

// 团队数据
export const useTeamStore = defineStore('team', () => {
  const members = ref<TeamMember[]>([
    {
      id: '1',
      name: '张明',
      position: '创始人 & CEO',
      avatar: '/images/team-1.jpg',
      description: '10年互联网从业经验，曾任职于知名互联网公司，对软件开发和团队管理有深刻理解。',
    },
    {
      id: '2',
      name: '李华',
      position: '技术总监',
      avatar: '/images/team-2.jpg',
      description: '全栈技术专家，精通前后端开发，主导过多个大型项目的技术架构设计。',
    },
    {
      id: '3',
      name: '王芳',
      position: '产品总监',
      avatar: '/images/team-3.jpg',
      description: '8年产品经验，擅长B端产品规划和用户体验设计，推动多款成功产品上线。',
    },
    {
      id: '4',
      name: '陈杰',
      position: '设计总监',
      avatar: '/images/team-4.jpg',
      description: '资深UI/UX设计师，对设计趋势有敏锐洞察，作品多次获得设计奖项。',
    },
  ])

  const milestones = ref<Milestone[]>([
    { year: '2019', title: '公司成立', description: '启云科技在重庆正式成立，开启创业征程' },
    { year: '2020', title: '团队扩张', description: '团队规模突破20人，服务客户超过50家' },
    { year: '2021', title: '技术升级', description: '全面拥抱云原生技术，提升交付效率' },
    { year: '2022', title: '业务突破', description: '年营收突破千万，成为区域头部服务商' },
    { year: '2023', title: '产品化转型', description: '推出自研SaaS产品，拓展服务边界' },
    { year: '2024', title: '持续创新', description: '探索AI技术应用，为客户创造更大价值' },
  ])

  const stats = ref({
    employees: 50,
    techRatio: 70,
    clients: 200,
    cases: 50,
    years: 5,
  })

  return {
    members,
    milestones,
    stats,
  }
})
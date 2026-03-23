// 服务类型
export interface Service {
  id: string
  title: string
  subtitle: string
  description: string
  icon: string
  features: string[]
  process: ProcessStep[]
}

export interface ProcessStep {
  step: number
  title: string
  description: string
}

// 案例类型
export interface Case {
  id: string
  title: string
  client: string
  industry: string
  type: 'miniapp' | 'app' | 'website'
  cover: string
  description: string
  background?: string
  solution?: string
  result?: string
  testimonial?: Testimonial
  techStack?: string[]
  duration?: string
}

export interface Testimonial {
  content: string
  author: string
  position: string
  company: string
}

// 新闻类型
export interface News {
  id: string
  title: string
  summary: string
  content: string
  cover: string
  category: 'company' | 'industry' | 'tech'
  publishDate: string
  author: string
}

// 职位类型
export interface Job {
  id: string
  title: string
  department: string
  location: string
  salary: string
  tags: string[]
  responsibilities: string[]
  requirements: string[]
  benefits: string[]
}

// 团队成员类型
export interface TeamMember {
  id: string
  name: string
  position: string
  avatar: string
  description: string
}

// 里程碑类型
export interface Milestone {
  year: string
  title: string
  description: string
}

// 联系表单类型
export interface ContactForm {
  name: string
  phone: string
  email: string
  type: 'miniapp' | 'app' | 'website' | 'other'
  description: string
}

// 导航项类型
export interface NavItem {
  name: string
  path: string
  children?: NavItem[]
}
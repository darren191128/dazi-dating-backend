<template>
  <div class="min-h-screen bg-gray-100">
    <!-- Admin Header -->
    <header class="bg-primary text-white">
      <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div class="flex justify-between items-center h-16">
          <div class="flex items-center">
            <h1 class="text-xl font-bold">管理后台</h1>
          </div>
          <div class="flex items-center space-x-4">
            <span class="text-sm">欢迎，{{ adminName }}</span>
            <button @click="logout" class="text-sm hover:text-accent">
              退出登录
            </button>
          </div>
        </div>
      </div>
    </header>
    
    <div class="flex">
      <!-- Sidebar -->
      <aside class="w-64 bg-white shadow-sm min-h-screen">
        <nav class="p-4">
          <ul class="space-y-2">
            <li>
              <router-link 
                to="/admin/dashboard"
                class="block px-4 py-2 rounded-lg hover:bg-gray-100"
                :class="{ 'bg-primary text-white hover:bg-primary-dark': $route.path === '/admin/dashboard' }"
              >
                控制台
              </router-link>
            </li>
            <li>
              <router-link 
                to="/admin/lawyers"
                class="block px-4 py-2 rounded-lg hover:bg-gray-100"
                :class="{ 'bg-primary text-white hover:bg-primary-dark': $route.path === '/admin/lawyers' }"
              >
                律师管理
              </router-link>
            </li>
            <li>
              <router-link 
                to="/admin/cases"
                class="block px-4 py-2 rounded-lg hover:bg-gray-100"
                :class="{ 'bg-primary text-white hover:bg-primary-dark': $route.path === '/admin/cases' }"
              >
                案例管理
              </router-link>
            </li>
            <li>
              <router-link 
                to="/admin/news"
                class="block px-4 py-2 rounded-lg hover:bg-gray-100"
                :class="{ 'bg-primary text-white hover:bg-primary-dark': $route.path === '/admin/news' }"
              >
                新闻管理
              </router-link>
            </li>
            <li>
              <router-link 
                to="/admin/careers"
                class="block px-4 py-2 rounded-lg hover:bg-gray-100"
                :class="{ 'bg-primary text-white hover:bg-primary-dark': $route.path === '/admin/careers' }"
              >
                招聘管理
              </router-link>
            </li>
            <li>
              <router-link 
                to="/admin/contacts"
                class="block px-4 py-2 rounded-lg hover:bg-gray-100"
                :class="{ 'bg-primary text-white hover:bg-primary-dark': $route.path === '/admin/contacts' }"
              >
                咨询管理
              </router-link>
            </li>
          </ul>
        </nav>
      </aside>
      
      <!-- Main Content -->
      <main class="flex-1 p-8">
        <div class="bg-white rounded-xl shadow-sm p-6">
          <h2 class="text-2xl font-bold text-primary mb-4">控制台</h2>
          <p class="text-gray-600">欢迎使用若山律师事务所管理后台</p>
          
          <!-- Stats Cards -->
          <div class="grid grid-cols-1 md:grid-cols-4 gap-6 mt-8">
            <div class="bg-primary/10 rounded-lg p-6">
              <div class="text-3xl font-bold text-primary">6</div>
              <div class="text-gray-600">律师数量</div>
            </div>
            <div class="bg-accent/10 rounded-lg p-6">
              <div class="text-3xl font-bold text-accent">6</div>
              <div class="text-gray-600">案例数量</div>
            </div>
            <div class="bg-green-100 rounded-lg p-6">
              <div class="text-3xl font-bold text-green-600">6</div>
              <div class="text-gray-600">新闻数量</div>
            </div>
            <div class="bg-blue-100 rounded-lg p-6">
              <div class="text-3xl font-bold text-blue-600">3</div>
              <div class="text-gray-600">招聘职位</div>
            </div>
          </div>
        </div>
      </main>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()
const adminName = ref('管理员')

onMounted(() => {
  const userStr = localStorage.getItem('admin_user')
  if (userStr) {
    const user = JSON.parse(userStr)
    adminName.value = user.name
  } else {
    router.push('/admin/login')
  }
})

const logout = () => {
  localStorage.removeItem('admin_token')
  localStorage.removeItem('admin_user')
  router.push('/admin/login')
}
</script>

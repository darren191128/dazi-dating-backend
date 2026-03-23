<template>
  <div class="min-h-screen bg-gray-100 flex items-center justify-center">
    <div class="bg-white rounded-xl shadow-lg p-8 w-full max-w-md">
      <div class="text-center mb-8">
        <h1 class="text-2xl font-bold text-primary">管理后台登录</h1>
        <p class="text-gray-600 mt-2">若山律师事务所管理系统</p>
      </div>
      
      <form @submit.prevent="handleLogin" class="space-y-6">
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-2">用户名</label>
          <input 
            v-model="form.username"
            type="text"
            required
            class="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-primary focus:border-transparent"
            placeholder="请输入用户名"
          />
        </div>
        
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-2">密码</label>
          <input 
            v-model="form.password"
            type="password"
            required
            class="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-primary focus:border-transparent"
            placeholder="请输入密码"
          />
        </div>
        
        <div v-if="error" class="bg-red-50 text-red-600 p-3 rounded-lg text-sm">
          {{ error }}
        </div>
        
        <button 
          type="submit"
          class="w-full btn-primary py-3"
          :disabled="isLoading"
        >
          {{ isLoading ? '登录中...' : '登录' }}
        </button>
      </form>
      
      <div class="mt-6 text-center text-sm text-gray-500">
        <p>默认账号: admin / admin123</p>
        <p>测试账号: test / test123</p>
      </div>
      
      <div class="mt-4 text-center">
        <router-link to="/" class="text-primary text-sm hover:underline">
          ← 返回网站首页
        </router-link>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()

const form = ref({
  username: '',
  password: ''
})

const error = ref('')
const isLoading = ref(false)

const handleLogin = async () => {
  error.value = ''
  isLoading.value = true
  
  try {
    // 这里应该调用后端API
    // const response = await apiClient.post('/admin/login', form.value)
    
    // 模拟登录成功
    if (form.value.username === 'admin' && form.value.password === 'admin123') {
      localStorage.setItem('admin_token', 'mock_token')
      localStorage.setItem('admin_user', JSON.stringify({
        username: 'admin',
        name: '系统管理员',
        role: 'SUPER_ADMIN'
      }))
      router.push('/admin/dashboard')
    } else if (form.value.username === 'test' && form.value.password === 'test123') {
      localStorage.setItem('admin_token', 'mock_token')
      localStorage.setItem('admin_user', JSON.stringify({
        username: 'test',
        name: '测试管理员',
        role: 'ADMIN'
      }))
      router.push('/admin/dashboard')
    } else {
      error.value = '用户名或密码错误'
    }
  } catch (e) {
    error.value = '登录失败，请稍后重试'
  } finally {
    isLoading.value = false
  }
}
</script>

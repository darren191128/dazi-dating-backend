<template>
  <div class="p-6">
    <h1 class="text-2xl font-bold mb-6">系统设置</h1>
    <el-form :model="settings" label-width="120px">
      <el-form-item label="网站名称">
        <el-input v-model="settings.siteName" />
      </el-form-item>
      <el-form-item label="网站Logo">
        <el-input v-model="settings.logo" />
      </el-form-item>
      <el-form-item label="SEO标题">
        <el-input v-model="settings.seoTitle" />
      </el-form-item>
      <el-form-item label="SEO描述">
        <el-input v-model="settings.seoDescription" type="textarea" rows="3" />
      </el-form-item>
      <el-form-item label="备案号">
        <el-input v-model="settings.icp" />
      </el-form-item>
      <el-form-item label="联系电话">
        <el-input v-model="settings.phone" />
      </el-form-item>
      <el-form-item label="联系邮箱">
        <el-input v-model="settings.email" />
      </el-form-item>
      <el-form-item label="公司地址">
        <el-input v-model="settings.address" type="textarea" rows="2" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="handleSave">保存设置</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
const settings = ref({
  siteName: '若山律师事务所',
  logo: '',
  seoTitle: '',
  seoDescription: '',
  icp: '',
  phone: '',
  email: '',
  address: ''
})
const handleSave = () => {}
</script>

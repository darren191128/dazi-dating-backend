<template>
  <div class="p-6">
    <div class="flex justify-between items-center mb-6">
      <h1 class="text-2xl font-bold">咨询管理</h1>
    </div>
    <el-table :data="contactsList" v-loading="loading">
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="name" label="姓名" width="120" />
      <el-table-column prop="phone" label="电话" width="150" />
      <el-table-column prop="content" label="咨询内容" />
      <el-table-column prop="status" label="状态" width="100">
        <template #default="{ row }">
          <el-tag :type="row.status === 'replied' ? 'success' : 'warning'">
            {{ row.status === 'replied' ? '已回复' : '待处理' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="200">
        <template #default="{ row }">
          <el-button size="small" @click="handleReply(row)">回复</el-button>
          <el-button size="small" type="success" @click="handleMark(row)">标记</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
const loading = ref(false)
const contactsList = ref([])
const handleReply = (row: any) => {}
const handleMark = (row: any) => {}
</script>

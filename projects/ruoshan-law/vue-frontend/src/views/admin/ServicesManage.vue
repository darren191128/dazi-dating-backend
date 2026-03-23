<template>
  <div class="p-6">
    <div class="flex justify-between items-center mb-6">
      <h1 class="text-2xl font-bold">服务管理</h1>
      <el-button type="primary" @click="handleAdd">新增服务</el-button>
    </div>
    <el-table :data="servicesList" v-loading="loading">
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="name" label="服务名称" />
      <el-table-column prop="description" label="服务简介" />
      <el-table-column label="操作" width="200">
        <template #default="{ row }">
          <el-button size="small" @click="handleEdit(row)">编辑</el-button>
          <el-button size="small" type="danger" @click="handleDelete(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
const loading = ref(false)
const servicesList = ref([])
const handleAdd = () => {}
const handleEdit = (row: any) => {}
const handleDelete = (row: any) => {}
</script>

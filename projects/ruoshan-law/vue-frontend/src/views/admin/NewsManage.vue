<template>
  <div class="p-6">
    <div class="flex justify-between items-center mb-6">
      <h1 class="text-2xl font-bold">新闻管理</h1>
      <el-button type="primary" @click="handleAdd">新增新闻</el-button>
    </div>
    
    <el-table :data="newsList" v-loading="loading">
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="title" label="标题" />
      <el-table-column prop="category" label="分类" width="120" />
      <el-table-column prop="publishDate" label="发布日期" width="180" />
      <el-table-column prop="status" label="状态" width="100">
        <template #default="{ row }">
          <el-tag :type="row.status === 'published' ? 'success' : 'info'">
            {{ row.status === 'published' ? '已发布' : '草稿' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="200">
        <template #default="{ row }">
          <el-button size="small" @click="handleEdit(row)">编辑</el-button>
          <el-button size="small" type="danger" @click="handleDelete(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    
    <el-pagination
      class="mt-4"
      background
      layout="prev, pager, next"
      :total="total"
      @current-change="handlePageChange"
    />
    
    <!-- 编辑对话框 -->
    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="600px">
      <el-form :model="form" label-width="80px">
        <el-form-item label="标题">
          <el-input v-model="form.title" />
        </el-form-item>
        <el-form-item label="分类">
          <el-select v-model="form.category">
            <el-option label="律所动态" value="firm" />
            <el-option label="行业资讯" value="industry" />
            <el-option label="法律知识" value="knowledge" />
          </el-select>
        </el-form-item>
        <el-form-item label="内容">
          <el-input v-model="form.content" type="textarea" rows="6" />
        </el-form-item>
        <el-form-item label="状态">
          <el-radio-group v-model="form.status">
            <el-radio label="published">发布</el-radio>
            <el-radio label="draft">草稿</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSave">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'

const loading = ref(false)
const newsList = ref([])
const total = ref(0)
const dialogVisible = ref(false)
const dialogTitle = ref('新增新闻')
const form = ref({
  id: null,
  title: '',
  category: 'firm',
  content: '',
  status: 'draft'
})

const fetchNews = async () => {
  loading.value = true
  // TODO: 调用API获取新闻列表
  loading.value = false
}

const handleAdd = () => {
  dialogTitle.value = '新增新闻'
  form.value = { id: null, title: '', category: 'firm', content: '', status: 'draft' }
  dialogVisible.value = true
}

const handleEdit = (row: any) => {
  dialogTitle.value = '编辑新闻'
  form.value = { ...row }
  dialogVisible.value = true
}

const handleSave = async () => {
  // TODO: 调用API保存
  ElMessage.success('保存成功')
  dialogVisible.value = false
  fetchNews()
}

const handleDelete = async (row: any) => {
  try {
    await ElMessageBox.confirm('确定删除该新闻？', '提示', { type: 'warning' })
    // TODO: 调用API删除
    ElMessage.success('删除成功')
    fetchNews()
  } catch {
    // 取消删除
  }
}

const handlePageChange = (page: number) => {
  fetchNews()
}

onMounted(fetchNews)
</script>

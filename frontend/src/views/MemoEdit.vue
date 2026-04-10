<template>
  <div>
    <h1 class="page-title">{{ isEdit ? '编辑备忘录' : '新建备忘录' }}</h1>
    
    <div class="card">
      <el-form ref="formRef" :model="form" label-width="80px">
        <el-form-item label="标题" prop="title">
          <el-input v-model="form.title" placeholder="请输入标题" maxlength="200" show-word-limit />
        </el-form-item>
        
        <el-form-item label="分类">
          <el-select v-model="form.categoryId" placeholder="选择分类" style="width: 100%;">
            <el-option label="无分类" :value="null" />
            <el-option
              v-for="cat in categories"
              :key="cat.id"
              :label="cat.name"
              :value="cat.id"
            />
          </el-select>
        </el-form-item>
        
        <el-form-item label="内容">
          <el-input
            v-model="form.content"
            type="textarea"
            :rows="10"
            placeholder="请输入内容..."
            maxlength="2000"
            show-word-limit
          />
        </el-form-item>
        
        <el-form-item>
          <el-button type="primary" :loading="loading" @click="handleSubmit">保存</el-button>
          <el-button @click="$router.back()">取消</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import request from '../utils/request'
import { ElMessage } from 'element-plus'

const route = useRoute()
const router = useRouter()
const formRef = ref()
const loading = ref(false)
const categories = ref([])

const isEdit = ref(!!route.params.id)

const form = ref({
  title: '',
  categoryId: null,
  content: ''
})

const fetchCategories = async () => {
  categories.value = await request.get('/category/list')
}

const fetchMemoDetail = async () => {
  const data = await request.get(`/memo/detail/${route.params.id}`)
  form.value = {
    title: data.title,
    categoryId: data.categoryId,
    content: data.content
  }
}

const handleSubmit = async () => {
  if (!form.value.title) {
    ElMessage.warning('请输入标题')
    return
  }
  
  loading.value = true
  try {
    if (isEdit.value) {
      await request.put(`/memo/update/${route.params.id}`, form.value)
      ElMessage.success('修改成功')
    } else {
      await request.post('/memo/add', form.value)
      ElMessage.success('创建成功')
    }
    router.push('/home')
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  fetchCategories()
  if (isEdit.value) {
    fetchMemoDetail()
  }
})
</script>

<template>
  <div class="memo-form-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>{{ isEdit ? '编辑备忘录' : '添加备忘录' }}</span>
        </div>
      </template>

      <el-form
        ref="formRef"
        :model="form"
        :rules="rules"
        label-width="80px"
        class="memo-form"
      >
        <el-form-item label="标题" prop="title">
          <el-input
            v-model="form.title"
            placeholder="请输入标题"
            maxlength="200"
            show-word-limit
          />
        </el-form-item>

        <el-form-item label="分类">
          <el-select v-model="form.categoryId" placeholder="选择分类" clearable>
            <el-option
              v-for="item in categoryList"
              :key="item.id"
              :label="item.name"
              :value="item.id"
            />
          </el-select>
        </el-form-item>

        <el-form-item label="优先级">
          <el-radio-group v-model="form.priority">
            <el-radio-button :label="0">普通</el-radio-button>
            <el-radio-button :label="1">重要</el-radio-button>
            <el-radio-button :label="2">紧急</el-radio-button>
          </el-radio-group>
        </el-form-item>

        <el-form-item label="提醒时间">
          <el-date-picker
            v-model="form.remindTime"
            type="datetime"
            placeholder="选择提醒时间"
            format="YYYY-MM-DD HH:mm"
            value-format="YYYY-MM-DDTHH:mm:ss"
          />
        </el-form-item>

        <el-form-item label="内容">
          <el-input
            v-model="form.content"
            type="textarea"
            :rows="8"
            placeholder="请输入备忘录内容"
            maxlength="2000"
            show-word-limit
          />
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="handleSubmit" :loading="loading">
            {{ isEdit ? '保存' : '创建' }}
          </el-button>
          <el-button @click="goBack">返回</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { addMemo, updateMemo, getMemoDetail } from '@/api/memo'
import { getCategoryList } from '@/api/category'

const route = useRoute()
const router = useRouter()
const formRef = ref()
const loading = ref(false)
const categoryList = ref([])

const isEdit = computed(() => !!route.params.id)

const form = reactive({
  title: '',
  categoryId: null,
  priority: 0,
  content: '',
  remindTime: null
})

const rules = {
  title: [
    { required: true, message: '请输入标题', trigger: 'blur' },
    { min: 1, max: 200, message: '长度在 1 到 200 个字符', trigger: 'blur' }
  ]
}

const fetchCategoryList = async () => {
  const res = await getCategoryList()
  categoryList.value = res.data
}

const fetchMemoDetail = async () => {
  if (!isEdit.value) return
  const res = await getMemoDetail(route.params.id)
  Object.assign(form, res.data)
}

const handleSubmit = async () => {
  const valid = await formRef.value?.validate().catch(() => false)
  if (!valid) return

  loading.value = true
  try {
    if (isEdit.value) {
      await updateMemo(route.params.id, form)
      ElMessage.success('更新成功')
    } else {
      await addMemo(form)
      ElMessage.success('创建成功')
    }
    router.push('/memo')
  } finally {
    loading.value = false
  }
}

const goBack = () => {
  router.back()
}

onMounted(() => {
  fetchCategoryList()
  fetchMemoDetail()
})
</script>

<style scoped>
.memo-form-container {
  max-width: 800px;
  margin: 0 auto;
}

.card-header {
  font-size: 16px;
  font-weight: 500;
}

.memo-form {
  padding: 20px 0;
}

@media (max-width: 768px) {
  .memo-form :deep(.el-form-item__label) {
    float: none;
    display: block;
    text-align: left;
    padding: 0 0 10px;
  }
  
  .memo-form :deep(.el-form-item__content) {
    margin-left: 0 !important;
  }
}
</style>

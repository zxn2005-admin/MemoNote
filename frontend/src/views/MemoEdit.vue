<template>
  <div class="memo-edit-page max-w-3xl mx-auto">
    <div class="flex items-center mb-6">
      <el-button :icon="ArrowLeft" @click="router.back()">返回</el-button>
      <h1 class="text-2xl font-bold text-gray-800 ml-4">
        {{ isEdit ? '编辑备忘录' : '新增备忘录' }}
      </h1>
    </div>

    <el-card>
      <el-form ref="formRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="标题" prop="title">
          <el-input v-model="form.title" placeholder="请输入标题" maxlength="200" show-word-limit />
        </el-form-item>

        <el-form-item label="分类">
          <el-select v-model="form.categoryId" placeholder="请选择分类" clearable class="w-full">
            <el-option
              v-for="cat in categories"
              :key="cat.id"
              :label="cat.name"
              :value="cat.id"
            >
              <span class="flex items-center">
                <span
                  class="w-3 h-3 rounded-full mr-2"
                  :style="{ backgroundColor: cat.color }"
                />
                {{ cat.name }}
              </span>
            </el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="优先级">
          <el-radio-group v-model="form.priority">
            <el-radio :value="0">普通</el-radio>
            <el-radio :value="1">重要</el-radio>
            <el-radio :value="2">紧急</el-radio>
          </el-radio-group>
        </el-form-item>

        <el-form-item label="状态">
          <el-radio-group v-model="form.status">
            <el-radio :value="0">未完成</el-radio>
            <el-radio :value="1">已完成</el-radio>
          </el-radio-group>
        </el-form-item>

        <el-form-item label="提醒时间">
          <el-date-picker
            v-model="form.reminderTime"
            type="datetime"
            placeholder="选择提醒时间"
            format="YYYY-MM-DD HH:mm"
            value-format="YYYY-MM-DD HH:mm:ss"
            class="w-full"
          />
        </el-form-item>

        <el-form-item label="标签">
          <el-select
            v-model="tagList"
            multiple
            filterable
            allow-create
            default-first-option
            placeholder="输入标签后回车添加"
            class="w-full"
            @change="handleTagChange"
          />
        </el-form-item>

        <el-form-item label="内容" prop="content">
          <el-input
            v-model="form.content"
            type="textarea"
            placeholder="请输入备忘录内容..."
            :rows="8"
            maxlength="5000"
            show-word-limit
          />
        </el-form-item>

        <el-form-item>
          <el-button type="primary" :loading="loading" @click="handleSubmit">
            {{ isEdit ? '保存修改' : '创建备忘录' }}
          </el-button>
          <el-button @click="router.back()">取消</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import { ArrowLeft } from '@element-plus/icons-vue'
import { memoApi } from '@/api/memo'
import { categoryApi } from '@/api/category'

const router = useRouter()
const route = useRoute()

const formRef = ref(null)
const loading = ref(false)
const categories = ref([])
const tagList = ref([])

const memoId = computed(() => route.params.id)
const isEdit = computed(() => !!memoId.value)

const form = reactive({
  title: '',
  content: '',
  categoryId: null,
  priority: 0,
  status: 0,
  reminderTime: null,
  tags: ''
})

const rules = {
  title: [
    { required: true, message: '请输入标题', trigger: 'blur' }
  ]
}

const loadCategories = async () => {
  try {
    const res = await categoryApi.getList()
    if (res.code === 200) {
      categories.value = res.data
    }
  } catch (error) {
    console.error(error)
  }
}

const loadMemo = async () => {
  if (!isEdit.value) return
  
  try {
    const res = await memoApi.getById(memoId.value)
    if (res.code === 200) {
      Object.assign(form, res.data)
      if (res.data.tags) {
        try {
          tagList.value = JSON.parse(res.data.tags)
        } catch {
          tagList.value = []
        }
      }
    }
  } catch (error) {
    console.error(error)
    ElMessage.error('加载备忘录失败')
    router.back()
  }
}

const handleTagChange = (tags) => {
  form.tags = JSON.stringify(tags)
}

const handleSubmit = async () => {
  if (!formRef.value) return
  
  await formRef.value.validate(async (valid) => {
    if (valid) {
      loading.value = true
      try {
        let res
        if (isEdit.value) {
          res = await memoApi.update(memoId.value, form)
        } else {
          res = await memoApi.create(form)
        }
        
        if (res.code === 200) {
          ElMessage.success(isEdit.value ? '修改成功' : '创建成功')
          router.push('/app/home')
        } else {
          ElMessage.error(res.message || '操作失败')
        }
      } catch (error) {
        console.error(error)
      } finally {
        loading.value = false
      }
    }
  })
}

onMounted(() => {
  loadCategories()
  loadMemo()
})
</script>

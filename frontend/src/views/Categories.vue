<template>
  <div>
    <div class="flex-between mb-20">
      <h1 class="page-title">分类管理</h1>
      <el-button type="primary" @click="showAddDialog = true">
        <el-icon><Plus /></el-icon>
        新增分类
      </el-button>
    </div>

    <div class="card" v-if="categories.length === 0">
      <el-empty description="暂无分类" />
    </div>

    <div class="category-list" v-else>
      <div v-for="cat in categories" :key="cat.id" class="category-item card">
        <div class="category-left">
          <div class="category-color" :style="{ background: cat.color }"></div>
          <span class="category-name">{{ cat.name }}</span>
        </div>
        <div class="category-actions">
          <el-button type="primary" icon="Edit" size="small" circle @click="handleEdit(cat)" />
          <el-button type="danger" icon="Delete" size="small" circle @click="handleDelete(cat)" />
        </div>
      </div>
    </div>

    <el-dialog v-model="showAddDialog" :title="editCategory ? '编辑分类' : '新增分类'" width="400px">
      <el-form label-width="80px">
        <el-form-item label="分类名称">
          <el-input v-model="categoryForm.name" placeholder="请输入分类名称" maxlength="50" />
        </el-form-item>
        <el-form-item label="分类颜色">
          <el-color-picker v-model="categoryForm.color" show-alpha />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showAddDialog = false">取消</el-button>
        <el-button type="primary" :loading="loading" @click="saveCategory">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import request from '../utils/request'
import { ElMessage, ElMessageBox } from 'element-plus'

const loading = ref(false)
const categories = ref([])
const showAddDialog = ref(false)
const editCategory = ref(null)

const categoryForm = ref({
  name: '',
  color: '#409EFF'
})

const fetchCategories = async () => {
  categories.value = await request.get('/category/list')
}

const handleEdit = (cat) => {
  editCategory.value = cat
  categoryForm.value = {
    name: cat.name,
    color: cat.color
  }
  showAddDialog.value = true
}

const handleDelete = async (cat) => {
  try {
    await ElMessageBox.confirm(`确定要删除分类"${cat.name}"吗？`, '提示', {
      type: 'warning'
    })
    await request.delete(`/category/delete/${cat.id}`)
    ElMessage.success('删除成功')
    fetchCategories()
  } catch (e) {
  }
}

const saveCategory = async () => {
  if (!categoryForm.value.name) {
    ElMessage.warning('请输入分类名称')
    return
  }
  
  loading.value = true
  try {
    if (editCategory.value) {
      await request.put(`/category/update/${editCategory.value.id}`, categoryForm.value)
      ElMessage.success('修改成功')
    } else {
      await request.post('/category/add', categoryForm.value)
      ElMessage.success('创建成功')
    }
    showAddDialog.value = false
    fetchCategories()
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  fetchCategories()
})
</script>

<style scoped>
.category-list {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.category-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.category-left {
  display: flex;
  align-items: center;
  gap: 15px;
}

.category-color {
  width: 24px;
  height: 24px;
  border-radius: 4px;
}

.category-name {
  font-size: 16px;
  font-weight: 500;
  color: #303133;
}

.category-actions {
  display: flex;
  gap: 10px;
}
</style>

<template>
  <div>
    <div class="flex-between mb-20">
      <h1 class="page-title">我的备忘录</h1>
      <el-button type="primary" @click="$router.push('/memo/add')">
        <el-icon><Plus /></el-icon>
        新建备忘
      </el-button>
    </div>

    <div class="card">
      <div class="search-bar">
        <el-input
          v-model="keyword"
          placeholder="搜索标题或内容..."
          prefix-icon="Search"
          clearable
          style="max-width: 300px; flex: 1;"
          @input="searchMemos"
        />
        <el-select v-model="selectedCategory" placeholder="选择分类" clearable @change="searchMemos" style="width: 150px;">
          <el-option label="全部" :value="null" />
          <el-option
            v-for="cat in categories"
            :key="cat.id"
            :label="cat.name"
            :value="cat.id"
          />
        </el-select>
      </div>
    </div>

    <el-empty v-if="memos.length === 0 && !loading" description="暂无备忘录" />

    <div v-else class="memo-list">
      <div
        v-for="memo in memos"
        :key="memo.id"
        class="memo-card card"
        :class="{ 'is-complete': memo.isComplete, 'is-top': memo.isTop }"
      >
        <div class="memo-header">
          <div class="memo-title">
            <el-tag
              v-if="getCategory(memo.categoryId)"
              :color="getCategory(memo.categoryId).color"
              size="small"
              style="margin-right: 10px;"
            >
              {{ getCategory(memo.categoryId).name }}
            </el-tag>
            <span :class="{ 'complete-text': memo.isComplete }">{{ memo.title }}</span>
            <el-icon v-if="memo.isTop" style="color: #f56c6c;"><Top /></el-icon>
          </div>
          <div class="memo-actions">
            <el-button
              :type="memo.isComplete ? 'success' : ''"
              :icon="memo.isComplete ? Check : CircleCheck"
              size="small"
              circle
              @click="toggleComplete(memo)"
            />
            <el-button
              :type="memo.isTop ? 'danger' : ''"
              icon="Top"
              size="small"
              circle
              @click="toggleTop(memo)"
            />
            <el-button
              type="primary"
              icon="Edit"
              size="small"
              circle
              @click="$router.push(`/memo/edit/${memo.id}`)"
            />
            <el-button
              type="danger"
              icon="Delete"
              size="small"
              circle
              @click="deleteMemo(memo)"
            />
          </div>
        </div>
        <div class="memo-content">
          {{ memo.content || '暂无内容' }}
        </div>
        <div class="memo-footer">
          <span class="memo-time">
            <el-icon><Clock /></el-icon>
            {{ formatTime(memo.createTime) }}
          </span>
        </div>
      </div>
    </div>

    <div class="pagination-wrap" v-if="total > pageSize">
      <el-pagination
        v-model:current-page="page"
        :page-size="pageSize"
        :total="total"
        layout="total, prev, pager, next"
        @current-change="fetchMemos"
      />
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import request from '../utils/request'
import { ElMessage, ElMessageBox } from 'element-plus'

const loading = ref(false)
const keyword = ref('')
const selectedCategory = ref(null)
const categories = ref([])
const memos = ref([])
const page = ref(1)
const pageSize = ref(10)
const total = ref(0)

const fetchCategories = async () => {
  categories.value = await request.get('/category/list')
}

const fetchMemos = async () => {
  loading.value = true
  try {
    const res = await request.get('/memo/list', {
      params: {
        page: page.value,
        size: pageSize.value,
        keyword: keyword.value,
        categoryId: selectedCategory.value
      }
    })
    memos.value = res.records
    total.value = res.total
  } finally {
    loading.value = false
  }
}

const searchMemos = () => {
  page.value = 1
  fetchMemos()
}

const getCategory = (id) => {
  return categories.value.find(c => c.id === id)
}

const toggleTop = async (memo) => {
  await request.put(`/memo/toggle-top/${memo.id}`)
  memo.isTop = !memo.isTop
  ElMessage.success(memo.isTop ? '已置顶' : '取消置顶')
}

const toggleComplete = async (memo) => {
  await request.put(`/memo/toggle-complete/${memo.id}`)
  memo.isComplete = !memo.isComplete
  ElMessage.success(memo.isComplete ? '已标记完成' : '取消完成')
}

const deleteMemo = async (memo) => {
  try {
    await ElMessageBox.confirm('确定要删除这条备忘录吗？', '提示', {
      type: 'warning'
    })
    await request.delete(`/memo/delete/${memo.id}`)
    ElMessage.success('删除成功')
    fetchMemos()
  } catch (e) {
  }
}

const formatTime = (time) => {
  if (!time) return ''
  return new Date(time).toLocaleString('zh-CN')
}

onMounted(() => {
  fetchCategories()
  fetchMemos()
})
</script>

<style scoped>
.search-bar {
  display: flex;
  gap: 15px;
  flex-wrap: wrap;
}

.memo-list {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.memo-card {
  transition: all 0.2s;
}

.memo-card:hover {
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.15);
}

.memo-card.is-top {
  border-left: 4px solid #f56c6c;
}

.memo-card.is-complete {
  opacity: 0.7;
  background: #f5f7fa;
}

.complete-text {
  text-decoration: line-through;
  color: #909399;
}

.memo-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 15px;
  flex-wrap: wrap;
  gap: 10px;
}

.memo-title {
  display: flex;
  align-items: center;
  font-size: 18px;
  font-weight: 600;
  color: #303133;
  flex: 1;
  min-width: 200px;
}

.memo-actions {
  display: flex;
  gap: 5px;
}

.memo-content {
  color: #606266;
  line-height: 1.6;
  margin-bottom: 15px;
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.memo-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding-top: 10px;
  border-top: 1px solid #ebeef5;
}

.memo-time {
  display: flex;
  align-items: center;
  gap: 5px;
  color: #909399;
  font-size: 12px;
}

.pagination-wrap {
  display: flex;
  justify-content: center;
  margin-top: 20px;
}

@media (max-width: 768px) {
  .memo-actions {
    width: 100%;
    justify-content: flex-end;
  }
  
  .memo-title {
    font-size: 16px;
  }
}
</style>

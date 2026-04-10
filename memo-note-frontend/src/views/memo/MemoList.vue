<template>
  <div class="memo-list-container">
    <el-row :gutter="20" class="statistics-row">
      <el-col :xs="24" :sm="8">
        <el-card class="stat-card">
          <div class="stat-item">
            <el-icon :size="40" color="#409EFF"><Document /></el-icon>
            <div class="stat-info">
              <div class="stat-value">{{ statistics.totalMemo || 0 }}</div>
              <div class="stat-label">全部备忘</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :xs="24" :sm="8">
        <el-card class="stat-card">
          <div class="stat-item">
            <el-icon :size="40" color="#67C23A"><CircleCheck /></el-icon>
            <div class="stat-info">
              <div class="stat-value">{{ statistics.completedMemo || 0 }}</div>
              <div class="stat-label">已完成</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :xs="24" :sm="8">
        <el-card class="stat-card">
          <div class="stat-item">
            <el-icon :size="40" color="#E6A23C"><Clock /></el-icon>
            <div class="stat-info">
              <div class="stat-value">{{ statistics.pendingMemo || 0 }}</div>
              <div class="stat-label">待完成</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-card class="filter-card">
      <el-form :inline="true" :model="queryForm">
        <el-form-item label="关键词">
          <el-input
            v-model="queryForm.keyword"
            placeholder="搜索标题或内容"
            clearable
            @keyup.enter="handleSearch"
          />
        </el-form-item>
        <el-form-item label="分类">
          <el-select v-model="queryForm.categoryId" placeholder="选择分类" clearable>
            <el-option
              v-for="item in categoryList"
              :key="item.id"
              :label="item.name"
              :value="item.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="queryForm.status" placeholder="选择状态" clearable>
            <el-option label="未完成" :value="0" />
            <el-option label="已完成" :value="1" />
          </el-select>
        </el-form-item>
        <el-form-item label="优先级">
          <el-select v-model="queryForm.priority" placeholder="选择优先级" clearable>
            <el-option label="普通" :value="0" />
            <el-option label="重要" :value="1" />
            <el-option label="紧急" :value="2" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">
            <el-icon><Search /></el-icon>搜索
          </el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card class="memo-card">
      <template #header>
        <div class="card-header">
          <span>备忘录列表</span>
          <el-button type="primary" @click="goAdd">
            <el-icon><Plus /></el-icon>添加备忘
          </el-button>
        </div>
      </template>

      <el-empty v-if="memoList.length === 0" description="暂无备忘录" />
      
      <div v-else class="memo-list">
        <div
          v-for="item in memoList"
          :key="item.id"
          class="memo-item"
          :class="{ completed: item.status === 1 }"
        >
          <div class="memo-header">
            <div class="memo-title-wrapper">
              <el-checkbox
                :model-value="item.status === 1"
                @change="() => toggleStatus(item)"
              />
              <span class="memo-title">{{ item.title }}</span>
              <el-tag
                v-if="item.priority === 2"
                type="danger"
                size="small"
                effect="dark"
              >紧急</el-tag>
              <el-tag
                v-else-if="item.priority === 1"
                type="warning"
                size="small"
                effect="dark"
              >重要</el-tag>
            </div>
            <div class="memo-actions">
              <el-button type="primary" link @click="goEdit(item.id)">
                <el-icon><Edit /></el-icon>
              </el-button>
              <el-button type="danger" link @click="handleDelete(item)">
                <el-icon><Delete /></el-icon>
              </el-button>
            </div>
          </div>
          
          <div class="memo-content" v-if="item.content">{{ item.content }}</div>
          
          <div class="memo-footer">
            <el-tag
              v-if="item.categoryName"
              :color="item.categoryColor"
              effect="dark"
              size="small"
            >{{ item.categoryName }}</el-tag>
            <span class="memo-time">{{ formatTime(item.createTime) }}</span>
          </div>
        </div>
      </div>

      <div class="pagination-wrapper">
        <el-pagination
          v-model:current-page="queryForm.pageNum"
          v-model:page-size="queryForm.pageSize"
          :page-sizes="[10, 20, 50]"
          :total="total"
          layout="total, sizes, prev, pager, next"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  Document, CircleCheck, Clock, Search, Plus, Edit, Delete
} from '@element-plus/icons-vue'
import { getMemoPage, deleteMemo, toggleMemoStatus, getStatistics } from '@/api/memo'
import { getCategoryList } from '@/api/category'
import dayjs from 'dayjs'

const router = useRouter()
const memoList = ref([])
const categoryList = ref([])
const total = ref(0)
const statistics = ref({})

const queryForm = reactive({
  keyword: '',
  categoryId: null,
  status: null,
  priority: null,
  pageNum: 1,
  pageSize: 10
})

const formatTime = (time) => {
  return dayjs(time).format('YYYY-MM-DD HH:mm')
}

const fetchMemoList = async () => {
  const res = await getMemoPage(queryForm)
  memoList.value = res.data.records
  total.value = res.data.total
}

const fetchCategoryList = async () => {
  const res = await getCategoryList()
  categoryList.value = res.data
}

const fetchStatistics = async () => {
  const res = await getStatistics()
  statistics.value = res.data
}

const handleSearch = () => {
  queryForm.pageNum = 1
  fetchMemoList()
}

const handleReset = () => {
  queryForm.keyword = ''
  queryForm.categoryId = null
  queryForm.status = null
  queryForm.priority = null
  queryForm.pageNum = 1
  fetchMemoList()
}

const handleSizeChange = (val) => {
  queryForm.pageSize = val
  fetchMemoList()
}

const handleCurrentChange = (val) => {
  queryForm.pageNum = val
  fetchMemoList()
}

const goAdd = () => {
  router.push('/memo/add')
}

const goEdit = (id) => {
  router.push(`/memo/edit/${id}`)
}

const toggleStatus = async (item) => {
  await toggleMemoStatus(item.id)
  ElMessage.success('状态更新成功')
  fetchMemoList()
  fetchStatistics()
}

const handleDelete = (item) => {
  ElMessageBox.confirm('确定要删除该备忘录吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    await deleteMemo(item.id)
    ElMessage.success('删除成功')
    fetchMemoList()
    fetchStatistics()
  })
}

onMounted(() => {
  fetchMemoList()
  fetchCategoryList()
  fetchStatistics()
})
</script>

<style scoped>
.statistics-row {
  margin-bottom: 20px;
}

.stat-card {
  margin-bottom: 10px;
}

.stat-item {
  display: flex;
  align-items: center;
}

.stat-info {
  margin-left: 15px;
}

.stat-value {
  font-size: 24px;
  font-weight: bold;
  color: #303133;
}

.stat-label {
  font-size: 14px;
  color: #909399;
  margin-top: 5px;
}

.filter-card {
  margin-bottom: 20px;
}

.memo-card {
  min-height: 400px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.memo-list {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.memo-item {
  padding: 15px;
  border: 1px solid #ebeef5;
  border-radius: 8px;
  transition: all 0.3s;
}

.memo-item:hover {
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.memo-item.completed {
  background-color: #f5f7fa;
}

.memo-item.completed .memo-title {
  text-decoration: line-through;
  color: #909399;
}

.memo-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
}

.memo-title-wrapper {
  display: flex;
  align-items: center;
  gap: 10px;
}

.memo-title {
  font-size: 16px;
  font-weight: 500;
  color: #303133;
}

.memo-content {
  color: #606266;
  font-size: 14px;
  line-height: 1.6;
  margin-bottom: 10px;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.memo-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.memo-time {
  font-size: 12px;
  color: #909399;
}

.pagination-wrapper {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}

@media (max-width: 768px) {
  .memo-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 10px;
  }
  
  .memo-actions {
    align-self: flex-end;
  }
}
</style>

<template>
  <div class="home-page">
    <div class="flex flex-col sm:flex-row justify-between items-start sm:items-center mb-6 gap-4">
      <div>
        <h1 class="text-2xl font-bold text-gray-800">我的备忘录</h1>
        <p class="text-gray-500 mt-1">共 {{ total }} 条备忘录</p>
      </div>
      
      <div class="flex flex-wrap gap-2">
        <el-input
          v-model="searchKeyword"
          placeholder="搜索备忘录..."
          class="w-48 sm:w-64"
          clearable
          @keyup.enter="handleSearch"
          @clear="loadMemos"
        >
          <template #append>
            <el-button :icon="Search" @click="handleSearch" />
          </template>
        </el-input>
        
        <el-button type="primary" :icon="Plus" @click="router.push('/app/memo/add')">
          新增备忘录
        </el-button>
      </div>
    </div>

    <div class="flex flex-col lg:flex-row gap-6">
      <div class="lg:w-64 flex-shrink-0">
        <el-card class="mb-4">
          <template #header>
            <span class="font-medium">筛选条件</span>
          </template>
          
          <div class="space-y-4">
            <div>
              <div class="text-sm text-gray-500 mb-2">分类</div>
              <el-select
                v-model="filter.categoryId"
                placeholder="全部分类"
                clearable
                class="w-full"
                @change="loadMemos"
              >
                <el-option
                  v-for="cat in categories"
                  :key="cat.id"
                  :label="cat.name"
                  :value="cat.id"
                />
              </el-select>
            </div>
            
            <div>
              <div class="text-sm text-gray-500 mb-2">状态</div>
              <el-select
                v-model="filter.status"
                placeholder="全部状态"
                clearable
                class="w-full"
                @change="loadMemos"
              >
                <el-option label="未完成" :value="0" />
                <el-option label="已完成" :value="1" />
              </el-select>
            </div>
            
            <div>
              <div class="text-sm text-gray-500 mb-2">优先级</div>
              <el-select
                v-model="filter.priority"
                placeholder="全部优先级"
                clearable
                class="w-full"
                @change="loadMemos"
              >
                <el-option label="普通" :value="0" />
                <el-option label="重要" :value="1" />
                <el-option label="紧急" :value="2" />
              </el-select>
            </div>
          </div>
        </el-card>

        <el-card>
          <template #header>
            <span class="font-medium">统计信息</span>
          </template>
          <div class="space-y-3">
            <div class="flex justify-between">
              <span class="text-gray-500">总备忘录</span>
              <span class="font-medium">{{ stats.total || 0 }}</span>
            </div>
            <div class="flex justify-between">
              <span class="text-gray-500">已完成</span>
              <span class="font-medium text-green-500">{{ stats.completed || 0 }}</span>
            </div>
            <div class="flex justify-between">
              <span class="text-gray-500">未完成</span>
              <span class="font-medium text-orange-500">{{ stats.pending || 0 }}</span>
            </div>
          </div>
        </el-card>
      </div>

      <div class="flex-1">
        <div v-if="loading" class="flex justify-center py-12">
          <el-icon class="animate-spin text-4xl text-blue-500">
            <Loading />
          </el-icon>
        </div>

        <div v-else-if="memoList.length === 0" class="text-center py-12">
          <el-empty description="暂无备忘录">
            <el-button type="primary" @click="router.push('/app/memo/add')">
              创建第一条备忘录
            </el-button>
          </el-empty>
        </div>

        <div v-else class="grid gap-4">
          <el-card
            v-for="memo in memoList"
            :key="memo.id"
            class="memo-card cursor-pointer"
            :class="{
              'priority-high': memo.priority === 2,
              'priority-medium': memo.priority === 1,
              'priority-low': memo.priority === 0
            }"
            @click="router.push(`/app/memo/edit/${memo.id}`)"
          >
            <div class="flex justify-between items-start">
              <div class="flex-1">
                <div class="flex items-center gap-2 mb-2">
                  <el-tag
                    v-if="memo.categoryName"
                    :color="memo.categoryColor"
                    effect="dark"
                    size="small"
                  >
                    {{ memo.categoryName }}
                  </el-tag>
                  <el-tag
                    :type="memo.priority === 2 ? 'danger' : memo.priority === 1 ? 'warning' : 'info'"
                    size="small"
                  >
                    {{ priorityText(memo.priority) }}
                  </el-tag>
                  <el-tag
                    :type="memo.status === 1 ? 'success' : 'info'"
                    size="small"
                  >
                    {{ memo.status === 1 ? '已完成' : '未完成' }}
                  </el-tag>
                </div>
                
                <h3 class="text-lg font-medium mb-2" :class="{ 'line-through text-gray-400': memo.status === 1 }">
                  {{ memo.title }}
                </h3>
                
                <p class="text-gray-500 text-sm line-clamp-2">
                  {{ memo.content || '暂无内容' }}
                </p>
                
                <div class="text-gray-400 text-xs mt-2">
                  {{ formatDate(memo.createTime) }}
                </div>
              </div>
              
              <div class="flex gap-2 ml-4" @click.stop>
                <el-button
                  v-if="memo.status === 0"
                  type="success"
                  size="small"
                  :icon="Check"
                  circle
                  @click="handleComplete(memo)"
                />
                <el-button
                  v-else
                  type="warning"
                  size="small"
                  :icon="RefreshLeft"
                  circle
                  @click="handleUncomplete(memo)"
                />
                <el-button
                  type="danger"
                  size="small"
                  :icon="Delete"
                  circle
                  @click="handleDelete(memo)"
                />
              </div>
            </div>
          </el-card>
        </div>

        <div v-if="total > pageSize" class="flex justify-center mt-6">
          <el-pagination
            v-model:current-page="currentPage"
            :page-size="pageSize"
            :total="total"
            layout="prev, pager, next"
            @current-change="loadMemos"
          />
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search, Plus, Check, RefreshLeft, Delete, Loading } from '@element-plus/icons-vue'
import { memoApi } from '@/api/memo'
import { categoryApi } from '@/api/category'

const router = useRouter()

const loading = ref(false)
const memoList = ref([])
const categories = ref([])
const stats = ref({})

const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)
const searchKeyword = ref('')

const filter = reactive({
  categoryId: null,
  status: null,
  priority: null
})

const priorityText = (priority) => {
  const map = { 0: '普通', 1: '重要', 2: '紧急' }
  return map[priority] || '普通'
}

const formatDate = (date) => {
  if (!date) return ''
  return new Date(date).toLocaleString('zh-CN')
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

const loadMemos = async () => {
  loading.value = true
  try {
    const res = await memoApi.getList({
      page: currentPage.value,
      size: pageSize.value,
      ...filter
    })
    if (res.code === 200) {
      memoList.value = res.data.list
      total.value = res.data.total
    }
  } catch (error) {
    console.error(error)
  } finally {
    loading.value = false
  }
}

const loadStats = async () => {
  try {
    const res = await memoApi.getStats()
    if (res.code === 200) {
      stats.value = res.data
    }
  } catch (error) {
    console.error(error)
  }
}

const handleSearch = async () => {
  if (!searchKeyword.value.trim()) {
    loadMemos()
    return
  }
  
  loading.value = true
  try {
    const res = await memoApi.search({
      keyword: searchKeyword.value,
      page: currentPage.value,
      size: pageSize.value
    })
    if (res.code === 200) {
      memoList.value = res.data.list
      total.value = res.data.total
    }
  } catch (error) {
    console.error(error)
  } finally {
    loading.value = false
  }
}

const handleComplete = async (memo) => {
  try {
    const res = await memoApi.updateStatus(memo.id, 1)
    if (res.code === 200) {
      ElMessage.success('标记为已完成')
      loadMemos()
      loadStats()
    }
  } catch (error) {
    console.error(error)
  }
}

const handleUncomplete = async (memo) => {
  try {
    const res = await memoApi.updateStatus(memo.id, 0)
    if (res.code === 200) {
      ElMessage.success('标记为未完成')
      loadMemos()
      loadStats()
    }
  } catch (error) {
    console.error(error)
  }
}

const handleDelete = async (memo) => {
  try {
    await ElMessageBox.confirm('确定要删除这条备忘录吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    const res = await memoApi.delete(memo.id)
    if (res.code === 200) {
      ElMessage.success('删除成功')
      loadMemos()
      loadStats()
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error(error)
    }
  }
}

onMounted(() => {
  loadCategories()
  loadMemos()
  loadStats()
})
</script>

<template>
  <div class="admin-page">
    <h1 class="text-2xl font-bold text-gray-800 mb-6">管理后台</h1>

    <el-row :gutter="20" class="mb-6">
      <el-col :xs="24" :sm="12" :md="8">
        <el-card shadow="hover">
          <div class="text-center">
            <el-icon :size="40" class="text-blue-500 mb-2">
              <User />
            </el-icon>
            <div class="text-3xl font-bold">{{ stats.totalUsers || 0 }}</div>
            <div class="text-gray-500">用户总数</div>
          </div>
        </el-card>
      </el-col>
      <el-col :xs="24" :sm="12" :md="8">
        <el-card shadow="hover">
          <div class="text-center">
            <el-icon :size="40" class="text-green-500 mb-2">
              <Notebook />
            </el-icon>
            <div class="text-3xl font-bold">{{ stats.totalMemos || 0 }}</div>
            <div class="text-gray-500">备忘录总数</div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-card>
      <template #header>
        <div class="flex justify-between items-center">
          <span class="font-medium">用户管理</span>
          <el-button size="small" :icon="Refresh" @click="loadUsers">刷新</el-button>
        </div>
      </template>
      
      <el-table :data="users" stripe v-loading="loading">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="username" label="用户名" />
        <el-table-column prop="nickname" label="昵称" />
        <el-table-column prop="email" label="邮箱" />
        <el-table-column label="角色" width="100">
          <template #default="{ row }">
            <el-tag :type="row.role === 1 ? 'danger' : 'info'">
              {{ row.role === 1 ? '管理员' : '普通用户' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'danger'">
              {{ row.status === 1 ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="注册时间" width="180">
          <template #default="{ row }">
            {{ formatDate(row.createTime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="120">
          <template #default="{ row }">
            <el-button
              v-if="row.role !== 1"
              :type="row.status === 1 ? 'danger' : 'success'"
              size="small"
              @click="handleToggleStatus(row)"
            >
              {{ row.status === 1 ? '禁用' : '启用' }}
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="flex justify-center mt-4">
        <el-pagination
          v-model:current-page="currentPage"
          :page-size="pageSize"
          :total="total"
          layout="prev, pager, next"
          @current-change="loadUsers"
        />
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { User, Notebook, Refresh } from '@element-plus/icons-vue'
import { adminApi } from '@/api/admin'

const loading = ref(false)
const users = ref([])
const stats = ref({})
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)

const formatDate = (date) => {
  if (!date) return ''
  return new Date(date).toLocaleString('zh-CN')
}

const loadStats = async () => {
  try {
    const res = await adminApi.getStats()
    if (res.code === 200) {
      stats.value = res.data
    }
  } catch (error) {
    console.error(error)
  }
}

const loadUsers = async () => {
  loading.value = true
  try {
    const res = await adminApi.getUserList({
      page: currentPage.value,
      size: pageSize.value
    })
    if (res.code === 200) {
      users.value = res.data.list
      total.value = res.data.total
    }
  } catch (error) {
    console.error(error)
  } finally {
    loading.value = false
  }
}

const handleToggleStatus = async (user) => {
  const newStatus = user.status === 1 ? 0 : 1
  const action = newStatus === 0 ? '禁用' : '启用'
  
  try {
    await ElMessageBox.confirm(`确定要${action}用户 "${user.username}" 吗？`, '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    const res = await adminApi.updateUserStatus(user.id, newStatus)
    if (res.code === 200) {
      ElMessage.success(`${action}成功`)
      loadUsers()
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error(error)
    }
  }
}

onMounted(() => {
  loadStats()
  loadUsers()
})
</script>

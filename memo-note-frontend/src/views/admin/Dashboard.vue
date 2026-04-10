<template>
  <div class="admin-container">
    <el-row :gutter="20" class="statistics-row">
      <el-col :xs="24" :sm="12" :md="6">
        <el-card class="stat-card">
          <div class="stat-item">
            <el-icon :size="36" color="#409EFF"><User /></el-icon>
            <div class="stat-info">
              <div class="stat-value">{{ statistics.totalUsers || 0 }}</div>
              <div class="stat-label">总用户数</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :xs="24" :sm="12" :md="6">
        <el-card class="stat-card">
          <div class="stat-item">
            <el-icon :size="36" color="#67C23A"><Document /></el-icon>
            <div class="stat-info">
              <div class="stat-value">{{ statistics.totalMemos || 0 }}</div>
              <div class="stat-label">总备忘录</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :xs="24" :sm="12" :md="6">
        <el-card class="stat-card">
          <div class="stat-item">
            <el-icon :size="36" color="#E6A23C"><UserFilled /></el-icon>
            <div class="stat-info">
              <div class="stat-value">{{ statistics.adminCount || 0 }}</div>
              <div class="stat-label">管理员数</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-card>
      <template #header>
        <div class="card-header">
          <span>用户管理</span>
        </div>
      </template>

      <el-table :data="userList" v-loading="loading" stripe>
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="username" label="用户名" />
        <el-table-column prop="nickname" label="昵称">
          <template #default="{ row }">
            {{ row.nickname || '-' }}
          </template>
        </el-table-column>
        <el-table-column prop="email" label="邮箱">
          <template #default="{ row }">
            {{ row.email || '-' }}
          </template>
        </el-table-column>
        <el-table-column prop="role" label="角色" width="100">
          <template #default="{ row }">
            <el-tag v-if="row.role === 1" type="danger" size="small">管理员</el-tag>
            <el-tag v-else type="info" size="small">普通用户</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-switch
              v-model="row.status"
              :active-value="1"
              :inactive-value="0"
              @change="(val) => handleStatusChange(row, val)"
            />
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="注册时间" width="180">
          <template #default="{ row }">
            {{ formatTime(row.createTime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="120" fixed="right">
          <template #default="{ row }">
            <el-button type="danger" link @click="handleDelete(row)">
              <el-icon><Delete /></el-icon>删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { User, Document, UserFilled, Delete } from '@element-plus/icons-vue'
import { getUserList, updateUserStatus, deleteUser, getAdminStatistics } from '@/api/admin'
import dayjs from 'dayjs'

const userList = ref([])
const statistics = ref({})
const loading = ref(false)

const formatTime = (time) => {
  return time ? dayjs(time).format('YYYY-MM-DD HH:mm') : '-'
}

const fetchUserList = async () => {
  loading.value = true
  try {
    const res = await getUserList()
    userList.value = res.data
  } finally {
    loading.value = false
  }
}

const fetchStatistics = async () => {
  const res = await getAdminStatistics()
  statistics.value = res.data
}

const handleStatusChange = async (row, status) => {
  try {
    await updateUserStatus(row.id, status)
    ElMessage.success('状态更新成功')
  } catch {
    row.status = status === 1 ? 0 : 1
  }
}

const handleDelete = (row) => {
  ElMessageBox.confirm(`确定要删除用户 "${row.username}" 吗？`, '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    await deleteUser(row.id)
    ElMessage.success('删除成功')
    fetchUserList()
    fetchStatistics()
  })
}

onMounted(() => {
  fetchUserList()
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

.card-header {
  font-size: 16px;
  font-weight: 500;
}
</style>

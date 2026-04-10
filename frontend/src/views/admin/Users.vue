<template>
  <div>
    <h1 class="page-title">用户管理</h1>
    
    <div class="card">
      <div class="search-bar mb-20">
        <el-input
          v-model="keyword"
          placeholder="搜索用户名/邮箱..."
          prefix-icon="Search"
          clearable
          style="max-width: 300px;"
          @keyup.enter="fetchUsers"
        />
        <el-button type="primary" @click="fetchUsers">搜索</el-button>
      </div>
      
      <el-table :data="users" stripe style="width: 100%">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="username" label="用户名" />
        <el-table-column prop="email" label="邮箱" />
        <el-table-column prop="role" label="角色" width="100">
          <template #default="{ row }">
            <el-tag :type="row.role === 'admin' ? 'danger' : 'primary'" size="small">
              {{ row.role === 'admin' ? '管理员' : '用户' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="注册时间" width="180">
          <template #default="{ row }">
            {{ formatTime(row.createTime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="180">
          <template #default="{ row }">
            <el-button
              v-if="row.role !== 'admin'"
              type="primary"
              size="small"
              @click="setAdmin(row)"
            >
              设为管理员
            </el-button>
            <el-button
              v-if="row.role === 'admin' && row.username !== 'admin'"
              size="small"
              @click="cancelAdmin(row)"
            >
              取消管理员
            </el-button>
            <el-button
              v-if="row.username !== 'admin'"
              type="danger"
              size="small"
              @click="deleteUser(row)"
            >
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      
      <div class="pagination-wrap mt-20">
        <el-pagination
          v-model:current-page="page"
          :page-size="pageSize"
          :total="total"
          layout="total, prev, pager, next"
          @current-change="fetchUsers"
        />
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import request from '../../utils/request'
import { ElMessage, ElMessageBox } from 'element-plus'

const keyword = ref('')
const users = ref([])
const page = ref(1)
const pageSize = ref(10)
const total = ref(0)

const fetchUsers = async () => {
  const res = await request.get('/admin/user/list', {
    params: {
      page: page.value,
      size: pageSize.value,
      keyword: keyword.value
    }
  })
  users.value = res.records
  total.value = res.total
}

const setAdmin = async (user) => {
  try {
    await ElMessageBox.confirm(`确定将"${user.username}"设为管理员吗？`, '提示', {
      type: 'warning'
    })
    await request.put(`/admin/user/update-role/${user.id}`, { role: 'admin' })
    ElMessage.success('设置成功')
    fetchUsers()
  } catch (e) {
  }
}

const cancelAdmin = async (user) => {
  try {
    await ElMessageBox.confirm(`确定取消"${user.username}"的管理员权限吗？`, '提示', {
      type: 'warning'
    })
    await request.put(`/admin/user/update-role/${user.id}`, { role: 'user' })
    ElMessage.success('操作成功')
    fetchUsers()
  } catch (e) {
  }
}

const deleteUser = async (user) => {
  try {
    await ElMessageBox.confirm(`确定删除用户"${user.username}"吗？`, '提示', {
      type: 'warning'
    })
    await request.delete(`/admin/user/delete/${user.id}`)
    ElMessage.success('删除成功')
    fetchUsers()
  } catch (e) {
  }
}

const formatTime = (time) => {
  if (!time) return ''
  return new Date(time).toLocaleString('zh-CN')
}

onMounted(() => {
  fetchUsers()
})
</script>

<style scoped>
.search-bar {
  display: flex;
  gap: 15px;
}

.pagination-wrap {
  display: flex;
  justify-content: center;
}
</style>

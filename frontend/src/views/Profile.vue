<template>
  <div>
    <h1 class="page-title">个人中心</h1>
    
    <div class="card">
      <div class="profile-header">
        <el-avatar :src="user.avatar" :size="80" />
        <div class="profile-info">
          <h2>{{ user.username }}</h2>
          <p>角色：{{ user.role === 'admin' ? '管理员' : '普通用户' }}</p>
          <p>注册时间：{{ formatTime(user.createTime) }}</p>
        </div>
      </div>
    </div>
    
    <div class="card">
      <h3 style="margin-bottom: 20px;">修改信息</h3>
      <el-form :model="form" label-width="80px">
        <el-form-item label="邮箱">
          <el-input v-model="form.email" placeholder="请输入邮箱" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :loading="loading" @click="handleUpdate">保存修改</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import request from '../utils/request'
import { ElMessage } from 'element-plus'
import { useUserStore } from '../stores/user'

const userStore = useUserStore()
const loading = ref(false)

const user = ref({})
const form = ref({
  email: ''
})

const fetchUserInfo = async () => {
  user.value = await request.get('/user/info')
  form.value.email = user.value.email || ''
}

const handleUpdate = async () => {
  loading.value = true
  try {
    await request.put('/user/update', form.value)
    ElMessage.success('修改成功')
    userStore.fetchUserInfo()
  } finally {
    loading.value = false
  }
}

const formatTime = (time) => {
  if (!time) return ''
  return new Date(time).toLocaleString('zh-CN')
}

onMounted(() => {
  fetchUserInfo()
})
</script>

<style scoped>
.profile-header {
  display: flex;
  align-items: center;
  gap: 20px;
  padding: 20px 0;
}

.profile-info h2 {
  font-size: 24px;
  color: #303133;
  margin-bottom: 10px;
}

.profile-info p {
  color: #606266;
  margin: 5px 0;
}
</style>

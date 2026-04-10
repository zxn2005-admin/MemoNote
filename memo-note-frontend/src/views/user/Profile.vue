<template>
  <div class="profile-container">
    <el-row :gutter="20">
      <el-col :xs="24" :md="8">
        <el-card class="profile-card">
          <div class="profile-header">
            <el-avatar :size="80" :icon="UserFilled" />
            <h3>{{ userStore.userInfo?.nickname || userStore.userInfo?.username }}</h3>
            <p>{{ userStore.userInfo?.email || '未设置邮箱' }}</p>
            <el-tag v-if="userStore.isAdmin" type="danger" effect="dark">管理员</el-tag>
            <el-tag v-else type="info">普通用户</el-tag>
          </div>
          
          <div class="profile-info">
            <div class="info-item">
              <span class="label">用户名</span>
              <span class="value">{{ userStore.userInfo?.username }}</span>
            </div>
            <div class="info-item">
              <span class="label">注册时间</span>
              <span class="value">{{ formatTime(userStore.userInfo?.createTime) }}</span>
            </div>
          </div>
        </el-card>
      </el-col>
      
      <el-col :xs="24" :md="16">
        <el-card>
          <template #header>
            <div class="card-header">
              <span>编辑资料</span>
            </div>
          </template>
          
          <el-tabs v-model="activeTab">
            <el-tab-pane label="基本信息" name="basic">
              <el-form
                ref="basicFormRef"
                :model="basicForm"
                :rules="basicRules"
                label-width="100px"
                class="profile-form"
              >
                <el-form-item label="昵称" prop="nickname">
                  <el-input v-model="basicForm.nickname" placeholder="请输入昵称" />
                </el-form-item>
                
                <el-form-item label="邮箱" prop="email">
                  <el-input v-model="basicForm.email" placeholder="请输入邮箱" />
                </el-form-item>
                
                <el-form-item>
                  <el-button type="primary" @click="updateBasic" :loading="basicLoading">
                    保存修改
                  </el-button>
                </el-form-item>
              </el-form>
            </el-tab-pane>
            
            <el-tab-pane label="修改密码" name="password">
              <el-form
                ref="pwdFormRef"
                :model="pwdForm"
                :rules="pwdRules"
                label-width="100px"
                class="profile-form"
              >
                <el-form-item label="原密码" prop="oldPassword">
                  <el-input
                    v-model="pwdForm.oldPassword"
                    type="password"
                    placeholder="请输入原密码"
                    show-password
                  />
                </el-form-item>
                
                <el-form-item label="新密码" prop="newPassword">
                  <el-input
                    v-model="pwdForm.newPassword"
                    type="password"
                    placeholder="请输入新密码"
                    show-password
                  />
                </el-form-item>
                
                <el-form-item label="确认密码" prop="confirmPassword">
                  <el-input
                    v-model="pwdForm.confirmPassword"
                    type="password"
                    placeholder="请再次输入新密码"
                    show-password
                  />
                </el-form-item>
                
                <el-form-item>
                  <el-button type="primary" @click="updatePassword" :loading="pwdLoading">
                    修改密码
                  </el-button>
                </el-form-item>
              </el-form>
            </el-tab-pane>
          </el-tabs>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { UserFilled } from '@element-plus/icons-vue'
import { useUserStore } from '@/stores/user'
import { getProfile, updateProfile } from '@/api/user'
import dayjs from 'dayjs'

const userStore = useUserStore()
const activeTab = ref('basic')

const basicFormRef = ref()
const basicLoading = ref(false)
const basicForm = reactive({
  nickname: '',
  email: ''
})

const basicRules = {
  email: [
    { type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' }
  ]
}

const pwdFormRef = ref()
const pwdLoading = ref(false)
const pwdForm = reactive({
  oldPassword: '',
  newPassword: '',
  confirmPassword: ''
})

const validateConfirmPwd = (rule, value, callback) => {
  if (value !== pwdForm.newPassword) {
    callback(new Error('两次输入的密码不一致'))
  } else {
    callback()
  }
}

const pwdRules = {
  oldPassword: [
    { required: true, message: '请输入原密码', trigger: 'blur' }
  ],
  newPassword: [
    { required: true, message: '请输入新密码', trigger: 'blur' },
    { min: 6, max: 20, message: '长度在 6 到 20 个字符', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, validator: validateConfirmPwd, trigger: 'blur' }
  ]
}

const formatTime = (time) => {
  return time ? dayjs(time).format('YYYY-MM-DD HH:mm') : '-'
}

const fetchProfile = async () => {
  const res = await getProfile()
  basicForm.nickname = res.data.nickname || ''
  basicForm.email = res.data.email || ''
}

const updateBasic = async () => {
  const valid = await basicFormRef.value?.validate().catch(() => false)
  if (!valid) return

  basicLoading.value = true
  try {
    await updateProfile(basicForm)
    ElMessage.success('保存成功')
    userStore.fetchUserInfo()
  } finally {
    basicLoading.value = false
  }
}

const updatePassword = async () => {
  const valid = await pwdFormRef.value?.validate().catch(() => false)
  if (!valid) return

  pwdLoading.value = true
  try {
    await updateProfile({
      oldPassword: pwdForm.oldPassword,
      newPassword: pwdForm.newPassword
    })
    ElMessage.success('密码修改成功，请重新登录')
    pwdForm.oldPassword = ''
    pwdForm.newPassword = ''
    pwdForm.confirmPassword = ''
  } finally {
    pwdLoading.value = false
  }
}

onMounted(() => {
  fetchProfile()
})
</script>

<style scoped>
.profile-container {
  max-width: 1200px;
  margin: 0 auto;
}

.profile-card {
  margin-bottom: 20px;
}

.profile-header {
  text-align: center;
  padding: 20px 0;
  border-bottom: 1px solid #ebeef5;
}

.profile-header h3 {
  margin: 15px 0 5px;
  font-size: 18px;
  color: #303133;
}

.profile-header p {
  color: #909399;
  font-size: 14px;
  margin-bottom: 10px;
}

.profile-info {
  padding: 20px 0;
}

.info-item {
  display: flex;
  justify-content: space-between;
  padding: 12px 0;
  border-bottom: 1px solid #ebeef5;
}

.info-item:last-child {
  border-bottom: none;
}

.info-item .label {
  color: #909399;
}

.info-item .value {
  color: #606266;
}

.card-header {
  font-size: 16px;
  font-weight: 500;
}

.profile-form {
  padding: 20px 0;
  max-width: 500px;
}

@media (max-width: 768px) {
  .profile-form :deep(.el-form-item__label) {
    float: none;
    display: block;
    text-align: left;
    padding: 0 0 10px;
  }
  
  .profile-form :deep(.el-form-item__content) {
    margin-left: 0 !important;
  }
}
</style>

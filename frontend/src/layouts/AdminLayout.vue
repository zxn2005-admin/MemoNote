<template>
  <el-container class="layout-container">
    <el-header class="admin-header">
      <div class="header-content">
        <div class="logo">
          <el-icon><Setting /></el-icon>
          <span>管理后台</span>
        </div>
        <div class="header-right">
          <el-button @click="$router.push('/home')">返回前台</el-button>
          <el-dropdown @command="handleCommand">
            <div class="user-info">
              <el-avatar :src="userStore.user?.avatar" size="small" />
              <span class="username">{{ userStore.user?.username }}</span>
            </div>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item command="logout">退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </div>
    </el-header>
    <el-container>
      <el-aside width="220px" class="layout-aside">
        <el-menu
          :default-active="activeMenu"
          router
          class="nav-menu"
        >
          <el-menu-item index="/admin/dashboard">
            <el-icon><DataAnalysis /></el-icon>
            <span>数据概览</span>
          </el-menu-item>
          <el-menu-item index="/admin/users">
            <el-icon><User /></el-icon>
            <span>用户管理</span>
          </el-menu-item>
        </el-menu>
      </el-aside>
      <el-main class="layout-main">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useUserStore } from '../stores/user'
import { ElMessage, ElMessageBox } from 'element-plus'

const router = useRouter()
const route = useRoute()
const userStore = useUserStore()

const activeMenu = computed(() => route.path)

const handleCommand = async (command) => {
  if (command === 'logout') {
    try {
      await ElMessageBox.confirm('确定要退出登录吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
      userStore.logout()
      ElMessage.success('已退出登录')
      router.push('/login')
    } catch (e) {
    }
  }
}
</script>

<style scoped>
.layout-container {
  height: 100vh;
}

.admin-header {
  background: linear-gradient(135deg, #0f172a 0%, #1e293b 100%);
  padding: 0;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.15);
}

.header-content {
  max-width: 100%;
  height: 100%;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 24px;
}

.logo {
  display: flex;
  align-items: center;
  gap: 10px;
  color: #fff;
  font-size: 20px;
  font-weight: 600;
}

.header-right {
  display: flex;
  align-items: center;
  gap: 15px;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 8px;
  color: #fff;
  cursor: pointer;
  padding: 8px 12px;
  border-radius: 6px;
  transition: background 0.2s;
}

.user-info:hover {
  background: rgba(255, 255, 255, 0.2);
}

.layout-aside {
  background: #001529;
}

.nav-menu {
  border: none;
  background: #001529;
  height: 100%;
}

.nav-menu :deep(.el-menu-item) {
  color: rgba(255, 255, 255, 0.7);
}

.nav-menu :deep(.el-menu-item:hover),
.nav-menu :deep(.el-menu-item.is-active) {
  background: #1890ff;
  color: #fff;
}

.layout-main {
  background: #f0f2f5;
  padding: 24px;
  overflow-y: auto;
}

@media (max-width: 768px) {
  .layout-aside {
    width: 60px !important;
  }
  
  .nav-menu span {
    display: none;
  }
  
  .username {
    display: none;
  }
}
</style>

<template>
  <el-container class="h-full">
    <el-aside :width="isCollapse ? '64px' : '220px'" class="bg-white shadow-md">
      <div class="h-16 flex items-center justify-center border-b border-gray-200">
        <el-icon v-if="isCollapse" :size="24" class="text-blue-500">
          <Notebook />
        </el-icon>
        <span v-else class="text-xl font-bold text-blue-500">MemoNote</span>
      </div>
      <el-menu
        :default-active="activeMenu"
        :collapse="isCollapse"
        :collapse-transition="false"
        router
        class="border-none"
      >
        <el-menu-item index="/app/home">
          <el-icon><HomeFilled /></el-icon>
          <template #title>首页</template>
        </el-menu-item>
        <el-menu-item index="/app/category">
          <el-icon><Folder /></el-icon>
          <template #title>分类管理</template>
        </el-menu-item>
        <el-menu-item index="/app/profile">
          <el-icon><User /></el-icon>
          <template #title>个人中心</template>
        </el-menu-item>
        <el-menu-item v-if="userStore.isAdmin" index="/app/admin">
          <el-icon><Setting /></el-icon>
          <template #title>管理后台</template>
        </el-menu-item>
      </el-menu>
    </el-aside>

    <el-container>
      <el-header class="bg-white shadow-sm flex items-center justify-between px-4">
        <div class="flex items-center">
          <el-icon 
            :size="20" 
            class="cursor-pointer hover:text-blue-500 mr-4"
            @click="isCollapse = !isCollapse"
          >
            <Fold v-if="!isCollapse" />
            <Expand v-else />
          </el-icon>
          <el-breadcrumb separator="/">
            <el-breadcrumb-item :to="{ path: '/app/home' }">首页</el-breadcrumb-item>
            <el-breadcrumb-item v-if="currentRoute.meta?.title">
              {{ currentRoute.meta.title }}
            </el-breadcrumb-item>
          </el-breadcrumb>
        </div>
        
        <div class="flex items-center">
          <el-dropdown trigger="click">
            <div class="flex items-center cursor-pointer hover:text-blue-500">
              <el-avatar :size="32" class="mr-2">
                {{ userStore.user?.nickname?.charAt(0) || 'U' }}
              </el-avatar>
              <span class="hidden sm:inline">{{ userStore.user?.nickname }}</span>
              <el-icon class="ml-1"><ArrowDown /></el-icon>
            </div>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item @click="router.push('/app/profile')">
                  <el-icon><User /></el-icon>个人中心
                </el-dropdown-item>
                <el-dropdown-item divided @click="handleLogout">
                  <el-icon><SwitchButton /></el-icon>退出登录
                </el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </el-header>

      <el-main class="bg-gray-50 p-4">
        <router-view v-slot="{ Component }">
          <transition name="fade" mode="out-in">
            <component :is="Component" />
          </transition>
        </router-view>
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { ElMessageBox } from 'element-plus'

const router = useRouter()
const route = useRoute()
const userStore = useUserStore()

const isCollapse = ref(false)
const activeMenu = computed(() => route.path)
const currentRoute = computed(() => route)

const handleLogout = () => {
  ElMessageBox.confirm('确定要退出登录吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    userStore.logout()
    router.push('/login')
  })
}
</script>

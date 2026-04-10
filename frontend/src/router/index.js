import { createRouter, createWebHistory } from 'vue-router'
import { useUserStore } from '@/stores/user'

const routes = [
  {
    path: '/',
    redirect: '/login'
  },
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/Login.vue'),
    meta: { requiresAuth: false }
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import('@/views/Register.vue'),
    meta: { requiresAuth: false }
  },
  {
    path: '/app',
    component: () => import('@/layouts/MainLayout.vue'),
    redirect: '/app/home',
    meta: { requiresAuth: true },
    children: [
      {
        path: 'home',
        name: 'Home',
        component: () => import('@/views/Home.vue'),
        meta: { title: '首页' }
      },
      {
        path: 'memo/add',
        name: 'MemoAdd',
        component: () => import('@/views/MemoEdit.vue'),
        meta: { title: '新增备忘录' }
      },
      {
        path: 'memo/edit/:id',
        name: 'MemoEdit',
        component: () => import('@/views/MemoEdit.vue'),
        meta: { title: '编辑备忘录' }
      },
      {
        path: 'category',
        name: 'Category',
        component: () => import('@/views/Category.vue'),
        meta: { title: '分类管理' }
      },
      {
        path: 'profile',
        name: 'Profile',
        component: () => import('@/views/Profile.vue'),
        meta: { title: '个人中心' }
      },
      {
        path: 'admin',
        name: 'Admin',
        component: () => import('@/views/Admin.vue'),
        meta: { title: '管理后台', requiresAdmin: true }
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  const userStore = useUserStore()
  const isLoggedIn = userStore.isLoggedIn
  
  if (to.meta.requiresAuth && !isLoggedIn) {
    next('/login')
  } else if (to.meta.requiresAdmin && userStore.user?.role !== 1) {
    next('/app/home')
  } else if ((to.path === '/login' || to.path === '/register') && isLoggedIn) {
    next('/app/home')
  } else {
    next()
  }
})

export default router

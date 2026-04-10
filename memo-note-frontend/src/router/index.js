import { createRouter, createWebHistory } from 'vue-router'
import { useUserStore } from '@/stores/user'

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/Login.vue'),
    meta: { public: true }
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import('@/views/Register.vue'),
    meta: { public: true }
  },
  {
    path: '/',
    name: 'Layout',
    component: () => import('@/views/Layout.vue'),
    redirect: '/memo',
    children: [
      {
        path: '/memo',
        name: 'MemoList',
        component: () => import('@/views/memo/MemoList.vue'),
        meta: { title: '备忘录列表' }
      },
      {
        path: '/memo/add',
        name: 'MemoAdd',
        component: () => import('@/views/memo/MemoForm.vue'),
        meta: { title: '添加备忘录' }
      },
      {
        path: '/memo/edit/:id',
        name: 'MemoEdit',
        component: () => import('@/views/memo/MemoForm.vue'),
        meta: { title: '编辑备忘录' }
      },
      {
        path: '/profile',
        name: 'Profile',
        component: () => import('@/views/user/Profile.vue'),
        meta: { title: '个人中心' }
      },
      {
        path: '/admin',
        name: 'Admin',
        component: () => import('@/views/admin/Dashboard.vue'),
        meta: { title: '管理后台', admin: true }
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
  
  if (!to.meta.public && !userStore.token) {
    next('/login')
  } else if (to.meta.admin && !userStore.isAdmin) {
    next('/')
  } else {
    next()
  }
})

export default router

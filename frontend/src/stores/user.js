import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { authApi } from '@/api/auth'

export const useUserStore = defineStore('user', () => {
  const token = ref(localStorage.getItem('token') || '')
  const user = ref(JSON.parse(localStorage.getItem('user') || 'null'))

  const isLoggedIn = computed(() => !!token.value)
  const isAdmin = computed(() => user.value?.role === 1)

  function setToken(newToken) {
    token.value = newToken
    localStorage.setItem('token', newToken)
  }

  function setUser(newUser) {
    user.value = newUser
    localStorage.setItem('user', JSON.stringify(newUser))
  }

  async function login(loginData) {
    const res = await authApi.login(loginData)
    if (res.code === 200) {
      setToken(res.data.token)
      setUser(res.data.user)
    }
    return res
  }

  async function register(registerData) {
    return await authApi.register(registerData)
  }

  async function getUserInfo() {
    const res = await authApi.getUserInfo()
    if (res.code === 200) {
      setUser(res.data)
    }
    return res
  }

  function logout() {
    token.value = ''
    user.value = null
    localStorage.removeItem('token')
    localStorage.removeItem('user')
  }

  return {
    token,
    user,
    isLoggedIn,
    isAdmin,
    setToken,
    setUser,
    login,
    register,
    getUserInfo,
    logout
  }
})

import { defineStore } from 'pinia'
import { ref } from 'vue'
import request from '../utils/request'

export const useUserStore = defineStore('user', () => {
  const user = ref(null)

  const initUser = () => {
    const userStr = localStorage.getItem('user')
    if (userStr) {
      user.value = JSON.parse(userStr)
    }
  }

  const login = async (loginForm) => {
    const data = await request.post('/auth/login', loginForm)
    localStorage.setItem('token', data.token)
    localStorage.setItem('user', JSON.stringify(data))
    user.value = data
    return data
  }

  const register = async (registerForm) => {
    return await request.post('/auth/register', registerForm)
  }

  const logout = () => {
    localStorage.removeItem('token')
    localStorage.removeItem('user')
    user.value = null
  }

  const fetchUserInfo = async () => {
    const data = await request.get('/user/info')
    user.value = data
    localStorage.setItem('user', JSON.stringify(data))
  }

  return {
    user,
    initUser,
    login,
    register,
    logout,
    fetchUserInfo
  }
})

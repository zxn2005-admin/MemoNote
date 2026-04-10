import request from './request'

export const userApi = {
  getProfile() {
    return request.get('/user/profile')
  },

  updateProfile(data) {
    return request.put('/user/profile', data)
  },

  updatePassword(data) {
    return request.put('/user/password', data)
  },

  getStats() {
    return request.get('/user/stats')
  }
}

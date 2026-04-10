import request from './request'

export const adminApi = {
  getUserList(params) {
    return request.get('/admin/users', { params })
  },

  updateUserStatus(id, status) {
    return request.put(`/admin/users/${id}/status`, null, { params: { status } })
  },

  getStats() {
    return request.get('/admin/stats')
  }
}

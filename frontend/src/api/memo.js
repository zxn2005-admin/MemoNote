import request from './request'

export const memoApi = {
  getList(params) {
    return request.get('/memo/list', { params })
  },

  search(params) {
    return request.get('/memo/search', { params })
  },

  getById(id) {
    return request.get(`/memo/${id}`)
  },

  create(data) {
    return request.post('/memo', data)
  },

  update(id, data) {
    return request.put(`/memo/${id}`, data)
  },

  delete(id) {
    return request.delete(`/memo/${id}`)
  },

  updateStatus(id, status) {
    return request.put(`/memo/${id}/status`, null, { params: { status } })
  },

  getStats() {
    return request.get('/memo/stats')
  }
}

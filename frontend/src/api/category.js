import request from './request'

export const categoryApi = {
  getList() {
    return request.get('/category/list')
  },

  create(data) {
    return request.post('/category', data)
  },

  update(id, data) {
    return request.put(`/category/${id}`, data)
  },

  delete(id) {
    return request.delete(`/category/${id}`)
  }
}

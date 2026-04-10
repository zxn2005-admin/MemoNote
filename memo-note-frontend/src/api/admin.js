import request from './request'

export const getUserList = () => {
  return request.get('/admin/users')
}

export const updateUserStatus = (id, status) => {
  return request.put(`/admin/users/${id}/status?status=${status}`)
}

export const deleteUser = (id) => {
  return request.delete(`/admin/users/${id}`)
}

export const getAdminStatistics = () => {
  return request.get('/admin/statistics')
}

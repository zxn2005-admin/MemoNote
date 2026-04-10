import request from './request'

export const getMemoList = () => {
  return request.get('/memo/list')
}

export const getMemoPage = (params) => {
  return request.get('/memo/page', { params })
}

export const getMemoDetail = (id) => {
  return request.get(`/memo/${id}`)
}

export const addMemo = (data) => {
  return request.post('/memo', data)
}

export const updateMemo = (id, data) => {
  return request.put(`/memo/${id}`, data)
}

export const deleteMemo = (id) => {
  return request.delete(`/memo/${id}`)
}

export const toggleMemoStatus = (id) => {
  return request.put(`/memo/${id}/toggle`)
}

export const getStatistics = () => {
  return request.get('/memo/statistics')
}

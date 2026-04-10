import request from './request'

export const getProfile = () => {
  return request.get('/user/profile')
}

export const updateProfile = (data) => {
  return request.put('/user/profile', data)
}

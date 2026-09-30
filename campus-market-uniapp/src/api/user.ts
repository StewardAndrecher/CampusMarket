import { request } from '@/utils/request'

export interface LoginResponse {
  token: string
  userId: number
  username: string
  nickname: string
}

// 注册
export function register(data: { username: string; password: string; nickname: string }) {
  return request<LoginResponse>({ url: '/user/register', method: 'POST', data })
}

// 登录
export function login(data: { username: string; password: string }) {
  return request<LoginResponse>({ url: '/user/login', method: 'POST', data })
}

const BASE_URL = 'http://localhost:8080/api'

interface ApiResult<T> {
  code: number
  message: string
  data: T
}

export function request<T>(options: {
  url: string
  method?: 'GET' | 'POST' | 'PUT' | 'DELETE'
  data?: Record<string, any>
}): Promise<T> {
  return new Promise((resolve, reject) => {
    uni.request({
      url: BASE_URL + options.url,
      method: options.method || 'GET',
      data: options.data,
      header: {
        'Content-Type': 'application/json',
        ...(uni.getStorageSync('campus_token')
          ? { Authorization: `Bearer ${uni.getStorageSync('campus_token')}` }
          : {}),
      },
      success: (res) => {
        const result = res.data as ApiResult<T>
        // 关键：判断业务 code，而不是 HTTP 状态码
        if (result && result.code === 200) {
          resolve(result.data)
        } else {
          uni.showToast({ title: result?.message || '请求失败', icon: 'none' })
          reject(new Error(result?.message || '请求失败'))
        }
      },
      fail: () => {
        uni.showToast({ title: '网络连接失败，请确认后端已启动', icon: 'none' })
        reject(new Error('网络连接失败'))
      },
    })
  })
}
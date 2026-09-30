const TOKEN_KEY = 'campus_token'
const USER_KEY = 'campus_user'

export function getToken(): string {
  return uni.getStorageSync(TOKEN_KEY) || ''
}

export function setLogin(token: string, user: Record<string, any>) {
  uni.setStorageSync(TOKEN_KEY, token)
  uni.setStorageSync(USER_KEY, user)
}

export function getUser(): Record<string, any> | null {
  return uni.getStorageSync(USER_KEY) || null
}

export function isLoggedIn(): boolean {
  return !!getToken()
}

export function clearLogin() {
  uni.removeStorageSync(TOKEN_KEY)
  uni.removeStorageSync(USER_KEY)
}
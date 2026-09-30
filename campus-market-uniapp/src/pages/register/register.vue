<template>
  <view class="page">
    <view class="title">注册新账号</view>

    <view class="form">
      <input class="input" v-model="username" placeholder="用户名（3-32位）" />
      <input class="input" v-model="password" password placeholder="密码（3-32位）" />
      <input class="input" v-model="nickname" placeholder="昵称" />
      <button class="btn" :loading="loading" @click="handleRegister">注 册</button>
      <view class="link" @click="goLogin">已有账号？去登录</view>
    </view>
  </view>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { register } from '@/api/user'
import { setLogin } from '@/utils/auth'

const username = ref('')
const password = ref('')
const nickname = ref('')
const loading = ref(false)

async function handleRegister() {
  if (!username.value || !password.value || !nickname.value) {
    uni.showToast({ title: '请填写完整信息', icon: 'none' })
    return
  }
  loading.value = true
  try {
    // 注册成功后端会直接返回 token（相当于自动登录）
    const res = await register({
      username: username.value,
      password: password.value,
      nickname: nickname.value,
    })
    setLogin(res.token, { userId: res.userId, username: res.username, nickname: res.nickname })
    uni.showToast({ title: '注册成功', icon: 'success' })
    setTimeout(() => uni.switchTab({ url: '/pages/user/user' }), 600)
  } catch (e) {
    // 错误提示已在 request.ts 里统一弹出
  } finally {
    loading.value = false
  }
}

function goLogin() {
  uni.navigateBack()
}
</script>

<style>
.page {
  min-height: 100vh;
  background: #f5f6fa;
  display: flex;
  flex-direction: column;
  align-items: center;
  padding-top: 120rpx;
}
.title { font-size: 44rpx; font-weight: 700; color: #3b82f6; margin-bottom: 60rpx; }
.form { width: 80%; }
.input {
  background: #fff;
  border-radius: 12rpx;
  padding: 24rpx;
  margin-bottom: 24rpx;
  font-size: 30rpx;
}
.btn {
  background: #3b82f6;
  color: #fff;
  border-radius: 12rpx;
  font-size: 32rpx;
  margin-top: 10rpx;
}
.link {
  text-align: center;
  color: #3b82f6;
  font-size: 28rpx;
  margin-top: 30rpx;
}
</style>
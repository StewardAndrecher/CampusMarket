<template>
  <view class="page">
    <view class="title">CampusMarket</view>
    <view class="subtitle">校园二手交易平台</view>

    <view class="form">
      <input class="input" v-model="username" placeholder="请输入用户名" />
      <input class="input" v-model="password" password placeholder="请输入密码" />
      <button class="btn" :loading="loading" @click="handleLogin">登 录</button>
      <view class="link" @click="goRegister">没有账号？去注册</view>
    </view>
  </view>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { login } from '@/api/user'
import { setLogin } from '@/utils/auth'

const username = ref('')
const password = ref('')
const loading = ref(false)

async function handleLogin() {
  if (!username.value || !password.value) {
    uni.showToast({ title: '请输入用户名和密码', icon: 'none' })
    return
  }
  loading.value = true
  try {
    const res = await login({ username: username.value, password: password.value })
    // 保存 token 和用户信息
    setLogin(res.token, { userId: res.userId, username: res.username, nickname: res.nickname })
    uni.showToast({ title: '登录成功', icon: 'success' })
    // 回到"我的"tab 页（tab 页必须用 switchTab）
    setTimeout(() => uni.switchTab({ url: '/pages/user/user' }), 600)
  } catch (e) {
    // 错误提示已在 request.ts 里统一弹出
  } finally {
    loading.value = false
  }
}

function goRegister() {
  uni.navigateTo({ url: '/pages/register/register' })
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
.title { font-size: 52rpx; font-weight: 700; color: #3b82f6; }
.subtitle { font-size: 26rpx; color: #999; margin-top: 10rpx; margin-bottom: 60rpx; }
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
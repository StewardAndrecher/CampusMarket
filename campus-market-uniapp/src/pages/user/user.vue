<template>
  <view class="page">
    <!-- 已登录：显示用户信息 -->
    <view v-if="user" class="card">
      <view class="avatar">{{ user.nickname?.charAt(0) }}</view>
      <view class="info">
        <view class="nickname">{{ user.nickname }}</view>
        <view class="username">@{{ user.username }}</view>
      </view>
    </view>

    <!-- 未登录：引导去登录 -->
    <view v-else class="card">
      <view class="avatar gray">?</view>
      <view class="info">
        <view class="nickname">未登录</view>
        <view class="username">登录后可发布商品、收藏、聊天</view>
      </view>
    </view>

    <button v-if="!user" class="btn" @click="goLogin">去登录 / 注册</button>
    <button v-else class="btn logout" @click="handleLogout">退出登录</button>
  </view>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { onShow } from '@dcloudio/uni-app'
import { getUser, clearLogin } from '@/utils/auth'

const user = ref(getUser())

// tab 页常驻内存，setup 只执行一次；每次切回本页都会触发 onShow → 重新读取登录状态
onShow(() => {
  user.value = getUser()
})

function goLogin() {
  uni.navigateTo({ url: '/pages/login/login' })
}

function handleLogout() {
  clearLogin()
  user.value = null
  uni.showToast({ title: '已退出登录', icon: 'none' })
}
</script>

<style>
.page {
  min-height: 100vh;
  background: #f5f6fa;
  padding: 30rpx;
}
.card {
  background: #fff;
  border-radius: 16rpx;
  padding: 40rpx 30rpx;
  display: flex;
  align-items: center;
  margin-bottom: 40rpx;
}
.avatar {
  width: 100rpx;
  height: 100rpx;
  border-radius: 50%;
  background: #3b82f6;
  color: #fff;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 40rpx;
  margin-right: 30rpx;
}
.avatar.gray { background: #ccc; }
.nickname { font-size: 34rpx; font-weight: 600; }
.username { font-size: 26rpx; color: #999; margin-top: 8rpx; }
.btn {
  background: #3b82f6;
  color: #fff;
  border-radius: 12rpx;
  font-size: 32rpx;
}
.btn.logout { background: #ff4d4f; }
</style>
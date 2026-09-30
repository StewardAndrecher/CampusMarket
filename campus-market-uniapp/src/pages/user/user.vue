<template>
  <view class="page">
    <view v-if="user" class="card">
      <view class="avatar">{{ user.nickname?.charAt(0) }}</view>
      <view class="info">
        <view class="nickname">{{ user.nickname }}</view>
        <view class="username">@{{ user.username }}</view>
      </view>
    </view>
    <view v-else class="card">
      <view class="avatar gray">?</view>
      <view class="info">
        <view class="nickname">未登录</view>
        <view class="username">登录后可发布商品、收藏、聊天</view>
      </view>
    </view>

    <view v-if="user" class="menu">
      <view class="menu-item" @click="go('/pages/order/list')">
        <text class="mi-icon">📦</text><text>我的订单</text><text class="arrow">›</text>
      </view>
      <view class="menu-item" @click="go('/pages/favorite/list')">
        <text class="mi-icon">⭐</text><text>我的收藏</text><text class="arrow">›</text>
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

onShow(() => { user.value = getUser() })

function goLogin() { uni.navigateTo({ url: '/pages/login/login' }) }
function go(url: string) { uni.navigateTo({ url }) }
function handleLogout() {
  clearLogin()
  user.value = null
  uni.showToast({ title: '已退出登录', icon: 'none' })
}
</script>

<style>
.page { min-height: 100vh; background: #f5f6fa; padding: 30rpx; }
.card { background: #fff; border-radius: 16rpx; padding: 40rpx 30rpx; display: flex; align-items: center; margin-bottom: 30rpx; }
.avatar { width: 100rpx; height: 100rpx; border-radius: 50%; background: #3b82f6; color: #fff; display: flex; align-items: center; justify-content: center; font-size: 40rpx; margin-right: 30rpx; }
.avatar.gray { background: #ccc; }
.nickname { font-size: 34rpx; font-weight: 600; }
.username { font-size: 26rpx; color: #999; margin-top: 8rpx; }
.menu { background: #fff; border-radius: 16rpx; margin-bottom: 30rpx; overflow: hidden; }
.menu-item { display: flex; align-items: center; padding: 30rpx; border-bottom: 1rpx solid #f0f1f3; font-size: 30rpx; }
.mi-icon { margin-right: 20rpx; font-size: 36rpx; }
.arrow { margin-left: auto; color: #ccc; font-size: 36rpx; }
.btn { background: #3b82f6; color: #fff; border-radius: 12rpx; font-size: 32rpx; }
.btn.logout { background: #ff4d4f; }
</style>

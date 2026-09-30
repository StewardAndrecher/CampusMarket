<template>
  <view class="page">
    <view v-if="list.length === 0" class="empty">还没有收藏，去首页逛逛吧</view>
    <view v-for="p in list" :key="p.id" class="card" @click="goDetail(p.id)">
      <image class="cover" :src="p.coverUrl || '/static/logo.png'" mode="aspectFill" />
      <view class="info">
        <view class="title">{{ p.title }}</view>
        <view class="price">¥{{ p.price }}</view>
      </view>
    </view>
  </view>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { onShow } from '@dcloudio/uni-app'
import { myFavorites } from '@/api/favorite'

const list = ref<any[]>([])

onShow(async () => {
  const data = await myFavorites()
  list.value = data.records || []
})

function goDetail(id: number) {
  uni.navigateTo({ url: `/pages/product/detail?id=${id}` })
}
</script>

<style>
.page { background: #f5f6fa; min-height: 100vh; }
.empty { text-align: center; color: #999; margin-top: 200rpx; }
.card {
  background: #fff; margin: 20rpx; padding: 20rpx;
  border-radius: 16rpx; display: flex;
}
.cover { width: 140rpx; height: 140rpx; border-radius: 12rpx; }
.info { margin-left: 20rpx; flex: 1; }
.title { font-size: 28rpx; }
.price { color: #ff4d4f; font-weight: 700; margin-top: 10rpx; }
</style>

<template>
  <view class="page">
    <view class="tabs">
      <view :class="['tab', tab === 'buy' && 'active']" @click="tab = 'buy'">我买到的</view>
      <view :class="['tab', tab === 'sell' && 'active']" @click="tab = 'sell'">我卖出的</view>
    </view>

    <view v-if="orders.length === 0" class="empty">暂无订单</view>

    <view v-for="o in orders" :key="o.id" class="card">
      <image class="cover" :src="o.productCover || '/static/logo.png'" mode="aspectFill" />
      <view class="info">
        <view class="title">{{ o.productTitle }}</view>
        <view class="price">¥{{ o.price }}</view>
        <view class="status">{{ o.statusText }}</view>
      </view>
    </view>
  </view>
</template>

<script setup lang="ts">
import { ref, watch } from 'vue'
import { onShow } from '@dcloudio/uni-app'
import { myBuyOrders, mySellOrders, OrderVO } from '@/api/order'

const tab = ref<'buy' | 'sell'>('buy')
const orders = ref<OrderVO[]>([])

onShow(load)
watch(tab, load)

async function load() {
  const data = tab.value === 'buy' ? await myBuyOrders() : await mySellOrders()
  orders.value = data.records || []
}
</script>

<style>
.page { background: #f5f6fa; min-height: 100vh; }
.tabs { display: flex; background: #fff; }
.tab { flex: 1; text-align: center; padding: 24rpx; font-size: 28rpx; color: #666; }
.tab.active { color: #ff4d4f; border-bottom: 4rpx solid #ff4d4f; font-weight: 600; }
.empty { text-align: center; color: #999; margin-top: 200rpx; }
.card {
  background: #fff; margin: 20rpx; padding: 20rpx;
  border-radius: 16rpx; display: flex;
}
.cover { width: 140rpx; height: 140rpx; border-radius: 12rpx; }
.info { margin-left: 20rpx; flex: 1; }
.title { font-size: 28rpx; }
.price { color: #ff4d4f; font-weight: 700; margin-top: 10rpx; }
.status { color: #3b82f6; font-size: 24rpx; margin-top: 10rpx; }
</style>
